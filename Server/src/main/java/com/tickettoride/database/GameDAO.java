package com.tickettoride.database;

import com.tickettoride.models.Game;

import exceptions.DatabaseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class GameDAO extends Database.DataAccessObject {

    private final String tableCreateString =
            // language=PostgreSQL
            "CREATE TABLE Games" +
                    "(" +
                    "gameID TEXT PRIMARY KEY NOT NULL CHECK ( length(gameID) > 0 )," +
                    "groupName TEXT NOT NULL UNIQUE," +
                    "numPlayer NUMERIC NOT NULL," +
                    "maxPlayer NUMERIC NOT NULL," +
                    "iStarted BOOLEAN DEFAULT FALSE," +
                    "curTurn NUMERIC" +
                    ");";

    public GameDAO(Connection connection) {
        super(connection);
    }

    private Logger logger = LogManager.getLogger(GameDAO.class.getName());

    @Override
    String getTableCreateString() {
        return tableCreateString;
    }

    public void addGame(Game game) throws DatabaseException {

        final String sql = "INSERT INTO Games (gameID, groupName, numPlayer, maxPlayer, curTurn) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, game.getGameID().toString());
            statement.setString(2, game.getGroupName());
            statement.setInt(3, game.getNumPlayer());
            statement.setInt(4, game.getMaxPlayer());
            statement.setInt(5, game.getCurTurn());

            statement.executeUpdate();
        } catch (SQLException e) { throw new DatabaseException("Could not add new game to Database!", e); }
    }

    @Nullable
    public Game getGame(UUID gameID) throws DatabaseException {
        Game game = null;
        String sql = "SELECT * FROM Games WHERE gameID = ?";
        try (var statement = connection.prepareStatement(sql)) {
            statement.setString(1, gameID.toString());
            var result = statement.executeQuery();
            if (result.next()) {
                game = buildGameFromQueryResult(result);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not retrieve game!", e);
        }
        return game;
    }

    public void updatePlayerCount(UUID gameID, int numberPlayers) throws DatabaseException {
        String sql = "UPDATE Games SET numPlayer = ? WHERE gameID = ?";
        try (var statement = connection.prepareStatement(sql)) {
            statement.setInt(1, numberPlayers);
            statement.setString(2, gameID.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Could not increase player count!", e);
        }
    }

    public void updateTurn(Game game) throws DatabaseException {
        String sql = "UPDATE Games SET curTurn = ? WHERE gameID = ?";
        try (var statement = connection.prepareStatement(sql)) {
            statement.setInt(1, game.getCurTurn());
            statement.setString(2, game.getGameID().toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Could not update turn!", e);
        }
    }

    public ArrayList<Game> allGames() throws DatabaseException {
        ArrayList<Game> games = new ArrayList<>();
        String sql = "Select * from Games";
        try (var statement = connection.prepareStatement(sql)) {
          var results = statement.executeQuery();
          while (results.next()) {
              var game = buildGameFromQueryResult(results);
              games.add(game);
          }
        } catch (SQLException e) {
            throw new DatabaseException(("Could Not Retrieve Games"), e);
        }
        return games;
    }

    private Game buildGameFromQueryResult(ResultSet result) throws SQLException {

        var tableGameID = UUID.fromString(result.getString("GameID"));
        var tableGroupName = result.getString("groupName");
        var tableNumPlayer = result.getInt("numPlayer");
        var tableMaxPlayer = result.getInt("maxPlayer");
        var tableIsStarted = result.getBoolean("iStarted");
        var tableCurrentTurn = result.getInt("curTurn");

        return new Game(tableGameID, tableGroupName, tableNumPlayer, tableMaxPlayer, tableIsStarted, tableCurrentTurn);
    }

    public void setGameToStarted(UUID gameID) throws DatabaseException {

        String sql = "UPDATE games SET istarted=TRUE WHERE gameid= ?";

        try (var statement = connection.prepareStatement(sql)){

            statement.setString(1, gameID.toString());
            statement.executeUpdate();

        } catch (SQLException e) {
            logger.catching(e);

            throw new DatabaseException("Could not start the game!", e);
        }

    }
}

