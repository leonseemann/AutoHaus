package de.autohaus.logic;

import java.math.BigInteger;

import de.autohaus.data.Login;
import de.autohaus.model.RSA;


public class CheckLogin extends Login {
    private final BigInteger passwort;

    public CheckLogin(String user, String passwort){
        super(user);

        this.passwort = new RSA().encrypt(passwort);
    }

    public boolean check(){
        System.out.println(this.passwort.equals(getPasswort()));
        return this.passwort.equals(getPasswort());
    }
}
