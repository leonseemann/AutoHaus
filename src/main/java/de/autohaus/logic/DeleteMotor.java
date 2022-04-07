package de.autohaus.logic;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

import static de.autohaus.model.Connect.connect;

public class DeleteMotor {

    public DeleteMotor(JTextField id, String benutzerID, JTextField MTtextFieldMTID) {
        if (!id.getText().equals("0") && !id.getText().isBlank() && !id.getText().isEmpty()) {
            try {
                String sql = "DELETE FROM motor WHERE MTID = ?";
                PreparedStatement psm = Objects.requireNonNull(connect()).prepareStatement(sql);
                psm.setString(1, id.getText());
                psm.executeUpdate();

                new InsertLogs(benutzerID, "geloescht", "Motor", MTtextFieldMTID.getText());
            } catch (SQLException ex) {
                new InsertLogs(benutzerID, "geloescht", "Motor", MTtextFieldMTID.getText(), true, "SQL");
                ex.printStackTrace();
            }
        } else {
            new InsertLogs(benutzerID, "geloescht", "Motor", true, "Zero");
        }
    }

}
