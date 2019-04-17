package com.tickettoride.database;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.tickettoride.database.interfaces.IDestinationCardDAO;
import com.tickettoride.models.CardState;
import com.tickettoride.models.City;
import com.tickettoride.models.DestinationCard;
import com.tickettoride.models.Game;
import com.tickettoride.models.Player;
import com.tickettoride.models.idtypes.GameID;
import com.tickettoride.models.idtypes.PlayerID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.BSON;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.UUID;

import exceptions.DatabaseException;

public class DestinationCardDAO extends Database.DataAccessObject implements IDestinationCardDAO {

    private List<DestinationCard> destinationCardList;

    private static final Logger logger = LogManager.getLogger(DestinationCardDAO.class.getName());

    public DestinationCardDAO(MongoDatabase database) {
        super(database);
        collectionName = "destinationcards";
        destinationCardList = DataManager.getDestinationCardList();
    }

    @Override
    public void initializeData() {
        List<DestinationCard> cardList = DataManager.getDestinationCardList();
        List<DestinationCard> allCards = allCards();
        for (DestinationCard card: allCards) {
            cardList.add(card);
        }
    }

    public List<DestinationCard> allCards(){
        FindIterable<Document> iterCards = getCollection().find();
        Iterator iter = iterCards.iterator();
        List<DestinationCard> cards = new ArrayList<>();
        while(iter.hasNext()) {
            Document doc = (Document) iter.next();
            cards.add(buildCardFromDocument(doc));
        }
        return cards;
    }

    @Override
    public void addDeck(Game game, Queue<DestinationCard> deck) throws DatabaseException {
        addDeck(game.getGameID(), deck);
    }

    @Override
    public void addDeck(GameID gameID, Queue<DestinationCard> deck) throws DatabaseException {
        deck = new ArrayDeque<>(deck);

        for (int deckPosition = 0; !deck.isEmpty(); deckPosition++) {
            var card = deck.remove();

            Document document = new Document();
            document.append("gameid", gameID.toString());
            document.append("destination1", card.getDestination1().name());
            document.append("destination2", card.getDestination2().name());
            document.append("pointvalue", card.getPointValue().asInt());
            document.append("sequenceposition", deckPosition);
            document.append("state", CardState.IN_DECK.name());
            document.append("playerid", "NULL");

            MongoCollection collection = getCollection();
            List<Object> parameters = new ArrayList<>();
            parameters.add(document);
            MongoCommand mongoCommand = new MongoCommand(collection, Database.INSERT_METHOD_NAME, parameters);
            Database.addCommand(mongoCommand);

            card.setGameID(gameID);
            card.setSequencePosition(deckPosition);
            card.setPlayerID(null);
            card.setState(CardState.IN_DECK);
            destinationCardList.add(card);
        }
    }

    @Override
    public Queue<DestinationCard> getDeckForGame(Game game) throws DatabaseException {
        return getDeckForGame(game.getGameID());
    }

    @Override
    public Queue<DestinationCard> getDeckForGame(GameID gameID) throws DatabaseException {
        List<DestinationCard> deck = new ArrayList<>();

        for (DestinationCard card: destinationCardList) {
            if (card.getGameID().equals(gameID)
                    && card.getState().equals(CardState.IN_DECK)) {
                deck.add(card);
            }
        }
        return reorderCards(deck);
    }

    private Queue<DestinationCard> reorderCards(List<DestinationCard> unordered){
        Queue<DestinationCard> ordered = new ArrayDeque<>();

        while (ordered.size() != unordered.size()){
            for (DestinationCard card: unordered){
                if (card.getSequencePosition() == ordered.size()){
                    ordered.add(card);
                }
            }
        }

        return ordered;
    }

    @Override
    public void offerCardsToPlayer(Player player, Collection<DestinationCard> cards) throws DatabaseException {

        // TODO: Add table constraints? How necessary is it?

        for (DestinationCard card: cards) {
            Bson filters1 = Filters.and(
                    Filters.eq("gameid", player.getGameID().toString()),
                    Filters.eq("destination1", card.getDestination1().name()),
                    Filters.eq("destination2", card.getDestination2().name()));
            Bson updates1 = Updates.combine(
                    Updates.set("sequenceposition", 0),
                    Updates.set("state", CardState.OFFERED_TO_PLAYER.name()),
                    Updates.set("playerid", player.getPlayerID().toString()));

            Bson filters2 = Filters.and(
                    Filters.eq("gameid", player.getGameID().toString()),
                    Filters.eq("state", CardState.IN_DECK.name()));
            Bson updates2 = Updates.inc("sequenceposition", -1);

            MongoCollection collection = getCollection();
            List<Object> parameters1 = new ArrayList<>();
            List<Object> parameters2 = new ArrayList<>();
            parameters1.add(filters1);
            parameters1.add(updates1);
            parameters2.add(filters2);
            parameters2.add(updates2);
            MongoCommand mongoCommand1 = new MongoCommand(collection, Database.UPDATE_METHOD_NAME, parameters1);
            MongoCommand mongoCommand2 = new MongoCommand(collection, Database.UPDATE_METHOD_NAME, parameters2);
            Database.addCommand(mongoCommand1);
            Database.addCommand(mongoCommand2);

            updateDataManagerMakeOffer(card, player);
            updateDataManagerAdjustPos(player.getGameID());
        }
    }

    @Override
    public List<DestinationCard> getPlayerHand(Player player) throws DatabaseException {
        List<DestinationCard> hand = new ArrayList<>();

        for (DestinationCard card: destinationCardList) {
            if (card.getPlayerID() == null) {
                continue;
            }
            if (card.getPlayerID().equals(player.getPlayerID())
                    && card.getState().equals(CardState.IN_PLAYER_HAND)) {
                hand.add(card);
            }
        }

        return hand;
    }

