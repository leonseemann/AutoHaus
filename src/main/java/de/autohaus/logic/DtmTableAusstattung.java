package de.autohaus.logic;

import de.autohaus.data.TableAusstattung;

import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DtmTableAusstattung {
    private DefaultTableModel dtm;

    public DtmTableAusstattung(){
        try {
            ResultSet rs = new TableAusstattung().getRsTableAusstattung();

            this.dtm = new DefaultTableModel(
                    null,
                    new String[]{"ASID", "FelgenZoll", "FelgenMaterial", "Sitzheizung?", "Lenkradheizung?", "Schiebedach?", "Farbe", "FarbeMaterial", "InnenraumMaterial", "SitzMaterial"}
            );

            while (rs.next()) {
                String[] data = {rs.getString(1), rs.getString(2), rs.getString(3), jaNeinBoolean(rs.getString(4)), jaNeinBoolean(rs.getString(5)), jaNeinBoolean(rs.getString(6)), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10)};

                dtm.addRow(data);
            }
        } catch (SQLException ex){
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
