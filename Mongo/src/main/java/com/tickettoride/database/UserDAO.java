package com.tickettoride.database;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.tickettoride.database.interfaces.IUserDAO;
import com.tickettoride.models.Password;
import com.tickettoride.models.Session;
import com.tickettoride.models.User;
import com.tickettoride.models.Username;
import com.tickettoride.models.idtypes.SessionID;
import com.tickettoride.models.idtypes.UserID;

import org.bson.Document;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import exceptions.DatabaseException;

public class UserDAO extends Database.DataAccessObject implements IUserDAO {

    private List<User> userList;

    public UserDAO(MongoDatabase database) {
        super(database);
        collectionName = "users";
        userList = DataManager.getUserList();
    }

    @Override
    public void initializeData() {
        List<User> userList = DataManager.getUserList();
        List<User> allUsers = allUsers();
        for (User user: allUsers) {
            userList.add(user);
        }
    }

    public List<User> allUsers() {
        FindIterable<Document> iterUsers = getCollection().find();
        Iterator iter = iterUsers.iterator();
        List<User> users = new ArrayList<>();
        while(iter.hasNext()) {
            Document doc = (Document) iter.next();
            users.add(buildUserFromDocument(doc));
        }
        return users;
    }

    @Override
    public void addUser(User user) {
        Document document = new Document();
        document.append("id", user.getUserID().toString());
        document.append("username", user.getUsername().toString());
        document.append("password", user.getPassword().toString());
        MongoCollection collection = getCollection();
        List<Object> parameters = new ArrayList<>();
        parameters.add(document);
        MongoCommand mongoCommand = new MongoCommand(collection, Database.INSERT_METHOD_NAME, parameters);
        Database.addCommand(mongoCommand);
        userList.add(user);
    }

    @Override
    public @Nullable User getUser(Username userName, Password password) {
        for (User user : userList) {
            if (user.getUsername().equals(userName) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public @Nullable User getUserByID(UserID id) throws DatabaseException {
        for (User user : userList) {
            if (user.getUserID().equals(id)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public User getUserBySessionID(SessionID sessionID) throws DatabaseException {
        List<Session> sessionList = DataManager.getSessionList();
        UserID userID = null;
        for (Session session : sessionList) {
            if (session.getSessionID().equals(sessionID)) {
                userID = session.getUserID();
            }
        }
        if (userID == null) return null;
        for (User user : userList) {
            if (user.getUserID().equals(userID)) {
                return user;
            }
        }
        return null;
    }

    private User buildUserFromDocument(Document doc) {
        String userID = doc.getString("id");
        Username username = new Username(doc.getString("username"));
        Password password = new Password(doc.getString("password"));
        return new User(username, password, userID);
    }

}
