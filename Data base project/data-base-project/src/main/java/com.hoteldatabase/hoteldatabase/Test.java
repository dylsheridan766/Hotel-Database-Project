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
    boolean filterSup = request.getParameter("useSup") != null;
    boolean filterChain = request.getParameter("useChain") != null;
    boolean filterCat = request.getParameter("useCat") != null;
    boolean filterPrice = request.getParameter("usePrice") != null;


    //check the input box
    String sDateVal = request.getParameter("sDateVal");
    String eDateVal = request.getParameter("eDateVal");
    String capVal = request.getParameter("capVal");
    String supVal = request.getParameter("superVal");
    String supChain = request.getParameter("chainVal");
    String supCat = request.getParameter("CatVal");
    String supPrice = request.getParameter("PriceVal");




    //string builder to made the sql query that joins chambres,hotels and hotel_chains
    StringBuilder sql = new StringBuilder("Select c.*, hc.Name"+ " From Chambres c " + " Join Hotels h On c.Hotel_ID = h.Hotel_ID " + " Join Hotel_chains hc On h.chain_ID = hc.chain_ID " +" Where 1=0");

    //add the other perameters if the box was checked
    if(filterCap){
        sql.append(" Or Capacite >=?");
    }

    if(filterDate){
        sql.append(" Or chambre_id Not In (Select chambre_id from Reservations_et_locations Where start_date < ? And end_date >?)");
    }

    if(filterSup){
        sql.append(" Or superficie >= ?");
    }

    if(filterChain){
        sql.append(" Or chain_id = ?");
    }

    if(filterCat){
        sql.append(" Or h.catagorie=?");
    }

    if(filterPrice){
        sql.append(" Or prix<= ?");
    }

    try (PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
        int i = 1;
        //replace the ? with the appropriate data
        if(filterCap){
            pstmt.setInt(i++, Integer.parseInt(capVal));       
         }

        if(filterDate){
            pstmt.setDate(i++, java.sql.Date.valueOf(eDateVal));
            pstmt.setDate(i++, java.sql.Date.valueOf(sDateVal));
        }
        
        if (filterSup) {
              pstmt.setInt(i++, Integer.parseInt(supVal));
        }

        if (filterChain) {
            pstmt.setInt(i++, Integer.parseInt(supChain));
        }

        if (filterCat) {
            pstmt.setInt(i++, Integer.parseInt(supCat));
        }
        
        if (filterPrice) {
             pstmt.setInt(i++, Integer.parseInt(supPrice));
        }
        try (ResultSet rs = pstmt.executeQuery()) {
            //make the table
            out.println("<html><body>");
            out.println("<h2>Available Hotels</h2>");
            out.println("<table border='1'><tr><th>Chambre_ID</th><th>Chaîne</th><th>Hotel_id</th><th>Numéro de chmabre</th><th>Prix</th><th>Commodites</th><th>Capacite</th><th>Vue</th><th>Lit Extra possible?</th><th>Superficie</th><th>État</th></tr>");
            
            //loop throught the database

            boolean found = false;
            while (rs.next()) {
                found = true;
                out.println("<tr>");

                out.println("<td>" + rs.getInt("Chambre_id") + "</td>");
                out.println("<td>" + rs.getString("Name") + "</td>");
                out.println("<td>" + rs.getInt("Hotel_ID") + "</td>");
                out.println("<td>" + rs.getInt("Room_number") + "</td>");
                out.println("<td>$" + rs.getInt("Prix") + "</td>");

                out.println("<td>" + rs.getString("Commodites") + "</td>");
                out.println("<td>" + rs.getInt("Capacite") + "</td>");
                out.println("<td>" + rs.getString("Vue") + "</td>");
                boolean extraLit = rs.getBoolean("Lit_Extra");
                out.println("<td>" + (extraLit ? "Yes" : "No") + "</td>");

                out.println("<td>" + rs.getInt("Superficie") + " m²</td>");
                out.println("<td>" + rs.getString("Etat") + "</td>");

            
                out.println("</tr>");
            }
            if (!found) {
                out.println("<tr><td colspan='11'>Aucun hôtel ne répond à ces exigences.</td></tr>");
            }
            out.println("</table></body></html>");
        }
    } catch (IllegalArgumentException e) {
        out.println("<p style='color:red;'>Error: veuillez entrer une date valid.</p>");
    }
    }
}
