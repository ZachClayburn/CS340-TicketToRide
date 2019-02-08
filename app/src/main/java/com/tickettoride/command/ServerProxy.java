package com.tickettoride.command;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.tickettoride.clientModels.User;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import command.Command;
import command.Response;
import modelAttributes.*;

public class ServerProxy {

    public static ServerProxy SINGLETON = new ServerProxy();

    private ServerProxy() { }

    public void login(Username user, Password pass) {
        try {
            List<Object> parameters = new ArrayList(Arrays.asList(user, pass));
            Command command = new Command("UserFacade", "create", parameters);
            ClientCommunicator.SINGLETON.send(command);
        } catch(Throwable t) {}
    }

    public void register() {}

    public void createGame(String name, int numPlayers){
        try{
            List<Object> parameters = new ArrayList(Arrays.asList(name, numPlayers, User.SINGLETON.getUserID()));
            Command command = new Command("GameFacade", "createGame", parameters);
            ClientCommunicator.SINGLETON.send(command);
        } catch(Throwable t){}
    }

    public void joinGame(String userID, String gameID){
        try {
            List<Object> parameters = new ArrayList(Arrays.asList(userID, gameID));
            Command command = new Command("GameFacade", "joinGame", parameters);
            ClientCommunicator.SINGLETON.send(command);
        } catch(Throwable t) {}
    }
}
