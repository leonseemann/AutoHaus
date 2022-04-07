package de.autohaus.logic;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

import static de.autohaus.model.Connect.connect;

public class UpdateAuto {
    PreparedStatement pstm;

    public UpdateAuto() {
        try {
            String pstm = "UPDATE auto SET Typ = ?, Modell = ?, Baujahr = ?, Hersteller = ?, Kommentar = ?, Preis = ?, bild = ? WHERE ATID = ?;";
            this.pstm = Objects.requireNonNull(connect()).prepareStatement(pstm);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public PreparedStatement getPstm() {
        return pstm;
    }
}
