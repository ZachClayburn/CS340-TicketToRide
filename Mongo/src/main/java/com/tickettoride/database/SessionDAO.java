package com.tickettoride.database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.tickettoride.database.interfaces.ISessionDAO;
import com.tickettoride.models.Session;
import com.tickettoride.models.idtypes.SessionID;

import java.sql.SQLException;

import exceptions.DatabaseException;

public class SessionDAO extends Database.DataAccessObject implements ISessionDAO {

    public SessionDAO(MongoDatabase database) {
        super(database);
        collectionName = "sessions";
    }

    @Override
    public void initializeData() {

    }

    @Override
    public void createSession(Session session) throws DatabaseException {

    }

    @Override
    public Session findSession(SessionID sessionID) throws DatabaseException {
        return null;
    }

    @Override
    public void deleteSession(SessionID sessionID) throws SQLException {

    }
}
