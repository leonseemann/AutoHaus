package de.autohaus.logic;

import de.autohaus.data.TableMain;

import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DtmTableMain {
    DefaultTableModel dtm;

    public DtmTableMain(){
        try {
            ResultSet rs = new TableMain().getRs();

            this.dtm = new DefaultTableModel(
                    null,
                    new String[]{"ATID", "Typ", "Modell", "Baujahr", "Hersteller", "Kommentar", "FelgenZoll", "FelgenMaterial", "Sitzheizung?", "Lenkradheizung?", "Schiebedach?", "Farbe", "FarbeMaterial", "InnenraumMaterial", "SitzMaterial", "Verbrauch", "Getriebe", "Kraftstoff", "Hubraum", "PS", "Preis"}
            );

            while(rs.next()) {
                String[] data = {rs.getString(1), rs.getString(2),rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), jaNeinBoolean(rs.getString(7)), jaNeinBoolean(rs.getString(8)), jaNeinBoolean(rs.getString(9)), rs.getString(10), rs.getString(11), rs.getString(12), rs.getString(13), rs.getString(14), rs.getString(15), rs.getString(16), rs.getString(17), rs.getString(18), rs.getString(19), rs.getString(20), rs.getString(21)};

                dtm.addRow(data);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public DefaultTableModel getDtm() {
        return dtm;
    }

    private static String jaNeinBoolean(String x) {
        switch (x) {
            case "0":
                return "Nein";
            case "1":
                return "Ja";
            default:
                return "Unbekannt";
        }
    }
}
