package GUI.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;


public class GUI {
    private JTable table1;
    private JPanel rootPanel;

    private void createTable(){
        DefaultTableModel dtm = new DefaultTableModel(
                null,
                new String[] {"ATID", "Typ", "Baujahr", "Hersteller", "Kommentar", "ASID", "MTID"}
        );

        Statement stm = null;
        try {
            stm = connect().createStatement();
            String execute = "SELECT * FROM Auto";
            ResultSet rs = stm.executeQuery(execute);

            while(rs.next()) {
                String[] data = {rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7)};
                dtm.addRow(data);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

            table1.setModel(dtm);
    }

    public GUI() {
        createTable();
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    private static final String url = "jdbc:mysql://localhost:3306/autohaus";
    private static final String user = "root";
    private static final String pass = "";

    private static Connection connect(){
        try {
            return DriverManager.getConnection(url, user, pass);
        } catch (SQLException e) {
            return null;
        }
    }
}
