package com.hoteldatabase.hoteldatabase;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@WebServlet("/testdb")
public class Test extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String url = "jdbc:postgresql://localhost:5432/postgres?currentSchema=databaseProject"; 
        String user = "postgres";
        String password = "";

        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(url, user, password);
            response.getWriter().println("Success! Connected to PostgreSQL.");
            conn.close();
        } catch (Exception e) {
            response.getWriter().println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
