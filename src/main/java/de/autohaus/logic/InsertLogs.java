package de.autohaus.logic;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import static de.autohaus.model.Connect.connect;

public class InsertLogs {
    public InsertLogs(String email, String aktivitaet, String art, String ID) {
        try {
            String sql = "INSERT INTO logs VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstm = connect().prepareStatement(sql);

            pstm.setString(1, email);
            pstm.setString(2, aktivitaet);
            pstm.setString(3, art);
            pstm.setTimestamp(4, new Timestamp(new Date().getTime()));
            pstm.setString(5, ID);

            pstm.executeUpdate();
        } catch (SQLException ex){
            ex.printStackTrace();
        }
    }
}
