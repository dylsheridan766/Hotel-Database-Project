package com.hoteldatabase.hoteldatabase;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
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
        out.println("unable to find db.properties");
        return;
    }
    // Load the properties file
    prop.load(input);

    // Get the values
    String url = prop.getProperty("db.url");
    String user = prop.getProperty("db.user");
    String pass = prop.getProperty("db.password");

    // Connect
    Class.forName("org.postgresql.Driver");
    Connection conn = DriverManager.getConnection(url, user, pass);
    out.println("<h1>Successuly connected using properties file.</h1>");
    conn.close();
} catch (Exception e) {
   out.println("<h3>Error:</h3>");
}
    }
}
