package controller;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

@WebServlet("/link")
public class LinkController extends AbstractServlet{
    @Override
    protected ResponseEntity<?> processPost(HttpServletRequest request, HttpServletResponse response) {

        ResponseEntity<Integer> rs = new ResponseEntity<>();
        try {
            String json = requestDataToString(request);
            IdHolder holder = mapper.readValue(json, IdHolder.class);
            System.out.println(holder.getReceiptId() + "tests");
            try {
                db.bindUserWithReceipt(holder.getUserId(), holder.getReceiptId());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            rs.setData(holder.getUserId());
            return rs;
        } catch (Exception e){
            rs.setError(e.getMessage());
            e.printStackTrace();
        }
        return rs;
    }
}


class IdHolder{
    private int userId;
    private int receiptId;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(int receiptId) {
        this.receiptId = receiptId;
    }
}