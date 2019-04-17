package com.tickettoride.database;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.tickettoride.database.interfaces.IDestinationCardDAO;
import com.tickettoride.models.City;
import com.tickettoride.models.DestinationCard;
import com.tickettoride.models.Game;
import com.tickettoride.models.Player;
import com.tickettoride.models.idtypes.GameID;

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
            document.append("gameid", gameID);
            document.append("destination1", card.getDestination1().name());
            document.append("destination2", card.getDestination2().name());
            document.append("pointvalue", card.getPointValue().asInt());
            document.append("sequenceposition", deckPosition);
            document.append("state", "inDeck");
            document.append("playerid", "NULL");

            MongoCollection collection = getCollection();
            List<Object> parameters = new ArrayList<>();
            parameters.add(document);
            MongoCommand mongoCommand = new MongoCommand(collection, Database.INSERT_METHOD_NAME, parameters);
            Database.addCommand(mongoCommand);
            destinationCardList.add(card);
        }
    }

    @Override
    public Queue<DestinationCard> getDeckForGame(Game game) throws DatabaseException {
        return getDeckForGame(game.getGameID());
    }

    @Override
    public Queue<DestinationCard> getDeckForGame(GameID gameID) throws DatabaseException {
        Queue<DestinationCard> deck = new ArrayDeque<>();

        FindIterable<Document> iterCards = getCollection().find();
        Iterator iter = iterCards.iterator();
        List<DestinationCard> destinationCards = new ArrayList<>();
        while(iter.hasNext()) {
            Document doc = (Document) iter.next();
            if(doc.getString("gameid").equals(gameID.toString())
                    && doc.getString("state").equals("inDeck")) {
                deck.add(buildCardFromDocument(doc));
                logger.debug(Integer.toString(doc.getInteger("sequenceposition")));
            }
        }
        return deck;
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
                    Updates.set("sequenceposition", "NULL"),
                    Updates.set("state", "offeredToPlayer"),
                    Updates.set("playerid", player.getPlayerID().toString()));

            Bson filters2 = Filters.and(
                    Filters.eq("gameid", player.getGameID().toString()),
                    Filters.eq("state", "inDeck"));
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
        }
    }

    @Override
    public List<DestinationCard> getPlayerHand(Player player) throws DatabaseException {
        List<DestinationCard> hand = new ArrayList<>();

        FindIterable<Document> iterCards = getCollection().find();
        Iterator iter = iterCards.iterator();
        while(iter.hasNext()) {
            Document doc = (Document) iter.next();
            if(doc.getString("playerid").equals(player.getPlayerID().toString())
                    && doc.getString("state").equals("inPlayerHand")) {
                hand.add(buildCardFromDocument(doc));
            }
        }

        return hand;
    }

    @Override
    public void acceptCards(Player player, Collection<DestinationCard> acceptedCards) throws DatabaseException {
        var offeredCards = getOfferedCards(player);
        assert offeredCards.containsAll(acceptedCards);
        offeredCards.removeAll(acceptedCards);

        Bson filters1 = Filters.eq("state", "offeredToPlayer");
        Bson updates1 = Updates.combine(
                Updates.set("state", "inPlayerHand"),
                Updates.set("sequencePosition", "NULL"));

        Bson filters2 = Filters.eq("gameid", player.getGameID());
        Bson updates2 = Updates.combine(
                Updates.set("state", "inDeck"),
                Updates.set("playerid", "NULL"),
                Updates.set("sequenceposition", getMaxSequencePosition(player.getGameID()) + 1));

        runCardUpdate(player, acceptedCards, filters1, updates1);
        runCardUpdate(player, offeredCards, filters2, updates2);
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

        FindIterable<Document> iterCards = getCollection().find();
        Iterator iter = iterCards.iterator();
        while(iter.hasNext()) {
            Document doc = (Document) iter.next();
            if(doc.getString("gameid").equals(gameID.toString())
                    && doc.getString("state").equals("inDeck")) {
                int curPos = doc.getInteger("sequenceposition");
                if (curPos > pos) {
                    pos = curPos;
                }
            }
        }

        return pos;
    }

    @Override
    public List<DestinationCard> getOfferedCards(Player player) throws DatabaseException {
        List<DestinationCard> offeredCards = new ArrayList<>();

        FindIterable<Document> iterCards = getCollection().find();
        Iterator iter = iterCards.iterator();
        while(iter.hasNext()) {
            Document doc = (Document) iter.next();
            if(doc.getString("playerid").equals(player.getPlayerID().toString())
                    && doc.getString("state").equals("offeredToPlayer")) {
                offeredCards.add(buildCardFromDocument(doc));
            }
        }

        return offeredCards;
    }

    private DestinationCard buildCardFromDocument(Document doc) {

        var destination1 = City.valueOf(doc.getString("destination1"));
        var destination2 = City.valueOf(doc.getString("destination2"));
        var pointValue = DestinationCard.Value.fromInt(doc.getInteger("pointValue"));

        return new DestinationCard(destination1, destination2, pointValue);
    }
}
