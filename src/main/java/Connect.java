import java.sql.*;

public class Connect {
    private static final String url = "jdbc:mysql://localhost:3306/autohaus";
    private static final String user = "root";
    private static final String pass = "";

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
