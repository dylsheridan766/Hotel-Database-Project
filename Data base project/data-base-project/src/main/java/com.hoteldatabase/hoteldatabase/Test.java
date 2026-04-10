
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
                        listChains(conn, out);
                        break;
                    case "search":
                        searchRooms(request, conn, out);
                        break;
                    case "append":
                        createClient(request, conn, out);
                        break;
                    case "resappend":
                        createReservation(request, conn, out);
                        break;
                    case "locappend":
                    createLocation(request, conn, out);
                    break;
                    case "viewTable":
                    viewTableGrid(request, conn, out);
                    break;
                    case "deleteRecord":
                    deleteTableRecord(request, conn, out);
                    break;
                    default:
                        out.println("Unknown action: " + action);
                }

            }
        } catch (Exception e) {
            out.println("<html><body>");
            out.println("<h3>Error: " + e.getMessage() + "</h3>");
            out.println("<br><form action='index.jsp'><button type='submit'>Retourner à la recherche</button></form>");
            out.println("</body></html>");
        }
    }


    private void listChains(Connection conn, PrintWriter out) throws SQLException {
        String sql = "SELECT * FROM Hotel_chains";

        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            out.println("<html><body><h1>Lists de Chanis de Hotel</h1><table border='1'>");
            out.println("<tr><th>Nom</th><th>Adress du sciege social</th><th>Nombre de Hotels</th><th>Email de contact</th><th>Numéro de téléphone</th></tr>");

            while (rs.next()) {
                out.println("<tr>");
                out.println("<td>" + rs.getString("Name") + "</td>");
                out.println("<td>" + rs.getString("adresse_du_siege_social") + "</td>");
                out.println("<td>" + rs.getString("nombre_hotels") + "</td>");
                out.println("<td>" + rs.getString("Emails") + "</td>");
                out.println("<td>" + rs.getString("Telephone") + "</td>");
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

        //make sure at least 1 box is checked
        boolean anyFilterSelected = filterDate || filterCap || filterSup || filterChain || filterCat || filterPrice;

        if (!anyFilterSelected) {
            out.println("<html><body>");
            out.println("<h3 style='color:red;'>Error: Vous devez selectioner au moins 1 critere</h3>");
            out.println("<a href='index.jsp'>Retournez</a>");
            out.println("</body></html>");
            return; // This stops the rest of the code from running
        }
        //check the input box
        String sDateVal = request.getParameter("sDateVal");
        String eDateVal = request.getParameter("eDateVal");
        String capVal = request.getParameter("capVal");
        String supVal = request.getParameter("superVal");
        String supChain = request.getParameter("chainVal");
        String supCat = request.getParameter("CatVal");
        String supPrice = request.getParameter("PriceVal");

        //string builder to made the sql query that joins chambres,hotels and hotel_chains
        StringBuilder sql = new StringBuilder("Select c.*, hc.Name, h.adresse " + " From Chambres c " + " Join Hotels h On c.Hotel_ID = h.Hotel_ID " + " Join Hotel_chains hc On h.chain_ID = hc.chain_ID " + " Where 1=1");

        //add the other perameters if the box was checked
        if (filterCap) {
            sql.append(" And Capacite >=?");
        }

        if (filterDate) {
            sql.append(" And chambre_id Not In (Select chambre_id from Reservations_et_locations Where start_date < ? And end_date >?)");
        }

        if (filterSup) {
            sql.append(" And superficie >= ?");
        }

        if (filterChain) {
            sql.append(" And chain_id = ?");
        }

        if (filterCat) {
            sql.append(" And h.categorie=?");
        }

        if (filterPrice) {
            sql.append(" And prix<= ?");
        }

        try (PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            int i = 1;
            //replace the ? with the appropriate data
            if (filterCap) {
                pstmt.setInt(i++, Integer.parseInt(capVal));
            }

            if (filterDate) {
                pstmt.setDate(i++, java.sql.Date.valueOf(eDateVal));
                pstmt.setDate(i++, java.sql.Date.valueOf(sDateVal));
            }

            if (filterSup) {
                pstmt.setInt(i++, Integer.parseInt(supVal));
            }

            if (filterChain) {
                pstmt.setString(i++, (supChain));
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
                out.println("<table border='1'><tr><th>Chambre_ID</th><th>Chaîne</th><th>Hotel Addresse</th><th>Numéro de chmabre</th><th>Prix</th><th>Commodites</th><th>Capacite</th><th>Vue</th><th>Lit Extra possible?</th><th>Superficie</th><th>État</th></tr>");

                //loop throught the database

                boolean found = false;
                while (rs.next()) {
                    found = true;
                    out.println("<tr>");

                    out.println("<td>" + rs.getInt("Chambre_id") + "</td>");
                    out.println("<td>" + rs.getString("Name") + "</td>");
                    out.println("<td>" + rs.getString("adresse") + "</td>");
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
                out.println("</table>");
                out.println("<br><form action='index.jsp'><button type='submit'>Retourner à la recherche</button></form>");
                out.println("</body></html>");


            }
        } catch (IllegalArgumentException e) {
            out.println("<p style='color:red;'>Error: veuillez entrer une date valid.</p>");
            out.println("<a href='index.jsp'>Retournez</a>");
        }
    }

    private void createClient(HttpServletRequest request, Connection conn, PrintWriter out) throws SQLException {
        //get the inputed data
        String cName = request.getParameter("name");
        String cAddress = request.getParameter("address");
        String cEmail = request.getParameter("email");
        String cNAS = request.getParameter("NAS");
//the initial query
        StringBuilder sql = new StringBuilder("Insert Into Clients (Nom,Addresse,client_Email,Nas) Values (?, ?, ?, ?)");
        try (PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            pstmt.setString(1, cName);
            pstmt.setString(2, cAddress);
            pstmt.setString(3, cEmail);
            pstmt.setInt(4, Integer.parseInt(cNAS));
            int rowsInserted = pstmt.executeUpdate();

            if (rowsInserted > 0) {
                out.println("<h3>Compte enregistré avec succès</h3>");
            }
        } catch (Exception e) {
            out.println("<h3>Error dans la créaction: " + e.getMessage() + "</h3>");
            out.println("<button onclick='history.back()'>Go Back</button>");

        }

    }

    private void createReservation(HttpServletRequest request, Connection conn, PrintWriter out) throws SQLException {
        String rID = request.getParameter("roomID");
        String cEmail = request.getParameter("cEM");
        String edate = request.getParameter("edate");
        String sdate = request.getParameter("sdate");

        StringBuilder sql = new StringBuilder(
                "INSERT INTO Reservations_et_locations " +
                        "(hotel_id, client_id, client_nas, chambre_id, Room_Number, type, Reservation_Date, Start_Date, End_Date) " +
                        "SELECT c.Hotel_ID, cl.client_id, cl.Nas, c.Chambre_ID, c.Room_number, 'Reservation', CURRENT_DATE, ?, ? " +
                        "FROM Chambres c, Clients cl " +
                        "WHERE c.Chambre_ID = ? AND cl.client_email = ? " +
                        "AND NOT EXISTS (" +
                        "    SELECT 1 FROM Reservations_et_locations r " +
                        "    WHERE r.chambre_id = c.Chambre_ID " +
                        "    AND r.Start_Date < ? " +
                        "    AND r.End_Date > ?" +
                        ")"
        );
        try (PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            pstmt.setDate(1, java.sql.Date.valueOf((sdate)));
            pstmt.setDate(2, java.sql.Date.valueOf((edate)));
            pstmt.setInt(3, Integer.parseInt((rID)));
            pstmt.setString(4, cEmail);
            pstmt.setDate(5, java.sql.Date.valueOf((edate)));
            pstmt.setDate(6, java.sql.Date.valueOf((sdate)));
            int rowsInserted = pstmt.executeUpdate();

            if (rowsInserted > 0) {
                out.println("<h3>Réservation enregistrée avec succès</h3>");
              
            } else {
                out.println("<h3>La réservation a échoué : La chambre est déjà réservée pour ces dates ou les infos sont invalides.</h3>");
            }

        } catch (Exception e) {
            out.println("<h3>Error dans la réservation: " + e.getMessage() + "</h3>");
            out.println("<button onclick='history.back()'>Go Back</button>");

        }
    }

    private void createLocation(HttpServletRequest request, Connection conn, PrintWriter out) throws SQLException {
        String rID = request.getParameter("rID");
        String cEmail = request.getParameter("cEM");
        String edate = request.getParameter("edate");
        String empID = request.getParameter("empID");

        StringBuilder sql = new StringBuilder(
                "INSERT INTO Reservations_et_locations " +
                        "(hotel_id, client_id, client_nas, chambre_id, Employe_ID, Room_Number, type, Reservation_Date, Start_Date, End_Date) " +
                        "SELECT c.Hotel_ID, cl.client_id, cl.Nas, c.Chambre_ID, ?, c.Room_number, 'Location', CURRENT_DATE, CURRENT_DATE, ? " +
                        "FROM Chambres c, Clients cl " +
                        "WHERE c.Chambre_ID = ? AND cl.client_email = ? " +
                        "AND NOT EXISTS (" +
                        "    SELECT 1 FROM Reservations_et_locations r " +
                        "    WHERE r.chambre_id = c.Chambre_ID " +
                        "    AND r.Start_Date < ? " +
                        "    AND r.End_Date > CURRENT_DATE" +
                        ")"
        );

        try (PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
    
    if (empID == null || empID.trim().isEmpty()) {
        pstmt.setNull(1, java.sql.Types.INTEGER);
    } else {
        pstmt.setInt(1, Integer.parseInt(empID));
    }

    pstmt.setDate(2, java.sql.Date.valueOf(edate));
    pstmt.setInt(3, Integer.parseInt(rID));
    pstmt.setString(4, cEmail);
    pstmt.setDate(5, java.sql.Date.valueOf(edate));

    int rowsInserted = pstmt.executeUpdate();

    if (rowsInserted > 0) { 
        out.println("<h3>Location enregistrée avec succès</h3>");
    } else {
        out.println("<h3>La location a échoué : La chambre est déjà réservée ou les infos sont invalides.</h3>");
    }
        out.println("<br><a href='testdb?action=viewTable&tableName=Chambres'><button>Retourner</button></a>");

} catch (NumberFormatException e) {
    out.println("<h3>Erreur : Le format du NAS ou de l'ID est invalide.</h3>");
} catch (Exception e) {
    out.println("<h3>Error dans la Location: " + e.getMessage() + "</h3>");
}
     }
     private void viewTableGrid(HttpServletRequest request, Connection conn, PrintWriter out) throws SQLException {
        String tableName = request.getParameter("tableName");
        //make sure a valid table was selected
        if (tableName == null || (!tableName.equals("Hotels") && !tableName.equals("Chambres") && 
            !tableName.equals("Employes") && !tableName.equals("Clients"))) {
            out.println("<html><body><h3 style='color:red;'>Table invalide sélectionnée.</h3><button onclick='history.back()'>Retourner</button></body></html>");
            return;
        }
        out.println("<html><body><h2>Data pour: " + tableName + "</h2>");

        out.println("<a href='testdb?action=showAddForm&tableName=" + tableName + "'><button>+ Add New " + tableName + "</button></a><br><br>");
        String sql = "Select * From " + tableName;
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            ResultSet rs = pstmt.executeQuery()) {
                out.println("<table border='1'>");
                if (tableName.equals("Hotels")) {
                out.println("<tr><th>Hotel ID</th><th>Category</th><th>Address</th><th>Actions</th></tr>");
                while (rs.next()) {
                    int id = rs.getInt("Hotel_ID");
                    out.println("<tr>");
                    out.println("<td>" + id + "</td>");
                    out.println("<td>" + rs.getString("categorie") + "</td>");
                    out.println("<td>" + rs.getString("adresse") + "</td>");
                    out.println("<td><a href='testdb?action=editRecord&tableName=Hotels&id=" + id + "'>Edit</a> | <a href='testdb?action=deleteRecord&tableName=Hotels&id=" + id + "' style='color:red;'>Delete</a></td>");
                    out.println("</tr>");
                }
            }
                else if (tableName.equals("Chambres")) {
                out.println("<tr><th>Chambre ID</th><th>Hotel ID</th><th>Room Num</th><th>Price</th><th>Actions</th></tr>");
                while (rs.next()) {
                    int id = rs.getInt("Chambre_ID");
                    out.println("<tr>");
                    out.println("<td>" + id + "</td>");
                    out.println("<td>" + rs.getInt("Hotel_ID") + "</td>");
                    out.println("<td>" + rs.getInt("Room_number") + "</td>");
                    out.println("<td>$" + rs.getInt("Prix") + "</td>");
                    out.println("<td><a href='testdb?action=editRecord&tableName=Chambres&id=" + id + "'>Edit</a> | <a href='testdb?action=deleteRecord&tableName=Chambres&id=" + id + "' style='color:red;'>Delete</a></td>");
                    out.println("</tr>");
                }
            }

            else if (tableName.equals("Employes")) {
                out.println("<tr><th>Emp ID</th><th>Name</th><th>Role</th><th>Actions</th></tr>");
                while (rs.next()) {
                    int id = rs.getInt("Employe_ID"); 
                    out.println("<tr>");
                    out.println("<td>" + id + "</td>");
                    out.println("<td>" + rs.getString("Nom_complet") + "</td>"); 
                    out.println("<td>" + rs.getString("Role") + "</td>");
                    out.println("<td><a href='testdb?action=editRecord&tableName=Employes&id=" + id + "'>Edit</a> | <a href='testdb?action=deleteRecord&tableName=Employes&id=" + id + "' style='color:red;'>Delete</a></td>");
                    out.println("</tr>");
                }
            }
            else if (tableName.equals("Clients")) {
                out.println("<tr><th>Client ID</th><th>Name</th><th>Email</th><th>Actions</th></tr>");
                while (rs.next()) {
                    int id = rs.getInt("client_id");
                    out.println("<tr>");
                    out.println("<td>" + id + "</td>");
                    out.println("<td>" + rs.getString("Nom") + "</td>");
                    out.println("<td>" + rs.getString("client_Email") + "</td>");
                    out.println("<td><a href='testdb?action=editRecord&tableName=Clients&id=" + id + "'>Edit</a> | <a href='testdb?action=deleteRecord&tableName=Clients&id=" + id + "' style='color:red;'>Delete</a></td>");
                    out.println("</tr>");
                }
            }
            out.println("</table>");
            out.println("<br><button type='button' onclick='history.back()'>Retourner</button>");
            out.println("</body></html>");
            }
            }

    private void deleteTableRecord(HttpServletRequest request, Connection conn, PrintWriter out) throws SQLException {
        String tableName = request.getParameter("tableName");
        String ID = request.getParameter("id");
        
        String tableCol="";
        //find the id of the selected row
        if (tableName.equals("Hotels")) {
            tableCol = "Hotel_ID";}
        else if (tableName.equals("Chambres")){
             tableCol = "Chambre_ID";}
        else if (tableName.equals("Employes")){
             tableCol = "Employe_ID";}
        else if (tableName.equals("Clients")){
             tableCol = "client_id";}

        String sql = "Delete From " + tableName + " Where " + tableCol + " = ? ";//use the id to find which dataset to delete 
        try (PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            pstmt.setInt(1, Integer.parseInt(ID));
            int rowsInserted = pstmt.executeUpdate();
             if (rowsInserted > 0) {
                out.println("<h3>Suppression réussie!</h3>");
               
            } else {
                out.println("<h3>La suppression a échoué.</h3>");
            }
        out.println("<a href='testdb?action=viewTable&tableName=" + tableName + "'><button>Return to Grid</button></a>");
        out.println("</body></html>");
        }catch (Exception e) {
            out.println("<h3>Error dans la Location: " + e.getMessage() + "</h3>");
            out.println("<button onclick='history.back()'>Go Back</button>");
    }
}

}
    

