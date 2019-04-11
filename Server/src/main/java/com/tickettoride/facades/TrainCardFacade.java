package com.tickettoride.facades;

import com.tickettoride.database.DatabaseProvider;
import com.tickettoride.database.interfaces.IDatabase;
import com.tickettoride.database.interfaces.ITrainCardDAO;
import com.tickettoride.facades.helpers.GameFacadeHelper;
import com.tickettoride.facades.helpers.PlayerHelper;
import com.tickettoride.facades.helpers.PlayerStateHelper;
import com.tickettoride.models.*;
import com.tickettoride.models.idtypes.GameID;
import com.tickettoride.models.idtypes.PlayerID;
import command.Command;
import exceptions.DatabaseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.UUID;

public class TrainCardFacade extends BaseFacade {
    private static TrainCardFacade SINGLETON = new TrainCardFacade();
    public static TrainCardFacade getSingleton() { return SINGLETON; }
    private static String CONTROLLER_NAME = "TrainCardController";
    private static Logger logger = LogManager.getLogger(TrainCardFacade.class.getName());
    private TrainCardFacade() {}

    public void incrementToDrawTrainsState(UUID connID, Player player) {
        try {
            PlayerState playerState = PlayerStateHelper.getSingleton().getPlayerState(player.getPlayerID());
            PlayerState newPlayerState = playerState.moveToDrawTrainCardsState();
            PlayerStateHelper.getSingleton().updatePlayerState(newPlayerState);
            Command command = new Command("GameController", "setGameState", newPlayerState);
            sendResponseToRoom(connID, command);
        } catch (Throwable throwable) {
            logger.error(throwable.getMessage(), throwable);
        }
    }

    public void initialize(UUID connID, GameID gameID) throws DatabaseException {
        TrainCardDeck deck = new TrainCardDeck();

        try (IDatabase IDatabase = DatabaseProvider.getDatabase()){
            if (IDatabase.getTrainCardDAO().hasGameInfo(gameID)){
                return;
            }

            initializeDeck(gameID, deck);

            for (Player player: PlayerHelper.getSingleton().getGamePlayers(gameID)){
                Hand hand = getInitialHand(gameID, player.getPlayerID());

                Command command = new Command(CONTROLLER_NAME, "initializeHand", player.getPlayerID(), hand);
                sendResponseToRoom(connID, command);
            }

            int deckSize = IDatabase.getTrainCardDAO().getFaceDownDeckSize(gameID);

            Command command = new Command(CONTROLLER_NAME, "initializeDecks", deck.getFaceUpDeck(), deckSize);
            sendResponseToRoom(connID, command);
        } catch (Throwable t) { logger.error(t.getMessage(), t); }
    }

    public void rejoin(UUID connID, GameID gameID) throws DatabaseException {
        try (IDatabase IDatabase = DatabaseProvider.getDatabase()){
            ITrainCardDAO dao = IDatabase.getTrainCardDAO();

            List<TrainCard> faceUp = dao.getFaceUpDeck(gameID);

            for (Player player: PlayerHelper.getSingleton().getGamePlayers(gameID)){
                Hand hand = dao.getPlayerHand(player.getPlayerID());

                Command command = new Command(CONTROLLER_NAME, "initializeHand", player.getPlayerID(), hand);
                sendResponseToRoom(connID, command);
            }

            int deckSize = dao.getFaceDownDeckSize(gameID);

            Command command = new Command(CONTROLLER_NAME, "initializeDecks", faceUp, deckSize);
            sendResponseToRoom(connID, command);
        } catch (Throwable t) { logger.error(t.getMessage(), t); }
    }

    public void drawFromFaceUp(UUID connID, GameID gameID, PlayerID playerID, Integer pos){
        try (IDatabase IDatabase = DatabaseProvider.getDatabase()){
            ITrainCardDAO dao = IDatabase.getTrainCardDAO();

            TrainCard card = dao.drawFromFaceUp(gameID, playerID, pos);
            List<TrainCard> faceUp = dao.getFaceUpDeck(gameID);
            IDatabase.commit();

            int deckSize = dao.getFaceDownDeckSize(gameID);

            Command command = new Command(CONTROLLER_NAME, "drawFromFaceUp", playerID, card, faceUp, deckSize);
            sendResponseToRoom(connID, command);
            updateHistory(connID, playerID, "Drew a " + card.getColor().name() + " train card");
        } catch (Throwable t) { logger.error(t.getMessage(), t); }
    }

    public void drawFromFaceDown(UUID connID, GameID gameID, PlayerID playerID){
        try (IDatabase IDatabase = DatabaseProvider.getDatabase()){
            ITrainCardDAO dao = IDatabase.getTrainCardDAO();

            TrainCard card = dao.drawFromFaceDown(gameID, playerID);

            IDatabase.commit();

            int deckSize = dao.getFaceDownDeckSize(gameID);

            Command command = new Command(CONTROLLER_NAME, "drawFromFaceDown", playerID, card, deckSize);
            sendResponseToRoom(connID, command);
            updateHistory(connID, playerID, "Drew a train card");
        } catch (Throwable t) { logger.error(t.getMessage(), t); }
    }

    public void finish(UUID connID, GameID gameID) {
        try {
            Game game = GameFacadeHelper.getSingleton().findGame(gameID);
            game = GameFacadeHelper.getSingleton().updateGameTurn(game);
            List<PlayerState> playerStates = PlayerStateHelper.getSingleton().incrementGamePlayerStates(game);
            Command command = new Command(CONTROLLER_NAME, "finish", game.getCurTurn(), playerStates);
            sendResponseToRoom(connID, command);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            //todo: send message somewhere that it failed
        }
    }

    public void initializeDeck(GameID gameID, TrainCardDeck deck) throws DatabaseException {
        try (IDatabase IDatabase = DatabaseProvider.getDatabase()) {
            ITrainCardDAO dao = IDatabase.getTrainCardDAO();
            dao.addDeck(gameID, deck);
            IDatabase.commit();
        }
    }

    public Hand getInitialHand(GameID gameID, PlayerID playerID) throws DatabaseException {
        try (IDatabase IDatabase = DatabaseProvider.getDatabase()) {
            ITrainCardDAO dao = IDatabase.getTrainCardDAO();
            Hand hand = dao.makeHand(gameID, playerID);
            IDatabase.commit();
            return hand;
        }
    }

    public int getDeckSize(GameID gameID) throws DatabaseException{
        try (IDatabase IDatabase = DatabaseProvider.getDatabase()) {
            ITrainCardDAO dao = IDatabase.getTrainCardDAO();
            return dao.getFaceDownDeckSize(gameID);
        }
    }

    public void discard(PlayerID playerID, Color color, int colorCards, int wildCards) throws DatabaseException {
        try (IDatabase IDatabase = DatabaseProvider.getDatabase()) {
            ITrainCardDAO dao = IDatabase.getTrainCardDAO();

            dao.discardCards(color, colorCards, wildCards, playerID);
            IDatabase.commit();
        }
    }
}
