package de.autohaus.data;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import static de.autohaus.model.Connect.connect;

public class Login {
    private BigInteger passwort;

    public Login(String user){
        try {
            String sql = "SELECT passwort FROM benutzer WHERE email = ?";
            PreparedStatement pstm = Objects.requireNonNull(connect()).prepareStatement(sql);
            pstm.setString(1, user);
            ResultSet rs = pstm.executeQuery();
            rs.next();

            this.passwort = new BigInteger(rs.getString(1));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public BigInteger getPasswort() {
        return passwort;
    }
}
