package com.tickettoride.facadeProxies;

import com.tickettoride.command.ClientCommunicator;

import command.Command;

public class GameFacadeProxy {

    public static GameFacadeProxy SINGLETON = new GameFacadeProxy();
    public static String FACADE_NAME = "GameFacade";

    private GameFacadeProxy() { }

    public void create(String name, int numPlayers){
        Command command = new Command(FACADE_NAME, "create", name, numPlayers);
        ClientCommunicator.SINGLETON.send(command);
    }

    public void join(){}
}
