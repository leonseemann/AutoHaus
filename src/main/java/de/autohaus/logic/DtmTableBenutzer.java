package de.autohaus.logic;

import de.autohaus.data.TableBenutzer;

import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DtmTableBenutzer {
    private DefaultTableModel dtm;

    public DtmTableBenutzer(){
        try {
            ResultSet rs = new TableBenutzer().getRs();

            this.dtm = new DefaultTableModel(
                    null,
                    new String[]{"E-Mail", "Passwort", "Name", "Vorname", "isAdmin"}
                    );

            while (rs.next()) {
                String[] data = {rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)};
                dtm.addRow(data);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public DefaultTableModel getDtm() {
        return dtm;
    }
}
