package de.autohaus.logic;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static de.autohaus.model.Connect.connect;

public class DeleteAuto {

    public DeleteAuto(JTextField id) {
        try {
            String sql = "DELETE auto, motor, ausstattung FROM auto RIGHT JOIN motor ON auto.MTID = motor.MTID RIGHT JOIN ausstattung ON auto.ASID = ausstattung.ASID WHERE auto.ATID = ?";
            PreparedStatement psm = connect().prepareStatement(sql);
            psm.setString(1, id.getText());
            psm.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
