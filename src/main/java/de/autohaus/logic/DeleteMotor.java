package de.autohaus.logic;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static de.autohaus.model.Connect.connect;

public class DeleteMotor {

    public DeleteMotor(JTextField id) {
        try {
            String sql = "DELETE FROM motor WHERE MTID = ?";
            PreparedStatement psm = connect().prepareStatement(sql);
            psm.setString(1, id.getText());
            psm.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
