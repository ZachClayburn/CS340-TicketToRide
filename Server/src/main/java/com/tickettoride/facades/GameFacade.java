package com.tickettoride.facades;

import com.tickettoride.command.ServerCommunicator;
import com.tickettoride.models.Game;
import com.tickettoride.models.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import command.Response;

public class GameFacade {
    public void createGame(String name, int numPlayers) {
        // TODO: Database stuff to make game, retrieve user info
        User creator = null;
        String gameID = UUID.randomUUID().toString();
        Game game = new Game(gameID, name, numPlayers, creator);
        try {
            Response response = new Response(game);
            ServerCommunicator.getINSTANCE().sendToMainLobby(response);
        } catch (Throwable t) {
            Response response = new Response(t);
            //TODO: where to get connID from
            ServerCommunicator.getINSTANCE().sendToOne(null, response);
        }
    }

    public void joinGame(String userID, String gameID){
        // TODO: Retrieve Game info from database, update game to include user as a player
        Game game = null;
        game.setNumPlayer(game.getNumPlayer() + 1);
        try {
            Response response = new Response(game);
            ServerCommunicator.getINSTANCE().sendToMainLobby(response);
            ServerCommunicator.getINSTANCE().sendToOne(null,response);
        } catch (Throwable t) {
            Response response = new Response(t);
            //TODO: where to get connID from
            ServerCommunicator.getINSTANCE().sendToOne(null, response);
        }
    }
}
