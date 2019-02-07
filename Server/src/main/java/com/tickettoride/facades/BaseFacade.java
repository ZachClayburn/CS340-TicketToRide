package com.tickettoride.facades;

import com.tickettoride.command.ServerCommunicator;

import java.util.ArrayList;
import java.util.List;
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

    protected Command buildCommandFromParameters(String facadeName, String methodName, Object... parameters) {
        List<Object> commandParameters = new ArrayList();
        for (Object object : parameters) { commandParameters.add(object); }
        return new Command(facadeName, methodName, commandParameters);
    }
}
