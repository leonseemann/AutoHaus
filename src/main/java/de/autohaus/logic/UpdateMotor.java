package de.autohaus.logic;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

import static de.autohaus.model.Connect.connect;

public class UpdateMotor {
    PreparedStatement pstm;

    public UpdateMotor() {
        try {
            String sqlAusstattung = "UPDATE motor SET verbrauch = ?, getriebe = ?, kraftstoff = ?, hubraum = ?, ps = ? WHERE MTID = ?";
            this.pstm = Objects.requireNonNull(connect()).prepareStatement(sqlAusstattung);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public PreparedStatement getPstm() {
        return pstm;
    }
}
