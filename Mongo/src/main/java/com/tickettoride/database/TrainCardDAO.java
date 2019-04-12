package com.tickettoride.database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.tickettoride.database.interfaces.ITrainCardDAO;
import com.tickettoride.models.Color;
import com.tickettoride.models.Hand;
import com.tickettoride.models.TrainCard;
import com.tickettoride.models.TrainCardDeck;
import com.tickettoride.models.idtypes.GameID;
import com.tickettoride.models.idtypes.PlayerID;

import java.util.List;

import exceptions.DatabaseException;

public class TrainCardDAO extends Database.DataAccessObject implements ITrainCardDAO {

    public TrainCardDAO(MongoDatabase database) {
        super(database);
        collectionName = "traincards";
    }

    @Override
    public void initializeData() {

    }

    @Override
    public void addDeck(GameID gameID, TrainCardDeck deck) throws DatabaseException {

    }

    @Override
    public void addFaceUp(GameID gameID, List<TrainCard> faceUpDeck) throws DatabaseException {

    }

    @Override
    public Hand makeHand(GameID gameID, PlayerID playerID) throws DatabaseException {
        return null;
    }

    @Override
    public void addFaceDown(GameID gameID, List<TrainCard> faceDownDeck) throws DatabaseException {

    }

    @Override
    public TrainCardDeck getDeckForGame(GameID gameID) throws DatabaseException {
        return null;
    }

    @Override
    public List<TrainCard> getFaceUpDeck(GameID gameID) throws DatabaseException {
        return null;
    }

    @Override
    public List<TrainCard> getFaceDownDeck(GameID gameID) throws DatabaseException {
        return null;
    }

    @Override
    public List<TrainCard> getDiscardDeck(GameID gameID) throws DatabaseException {
        return null;
    }

    @Override
    public Hand getPlayerHand(PlayerID playerID) throws DatabaseException {
        return null;
    }

    @Override
    public void discardCards(Color color, int colorCards, int wildCards, PlayerID playerID) throws DatabaseException {

    }

    @Override
    public void replaceFaceDown(GameID gameID) throws DatabaseException {

    }

    @Override
    public TrainCard drawFromFaceUp(GameID gameID, PlayerID playerID, int pos) throws DatabaseException {
        return null;
    }

    @Override
    public TrainCard drawFromFaceDown(GameID gameID, PlayerID playerID) throws DatabaseException {
        return null;
    }

    @Override
    public int getFaceDownDeckSize(GameID gameID) throws DatabaseException {
        return 0;
    }

    @Override
    public boolean hasGameInfo(GameID gameID) throws DatabaseException {
        return false;
    }
}
