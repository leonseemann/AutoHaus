package de.autohaus.logic;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static de.autohaus.model.Connect.connect;

public class DeleteAusstattung {
    public  DeleteAusstattung(JTextField id){
        try {
            String sql = "DELETE FROM ausstattung WHERE ASID = ?";
            PreparedStatement pstm = connect().prepareStatement(sql);
            pstm.setString(1, id.getText());
            pstm.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
