package com.tickettoride.database;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.tickettoride.database.interfaces.IPlayerDAO;
import com.tickettoride.database.interfaces.IPlayerStateDAO;
import com.tickettoride.models.Player;
import com.tickettoride.models.PlayerState;
import com.tickettoride.models.idtypes.GameID;
import com.tickettoride.models.idtypes.PlayerID;
import com.tickettoride.models.idtypes.PlayerStateID;

import org.bson.Document;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import exceptions.DatabaseException;

public class PlayerStateDAO extends Database.DataAccessObject implements IPlayerStateDAO {
    List <PlayerState> playerStates;
    public PlayerStateDAO(MongoDatabase database) {
        super(database);
        collectionName = "playerstates";
        playerStates = DataManager.getPlayerStateList();
    }

    @Override
    public void initializeData() {
        List<PlayerState> playerStateList = DataManager.getPlayerStateList();
        List<PlayerState> allPlayerStates = allPlayerStates();
        for (PlayerState playerState: allPlayerStates) {
            playerStateList.add(playerState);
        }
    }

    public List<PlayerState> allPlayerStates() {
        FindIterable<Document> iterUsers = getCollection().find();
        Iterator iter = iterUsers.iterator();
        List<PlayerState> playerStates = new ArrayList<>();
        while(iter.hasNext()) {
            try {
                ResultSet result = (ResultSet) iter.next();
                playerStates.add(buildPlayerStateFromResult(result));
            } catch (Exception ex) {

            }
        }
        return playerStates;
    }
    @Override
    public void addPlayerState(PlayerState playerState) throws DatabaseException {
        Document document = new Document();
        document.append("playerStateID", playerState.getPlayerStateID().toString());
        document.append("playerID", playerState.getPlayerID().toString());
        document.append("gameID", playerState.getGameID().toString());
        document.append("type", playerState.getClass().getName());
        MongoCollection collection = getCollection();
        List<Object> parameters = new ArrayList<>();
        parameters.add(document);
        MongoCommand mongoCommand = new MongoCommand(collection, Database.INSERT_METHOD_NAME, parameters);
        Database.addCommand(mongoCommand);
        playerStates.add(playerState);
    }

    @Override
    public PlayerState getPlayerState(PlayerStateID playerStateID) throws DatabaseException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        for (PlayerState playerState : playerStates) {
            if (playerState.getPlayerStateID().equals(playerStateID)) {
                return playerState;
            }
        }
        return null;
    }

    @Override
    public PlayerState getPlayerState(PlayerID playerID) throws DatabaseException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        for (PlayerState playerState : playerStates) {
            if (playerState.getPlayerID().equals(playerID)) {
                return playerState;
            }
        }
        return null;
    }

    @Override
    public void updatePlayerState(PlayerState playerState) throws DatabaseException {
        MongoCollection collection = getCollection();
        collection.updateOne(Filters.eq("playerStateID", playerState.getPlayerStateID()), Updates.set("type", playerState.getClass().getName()));
        collection.updateOne(Filters.eq("playerStateID", playerState.getPlayerStateID()), Updates.set("gameID", playerState.getGameID()));
        collection.updateOne(Filters.eq("playerStateID", playerState.getPlayerStateID()), Updates.set("playerID", playerState.getPlayerID()));
    }

    @Override
    public List<PlayerState> getGamePlayerStates(GameID gameID) throws DatabaseException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        List<PlayerState> states = new ArrayList<>();
        for (PlayerState playerState : playerStates) {
            if (playerState.getGameID().equals(gameID)) {
                states.add(playerState);
            }
        }
        return states;
    }

    @Override
    public PlayerState buildPlayerStateFromResult(ResultSet result) throws SQLException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        PlayerStateID playerStateID = PlayerStateID.fromString(result.getString("playerStateID"));
        PlayerID playerID = PlayerID.fromString(result.getString("playerID"));
        GameID gameID = GameID.fromString(result.getString("gameID"));
        String type = result.getString("type");
        Class playerStateClass = Class.forName(type);
        Constructor constructor = playerStateClass.getConstructor(PlayerStateID.class, PlayerID.class, GameID.class);
        return (PlayerState) constructor.newInstance(playerStateID, playerID, gameID);
    }
}
