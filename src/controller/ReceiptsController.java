package controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.org.apache.regexp.internal.RE;
import model.Receipt;

import javax.persistence.Id;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet("/receipts")
public class ReceiptsController extends AbstractServlet{
    private static final ObjectMapper mapper = new ObjectMapper(){{
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }};

    @Override
    protected ResponseEntity<?> processGet(HttpServletRequest request, HttpServletResponse response) {
        ResponseEntity<List<Receipt>> rs = new ResponseEntity<>();
        List<Receipt> list = null;
        try {
            list = db.getAllReceipts();
        } catch (SQLException e) {
            e.printStackTrace();
            rs.setError(e.getMessage());
        }



        if (!list.isEmpty()){
            for(Map.Entry<String, String[]> entry: request.getParameterMap().entrySet()) {
                switch (entry.getKey()) {
                    case "id":
                        int id = Integer.parseInt(entry.getValue()[0]);
                        list = list.stream().filter((receipt -> receipt.getId()==id)).collect(Collectors.toList());
                        break;

                    case "user_id":
                        int user_id = Integer.parseInt(entry.getValue()[0]);
                        list = list.stream().filter((receipt ->  receipt.getUserId()==user_id)).collect(Collectors.toList());
                        break;

                }
            }
        }

        rs.setData(list);
        return rs;
    }

    @Override
    protected ResponseEntity<?> processPost(HttpServletRequest request, HttpServletResponse response) {
        ResponseEntity<Integer> rs = new ResponseEntity<>();
        try {
            String requestData = requestDataToString(request);
            Receipt receipt = mapper.readValue(requestData, Receipt.class);

            db.insertReceipt(receipt);
            rs.setData(receipt.getId());
        } catch (Exception e) {
            e.printStackTrace();
            rs.setError(e.getMessage());
        }
        return rs;
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String json = requestDataToString(req);
//        IdHolder holder = mapper.readValue(json, IdHolder.class);
//        System.out.println(holder.getReceiptId()+"tests");
//        try {
//            db.bindUserWithReceipt(holder.getUserId(), holder.getReceiptId());
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

    }
}
//
//class IdHolder{
//    private int userId;
//    private int receiptId;
//
//    public int getUserId() {
//        return userId;
//    }
//
//    public void setUserId(int userId) {
//        this.userId = userId;
//    }
//
//    public int getReceiptId() {
//        return receiptId;
//    }
//
//    public void setReceiptId(int receiptId) {
//        this.receiptId = receiptId;
//    }
//}