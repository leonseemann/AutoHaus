import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main extends Connect{
    public static void main(String[] args) throws SQLException {
        Statement stm = connect().createStatement();

        String execute = "SELECT * FROM auto";

        ResultSet rs = stm.executeQuery(execute);

        while(rs.next()){
            System.out.printf("%s - %s - %s - %s - %s - %s - %s%n",rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7));
        }
    }
}
