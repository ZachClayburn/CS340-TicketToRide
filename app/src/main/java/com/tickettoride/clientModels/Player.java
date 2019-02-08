package com.tickettoride.clientModels;

import modelInterfaces.IUser;

public class Player implements IUser {
    private String username;

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
