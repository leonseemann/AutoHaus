package de.autohaus.logic;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

import static de.autohaus.model.Connect.connect;

public class UpdateAusstattung {
    PreparedStatement pstm;

    public UpdateAusstattung() {
        try {
            String sql = "UPDATE ausstattung SET FelgenZoll = ?, Felgenmaterial = ?, Sitzheizung = ?, Lenkradheizung = ?, Schiebedach = ?, Farbe = ?, FarbeMaterial = ?, InnenraumMaterial = ?, SitzMaterial = ? WHERE ASID = ?";
            this.pstm = Objects.requireNonNull(connect()).prepareStatement(sql);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public PreparedStatement getPstm() {
        return pstm;
    }
}
