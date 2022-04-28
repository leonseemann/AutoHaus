package de.autohaus.model;

import java.sql.*;

public class Connect {
    public static final String url = "jdbc:mariadb://localhost:3306/Autohaus";
    public static final String user = "Autohaus";
    public static final String pass = "vbewyfpigq";

    public static Connection connect(){
        try {
            return DriverManager.getConnection(url, user, pass);
        } catch (SQLException e) {
            return null;
        }
    }

    public static boolean tryConnect(){
        try {
            DriverManager.getConnection(url, user, pass);
            System.err.println("[SQL] Verbindung erfolgreich hergestellt");
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.err.println("[SQL] Verbindung nicht erfolgreich!");
            return false;
        }
    }


}
