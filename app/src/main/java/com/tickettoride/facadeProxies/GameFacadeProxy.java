package com.tickettoride.facadeProxies;

import com.tickettoride.command.ClientCommunicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import command.Command;

public class GameFacadeProxy {

    public static GameFacadeProxy SINGLETON = new GameFacadeProxy();

    private GameFacadeProxy() { }

    public void create(String name, int numPlayers){
        List<Object> parameters = new ArrayList(Arrays.asList(name, numPlayers));
        Command command = new Command("GameFacade", "createGame", parameters);
        ClientCommunicator.SINGLETON.send(command);
    }

    public void join(){}
}
