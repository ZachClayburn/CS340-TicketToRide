package com.tickettoride.facadeProxies;

import android.util.Log;

import com.tickettoride.clientModels.DataManager;
import com.tickettoride.command.ClientCommunicator;

import java.util.UUID;

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

    public void delete() {
        UUID sessionID = DataManager.SINGLETON.getSession().getSessionId();
        Command command = new Command(FACADE_NAME, "delete", sessionID);
        ClientCommunicator.SINGLETON.send(command);
    }
}
