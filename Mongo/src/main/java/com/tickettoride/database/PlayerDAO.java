package com.tickettoride.database;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.tickettoride.database.interfaces.IPlayerDAO;
import com.tickettoride.models.Player;
import com.tickettoride.models.idtypes.GameID;
import com.tickettoride.models.idtypes.PlayerID;
import com.tickettoride.models.idtypes.UserID;

import org.bson.Document;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import exceptions.DatabaseException;

public class PlayerDAO extends Database.DataAccessObject implements IPlayerDAO {
    private List<Player> playerList;
    public PlayerDAO(MongoDatabase database) {
        super(database);
        collectionName = "players";
        playerList = DataManager.getPlayerList();
    }

    @Override
    public void initializeData() {
        List<Player> playerList = DataManager.getPlayerList();
        List<Player> allPlayers = allPlayers();
        for (Player player: allPlayers) {
            playerList.add(player);
        }
    }
    public List<Player> allPlayers() {
        FindIterable<Document> iterUsers = getCollection().find();
        Iterator iter = iterUsers.iterator();
        List<Player> players = new ArrayList<>();
        while(iter.hasNext()) {
            Document doc = (Document) iter.next();
            players.add(buildPlayerFromDocument(doc));
        }
        return players;
    }

    @Override
    public void addPlayer(Player player) throws DatabaseException {
        Document document = new Document();
        document.append("playerID", player.getPlayerID().toString());
        document.append("userID", player.getUserID().toString());
        document.append("gameID", player.getGameID().toString());
        document.append("turn", player.getTurn());
        document.append("trainCarCount", player.getTrainCarCount());
        document.append("points", player.getPoints());
        MongoCollection collection = getCollection();
        List<Object> parameters = new ArrayList<>();
        parameters.add(document);
        MongoCommand mongoCommand = new MongoCommand(collection, Database.INSERT_METHOD_NAME, parameters);
        Database.addCommand(mongoCommand);
        playerList.add(player);
    }

    @Override
    public List<Player> getGamePlayers(GameID gameID) throws DatabaseException {
        List<Player> gamePlayers = new ArrayList<>();
        for (Player player : playerList) {
            if (player.getGameID().equals(gameID)) {
                gamePlayers.add(player);
            }
        }
        return gamePlayers;
    }

    @Override
    public List<GameID> getGameForPlayer(PlayerID playerID) throws DatabaseException {
        List<GameID> games = new ArrayList<>();
        for (Player player : playerList) {
            if (player.getPlayerID().equals(playerID)) {
                games.add(player.getGameID());
            }
        }
        return games;
    }

    @Override
    public @Nullable Player getPlayerByPlayerID(PlayerID playerID) throws DatabaseException {
        for (Player player : playerList) {
            if (player.getPlayerID().equals(playerID)) {
                return player;
            }
        }
        return null;
    }

    @Override
    public void setTurn(PlayerID playerID, int turn) throws DatabaseException {
        MongoCollection collection = getCollection();
        collection.updateOne(Filters.eq("playerID", playerID), Updates.set("turn", turn));
    }

    @Override
    public void setPoints(PlayerID playerID, int points) throws DatabaseException {
        MongoCollection collection = getCollection();
        collection.updateOne(Filters.eq("playerID", playerID), Updates.set("points", points));
    }

    @Override
    public void setTrainCarCount(PlayerID playerID, int trainCarCount) throws DatabaseException {
        MongoCollection collection = getCollection();
        collection.updateOne(Filters.eq("playerID", playerID), Updates.set("trainCarCount", trainCarCount));
    }

    @Override
    public void setPlayersUserName(List<Player> players) throws DatabaseException {
        MongoCollection collection = getCollection();
        for (Player player : players) {
            collection.updateOne(Filters.eq("playerID", player.getPlayerID()), Updates.set("userID", player.getUserID()));
        }
    }

    @Override
    public void deletePlayer(UUID sessionID) throws SQLException {

    }
    private Player buildPlayerFromDocument(Document doc) {
        String playerID = doc.getString("playerID");
        String userID = doc.getString("userID");
        String gameID = doc.getString("gameID");
        int turn = doc.getInteger("turn");
        int trainCarCount = doc.getInteger("trainCarCount");
        int points = doc.getInteger("points");
        return new Player(UserID.fromString(userID), GameID.fromString(gameID), PlayerID.fromString(playerID), turn, trainCarCount, points);
    }
}
