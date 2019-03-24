package com.tickettoride.facades;

import com.tickettoride.database.Database;
import com.tickettoride.database.TrainCardDAO;
import com.tickettoride.facades.helpers.GameFacadeHelper;
import com.tickettoride.facades.helpers.PlayerHelper;
import com.tickettoride.models.Game;
import com.tickettoride.models.Hand;
import com.tickettoride.models.Player;
import com.tickettoride.models.TrainCard;
import com.tickettoride.models.TrainCardDeck;
import com.tickettoride.models.idtypes.GameID;
import com.tickettoride.models.idtypes.PlayerID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

            for (Player player: PlayerHelper.getSingleton().getGamePlayers(gameID)){
                Hand hand = getInitialHand(gameID, player.getPlayerID());

                Command command = new Command(CONTROLLER_NAME, "initializeHand", player.getPlayerID(), hand);
                sendResponseToRoom(connID, command);
            }

            Command command = new Command(CONTROLLER_NAME, "initializeDecks", deck.getFaceUpDeck());
            sendResponseToRoom(connID, command);
        } catch (Throwable t) { logger.error(t.getMessage(), t); }
    }

    public void drawFromFaceUp(UUID connID, GameID gameID, PlayerID playerID, int pos){
        try (Database database = new Database()){
            TrainCardDAO dao = database.getTrainCardDAO();

            TrainCard card = dao.drawFromFaceUp(gameID, playerID, pos);

            List<TrainCard> faceUp = dao.getFaceUpDeck(gameID);

            Command command = new Command(CONTROLLER_NAME, "drawFromFaceUp", playerID, card, faceUp);
            sendResponseToRoom(connID, command);
        } catch (Throwable t) { logger.error(t.getMessage(), t); }
    }

    public void drawFromFaceDown(UUID connID, GameID gameID, PlayerID playerID){
        try (Database database = new Database()){
            TrainCardDAO dao = database.getTrainCardDAO();

            TrainCard card = dao.drawFromFaceDown(gameID, playerID);

            Command command = new Command(CONTROLLER_NAME, "drawFromFaceDown", playerID, card);
            sendResponseToRoom(connID, command);
        } catch (Throwable t) { logger.error(t.getMessage(), t); }
    }

    public void initializeDeck(GameID gameID, TrainCardDeck deck) throws DatabaseException {
        try (Database database = new Database()) {
            TrainCardDAO dao = database.getTrainCardDAO();
            dao.addDeck(gameID, deck);
        }
    }

    public Hand getInitialHand(GameID gameID, PlayerID playerID) throws DatabaseException {
        try (Database database = new Database()) {
            TrainCardDAO dao = database.getTrainCardDAO();
            return dao.makeHand(gameID, playerID);
        }
    }
}
