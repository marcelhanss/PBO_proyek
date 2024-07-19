package org.example.uas.beans;

public class Login {
    private String nip, password;

    public Login() {
    }

    public Login(String nip, String password) {
        this.nip = nip;
        this.password = password;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
