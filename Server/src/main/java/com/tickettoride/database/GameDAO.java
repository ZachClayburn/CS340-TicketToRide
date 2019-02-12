package com.tickettoride.database;

import com.tickettoride.models.Game;

import exceptions.DatabaseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class GameDAO extends Database.DataAccessObject {

    private final String tableCreateString =
            // language=PostgreSQL
            "CREATE TABLE Games" +
                    "(" +
                    "gameID TEXT PRIMARY KEY NOT NULL CHECK ( length(gameID) > 0 )," +
                    "groupName TEXT NOT NULL UNIQUE," +
                    "numPlayer NUMERIC NOT NULL," +
                    "maxPlayer NUMERIC NOT NULL," +
                    "iStarted BOOLEAN DEFAULT FALSE" +
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
        final String sql = "INSERT INTO Games (gameID, groupName, numPlayer, maxPlayer) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, game.getGameID().toString());
            statement.setString(2, game.getGroupName());
            statement.setInt(3, game.getNumPlayer());
            statement.setInt(4, game.getMaxPlayer());
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
                UUID tableGameID = UUID.fromString(result.getString("GameID"));
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

    public void updatePlayerCount(UUID gameID, int numberPlayers) throws DatabaseException {
        String sql = "UPDATE Games SET numPlayer = ? WHERE gameID = ?";
        try (var statement = connection.prepareStatement(sql)) {
            statement.setInt(1, numberPlayers);
            statement.setString(2, gameID.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Could not retrieve increase player count!", e);
        }
    }

    public ArrayList<Game> allGames() throws DatabaseException {
        ArrayList<Game> games = new ArrayList<>();
        String sql = "Select * from Games";
        try (var statement = connection.prepareStatement(sql)) {
          var results = statement.executeQuery();
          while (results.next()) {
              UUID tableGameID = UUID.fromString(results.getString("GameID"));
              var tableGroupName = results.getString("groupName");
              var tableNumPlayer = results.getInt("numPlayer");
              var tableMaxPlayer = results.getInt("maxPlayer");
              Game game = new Game(tableGameID, tableGroupName, tableNumPlayer, tableMaxPlayer);
              games.add(game);
          }
        } catch (SQLException e) {
            throw new DatabaseException(("Could Not Retrieve Games"));
        }
        return games;
    }

    public void setGameToStarted(UUID gameID) throws DatabaseException {

        String sql = "UPDATE games SET istarted=TRUE WHERE gameid=?";

        try (var statement = connection.prepareStatement(sql)){

            statement.setString(0, gameID.toString());

        } catch (SQLException e) {
            logger.catching(e);

            throw new DatabaseException("Could not start the game!", e);
        }

    }
}

