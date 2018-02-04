package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import db.DatabaseAccessor;
import db.DatabaseAccessorImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

public abstract class AbstractServlet extends HttpServlet {

    final ObjectMapper mapper = new ObjectMapper();
    public DatabaseAccessor db = new DatabaseAccessorImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String json = mapper.writeValueAsString(processGet(req, resp));
        System.out.println("getted");
        resp.getWriter().write(json);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        System.out.println("posted");
        String json = mapper.writeValueAsString(processPost(req, resp));
        resp.getWriter().write(json);

    }

    protected ResponseEntity<?> processPost(HttpServletRequest request, HttpServletResponse response) {
        return new ResponseEntity<>();
    }

    protected ResponseEntity<?> processGet(HttpServletRequest request, HttpServletResponse response) {
        return new ResponseEntity<>();
    }

    public String requestDataToString(HttpServletRequest request) throws IOException {
        String data = "";

        try (BufferedReader buffer = request.getReader()) {
            String temp = buffer.readLine();
            while (temp != null) {
                data += temp;
                temp = buffer.readLine();
            }
        }
        return data;
    }

}
