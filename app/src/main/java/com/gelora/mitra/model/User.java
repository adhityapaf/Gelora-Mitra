package com.gelora.mitra.model;

public class User {
    private String namaMitra;
    private String emailMitra;
    private String passwordMitra;
    private String role;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public User() {

    }

    public User(String namaMitra, String emailMitra, String passwordMitra, String role) {
        this.namaMitra = namaMitra;
        this.emailMitra = emailMitra;
        this.passwordMitra = passwordMitra;
        this.role = role;
    }

    public String getNamaMitra() {
        return namaMitra;
    }

    public void setNamaMitra(String namaMitra) {
        this.namaMitra = namaMitra;
    }

    public String getEmailMitra() {
        return emailMitra;
    }

    public void setEmailMitra(String emailMitra) {
        this.emailMitra = emailMitra;
    }

    public String getPasswordMitra() {
        return passwordMitra;
    }

    public void setPasswordMitra(String passwordMitra) {
        this.passwordMitra = passwordMitra;
    }
}
