package de.autohaus.logic;

import java.sql.*;
import java.util.Date;

import static de.autohaus.model.Connect.connect;

public class InsertLogs {

    public InsertLogs(String email, String aktivitaet, String wo, String ID, String modell) {
        try {
            String sql = "INSERT INTO logs (email, aktivitaet, wo, art,datumzeit, ID, modell, error) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstm = connect().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstm.setString(1, email);
            pstm.setString(2, aktivitaet);

            pstm.setString(3, wo);

            pstm.setString(4, "erfolgreich");

            pstm.setTimestamp(5, new Timestamp(new Date().getTime()));

            pstm.setString(6, ID);


            pstm.setString(7, modell);

            pstm.setBoolean(8, false);

            pstm.executeUpdate();
        } catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    public InsertLogs(String email, String aktivitaet, String wo, String ID) {
        try {
            String sql = "INSERT INTO logs (email, aktivitaet, wo, art, datumzeit, ID, modell, error) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstm = connect().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstm.setString(1, email);
            pstm.setString(2, aktivitaet);

            pstm.setString(3, wo);

            pstm.setString(4, "erfolgreich");

            pstm.setTimestamp(5, new Timestamp(new Date().getTime()));

            pstm.setString(6, ID);

            pstm.setString(7, null);

            pstm.setBoolean(8, false);

            pstm.executeUpdate();
        } catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    public InsertLogs(String email, String aktivitaet, String wo, String ID, Boolean error, String art_error){
        try {
            String sql = "INSERT INTO logs (email, aktivitaet, wo, art, datumzeit, ID, error, art_error) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstm = connect().prepareStatement(sql);

            pstm.setString(1, email);
            pstm.setString(2, aktivitaet);
            pstm.setString(3, wo);
            pstm.setString(4, "fehlgeschlagen");
            pstm.setTimestamp(5, new Timestamp(new Date().getTime()));
            pstm.setString(6, ID);
            pstm.setBoolean(7, error);
            pstm.setString(8, art_error);

            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public InsertLogs(String email, String aktivitaet, String wo) {
        try {
            String sql = "INSERT INTO logs (email, aktivitaet, wo, art,datumzeit, error) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstm = connect().prepareStatement(sql);

            pstm.setString(1, email);
            pstm.setString(2, aktivitaet);
            pstm.setString(3, wo);
            pstm.setString(4, "erfolgreich");
            pstm.setTimestamp(5, new Timestamp(new Date().getTime()));
            pstm.setBoolean(6, false);

            pstm.executeUpdate();
        } catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    public InsertLogs(String email, String aktivitaet, String wo, Boolean error, String art_error) {
        try {
            String sql = "INSERT INTO logs (email, aktivitaet, wo, art,datumzeit, error, art_error) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstm = connect().prepareStatement(sql);

            pstm.setString(1, email);
            pstm.setString(2, aktivitaet);
            pstm.setString(3, wo);
            pstm.setString(4, "fehlgeschlagen");
            pstm.setTimestamp(5, new Timestamp(new Date().getTime()));
            pstm.setBoolean(6, error);
            pstm.setString(7, art_error);

            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public InsertLogs(String aktivitaet, String art) {
        try {
            String sql = "INSERT INTO logs (aktivitaet, art, datumzeit, error) VALUES (?, ?, ?, ?)";
            PreparedStatement pstm = connect().prepareStatement(sql);

            pstm.setString(1, aktivitaet);
            pstm.setString(2, art);
            pstm.setTimestamp(3, new Timestamp(new Date().getTime()));
            pstm.setBoolean(4, false);

            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
