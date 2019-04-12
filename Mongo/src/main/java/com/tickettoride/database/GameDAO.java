package com.tickettoride.database;

import com.mongodb.client.MongoDatabase;
import com.tickettoride.database.interfaces.IGameDAO;
import com.tickettoride.models.Game;
import com.tickettoride.models.idtypes.GameID;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

import exceptions.DatabaseException;

public class GameDAO extends Database.DataAccessObject implements IGameDAO {

    public GameDAO(MongoDatabase database) {
        super(database);
        collectionName = "games";
    }

    @Override
    public void initializeData() {

    }

    @Override
    public void addGame(Game game) throws DatabaseException {

    }

    @Override
    public @Nullable Game getGame(GameID gameID) throws DatabaseException {
        return null;
    }

    @Override
    public void updatePlayerCount(GameID gameID, int numberPlayers) throws DatabaseException {

    }

    @Override
    public void updateTurn(Game game) throws DatabaseException {

    }

    @Override
    public void updateGameFinished(Game game) throws DatabaseException {

    }

    @Override
    public ArrayList<Game> allGames() throws DatabaseException {
        return null;
    }

    @Override
    public void setGameToStarted(GameID gameID) throws DatabaseException {

    }
}
