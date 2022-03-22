package de.autohaus.logic;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import static de.autohaus.model.Connect.connect;

public class InsertMotor {
    private PreparedStatement pstm;

    public InsertMotor() {
        try {
            String sql = "INSERT INTO motor (Verbrauch, Getriebe, Kraftstoff, Hubraum, PS) VALUES (?, ?, ?, ?, ?);";
            this.pstm = connect().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public PreparedStatement getPstm() {
        return pstm;
    }
}
