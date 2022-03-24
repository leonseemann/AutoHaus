package de.autohaus.logic;

import java.sql.*;
import java.util.Date;

import static de.autohaus.model.Connect.connect;

public class InsertLogs {

    public InsertLogs(String email, String aktivitaet, String art, String ID, String modell) {
        try {
            String sql = "INSERT INTO logs (email, aktivitaet, art, datumzeit, ID, modell, error) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstm = connect().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstm.setString(1, email);
            pstm.setString(2, aktivitaet);

            pstm.setString(3, art);

            pstm.setTimestamp(4, new Timestamp(new Date().getTime()));

            pstm.setString(5, ID);


            pstm.setString(6, modell);

            pstm.setBoolean(7, false);

            pstm.executeUpdate();
        } catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    public InsertLogs(String email, String aktivitaet, String art, String ID) {
        try {
            String sql = "INSERT INTO logs (email, aktivitaet, art, datumzeit, ID, modell, error) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstm = connect().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstm.setString(1, email);
            pstm.setString(2, aktivitaet);

            pstm.setString(3, art);

            pstm.setTimestamp(4, new Timestamp(new Date().getTime()));

            pstm.setString(5, ID);

            pstm.setString(6, null);

            pstm.setBoolean(7, false);

            pstm.executeUpdate();
        } catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    public InsertLogs(String email, String aktivitaet, String art, String ID, Boolean error){
        try {
            String sql = "INSERT INTO logs (email, aktivitaet, art,datumzeit, ID, error) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstm = connect().prepareStatement(sql);

            pstm.setString(1, email);
            pstm.setString(2, aktivitaet);
            pstm.setString(3, art);
            pstm.setTimestamp(4, new Timestamp(new Date().getTime()));
            pstm.setString(5, ID);
            pstm.setBoolean(6, error);

            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public InsertLogs(String email, String aktivitaet) {
        try {
            String sql = "INSERT INTO logs (email, aktivitaet, datumzeit, error) VALUES (?, ?, ?, ?)";
            PreparedStatement pstm = connect().prepareStatement(sql);

            pstm.setString(1, email);
            pstm.setString(2, aktivitaet);
            pstm.setTimestamp(3, new Timestamp(new Date().getTime()));
            pstm.setBoolean(4, false);

            pstm.executeUpdate();
        } catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    public InsertLogs(String email, String aktivitaet, String art, Boolean error) {
        try {
            String sql = "INSERT INTO logs (email, aktivitaet, art, datumzeit, error) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstm = connect().prepareStatement(sql);

            pstm.setString(1, email);
            pstm.setString(2, aktivitaet);
            pstm.setString(3, art);
            pstm.setTimestamp(4, new Timestamp(new Date().getTime()));
            pstm.setBoolean(5, error);

            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static String getID(PreparedStatement ps){
        int id = 0;
        try {
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                id = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Integer.toString(id);
    }
}
