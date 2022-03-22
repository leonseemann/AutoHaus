package de.autohaus.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static de.autohaus.model.Connect.connect;

public class TableCars {
    private ResultSet rs;

    public TableCars() {
        try {
            String execute = "SELECT * FROM auto";
            PreparedStatement stm = connect().prepareStatement(execute);
            ResultSet rs;
            rs = stm.executeQuery();

            this.rs = rs;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getRs() {
        return rs;
    }
}
