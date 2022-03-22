package de.autohaus.data;

import de.autohaus.logic.DtmTableMotor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static de.autohaus.model.Connect.connect;

public class TableMotor {
    ResultSet rs;

    public TableMotor() {
        try {
            String execute = "SELECT * FROM motor";
            PreparedStatement stm = connect().prepareStatement(execute);
            ResultSet rs = stm.executeQuery();

            this.rs = rs;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public ResultSet getRs() {
        return rs;
    }

    private void test() {
        DtmTableMotor dtm = new DtmTableMotor();
    }
}
