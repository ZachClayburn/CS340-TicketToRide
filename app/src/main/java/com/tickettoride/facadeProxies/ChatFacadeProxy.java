package com.tickettoride.facadeProxies;

import com.tickettoride.clientModels.DataManager;

import com.tickettoride.command.ClientCommunicator;
import com.tickettoride.models.Game;
import com.tickettoride.models.Message;

import java.util.UUID;

import com.tickettoride.models.idtypes.GameID;
import command.Command;

public class ChatFacadeProxy {

    private static final String TAG = "GAME_FACADE_PROXY";
    public static ChatFacadeProxy SINGLETON = new ChatFacadeProxy();
    public static String FACADE_NAME = "ChatFacade";
    
    public void sendMessage(Message message){
        Command command=new Command(FACADE_NAME, "sendMessage", message);
        ClientCommunicator.SINGLETON.send(command);
    }
    
    public void getChat(GameID gameid){
        Command command=new Command(FACADE_NAME,"getChat", gameid);
        ClientCommunicator.SINGLETON.send(command);
    }
}
