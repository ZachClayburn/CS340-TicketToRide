package com.tickettoride.facades.helpers;

import com.tickettoride.database.Database;
import com.tickettoride.database.GameDAO;
import com.tickettoride.database.PlayerDAO;
import com.tickettoride.facades.BaseFacade;
import com.tickettoride.facades.GameFacade;
import com.tickettoride.facades.UserFacade;
import com.tickettoride.models.DestinationCard;
import com.tickettoride.models.Game;
import com.tickettoride.models.Player;
import com.tickettoride.models.Session;
import com.tickettoride.models.User;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;

import command.Command;
import exceptions.DatabaseException;

public class GameFacadeHelper extends BaseFacade {

    private static GameFacadeHelper SINGLETON = new GameFacadeHelper();
    public static GameFacadeHelper getSingleton() { return SINGLETON; }
    private static String CONTROLLER_NAME = "GameController";
    private static Logger logger = LogManager.getLogger(GameFacade.class.getName());
    private GameFacadeHelper() {}

    public void startGame(UUID gameID) throws DatabaseException {
        try (var db = new Database()){
            db.getGameDAO().setGameToStarted(gameID);
            db.getDestinationCardDAO().addDeck(gameID, DestinationCard.getShuffledDeck());
            db.commit();
        }
    }

    public Game findGame(UUID gameID) throws DatabaseException {
        try (Database database = new Database()) {
            GameDAO dao = database.getGameDAO();
            return dao.getGame(gameID);
        }
    }

    public Game createGame(String gameName, Integer maxPlayers) throws DatabaseException {
        try (Database database = new Database()) {
            Game game = new Game(gameName, maxPlayers);
            game.setGameID(UUID.randomUUID());
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

    public void updateGamePlayerCount(UUID gameID, Integer playerCount) throws DatabaseException {
        try (Database database = new Database()) {
            GameDAO dao = database.getGameDAO();
            dao.updatePlayerCount(gameID, playerCount);
            database.commit();
        }
    }

    public ArrayList<Game> determineJoinGames(User user, ArrayList<Game> games) throws DatabaseException {
        ArrayList<Game> joinGames = new ArrayList<>();
        for (Game game : games) {
            Player alreadyPlayer = PlayerHelper.getSingleton().isAlreadyPlayer(user, game);
            if (alreadyPlayer == null) { joinGames.add(game); } }
        return joinGames;
    }

    public ArrayList<Game> determineRejoinGames(User user, ArrayList<Game> games) throws DatabaseException {
        ArrayList<Game> rejoinGames = new ArrayList<>();
        for (Game game : games) {
            Player alreadyPlayer = PlayerHelper.getSingleton().isAlreadyPlayer(user, game);
            if (alreadyPlayer != null) { rejoinGames.add(game); } }
        return rejoinGames;
    }


    public Command rejoinIsStartedCommand(Game game, Session session, User user, List<Player> players) throws DatabaseException {
        PlayerHelper.getSingleton().pickColors(players);
        Player player = PlayerHelper.getSingleton().isAlreadyPlayer(user, players);
        List<DestinationCard> playerDestinationCards = DestinationCardFacadeHelper.getSingleton().destinationCardsInPlayersHand(player);
        Queue<DestinationCard> gameDeck = DestinationCardFacadeHelper.getSingleton().destinationCardsinGameDeck(game.getGameID());
        int deckCount = gameDeck.size();
        return new Command(CONTROLLER_NAME, "rejoinIsStarted",
                           session.getSessionID(), game.getGameID(), game.getGroupName(), players, playerDestinationCards, deckCount);

    }

    public Command joinCommand(Game game, User user, UUID sessionID) throws Exception {
        if (game.getNumPlayer() >= game.getMaxPlayer()) throw new Exception("Cannot join a full game");
        Player player = PlayerHelper.getSingleton().createPlayer(user.getUserID(), game.getGameID());
        GameFacadeHelper.getSingleton().updateGamePlayerCount(game.getGameID(), game.getNumPlayer() + 1);
        game.setNumPlayer((game.getNumPlayer() + 1));
        return new Command(CONTROLLER_NAME, "join", player.getPlayerID(), sessionID,
                           game.getGameID(), game.getGroupName(), game.getNumPlayer(), game.getMaxPlayer());
    }

    public Command rejoinNotStartedCommand(Game game, Player player, UUID sessionID) {
        return new Command(CONTROLLER_NAME, "rejoinNotStarted", player.getPlayerID(), sessionID,
                           game.getGameID(), game.getGroupName(), game.getNumPlayer(), game.getMaxPlayer());
    }
}