package de.autohaus.logic;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static de.autohaus.model.Connect.connect;

public class DeleteAusstattung {
    public  DeleteAusstattung(JTextField id, String benutzerID, JTextField AStextFieldASID){
        if (!id.getText().equals("0") && !id.getText().isBlank() && !id.getText().isEmpty()) {
            try {
                String sql = "DELETE FROM ausstattung WHERE ASID = ?";
                PreparedStatement pstm = connect().prepareStatement(sql);
                pstm.setString(1, id.getText());
                pstm.executeUpdate();

                new InsertLogs(benutzerID, "geloescht", "Ausstattung", AStextFieldASID.getText());
            } catch (SQLException ex) {
                new InsertLogs(benutzerID, "geloescht", "Ausstattung", AStextFieldASID.getText(), true);
                ex.printStackTrace();
            }
        } else {
            new InsertLogs(benutzerID, "geloescht", "Ausstattung", true);
        }
    }
}
