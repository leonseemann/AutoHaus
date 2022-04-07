package de.autohaus.logic;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import static de.autohaus.model.Connect.connect;

public class GetBenutzerAdmin {

    private ResultSet rs;

    public GetBenutzerAdmin(String email){
        try {
            String execute = "SELECT isAdmin FROM benutzer WHERE email = ?";
            PreparedStatement stm = Objects.requireNonNull(connect()).prepareStatement(execute);

            stm.setString(1, email);

            this.rs = stm.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Boolean getAdmin() {
        try {
            rs.next();
            return rs.getBoolean(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
