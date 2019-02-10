package com.tickettoride.facadeProxies;

import com.tickettoride.command.ClientCommunicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import command.Command;

public class GameFacadeProxy {

    public static GameFacadeProxy SINGLETON = new GameFacadeProxy();
    public static String FACADE_NAME = "GameFacade";

    private GameFacadeProxy() { }

    public void create(String name, int maxPlayers, String userID){
        try{
            List<Object> parameters = new ArrayList(Arrays.asList(name, maxPlayers, userID));
            Command command = new Command("GameFacade", "create", parameters);
            ClientCommunicator.SINGLETON.send(command);
        } catch(Throwable t){}
    }

    public void join(String userID, String gameID){
        try {
            List<Object> parameters = new ArrayList(Arrays.asList(userID, gameID));
            Command command = new Command("GameFacade", "join", parameters);
            ClientCommunicator.SINGLETON.send(command);
        } catch(Throwable t) {}
    }
}
