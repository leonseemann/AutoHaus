package de.autohaus.logic;

import de.autohaus.data.Login;


public class CheckLogin extends Login {
    private String passwort;

    public CheckLogin(String user, String passwort){
        super(user);

        this.passwort = passwort;
    }

    public boolean check(){
        if (this.passwort.equals(getPasswort())){
            return true;
        } else {
            return false;
        }
    }
}
