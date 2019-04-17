package com.tickettoride.database;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.tickettoride.database.interfaces.ITrainCardDAO;
import com.tickettoride.models.CardState;
import com.tickettoride.models.Color;
import com.tickettoride.models.Hand;
import com.tickettoride.models.Player;
import com.tickettoride.models.TrainCard;
import com.tickettoride.models.TrainCardDeck;
import com.tickettoride.models.idtypes.GameID;
import com.tickettoride.models.idtypes.PlayerID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

import exceptions.DatabaseException;

public class TrainCardDAO extends Database.DataAccessObject implements ITrainCardDAO {

    private static final Logger logger = LogManager.getLogger(DestinationCardDAO.class.getName());
    private List<TrainCard> trainCardList;

    public TrainCardDAO(MongoDatabase database) {
        super(database);
        collectionName = "traincards";
        trainCardList = DataManager.getTrainCardList();
    }

    @Override
    public void initializeData() {
        List<TrainCard> cardList = DataManager.getTrainCardList();
        List<TrainCard> allCards = allCards();
        for (TrainCard card: allCards) {
            cardList.add(card);
        }
    }

    public List<TrainCard> allCards(){
        FindIterable<Document> iterCards = getCollection().find();
        Iterator iter = iterCards.iterator();
        List<TrainCard> cards = new ArrayList<>();
        while(iter.hasNext()) {
            Document doc = (Document) iter.next();
            cards.add(buildCardFromDocument(doc));
        }
        return cards;
    }

    @Override
    public void addDeck(GameID gameID, TrainCardDeck deck) throws DatabaseException {
        addFaceDown(gameID, deck.getFaceDownDeck());
        addFaceUp(gameID, deck.getFaceUpDeck());
        addDiscard(gameID, deck.getDiscardPile());
    }

    @Override
    public void addFaceUp(GameID gameID, List<TrainCard> faceUpDeck) throws DatabaseException {
        for (int deckPosition = 0; deckPosition < faceUpDeck.size(); deckPosition++) {
            var card = faceUpDeck.get(deckPosition);

            Document document = new Document();
            document.append("gameid", gameID.toString());
            document.append("color", card.getColor().name());
            document.append("sequenceposition", deckPosition);
            document.append("state", CardState.FACE_UP.name());
            document.append("playerid", "NULL");

            MongoCollection collection = getCollection();
            List<Object> parameters = new ArrayList<>();
            parameters.add(document);
            MongoCommand mongoCommand = new MongoCommand(collection, Database.INSERT_METHOD_NAME, parameters);
            Database.addCommand(mongoCommand);

            card.setGameID(gameID);
            card.setSequencePosition(deckPosition);
            card.setPlayerID(null);
            card.setState(CardState.FACE_UP);
            trainCardList.add(card);
        }
    }

    @Override
    public Hand makeHand(GameID gameID, PlayerID playerID) throws DatabaseException {
        Hand hand = new Hand();

        for (int i = 0; i < 4; i++){
            hand.addCard(drawFromFaceDown(gameID, playerID));
        }

        return hand;
    }

    @Override
    public void addFaceDown(GameID gameID, List<TrainCard> faceDownDeck) throws DatabaseException {
        for (int deckPosition = 0; deckPosition < faceDownDeck.size(); deckPosition++) {
            var card = faceDownDeck.get(deckPosition);

            Document document = new Document();
            document.append("gameid", gameID.toString());
            document.append("color", card.getColor().name());
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
            trainCardList.add(card);
        }
    }

    public void addDiscard(GameID gameID, List<TrainCard> discard) throws DatabaseException{
        for (int deckPosition = 0; deckPosition < discard.size(); deckPosition++) {
            var card = discard.get(deckPosition);

            Document document = new Document();
            document.append("gameid", gameID.toString());
            document.append("color", card.getColor().name());
            document.append("sequenceposition", 0);
            document.append("state", CardState.IN_DISCARD.name());
            document.append("playerid", "NULL");

            MongoCollection collection = getCollection();
            List<Object> parameters = new ArrayList<>();
            parameters.add(document);
            MongoCommand mongoCommand = new MongoCommand(collection, Database.INSERT_METHOD_NAME, parameters);
            Database.addCommand(mongoCommand);

            card.setGameID(gameID);
            card.setSequencePosition(0);
            card.setPlayerID(null);
            card.setState(CardState.IN_DISCARD);
            trainCardList.add(card);
        }
    }

