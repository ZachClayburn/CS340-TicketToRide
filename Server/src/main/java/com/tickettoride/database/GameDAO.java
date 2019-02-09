package com.tickettoride.database;

import com.tickettoride.database.Database.DatabaseException;
import com.tickettoride.models.Game;
import com.tickettoride.models.User;

import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GameDAO extends Database.DataAccessObject {

    private final String tableCreateString =
            // language=PostgreSQL
            "CREATE TABLE Games" +
                    "(" +
                    "gameID TEXT PRIMARY KEY NOT NULL," +
                    "groupName TEXT NOT NULL UNIQUE," +
                    "numPlayer NUMERIC NOT NULL," +
                    "maxPlayer NUMERIC NOT NULL" +
                    ");";

    public GameDAO(Connection connection) {
        super(connection);
    }

    @Override
    String getTableCreateString() {
        return tableCreateString;
    }

    public void addGame(Game game) throws DatabaseException {
        final String sql = "INSERT INTO Games (gameID, groupName, numPlayer, maxPlayer) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, game.getGameID());
            statement.setString(2, game.getGroupName());
            statement.setInt(3, game.getNumPlayer());
            statement.setInt(4, game.getMaxPlayer());
            statement.executeUpdate();
        } catch (SQLException e) { throw new DatabaseException("Could not add new game to Database!", e); }
    }

    @Nullable
    public Game getGame(String gameID) throws DatabaseException {
        Game game = null;
        String sql = "SELECT * FROM Games WHERE gameID = ?";
        try (var statement = connection.prepareStatement(sql)) {
            statement.setString(1, gameID);
            var result = statement.executeQuery();
            if (result.next()) {
                var tableGameID = result.getString("GameID");
                var tableGroupName = result.getString("groupName");
                var tableNumPlayer = result.getInt("numPlayer");
                var tableMaxPlayer = result.getInt("maxPlayer");
                game = new Game(tableGameID, tableGroupName, tableNumPlayer, tableMaxPlayer);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not retrieve game!", e);
        }
        return game;
    }

    public void increasePlayerCount(String gameID, int num) throws DatabaseException {
        String sql = "UPDATE Games SET numPlayer = ? WHERE gameID = ?";
        try (var statement = connection.prepareStatement(sql)) {
            statement.setInt(1, num);
            statement.setString(2, gameID);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Could not retrieve increase player count!", e);
        }
    }
}
