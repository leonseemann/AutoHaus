package de.autohaus.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import static de.autohaus.model.Connect.connect;

public class TableMotor {
    ResultSet rs;

    public TableMotor() {
        try {
            String execute = "SELECT * FROM motor";
            PreparedStatement stm = Objects.requireNonNull(connect()).prepareStatement(execute);
            this.rs = stm.executeQuery();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public ResultSet getRs() {
        return rs;
    }

}
