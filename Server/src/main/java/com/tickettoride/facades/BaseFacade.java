package com.tickettoride.facades;

import com.tickettoride.command.ServerCommunicator;

import java.util.UUID;

import command.Command;
import command.Response;

public abstract class BaseFacade {
    protected void sendResponseToOne(UUID roomID, Command command) {
        Response response = new Response(command);
        ServerCommunicator.getINSTANCE().sendToOne(roomID, response);
    }

    protected void sendResponseToRoom(UUID roomID, Command command) {
        Response response = new Response(command);
        ServerCommunicator.getINSTANCE().sendToRoom(roomID, response);
    }

    protected void sendResponseToMainLobby(Command command) {
        Response response = new Response(command);
        ServerCommunicator.getINSTANCE().sendToMainLobby(response);
    }
}
