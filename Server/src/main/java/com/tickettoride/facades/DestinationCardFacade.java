package com.tickettoride.facades;

import com.google.gson.internal.LinkedTreeMap;
import com.tickettoride.command.ServerCommunicator;
import com.tickettoride.database.Database;
import com.tickettoride.facades.helpers.DestinationCardFacadeHelper;
import com.tickettoride.facades.helpers.GameFacadeHelper;
import com.tickettoride.models.City;
import com.tickettoride.models.DestinationCard;
import com.tickettoride.models.Game;
import com.tickettoride.models.Player;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.UUID;

import command.Command;
import exceptions.DatabaseException;

public class DestinationCardFacade extends BaseFacade {

    private static String CONTROLLER_NAME = "DestinationCardController";
    private static Logger logger = LogManager.getLogger(DestinationCardFacade.class.getName());
    private static DestinationCardFacade SINGLETON = new DestinationCardFacade();

    public static DestinationCardFacade getSingleton() {
        return SINGLETON;
    }

    private DestinationCardFacade() {}

    public void drawDestinationCards(UUID connID, Player player, Integer cardsToKeep) {
        logger.debug("Player " + player.getUsername() + " is drawing cards");
        try {
            Queue<DestinationCard> destinationDeck = DestinationCardFacadeHelper.getSingleton().gameDestinationCards(player.getGameID());
            List<DestinationCard> offeredCards = new ArrayList<>();
            offeredCards.add(destinationDeck.poll());
            offeredCards.add(destinationDeck.poll());
            offeredCards.add(destinationDeck.poll());
            DestinationCardFacadeHelper.getSingleton().offerCardsToPlayer(player, offeredCards);
            Command command = DestinationCardFacadeHelper.getSingleton().offerDestinationCards(player, offeredCards, cardsToKeep);
            sendResponseToRoom(connID, command);
        } catch (Throwable throwable) {
            logger.error(throwable);//FIXME add proper error handling
        }
    }

    public void acceptDestinationCards(UUID connID, Player player, ArrayList<LinkedTreeMap> gsonCards) {
        try {
            List<DestinationCard> acceptedCards = DestinationCard.unGsonCards(gsonCards);
            Queue<DestinationCard> gameDeck = DestinationCardFacadeHelper.getSingleton().destinationCardsinGameDeck(player.getGameID());
            int deckCount = gameDeck.size();
            DestinationCardFacadeHelper.getSingleton().acceptCardsForPlayer(player, acceptedCards);
            Command command = new Command(CONTROLLER_NAME, "setPlayerAcceptedCards", player, acceptedCards, deckCount);
            sendResponseToRoom(connID, command);
        } catch (Throwable throwable) {
            logger.error(throwable);//FIXME add proper error handling
        }
    }

    public void getOfferedCards(UUID conID, Player player, Integer requiredToKeep){

        try (var db = new Database()) {

            var offeredCards = db.getDestinationCardDAO().getOfferedCards(player);

            sendResponseToRoom(conID,
                    new Command(CONTROLLER_NAME, "offerDestinationCards", player, offeredCards, requiredToKeep)
            );

        } catch (DatabaseException e) {
            logger.error(e);//FIXME add proper error handling
        }
    }
}
