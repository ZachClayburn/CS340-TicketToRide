package com.tickettoride.facades;
import com.tickettoride.command.ServerCommunicator;
import com.tickettoride.facades.helpers.DestinationCardFacadeHelper;
import com.tickettoride.database.Database;
import com.tickettoride.facades.helpers.GameFacadeHelper;
import com.tickettoride.facades.helpers.PlayerHelper;
import com.tickettoride.facades.helpers.RouteHelper;
import com.tickettoride.models.*;
import com.tickettoride.models.idtypes.GameID;
import com.tickettoride.models.idtypes.SessionID;
import exceptions.DatabaseException;
import com.tickettoride.database.GameDAO;
import com.tickettoride.database.PlayerDAO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

import command.Command;

public class GameFacade extends BaseFacade {
    private static GameFacade SINGLETON = new GameFacade();
    public static GameFacade getSingleton() { return SINGLETON; }
    private static String CONTROLLER_NAME = "GameController";
    private static Logger logger = LogManager.getLogger(GameFacade.class.getName());
    private GameFacade() {}

    public void create(UUID connID, SessionID sessionID, String gameName, Integer maxPlayers) {
        try {
            Game game = GameFacadeHelper.getSingleton().createGame(gameName, maxPlayers);
            Session session = new Session(sessionID);
            User user = UserFacade.getSingleton().find_user(session);
            Player player = PlayerHelper.getSingleton().createPlayer(user.getUserID(), game.getGameID());
            ServerCommunicator.getINSTANCE().addRoom(game.getGameID());
            ServerCommunicator.getINSTANCE().moveToRoom(connID, game.getGameID());
            Command command = new Command(
                "GameController", "create",
                player.getPlayerID(), sessionID,
                game.getGameID(), game.getGroupName(), game.getNumPlayer(), game.getMaxPlayer(), game.isStarted());
            sendResponseToOne(connID, command);
            sendResponseToMainLobby(command);
        } catch (Throwable throwable) {
            logger.error(throwable.getMessage(), throwable);
            Command command = new Command(CONTROLLER_NAME, "errorCreate", throwable.getMessage());
            sendResponseToOne(connID, command);
        }
    }

    public void join(UUID connID, SessionID sessionID, GameID gameID) {
        try {
            Game game = GameFacadeHelper.getSingleton().findGame(gameID);
            Session session = new Session(sessionID);
            User user = UserFacade.getSingleton().find_user(session);
            List<Player> players = PlayerHelper.getSingleton().getGamePlayers(game);
            PlayerHelper.getSingleton().setUsernames(players);
            Player player = PlayerHelper.getSingleton().isAlreadyPlayer(user,players);
            Command command;
            if (player == null) { command = GameFacadeHelper.getSingleton().joinCommand(game, user, sessionID); }
            else if (game.IsStarted()) { command = GameFacadeHelper.getSingleton().rejoinIsStartedCommand(game, session, user, players); }
            else { command = GameFacadeHelper.getSingleton().rejoinNotStartedCommand(game, player, sessionID); }
            ServerCommunicator.getINSTANCE().moveToRoom(connID, game.getGameID());
            sendResponseToRoom(connID, command);
            if (game.getNumPlayer() == game.getMaxPlayer()) sendResponseToMainLobby(command);
        } catch (Throwable throwable) {
            logger.error(throwable.getMessage());
            Command command = new Command(CONTROLLER_NAME, "errorJoin");
            sendResponseToOne(connID, command);
        }
    }

    public void leave(UUID connID, SessionID sessionID) {
        try {
            Session session = SessionFacade.getSingleton().find_session(sessionID);
            User user = UserFacade.getSingleton().find_user(session);
            ServerCommunicator.getINSTANCE().moveToMainLobby(connID);
            ArrayList<Game> games = GameFacadeHelper.getSingleton().allGames();
            ArrayList<Game> joinGames = GameFacadeHelper.getSingleton().determineJoinGames(user, games);
            ArrayList<Game> rejoinGames = GameFacadeHelper.getSingleton().determineRejoinGames(user, games);
            Command command = new Command(CONTROLLER_NAME, "leave", joinGames, rejoinGames);
            sendResponseToMainLobby(command);
        } catch (Throwable throwable) {
            logger.error(throwable.getMessage(), throwable);
            Command command = new Command(CONTROLLER_NAME, "errorLeave", throwable);
            sendResponseToOne(connID, command);
        }
    }

    public void start(UUID connID, GameID gameID) throws DatabaseException, CloneNotSupportedException {
        logger.info("Attempting to start game " + gameID);
        GameFacadeHelper.getSingleton().startGame(gameID);
        Game game = GameFacadeHelper.getSingleton().findGame(gameID);
        ArrayList<Player> players = (ArrayList<Player>) PlayerHelper.getSingleton().getGamePlayers(gameID);
        PlayerHelper.getSingleton().pickTurnOrder(players);
        PlayerHelper.getSingleton().pickColors(players);
        PlayerHelper.getSingleton().setUsernames(players);
        PlayerHelper.getSingleton().setTrainCounts(players);
        List<Route> routes = RouteHelper.getSingleton().createGameRoutes(gameID);
        DestinationCardFacadeHelper.dealCards(gameID);
        var command = new Command(CONTROLLER_NAME, "start", players, routes, game.getCurTurn());
        sendResponseToRoom(connID, command);
    }

    public void incrementTurn(UUID connID, GameID gameID) throws DatabaseException {
        Game game = GameFacadeHelper.getSingleton().findGame(gameID);
        game = GameFacadeHelper.getSingleton().updateGameTurn(game);
        Command command = new Command(CONTROLLER_NAME, "nextTurn", game.getCurTurn());
        sendResponseToRoom(connID, command);
    }
}
