package GUI.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static SQL.Connect.connect;

public class GUI {
    private JPanel rootPanel;
    private JTabbedPane tabbedPane1;

    private JTable tableCars;
    private JTable tableAusstattung;
    private JTable tableMotor;


    private void createTableCars() {
        try {
            DefaultTableModel dtm = new DefaultTableModel(
                    null,
                    new String[]{"ATID", "Typ", "Baujahr", "Hersteller", "Kommentar", "ASID", "MTID"}
            );
            String execute = "SELECT * FROM Auto";
            PreparedStatement stm = connect().prepareStatement(execute);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                String[] data = {rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7)};
                dtm.addRow(data);
            }

            tableCars.setModel(dtm);

            stm.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTableAusstattung(){
        try {
            DefaultTableModel dtm = new DefaultTableModel(
                    null,
                    new String[]{"ASID", "FelgenZoll", "FelgenMaterial", "Sitzheizung?", "Lenkradheizung?", "Schiebedach?", "Farbe", "FarbeMaterial", "InnenraumMaterial", "SitzMaterial"}
            );
            String execute = "SELECT * FROM Ausstattung";
            PreparedStatement stm = connect().prepareStatement(execute);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                String[] data = {rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10)};
                dtm.addRow(data);
            }

            tableAusstattung.setModel(dtm);

            stm.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTableMotor() {
        try {
            DefaultTableModel dtm = new DefaultTableModel(
                    null,
                    new String[]{"MTID", "Verbrauch", "Getriebe", "Kraftstoff", "Hubraum", "PS"}
            );
            String execute = "SELECT * FROM Motor";
            PreparedStatement stm = connect().prepareStatement(execute);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                String[] data = {rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6)};
                dtm.addRow(data);
            }

            tableMotor.setModel(dtm);

            stm.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void reloadTables() {
        createTableMotor();
        createTableCars();
        createTableAusstattung();
    }

    public GUI() {
        createTableCars();
        createTableAusstattung();
        createTableMotor();
        EventListener();
    }

    private void EventListener() {
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

}

    
