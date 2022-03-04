package GUI.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
    private JComboBox comboBoxTyp;
    private JComboBox comboBoxHerseller;
    private JTextArea kommentarTextArea;
    private JSpinner spinnerMotor;
    private JSpinner spinnerAusstattung;
    private JButton einfügenButton;
    private JButton aktialisierenButton;
    private JButton löschenButton;
    private JSpinner spinnerJahr;
    private JSpinner spinnerAID;

    String AID;

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

    private void EventListener() {
        tableCars.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                setAuto(tableCars.getSelectedRow(), tableCars.getModel());
            }
        });

        einfügenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String sqlAuto = "INSERT INTO Auto (Typ, Baujahr, Hersteller, Kommentar, ASID, MTID) VALUES (?, ?, ?, ?, ?, ?);";
                    PreparedStatement pstmAuto = connect().prepareStatement(sqlAuto);

                    pstmAuto.setString(1, comboBoxTyp.getSelectedItem().toString());
                    pstmAuto.setString(2, spinnerJahr.getValue().toString());
                    pstmAuto.setString(3, comboBoxHerseller.getSelectedItem().toString());

                    if(kommentarTextArea.getText().isBlank()){
                        pstmAuto.setString(4, null);
                    } else {
                        pstmAuto.setString(4, kommentarTextArea.getText());
                    }

                    pstmAuto.setString(5, spinnerAusstattung.getValue().toString());
                    pstmAuto.setString(6, spinnerMotor.getValue().toString());

                    pstmAuto.executeUpdate();

                    reloadTables();
                    setAutoZero();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        löschenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String sqlDelete = "DELETE FROM `auto` WHERE `auto`.`ATID` = ?";
                try {
                    PreparedStatement psm = connect().prepareStatement(sqlDelete);
                    psm.setString(1, AID);

                    psm.executeUpdate();

                    reloadTables();
                    setAutoZero();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        aktialisierenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String sqlUpdateAuto = "UPDATE `auto` SET `Typ` = ?, `Baujahr` = ?, `Hersteller` = ?, `Kommentar` = ?, `ASID` = ?, `MTID` = ? WHERE `auto`.`ATID` = ?;";
                    PreparedStatement pstmUpdateAuto = connect().prepareStatement(sqlUpdateAuto);

                    pstmUpdateAuto.setString(1, comboBoxTyp.getSelectedItem().toString());
                    pstmUpdateAuto.setString(2, spinnerJahr.getValue().toString());
                    pstmUpdateAuto.setString(3, comboBoxHerseller.getSelectedItem().toString());
                    pstmUpdateAuto.setString(4, kommentarTextArea.getText());
                    pstmUpdateAuto.setString(5, spinnerAusstattung.getValue().toString());
                    pstmUpdateAuto.setString(6, spinnerMotor.getValue().toString());
                    pstmUpdateAuto.setString(7, AID);

                    pstmUpdateAuto.executeUpdate();

                    reloadTables();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void setAuto(int i, TableModel tbm){
        AID = tbm.getValueAt(i,0).toString();

        spinnerAID.setValue(Integer.parseInt(AID));

        switch(tbm.getValueAt(i,1).toString()){
            case "Combi":
                comboBoxTyp.setSelectedIndex(1);
                break;
            case "Coupe":
                comboBoxTyp.setSelectedIndex(2);
                break;
            case "Sportwagen":
                comboBoxTyp.setSelectedIndex(3);
                break;
            case "Limo":
                comboBoxTyp.setSelectedIndex(4);
                break;
            case "SUV":
                comboBoxTyp.setSelectedIndex(5);
                break;
            case "Cabrio":
                comboBoxTyp.setSelectedIndex(6);
                break;
            default:
                comboBoxTyp.setSelectedIndex(0);
                break;
        }

        spinnerJahr.setValue(Integer.parseInt(tbm.getValueAt(i,2).toString()));

        switch(tbm.getValueAt(i,3).toString()){
            case "BMW":
                comboBoxHerseller.setSelectedIndex(1);
                break;
            case "Mercedes":
                comboBoxHerseller.setSelectedIndex(2);
                break;
            case "VW":
                comboBoxHerseller.setSelectedIndex(3);
                break;
            case "Audi":
                comboBoxHerseller.setSelectedIndex(4);
                break;
            case "Opel":
                comboBoxHerseller.setSelectedIndex(5);
                break;
            case "Nissan":
                comboBoxHerseller.setSelectedIndex(6);
                break;
            case "Porsche":
                comboBoxHerseller.setSelectedIndex(7);
                break;
            case "Lamborghini":
                comboBoxHerseller.setSelectedIndex(8);
                break;
            case "Smart":
                comboBoxHerseller.setSelectedIndex(9);
                break;
            case "Ferrari":
                comboBoxHerseller.setSelectedIndex(10);
                break;
            case "Toyota":
                comboBoxHerseller.setSelectedIndex(11);
                break;
            case "Tesla":
                comboBoxHerseller.setSelectedIndex(12);
                break;
        }

        if(tbm.getValueAt(i,4) == null) {
            kommentarTextArea.setText("NULL");
        } else {
            kommentarTextArea.setText(tbm.getValueAt(i,4).toString());
        }

        spinnerAusstattung.setValue(Integer.parseInt(tbm.getValueAt(i, 5).toString()));

        spinnerMotor.setValue(Integer.parseInt(tbm.getValueAt(i, 6).toString()));
    }

    private void setAutoZero(){
        AID = "0";

        spinnerAID.setValue(Integer.parseInt(AID));

        comboBoxTyp.setSelectedIndex(0);

        spinnerJahr.setValue(0);

        comboBoxHerseller.setSelectedIndex(0);

        kommentarTextArea.setText("");

        spinnerAusstattung.setValue(0);

        spinnerMotor.setValue(0);
    }

    private static String getID(PreparedStatement ps){
        int id = 0;
        try {
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                id = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Integer.toString(id);
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public GUI() {
        spinnerJahr.setEditor(new JSpinner.NumberEditor(spinnerJahr,"#"));

        createTableCars();
        createTableAusstattung();
        createTableMotor();
        EventListener();
    }
}

    
