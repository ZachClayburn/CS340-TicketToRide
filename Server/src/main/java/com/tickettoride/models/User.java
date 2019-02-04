package com.tickettoride.models;

import modelInterfaces.IUser;
import modelAttributes.Password;
import modelAttributes.Username;

import java.util.UUID;

public class User implements IUser {

    public static User findBy(Username username, Password password) {
        //TODO Implement Database Find User
        return new User(username, password);
    }

    private final Username username;
    private final Password password;
    private final UUID userID;

    public User(Username username, Password password) {
        this.username = username;
        this.password = password;
        userID = UUID.randomUUID();
    }

    public Username getUsername() {
        return username;
    }

    public Password getPassword() {
        return password;
    }

    public UUID getUserID() {
        return userID;
    }
}