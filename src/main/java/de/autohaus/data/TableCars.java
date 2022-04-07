package de.autohaus.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import static de.autohaus.model.Connect.connect;

public class TableCars {
    private ResultSet rs;

    public TableCars() {
        try {
            String execute = "SELECT * FROM auto";
            PreparedStatement stm = Objects.requireNonNull(connect()).prepareStatement(execute);
            this.rs = stm.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getRs() {
        return rs;
    }
}
