package de.autohaus.logic;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import static de.autohaus.model.Connect.connect;

public class InsertAuto {
    private PreparedStatement pstm;

    public InsertAuto() {
        try {
            String sql = "INSERT INTO auto (Typ, Baujahr, Hersteller, Kommentar, ASID, MTID, Preis, bild) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
            this.pstm = connect().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public PreparedStatement getPstm() {
        return pstm;
    }
}
