package de.autohaus.ui;

import de.autohaus.logic.*;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static de.autohaus.model.Connect.connect;

public class GUI {
    private JPanel rootPanel;
    private JTabbedPane tabbedPane1;
    private JTable tableCars;
    private JTable tableAusstattung;
    private JTable tableMotor;
    private JComboBox comboBoxTyp;
    private JComboBox comboBoxHerseller;
    private JTextArea kommentarTextArea;
    private JButton ATeinfügenButton;
    private JButton ATaktialisierenButton;
    private JButton ATlöschenButton;
    private JSpinner spinnerJahr;
    private JTextField ATtextFieldATID;
    private JTextField AStextFieldASID;
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
    private JTextField MTtextFieldMTID;
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
    private JTextField MAINtextFieldATID;
    private JTextField MAINtextFieldASID;
    private JTextField MAINtextFieldMTID;
    private JTextField ATtextFieldASID;
    private JTextField ATtextFieldMTID;
    private JButton ATbrowse;
    private JTextField ATbrowseLink;
    private JTextField ATtextFieldModell;

    private String BenutzerID;

    public void setBenutzerID(String benutzerID) {
        BenutzerID = benutzerID;
    }

    public String getBenutzerID() {
        return BenutzerID;
    }

    /* ----------------------------------- createTable --------------------------*/

    private void createTableCars() {
        tableCars.setModel(new DtmTableCars().getDtm());
    }

    private void createTableAusstattung(){
        tableAusstattung.setModel(new DtmTableAusstattung().getDtm());
    }

    private void createTableMotor() {
            tableMotor.setModel(new DtmTableMotor().getDtm());
    }

