
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
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    response.setContentType("text/html; charset=UTF-8");
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
                    case "editRecord":
                        EditForm(request, conn, out);
                        break;
                    case "updateRecord":
                        updateRecord(request, conn, out);
                        break;
                    case "roombyzone":
                        roombyzone(conn, out);
                        break;
                    case "capbyhotel":
                        capbyhotel(conn, out);
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
        String edate = request.getParameter("endate");
        String empID = request.getParameter("empID");

        if (rID == null || cEmail == null || edate == null || edate.isEmpty()) {
        out.println("<h3 style='color:red;'>Erreur : Données manquantes (ID Chambre, Email, ou Date).</h3>");
        out.println("<br><a href='index.jsp'><button type='button'>Retourner à l'accueil</button></a>"); 
           return; 
    }

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
        out.println("<br><a href='index.jsp'><button type='button'>Retourner à l'accueil</button></a>");
} catch (NumberFormatException e) {
    out.println("<h3>Erreur : Le format du NAS ou de l'ID est invalide.</h3>");
} catch (Exception e) {
    out.println("<h3>Error dans la Location: " + e.getMessage() + "</h3>");
}
     }
    private void viewTableGrid(HttpServletRequest request, Connection conn, PrintWriter out) throws SQLException {
    String tableName = request.getParameter("tableName");

    if (tableName == null || (!tableName.equals("Hotels") && !tableName.equals("Chambres") && 
        !tableName.equals("Employes") && !tableName.equals("Clients"))) {
        out.println("<html><body><h3 style='color:red;'>Table invalide.</h3></body></html>");
        return;
    }

    String tableCol = "";
    if (tableName.equals("Hotels")) {
            tableCol = "Hotel_ID";}
        else if (tableName.equals("Chambres")){
             tableCol = "Chambre_ID";}
        else if (tableName.equals("Employes")){
             tableCol = "Employe_ID";}
        else if (tableName.equals("Clients")){
             tableCol = "client_id";}
    String sql = "SELECT * FROM " + tableName + " ORDER BY " + tableCol + " ASC";

    out.println("<html><head><meta charset='UTF-8'>");
    out.println("<style>table { border-collapse: collapse; width: 100%; font-family: sans-serif; } " +
                "th, td { padding: 12px; text-align: left; border-bottom: 1px solid #ddd; } " +
                "tr:hover {background-color: #f9f9f9;} th { background-color: #f2f2f2; }</style>");
    out.println("</head><body>");
    out.println("<h2>Gestion : " + tableName + "</h2>");
    out.println("<a href='testdb?action=showAddForm&tableName=" + tableName + "'><button>+ Ajouter un(e) " + tableName + "</button></a><br><br>");

    try (PreparedStatement pstmt = conn.prepareStatement(sql);
         ResultSet rs = pstmt.executeQuery()) {
        
        out.println("<table>");

        if (tableName.equals("Hotels")) {
            out.println("<tr><th>ID</th><th>Catégorie</th><th>Adresse</th><th>ID Manager</th><th>Emails</th><th>Téléphone</th><th>Actions</th></tr>");
            while (rs.next()) {
                int id = rs.getInt("Hotel_ID");
                out.println("<tr><td>" + id + "</td><td>" + rs.getInt("categorie") + " ★</td><td>" + rs.getString("adresse") + "</td>");
                out.println("<td>" + rs.getInt("Manager_id") + "</td><td>" + rs.getString("Emails") + "</td><td>" + rs.getString("Telephone") + "</td>");
                out.println("<td>" + getActionLinks(tableName, id) + "</td></tr>");
            }
        } 
        else if (tableName.equals("Chambres")) {
            out.println("<tr><th>ID</th><th>Hôtel</th><th>No.</th><th>Prix</th><th>Capacité</th><th>Commodités</th><th>Vue</th><th>Lit Extra</th><th>Superficie</th><th>État</th><th>Actions</th></tr>");
            while (rs.next()) {
                int id = rs.getInt("Chambre_ID");
                out.println("<tr><td>" + id + "</td><td>" + rs.getInt("Hotel_ID") + "</td><td>" + rs.getInt("Room_number") + "</td>");
                out.println("<td>" + rs.getInt("Prix") + " $</td><td>" + rs.getInt("Capacite") + " pers.</td><td>" + rs.getString("Commodites") + "</td>");
                out.println("<td>" + rs.getString("Vue") + "</td><td>" + (rs.getBoolean("Lit_Extra") ? "Oui" : "Non") + "</td>");
                out.println("<td>" + rs.getInt("Superficie") + " m²</td><td>" + rs.getString("Etat") + "</td>");
                out.println("<td>" + getActionLinks(tableName, id) + "</td></tr>");
            }
        }
        else if (tableName.equals("Employes")) {
            out.println("<tr><th>ID</th><th>Nom Complet</th><th>Adresse</th><th>Rôle</th><th>ID Hôtel</th><th>Actions</th></tr>");
            while (rs.next()) {
                int id = rs.getInt("Employe_ID");
                out.println("<tr><td>" + id + "</td><td>" + rs.getString("Nom_complet") + "</td><td>" + rs.getString("Addresse") + "</td>");
                out.println("<td>" + rs.getString("Role") + "</td><td>" + rs.getInt("Hotel_ID") + "</td>");
                out.println("<td>" + getActionLinks(tableName, id) + "</td></tr>");
            }
        }
        else if (tableName.equals("Clients")) {
            out.println("<tr><th>ID</th><th>Nom</th><th>Email</th><th>Adresse</th><th>Actions</th></tr>");
            while (rs.next()) {
                int id = rs.getInt("client_id");
                out.println("<tr><td>" + id + "</td><td>" + rs.getString("Nom") + "</td><td>" + rs.getString("client_Email") + "</td>");
                out.println("<td>" + rs.getString("Addresse") + "</td>");
                out.println("<td>" + getActionLinks(tableName, id) + "</td></tr>");
            }
        }
        out.println("</table><br>");
        out.println("<a href='index.jsp'><button type='button'>Retour à l'accueil</button></a>");
        out.println("</body></html>");
    }
}


