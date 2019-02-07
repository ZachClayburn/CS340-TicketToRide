package com.tickettoride.facades;

import com.tickettoride.command.ServerCommunicator;
import com.tickettoride.models.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import command.Command;
import modelAttributes.Password;
import modelAttributes.Username;

public class UserFacade {
    public static UserFacade SINGLETON = new UserFacade();

    public void create(Username username, Password password) {
        User user = new User(username, password);
        List<Object> parameters = new ArrayList(Arrays.asList(user));
        Command command = new Command("UserController", "create", parameters);
//        ServerCommunicator.SINGLETON.send(command);
    }
}


