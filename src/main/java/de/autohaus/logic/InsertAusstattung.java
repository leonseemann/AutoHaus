package de.autohaus.logic;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import static de.autohaus.model.Connect.connect;

public class InsertAusstattung {
    PreparedStatement pstm;

    public InsertAusstattung() {
        try {
            String sql = "INSERT INTO ausstattung (FelgenZoll, Felgenmaterial, Sitzheizung, Lenkradheizung, Schiebedach, Farbe, FarbeMaterial, InnenraumMaterial, SitzMaterial) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
            this.pstm = connect().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public PreparedStatement getPstm() {
        return pstm;
    }
}
