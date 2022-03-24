package de.autohaus.logic;

import de.autohaus.data.Login;


public class CheckLogin extends Login {
    private final String passwort;

    public CheckLogin(String user, String passwort){
        super(user);

        this.passwort = passwort;
    }

    public boolean check(){
        return this.passwort.equals(getPasswort());
    }
}
