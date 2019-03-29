package com.tickettoride.facades;

import com.tickettoride.database.Database;
import com.tickettoride.database.TrainCardDAO;
import com.tickettoride.facades.helpers.GameFacadeHelper;
import com.tickettoride.facades.helpers.PlayerHelper;
import com.tickettoride.models.Color;
import com.tickettoride.models.Game;
import com.tickettoride.models.Hand;
import com.tickettoride.models.Player;
import com.tickettoride.models.TrainCard;
import com.tickettoride.models.TrainCardDeck;
import com.tickettoride.models.idtypes.GameID;
import com.tickettoride.models.idtypes.PlayerID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.crypto.Data;

import command.Command;
import exceptions.DatabaseException;

public class TrainCardFacade extends BaseFacade {
    private static TrainCardFacade SINGLETON = new TrainCardFacade();
    public static TrainCardFacade getSingleton() { return SINGLETON; }
    private static String CONTROLLER_NAME = "TrainCardController";
    private static Logger logger = LogManager.getLogger(TrainCardFacade.class.getName());
    private TrainCardFacade() {}

    public void initialize(UUID connID, GameID gameID) throws DatabaseException {
        TrainCardDeck deck = new TrainCardDeck();

        try {
            initializeDeck(gameID, deck);
            int deckSize = getDeckSize(gameID);

            for (Player player: PlayerHelper.getSingleton().getGamePlayers(gameID)){
                Hand hand = getInitialHand(gameID, player.getPlayerID());

                Command command = new Command(CONTROLLER_NAME, "initializeHand", player.getPlayerID(), hand);
                sendResponseToRoom(connID, command);
            }

            Command command = new Command(CONTROLLER_NAME, "initializeDecks", deck.getFaceUpDeck(), deckSize);
            sendResponseToRoom(connID, command);
        } catch (Throwable t) { logger.error(t.getMessage(), t); }
    }

    public void rejoin(UUID connID, GameID gameID) throws DatabaseException {
        try (Database database = new Database()){
            TrainCardDAO dao = database.getTrainCardDAO();

            List<TrainCard> faceUp = dao.getFaceUpDeck(gameID);
            int deckSize = getDeckSize(gameID);

            for (Player player: PlayerHelper.getSingleton().getGamePlayers(gameID)){
                Hand hand = dao.getPlayerHand(player.getPlayerID());

                Command command = new Command(CONTROLLER_NAME, "initializeHand", player.getPlayerID(), hand);
                sendResponseToRoom(connID, command);
            }

            Command command = new Command(CONTROLLER_NAME, "initializeDecks", faceUp, deckSize);
            sendResponseToRoom(connID, command);
        } catch (Throwable t) { logger.error(t.getMessage(), t); }
    }

    public void drawFromFaceUp(UUID connID, GameID gameID, PlayerID playerID, Integer pos){
        try (Database database = new Database()){
            TrainCardDAO dao = database.getTrainCardDAO();

            TrainCard card = dao.drawFromFaceUp(gameID, playerID, pos);
            int deckSize = getDeckSize(gameID);
            List<TrainCard> faceUp = dao.getFaceUpDeck(gameID);
            database.commit();

            Command command = new Command(CONTROLLER_NAME, "drawFromFaceUp", playerID, card, faceUp, deckSize);
            sendResponseToRoom(connID, command);
        } catch (Throwable t) { logger.error(t.getMessage(), t); }
    }

    public void drawFromFaceDown(UUID connID, GameID gameID, PlayerID playerID){
        try (Database database = new Database()){
            TrainCardDAO dao = database.getTrainCardDAO();

            TrainCard card = dao.drawFromFaceDown(gameID, playerID);
            int deckSize = getDeckSize(gameID);
            database.commit();

            Command command = new Command(CONTROLLER_NAME, "drawFromFaceDown", playerID, card, deckSize);
            sendResponseToRoom(connID, command);
        } catch (Throwable t) { logger.error(t.getMessage(), t); }
    }

    public void finish(UUID connID, GameID gameID) throws DatabaseException {
        Game game = GameFacadeHelper.getSingleton().findGame(gameID);
        game = GameFacadeHelper.getSingleton().updateGameTurn(game);
        Command command = new Command(CONTROLLER_NAME, "finish", game.getCurTurn());
        sendResponseToRoom(connID, command);
    }

    public void initializeDeck(GameID gameID, TrainCardDeck deck) throws DatabaseException {
        try (Database database = new Database()) {
            TrainCardDAO dao = database.getTrainCardDAO();
            dao.addDeck(gameID, deck);
            database.commit();
        }
    }

    public Hand getInitialHand(GameID gameID, PlayerID playerID) throws DatabaseException {
        try (Database database = new Database()) {
            TrainCardDAO dao = database.getTrainCardDAO();
            Hand hand = dao.makeHand(gameID, playerID);
            database.commit();
            return hand;
        }
    }

    public int getDeckSize(GameID gameID) throws DatabaseException{
        try (Database database = new Database()) {
            TrainCardDAO dao = database.getTrainCardDAO();
            return dao.getFaceDownDeckSize(gameID);
        }
    }

    public void discard(PlayerID playerID, Color color, int colorCards, int wildCards) throws DatabaseException {
        try (Database database = new Database()) {
            TrainCardDAO dao = database.getTrainCardDAO();

            dao.discardCards(color, colorCards, wildCards, playerID);
            database.commit();
        }
    }
}
