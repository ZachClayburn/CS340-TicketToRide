package com.tickettoride.database;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.tickettoride.database.interfaces.ISessionDAO;
import com.tickettoride.models.Session;
import com.tickettoride.models.idtypes.SessionID;
import com.tickettoride.models.idtypes.UserID;

import org.bson.Document;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import exceptions.DatabaseException;

public class SessionDAO extends Database.DataAccessObject implements ISessionDAO {
    private List<Session> sessions;
    public SessionDAO(MongoDatabase database) {
        super(database);
        collectionName = "sessions";
        sessions = DataManager.getSessionList();
    }

    @Override
    public void initializeData() {
        List<Session> sessionList = DataManager.getSessionList();
        List<Session> allSessions = allSessions();
        for (Session session: allSessions) {
            sessionList.add(session);
        }
    }

    public List<Session> allSessions() {
        FindIterable<Document> iterUsers = getCollection().find();
        Iterator iter = iterUsers.iterator();
        List<Session> sessions = new ArrayList<>();
        while(iter.hasNext()) {
            Document doc = (Document) iter.next();
            sessions.add(buildSessionFromDocument(doc));
        }
        return sessions;
    }
    @Override
    public void createSession(Session session) throws DatabaseException {
        Document document = new Document();
        document.append("userID", session.getUserID().toString());
        document.append("sessionID", session.getSessionID().toString());
        MongoCollection collection = getCollection();
        List<Object> parameters = new ArrayList<>();
        parameters.add(document);
        MongoCommand mongoCommand = new MongoCommand(collection, Database.INSERT_METHOD_NAME, parameters);
        Database.addCommand(mongoCommand);
        sessions.add(session);
    }

    @Override
    public Session findSession(SessionID sessionID) throws DatabaseException {
        for (Session session : sessions) {
            if (session.getSessionID().equals(sessionID)) {
                return session;
            }
        }
        return null;
    }

    @Override
    public void deleteSession(SessionID sessionID) throws SQLException {
        MongoCollection collection = getCollection();
        collection.deleteOne(Filters.eq("sessionID", sessionID));
    }

    private Session buildSessionFromDocument(Document doc) {
        String userID = doc.getString("userID");
        String sessionID = doc.getString("sessionID");
        return new Session(SessionID.fromString(sessionID), UserID.fromString(userID));
    }

}
