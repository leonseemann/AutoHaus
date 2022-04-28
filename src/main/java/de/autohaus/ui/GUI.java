package de.autohaus.ui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import de.autohaus.logic.*;
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

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        rootPanel = new JPanel();
        rootPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        rootPanel.setPreferredSize(new Dimension(857, 491));
        tabbedPane1 = new JTabbedPane();
        tabbedPane1.setEnabled(true);
        rootPanel.add(tabbedPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Übersicht", panel1);
        final JScrollPane scrollPane1 = new JScrollPane();
        panel1.add(scrollPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        tableMain = new JTable();
        scrollPane1.setViewportView(tableMain);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        MAINloeschenButton2 = new JButton();
        MAINloeschenButton2.setText("Loeschen");
        panel2.add(MAINloeschenButton2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        MAINaktualisierenButton = new JButton();
        MAINaktualisierenButton.setText("Reload");
        panel2.add(MAINaktualisierenButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        MAINtextFieldATID = new JTextField();
        MAINtextFieldATID.setEditable(false);
        panel3.add(MAINtextFieldATID, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        MAINtextFieldASID = new JTextField();
        MAINtextFieldASID.setEditable(false);
        panel3.add(MAINtextFieldASID, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        MAINtextFieldMTID = new JTextField();
        MAINtextFieldMTID.setEditable(false);
        panel3.add(MAINtextFieldMTID, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("AutoID");
        panel3.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        final JLabel label2 = new JLabel();
        label2.setText("AusstattungID");
        panel3.add(label2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("MotorID");
        panel3.add(label3, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JSplitPane splitPane1 = new JSplitPane();
        tabbedPane1.addTab("Autos", splitPane1);
        final JScrollPane scrollPane2 = new JScrollPane();
        splitPane1.setRightComponent(scrollPane2);
        tableCars = new JTable();
        scrollPane2.setViewportView(tableCars);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(13, 2, new Insets(0, 0, 0, 0), -1, -1));
        splitPane1.setLeftComponent(panel4);
        comboBoxTyp = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        defaultComboBoxModel1.addElement("");
        defaultComboBoxModel1.addElement("Kombi");
        defaultComboBoxModel1.addElement("Coupe");
        defaultComboBoxModel1.addElement("Sportwagen");
        defaultComboBoxModel1.addElement("Limo");
        defaultComboBoxModel1.addElement("SUV");
        defaultComboBoxModel1.addElement("Cabrio");
        comboBoxTyp.setModel(defaultComboBoxModel1);
        panel4.add(comboBoxTyp, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        comboBoxHerseller = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel2 = new DefaultComboBoxModel();
        defaultComboBoxModel2.addElement("");
        defaultComboBoxModel2.addElement("BMW");
        defaultComboBoxModel2.addElement("Mercedes");
        defaultComboBoxModel2.addElement("VW");
        defaultComboBoxModel2.addElement("Audi");
        defaultComboBoxModel2.addElement("Opel");
        defaultComboBoxModel2.addElement("Nissan");
        defaultComboBoxModel2.addElement("Porsche");
        defaultComboBoxModel2.addElement("Lamborghini");
        defaultComboBoxModel2.addElement("Smart");
        defaultComboBoxModel2.addElement("Ferrari");
        defaultComboBoxModel2.addElement("Toyota");
        defaultComboBoxModel2.addElement("Tesla");
        comboBoxHerseller.setModel(defaultComboBoxModel2);
        panel4.add(comboBoxHerseller, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        kommentarTextArea = new JTextArea();
        kommentarTextArea.setText("");
        panel4.add(kommentarTextArea, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel4.add(panel5, new GridConstraints(11, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        ATeinfuegenButton = new JButton();
        ATeinfuegenButton.setText("Einfuegen");
        panel5.add(ATeinfuegenButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ATaktialisierenButton = new JButton();
        ATaktialisierenButton.setText("Aktialisieren");
        panel5.add(ATaktialisierenButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ATloeschenButton = new JButton();
        ATloeschenButton.setText("Loeschen");
        panel5.add(ATloeschenButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel4.add(spacer1, new GridConstraints(12, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel4.add(spacer2, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel4.add(panel6, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        spinnerJahr = new JSpinner();
        panel6.add(spinnerJahr, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Typ");
        panel4.add(label4, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("AutoID");
        panel4.add(label5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("Baujahr");
        panel4.add(label6, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText("Hersteller");
        panel4.add(label7, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label8 = new JLabel();
        label8.setText("Kommentar");
        panel4.add(label8, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label9 = new JLabel();
        label9.setText("AusstattungID");
        panel4.add(label9, new GridConstraints(8, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label10 = new JLabel();
        label10.setText("MotorID");
        panel4.add(label10, new GridConstraints(9, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ATtextFieldATID = new JTextField();
        ATtextFieldATID.setEditable(false);
        ATtextFieldATID.setText("");
        panel4.add(ATtextFieldATID, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        textFieldPreis = new JTextField();
        panel4.add(textFieldPreis, new GridConstraints(7, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label11 = new JLabel();
        label11.setText("Preis");
        panel4.add(label11, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ATtextFieldASID = new JTextField();
        ATtextFieldASID.setEditable(false);
        panel4.add(ATtextFieldASID, new GridConstraints(8, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        ATtextFieldMTID = new JTextField();
        ATtextFieldMTID.setEditable(false);
        panel4.add(ATtextFieldMTID, new GridConstraints(9, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label12 = new JLabel();
        label12.setText("Bild");
        panel4.add(label12, new GridConstraints(10, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel4.add(panel7, new GridConstraints(10, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        ATbrowse = new JButton();
        ATbrowse.setText("Browse");
        panel7.add(ATbrowse, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ATbrowseLink = new JTextField();
        ATbrowseLink.setEditable(false);
        ATbrowseLink.setEnabled(true);
        panel7.add(ATbrowseLink, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        ATtextFieldModell = new JTextField();
        ATtextFieldModell.setText("");
        panel4.add(ATtextFieldModell, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label13 = new JLabel();
        label13.setText("Modell");
        panel4.add(label13, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JSplitPane splitPane2 = new JSplitPane();
        tabbedPane1.addTab("Ausstattung", splitPane2);
        final JScrollPane scrollPane3 = new JScrollPane();
        splitPane2.setRightComponent(scrollPane3);
        tableAusstattung = new JTable();
        scrollPane3.setViewportView(tableAusstattung);
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new GridLayoutManager(14, 2, new Insets(0, 0, 0, 0), -1, -1));
        splitPane2.setLeftComponent(panel8);
        AStextFieldASID = new JTextField();
        AStextFieldASID.setEditable(false);
        panel8.add(AStextFieldASID, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel8.add(spacer3, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JLabel label14 = new JLabel();
        label14.setText("AusstattungID");
        panel8.add(label14, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        spinnerZoll = new JSpinner();
        panel8.add(spinnerZoll, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label15 = new JLabel();
        label15.setText("Felgen(Zoll)");
        panel8.add(label15, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        panel8.add(spacer4, new GridConstraints(12, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        comboBoxFelgenMaterial = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel3 = new DefaultComboBoxModel();
        defaultComboBoxModel3.addElement("");
        defaultComboBoxModel3.addElement("Aluminium");
        defaultComboBoxModel3.addElement("Stahl");
        defaultComboBoxModel3.addElement("Carbon");
        defaultComboBoxModel3.addElement("Magnesium");
        defaultComboBoxModel3.addElement("Silizium");
        defaultComboBoxModel3.addElement("Mangan");
        comboBoxFelgenMaterial.setModel(defaultComboBoxModel3);
        panel8.add(comboBoxFelgenMaterial, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label16 = new JLabel();
        label16.setText("FelgenMaterial");
        panel8.add(label16, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        checkBoxSitzheizung = new JCheckBox();
        checkBoxSitzheizung.setText("");
        panel8.add(checkBoxSitzheizung, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        checkBoxLenkradheizung = new JCheckBox();
        checkBoxLenkradheizung.setText("");
        panel8.add(checkBoxLenkradheizung, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        checkBoxSchiebedach = new JCheckBox();
        checkBoxSchiebedach.setText("");
        panel8.add(checkBoxSchiebedach, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textFieldFarbe = new JTextField();
        panel8.add(textFieldFarbe, new GridConstraints(7, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        comboBoxFarbMaterial = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel4 = new DefaultComboBoxModel();
        defaultComboBoxModel4.addElement("");
        defaultComboBoxModel4.addElement("Matt");
        defaultComboBoxModel4.addElement("Glanz");
        defaultComboBoxModel4.addElement("Perleffekt");
        comboBoxFarbMaterial.setModel(defaultComboBoxModel4);
        panel8.add(comboBoxFarbMaterial, new GridConstraints(8, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        comboBoxInnenraumMaterial = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel5 = new DefaultComboBoxModel();
        defaultComboBoxModel5.addElement("");
        defaultComboBoxModel5.addElement("Carbon");
        defaultComboBoxModel5.addElement("Alkantara");
        defaultComboBoxModel5.addElement("Holz");
        defaultComboBoxModel5.addElement("Plastik");
        comboBoxInnenraumMaterial.setModel(defaultComboBoxModel5);
        panel8.add(comboBoxInnenraumMaterial, new GridConstraints(9, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        comboBoxSitzMaterial = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel6 = new DefaultComboBoxModel();
        defaultComboBoxModel6.addElement("");
        defaultComboBoxModel6.addElement("Stoff");
        defaultComboBoxModel6.addElement("Leder");
        defaultComboBoxModel6.addElement("Alkantara");
        comboBoxSitzMaterial.setModel(defaultComboBoxModel6);
        panel8.add(comboBoxSitzMaterial, new GridConstraints(10, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label17 = new JLabel();
        label17.setText("Sitzheizung");
        panel8.add(label17, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label18 = new JLabel();
        label18.setText("Lenkradheizung");
        panel8.add(label18, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label19 = new JLabel();
        label19.setText("Schiebedach");
        panel8.add(label19, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label20 = new JLabel();
        label20.setText("Farbe");
        panel8.add(label20, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label21 = new JLabel();
        label21.setText("FarbMaterial");
        panel8.add(label21, new GridConstraints(8, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label22 = new JLabel();
        label22.setText("InnenraumMaterial");
        panel8.add(label22, new GridConstraints(9, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label23 = new JLabel();
        label23.setText("SitzMaterial");
        panel8.add(label23, new GridConstraints(10, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel9 = new JPanel();
        panel9.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel8.add(panel9, new GridConstraints(11, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        ASeinfuegenButton = new JButton();
        ASeinfuegenButton.setText("Einfuegen");
        panel9.add(ASeinfuegenButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ASaktualisierenButton = new JButton();
        ASaktualisierenButton.setText("Aktualisieren");
        panel9.add(ASaktualisierenButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ASloeschenButton = new JButton();
        ASloeschenButton.setText("Loeschen");
        panel9.add(ASloeschenButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ASErrorCode = new JLabel();
        ASErrorCode.setText("");
        panel8.add(ASErrorCode, new GridConstraints(13, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ASError = new JLabel();
        ASError.setEnabled(true);
        ASError.setText("Error:");
        panel8.add(ASError, new GridConstraints(13, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JSplitPane splitPane3 = new JSplitPane();
        tabbedPane1.addTab("Motor", splitPane3);
        final JScrollPane scrollPane4 = new JScrollPane();
        splitPane3.setRightComponent(scrollPane4);
        tableMotor = new JTable();
        scrollPane4.setViewportView(tableMotor);
        final JPanel panel10 = new JPanel();
        panel10.setLayout(new GridLayoutManager(9, 2, new Insets(0, 0, 0, 0), -1, -1));
        splitPane3.setLeftComponent(panel10);
        final Spacer spacer5 = new Spacer();
        panel10.add(spacer5, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer6 = new Spacer();
        panel10.add(spacer6, new GridConstraints(8, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        MTtextFieldMTID = new JTextField();
        MTtextFieldMTID.setEditable(false);
        panel10.add(MTtextFieldMTID, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        textFieldVerbrauch = new JTextField();
        textFieldVerbrauch.setText("0,0");
        panel10.add(textFieldVerbrauch, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label24 = new JLabel();
        label24.setText("MotorID");
        panel10.add(label24, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label25 = new JLabel();
        label25.setText("Verbrauch");
        panel10.add(label25, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        comboBoxGetriebe = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel7 = new DefaultComboBoxModel();
        defaultComboBoxModel7.addElement("");
        defaultComboBoxModel7.addElement("Manuell");
        defaultComboBoxModel7.addElement("Automatik");
        defaultComboBoxModel7.addElement("Halb-Automatik");
        comboBoxGetriebe.setModel(defaultComboBoxModel7);
        panel10.add(comboBoxGetriebe, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label26 = new JLabel();
        label26.setText("Getriebe");
        panel10.add(label26, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        comboBoxKraftstoff = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel8 = new DefaultComboBoxModel();
        defaultComboBoxModel8.addElement("");
        defaultComboBoxModel8.addElement("Diesel");
        defaultComboBoxModel8.addElement("Benzin");
        defaultComboBoxModel8.addElement("Strom");
        defaultComboBoxModel8.addElement("Gas");
        comboBoxKraftstoff.setModel(defaultComboBoxModel8);
        panel10.add(comboBoxKraftstoff, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label27 = new JLabel();
        label27.setText("Kraftstoff");
        panel10.add(label27, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        spinnerHubraum = new JSpinner();
        panel10.add(spinnerHubraum, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        spinnerPS = new JSpinner();
        panel10.add(spinnerPS, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label28 = new JLabel();
        label28.setText("Hubraum");
        panel10.add(label28, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label29 = new JLabel();
        label29.setText("PS");
        panel10.add(label29, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel11 = new JPanel();
        panel11.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel10.add(panel11, new GridConstraints(7, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        MTeinfuegenButton = new JButton();
        MTeinfuegenButton.setText("Einfuegen");
        panel11.add(MTeinfuegenButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        MTaktualisierenButton = new JButton();
        MTaktualisierenButton.setText("Aktualisieren");
        panel11.add(MTaktualisierenButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        MTloeschenButton = new JButton();
        MTloeschenButton.setText("Loeschen");
        panel11.add(MTloeschenButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        SplitBenutzer = new JSplitPane();
        SplitBenutzer.setEnabled(true);
        tabbedPane1.addTab("Benutzer", SplitBenutzer);
        tabbedPane1.setEnabledAt(4, false);
        final JPanel panel12 = new JPanel();
        panel12.setLayout(new GridLayoutManager(8, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel12.setEnabled(true);
        SplitBenutzer.setLeftComponent(panel12);
        final Spacer spacer7 = new Spacer();
        panel12.add(spacer7, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer8 = new Spacer();
        panel12.add(spacer8, new GridConstraints(7, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        BNemail = new JTextField();
        panel12.add(BNemail, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label30 = new JLabel();
        label30.setText("E-Mail");
        panel12.add(label30, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label31 = new JLabel();
        label31.setText("Passwort");
        panel12.add(label31, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        BNpassword = new JPasswordField();
        panel12.add(BNpassword, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label32 = new JLabel();
        label32.setText("Name");
        panel12.add(label32, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        BNname = new JTextField();
        BNname.setText("");
        panel12.add(BNname, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        BNvorname = new JTextField();
        panel12.add(BNvorname, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label33 = new JLabel();
        label33.setText("Vorname");
        panel12.add(label33, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        BNcheckBox = new JCheckBox();
        BNcheckBox.setText("");
        panel12.add(BNcheckBox, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label34 = new JLabel();
        label34.setText("isAdmin?");
        panel12.add(label34, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel13 = new JPanel();
        panel13.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel12.add(panel13, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        einfuegenButton = new JButton();
        einfuegenButton.setText("Einfügen");
        panel13.add(einfuegenButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        BNaktualisierenButton = new JButton();
        BNaktualisierenButton.setText("Aktualisieren");
        panel13.add(BNaktualisierenButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane5 = new JScrollPane();
        SplitBenutzer.setRightComponent(scrollPane5);
        tableBenutzer = new JTable();
        scrollPane5.setViewportView(tableBenutzer);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return rootPanel;
    }
}