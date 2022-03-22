package de.autohaus.logic;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static de.autohaus.model.Connect.connect;

public class UpdateAuto {
    PreparedStatement pstm;

    public UpdateAuto() {
        try {
            String pstm = "UPDATE auto SET Typ = ?, Baujahr = ?, Hersteller = ?, Kommentar = ?, ASID = ?, MTID = ?, Preis = ?, bild = ? WHERE ATID = ?;";
            this.pstm = connect().prepareStatement(pstm);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public PreparedStatement getPstm() {
        return pstm;
    }
}