    private void createTableMain() {
            tableMain.setModel(new DtmTableMain().getDtm());
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
                PreparedStatement pstm = new InsertAuto().getPstm();

                try {
                    pstm.setString(1, comboBoxTyp.getSelectedItem().toString());
                    pstm.setString(2, ATtextFieldModell.getText());
                    pstm.setString(3, spinnerJahr.getValue().toString());
                    pstm.setString(4, comboBoxHerseller.getSelectedItem().toString());

                    if(kommentarTextArea.getText().isBlank()){
                        pstm.setString(5, null);
                    } else {
                        pstm.setString(5, kommentarTextArea.getText());
                    }

                    pstm.setString(6, ATtextFieldASID.getText());
                    pstm.setString(7, ATtextFieldMTID.getText());
                    pstm.setString(8, textFieldPreis.getText().replace(",","."));

                    if (ATbrowseLink.getText().isBlank()) {
                        pstm.setString(9, null);
                    } else {
                        InputStream in = new FileInputStream(ATbrowseLink.getText());
                        pstm.setBlob(9, in);
                    }

                    pstm.executeUpdate();

                    new InsertLogs(getBenutzerID(), "erstellt", "Auto", getID(pstm), ATtextFieldModell.getText());

                    reloadTables();
                    setAutoZero();
                } catch (SQLException ex) {
                    new InsertLogs(getBenutzerID(), "erstellt", "Auto", true);
                    ex.printStackTrace();
                } catch (FileNotFoundException ex) {
                    new InsertLogs(getBenutzerID(), "erstellt", "Auto", true);
                    ex.printStackTrace();
                }
            }
        });

        ATlöschenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    new DeleteAuto(ATtextFieldATID, getBenutzerID(), ATtextFieldATID);

                    reloadTables();
                    setAutoZero();
            }
        });

        ATaktialisierenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!ATtextFieldATID.getText().equals("0") && !ATtextFieldATID.getText().isBlank() && !ATtextFieldATID.getText().isEmpty()) {
                    PreparedStatement pstm = new UpdateAuto().getPstm();

                    try {

                        pstm.setString(1, comboBoxTyp.getSelectedItem().toString());
                        pstm.setString(2, ATtextFieldModell.getText());
                        pstm.setString(3, spinnerJahr.getValue().toString());
                        pstm.setString(4, comboBoxHerseller.getSelectedItem().toString());

                        if(kommentarTextArea.getText().isBlank()){
                            pstm.setString(5, null);
                        } else {
                            pstm.setString(5, kommentarTextArea.getText());
                        }

                        pstm.setString(6, textFieldPreis.getText().replace(",","."));
                        pstm.setString(8, ATtextFieldATID.getText());

                        if (ATbrowseLink.getText().isBlank()) {
                            String sql = "SELECT bild FROM auto WHERE ATID = ?;";
                            PreparedStatement pstmSelect = connect().prepareStatement(sql);
                            pstmSelect.setString(1, ATtextFieldATID.getText());
                            ResultSet rsSelect = pstmSelect.executeQuery();

                            rsSelect.next();
                            pstm.setBlob(7, rsSelect.getBlob("bild"));
                        } else {
                            InputStream in = new FileInputStream(ATbrowseLink.getText());
                            pstm.setBlob(7, in);
                        }

                        pstm.executeUpdate();

                        new InsertLogs(getBenutzerID(), "editiert", "Auto", ATtextFieldATID.getText(), ATtextFieldModell.getText());
                        ATbrowseLink.setText(null);
                        reloadTables();
                    } catch (SQLException ex) {
                        new InsertLogs(getBenutzerID(), "editiert", "Auto", getID(pstm), true);
                        ex.printStackTrace();
                    } catch (FileNotFoundException ex) {
                        new InsertLogs(getBenutzerID(), "editiert", "Auto", getID(pstm), true);
                        ex.printStackTrace();
                    }
                } else {
                    new InsertLogs(getBenutzerID(), "editiert", "Auto", true);
                }
            }
        });

        ATbrowse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ATbrowseLink.setText(new Browse().getLinkFile());
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
                PreparedStatement pstm = new InsertAusstattung().getPstm();

                try {

                    pstm.setString(1,spinnerZoll.getValue().toString());
                    pstm.setObject(2, comboBoxFelgenMaterial.getSelectedItem());
                    pstm.setBoolean(3,checkBoxSitzheizung.isSelected());
                    pstm.setBoolean(4,checkBoxLenkradheizung.isSelected());
                    pstm.setBoolean(5,checkBoxSchiebedach.isSelected());

                    if(textFieldFarbe.getText().isBlank()) {
                        pstm.setString(6, null);
                    } else {
                        pstm.setString(6, textFieldFarbe.getText());
                    }

                    pstm.setObject(7,comboBoxFarbMaterial.getSelectedItem());
                    pstm.setObject(8,comboBoxInnenraumMaterial.getSelectedItem());
                    pstm.setObject(9, comboBoxSitzMaterial.getSelectedItem());

                    pstm.executeUpdate();

                    ATtextFieldASID.setText(getID(pstm));

                    new InsertLogs(getBenutzerID(), "erstellt", "Ausstattung", getID(pstm));

                    reloadTables();
                    setAusstattungZero();
                } catch (SQLException ex) {
                    new InsertLogs(getBenutzerID(), "erstellt", "Ausstattung", true);
                    ex.printStackTrace();
                }
            }
        });

        ASaktualisierenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!AStextFieldASID.getText().equals("0") && !AStextFieldASID.getText().isBlank() && !AStextFieldASID.getText().isEmpty()) {
                    PreparedStatement pstm = new UpdateAusstattung().getPstm();

                    try {

                        pstm.setString(1, spinnerZoll.getValue().toString());
                        pstm.setString(2, comboBoxFelgenMaterial.getSelectedItem().toString());
                        pstm.setBoolean(3, checkBoxSitzheizung.isSelected());
                        pstm.setBoolean(4, checkBoxLenkradheizung.isSelected());
                        pstm.setBoolean(5, checkBoxSchiebedach.isSelected());
                        pstm.setString(6, textFieldFarbe.getText());
                        pstm.setString(7, comboBoxFarbMaterial.getSelectedItem().toString());
                        pstm.setString(8, comboBoxInnenraumMaterial.getSelectedItem().toString());
                        pstm.setString(9, comboBoxSitzMaterial.getSelectedItem().toString());
                        pstm.setString(10, AStextFieldASID.getText());

                        pstm.executeUpdate();

                        new InsertLogs(getBenutzerID(), "editiert", "Ausstattung", AStextFieldASID.getText());

                        reloadTables();
                    } catch (SQLException ex) {
                        new InsertLogs(getBenutzerID(), "editiert", "Ausstattung", getID(pstm), true);
                        ex.printStackTrace();
                    }
                } else {
                    new InsertLogs(getBenutzerID(), "editiert", "Ausstattung", true);
                }
            }
        });

        ASlöschenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new DeleteAusstattung(AStextFieldASID, getBenutzerID(), AStextFieldASID);



               reloadTables();
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
                if (!comboBoxKraftstoff.getSelectedItem().toString().isEmpty()
                        && !comboBoxKraftstoff.getSelectedItem().toString().isBlank()
                        && !comboBoxGetriebe.getSelectedItem().toString().isEmpty()
                        && !comboBoxGetriebe.getSelectedItem().toString().isBlank()) {
                    PreparedStatement pstm = new InsertMotor().getPstm();

                    try {

                        pstm.setString(1, textFieldVerbrauch.getText().replace(",","."));
                        pstm.setString(2, comboBoxGetriebe.getSelectedItem().toString());
                        pstm.setString(3, comboBoxKraftstoff.getSelectedItem().toString());
                        pstm.setString(4, spinnerHubraum.getValue().toString());
                        pstm.setString(5, spinnerPS.getValue().toString());

                        pstm.executeUpdate();

                        ATtextFieldMTID.setText(getID(pstm));

                        new InsertLogs(getBenutzerID(), "erstellt", "Motor", getID(pstm));

                        setMotorZero();

                        reloadTables();
                    } catch (SQLException ex) {
                        new InsertLogs(getBenutzerID(), "erstellt", "Motor", getID(pstm), true);
                        ex.printStackTrace();
                    }
                } else {
                    new InsertLogs(getBenutzerID(), "erstellt", "Motor", true);
                }
            }
        });

        MTaktualisierenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!MTtextFieldMTID.getText().equals("0") && !MTtextFieldMTID.getText().isBlank() && !MTtextFieldMTID.getText().isEmpty()) {
                    PreparedStatement pstm = new UpdateMotor().getPstm();

                    try {

                        pstm.setString(1, textFieldVerbrauch.getText().replace(",","."));
                        pstm.setString(2, comboBoxGetriebe.getSelectedItem().toString());
                        pstm.setString(3, comboBoxKraftstoff.getSelectedItem().toString());
                        pstm.setString(4, spinnerHubraum.getValue().toString());
                        pstm.setString(5, spinnerPS.getValue().toString());
                        pstm.setString(6, MTtextFieldMTID.getText());

                        pstm.executeUpdate();

                        new InsertLogs(getBenutzerID(), "editiert", "Motor", MTtextFieldMTID.getText());

                        reloadTables();
                    } catch (SQLException ex) {
                        new InsertLogs(getBenutzerID(), "editiert", "Motor", getID(pstm), true);
                        ex.printStackTrace();
                    }
                } else {
                    new InsertLogs(getBenutzerID(), "editiert", "Motor", true);
                }
            }
        });

        MTlöschenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    new DeleteMotor(MTtextFieldMTID, getBenutzerID(), MTtextFieldMTID);


                    setMotorZero();
                    reloadTables();
            }
        });
    }

    private String modellMain;

    private void eventListenerMain(){
        tableMain.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                setMain(tableMain.getSelectedRow(), tableMain.getModel());
                modellMain = tableMain.getValueAt(tableMain.getSelectedRow(), 3).toString();
            }
        });

        MAINaktualisierenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reloadTables();
            }
        });

        MAINlöschenButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                    new DeleteMain(MAINtextFieldATID, getBenutzerID(), MAINtextFieldATID, modellMain);


                    reloadTables();
                    setAutoZero();
            }
        });
    }

    /* ----------------------------------- setMethoden --------------------------*/

    private void setAuto(int i, TableModel tbm){

        ATtextFieldATID.setText(tbm.getValueAt(i,0).toString());

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

        ATtextFieldModell.setText(tbm.getValueAt(i,2).toString());

        spinnerJahr.setValue(Integer.parseInt(tbm.getValueAt(i,3).toString()));

        switch(tbm.getValueAt(i,4).toString()){
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

        if(tbm.getValueAt(i,5) == null) {
            kommentarTextArea.setText("NULL");
        } else {
            kommentarTextArea.setText(tbm.getValueAt(i,5).toString());
        }

        ATtextFieldASID.setText(tbm.getValueAt(i, 6).toString());

        ATtextFieldMTID.setText(tbm.getValueAt(i, 7).toString());

        textFieldPreis.setText(tbm.getValueAt(i,8).toString());
    }

    private void setAutoZero(){
        ATtextFieldATID.setText(null);

        comboBoxTyp.setSelectedIndex(0);

        spinnerJahr.setValue(0);

        comboBoxHerseller.setSelectedIndex(0);

        kommentarTextArea.setText("");

        ATtextFieldASID.setText("0");

        ATtextFieldMTID.setText("0");

        textFieldPreis.setText("0,0");

        ATbrowseLink.setText(null);
    }

    private void setAusstattung(int i, TableModel tbm){

        AStextFieldASID.setText(tbm.getValueAt(i, 0).toString());

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

        checkBoxSitzheizung.setSelected(tbm.getValueAt(i, 3).toString().equals("Ja"));

        checkBoxLenkradheizung.setSelected(tbm.getValueAt(i, 4).toString().equals("Ja"));

        checkBoxSchiebedach.setSelected(tbm.getValueAt(i, 5).toString().equals("Ja"));

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
        AStextFieldASID.setText("0");

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
        MTtextFieldMTID.setText(tbm.getValueAt(i,0).toString());

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
        MTtextFieldMTID.setText(null);

        textFieldVerbrauch.setText("0,0");

        comboBoxGetriebe.setSelectedIndex(0);

        comboBoxKraftstoff.setSelectedIndex(0);

        spinnerHubraum.setValue(0);

        spinnerPS.setValue(0);
    }

    private void setMain(int i, TableModel tbm){
        MAINtextFieldATID.setText(tbm.getValueAt(i, 0).toString());

        try {
            String sql = "SELECT ASID, MTID FROM auto WHERE ATID = ?";
            PreparedStatement pstm = connect().prepareStatement(sql);
            pstm.setString(1, MAINtextFieldATID.getText());

            ResultSet rs = pstm.executeQuery();

            rs.next();
            MAINtextFieldASID.setText(rs.getString(1));
            MAINtextFieldMTID.setText(rs.getString(2));

            pstm.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void setMainZero() {
        MAINtextFieldATID.setText(null);
        MAINtextFieldASID.setText(null);
        MAINtextFieldMTID.setText(null);
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

/test