    @Override
    public TrainCardDeck getDeckForGame(GameID gameID) throws DatabaseException {
        List<TrainCard> faceDown = getFaceDownDeck(gameID);
        List<TrainCard> faceUp = getFaceUpDeck(gameID);
        List<TrainCard> discard = getDiscardDeck(gameID);

        return new TrainCardDeck(faceUp, faceDown, discard);
    }

    @Override
    public List<TrainCard> getFaceUpDeck(GameID gameID) throws DatabaseException {
        List<TrainCard> faceUp = new ArrayList<>();

        for (TrainCard card: trainCardList) {
            if (card.getGameID().equals(gameID)
                    && card.getState().equals(CardState.FACE_UP)) {
                // TODO: Check if ordered by sequence position
                logger.debug("faceup pos: " + Integer.toString(card.getSequencePosition()));
                faceUp.add(card);
            }
        }

        return faceUp;
    }

    @Override
    public List<TrainCard> getFaceDownDeck(GameID gameID) throws DatabaseException {
        List<TrainCard> faceDown = new ArrayList<>();

        for (TrainCard card: trainCardList) {
            if (card.getGameID().equals(gameID)
                    && card.getState().equals(CardState.IN_DECK)) {
                // TODO: Check if ordered by sequence position
                faceDown.add(card);
            }
        }

        return faceDown;
    }

    @Override
    public List<TrainCard> getDiscardDeck(GameID gameID) throws DatabaseException {
        List<TrainCard> discard = new ArrayList<>();

        for (TrainCard card: trainCardList) {
            if (card.getGameID().equals(gameID)
                    && card.getState().equals(CardState.IN_DISCARD)) {
                discard.add(card);
            }
        }

        return discard;
    }

    @Override
    public Hand getPlayerHand(PlayerID playerID) throws DatabaseException {
        Hand hand = new Hand();

        for (TrainCard card: trainCardList) {
            if (card.getPlayerID().equals(playerID)) {
                hand.addCard(card);
            }
        }

        return hand;
    }

    @Override
    public void discardCards(Color color, int colorCards, int wildCards, PlayerID playerID) throws DatabaseException {
        Bson updates = Updates.combine(
                Updates.set("state", CardState.IN_DISCARD.name()),
                Updates.set("playerid", "NULL"),
                Updates.set("sequenceposition", 0));

        for(int i = 0; i < colorCards; i++){
            handToDiscard(color, playerID, updates);
        }
        for (int i = 0; i < wildCards; i++){
            handToDiscard(Color.WILD, playerID, updates);
        }
    }

    private void handToDiscard(Color color, PlayerID playerID, Bson updates){
        Bson filters = Filters.and(
                Filters.eq("playerid", playerID.toString()),
                Filters.eq("color", color.name()),
                Filters.eq("sequenceposition", getMaxHandPosition(color, playerID)));

        updateDataManager(playerID, color, getMaxHandPosition(color, playerID));

        MongoCollection collection = getCollection();
        List<Object> parameters = new ArrayList<>();
        parameters.add(filters);
        parameters.add(updates);
        MongoCommand mongoCommand = new MongoCommand(collection, Database.UPDATE_METHOD_NAME, parameters);
        Database.addCommand(mongoCommand);
    }

    private int getMaxHandPosition(Color color, PlayerID playerID){
        int maxPos = 0;

        for (TrainCard card: trainCardList) {
            if (card.getPlayerID().equals(playerID) && card.getColor().equals(color)) {
                if (card.getSequencePosition() > maxPos){
                    maxPos = card.getSequencePosition();
                }
            }
        }

        return maxPos;
    }

    @Override
    public void replaceFaceDown(GameID gameID) throws DatabaseException {
        List<TrainCard> discardDeck = getDiscardDeck(gameID);
        clearDiscard(gameID);
        Collections.shuffle(discardDeck);
        addFaceDown(gameID, discardDeck);
    }

    private void clearDiscard(GameID gameID){
        Bson filters = Filters.and(
                Filters.eq("gameid", gameID.toString()),
                Filters.eq("state", CardState.IN_DISCARD));

        MongoCollection collection = getCollection();
        List<Object> parameters = new ArrayList<>();
        parameters.add(filters);
        MongoCommand mongoCommand = new MongoCommand(collection, Database.DELETE_MANY_METHOD_NAME, parameters);
        Database.addCommand(mongoCommand);

        deleteFromDataManager(gameID, CardState.IN_DISCARD);
    }

