import java.sql.*;

public class Connect {
    private static final String url = "jdbc:mysql://localhost:3306/autohaus";
    private static final String user = "root";
    private static final String pass = "";

    public static Connection connect(){
        try {
            Connection con = DriverManager.getConnection(url, user, pass);
            System.out.println("[SQL] Verbindung erfolgreich hergestellt");
            return con;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.err.println("[SQL] Verbindung nicht erfolgreich!");
            return null;
        }
    }
}
