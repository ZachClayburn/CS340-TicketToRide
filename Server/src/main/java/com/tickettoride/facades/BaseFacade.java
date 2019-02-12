package com.tickettoride.facades;

import com.tickettoride.command.ServerCommunicator;

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
}
