package com.tickettoride.models;

import com.tickettoride.models.idtypes.UserID;

import java.util.Objects;
import java.util.UUID;

public class User {

    private final Username username;
    private final Password password;
    private final UserID userID;

    public User(Username username, Password password) {

        this.username = username;
        this.password = password;
        userID = UserID.randomUUID();
    }

    public User(Username username, Password password, String userID) {

        this.username = username;
        this.password = password;
        this.userID = UserID.fromString(userID);
    }

    public Username getUsername() {
        return username;
    }

    public Password getPassword() {
        return password;
    }

    public UserID getUserID() {
        return userID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getUsername().equals(user.getUsername()) &&
                getPassword().equals(user.getPassword()) &&
                getUserID().equals(user.getUserID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getPassword(), getUserID());
    }
}