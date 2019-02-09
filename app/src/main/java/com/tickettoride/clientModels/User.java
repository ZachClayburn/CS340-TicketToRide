package com.tickettoride.clientModels;

import java.util.UUID;

import modelAttributes.Username;
import modelInterfaces.IUser;

// Current User
public class User implements IUser {
    private Username username;
    private UUID userID;

    public static User SINGLETON = new User();

    private User() { }

    public String getUsername() {
        return username.toString();
    }

    public void setUsername(Username username) {
        this.username = username;
    }

    public String getUserID(){return userID.toString();}

    public void setUserID(UUID userID) {this.userID = userID;}
}
