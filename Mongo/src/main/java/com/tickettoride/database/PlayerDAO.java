package com.tickettoride.database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.tickettoride.database.interfaces.IPlayerDAO;
import com.tickettoride.models.Player;
import com.tickettoride.models.idtypes.GameID;
import com.tickettoride.models.idtypes.PlayerID;

import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import exceptions.DatabaseException;

public class PlayerDAO extends Database.DataAccessObject implements IPlayerDAO {

    public PlayerDAO(MongoDatabase database) {
        super(database);
        collectionName = "players";
    }

    @Override
    public void initializeData() {

    }

    @Override
    public void addPlayer(Player player) throws DatabaseException {

    }

    @Override
    public List<Player> getGamePlayers(GameID gameID) throws DatabaseException {
        return null;
    }

    @Override
    public List<GameID> getGameForPlayer(PlayerID playerID) throws DatabaseException {
        return null;
    }

    @Override
    public @Nullable Player getPlayerByPlayerID(PlayerID playerID) throws DatabaseException {
        return null;
    }

    @Override
    public void setTurn(PlayerID playerID, int turn) throws DatabaseException {

    }

    @Override
    public void setPoints(PlayerID playerID, int points) throws DatabaseException {

    }

    @Override
    public void setTrainCarCount(PlayerID playerID, int trainCarCount) throws DatabaseException {

    }

    @Override
    public void setPlayersUserName(List<Player> players) throws DatabaseException {

    }

    @Override
    public void deletePlayer(UUID sessionID) throws SQLException {

    }
}
