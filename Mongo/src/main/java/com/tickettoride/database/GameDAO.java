package com.tickettoride.database;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.tickettoride.database.interfaces.IGameDAO;
import com.tickettoride.models.Game;
import com.tickettoride.models.idtypes.GameID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import exceptions.DatabaseException;

public class GameDAO extends Database.DataAccessObject implements IGameDAO {

    private List<Game> gameList;
    private static final Logger logger = LogManager.getLogger(GameDAO.class.getName());

    public GameDAO(MongoDatabase database) {
        super(database);
        collectionName = "games";
        gameList = DataManager.getGameList();
    }

    @Override
    public void initializeData() {
        List<Game> gameList = DataManager.getGameList();
        try{
            List<Game> allGames = allGames();
            for (Game game: allGames) {
                gameList.add(game);
            }
        }
        catch(Exception e){
            logger.error(e);
        }
    }

    @Override
    public void addGame(Game game) throws DatabaseException {
        Document document = new Document();
        document.append("gameid", game.getGameID().toString());
        document.append("groupname", game.getGroupName());
        document.append("numplayer", game.getNumPlayer());
        document.append("maxplayer", game.getMaxPlayer());
        document.append("curturn", game.getCurTurn());
        document.append("isstarted", Boolean.FALSE);
        document.append("finished", Boolean.FALSE);
        MongoCollection collection = getCollection();
        List<Object> parameters = new ArrayList<>();
        parameters.add(document);
        MongoCommand mongoCommand = new MongoCommand(collection, Database.INSERT_METHOD_NAME, parameters);
        Database.addCommand(mongoCommand);
        gameList.add(game);
    }

    @Override
    public @Nullable Game getGame(GameID gameID) throws DatabaseException {
        for (Game game: gameList) {
            if (game.getGameID().equals(gameID)) {
                return game;
            }
        }
        return null;
    }

    @Override
    public void updatePlayerCount(GameID gameID, int numberPlayers) throws DatabaseException {
        Bson filters = Filters.eq("gameid", gameID);
        Bson updates = Updates.set("numplayer", numberPlayers);

        MongoCollection collection = getCollection();
        List<Object> parameters = new ArrayList<>();
        parameters.add(filters);
        parameters.add(updates);
        MongoCommand mongoCommand = new MongoCommand(collection, Database.UPDATE_METHOD_NAME, parameters);
        Database.addCommand(mongoCommand);

        updateDataManagerPlayerCount(gameID, numberPlayers);
    }

    private void updateDataManagerPlayerCount(GameID gameID, int numberPlayers) {
        for (Game game: gameList) {
            if (game.getGameID().equals(gameID)) {
                game.setNumPlayer(numberPlayers);
            }
        }
    }

    @Override
    public void updateTurn(Game game) throws DatabaseException {
        Bson filters = Filters.eq("gameid", game.getGameID());
        Bson updates = Updates.set("curturn", game.getCurTurn());

        MongoCollection collection = getCollection();
        List<Object> parameters = new ArrayList<>();
        parameters.add(filters);
        parameters.add(updates);
        MongoCommand mongoCommand = new MongoCommand(collection, Database.UPDATE_METHOD_NAME, parameters);
        Database.addCommand(mongoCommand);

        updateDataManagerTurn(game);
    }

    private void updateDataManagerTurn(Game curGame){
        for (Game game: gameList) {
            if (game.getGameID().equals(curGame.getGameID())) {
                game.setCurTurn(curGame.getCurTurn());
            }
        }
    }

    @Override
    public void updateGameFinished(Game game) throws DatabaseException {
        Bson filters = Filters.eq("gameid", game.getGameID());
        Bson updates = Updates.set("finished", Boolean.TRUE);

        MongoCollection collection = getCollection();
        List<Object> parameters = new ArrayList<>();
        parameters.add(filters);
        parameters.add(updates);
        MongoCommand mongoCommand = new MongoCommand(collection, Database.UPDATE_METHOD_NAME, parameters);
        Database.addCommand(mongoCommand);

        updateDataManagerFinished(game);
    }

    private void updateDataManagerFinished(Game curGame){
        for (Game game: gameList) {
            if (game.getGameID().equals(curGame.getGameID())) {
                game.setFinished(true);
            }
        }
    }

    @Override
    public ArrayList<Game> allGames() throws DatabaseException {
        FindIterable<Document> iterGames = getCollection().find();
        Iterator iter = iterGames.iterator();
        ArrayList<Game> games = new ArrayList<>();
        while(iter.hasNext()) {
            Document doc = (Document) iter.next();
            if(!doc.getBoolean("finished")){
                games.add(buildGameFromDocument(doc));
            }
        }
        return games;
    }

    @Override
    public void setGameToStarted(GameID gameID) throws DatabaseException {
        Bson filters = Filters.eq("gameid", gameID);
        Bson updates = Updates.set("isstarted", Boolean.TRUE);

        MongoCollection collection = getCollection();
        List<Object> parameters = new ArrayList<>();
        parameters.add(filters);
        parameters.add(updates);
        MongoCommand mongoCommand = new MongoCommand(collection, Database.UPDATE_METHOD_NAME, parameters);
        Database.addCommand(mongoCommand);
    }

    private void updateDataManagerStarted(GameID gameID){
        for (Game game: gameList) {
            if (game.getGameID().equals(gameID)) {
                game.setStarted(true);
            }
        }
    }

    private Game buildGameFromDocument(Document doc){
        var tableGameID = GameID.fromString(doc.getString("gameid"));
        var tableGroupName = doc.getString("groupname");
        var tableNumPlayer = doc.getInteger("numplayer");
        var tableMaxPlayer = doc.getInteger("maxplayer");
        var tableIsStarted = doc.getBoolean("istarted");
        var tableCurrentTurn = doc.getInteger("curturn");
        var finished = doc.getBoolean("finished");
        return new Game(tableGameID, tableGroupName, tableNumPlayer, tableMaxPlayer, tableIsStarted, tableCurrentTurn, finished);
    }
}
