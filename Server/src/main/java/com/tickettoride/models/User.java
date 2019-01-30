package com.tickettoride.models;

import modelInterfaces.IUser;
import modelAttributes.Password;
import modelAttributes.Username;

public class User implements IUser {
    public static User findBy(Username username, Password password) {
//        Implement Database Find User
        return new User(username, password);
    }

    public User(Username username, Password password) {

    }
}