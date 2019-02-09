package com.tickettoride.facades;
import com.tickettoride.database.Database;
import com.tickettoride.database.GameDAO;
import com.tickettoride.models.Game;

import com.tickettoride.models.Player;
import com.tickettoride.models.User;
import java.util.UUID;

import command.Command;

public class GameFacade extends BaseFacade {
    private static GameFacade SINGLETON = new GameFacade();
    public static GameFacade getSingleton() { return SINGLETON; }
    private GameFacade() {}

    public void createGame(UUID connID, String groupName, int maxPlayers, String userID) throws Database.DatabaseException {
        // TODO: Database stuff to make game, retrieve user info
        User creator = null;
        String gameID = UUID.randomUUID().toString();
        Game game = new Game(gameID, groupName, 1, maxPlayers);
        Player player = null;
        try (Database database = new Database()) {
            database.getGameDAO().addGame(game);
            User user = database.getUserDAO().getUser(userID);
            player = new Player(user, game, UUID.randomUUID());
            database.getPlayerDAO().addNewPlayer(player);
        }
        try {
            Command command = new Command("GameController", "createGame", player, game);
            sendResponseToOne(connID, command);
        } catch (Throwable t) {
            Command command = new Command("GameController", "errorCreate", t);
            sendResponseToOne(connID, command);
        }
    }

    public void joinGame(UUID connID, String userID, String gameID) throws Database.DatabaseException {
        Game game = null;
        Player player = null;
        try (Database database = new Database()) {
            GameDAO dao = database.getGameDAO();
            game = dao.getGame(gameID);

            game.setNumPlayer(game.getNumPlayer() + 1);
            dao.increasePlayerCount(gameID, game.getNumPlayer());

            User user = database.getUserDAO().getUser(userID);
            player = new Player(user, game, UUID.randomUUID());
            database.getPlayerDAO().addNewPlayer(player);
        }
        try {
            Command command = new Command("GameController", "joinGame", player, game);
            sendResponseToOne(connID, command);
            sendResponseToMainLobby(command);
        } catch (Throwable t) {
            Command command = new Command("GameController", "errorJoin", t);
            sendResponseToOne(connID, command);
        }
    }
}
