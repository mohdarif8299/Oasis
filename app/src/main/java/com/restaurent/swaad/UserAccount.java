package com.restaurent.swaad;

public class UserAccount {
    private String email,number,name;

    public UserAccount() {
    }

    public UserAccount(String email, String number, String name) {
        this.email = email;
        this.number = number;
        this.name = name;
    }
    public void setEmail(String email) { this.email = email; }
    public void setNumber(String number) { this.number = number; }
    public String getEmail() {
        return email;
    }
    public String getNumber() {
        return number;
    }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