    @Override
    public TrainCard drawFromFaceUp(GameID gameID, PlayerID playerID, int pos) throws DatabaseException {

        TrainCard cardDrawn = null;

        for (TrainCard card: trainCardList) {
            if (card.getGameID().equals(gameID)
                    && card.getState().equals(CardState.FACE_UP)
                    && card.getSequencePosition() == pos) {
                cardDrawn = card;
                break;
            }
        }

        moveFaceUpToHand(gameID, playerID, cardDrawn, pos);

        replaceOneFaceUpCard(pos, gameID);

        if (getFaceDownDeckSize(gameID) == 0){
            replaceFaceDown(gameID);
        }

        while (tooManyWilds(gameID)) {
            replaceAllFaceUpCards(gameID);
        }

        return cardDrawn;
    }

    private void moveFaceUpToHand(GameID gameID, PlayerID playerID, TrainCard card, int pos) throws DatabaseException {
        Bson filters = Filters.and(
                Filters.eq("state", CardState.FACE_UP.name()),
                Filters.eq("gameid", gameID.toString()),
                Filters.eq("sequenceposition", pos));

        Bson updates = Updates.combine(
                Updates.set("state", CardState.IN_PLAYER_HAND.name()),
                Updates.set("playerid", playerID.toString()),
                Updates.set("sequenceposition", getCardHandPosition(playerID, card)));

        updateDataManager(gameID, CardState.IN_PLAYER_HAND, getCardHandPosition(playerID, card),
                playerID, CardState.FACE_UP, pos);

        MongoCollection collection = getCollection();
        List<Object> parameters = new ArrayList<>();
        parameters.add(filters);
        parameters.add(updates);
        MongoCommand mongoCommand = new MongoCommand(collection, Database.UPDATE_METHOD_NAME, parameters);
        Database.addCommand(mongoCommand);
    }

    private void replaceOneFaceUpCard(int pos, GameID gameID) {
        Bson filters = Filters.and(
                Filters.eq("state", CardState.IN_DECK.name()),
                Filters.eq("gameid", gameID.toString()),
                Filters.eq("sequenceposition", getMaxPosInDeck(gameID)));

        Bson updates = Updates.combine(
                Updates.set("state", CardState.FACE_UP.name()),
                Updates.set("sequenceposition", pos));

        updateDataManager(gameID, CardState.FACE_UP, pos,null, CardState.IN_DECK, getMaxPosInDeck(gameID));

        MongoCollection collection = getCollection();
        List<Object> parameters = new ArrayList<>();
        parameters.add(filters);
        parameters.add(updates);
        MongoCommand mongoCommand = new MongoCommand(collection, Database.UPDATE_METHOD_NAME, parameters);
        Database.addCommand(mongoCommand);
    }

    private void replaceAllFaceUpCards(GameID gameID) throws DatabaseException{
        moveFaceUpToDiscard(gameID);

        for(int i = 0; i < 5; i++) {
            replaceOneFaceUpCard(i, gameID);

            if (getFaceDownDeckSize(gameID) == 0){
                replaceFaceDown(gameID);
            }
        }
    }

    private void moveFaceUpToDiscard(GameID gameID) {
        Bson filters = Filters.and(
                Filters.eq("state", CardState.FACE_UP.name()),
                Filters.eq("gameid", gameID.toString()));

        Bson updates = Updates.combine(
                Updates.set("state", CardState.IN_DISCARD.name()),
                Updates.set("sequenceposition", 0));

        updateDataManager(gameID, CardState.FACE_UP, CardState.IN_DISCARD, 0);

        MongoCollection collection = getCollection();
        List<Object> parameters = new ArrayList<>();
        parameters.add(filters);
        parameters.add(updates);
        MongoCommand mongoCommand = new MongoCommand(collection, Database.UPDATE_METHOD_NAME, parameters);
        Database.addCommand(mongoCommand);
    }

    private boolean tooManyWilds(GameID gameID) {
        int wildCount = 0;

        for (TrainCard card: trainCardList) {
            if (card.getGameID().equals(gameID)
                    && card.getState().equals(CardState.FACE_UP)
                    && card.getColor().equals(Color.WILD)) {
                wildCount ++;
            }
        }

        return wildCount >= 3;
    }

    // Next position in hand for drawn card
    private int getCardHandPosition(PlayerID playerID, TrainCard playerCard) {
        int pos = 0;

        for (TrainCard card: trainCardList) {
            if (card.getPlayerID().equals(playerID)
                    && card.getColor().equals(playerCard.getColor())) {
                pos++;
            }
        }

        return pos;
    }

