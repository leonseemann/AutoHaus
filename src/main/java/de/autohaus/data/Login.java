package de.autohaus.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static de.autohaus.model.Connect.connect;

public class Login {
    private String passwort;

    public Login(String user){
        try {
            String sql = "SELECT passwort FROM benutzer WHERE email = ?";
            PreparedStatement pstm = connect().prepareStatement(sql);
            pstm.setString(1, user);
            ResultSet rs = pstm.executeQuery();
            rs.next();

            this.passwort = rs.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getPasswort() {
        return passwort;
    }
}
