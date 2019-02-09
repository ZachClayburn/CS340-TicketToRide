package com.tickettoride.facades;

import com.tickettoride.command.ServerCommunicator;
import com.tickettoride.database.Database;
import com.tickettoride.database.GameDAO;
import com.tickettoride.models.Game;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

import com.tickettoride.models.Player;
import com.tickettoride.models.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import command.Command;
import command.Response;

public class GameFacade extends BaseFacade{
    public void createGame(UUID connID, String groupName, int maxPlayers, String userID) throws Database.DatabaseException {
        String gameID = UUID.randomUUID().toString();
        Game game = new Game(gameID, groupName, 1, maxPlayers);
        try (Database database = new Database()) {
            database.getGameDAO().addGame(game);
            User user = database.getUserDAO().getUser(userID);
            Player player = new Player(user, game, UUID.randomUUID());
            database.getPlayerDAO().addNewPlayer(player);
        }
        try {
            Command command = new Command("GameController", "createGame");
            sendResponseToOne(connID, command);
        } catch (Throwable t) {
            Command command = new Command("GameController", "errorCreate", t);
            sendResponseToOne(connID, command);
        }
    }

    public void joinGame(UUID connID, String userID, String gameID) throws Database.DatabaseException {
        Game game = null;
        try (Database database = new Database()) {
            GameDAO dao = database.getGameDAO();
            game = dao.getGame(gameID);

            game.setNumPlayer(game.getNumPlayer() + 1);
            dao.increasePlayerCount(gameID, game.getNumPlayer());

            User user = database.getUserDAO().getUser(userID);
            Player player = new Player(user, game, UUID.randomUUID());
            database.getPlayerDAO().addNewPlayer(player);
        }
        try {
            Command command = new Command("GameController", "joinGame");
            sendResponseToOne(connID, command);
            sendResponseToMainLobby(command);
        } catch (Throwable t) {
            Command command = new Command("GameController", "errorJoin", t);
            sendResponseToOne(connID, command);
        }
    }
}
