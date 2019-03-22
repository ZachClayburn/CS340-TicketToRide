package com.tickettoride.facadeProxies;

import com.tickettoride.clientModels.DataManager;
import com.tickettoride.command.ClientCommunicator;

import java.util.UUID;

import com.tickettoride.models.idtypes.SessionID;
import command.Command;
import com.tickettoride.models.Password;
import com.tickettoride.models.Username;

public class SessionFacadeProxy {

    public static SessionFacadeProxy SINGLETON = new SessionFacadeProxy();
    public static String FACADE_NAME = "SessionFacade";

    private SessionFacadeProxy() { }

    public void create(Username user, Password pass) {
        Command command = new Command(FACADE_NAME, "create", user, pass);
        ClientCommunicator.SINGLETON.send(command);
    }

    public void delete() {
        SessionID sessionID = DataManager.SINGLETON.getSession().getSessionID();
        Command command = new Command(FACADE_NAME, "delete", sessionID);
        ClientCommunicator.SINGLETON.send(command);
    }
}
