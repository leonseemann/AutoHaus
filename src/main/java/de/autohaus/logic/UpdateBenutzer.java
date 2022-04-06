package de.autohaus.logic;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static de.autohaus.model.Connect.connect;

public class UpdateBenutzer {

    PreparedStatement pstm;

    public UpdateBenutzer() {
        try {
            String pstm = "UPDATE benutzer SET passwort = ?, name = ?, vorname = ?, isAdmin = ? WHERE benutzer.email = ?";
            this.pstm = connect().prepareStatement(pstm);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public PreparedStatement getPstm() {
        return pstm;
    }

}
