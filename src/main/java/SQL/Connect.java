package SQL;

import java.sql.*;

public class Connect {
    public static final String url = "jdbc:mysql://192.168.0.2:3306/Autohaus";
    public static final String user = "autohaus";
    public static final String pass = "";

    public static Connection connect(){
        try {
            return DriverManager.getConnection(url, user, pass);
        } catch (SQLException e) {
            return null;
        }
    }

    public static void tryConnect(){
        try {
            DriverManager.getConnection(url, user, pass);
            System.err.println("[SQL] Verbindung erfolgreich hergestellt");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.err.println("[SQL] Verbindung nicht erfolgreich!");
        }
    }


}