    @Override
    public void acceptCards(Player player, Collection<DestinationCard> acceptedCards) throws DatabaseException {
        var offeredCards = getOfferedCards(player);
        assert offeredCards.containsAll(acceptedCards);
        offeredCards.removeAll(acceptedCards);

        Bson filters1 = Filters.eq("state", CardState.OFFERED_TO_PLAYER.name());
        Bson updates1 = Updates.combine(
                Updates.set("state", CardState.IN_PLAYER_HAND.name()),
                Updates.set("sequencePosition", 0));

        Bson filters2 = Filters.eq("gameid", player.getGameID().toString());
        Bson updates2 = Updates.combine(
                Updates.set("state", CardState.IN_DECK.name()),
                Updates.set("playerid", "NULL"),
                Updates.set("sequenceposition", getMaxSequencePosition(player.getGameID()) + 1));

        runCardUpdate(player, acceptedCards, filters1, updates1);
        runCardUpdate(player, offeredCards, filters2, updates2);

        updateDataManagerAccepted(player.getPlayerID(), acceptedCards);
        updateDataManagerOffered(player.getPlayerID(), player.getGameID(), offeredCards);
    }

    private void runCardUpdate(Player player, Collection<DestinationCard> cards, Bson filters, Bson updates) {
        for (var card : cards) {
            Bson filtersFinal = Filters.and(
                    filters,
                    Filters.eq("playerid", player.getPlayerID().toString()),
                    Filters.eq("destination1", card.getDestination1().name()),
                    Filters.eq("destination2", card.getDestination2().name()));

            MongoCollection collection = getCollection();
            List<Object> parameters = new ArrayList<>();
            parameters.add(filtersFinal);
            parameters.add(updates);
            MongoCommand mongoCommand = new MongoCommand(collection, Database.UPDATE_METHOD_NAME, parameters);
            Database.addCommand(mongoCommand);
        }
    }

    private int getMaxSequencePosition(GameID gameID) {
        int pos = 0;

        for (DestinationCard card: destinationCardList) {
            if (card.getGameID().equals(gameID)
                    && card.getState().equals(CardState.IN_DECK)) {
                if (card.getSequencePosition() > pos){
                    pos = card.getSequencePosition();
                }
            }
        }

        return pos;
    }

    @Override
    public List<DestinationCard> getOfferedCards(Player player) throws DatabaseException {
        List<DestinationCard> offeredCards = new ArrayList<>();
        for (DestinationCard card: destinationCardList) {
            if (card.getPlayerID() == null){
                continue;
            }
            if (card.getPlayerID().equals(player.getPlayerID())
                    && card.getState().equals(CardState.OFFERED_TO_PLAYER)) {
                offeredCards.add(card);
            }
        }

        return offeredCards;
    }

    private void updateDataManagerMakeOffer(DestinationCard curCard, Player player){
        for (DestinationCard card: destinationCardList) {
            if (card.getGameID().equals(player.getGameID())
                    && card.getDestination1().equals(curCard.getDestination1())
                    && card.getDestination2().equals(curCard.getDestination2())) {
                card.setSequencePosition(0);
                card.setState(CardState.OFFERED_TO_PLAYER);
                card.setPlayerID(player.getPlayerID());
            }
        }
    }

    private void updateDataManagerAdjustPos(GameID gameID){
        for (DestinationCard card: destinationCardList) {
            if (card.getGameID().equals(gameID)
                    && card.getState().equals(CardState.IN_DECK)) {
                card.setSequencePosition(card.getSequencePosition() - 1);
            }
        }
    }

    private void updateDataManagerAccepted(PlayerID playerID, Collection<DestinationCard> cards){
        for (var curCard : cards) {
            for (DestinationCard card: destinationCardList) {
                if (card.getPlayerID() == null) {
                    continue;
                }
                if (card.getState().equals(CardState.OFFERED_TO_PLAYER)
                        && card.getPlayerID().equals(playerID)
                        && card.getDestination1().equals(curCard.getDestination1())
                        && card.getDestination2().equals(curCard.getDestination2())) {
                    card.setState(CardState.IN_PLAYER_HAND);
                    card.setSequencePosition(0);
                }
            }
        }
    }

    private void updateDataManagerOffered(PlayerID playerID, GameID gameID, Collection<DestinationCard> cards){
        for (var curCard : cards) {
            for (DestinationCard card: destinationCardList) {
                if (card.getPlayerID() == null) {
                    continue;
                }
                if (card.getPlayerID().equals(playerID)
                        && card.getDestination1().equals(curCard.getDestination1())
                        && card.getDestination2().equals(curCard.getDestination2())) {
                    card.setState(CardState.IN_DECK);
                    card.setPlayerID(null);
                    card.setSequencePosition(getMaxSequencePosition(gameID) + 1);
                }
            }
        }
    }

    private DestinationCard buildCardFromDocument(Document doc) {

        var destination1 = City.valueOf(doc.getString("destination1"));
        var destination2 = City.valueOf(doc.getString("destination2"));
        var pointValue = DestinationCard.Value.fromInt(doc.getInteger("pointvalue"));
        var gameID = GameID.fromString(doc.getString("gameid"));
        var sequencePosition = doc.getInteger("sequenceposition");
        var state = CardState.valueOf(doc.getString("state"));
        PlayerID playerID = null;
        if (!doc.getString("playerid").equals("NULL")) playerID = PlayerID.fromString(doc.getString("playerid"));

        return new DestinationCard(destination1, destination2, gameID, sequencePosition, state, playerID, pointValue);
    }
}
