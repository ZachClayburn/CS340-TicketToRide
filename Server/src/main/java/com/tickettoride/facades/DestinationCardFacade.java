package com.tickettoride.facades;

import com.google.gson.internal.LinkedTreeMap;
import com.tickettoride.command.ServerCommunicator;
import com.tickettoride.database.Database;
import com.tickettoride.models.City;
import com.tickettoride.models.DestinationCard;
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

    public void drawDestinationCards(UUID connID, Player player) {
        logger.debug("Player " + player.getUsername() + " is drawing cards");
        try (var db = new Database()){
            var game = db.getGameDAO().getGame(player.getGameID());
            assert game != null;

            Queue<DestinationCard> destinationDeck = db.getDestinationCardDAO().getDeckForGame(game);

            List<DestinationCard> offeredCards = new ArrayList<>();

            offeredCards.add(destinationDeck.poll());
            offeredCards.add(destinationDeck.poll());
            offeredCards.add(destinationDeck.poll());

            db.getDestinationCardDAO().offerCardsToPlayer(player, offeredCards);

            var command = offerDestinationCards(player, offeredCards);
            sendResponseToRoom(connID, command);

            db.commit();

        } catch (DatabaseException e) {
            logger.catching(e);//FIXME add proper error handling
        }
    }

    public void acceptDestinationCards(UUID connID, Player player,
                                       ArrayList<LinkedTreeMap> gsonCards) {
        List<DestinationCard> acceptedCards = DestinationCard.unGsonCards(gsonCards);
        try (var db = new Database()) {
            //FIXME Come up with way to track that everyone has accepted their first cards
            db.getDestinationCardDAO().acceptCards(player, acceptedCards);

            var cmd = new Command(CONTROLLER_NAME, "setPlayerAcceptedCards",
                    player, acceptedCards);

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

    Command offerDestinationCards(Player player, List<DestinationCard> offeredCards, int requiredToKeep) {
        return offerDestinationCards(player,  offeredCards.get(0), offeredCards.get(1), offeredCards.get(2), requiredToKeep);
    }

    Command offerDestinationCards(Player player, List<DestinationCard> offeredCards) {
        return offerDestinationCards(player,  offeredCards.get(0), offeredCards.get(1), offeredCards.get(2),
                1);
    }

    Command offerDestinationCards(Player player, DestinationCard card1, DestinationCard card2, DestinationCard card3,
                                  int requiredToKeep) {
        return new Command(CONTROLLER_NAME, "offerDestinationCards",
                player, card1, card2, card3, requiredToKeep);
    }

    Command sendDestinationDeck(Queue<DestinationCard> deck) {
        return new Command(CONTROLLER_NAME, "updateDestinationDeck", deck.size());
    }
}
