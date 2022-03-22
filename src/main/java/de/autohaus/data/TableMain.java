package de.autohaus.data;

import de.autohaus.logic.DtmTableMain;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static de.autohaus.model.Connect.connect;

public class TableMain {
    ResultSet rs;

    public TableMain(){
        try {
            String sql = "SELECT ATID, Typ, Baujahr, Hersteller, Kommentar, FelgenZoll, FelgenMaterial, Sitzheizung, Lenkradheizung, Schiebedach, Farbe, FarbeMaterial, InnenraumMaterial, SitzMaterial, Verbrauch, Getriebe, Kraftstoff, Hubraum, PS, Preis FROM auto JOIN ausstattung ON auto.ASID = ausstattung.ASID JOIN motor ON auto.MTID = motor.MTID ORDER BY auto.hersteller,auto.typ ASC;";
            PreparedStatement stm = connect().prepareStatement(sql);
            ResultSet rs = stm.executeQuery();

            this.rs = rs;
        } catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    public ResultSet getRs() {
        return rs;
    }

    private void test(){
        DtmTableMain dtm = new DtmTableMain();
    }
}
