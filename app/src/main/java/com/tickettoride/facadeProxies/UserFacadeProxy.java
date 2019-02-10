package com.tickettoride.facadeProxies;

import android.util.Log;

import com.tickettoride.command.ClientCommunicator;

import command.Command;
import modelAttributes.Password;
import modelAttributes.Username;

public class UserFacadeProxy {

    public static UserFacadeProxy SINGLETON = new UserFacadeProxy();

    private UserFacadeProxy() { }

    public void create(Username user, Password pass) {
        Log.i("create", "Caught in User Facade Proxy Create");
        Command command = new Command("UserFacade", "create", user, pass);
        ClientCommunicator.SINGLETON.send(command);
    }
}
