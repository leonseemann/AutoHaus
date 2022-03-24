package de.autohaus.logic;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import static de.autohaus.model.Connect.connect;

public class InsertLogs {
    public InsertLogs(String email, String aktivitaet, String art, String ID, String modell) {
        try {
            String sql = "INSERT INTO logs VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstm = connect().prepareStatement(sql);

            pstm.setString(1, email);
            pstm.setString(2, aktivitaet);

            if (art == null){
                pstm.setString(3, null);
            } else {
                pstm.setString(3, art);
            }

            pstm.setTimestamp(4, new Timestamp(new Date().getTime()));

            if (ID == null){
                pstm.setString(5, null);
            } else {
                pstm.setString(5, ID);
            }


            if(modell == null) {
                pstm.setString(6, null);
            } else {
                pstm.setString(6, modell);
            }

            pstm.executeUpdate();
        } catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    public InsertLogs(String email, String aktivitaet, String art, String ID) {
        try {
            String sql = "INSERT INTO logs VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstm = connect().prepareStatement(sql);

            pstm.setString(1, email);
            pstm.setString(2, aktivitaet);

            if (art == null){
                pstm.setString(3, null);
            } else {
                pstm.setString(3, art);
            }

            pstm.setTimestamp(4, new Timestamp(new Date().getTime()));

            if (ID == null){
                pstm.setString(5, null);
            } else {
                pstm.setString(5, ID);
            }


            pstm.setString(6, null);

            pstm.executeUpdate();
        } catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    public InsertLogs(String email, String aktivitaet) {
        try {
            String sql = "INSERT INTO logs (email, aktivitaet, datumzeit) VALUES (?, ?, ?)";
            PreparedStatement pstm = connect().prepareStatement(sql);

            pstm.setString(1, email);
            pstm.setString(2, aktivitaet);
            pstm.setTimestamp(3, new Timestamp(new Date().getTime()));

            pstm.executeUpdate();
        } catch (SQLException ex){
            ex.printStackTrace();
        }
    }
}
