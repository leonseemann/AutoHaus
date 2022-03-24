package de.autohaus.logic;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static de.autohaus.model.Connect.connect;

public class DeleteMain {

    private PreparedStatement pstm;

    public DeleteMain(JTextField id) {
        try {
            String sql = "DELETE auto, motor, ausstattung FROM auto RIGHT JOIN motor ON auto.MTID = motor.MTID RIGHT JOIN ausstattung ON auto.ASID = ausstattung.ASID WHERE auto.ATID = ?";
            this.pstm = connect().prepareStatement(sql);
            this.pstm.setString(1, id.getText());
            this.pstm.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public PreparedStatement getPstm() {
        return pstm;
    }
}
