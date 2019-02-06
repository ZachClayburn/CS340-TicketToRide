package com.tickettoride.facadeProxies;

import com.tickettoride.command.ClientCommunicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import command.Command;
import modelAttributes.Password;
import modelAttributes.Username;

public class UserFacadeProxy {

    public static UserFacadeProxy SINGLETON = new UserFacadeProxy();

    private UserFacadeProxy() { }

    public void create(Username user, Password pass) {
        List<Object> parameters = new ArrayList(Arrays.asList(user, pass));
        Command command = new Command("UserFacade", "create", parameters);
        ClientCommunicator.SINGLETON.send(command);
    }
}
