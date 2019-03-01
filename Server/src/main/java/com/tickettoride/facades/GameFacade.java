package com.tickettoride.facades;
import com.tickettoride.command.ServerCommunicator;
import com.tickettoride.database.Database;
import com.tickettoride.models.*;
import exceptions.DatabaseException;
import com.tickettoride.database.GameDAO;
import com.tickettoride.database.PlayerDAO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

import command.Command;
import modelAttributes.Color;
import modelAttributes.PlayerColor;

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
            Player player = PlayerFacade.getSingleton().createPlayer(user.getUserID(), game.getGameID());
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
            Command command = new Command(CONTROLLER_NAME, "errorCreate", throwable);
            sendResponseToOne(connID, command);
      }
    }

    public void join(UUID connID, UUID sessionID, UUID gameID) {
        try {
            Game game = findGame(gameID);
            Session session = new Session(sessionID);
            User user = UserFacade.getSingleton().find_user(session);
            List<Player> players = PlayerFacade.getSingleton().getGamePLayers(game);
            Player player = PlayerFacade.getSingleton().isAlreadyPlayer(user,players);
            String commandMethodName;
            if (player == null) {
                if (game.getNumPlayer() >= game.getMaxPlayer()) throw new Exception("Cannot join a full game");
                player = PlayerFacade.getSingleton().createPlayer(user.getUserID(), game.getGameID());
                updateGamePlayerCount(game.getGameID(), game.getNumPlayer() + 1);
                game.setNumPlayer((game.getNumPlayer() + 1));
                commandMethodName = "join";
            } else { commandMethodName = "rejoin"; }
            Command command = new Command(
                    CONTROLLER_NAME, commandMethodName,
                    player.getPlayerID(), sessionID,
                    game.getGameID(), game.getGroupName(), game.getNumPlayer(), game.getMaxPlayer(), game.isStarted());
            ServerCommunicator.getINSTANCE().moveToRoom(connID, game.getGameID());
            sendResponseToRoom(connID, command);
            if (game.getNumPlayer() == game.getMaxPlayer()) sendResponseToMainLobby(command);
        } catch (Throwable throwable) {
            logger.error(throwable.getMessage());
            Command command = new Command(CONTROLLER_NAME, "errorJoin");
            sendResponseToOne(connID, command);
        }
    }

    public void leave(UUID connID, UUID sessionID) {
        try {
            Session session = SessionFacade.getSingleton().find_session(sessionID);
            User user = UserFacade.getSingleton().find_user(session);
            ServerCommunicator.getINSTANCE().moveToMainLobby(connID);
            ArrayList<Game> games = GameFacade.getSingleton().allGames();
            ArrayList<Game> joinGames = GameFacade.getSingleton().determineJoinGames(user, games);
            ArrayList<Game> rejoinGames = GameFacade.getSingleton().determineRejoinGames(user, games);
            Command command = new Command(CONTROLLER_NAME, "leave", joinGames, rejoinGames);
            sendResponseToMainLobby(command);
        } catch (Throwable throwable) {
            logger.error(throwable.getMessage(), throwable);
            Command command = new Command(CONTROLLER_NAME, "errorLeave", throwable);
            sendResponseToOne(connID, command);
        }
    }

    public void start(UUID connID, UUID gameID) throws DatabaseException {
        logger.info("Attempting to start game " + gameID);
        try (var db = new Database()){

            db.getGameDAO().setGameToStarted(gameID);
            db.commit();
        }

        var command = new Command(CONTROLLER_NAME, "start");

        sendResponseToRoom(connID, command);
    }

    public void acceptDestinationCards(UUID connID, UUID sessionID, Player player,
                                       Collection<DestinationCard> acceptedCards) {
        try (var db = new Database()) {
            //FIXME Come up with way to track that everyone has accepted their first cards
            db.getDestinationCardDAO().acceptCards(player, acceptedCards);

            var cmd = new Command(CONTROLLER_NAME, "setPlayerAcceptedCards",
                    player, acceptedCards); //FIXME Make this command point to a method on the client

            sendResponseToRoom(connID, cmd);

            db.commit();

        } catch (DatabaseException e) {
            e.printStackTrace();//FIXME Add proper error handling
        }
    }

    void dealCards(UUID conID, UUID gameID) {
        logger.debug("Dealing to game " + gameID);

        List<Command> commands = new ArrayList<>();
        try (var db = new Database()) {

            var game = db.getGameDAO().getGame(gameID);
            assert game != null;

            Queue<DestinationCard> destinationDeck = DestinationCard.getShuffledDeck();

            for (var player: db.getPlayerDAO().getGamePlayers(gameID)){
                List<DestinationCard> offeredCards = new ArrayList<>();

                offeredCards.add(destinationDeck.remove());
                offeredCards.add(destinationDeck.remove());
                offeredCards.add(destinationDeck.remove());

                db.getDestinationCardDAO().offerCardsToPlayer(player, offeredCards);
                commands.add(offerDestinationCards(player, offeredCards, 2));
            }

            commands.add(sendDestinationDeck(destinationDeck));

            db.commit();

        } catch (DatabaseException e) {
            e.printStackTrace();//FIXME Add proper error handling
        }

        commands.forEach(command -> sendResponseToRoom(conID, command));
    }

    Command offerDestinationCards(Player player, List<DestinationCard> offeredCards) {
        return offerDestinationCards(player, offeredCards, 1);
    }

    Command offerDestinationCards(Player player, List<DestinationCard> offeredCards,
                                  int requiredToKeep) {
        return new Command(CONTROLLER_NAME, "offerDestinationCards",
                player, offeredCards, requiredToKeep); //FIXME Make this command point to a method on the client
    }

    Command sendDestinationDeck(Queue<DestinationCard> deck) {
        return new Command(CONTROLLER_NAME, "updateDestinationDeck",
                deck.size()); //FIXME Make this command point to a method on the client
    }

    Game findGame(UUID gameID) throws DatabaseException {
        try (Database database = new Database()) {
            GameDAO dao = database.getGameDAO();
            return dao.getGame(gameID);
        }
    }

    public Game createGame(String gameName, Integer maxPlayers, UUID sessionID) throws DatabaseException {
        try (Database database = new Database()) {
            Session session = new Session(sessionID);
            User user = UserFacade.getSingleton().find_user(session);
            Game game = new Game(gameName, maxPlayers);
            GameDAO dao = database.getGameDAO();
            dao.addGame(game);
            database.commit();
            return game;
        }
    }

    ArrayList<Game> allGames() throws DatabaseException {
        try (Database database = new Database()) {
            GameDAO dao = database.getGameDAO();
            return dao.allGames();
        }
    }

    Player createPlayer(UUID user, UUID game) throws DatabaseException {
        try (Database database = new Database()) {
            Player player = new Player(user, game);
            PlayerDAO dao = database.getPlayerDAO();
            dao.addPlayer(player);
            database.commit();
            return player;
        }
    }

    void updateGamePlayerCount(UUID gameID, Integer playerCount) throws DatabaseException {
        try (Database database = new Database()) {
            GameDAO dao = database.getGameDAO();
            dao.updatePlayerCount(gameID, playerCount);
            database.commit();
        }
    }

    public void setup(UUID connID, UUID gameID) throws DatabaseException {
        try (Database database = new Database()) {
            PlayerDAO dao = database.getPlayerDAO();
            List<Player> players = dao.getGamePlayers(gameID);
            pickTurnOrder(players);
            pickColors(players);
            Command command = new Command(CONTROLLER_NAME, "setup", players);
            sendResponseToRoom(connID, command);
        }
        catch (Throwable throwable) {
            logger.error(throwable.getMessage(), throwable);
            Command command = new Command(CONTROLLER_NAME, "errorSetup", throwable);
            sendResponseToRoom(connID, command);
        }
    }

    public void pickTurnOrder(List<Player> players) throws DatabaseException {
        Collections.shuffle(players);
        try (Database database = new Database()) {
            PlayerDAO dao = database.getPlayerDAO();
            for (int i = 0; i < players.size(); i++) {
                Player player = players.get(i);
                player.setTurn(i + 1);
                dao.setTurn(player.getPlayerID(), player.getTurn());
            }
            database.commit();
        }
    }

    public void pickColors(List<Player> players) {
        for (Player player:players){
            player.setColor();
        }
    }

    public List<Player> getGamePLayers(Game game) throws DatabaseException {
        try (Database database = new Database()) {
            PlayerDAO dao = database.getPlayerDAO();
            return dao.getGamePlayers(game.getGameID());
        }
    }

    public ArrayList<Game> determineJoinGames(User user, ArrayList<Game> games) throws DatabaseException {
        ArrayList<Game> joinGames = new ArrayList<>();
        for (Game game : games) {
            Player alreadyPlayer = PlayerFacade.getSingleton().isAlreadyPlayer(user, game);
            if (alreadyPlayer == null) { joinGames.add(game); } }
        return joinGames;
    }

    public ArrayList<Game> determineRejoinGames(User user, ArrayList<Game> games) throws DatabaseException {
        ArrayList<Game> rejoinGames = new ArrayList<>();
        for (Game game : games) {
            Player alreadyPlayer = PlayerFacade.getSingleton().isAlreadyPlayer(user, game);
            if (alreadyPlayer != null) { rejoinGames.add(game); } }
        return rejoinGames;
    }
}
