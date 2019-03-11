package com.tickettoride.facades.helpers;

import com.tickettoride.database.Database;
import com.tickettoride.database.DestinationCardDAO;
import com.tickettoride.facades.BaseFacade;
import com.tickettoride.facades.GameFacade;
import com.tickettoride.models.DestinationCard;
import com.tickettoride.models.Player;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Queue;
import java.util.UUID;

import command.Command;
import exceptions.DatabaseException;

public class DestinationCardFacadeHelper extends BaseFacade {
    private static DestinationCardFacadeHelper SINGLETON = new DestinationCardFacadeHelper();
    public static DestinationCardFacadeHelper getSingleton() { return SINGLETON; }
    private static String CONTROLLER_NAME = "DestinationCardController";
    private static Logger logger = LogManager.getLogger(GameFacade.class.getName());
    private DestinationCardFacadeHelper() {}

    public Queue<DestinationCard> gameDestinationCards(UUID gameID) throws DatabaseException {
        try (var db = new Database()) {
            DestinationCardDAO dao = db.getDestinationCardDAO();
            return dao.getDeckForGame(gameID);
        }
    }

    public void offerCardsToPlayer(Player player, List<DestinationCard> destinationCards) throws DatabaseException {
        try (var db = new Database()) {
            DestinationCardDAO dao = db.getDestinationCardDAO();
            dao.offerCardsToPlayer(player, destinationCards);
            db.commit();
        }
    }

    public void acceptCardsForPlayer(Player player, List<DestinationCard> destinationCards) throws DatabaseException {
        try (var db = new Database()) {
            DestinationCardDAO dao = db.getDestinationCardDAO();
            dao.acceptCards(player, destinationCards);
            db.commit();
        }
    }

    public List<DestinationCard> destinationCardsInPlayersHand(Player player) throws DatabaseException {
        try (var db = new Database()) {
            DestinationCardDAO dao = db.getDestinationCardDAO();
            return dao.getPlayerHand(player);
        }
    }

    public Command offerDestinationCards(Player player, List<DestinationCard> offeredCards, int requiredToKeep) {
        return new Command(CONTROLLER_NAME, "offerDestinationCards",
                player, offeredCards, requiredToKeep);
    }

    public Queue<DestinationCard> destinationCardsinGameDeck(UUID gameID) throws DatabaseException {
        try (var db = new Database()) {
            DestinationCardDAO dao = db.getDestinationCardDAO();
            return dao.getDeckForGame(gameID);
        }
    }
}
