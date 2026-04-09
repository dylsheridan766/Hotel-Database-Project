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
    
    //whats being passed from html
    private Properties prop = new Properties();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

    
        String action = request.getParameter("action");
        if (action == null) action = "list";

    
        try (InputStream input = getServletContext().getResourceAsStream("/WEB-INF/classes/db.properties")) {
            if (input == null) {
                out.println("Unable to find db.properties");
                return;
            }
            prop.load(input);
            Class.forName("org.postgresql.Driver");

            try (Connection conn = DriverManager.getConnection(
                    prop.getProperty("db.url"), 
                    prop.getProperty("db.user"), 
                    prop.getProperty("db.password"))) {
                
                // handle the diffrent actions
                switch (action) {
                    case "list":
                        listHotels(conn, out);
                        break;
                    case "search":
                        searchRooms(request, conn, out);
                        break;
                    default:
                        out.println("Unknown action: " + action);
                }

            }
        } catch (Exception e) {
            out.println("<h3>Error: " + e.getMessage() + "</h3>");
        }
    }

    
    private void listHotels(Connection conn, PrintWriter out) throws SQLException {
        String sql = "SELECT * FROM Hotels";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            out.println("<html><body><h1>Hotel List</h1><table border='1'>");
            out.println("<tr><th>ID</th><th>Categorie</th><th>Adresse</th></tr>");

            while (rs.next()) {
                out.println("<tr>");
                out.println("<td>" + rs.getInt("hotel_id") + "</td>");
                out.println("<td>" + rs.getString("categorie") + "</td>");
                out.println("<td>" + rs.getString("Adresse") + "</td>");
                out.println("</tr>");
            }
            out.println("</table></body></html>");
        }
    }
    private void searchRooms(HttpServletRequest request, Connection conn, PrintWriter out) throws SQLException {
    //check the checkboxes
    boolean filterDate = request.getParameter("useDate") != null;
    boolean filterCap = request.getParameter("useCap") != null;
    //check the input box
    String DateVal = request.getParameter("dateVal");
    String capVal = request.getParameter("capVal");

    //string builder to made the sql query
    StringBuilder sql = new StringBuilder("Select * From Chambres Where 1=1");

    //add the other perameters if the box was checked
    if(filterDate){
        sql.append("And chambre_id Not In (Select chambre_id from Reservations_et_locations Where start_date < ? And end_date >?)");
    }
    
    
    }
}