import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Main extends Connect{
    public static void main(String[] args) throws SQLException {
        tryConnect();
        Statement stm = connect().createStatement();

        String execute = "SELECT * FROM ausstattung";

        ResultSet rs = stm.executeQuery(execute);

        while(rs.next()){
            System.out.printf("%s - %s - %s - %s - %s - %s - %s - %s -%s -%s%n",rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9),rs.getString(10));
        }


        //System.out.println(inputFelgenmaterial());

        inputSQL();
    }

    private static void inputSQL(){
        String sqlAusstattung = "INSERT INTO `ausstattung` (`FelgenZoll`, `Felgenmaterial`, `Sitzheizung`, `Lenkradheizung`, `Schiebedach`, `Farbe`, `FarbeMaterial`, `InnenraumMaterial`, `SitzMaterial`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
        String sqlMotor = "INSERT INTO `motor` (`Verbrauch`, `Getriebe`, `Kraftstoff`, `Hubraum`, `PS`) VALUES (?, ?, ?, ?, ?);";
        String sqlAuto = "INSERT INTO `auto` (`Typ`, `Baujahr`, `Hersteller`, `Kommentar`, `ASID`, `MTID`) VALUES (?, ?, ?, ?, ?, ?);";

        try {
            PreparedStatement pstmAusstattung = connect().prepareStatement(sqlAusstattung);

            pstmAusstattung.setString(1, inputInt("Felgen Zoll: "));
            pstmAusstattung.setString(2, inputFelgenmaterial());
            pstmAusstattung.setString(3, inputBoolean("Sitzheizung?: "));
            pstmAusstattung.setString(4, inputBoolean("Lenkradheizung?: "));
            pstmAusstattung.setString(5, inputBoolean("Schiebedach?: "));
            pstmAusstattung.setString(6, inputString("Farbe: "));
            pstmAusstattung.setString(7, inputFarbMaterial());
            pstmAusstattung.setString(8, inputInnenraumMaterial());
            pstmAusstattung.setString(9, inputSitzMaterial());

            pstmAusstattung.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private  static void printArray(String[] stringArray){
        for(int i = 0; i < stringArray.length; i++){
            System.out.printf("[%s] %s - ", i+1, stringArray[i]);
        }
        System.out.print(":");
    }

    private static String inputString(String name){
        Scanner scan = new Scanner(System.in);
        System.out.print(name);
        return scan.next();
    }

    private static String inputBoolean(String name){
        Scanner scan = new Scanner(System.in);
        System.out.print(name);

        if(scan.nextBoolean()){
            return Integer.toString(1);
        } else {
            return Integer.toString(0);
        }
    }

    private static String inputInt(String name){
        Scanner scan = new Scanner(System.in);
        System.out.print(name);
        return Integer.toString(scan.nextInt());
    }

    private static String inputFelgenmaterial(){
        String[] stringArray = {"Aluminium","Stahl", "Carbon", "Magnesium", "Silizium", "Mangan"};

        printArray(stringArray);

        Scanner scan = new Scanner(System.in);
        int i = scan.nextInt();
        switch (i){
            case (1):
                return stringArray[0];
            case (2):
                return stringArray[1];
            case (3):
                return stringArray[2];
            case (4):
                return stringArray[3];
            case (5):
                return stringArray[4];
            case (6):
                return stringArray[5];
            default:
                System.err.println("Das gibt es nicht!");
                inputFelgenmaterial();
        }
        return null;
    }

    private static String inputFarbMaterial(){
        String[] stringArray = {"Matt", "Glaz", "Perleffekt"};

        printArray(stringArray);

        Scanner scan = new Scanner(System.in);
        int i = scan.nextInt();
        switch (i){
            case (1):
                return stringArray[0];
            case (2):
                return stringArray[1];
            case (3):
                return stringArray[2];
            default:
                System.err.println("Das gibt es nicht!");
                inputFarbMaterial();
                return null;
        }
    }

    private static String inputInnenraumMaterial(){
        String[] stringArray = {"Carbon", "Alkantara", "Holz", "Plastik"};

        printArray(stringArray);

        Scanner scan = new Scanner(System.in);
        int i = scan.nextInt();
        switch (i){
            case (1):
                return stringArray[0];
            case (2):
                return stringArray[1];
            case (3):
                return stringArray[2];
            case (4):
                return stringArray[3];
            default:
                System.err.println("Das gibt es nicht!");
                inputFarbMaterial();
                return null;
        }
    }

    private static String inputSitzMaterial(){
        String[] stringArray = {"Stoff", "Leder", "Alkantara"};

        printArray(stringArray);

        Scanner scan = new Scanner(System.in);
        int i = scan.nextInt();
        switch (i){
            case (1):
                return stringArray[0];
            case (2):
                return stringArray[1];
            case (3):
                return stringArray[2];
            default:
                System.err.println("Das gibt es nicht!");
                inputFarbMaterial();
                return null;
        }
    }

    private static String inputGetriebe(){
        String[] stringArray = {"Manuell", "Automatik", "Halb-Automatik"};

        printArray(stringArray);

        Scanner scan = new Scanner(System.in);
        int i = scan.nextInt();
        switch (i){
            case (1):
                return stringArray[0];
            case (2):
                return stringArray[1];
            case (3):
                return stringArray[2];
            default:
                System.err.println("Das gibt es nicht!");
                inputFarbMaterial();
                return null;
        }
    }

    private static String inputKraftstoff(){
        String[] stringArray = {"Diesel", "Benzin", "Strom", "Gas"};

        printArray(stringArray);

        Scanner scan = new Scanner(System.in);
        int i = scan.nextInt();
        switch (i){
            case (1):
                return stringArray[0];
            case (2):
                return stringArray[1];
            case (3):
                return stringArray[2];
            case (4):
                return stringArray[3];
            default:
                System.err.println("Das gibt es nicht!");
                inputFarbMaterial();
                return null;
        }
    }

    private static String inputTyp(){
        String[] stringArray = {"Combi", "Coupe", "Sportwagen", "Limo", "SUV", "Cabrio"};

        printArray(stringArray);

        Scanner scan = new Scanner(System.in);
        int i = scan.nextInt();
        switch (i){
            case (1):
                return stringArray[0];
            case (2):
                return stringArray[1];
            case (3):
                return stringArray[2];
            case (4):
                return stringArray[3];
            case (5):
                return stringArray[4];
            case (6):
                return stringArray[5];
            default:
                System.err.println("Das gibt es nicht!");
                inputFarbMaterial();
                return null;
        }
    }

    private static String inputHersteller(){
        String[] stringArray = {"BMW", "Mercedes", "VW", "Audi", "Opel", "Nissan", "Porsche", "Lamborghini", "Smart", "Ferrari", "Toyota", "Tesla"};

        printArray(stringArray);

        Scanner scan = new Scanner(System.in);
        int i = scan.nextInt();
        switch (i){
            case (1):
                return stringArray[0];
            case (2):
                return stringArray[1];
            case (3):
                return stringArray[2];
            case (4):
                return stringArray[3];
            case (5):
                return stringArray[4];
            case (6):
                return stringArray[5];
            case (7):
                return stringArray[6];
            case (8):
                return stringArray[7];
            case (9):
                return stringArray[8];
            case (10):
                return stringArray[9];
            case (11):
                return stringArray[10];
            case (12):
                return stringArray[11];
            default:
                System.err.println("Das gibt es nicht!");
                inputFarbMaterial();
                return null;
        }
    }
}
