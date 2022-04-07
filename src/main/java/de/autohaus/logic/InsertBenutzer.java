package de.autohaus.logic;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

import static de.autohaus.model.Connect.connect;

public class InsertBenutzer {

    private PreparedStatement pstm;

    public InsertBenutzer() {
        try {
            String sql = "INSERT INTO benutzer VALUES (?, ?, ?, ?, ?);";
            this.pstm = Objects.requireNonNull(connect()).prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public PreparedStatement getPstm() {
        return pstm;
    }

}
