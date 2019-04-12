package com.tickettoride.database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.tickettoride.database.interfaces.IPlayerDAO;
import com.tickettoride.database.interfaces.IPlayerStateDAO;
import com.tickettoride.models.Player;
import com.tickettoride.models.PlayerState;
import com.tickettoride.models.idtypes.GameID;
import com.tickettoride.models.idtypes.PlayerID;
import com.tickettoride.models.idtypes.PlayerStateID;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import exceptions.DatabaseException;

public class PlayerStateDAO extends Database.DataAccessObject implements IPlayerStateDAO {

    public PlayerStateDAO(MongoDatabase database) {
        super(database);
        collectionName = "playerstates";
    }

    @Override
    public void initializeData() {

    }

    @Override
    public void addPlayerState(PlayerState playerState) throws DatabaseException {

    }

    @Override
    public PlayerState getPlayerState(PlayerStateID playerStateID) throws DatabaseException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        return null;
    }

    @Override
    public PlayerState getPlayerState(PlayerID playerID) throws DatabaseException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        return null;
    }

    @Override
    public void updatePlayerState(PlayerState playerState) throws DatabaseException {

    }

    @Override
    public List<PlayerState> getGamePlayerStates(GameID gameID) throws DatabaseException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        return null;
    }

    @Override
    public PlayerState buildPlayerStateFromResult(ResultSet result) throws SQLException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        return null;
    }
}
