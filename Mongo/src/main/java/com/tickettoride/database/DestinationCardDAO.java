package com.tickettoride.database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.tickettoride.database.interfaces.IDestinationCardDAO;
import com.tickettoride.models.DestinationCard;
import com.tickettoride.models.Game;
import com.tickettoride.models.Player;
import com.tickettoride.models.idtypes.GameID;

import org.bson.Document;

import java.util.Collection;
import java.util.List;
import java.util.Queue;

import exceptions.DatabaseException;

public class DestinationCardDAO extends Database.DataAccessObject implements IDestinationCardDAO {

    public DestinationCardDAO(MongoDatabase database) {
        super(database);
        collectionName = "destinationcards";
    }

    @Override
    public void initializeData() {

    }

    @Override
    public void addDeck(Game game, Queue<DestinationCard> deck) throws DatabaseException {

    }

    @Override
    public void addDeck(GameID gameID, Queue<DestinationCard> deck) throws DatabaseException {

    }

    @Override
    public Queue<DestinationCard> getDeckForGame(Game game) throws DatabaseException {
        return null;
    }

    @Override
    public Queue<DestinationCard> getDeckForGame(GameID gameID) throws DatabaseException {
        return null;
    }

    @Override
    public void offerCardsToPlayer(Player player, Collection<DestinationCard> cards) throws DatabaseException {

    }

    @Override
    public List<DestinationCard> getPlayerHand(Player player) throws DatabaseException {
        return null;
    }

    @Override
    public void acceptCards(Player player, Collection<DestinationCard> acceptedCards) throws DatabaseException {

    }

    @Override
    public List<DestinationCard> getOfferedCards(Player player) throws DatabaseException {
        return null;
    }
}
