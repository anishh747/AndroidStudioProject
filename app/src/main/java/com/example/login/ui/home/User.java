package com.example.login.ui.home;

public class User {
    public String emailAddress;
    public String fullName;
    public String password;
    public String username;

    public User(){}
    public User (String emailAddress, String fullName, String password,String username){
        this.emailAddress = emailAddress;
        this.fullName = fullName;
        this.password = password;
        this.username = username;
    }
}