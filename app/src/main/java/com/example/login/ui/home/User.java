package com.example.login.ui.home;

public class User {
    public String emailAddress;
    public String fullName;
    public String password;
    public boolean isFirst;

    public User(){}
    public User (String emailAddress, String fullName, String password){
        this.emailAddress = emailAddress;
        this.fullName = fullName;
        this.password = password;
        isFirst = true;
    }
}