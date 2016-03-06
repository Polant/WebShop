package com.polant.webshop.model;

/**
 * Created by Antony on 05.03.2016.
 */
public class User {

    private int id;
    private String login;
    private String password;
    private String email;
    private boolean isBanned;
    private boolean isAdmin;

    public User(int id, String login, String password, String email, boolean isBanned, boolean isAdmin) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.email = email;
        this.isBanned = isBanned;
        this.isAdmin = isAdmin;
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

    public boolean isBanned() {
        return isBanned;
    }

    public void setBanned(boolean banned) {
        isBanned = banned;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", isBanned=" + isBanned +
                ", isAdmin=" + isAdmin +
                '}';
    }
}
