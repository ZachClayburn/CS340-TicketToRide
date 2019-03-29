package com.tickettoride.facades;

import com.tickettoride.command.ServerCommunicator;
import com.tickettoride.models.Message;
import com.tickettoride.models.idtypes.PlayerID;

import java.util.UUID;

import command.Command;
import command.Response;

public abstract class BaseFacade {
    protected void sendResponseToOne(UUID connID, Command command) {
        Response response = new Response(command);
        ServerCommunicator.getINSTANCE().sendToOne(connID, response);
    }

    protected void sendResponseToRoom(UUID connID, Command command) {
        Response response = new Response(command);
        ServerCommunicator.getINSTANCE().sendToConnectionRoom(connID, response);
    }

    protected void sendResponseToMainLobby(Command command) {
        Response response = new Response(command);
        ServerCommunicator.getINSTANCE().sendToMainLobby(response);
    }
    
    protected void updateHistory(UUID connID, PlayerID playerID, String event){
        Message message=new Message(event,playerID);
        HistoryFacade.getSingleton().sendEvent(connID,message);
    }
}
