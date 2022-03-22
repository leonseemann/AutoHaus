package de.autohaus.logic;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static de.autohaus.model.Connect.connect;

public class UpdateMotor {
    PreparedStatement pstm;

    public UpdateMotor() {
        try {
            String sqlAusstattung = "UPDATE ausstattung SET FelgenZoll = ?, Felgenmaterial = ?, Sitzheizung = ?, Lenkradheizung = ?, Schiebedach = ?, Farbe = ?, FarbeMaterial = ?, InnenraumMaterial = ?, SitzMaterial = ? WHERE ASID = ?";
            this.pstm = connect().prepareStatement(sqlAusstattung);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public PreparedStatement getPstm() {
        return pstm;
    }
}
