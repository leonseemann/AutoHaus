package de.autohaus.logic;

import de.autohaus.data.TableMotor;

import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DtmTableMotor {
    DefaultTableModel dtm;

    public DtmTableMotor() {
        try {
            ResultSet rs = new TableMotor().getRs();

            DefaultTableModel dtm = new DefaultTableModel(
                    null,
                    new String[]{"MTID", "Verbrauch", "Getriebe", "Kraftstoff", "Hubraum", "PS"}
            );


            while (rs.next()) {
                String[] data = {rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6)};
                dtm.addRow(data);
            }

            this.dtm = dtm;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public DefaultTableModel getDtm() {
        return dtm;
    }
}
