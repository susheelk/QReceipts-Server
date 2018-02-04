package db;

import model.Receipt;
import model.Vendor;

import java.sql.SQLException;
import java.util.List;

public interface DatabaseAccessor {
    public int insertReceipt(Receipt receipt) throws SQLException;

    public List<Receipt> getAllReceipts() throws SQLException;

    public Vendor getVendorById(int id) throws SQLException;

    public void bindUserWithReceipt(int userId, int receiptId) throws SQLException;
}