    @Override
    public TrainCard drawFromFaceDown(GameID gameID, PlayerID playerID) throws DatabaseException {
        TrainCard cardDrawn = null;

        for (TrainCard card: trainCardList) {
            if (card.getGameID().equals(gameID)
                    && card.getState().equals(CardState.IN_DECK)
                    && card.getSequencePosition() == getMaxPosInDeck(gameID)) {
                cardDrawn = card;
                break;
            }
        }

        movefaceDownToHand(gameID, playerID, cardDrawn);

        if (getFaceDownDeckSize(gameID) == 0){
            replaceFaceDown(gameID);
        }

        return cardDrawn;
    }

    private void movefaceDownToHand(GameID gameID, PlayerID playerID, TrainCard card){
        Bson filters = Filters.and(
                Filters.eq("state", CardState.IN_DECK.name()),
                Filters.eq("gameid", gameID.toString()),
                Filters.eq("sequenceposition", getMaxPosInDeck(gameID)));

        Bson updates = Updates.combine(
                Updates.set("state", CardState.IN_PLAYER_HAND.name()),
                Updates.set("playerid", playerID.toString()),
                Updates.set("sequenceposition", getCardHandPosition(playerID, card)));

        updateDataManager(gameID, CardState.IN_PLAYER_HAND, getCardHandPosition(playerID, card),
                playerID, CardState.IN_DECK, getMaxPosInDeck(gameID));

        MongoCollection collection = getCollection();
        List<Object> parameters = new ArrayList<>();
        parameters.add(filters);
        parameters.add(updates);
        MongoCommand mongoCommand = new MongoCommand(collection, Database.UPDATE_METHOD_NAME, parameters);
        Database.addCommand(mongoCommand);
    }

    @Override
    public int getFaceDownDeckSize(GameID gameID) throws DatabaseException {
        int deckSize = 0;

        for (TrainCard card: trainCardList) {
            if (card.getGameID().equals(gameID) && card.getState().equals(CardState.IN_DECK)) {
                deckSize++;
            }
        }

        return deckSize;
    }

    @Override
    public boolean hasGameInfo(GameID gameID) throws DatabaseException {

        for (TrainCard card: trainCardList) {
            if (card.getGameID().equals(gameID)) {
                return true;
            }
        }

        return false;
    }

    private int getMaxPosInDeck(GameID gameID){
        int maxPos = 0;

        for (TrainCard card: trainCardList) {
            if (card.getGameID().equals(gameID) && card.getState().equals(CardState.IN_DECK)) {
                if (card.getSequencePosition() > maxPos){
                    maxPos = card.getSequencePosition();
                }
            }
        }

        return maxPos;
    }

    private void updateDataManager(GameID gameID, CardState newState, int newPos,
                                 PlayerID playerID, CardState oldState, int oldPos){
        for (TrainCard card: trainCardList) {
            if (card.getGameID().equals(gameID)
                    && card.getState().equals(oldState)
                    && card.getSequencePosition() == oldPos) {
                card.setPlayerID(playerID);
                card.setState(newState);
                card.setSequencePosition(newPos);
            }
        }
    }

    private void updateDataManager(GameID gameID, CardState oldState, CardState newState, int newPos){
        for (TrainCard card: trainCardList) {
            if (card.getGameID().equals(gameID)
                    && card.getState().equals(oldState)) {
                card.setState(newState);
                card.setSequencePosition(newPos);
            }
        }
    }

    private void updateDataManager(PlayerID playerID, Color color, int oldPos){
        for (TrainCard card: trainCardList) {
            if (card.getPlayerID().equals(playerID)
                    && card.getColor().equals(color)
                    && card.getSequencePosition() == oldPos) {
                card.setState(CardState.IN_DISCARD);
                card.setSequencePosition(0);
                card.setPlayerID(null);
            }
        }
    }

    private void deleteFromDataManager(GameID gameID, CardState state){
        ListIterator<TrainCard> iter = trainCardList.listIterator();
        while(iter.hasNext()){
            TrainCard card = iter.next();
            if(card.getGameID().equals(gameID) && card.getState().equals(state)){
                iter.remove();
            }
        }
    }

    private TrainCard buildCardFromDocument(Document doc) {
        var color = Color.valueOf(doc.getString("color"));
        var gameID = GameID.fromString(doc.getString("gameid"));
        var sequencePosition = doc.getInteger("sequenceposition");
        var state = CardState.valueOf(doc.getString("state"));
        PlayerID playerID = null;
        if (!doc.getString("playerid").equals("NULL")) playerID = PlayerID.fromString(doc.getString("playerid"));
        return new TrainCard(color, gameID, sequencePosition, state, playerID);
    }
}
