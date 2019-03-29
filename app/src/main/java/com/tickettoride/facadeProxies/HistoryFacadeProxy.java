package com.tickettoride.facadeProxies;

import com.tickettoride.clientModels.DataManager;

import com.tickettoride.command.ClientCommunicator;
import com.tickettoride.models.Game;
import com.tickettoride.models.Message;

import java.util.UUID;

import com.tickettoride.models.idtypes.GameID;
import command.Command;

public class HistoryFacadeProxy {

    private static final String TAG = "HISTORY_FACADE_PROXY";
    public static HistoryFacadeProxy SINGLETON = new HistoryFacadeProxy();
    public static String FACADE_NAME = "HistoryFacade";

    public void getHistory(GameID gameid){
        Command command=new Command(FACADE_NAME,"getHistory", gameid);
        ClientCommunicator.SINGLETON.send(command);
    }
}