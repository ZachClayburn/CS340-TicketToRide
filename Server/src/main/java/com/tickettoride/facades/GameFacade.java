package com.tickettoride.facades;
import com.tickettoride.command.ServerCommunicator;
import com.tickettoride.database.Database;
import exceptions.DatabaseException;
import com.tickettoride.database.GameDAO;
import com.tickettoride.database.PlayerDAO;
import com.tickettoride.models.Game;

import com.tickettoride.models.Player;
import com.tickettoride.models.Session;
import com.tickettoride.models.User;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.UUID;

import command.Command;

public class GameFacade extends BaseFacade {
    private static GameFacade SINGLETON = new GameFacade();
    public static GameFacade getSingleton() { return SINGLETON; }
    private static String CONTROLLER_NAME = "GameController";
    private static Logger logger = LogManager.getLogger(GameFacade.class.getName());
    private GameFacade() {}

    public void create(UUID connID, UUID sessionID, String gameName, Integer maxPlayers) {
        try {
            Game game = createGame(gameName, maxPlayers, sessionID);
            Session session = new Session(sessionID);
            User user = UserFacade.getSingleton().find_user(session);
            Player player = createPlayer(user, game);
            ServerCommunicator.getINSTANCE().addRoom(game.getGameID());
            ServerCommunicator.getINSTANCE().moveToRoom(connID, game.getGameID());
            Command command = new Command(
                "GameController", "create",
                player.getPlayerID(), sessionID,
                game.getGameID(), game.getGroupName(), game.getNumPlayer(), game.getMaxPlayer());
            sendResponseToOne(connID, command);
            sendResponseToMainLobby(command);
        } catch (Throwable throwable) {
            logger.error(throwable.getMessage(), throwable);
            Command command = new Command(CONTROLLER_NAME, "errorCreate", throwable);
            sendResponseToOne(connID, command);
      }
    }

    public void join(UUID connID, UUID sessionID, UUID gameID) {
        try {
            Game game = findGame(gameID);
            Session session = new Session(sessionID);
            User user = UserFacade.getSingleton().find_user(session);
            Player player = createPlayer(user, game);
            updatePlayerCount(game.getGameID(), game.getNumPlayer() + 1);
            game.setNumPlayer((game.getNumPlayer() + 1));
            ServerCommunicator.getINSTANCE().moveToRoom(connID, game.getGameID());
            Command command = new Command(
                    CONTROLLER_NAME, "join",
                    player.getPlayerID(), sessionID,
                    game.getGameID(), game.getGroupName(), game.getNumPlayer(), game.getMaxPlayer());
            sendResponseToRoom(connID, command);
            if (game.getNumPlayer() == game.getMaxPlayer()) sendResponseToMainLobby(command);
        } catch (Throwable throwable) {
            logger.error(throwable.getMessage(), throwable);
            Command command = new Command(CONTROLLER_NAME, "errorJoin", throwable);
            sendResponseToOne(connID, command);
        }
    }

    public Game findGame(UUID gameID) throws DatabaseException {
        try (Database database = new Database()) {
            GameDAO dao = database.getGameDAO();
            return dao.getGame(gameID);
        }
    }


    public Game createGame(String gameName, Integer maxPlayers, UUID sessionID) throws DatabaseException {
        try (Database database = new Database()) {
            Session session = new Session(sessionID);
            User user = UserFacade.getSingleton().find_user(session);
            Game game = new Game(gameName, maxPlayers, user);
            GameDAO dao = database.getGameDAO();
            dao.addGame(game);
            database.commit();
            return game;
        }
    }

    public ArrayList<Game> allGames() throws DatabaseException {
        try (Database database = new Database()) {
            GameDAO dao = database.getGameDAO();
            return dao.allGames();
        }
    }

    public Player createPlayer(User user, Game game) throws DatabaseException {
        try (Database database = new Database()) {
            Player player = new Player(user, game);
            PlayerDAO dao = database.getPlayerDAO();
            dao.addNewPlayer(player);
            database.commit();
            return player;
        }
    }

    public void updatePlayerCount(UUID gameID, Integer playerCount) throws DatabaseException {
        try (Database database = new Database()) {
            GameDAO dao = database.getGameDAO();
            dao.updatePlayerCount(gameID, playerCount);
            database.commit();
        }
    }

}
