package com.hoteldatabase.hoteldatabase;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/testdb")
public class Test extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        Properties prop = new Properties();

        try (InputStream input = getServletContext().getResourceAsStream("/WEB-INF/classes/db.properties")) {
            if (input == null) {
                out.println("Unable to find db.properties");
                return;
            }
            prop.load(input);

            String url = prop.getProperty("db.url");
            String user = prop.getProperty("db.user");
            String pass = prop.getProperty("db.password");

            Class.forName("org.postgresql.Driver");

            String sql = "SELECT * FROM Hotels";

            try (Connection conn = DriverManager.getConnection(url, user, pass);
                 PreparedStatement pstmt = conn.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {

                out.println("<html><body>");
                out.println("<h1>Hotel List</h1>");
                out.println("<table border='1'><tr><th>ID</th><th>Name</th><th>Location</th></tr>");

                while (rs.next()) {
                    out.println("<tr>");
                    out.println("<td>" + rs.getInt("id") + "</td>");
                    out.println("<td>" + rs.getString("name") + "</td>");
                    out.println("<td>" + rs.getString("location") + "</td>");
                    out.println("</tr>");
                }
                out.println("</table>");
                out.println("</body></html>");
            }
        } catch (ClassNotFoundException | SQLException e) {
            out.println("<h3>Database Error: " + e.getMessage() + "</h3>");
        } catch (Exception e) {
            out.println("<h3>General Error: " + e.getMessage() + "</h3>");
        }
    }
}