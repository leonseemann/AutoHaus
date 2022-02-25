package GUI.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import static SQL.Connect.connect;


public class GUI {
    private JTable tableCars;
    private JPanel rootPanel;
    private JTabbedPane tabbedPaneList;
    private JSplitPane split;
    private JButton button1;
    private JTextArea textArea1;
    private JTextArea textArea2;
    private JTextArea textArea3;
    private JTextArea textArea4;
    private JTextArea textArea5;
    private JTable tableDelete;
    private JSpinner spinner1;
    private JButton button2;


    private void createTable() {
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
            tableDelete.setModel(dtm);

            stm.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void splitPanel() {
        split.setOrientation(0);
    }

    private void reloadTables() {
        try {
            DefaultTableModel dtm = new DefaultTableModel(
                    null,
                    new String[]{"ATID", "Typ", "Baujahr", "Hersteller", "Kommentar", "ASID", "MTID"}
            );
            String execute = "SELECT * FROM Auto";
            PreparedStatement pst = null;
            pst = connect().prepareStatement(execute);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String[] data = {rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7)};
                dtm.addRow(data);
            }

            tableCars.setModel(dtm);
            tableDelete.setModel(dtm);

            pst.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public GUI() {
        createTable();
        splitPanel();
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Pressed");
            }
        }); //TODO ActionListener list for better Code (New Class or something)!
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String sql = "DELETE FROM Auto WHERE ATID = ?";
                try {
                    PreparedStatement delete = connect().prepareStatement(sql);
                    delete.setString(1, spinner1.getValue().toString());
                    delete.executeUpdate();
                    System.out.println("DONE");
                    reloadTables();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

}

    
