package com.tickettoride.database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.tickettoride.database.interfaces.IHistoryDAO;
import com.tickettoride.models.Message;
import com.tickettoride.models.idtypes.GameID;

import java.util.List;

import exceptions.DatabaseException;

public class HistoryDAO extends Database.DataAccessObject implements IHistoryDAO {

    public HistoryDAO(MongoDatabase database) {
        super(database);
        collectionName = "histories";
    }

    @Override
    public void initializeData() {

    }

    @Override
    public void addEvent(GameID gameID, Message message) throws DatabaseException {

    }

    @Override
    public List<Message> getHistory(GameID gameID) throws DatabaseException {
        return null;
    }
}
