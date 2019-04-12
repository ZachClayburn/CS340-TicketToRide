package com.tickettoride.database;

import com.google.gson.Gson;
import com.mongodb.MongoCommandException;
import com.mongodb.client.MongoCollection;
import com.tickettoride.database.interfaces.*;
import exceptions.DatabaseException;
import com.mongodb.client.MongoDatabase;
import com.mongodb.MongoClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.SynchronousQueue;

public class Database implements IDatabase {

    protected SessionDAO sessionDAO;
    protected UserDAO userDAO;
    protected GameDAO gameDAO;
    protected PlayerDAO playerDAO;
    protected ChatDAO chatDAO;
    protected DestinationCardDAO destinationCardDAO;
    protected RouteDAO routeDAO;
    protected LineDAO lineDAO;
    protected HistoryDAO historyDAO;
    protected TrainCardDAO trainCardDAO;
    protected PlayerStateDAO playerStateDAO;

    List<DataAccessObject> DAOs = new ArrayList<>();

    protected MongoDatabase database;

    private static Queue<MongoCommand> mongoCommands;
    private static int queDelay = 4;
    private boolean doCommit = false;

    public static final String INSERT_METHOD_NAME = "insertOne";
    public static final String RETRIEVE_COLLECTION_METHOD_NAME = "find";
    public static final String UPDATE_METHOD_NAME = "updateOne";
    public static final String DELETE_METHOD_NAME = "deleteOne";

    public Database() {
        HashMap<String, Object> databaseParameters = extractParametersFromConfigFile();
        MongoClient mongo = new MongoClient( (String) databaseParameters.get("URL") , ((Double) databaseParameters.get("port")).intValue());
        database = mongo.getDatabase((String) databaseParameters.get("databaseName") );
        initializeDAOs();
        createAllCollections();
        initializeDataManagerData();
        mongoCommands = new SynchronousQueue<>();
    }

    private void executeCommands() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        while (mongoCommands.size() > 0) {
            MongoCommand mongoCommand = mongoCommands.poll();
            mongoCommand.execute();
        }
    }

    public static void addCommand(MongoCommand mongoCommand) {
        mongoCommands.add(mongoCommand);
    }

    private void initializeDataManagerData() {
        if (!DataManager.getDataInitialized()) {
            for (DataAccessObject dao : DAOs) dao.initializeData();
            DataManager.setDataInitialized(true);
        }
    }

    private void initializeDAOs() {
        userDAO = new UserDAO(database);
        DAOs.add(userDAO);
        sessionDAO = new SessionDAO(database);
        DAOs.add(sessionDAO);
        gameDAO = new GameDAO(database);
        DAOs.add(gameDAO);
        playerDAO = new PlayerDAO(database);
        DAOs.add(playerDAO);
        chatDAO=new ChatDAO(database);
        DAOs.add(chatDAO);
        destinationCardDAO = new DestinationCardDAO(database);
        DAOs.add(destinationCardDAO);
        routeDAO = new RouteDAO(database);
        DAOs.add(routeDAO);
        lineDAO = new LineDAO(database);
        DAOs.add(lineDAO);
        historyDAO = new HistoryDAO(database);
        DAOs.add(historyDAO);
        trainCardDAO = new TrainCardDAO(database);
        DAOs.add(trainCardDAO);
        playerStateDAO = new PlayerStateDAO(database);
        DAOs.add(playerStateDAO);
    }

    @Override
    public void resetDatabase() {
        dropAllCollections();
        createAllCollections();
    }

    private void createAllCollections() {
        for (DataAccessObject dao : DAOs) dao.createCollection();
    }

    private void dropAllCollections() {
        for (DataAccessObject dao : DAOs) {
            MongoCollection collection = dao.getCollection();
            if (collection != null) collection.drop();
        }
    }

    @Override
    public void close() throws DatabaseException {
        try {
            if (mongoCommands.size() >= queDelay) {
                executeCommands();
                doCommit = true;
            }
        } catch (Exception e) { throw new DatabaseException("Mongo Database Transaction failed", e); }
    }

    @Override
    public ISessionDAO getSessionDAO() { return sessionDAO; }

    @Override
    public IUserDAO getUserDAO() { return userDAO; }

    @Override
    public IGameDAO getGameDAO() { return gameDAO; }

    @Override
    public IPlayerDAO getPlayerDAO() { return playerDAO; }

    @Override
    public IChatDAO getChatDAO() { return chatDAO; }

    @Override
    public IDestinationCardDAO getDestinationCardDAO() { return destinationCardDAO; }

    @Override
    public IRouteDAO getRouteDAO() { return routeDAO; }

    @Override
    public ILineDAO getLineDAO() { return lineDAO; }

    @Override
    public IHistoryDAO getHistoryDAO() { return historyDAO; }

    @Override
    public ITrainCardDAO getTrainCardDAO() { return trainCardDAO; }

    @Override
    public IPlayerStateDAO getPlayerStateDAO() { return playerStateDAO; }

    @Override
    public void commit() {
        doCommit = true;
    }

    static abstract class DataAccessObject {

        protected MongoDatabase database;
        protected String collectionName;

        DataAccessObject(MongoDatabase database) {
            this.database = database;
        }
        public MongoCollection getCollection() { return database.getCollection(collectionName); }
        public void createCollection() {
            try {
                database.createCollection(collectionName);
            } catch (MongoCommandException e) {}
        }

        public abstract void initializeData();
    }

    private HashMap<String, Object> extractParametersFromConfigFile() {
        Gson gson = new Gson();
        URL fileurl = Database.class.getClassLoader().getResource("databaseParams.json");
        InputStream in = null;
        try {
            in = fileurl.openStream();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        Reader reader = new InputStreamReader(in);

        return gson.fromJson(reader, HashMap.class);
    }
}
