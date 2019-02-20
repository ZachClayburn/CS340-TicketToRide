package com.tickettoride.facades;
import com.tickettoride.command.ServerCommunicator;
import com.tickettoride.database.Database;
import com.tickettoride.models.*;
import exceptions.DatabaseException;
import com.tickettoride.database.GameDAO;
import com.tickettoride.database.PlayerDAO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
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
            Player player = createPlayer(user.getUserID(), game.getGameID());
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
            if (game.getNumPlayer() >= game.getMaxPlayer()) throw new Exception("Cannot join a full game");
            Session session = new Session(sessionID);
            User user = UserFacade.getSingleton().find_user(session);
            List<Player> players=null;
            try (Database database = new Database()) {
                PlayerDAO dao = database.getPlayerDAO();
                players=dao.getGamePlayers(gameID);
            }
            Player player=isAlreadyPlayer(user,players);
            Command command;
            if(player==null){
                player = createPlayer(user.getUserID(), game.getGameID());
                updatePlayerCount(game.getGameID(), game.getNumPlayer() + 1);
                game.setNumPlayer((game.getNumPlayer() + 1));
                command = new Command(
                        CONTROLLER_NAME, "join",
                        player.getPlayerID(), sessionID,
                        game.getGameID(), game.getGroupName(), game.getNumPlayer(), game.getMaxPlayer(), game.isStarted());
            }else{
                command = new Command(
                        CONTROLLER_NAME, "rejoin",
                        player.getPlayerID(), sessionID,
                        game.getGameID(), game.getGroupName(), game.getNumPlayer(), game.getMaxPlayer(), game.isStarted());
            }
            ServerCommunicator.getINSTANCE().moveToRoom(connID, game.getGameID());
            
            sendResponseToRoom(connID, command);
            if (game.getNumPlayer() == game.getMaxPlayer()) sendResponseToMainLobby(command);
        } catch (Throwable throwable) {
            logger.error(throwable.getMessage());
            Command command = new Command(CONTROLLER_NAME, "errorJoin");
            sendResponseToOne(connID, command);
        }
    }

    public void leave(UUID connID) {
        try {
            //deletePlayer(sessionID);
            //int originalPlayerCount = game.getNumPlayer();
            //updatePlayerCount(game.getGameID(), game.getNumPlayer() - 1);
            ServerCommunicator.getINSTANCE().moveToMainLobby(connID);
            Command command = new Command(CONTROLLER_NAME, "leave", allGames());
            sendResponseToMainLobby(command);
        } catch (Throwable throwable) {
            logger.error(throwable.getMessage(), throwable);
            Command command = new Command(CONTROLLER_NAME, "errorLeave", throwable);
            sendResponseToOne(connID, command);
        }
    }
    
    private Player isAlreadyPlayer(User user, List<Player> players){
        if(players==null||players.size()==0){
            return null;
        }
        for(Player p:players){
            if(user.getUserID().equals(p.getUserID())){
                return p;
            }
        }
        return null;
    }

    public void start(UUID connID, UUID gameID) throws DatabaseException {
        logger.info("Attempting to start game " + gameID);
        //FIXME This should be where the the Players associated with each user should be added
        try (var db = new Database()){

            db.getGameDAO().setGameToStarted(gameID);
            db.commit();
        }

        var command = new Command(CONTROLLER_NAME, "start");

        sendResponseToRoom(connID, command);
    }

    void dealCards(UUID conID, UUID gameID) {
        logger.debug("Dealing to game " + gameID);

        List<Command> commands = new ArrayList<>();
        try (var db = new Database()) {

            var game = db.getGameDAO().getGame(gameID);

            assert game != null;
            assert game.getDestinationDeck().size() == 0;

            Queue<DestinationCard> destinationDeck = DestinationCard.getShuffledDeck();


            for (var player: db.getPlayerDAO().getGamePlayers(gameID)){
                player.addDestinationCardToHand(destinationDeck.remove());
                player.addDestinationCardToHand(destinationDeck.remove());
                player.addDestinationCardToHand(destinationDeck.remove());

                db.getPlayerDAO().updateHand(player);
                //TODO Create command to send this hand to all players
            }

            game.setDestinationDeck(destinationDeck);
            db.getGameDAO().updateDecks(game);
            //TODO Create command to send this deck to each player

            db.commit();

        } catch (DatabaseException e) {
            e.printStackTrace();
        }

        commands.forEach(command -> sendResponseToRoom(conID, command));
    }

    Game findGame(UUID gameID) throws DatabaseException {
        try (Database database = new Database()) {
            GameDAO dao = database.getGameDAO();
            return dao.getGame(gameID);
        }
    }

    void deletePlayer(UUID sessionID) throws DatabaseException, SQLException {
        try (Database database = new Database()) {
            PlayerDAO dao = database.getPlayerDAO();
            dao.deletePlayer(sessionID);
            database.commit();
        }
    }


    Game createGame(String gameName, Integer maxPlayers, UUID sessionID) throws DatabaseException {
        try (Database database = new Database()) {

            User user = UserFacade.getSingleton().find_user(sessionID);
            Game game = new Game(gameName, maxPlayers);
            Player player = new Player(user.getUserID(), game.getGameID());

            database.getPlayerDAO().addPlayer(player);
            database.getGameDAO().addGame(game);

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

    void updatePlayerCount(UUID gameID, Integer playerCount) throws DatabaseException {
        try (Database database = new Database()) {
            GameDAO dao = database.getGameDAO();
            dao.updatePlayerCount(gameID, playerCount);
            database.commit();
        }
    }

}
