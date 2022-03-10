package GUI.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
    private JButton ATeinfügenButton;
    private JButton ATaktialisierenButton;
    private JButton ATlöschenButton;
    private JSpinner spinnerJahr;
    private JTextField textFieldAID;
    private JTextField textFieldAAID;
    private JSpinner spinnerZoll;
    private JComboBox comboBoxFelgenMaterial;
    private JCheckBox checkBoxSitzheizung;
    private JCheckBox checkBoxLenkradheizung;
    private JCheckBox checkBoxSchiebedach;
    private JTextField textFieldFarbe;
    private JComboBox comboBoxFarbMaterial;
    private JComboBox comboBoxInnenraumMaterial;
    private JComboBox comboBoxSitzMaterial;
    private JButton ASeinfügenButton;
    private JButton ASaktualisierenButton;
    private JButton ASlöschenButton;
    private JTextField textFieldMotorID;
    private JTextField textFieldVerbrauch;
    private JComboBox comboBoxGetriebe;
    private JComboBox comboBoxKraftstoff;
    private JSpinner spinnerHubraum;
    private JSpinner spinnerPS;
    private JButton MAINlöschenButton2;
    private JButton MAINaktualisierenButton;
    private JTable tableMain;
    private JButton MTeinfügenButton;
    private JButton MTaktualisierenButton;
    private JButton MTlöschenButton;
    private JLabel ASErrorCode;
    private JLabel ASError;
    private JTextField textFieldPreis;


    /* ----------------------------------- createTable --------------------------*/

    private void createTableCars() {
        try {
            DefaultTableModel dtm = new DefaultTableModel(
                    null,
                    new String[]{"ATID", "Typ", "Baujahr", "Hersteller", "Kommentar", "ASID", "MTID", "Preis"}
            );
            String execute = "SELECT * FROM Auto";
            PreparedStatement stm = connect().prepareStatement(execute);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                String[] data = {rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8)};
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
                String[] data = {rs.getString(1), rs.getString(2), rs.getString(3), jaNeinBoolean(rs.getString(4)), jaNeinBoolean(rs.getString(5)), jaNeinBoolean(rs.getString(6)), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10)};
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

    private void createTableMain() { //TODO Anfangen
        try {
            DefaultTableModel dtm = new DefaultTableModel(
                    null,
                    new String[]{"ATID", "Typ", "Baujahr", "Hersteller", "Kommentar", "FelgenZoll", "FelgenMaterial", "Sitzheizung?", "Lenkradheizung?", "Schiebedach?", "Farbe", "FarbeMaterial", "InnenraumMaterial", "SitzMaterial", "Verbrauch", "Getriebe", "Kraftstoff", "Hubraum", "PS", "Preis"}
            );


            String sql = "SELECT ATID, Typ, Baujahr, Hersteller, Kommentar, FelgenZoll, FelgenMaterial, Sitzheizung, Lenkradheizung, Schiebedach, Farbe, FarbeMaterial, InnenraumMaterial, SitzMaterial, Verbrauch, Getriebe, Kraftstoff, Hubraum, PS, Preis FROM Auto JOIN Ausstattung ON Auto.ASID = Ausstattung.ASID JOIN Motor ON Auto.MTID = Motor.MTID ORDER BY Auto.Hersteller,Auto.Typ ASC;";
            PreparedStatement stm = connect().prepareStatement(sql);
            ResultSet rs = stm.executeQuery();

            while(rs.next()) {
                String[] data = {rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12), rs.getString(13), rs.getString(14), rs.getString(15), rs.getString(16), rs.getString(17), rs.getString(18), rs.getString(19), rs.getString(20)};
                dtm.addRow(data);
            }


            tableMain.setModel(dtm);

           /*stm.close();
           rs.close();*/
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /* ----------------------------------- reloadTable --------------------------*/

    private void reloadTables() {
        createTableMotor();
        createTableCars();
        createTableAusstattung();
        createTableMain();
    }

    /* ----------------------------------- eventListener --------------------------*/

    private void eventListenerAuto() {
        tableCars.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                setAuto(tableCars.getSelectedRow(), tableCars.getModel());
            }
        });

        tableCars.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                setAuto(tableCars.getSelectedRow(), tableCars.getModel());
            }
        });

        ATeinfügenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String sqlAuto = "INSERT INTO Auto (Typ, Baujahr, Hersteller, Kommentar, ASID, MTID, Preis) VALUES (?, ?, ?, ?, ?, ?, ?);";
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
                    pstmAuto.setString(7, textFieldPreis.getText().replace(",","."));

                    pstmAuto.executeUpdate();

                    reloadTables();
                    setAutoZero();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        ATlöschenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String sqlDelete = "DELETE FROM `Auto` WHERE ATID = ?";
                try {
                    PreparedStatement psm = connect().prepareStatement(sqlDelete);
                    psm.setString(1, textFieldAID.getText());

                    psm.executeUpdate();

                    reloadTables();
                    setAutoZero();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        ATaktialisierenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String sqlUpdateAuto = "UPDATE `Auto` SET `Typ` = ?, `Baujahr` = ?, `Hersteller` = ?, `Kommentar` = ?, `ASID` = ?, `MTID` = ? `Preis` = ? WHERE ATID = ?;";
                    PreparedStatement pstmUpdateAuto = connect().prepareStatement(sqlUpdateAuto);

                    pstmUpdateAuto.setString(1, comboBoxTyp.getSelectedItem().toString());
                    pstmUpdateAuto.setString(2, spinnerJahr.getValue().toString());
                    pstmUpdateAuto.setString(3, comboBoxHerseller.getSelectedItem().toString());

                    if(kommentarTextArea.getText().isBlank()){
                        pstmUpdateAuto.setString(4, null);
                    } else {
                        pstmUpdateAuto.setString(4, kommentarTextArea.getText());
                    }

                    pstmUpdateAuto.setString(5, spinnerAusstattung.getValue().toString());
                    pstmUpdateAuto.setString(6, spinnerMotor.getValue().toString());
                    pstmUpdateAuto.setString(7, textFieldPreis.getText().replace(",","."));
                    pstmUpdateAuto.setString(8, textFieldAID.getText());

                    pstmUpdateAuto.executeUpdate();

                    reloadTables();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void eventListenerAusstattung(){
        tableAusstattung.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                setAusstattung(tableAusstattung.getSelectedRow(), tableAusstattung.getModel());
            }
        });

        tableAusstattung.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                setAusstattung(tableAusstattung.getSelectedRow(), tableAusstattung.getModel());
            }
        });

        ASeinfügenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String sqlAusstattung = "INSERT INTO Ausstattung (FelgenZoll, Felgenmaterial, Sitzheizung, Lenkradheizung, Schiebedach, Farbe, FarbeMaterial, InnenraumMaterial, SitzMaterial) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
                    PreparedStatement pstmAusstattung = connect().prepareStatement(sqlAusstattung, Statement.RETURN_GENERATED_KEYS);

                    pstmAusstattung.setString(1,spinnerZoll.getValue().toString());
                    pstmAusstattung.setString(2, comboBoxFelgenMaterial.getSelectedItem().toString());
                    pstmAusstattung.setBoolean(3,checkBoxSitzheizung.isSelected());
                    pstmAusstattung.setBoolean(4,checkBoxLenkradheizung.isSelected());
                    pstmAusstattung.setBoolean(5,checkBoxSchiebedach.isSelected());

                    if(textFieldFarbe.getText().isBlank()) {
                        pstmAusstattung.setString(6, null);
                    } else {
                        pstmAusstattung.setString(6, textFieldFarbe.getText());
                    }

                    pstmAusstattung.setString(7,comboBoxFarbMaterial.getSelectedItem().toString());
                    pstmAusstattung.setString(8,comboBoxInnenraumMaterial.getSelectedItem().toString());
                    pstmAusstattung.setString(9, comboBoxSitzMaterial.getSelectedItem().toString());

                    pstmAusstattung.executeUpdate();

                    spinnerAusstattung.setValue(Integer.parseInt(getID(pstmAusstattung)));

                    reloadTables();
                    setAusstattungZero();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        ASaktualisierenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String sqlAusstattung = "UPDATE Ausstattung SET FelgenZoll = ?, Felgenmaterial = ?, Sitzheizung = ?, Lenkradheizung = ?, Schiebedach = ?, Farbe = ?, FarbeMaterial = ?, InnenraumMaterial = ?, SitzMaterial = ? WHERE ASID = ?";
                    PreparedStatement pstmAusstattung = connect().prepareStatement(sqlAusstattung);

                    pstmAusstattung.setString(1, spinnerZoll.getValue().toString());
                    pstmAusstattung.setString(2, comboBoxFelgenMaterial.getSelectedItem().toString());
                    pstmAusstattung.setBoolean(3, checkBoxSitzheizung.isSelected());
                    pstmAusstattung.setBoolean(4, checkBoxLenkradheizung.isSelected());
                    pstmAusstattung.setBoolean(5, checkBoxSchiebedach.isSelected());
                    pstmAusstattung.setString(6, textFieldFarbe.getText());
                    pstmAusstattung.setString(7, comboBoxFarbMaterial.getSelectedItem().toString());
                    pstmAusstattung.setString(8, comboBoxInnenraumMaterial.getSelectedItem().toString());
                    pstmAusstattung.setString(9, comboBoxSitzMaterial.getSelectedItem().toString());
                    pstmAusstattung.setString(10, textFieldAAID.getText());

                    pstmAusstattung.executeUpdate();

                    reloadTables();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        ASlöschenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String sqlAusstattung = "DELETE FROM Ausstattung WHERE ASID = ?";
                    PreparedStatement pstmAusstattung = connect().prepareStatement(sqlAusstattung);

                    pstmAusstattung.setString(1, textFieldAAID.getText());

                    pstmAusstattung.executeUpdate();

                    reloadTables();
                } catch (SQLException ex) {
                    if (ex.toString().startsWith("java.sql.SQLIntegrityConstraintViolationException: Cannot delete or update a parent row: a foreign key constraint fails")) {
                        System.out.println("Foregin Key in Use!");
                        ASError.setVisible(true);
                        ASErrorCode.setText("Foregin Key in Use!");
                        new java.util.Timer().schedule(
                                new java.util.TimerTask() {
                                    @Override
                                    public void run() {
                                        ASErrorCode.setText(null);
                                        ASError.setVisible(false);
                                    }
                                },
                                2000
                        );
                    }
                }
            }
        });
    }

    private void eventListenerMotor(){
        tableMotor.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                setMotor(tableMotor.getSelectedRow(), tableMotor.getModel());
            }
        });

        tableMotor.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                setMotor(tableMotor.getSelectedRow(), tableMotor.getModel());
            }
        });

        MTeinfügenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String sqlMotor = "INSERT INTO Motor (Verbrauch, Getriebe, Kraftstoff, Hubraum, PS) VALUES (?, ?, ?, ?, ?);";
                    PreparedStatement pstmMotor = connect().prepareStatement(sqlMotor, Statement.RETURN_GENERATED_KEYS);

                    pstmMotor.setString(1, textFieldVerbrauch.getText().replace(",","."));
                    pstmMotor.setString(2, comboBoxGetriebe.getSelectedItem().toString());
                    pstmMotor.setString(3, comboBoxKraftstoff.getSelectedItem().toString());
                    pstmMotor.setString(4, spinnerHubraum.getValue().toString());
                    pstmMotor.setString(5, spinnerPS.getValue().toString());

                    pstmMotor.executeUpdate();

                    spinnerMotor.setValue(Integer.parseInt(getID(pstmMotor)));

                    setMotorZero();

                    reloadTables();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

            }
        });

        MTaktualisierenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String sqlMotor = "UPDATE Motor SET Verbrauch = ?, Getriebe = ?, Kraftstoff = ?, Hubraum = ?, PS = ? WHERE MTID = ?";
                    PreparedStatement pstmMotor = connect().prepareStatement(sqlMotor);

                    pstmMotor.setString(1, textFieldVerbrauch.getText().replace(",","."));
                    pstmMotor.setString(2, comboBoxGetriebe.getSelectedItem().toString());
                    pstmMotor.setString(3, comboBoxKraftstoff.getSelectedItem().toString());
                    pstmMotor.setString(4, spinnerHubraum.getValue().toString());
                    pstmMotor.setString(5, spinnerPS.getValue().toString());
                    pstmMotor.setString(6, textFieldMotorID.getText());

                    pstmMotor.executeUpdate();

                    reloadTables();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        MTlöschenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String sqlMotor = "DELETE FROM Motor WHERE MTID = ?";
                    PreparedStatement pstmMotor = connect().prepareStatement(sqlMotor);

                    pstmMotor.setString(1, textFieldMotorID.getText());

                    pstmMotor.executeUpdate();

                    setMotorZero();

                    reloadTables();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void eventListenerMain(){
        MAINaktualisierenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reloadTables();
            }
        });
    }

    /* ----------------------------------- setMethoden --------------------------*/

    private void setAuto(int i, TableModel tbm){

        textFieldAID.setText(tbm.getValueAt(i,0).toString());

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

        textFieldPreis.setText(tbm.getValueAt(i,7).toString());
    }

    private void setAutoZero(){
        textFieldAID.setText("0");

        comboBoxTyp.setSelectedIndex(0);

        spinnerJahr.setValue(0);

        comboBoxHerseller.setSelectedIndex(0);

        kommentarTextArea.setText("");

        spinnerAusstattung.setValue(0);

        spinnerMotor.setValue(0);

        textFieldPreis.setText("0,0");
    }

    private void setAusstattung(int i, TableModel tbm){

        textFieldAAID.setText(tbm.getValueAt(i, 0).toString());

        spinnerZoll.setValue(Integer.parseInt(tbm.getValueAt(i,1).toString()));

        switch(tbm.getValueAt(i,2).toString()){
            case "Aluminium":
                comboBoxFelgenMaterial.setSelectedIndex(1);
                break;
            case "Stahl":
                comboBoxFelgenMaterial.setSelectedIndex(2);
                break;
            case "Carbon":
                comboBoxFelgenMaterial.setSelectedIndex(3);
                break;
            case "Magnesium":
                comboBoxFelgenMaterial.setSelectedIndex(4);
                break;
            case "Silizium":
                comboBoxFelgenMaterial.setSelectedIndex(5);
                break;
            case "Mangan":
                comboBoxFelgenMaterial.setSelectedIndex(6);
                break;
            default:
                comboBoxFelgenMaterial.setSelectedIndex(0);
                break;
        }

        System.out.println(tbm.getValueAt(i,3).toString());

        if (tbm.getValueAt(i,3).toString().equals("Ja")){
            checkBoxSitzheizung.setSelected(true);
        } else {
            checkBoxSitzheizung.setSelected(false);
        }

        if (tbm.getValueAt(i,4).toString().equals("Ja")){
            checkBoxLenkradheizung.setSelected(true);
        } else {
            checkBoxLenkradheizung.setSelected(false);
        }

        if (tbm.getValueAt(i,5).toString().equals("Ja")){
            checkBoxSchiebedach.setSelected(true);
        } else {
            checkBoxSchiebedach.setSelected(false);
        }

        textFieldFarbe.setText(tbm.getValueAt(i,6).toString());

        switch(tbm.getValueAt(i,7).toString()) {
            case "Matt":
                comboBoxFarbMaterial.setSelectedIndex(1);
                break;
            case "Glanz":
                comboBoxFarbMaterial.setSelectedIndex(1);
                break;
            case "Perleffekt":
                comboBoxFarbMaterial.setSelectedIndex(1);
                break;
            default:
                comboBoxFarbMaterial.setSelectedIndex(0);
                break;
        }

        switch(tbm.getValueAt(i, 8).toString()) {
            case "Carbon":
                comboBoxInnenraumMaterial.setSelectedIndex(1);
                break;
            case "Alkantara":
                comboBoxInnenraumMaterial.setSelectedIndex(2);
                break;
            case "Holz":
                comboBoxInnenraumMaterial.setSelectedIndex(3);
                break;
            case "Plastik":
                comboBoxInnenraumMaterial.setSelectedIndex(4);
                break;
            default:
                comboBoxInnenraumMaterial.setSelectedIndex(0);
                break;
        }

        switch (tbm.getValueAt(i,9).toString()) {
            case "Stoff":
                comboBoxSitzMaterial.setSelectedIndex(1);
                break;
            case "Leder":
                comboBoxSitzMaterial.setSelectedIndex(2);
                break;
            case "Alkantara":
                comboBoxSitzMaterial.setSelectedIndex(3);
                break;
            default:
                comboBoxSitzMaterial.setSelectedIndex(0);
                break;
        }
    }

    private void setAusstattungZero() {
        textFieldAAID.setText("0");

        spinnerZoll.setValue(0);

        comboBoxFelgenMaterial.setSelectedIndex(0);

        checkBoxSitzheizung.setSelected(false);

        checkBoxLenkradheizung.setSelected(false);

        checkBoxSchiebedach.setSelected(false);

        textFieldFarbe.setText(null);

        comboBoxFarbMaterial.setSelectedIndex(0);

        comboBoxInnenraumMaterial.setSelectedIndex(0);

        comboBoxSitzMaterial.setSelectedIndex(0);
    }

    private void setMotor (int i, TableModel tbm) {
        textFieldMotorID.setText(tbm.getValueAt(i,0).toString());

        textFieldVerbrauch.setText(tbm.getValueAt(i,1).toString());

        switch(tbm.getValueAt(i,2).toString()){
            case "Manuell":
                comboBoxGetriebe.setSelectedIndex(1);
                break;
            case "Automatik":
                comboBoxGetriebe.setSelectedIndex(2);
                break;
            case "Halb-Automatik":
                comboBoxGetriebe.setSelectedIndex(3);
                break;
            default:
                comboBoxGetriebe.setSelectedIndex(0);
                break;
        }

        switch (tbm.getValueAt(i,3).toString()) {
            case "Diesel":
                comboBoxKraftstoff.setSelectedIndex(1);
                break;
            case "Benzin":
                comboBoxKraftstoff.setSelectedIndex(2);
                break;
            case "Strom":
                comboBoxKraftstoff.setSelectedIndex(3);
                break;
            case "Gas":
                comboBoxKraftstoff.setSelectedIndex(4);
                break;
            default:
                comboBoxKraftstoff.setSelectedIndex(0);
                break;
        }

        spinnerHubraum.setValue(Integer.parseInt(tbm.getValueAt(i,4).toString()));

        spinnerPS.setValue(Integer.parseInt(tbm.getValueAt(i,5).toString()));
    }

    private void setMotorZero() {
        textFieldMotorID.setText(null);

        textFieldVerbrauch.setText("0");

        comboBoxGetriebe.setSelectedIndex(0);

        comboBoxKraftstoff.setSelectedIndex(0);

        spinnerHubraum.setValue(0);

        spinnerPS.setValue(0);
    }

    /* ----------------------------------- Sonstiges --------------------------*/

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

    private static String jaNeinBoolean(String x) {
        switch (x) {
            case "0":
                return "Nein";
            case "1":
                return "Ja";
            default:
                return "Unbekannt";
        }
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public GUI() {
        spinnerJahr.setEditor(new JSpinner.NumberEditor(spinnerJahr,"#"));
        spinnerHubraum.setEditor(new JSpinner.NumberEditor(spinnerHubraum,"#"));
        spinnerPS.setEditor(new JSpinner.NumberEditor(spinnerPS,"#"));

        ASError.setVisible(false);

        createTableCars();
        createTableAusstattung();
        createTableMotor();
        createTableMain();
        eventListenerAuto();
        eventListenerAusstattung();
        eventListenerMotor();
        eventListenerMain();
    }
}

//TODO Motor Foregin Key Error!

    
