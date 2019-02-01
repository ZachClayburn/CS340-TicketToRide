package com.tickettoride.models;

import modelAttributes.Password;
import modelAttributes.Username;
import modelInterfaces.IUser;

public class User implements IUser {
    public static User findBy(Username username, Password password) {
//        Implement Database Find User
        return new User();
    }

    public User(Username username, Password password) {

    }
}