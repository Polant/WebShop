package com.polant.webshop;

/**
 * Created by Antony on 05.03.2016.
 */
public class Client {

    private int id;
    private String login;
    private String password;
    private String email;
    private boolean isActive;

    public Client(int id, String login, String password, String email, boolean isActive) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.email = email;
        this.isActive = isActive;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
