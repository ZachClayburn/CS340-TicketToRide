package com.tickettoride.facades;

import com.tickettoride.facades.helpers.GameFacadeHelper;
import com.tickettoride.facades.helpers.PlayerHelper;
import com.tickettoride.models.Game;
import com.tickettoride.models.Hand;
import com.tickettoride.models.Player;
import com.tickettoride.models.TrainCardDeck;
import com.tickettoride.models.idtypes.GameID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

        for (Player player: PlayerHelper.getSingleton().getGamePlayers(gameID)){
            Hand hand = deck.getInitialHand();
            // Add hand info for each player in database

            Command command = new Command(CONTROLLER_NAME, "initializeHand", player.getPlayerID(), hand);
            sendResponseToRoom(connID, command);
        }

        // Add deck info (facedown, faceup) to database
        Command command = new Command(CONTROLLER_NAME, "initializeDecks", deck.getFaceUpDeck(), deck.getFaceDownDeck(), deck.getDiscardPile());
        sendResponseToRoom(connID, command);
    }
}
