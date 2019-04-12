package com.tickettoride.database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.tickettoride.database.interfaces.IChatDAO;
import com.tickettoride.models.Message;
import com.tickettoride.models.idtypes.GameID;

import org.bson.Document;

import java.util.List;

import exceptions.DatabaseException;

public class ChatDAO extends Database.DataAccessObject implements IChatDAO {

    public ChatDAO(MongoDatabase database) {
        super(database);
        collectionName = "chats";
    }

    @Override
    public void initializeData() {

    }

    @Override
    public void addMessage(GameID gameID, Message message) throws DatabaseException {

    }

    @Override
    public List<Message> getChat(GameID gameID) throws DatabaseException {
        return null;
    }
}
