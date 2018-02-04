package db;

import com.sun.org.apache.regexp.internal.RE;
import model.Item;
import model.Location;
import model.Receipt;
import model.Vendor;
import org.postgresql.jdbc3.Jdbc3PoolingDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DatabaseAccessorImpl implements DatabaseAccessor {

    private static final Jdbc3PoolingDataSource poolService = new Jdbc3PoolingDataSource() {{
        setServerName("localhost");
        setDatabaseName("qrreceipts");
        setUser("postgres");
        setPassword("root");
        setMaxConnections(10);
    }};

    public static final String insertReceipt = "INSERT INTO qrreceipts.model.receipts (id, vendor, subtotal, tip, tax, total) VALUES (?, ?, ?, ?, ?, ?)";
    public static final String insertVendor = "INSERT INTO qrreceipts.model.vendors (id, name, location_lat, location_lng, img) VALUES (?, ?, ?, ?, ?)";
    public static final String getReceipts = "SELECT * FROM qrreceipts.model.receipts";
    public static final String getVendorById = "SELECT * FROM qrreceipts.model.vendors WHERE id=?";
    public static final String insertItem= "INSERT INTO qrreceipts.model.items (id, name, total, img, quantity, receipt_id, spec_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
    public static final String getItemByReceipt = "SELECT * FROM qrreceipts.model.items WHERE receipt_id = ?";
    public static final String bindUserWithReceipt = "UPDATE qrreceipts.model.receipts SET user_id = ? WHERE id = ?";




    @Override
    public int insertReceipt(Receipt receipt) throws SQLException {

        Vendor vendor = receipt.getVendor();
        if(getVendorById(vendor.getId()) == null){
            insertVendor(vendor);
            System.out.println("vendor inserted");
        } else {
            System.out.println("vendor already exists");
        }

       try (Connection conn = poolService.getConnection();
            PreparedStatement statement = conn.prepareStatement(insertReceipt)) {

            receipt.setId((int)(System.currentTimeMillis()/1000-(10e7)));
            statement.setInt(1, receipt.getId());
            statement.setInt(2, vendor.getId());
            statement.setInt(3, receipt.getSubtotal());
            statement.setInt(4, receipt.getTip());
            statement.setInt(5, receipt.getTax());
            statement.setInt(6, receipt.getTotal());
            statement.execute();
           conn.close();
        } catch (SQLException e) {
            throw e;
        }

        for(Item item: receipt.getItems()){
            insertItem(item, receipt);
        }

        return receipt.getId();
    }

    public void insertItem(Item item, Receipt receipt) throws SQLException {
        try (Connection conn = poolService.getConnection();
             PreparedStatement statement = conn.prepareStatement(insertItem)) {
            statement.setInt(1, item.getId());
            statement.setString(2, item.getName());
            statement.setInt(3, item.getTotal());
            statement.setString(4, item.getImg());
            statement.setString(5, item.getQuantity());
            statement.setInt(6, receipt.getId());
            statement.setString(7, uniqueId());
            statement.execute();
            conn.close();
        } catch (SQLException e) {
            throw e;
        }
    }

    public List<Item> getItemsByReceipt(Receipt receipt) throws SQLException {
        List<Item> list = new ArrayList<>();
        try {
            Connection connection = poolService.getConnection();
            PreparedStatement statement = connection.prepareStatement(getItemByReceipt);
            statement.setInt(1, receipt.getId());
            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                Item item = new Item();
                item.setId(rs.getInt("id"));
                item.setName(rs.getString("name"));
                item.setTotal(rs.getInt("total"));
                item.setImg(rs.getString("img"));
                item.setQuantity(rs.getString("quantity"));
//                user.setName(rs.getString("name"));
                list.add(item);
                connection.close();
            }
        } catch (Exception e) {
            throw e;
        }
        return list;
    }


    public int insertVendor(Vendor vendor) throws SQLException {
        try (Connection conn = poolService.getConnection();
             PreparedStatement statement = conn.prepareStatement(insertVendor)) {
            statement.setInt(1, vendor.getId());
            statement.setString(2, vendor.getName());
            statement.setString(3, vendor.getLocation().getLat());
            statement.setString(4, vendor.getLocation().getLng());
            statement.setString(5, vendor.getImg());
            statement.execute();
            conn.close();
        } catch (SQLException e) {
            throw e;
        }
        return vendor.getId();
    }

    @Override
    public List<Receipt> getAllReceipts() throws SQLException {
        List<Receipt> list = new ArrayList<>();
        try {
            Connection connection = poolService.getConnection();
            PreparedStatement statement = connection.prepareStatement(getReceipts);
//            statement.setString(1, email);
//            statement.setString(2, password);
            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                Receipt receipt = new Receipt();
                receipt.setId(rs.getInt("id"));
                receipt.setSubtotal(rs.getInt("subtotal"));
                receipt.setTip(rs.getInt("tip"));
                receipt.setTax(rs.getInt("tax"));
                receipt.setTotal(rs.getInt("total"));
//                user.setName(rs.getString("name"));
                receipt.setVendor(getVendorById(rs.getInt("vendor")));
                receipt.setItems(getItemsByReceipt(receipt));
                receipt.setUserId(rs.getInt("user_id"));
                if(receipt.getUserId() == 0){
                    receipt.setUserId(-1);
                }
                list.add(receipt);
                connection.close();
            }
        } catch (Exception e) {
            throw e;
        }
        return list;
    }

    @Override
    public Vendor getVendorById(int id) throws SQLException {
        Vendor vendor = null;
        try {
            Connection connection = poolService.getConnection();
            PreparedStatement statement = connection.prepareStatement(getVendorById);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if(rs.next()) {
                vendor = new Vendor();
                vendor.setId(rs.getInt("id"));
                vendor.setName(rs.getString("name"));
                vendor.setLocation(new Location(rs.getString("location_lat"), rs.getString("location_lng")));
                vendor.setImg(rs.getString("img"));
            }
            connection.close();
        } catch (Exception e) {
            throw e;
        }
        return vendor;
    }

    @Override
    public void bindUserWithReceipt(int userId, int receiptId) throws SQLException {
        try (Connection conn = poolService.getConnection();
             PreparedStatement statement = conn.prepareStatement(bindUserWithReceipt)) {
            statement.setInt(1, userId);
            statement.setInt(2, receiptId);
            statement.execute();
            conn.close();
        } catch (SQLException e) {
            throw e;
        }
    }

    private String uniqueId(){
        return UUID.randomUUID().toString();
    }

}
