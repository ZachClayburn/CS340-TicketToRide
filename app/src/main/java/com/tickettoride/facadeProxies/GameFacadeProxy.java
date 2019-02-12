package com.tickettoride.facadeProxies;

import com.tickettoride.clientModels.DataManager;
import com.tickettoride.clientModels.Session;
import com.tickettoride.command.ClientCommunicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import command.Command;

public class GameFacadeProxy {

    public static GameFacadeProxy SINGLETON = new GameFacadeProxy();
    public static String FACADE_NAME = "GameFacade";

    private GameFacadeProxy() { }

    public void create(String name, int maxPlayers){
        Session session = DataManager.getSINGLETON().getSession();
        try{
            Command command = new Command(FACADE_NAME, "create", session.getSessionId(), name, maxPlayers);
            ClientCommunicator.SINGLETON.send(command);
        } catch(Throwable t){}
    }

    public void join(UUID gameID){
        Session session = DataManager.getSINGLETON().getSession();
        try {
            Command command = new Command(FACADE_NAME, "join", session.getSessionId(), gameID);
            ClientCommunicator.SINGLETON.send(command);
        } catch(Throwable t) {}
    }

    public void leave() {
        try{
            Command command = new Command(FACADE_NAME, "leave");
            ClientCommunicator.SINGLETON.send(command);
        } catch(Throwable t){}
    }
}
