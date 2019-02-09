package com.tickettoride.facades;

import com.tickettoride.command.ServerCommunicator;
import com.tickettoride.database.Database;
import com.tickettoride.database.GameDAO;
import com.tickettoride.models.Game;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tickettoride.models.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import command.Command;
import command.Response;

public class GameFacade {
    public void createGame(UUID connID, String groupName, int maxPlayers, String userID) throws Database.DatabaseException {
        String gameID = UUID.randomUUID().toString();
        Game game = new Game(gameID, groupName, 1, maxPlayers);
        try (Database database = new Database()) {
            GameDAO dao = database.getGameDAO();
            dao.addGame(game);
        }
        try {
            Command command = new Command("GameController", "createGame", game);
            Response response = new Response(command); //TODO: fix this
            ServerCommunicator.getINSTANCE().sendToMainLobby(response);
        } catch (Throwable t) {
            Response response = new Response(t.getMessage());
            ServerCommunicator.getINSTANCE().sendToOne(connID, response);
        }
    }

    public void joinGame(UUID connID, String userID, String gameID) throws Database.DatabaseException {
        // TODO: update game to include user as a player
        Game game = null;
        try (Database database = new Database()) {
            GameDAO dao = database.getGameDAO();
            game = dao.getGame(gameID);

            game.setNumPlayer(game.getNumPlayer() + 1);
            dao.increasePlayerCount(gameID, game.getNumPlayer());
        }
        try {
            Command command = new Command("GameController", "joinGame", game);
            Response response = new Response(command); //TODO: fix this
            ServerCommunicator.getINSTANCE().sendToMainLobby(response);
            ServerCommunicator.getINSTANCE().sendToOne(connID,response);
        } catch (Throwable t) {
            Response response = new Response(t.getMessage());
            ServerCommunicator.getINSTANCE().sendToOne(connID, response);
        }
    }
}
