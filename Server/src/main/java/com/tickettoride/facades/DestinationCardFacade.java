package com.tickettoride.facades;

import com.google.gson.internal.LinkedTreeMap;
import com.tickettoride.database.Database;
import com.tickettoride.facades.helpers.DestinationCardFacadeHelper;
import com.tickettoride.facades.helpers.GameFacadeHelper;
import com.tickettoride.facades.helpers.PlayerStateHelper;
import com.tickettoride.models.DestinationCard;
import com.tickettoride.models.Game;
import com.tickettoride.models.Player;
import com.tickettoride.models.PlayerState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.ArrayList;
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

    public void incrementToDrawDestinationState(UUID connID, Player player) {
        try {
            PlayerState playerState = PlayerStateHelper.getSingleton().getPlayerState(player.getPlayerID());
            PlayerState newPlayerState = playerState.moveToDrawDestinationCardsState();
            PlayerStateHelper.getSingleton().updatePlayerState(newPlayerState);
            Command command = new Command("GameController", "setGameState", newPlayerState);
            sendResponseToRoom(connID, command);
        } catch (Throwable throwable) {
            logger.error(throwable.getMessage(), throwable);
        }
    }

    public void drawDestinationCards(UUID connID, Player player, Integer cardsToKeep) {
        logger.debug("Player " + player.getUsername() + " is drawing cards");
        try {
            Queue<DestinationCard> destinationDeck = DestinationCardFacadeHelper.getSingleton().gameDestinationCards(player.getGameID());
            List<DestinationCard> offeredCards = new ArrayList<>();
            int drawnCards = 1;
            offeredCards.add(destinationDeck.poll());
            if (!destinationDeck.isEmpty()){
                offeredCards.add(destinationDeck.poll());
                drawnCards++;
            }
            if (!destinationDeck.isEmpty()){
                offeredCards.add(destinationDeck.poll());
                drawnCards++;
            }
            DestinationCardFacadeHelper.getSingleton().offerCardsToPlayer(player, offeredCards);
            Command command = DestinationCardFacadeHelper.getSingleton().offerDestinationCards(player, offeredCards, cardsToKeep);
            sendResponseToRoom(connID, command);
            String event="Drew " + drawnCards + " destination cards";
            updateHistory(connID,player.getPlayerID(),event);
        } catch (Throwable throwable) {
            logger.error(throwable.getMessage(),throwable);//FIXME add proper error handling
        }
    }

    public void acceptDestinationCards(UUID connID, Player player, ArrayList<LinkedTreeMap> gsonCards, Boolean incrementTurn) {
        try {
            Game game = GameFacadeHelper.getSingleton().findGame(player.getGameID());
            List<DestinationCard> acceptedCards = DestinationCard.unGsonCards(gsonCards);
            Queue<DestinationCard> gameDeck = DestinationCardFacadeHelper.getSingleton().destinationCardsinGameDeck(game.getGameID());
            int deckCount = gameDeck.size();
            DestinationCardFacadeHelper.getSingleton().acceptCardsForPlayer(player, acceptedCards);
            List<PlayerState> playerStates = null;
            if (incrementTurn) {
                game = GameFacadeHelper.getSingleton().updateGameTurn(game);
                playerStates = PlayerStateHelper.getSingleton().incrementGamePlayerStates(game);
            } else {
                playerStates = PlayerStateHelper.getSingleton().gamePlayerStates(game.getGameID());
            }
            playerStates = PlayerStateHelper.getSingleton().incrementInitialPlayerState(playerStates, player, game.getCurTurn());
            Command command = new Command(CONTROLLER_NAME, "setPlayerAcceptedCards", player, acceptedCards, deckCount, game.getCurTurn(), playerStates);
            sendResponseToRoom(connID, command);
            String event="Kept "+acceptedCards.size()+" destination cards.";
            updateHistory(connID,player.getPlayerID(),event);
        } catch (Throwable throwable) {
            logger.error(throwable.getMessage(),throwable);//FIXME add proper error handling
        }
    }

    public void getOfferedCards(UUID conID, Player player, Integer requiredToKeep){

        try (var db = new Database()) {

            var offeredCards = db.getDestinationCardDAO().getOfferedCards(player);

            sendResponseToRoom(conID,
                    new Command(CONTROLLER_NAME, "offerDestinationCards", player, offeredCards, requiredToKeep)
            );

        } catch (DatabaseException e) {
            logger.error(e.getMessage(),e);
            logger.error(e);//FIXME add proper error handling
        }
    }
}
