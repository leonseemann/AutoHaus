package de.autohaus.logic;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static de.autohaus.model.Connect.connect;

public class DeleteMain {

    private PreparedStatement pstm;

    public DeleteMain(JTextField id, String benutzerID, JTextField MAINtextFieldATID, String modellMain) {
        if (!id.getText().equals("0") && !id.getText().isBlank() && !id.getText().isEmpty()) {
            try {
                String sql = "DELETE auto, motor, ausstattung FROM auto RIGHT JOIN motor ON auto.MTID = motor.MTID RIGHT JOIN ausstattung ON auto.ASID = ausstattung.ASID WHERE auto.ATID = ?";
                this.pstm = connect().prepareStatement(sql);
                this.pstm.setString(1, id.getText());
                this.pstm.executeUpdate();

                new InsertLogs(benutzerID, "geloescht", "Main", MAINtextFieldATID.getText(), modellMain);
            } catch (SQLException ex) {
                new InsertLogs(benutzerID, "geloescht", "Main", MAINtextFieldATID.getText(), true);
                ex.printStackTrace();
            }
        } else {
            new InsertLogs(benutzerID, "geloescht", "Main", true);
        }
    }

    public PreparedStatement getPstm() {
        return pstm;
    }
}
