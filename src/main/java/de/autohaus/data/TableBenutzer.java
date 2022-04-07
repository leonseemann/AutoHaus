package de.autohaus.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import static de.autohaus.model.Connect.connect;

public class TableBenutzer {
    private ResultSet rs;

    public TableBenutzer(){
        try {
            String execute = "SELECT * FROM benutzer";
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
