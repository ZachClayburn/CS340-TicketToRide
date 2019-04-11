package com.tickettoride.database.interfaces;

import com.tickettoride.models.Game;
import com.tickettoride.models.idtypes.GameID;
import exceptions.DatabaseException;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public interface IGameDAO {
    void addGame(Game game) throws DatabaseException;

    @Nullable
    Game getGame(GameID gameID) throws DatabaseException;

    void updatePlayerCount(GameID gameID, int numberPlayers) throws DatabaseException;

    void updateTurn(Game game) throws DatabaseException;

    void updateGameFinished(Game game) throws DatabaseException;

    ArrayList<Game> allGames() throws DatabaseException;

    default Game buildGameFromQueryResult(ResultSet result) throws SQLException {

        var tableGameID = GameID.fromString(result.getString("GameID"));
        var tableGroupName = result.getString("groupName");
        var tableNumPlayer = result.getInt("numPlayer");
        var tableMaxPlayer = result.getInt("maxPlayer");
        var tableIsStarted = result.getBoolean("iStarted");
        var tableCurrentTurn = result.getInt("curTurn");
        var finished = result.getBoolean("finished");

        return new Game(tableGameID, tableGroupName, tableNumPlayer, tableMaxPlayer, tableIsStarted, tableCurrentTurn, finished);
    }

    void setGameToStarted(GameID gameID) throws DatabaseException;
}
