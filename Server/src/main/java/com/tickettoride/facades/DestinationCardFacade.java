package com.tickettoride.facades;

import com.tickettoride.database.Database;
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
}
