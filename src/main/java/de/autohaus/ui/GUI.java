package de.autohaus.ui;

import de.autohaus.logic.DeleteAusstattung;
import de.autohaus.logic.DeleteAuto;
import de.autohaus.logic.DeleteMain;
import de.autohaus.logic.DeleteMotor;
import de.autohaus.logic.DtmTableAusstattung;
import de.autohaus.logic.DtmTableBenutzer;
import de.autohaus.logic.DtmTableCars;
import de.autohaus.logic.DtmTableMain;
import de.autohaus.logic.DtmTableMotor;
import de.autohaus.logic.InsertAusstattung;
import de.autohaus.logic.InsertAuto;
import de.autohaus.logic.InsertBenutzer;
import de.autohaus.logic.InsertLogs;
import de.autohaus.logic.InsertMotor;
import de.autohaus.logic.UpdateAusstattung;
import de.autohaus.logic.UpdateAuto;
import de.autohaus.logic.UpdateBenutzer;
import de.autohaus.logic.UpdateMotor;
import de.autohaus.model.RSA;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.*;
import java.util.Base64;
import java.util.Iterator;
import java.util.Objects;

import static de.autohaus.model.Connect.connect;

@SuppressWarnings("rawtypes")
public class GUI {
    private JPanel rootPanel;
    public JTabbedPane tabbedPane1;
    private JTable tableCars;
    private JTable tableAusstattung;
    private JTable tableMotor;
    @SuppressWarnings("rawtypes")
    private JComboBox comboBoxTyp;
    private JComboBox comboBoxHerseller;
    private JTextArea kommentarTextArea;
    private JButton ATeinfuegenButton;
    private JButton ATaktialisierenButton;
    private JButton ATloeschenButton;
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
    private JButton ASeinfuegenButton;
    private JButton ASaktualisierenButton;
    private JButton ASloeschenButton;
    private JTextField MTtextFieldMTID;
    private JTextField textFieldVerbrauch;
    private JComboBox comboBoxGetriebe;
    private JComboBox comboBoxKraftstoff;
    private JSpinner spinnerHubraum;
    private JSpinner spinnerPS;
    private JButton MAINloeschenButton2;
    private JButton MAINaktualisierenButton;
    private JTable tableMain;
    private JButton MTeinfuegenButton;
    private JButton MTaktualisierenButton;
    private JButton MTloeschenButton;
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
    private JTable tableBenutzer;
    private JTextField BNemail;
    private JPasswordField BNpassword;
    private JTextField BNname;
    private JTextField BNvorname;
    private JCheckBox BNcheckBox;
    private JButton einfuegenButton;
    private JSplitPane SplitBenutzer;
    private JButton BNaktualisierenButton;

    private String BenutzerID;
    private String BNnameString;

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

    private void createTableAusstattung() {
        tableAusstattung.setModel(new DtmTableAusstattung().getDtm());
    }

    private void createTableMotor() {
        tableMotor.setModel(new DtmTableMotor().getDtm());
    }

    private void createTableMain() {
        tableMain.setModel(new DtmTableMain().getDtm());
    }

    private void createTableBenutzer() {
        tableBenutzer.setModel(new DtmTableBenutzer().getDtm());
    }

    /* ----------------------------------- reloadTable --------------------------*/

