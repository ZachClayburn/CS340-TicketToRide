package com.tickettoride.facadeProxies;

import com.tickettoride.command.ClientCommunicator;

import command.Command;
import modelAttributes.Password;
import modelAttributes.Username;

public class SessionFacadeProxy {

    public static SessionFacadeProxy SINGLETON = new SessionFacadeProxy();
    public static String FACADE_NAME = "SessionFacade";

    private SessionFacadeProxy() { }

    public void create(Username user, Password pass) {
        Command command = new Command(FACADE_NAME, "create", user, pass);
        ClientCommunicator.SINGLETON.send(command);
    }
}