private String getActionLinks(String tableName, int id) {
    return "<a href='testdb?action=editRecord&tableName=" + tableName + "&id=" + id + "'>Modifier</a> | " +
           "<a href='testdb?action=deleteRecord&tableName=" + tableName + "&id=" + id + "' style='color:red;' onclick='return confirm(\"Voulez-vous vraiment supprimer cet élément ?\")'>Supprimer</a>";
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

private void EditForm(HttpServletRequest request, Connection conn, PrintWriter out) throws SQLException {
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
    String sql = "SELECT * FROM " + tableName + " WHERE " + tableCol + " = ?";
    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setInt(1, Integer.parseInt(ID));
        //prints relevent info so it can be eddited
        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                out.println("<html><head><meta charset='UTF-8'></head><body>");
                out.println("<h2>Modifier l'enregistrement (ID: " + ID + ")</h2>");
                out.println("<form action='testdb' method='GET' style='line-height: 2;'>");
                out.println("<input type='hidden' name='action' value='updateRecord'>");
                out.println("<input type='hidden' name='tableName' value='" + tableName + "'>");
                out.println("<input type='hidden' name='id' value='" + ID + "'>");

                if (tableName.equals("Hotels")) {
                    out.println("Catégorie : <input type='number' name='cat' value='" + rs.getInt("categorie") + "'> étoiles<br>");
                    out.println("Adresse : <input type='text' name='addr' value='" + rs.getString("adresse") + "'><br>");
                    out.println("ID Manager : <input type='number' name='manID' value='" + rs.getInt("Manager_id") + "'><br>");
                    out.println("Emails : <input type='text' name='emails' value='" + rs.getString("Emails") + "'><br>");
                    out.println("Téléphone : <input type='text' name='phone' value='" + rs.getString("Telephone") + "'><br>");
                } 
                else if (tableName.equals("Chambres")) {
                    out.println("Numéro de chambre : <input type='number' name='rNum' value='" + rs.getInt("Room_number") + "'><br>");
                    out.println("Prix : <input type='number' name='prix' value='" + rs.getInt("Prix") + "'> $<br>");
                    out.println("Capacité : <input type='number' name='cap' value='" + rs.getInt("Capacite") + "'> personnes<br>");
                    out.println("Commodités : <input type='text' name='com' value='" + rs.getString("Commodites") + "'><br>");
                    out.println("Vue : <input type='text' name='vue' value='" + rs.getString("Vue") + "'><br>");
                    out.println("Lit Extra : <select name='extra'>");
                    out.println("<option value='true' " + (rs.getBoolean("Lit_Extra") ? "selected" : "") + ">Oui</option>");
                    out.println("<option value='false' " + (!rs.getBoolean("Lit_Extra") ? "selected" : "") + ">Non</option>");
                    out.println("</select><br>");
                    out.println("Superficie : <input type='number' name='sup' value='" + rs.getInt("Superficie") + "'> m²<br>");
                    out.println("État : <input type='text' name='etat' value='" + rs.getString("Etat") + "'><br>");
                }
                else if (tableName.equals("Employes")) {
                    out.println("Nom complet : <input type='text' name='nom' value='" + rs.getString("Nom_complet") + "'><br>");
                    out.println("Adresse : <input type='text' name='addr' value='" + rs.getString("Addresse") + "'><br>");
                    out.println("Rôle : <input type='text' name='role' value='" + rs.getString("Role") + "'><br>");
                    out.println("ID Hôtel : <input type='number' name='hID' value='" + rs.getInt("Hotel_ID") + "'><br>");
                }
                else if (tableName.equals("Clients")) {
                    out.println("Nom complet : <input type='text' name='nom' value='" + rs.getString("Nom") + "'><br>");
                    out.println("Email : <input type='text' name='email' value='" + rs.getString("client_Email") + "'><br>");
                    out.println("Adresse : <input type='text' name='addr' value='" + rs.getString("Addresse") + "'><br>");
                }

                out.println("<br><button type='submit'>Sauvegarder les modifications</button>");
                out.println(" <a href='index.jsp'><button type='button'>Annuler</button></a>");
                out.println("</form></body></html>");
            }
        }
    }
}
    private void updateRecord(HttpServletRequest request, Connection conn, PrintWriter out) throws SQLException {
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

        StringBuilder sql = new StringBuilder("UPDATE " + tableName + " SET ");
        //add the appropreat attributes for the appropriat tabel
        if (tableName.equals("Hotels")) {
        sql.append("categorie=?, adresse=?, Manager_id=?, Emails=?, Telephone=?");
        } else if (tableName.equals("Chambres")) {
        sql.append("Room_number=?, Prix=?, Capacite=?, Commodites=?, Vue=?, Lit_Extra=?, Superficie=?, Etat=?");
        } else if (tableName.equals("Employes")) {
        sql.append("Nom_complet=?, Addresse=?, Role=?, Hotel_ID=?");
        } else if (tableName.equals("Clients")) {
        sql.append("Nom=?, client_Email=?, Addresse=?");
        }
        sql.append(" WHERE ").append(tableCol).append(" = ?");
        try (PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
        int i = 1;
        if (tableName.equals("Hotels")) {
            pstmt.setInt(i++, Integer.parseInt(request.getParameter("cat")));
            pstmt.setString(i++, request.getParameter("addr"));
            pstmt.setInt(i++, Integer.parseInt(request.getParameter("manID")));
            pstmt.setString(i++, request.getParameter("emails"));
            pstmt.setString(i++, request.getParameter("phone"));
        } else if (tableName.equals("Chambres")) {
            pstmt.setInt(i++, Integer.parseInt(request.getParameter("rNum")));
            pstmt.setInt(i++, Integer.parseInt(request.getParameter("prix")));
            pstmt.setInt(i++, Integer.parseInt(request.getParameter("cap")));
            pstmt.setString(i++, request.getParameter("com"));
            pstmt.setString(i++, request.getParameter("vue"));
            pstmt.setBoolean(i++, Boolean.parseBoolean(request.getParameter("extra")));
            pstmt.setInt(i++, Integer.parseInt(request.getParameter("sup")));
            pstmt.setString(i++, request.getParameter("etat"));
        } else if (tableName.equals("Employes")) {
            pstmt.setString(i++, request.getParameter("nom"));
            pstmt.setString(i++, request.getParameter("addr"));
            pstmt.setString(i++, request.getParameter("role"));
            pstmt.setInt(i++, Integer.parseInt(request.getParameter("hID")));
        } else if (tableName.equals("Clients")) {
            pstmt.setString(i++, request.getParameter("nom"));
            pstmt.setString(i++, request.getParameter("email"));
            pstmt.setString(i++, request.getParameter("addr"));
        }
        pstmt.setInt(i, Integer.parseInt(ID));

        int rows = pstmt.executeUpdate();
        if (rows > 0) {
            out.println("<html><body><h3>Mise à jour réussie!</h3>");
            out.println("<a href='index.jsp'>Retour à l'accueil</a></body></html>");
        }
    }catch (Exception e) {
        out.println("Error aves la mise a jour " + tableName + ": " + e.getMessage());
    }
}

private void roombyzone(Connection conn, PrintWriter out) throws SQLException {
        String sql = "SELECT * FROM num_chambre_par_zone";

        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            out.println("<html><body><h1>Nombre de Hotels par Zone</h1><table border='1'>");
            out.println("<tr><th>Zone</th><th>Nombre de Chambres</th></tr>");

            while (rs.next()) {
                out.println("<tr>");
                out.println("<td>" + rs.getString("Zone") + "</td>");
                out.println("<td>" + rs.getString("count") + "</td>");
                out.println("</tr>");
            }
            out.println("</table></body></html>");
        }
    }
    private void capbyhotel(Connection conn, PrintWriter out) throws SQLException {
        String sql = "SELECT * FROM tot_capacity";

        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            out.println("<html><body><h1>Capacité total Hotel</h1><table border='1'>");
            out.println("<tr><th>Hotel</th><th>Capacité</th></tr>");

            while (rs.next()) {
                out.println("<tr>");
                out.println("<td>" + rs.getString("Hotel_id") + "</td>");
                out.println("<td>" + rs.getString("count") + "</td>");
                out.println("</tr>");
            }
            out.println("</table></body></html>");
        }
    }

}
