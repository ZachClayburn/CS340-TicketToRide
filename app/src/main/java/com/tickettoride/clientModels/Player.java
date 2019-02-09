package com.tickettoride.clientModels;

import java.util.UUID;

import modelAttributes.Username;
import modelInterfaces.IUser;

// Generic Player
public class Player implements IUser {
    private Username username;
    private UUID userID;

    public String getUsername() {
        return username.toString();
    }

    public String getUserID(){return userID.toString();}
}
