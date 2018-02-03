package controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.org.apache.regexp.internal.RE;
import model.Receipt;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/receipts")
public class ReceiptsController extends AbstractServlet{
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected ResponseEntity<?> processGet(HttpServletRequest request, HttpServletResponse response) {
        ResponseEntity<List<Receipt>> rs = new ResponseEntity<>();
        List<Receipt> list = new ArrayList<>();

        list.add(new Receipt(){{
            setId(1);
        }});

        rs.setData(list);

        return rs;
    }

    @Override
    protected ResponseEntity<?> processPost(HttpServletRequest request, HttpServletResponse response) {

        try {
            String requestData = requestDataToString(request);
            Receipt receipt = mapper.readValue(requestData, Receipt.class);
            System.out.println(receipt.getItems().size());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