    private void reloadTables() {
        createTableMotor();
        createTableCars();
        createTableAusstattung();
        createTableMain();
        createTableBenutzer();
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

        ATeinfuegenButton.addActionListener(e -> {
            PreparedStatement pstm = new InsertAuto().getPstm();

            try {
                pstm.setString(1, Objects.requireNonNull(comboBoxTyp.getSelectedItem()).toString());
                pstm.setString(2, ATtextFieldModell.getText());
                pstm.setString(3, spinnerJahr.getValue().toString());
                pstm.setString(4, Objects.requireNonNull(comboBoxHerseller.getSelectedItem()).toString());

                if (kommentarTextArea.getText().isBlank()) {
                    pstm.setString(5, null);
                } else {
                    pstm.setString(5, kommentarTextArea.getText());
                }

                pstm.setString(6, ATtextFieldASID.getText());
                pstm.setString(7, ATtextFieldMTID.getText());
                pstm.setString(8, textFieldPreis.getText().replace(",", "."));

                if (ATbrowseLink.getText().isBlank()) {
                    pstm.setString(9, null);
                    pstm.setString(10, null);
                } else {
                    FileInputStream inputStream = new FileInputStream(ATbrowseLink.getText());

                    pstm.setString(9, encodeFileToBase64Binary(ATbrowseLink.getText()));

                    ImageInputStream iis = ImageIO.createImageInputStream(inputStream);

                    Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(iis);

                    while (imageReaders.hasNext()) {
                        ImageReader reader = imageReaders.next();
                        pstm.setString(10, reader.getFormatName());
                    }
                }

                pstm.executeUpdate();

                new InsertLogs(getBenutzerID(), "erstellt", "Auto", getID(pstm), ATtextFieldModell.getText());

                reloadTables();
                setAutoZero();
            } catch (SQLException | FileNotFoundException ex) {
                new InsertLogs(getBenutzerID(), "erstellt", "Auto", true, "SQL");
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        ATloeschenButton.addActionListener(e -> {
            new DeleteAuto(ATtextFieldATID, getBenutzerID(), ATtextFieldATID);

            reloadTables();
            setAutoZero();
        });

        ATaktialisierenButton.addActionListener(e -> {
            if (!ATtextFieldATID.getText().equals("0") && !ATtextFieldATID.getText().isBlank() && !ATtextFieldATID.getText().isEmpty()) {
                PreparedStatement pstm = new UpdateAuto().getPstm();

                try {

                    pstm.setString(1, Objects.requireNonNull(comboBoxTyp.getSelectedItem()).toString());
                    pstm.setString(2, ATtextFieldModell.getText());
                    pstm.setString(3, spinnerJahr.getValue().toString());
                    pstm.setString(4, Objects.requireNonNull(comboBoxHerseller.getSelectedItem()).toString());

                    if (kommentarTextArea.getText().isBlank()) {
                        pstm.setString(5, null);
                    } else {
                        pstm.setString(5, kommentarTextArea.getText());
                    }

                    pstm.setString(6, textFieldPreis.getText().replace(",", "."));
                    pstm.setString(8, ATtextFieldATID.getText());

                    if (ATbrowseLink.getText().isBlank()) {
                        String sql = "SELECT bild FROM auto WHERE ATID = ?;";
                        PreparedStatement pstmSelect = Objects.requireNonNull(connect()).prepareStatement(sql);
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
                } catch (SQLException | FileNotFoundException ex) {
                    new InsertLogs(getBenutzerID(), "editiert", "Auto", getID(pstm), true, "SQL");
                    ex.printStackTrace();
                }
            } else {
                new InsertLogs(getBenutzerID(), "editiert", "Auto", true, "Zero");
            }
        });

        ATbrowse.addActionListener(e -> ATbrowseLink.setText(new Browse().getLinkFile()));
    }

    private void eventListenerAusstattung() {
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

        ASeinfuegenButton.addActionListener(e -> {
            PreparedStatement pstm = new InsertAusstattung().getPstm();

            try {

                pstm.setString(1, spinnerZoll.getValue().toString());
                pstm.setObject(2, comboBoxFelgenMaterial.getSelectedItem());
                pstm.setBoolean(3, checkBoxSitzheizung.isSelected());
                pstm.setBoolean(4, checkBoxLenkradheizung.isSelected());
                pstm.setBoolean(5, checkBoxSchiebedach.isSelected());

                if (textFieldFarbe.getText().isBlank()) {
                    pstm.setString(6, null);
                } else {
                    pstm.setString(6, textFieldFarbe.getText());
                }

                pstm.setObject(7, comboBoxFarbMaterial.getSelectedItem());
                pstm.setObject(8, comboBoxInnenraumMaterial.getSelectedItem());
                pstm.setObject(9, comboBoxSitzMaterial.getSelectedItem());

                pstm.executeUpdate();

                ATtextFieldASID.setText(getID(pstm));

                new InsertLogs(getBenutzerID(), "erstellt", "Ausstattung", getID(pstm));

                reloadTables();
                setAusstattungZero();
            } catch (SQLException ex) {
                new InsertLogs(getBenutzerID(), "erstellt", "Ausstattung", true, "SQL");
                ex.printStackTrace();
            }
        });

        ASaktualisierenButton.addActionListener(e -> {
            if (!AStextFieldASID.getText().equals("0") && !AStextFieldASID.getText().isBlank() && !AStextFieldASID.getText().isEmpty()) {
                PreparedStatement pstm = new UpdateAusstattung().getPstm();

                try {

                    pstm.setString(1, spinnerZoll.getValue().toString());
                    pstm.setString(2, Objects.requireNonNull(comboBoxFelgenMaterial.getSelectedItem()).toString());
                    pstm.setBoolean(3, checkBoxSitzheizung.isSelected());
                    pstm.setBoolean(4, checkBoxLenkradheizung.isSelected());
                    pstm.setBoolean(5, checkBoxSchiebedach.isSelected());
                    pstm.setString(6, textFieldFarbe.getText());
                    pstm.setString(7, Objects.requireNonNull(comboBoxFarbMaterial.getSelectedItem()).toString());
                    pstm.setString(8, Objects.requireNonNull(comboBoxInnenraumMaterial.getSelectedItem()).toString());
                    pstm.setString(9, Objects.requireNonNull(comboBoxSitzMaterial.getSelectedItem()).toString());
                    pstm.setString(10, AStextFieldASID.getText());

                    pstm.executeUpdate();

                    new InsertLogs(getBenutzerID(), "editiert", "Ausstattung", AStextFieldASID.getText());

                    reloadTables();
                } catch (SQLException ex) {
                    new InsertLogs(getBenutzerID(), "editiert", "Ausstattung", getID(pstm), true, "SQL");
                    ex.printStackTrace();
                }
            } else {
                new InsertLogs(getBenutzerID(), "editiert", "Ausstattung", true, "Zero");
            }
        });

        ASloeschenButton.addActionListener(e -> {
            new DeleteAusstattung(AStextFieldASID, getBenutzerID(), AStextFieldASID);


            reloadTables();
        });
    }

    private void eventListenerMotor() {
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

        MTeinfuegenButton.addActionListener(e -> {
            if (!Objects.requireNonNull(comboBoxKraftstoff.getSelectedItem()).toString().isEmpty()
                    && !comboBoxKraftstoff.getSelectedItem().toString().isBlank()
                    && !Objects.requireNonNull(comboBoxGetriebe.getSelectedItem()).toString().isEmpty()
                    && !comboBoxGetriebe.getSelectedItem().toString().isBlank()) {
                PreparedStatement pstm = new InsertMotor().getPstm();

                try {

                    pstm.setString(1, textFieldVerbrauch.getText().replace(",", "."));
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
                    new InsertLogs(getBenutzerID(), "erstellt", "Motor", getID(pstm), true, "SQL");
                    ex.printStackTrace();
                }
            } else {
                new InsertLogs(getBenutzerID(), "erstellt", "Motor", true, "Zero");
            }
        });

        MTaktualisierenButton.addActionListener(e -> {
            if (!MTtextFieldMTID.getText().equals("0") && !MTtextFieldMTID.getText().isBlank() && !MTtextFieldMTID.getText().isEmpty()) {
                PreparedStatement pstm = new UpdateMotor().getPstm();

                try {

                    pstm.setString(1, textFieldVerbrauch.getText().replace(",", "."));
                    pstm.setString(2, Objects.requireNonNull(comboBoxGetriebe.getSelectedItem()).toString());
                    pstm.setString(3, Objects.requireNonNull(comboBoxKraftstoff.getSelectedItem()).toString());
                    pstm.setString(4, spinnerHubraum.getValue().toString());
                    pstm.setString(5, spinnerPS.getValue().toString());
                    pstm.setString(6, MTtextFieldMTID.getText());

                    pstm.executeUpdate();

                    new InsertLogs(getBenutzerID(), "editiert", "Motor", MTtextFieldMTID.getText());

                    reloadTables();
                } catch (SQLException ex) {
                    new InsertLogs(getBenutzerID(), "editiert", "Motor", getID(pstm), true, "SQL");
                    ex.printStackTrace();
                }
            } else {
                new InsertLogs(getBenutzerID(), "editiert", "Motor", true, "Zero");
            }
        });

        MTloeschenButton.addActionListener(e -> {
            new DeleteMotor(MTtextFieldMTID, getBenutzerID(), MTtextFieldMTID);

            setMotorZero();
            reloadTables();
        });
    }

    private String modellMain;

    private void eventListenerMain() {
        tableMain.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                setMain(tableMain.getSelectedRow(), tableMain.getModel());
                modellMain = tableMain.getValueAt(tableMain.getSelectedRow(), 3).toString();
            }
        });

        MAINaktualisierenButton.addActionListener(e -> {
            reloadTables();

            new InsertLogs(getBenutzerID(), "reload", "Main");
        });

        MAINloeschenButton2.addActionListener(actionEvent -> {
            new DeleteMain(MAINtextFieldATID, getBenutzerID(), MAINtextFieldATID, modellMain);

            reloadTables();
            setAutoZero();
        });
    }

    @SuppressWarnings("deprecation")
    private void eventListenerBenutzer() {
        tableBenutzer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                setBenutzer(tableBenutzer.getSelectedRow(), tableBenutzer.getModel());
            }
        });

        tableBenutzer.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                setBenutzer(tableBenutzer.getSelectedRow(), tableBenutzer.getModel());
            }
        });

        einfuegenButton.addActionListener(e -> {
            PreparedStatement pstm = new InsertBenutzer().getPstm();
            if (!BNemail.getText().isEmpty() && !BNemail.getText().isBlank()) {
                try {
                    pstm.setString(1, BNemail.getText());
                    pstm.setString(2, new RSA().encrypt(BNpassword.getText()).toString());
                    pstm.setString(3, BNname.getText());
                    pstm.setString(4, BNvorname.getText());
                    pstm.setBoolean(5, BNcheckBox.isSelected());

                    pstm.executeUpdate();

                    new InsertLogs(getBenutzerID(), "erstellt", "Benutzer", BNemail.getText());

                    reloadTables();
                } catch (SQLException ex) {
                    new InsertLogs(getBenutzerID(), "erstellt", "Benutzer", true, "SQL");
                    ex.printStackTrace();
                }
            } else {
                new InsertLogs(getBenutzerID(), "erstellt", "Benutzer", true, "Zero");
            }
        });

        BNaktualisierenButton.addActionListener(e -> {
            if (!BNnameString.isBlank() && !BNnameString.isEmpty()) {
                PreparedStatement pstm = new UpdateBenutzer().getPstm();

                try {

                    if (BNpassword.getText().isBlank() && BNpassword.getText().isEmpty()) {
                        String sql = "SELECT passwort FROM benutzer WHERE email = ?";
                        PreparedStatement pstmB = Objects.requireNonNull(connect()).prepareStatement(sql);
                        pstmB.setString(1, BNemail.getText());
                        ResultSet rs = pstmB.executeQuery();
                        rs.next();
                        pstm.setString(1, rs.getString(1));
                        rs.close();
                        pstmB.close();
                    } else {
                        pstm.setString(1, new RSA().encrypt(BNpassword.getText()).toString());
                    }

                    pstm.setString(2, BNname.getText());
                    pstm.setString(3, BNvorname.getText());
                    pstm.setBoolean(4, BNcheckBox.isSelected());
                    pstm.setString(5, BNnameString);

                    pstm.executeUpdate();

                    new InsertLogs(getBenutzerID(), "editiert", "Benutzer", BNemail.getText());

                    reloadTables();
                } catch (SQLException ex) {
                    new InsertLogs(getBenutzerID(), "editiert", "Benutzer", getID(pstm), true, "SQL");
                    ex.printStackTrace();
                }
            } else {
                new InsertLogs(getBenutzerID(), "editiert", "Benutzer", true, "Zero");
            }
        });
    }

    /* ----------------------------------- setMethoden --------------------------*/

    private void setAuto(int i, TableModel tbm) {

        ATtextFieldATID.setText(tbm.getValueAt(i, 0).toString());

        switch (tbm.getValueAt(i, 1).toString()) {
            case "Kombi" -> comboBoxTyp.setSelectedIndex(1);
            case "Coupe" -> comboBoxTyp.setSelectedIndex(2);
            case "Sportwagen" -> comboBoxTyp.setSelectedIndex(3);
            case "Limo" -> comboBoxTyp.setSelectedIndex(4);
            case "SUV" -> comboBoxTyp.setSelectedIndex(5);
            case "Cabrio" -> comboBoxTyp.setSelectedIndex(6);
            default -> comboBoxTyp.setSelectedIndex(0);
        }

        ATtextFieldModell.setText(tbm.getValueAt(i, 2).toString());

        spinnerJahr.setValue(Integer.parseInt(tbm.getValueAt(i, 3).toString()));

        switch (tbm.getValueAt(i, 4).toString()) {
            case "BMW" -> comboBoxHerseller.setSelectedIndex(1);
            case "Mercedes" -> comboBoxHerseller.setSelectedIndex(2);
            case "VW" -> comboBoxHerseller.setSelectedIndex(3);
            case "Audi" -> comboBoxHerseller.setSelectedIndex(4);
            case "Opel" -> comboBoxHerseller.setSelectedIndex(5);
            case "Nissan" -> comboBoxHerseller.setSelectedIndex(6);
            case "Porsche" -> comboBoxHerseller.setSelectedIndex(7);
            case "Lamborghini" -> comboBoxHerseller.setSelectedIndex(8);
            case "Smart" -> comboBoxHerseller.setSelectedIndex(9);
            case "Ferrari" -> comboBoxHerseller.setSelectedIndex(10);
            case "Toyota" -> comboBoxHerseller.setSelectedIndex(11);
            case "Tesla" -> comboBoxHerseller.setSelectedIndex(12);
        }

        if (tbm.getValueAt(i, 5) == null) {
            kommentarTextArea.setText("NULL");
        } else {
            kommentarTextArea.setText(tbm.getValueAt(i, 5).toString());
        }

        ATtextFieldASID.setText(tbm.getValueAt(i, 6).toString());

        ATtextFieldMTID.setText(tbm.getValueAt(i, 7).toString());

        textFieldPreis.setText(tbm.getValueAt(i, 8).toString());
    }

    private void setAutoZero() {
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

    private void setAusstattung(int i, TableModel tbm) {

        AStextFieldASID.setText(tbm.getValueAt(i, 0).toString());

        spinnerZoll.setValue(Integer.parseInt(tbm.getValueAt(i, 1).toString()));

        switch (tbm.getValueAt(i, 2).toString()) {
            case "Aluminium" -> comboBoxFelgenMaterial.setSelectedIndex(1);
            case "Stahl" -> comboBoxFelgenMaterial.setSelectedIndex(2);
            case "Carbon" -> comboBoxFelgenMaterial.setSelectedIndex(3);
            case "Magnesium" -> comboBoxFelgenMaterial.setSelectedIndex(4);
            case "Silizium" -> comboBoxFelgenMaterial.setSelectedIndex(5);
            case "Mangan" -> comboBoxFelgenMaterial.setSelectedIndex(6);
            default -> comboBoxFelgenMaterial.setSelectedIndex(0);
        }

        checkBoxSitzheizung.setSelected(tbm.getValueAt(i, 3).toString().equals("Ja"));

        checkBoxLenkradheizung.setSelected(tbm.getValueAt(i, 4).toString().equals("Ja"));

        checkBoxSchiebedach.setSelected(tbm.getValueAt(i, 5).toString().equals("Ja"));

        textFieldFarbe.setText(tbm.getValueAt(i, 6).toString());

        switch (tbm.getValueAt(i, 7).toString()) {
            case "Matt", "Perleffekt", "Glanz" -> comboBoxFarbMaterial.setSelectedIndex(1);
            default -> comboBoxFarbMaterial.setSelectedIndex(0);
        }

        switch (tbm.getValueAt(i, 8).toString()) {
            case "Carbon" -> comboBoxInnenraumMaterial.setSelectedIndex(1);
            case "Alkantara" -> comboBoxInnenraumMaterial.setSelectedIndex(2);
            case "Holz" -> comboBoxInnenraumMaterial.setSelectedIndex(3);
            case "Plastik" -> comboBoxInnenraumMaterial.setSelectedIndex(4);
            default -> comboBoxInnenraumMaterial.setSelectedIndex(0);
        }

        switch (tbm.getValueAt(i, 9).toString()) {
            case "Stoff" -> comboBoxSitzMaterial.setSelectedIndex(1);
            case "Leder" -> comboBoxSitzMaterial.setSelectedIndex(2);
            case "Alkantara" -> comboBoxSitzMaterial.setSelectedIndex(3);
            default -> comboBoxSitzMaterial.setSelectedIndex(0);
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

    private void setMotor(int i, TableModel tbm) {
        MTtextFieldMTID.setText(tbm.getValueAt(i, 0).toString());

        textFieldVerbrauch.setText(tbm.getValueAt(i, 1).toString());

        switch (tbm.getValueAt(i, 2).toString()) {
            case "Manuell" -> comboBoxGetriebe.setSelectedIndex(1);
            case "Automatik" -> comboBoxGetriebe.setSelectedIndex(2);
            case "Halb-Automatik" -> comboBoxGetriebe.setSelectedIndex(3);
            default -> comboBoxGetriebe.setSelectedIndex(0);
        }

        switch (tbm.getValueAt(i, 3).toString()) {
            case "Diesel" -> comboBoxKraftstoff.setSelectedIndex(1);
            case "Benzin" -> comboBoxKraftstoff.setSelectedIndex(2);
            case "Strom" -> comboBoxKraftstoff.setSelectedIndex(3);
            case "Gas" -> comboBoxKraftstoff.setSelectedIndex(4);
            default -> comboBoxKraftstoff.setSelectedIndex(0);
        }

        spinnerHubraum.setValue(Integer.parseInt(tbm.getValueAt(i, 4).toString()));

        spinnerPS.setValue(Integer.parseInt(tbm.getValueAt(i, 5).toString()));
    }

    private void setMotorZero() {
        MTtextFieldMTID.setText(null);

        textFieldVerbrauch.setText("0,0");

        comboBoxGetriebe.setSelectedIndex(0);

        comboBoxKraftstoff.setSelectedIndex(0);

        spinnerHubraum.setValue(0);

        spinnerPS.setValue(0);
    }

    private void setMain(int i, TableModel tbm) {
        MAINtextFieldATID.setText(tbm.getValueAt(i, 0).toString());

        try {
            String sql = "SELECT ASID, MTID FROM auto WHERE ATID = ?";
            PreparedStatement pstm = Objects.requireNonNull(connect()).prepareStatement(sql);
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

    private void setBenutzer(int i, TableModel tbm) {
        BNemail.setText(tbm.getValueAt(i, 0).toString());

        BNnameString = tbm.getValueAt(i, 0).toString();

        BNname.setText(tbm.getValueAt(i, 2).toString());

        BNvorname.setText(tbm.getValueAt(i, 3).toString());

        BNcheckBox.setSelected(tbm.getValueAt(i, 4).toString().equals("1"));
    }
    /* ----------------------------------- Sonstiges --------------------------*/

    private static String getID(PreparedStatement ps) {
        int id = 0;
        try {
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Integer.toString(id);
    }

    private static String encodeFileToBase64Binary(String fileName) throws IOException {
        File file = new File(fileName);
        byte[] encoded = Base64.getEncoder().encode(Files.readAllBytes(file.toPath()));
        return new String(encoded, StandardCharsets.US_ASCII);
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public GUI() {
        spinnerJahr.setEditor(new JSpinner.NumberEditor(spinnerJahr, "#"));
        spinnerHubraum.setEditor(new JSpinner.NumberEditor(spinnerHubraum, "#"));
        spinnerPS.setEditor(new JSpinner.NumberEditor(spinnerPS, "#"));

        ASError.setVisible(false);


        createTableCars();
        createTableAusstattung();
        createTableMotor();
        createTableMain();
        createTableBenutzer();
        eventListenerAuto();
        eventListenerAusstattung();
        eventListenerMotor();
        eventListenerMain();
        eventListenerBenutzer();
    }

}