package com.tickettoride.database;

import com.tickettoride.database.interfaces.IGameDAO;
import com.tickettoride.models.Game;

import com.tickettoride.models.idtypes.GameID;
import exceptions.DatabaseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class GameDAO extends Database.DataAccessObject implements IGameDAO {

    private final String tableCreateString =
            // language=PostgreSQL
            "CREATE TABLE Games" +
                    "(" +
                    "gameID TEXT PRIMARY KEY NOT NULL CHECK ( length(gameID) > 0 )," +
                    "groupName TEXT NOT NULL UNIQUE," +
                    "numPlayer NUMERIC NOT NULL," +
                    "maxPlayer NUMERIC NOT NULL," +
                    "iStarted BOOLEAN DEFAULT FALSE," +
                    "finished BOOLEAN DEFAULT FALSE," +
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

    @Override
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

    @Override
    @Nullable
    public Game getGame(GameID gameID) throws DatabaseException {
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

    @Override
    public void updatePlayerCount(GameID gameID, int numberPlayers) throws DatabaseException {
        String sql = "UPDATE Games SET numPlayer = ? WHERE gameID = ?";
        try (var statement = connection.prepareStatement(sql)) {
            statement.setInt(1, numberPlayers);
            statement.setString(2, gameID.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Could not increase player count!", e);
        }
    }

    @Override
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

    @Override
    public void updateGameFinished(Game game) throws DatabaseException {
        String sql = "UPDATE games SET finished=TRUE where gameID = ?";
        try (var statement = connection.prepareStatement(sql)) {
            statement.setString(1, game.getGameID().toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Could not update finished", e);
        }
    }

    @Override
    public ArrayList<Game> allGames() throws DatabaseException {
        ArrayList<Game> games = new ArrayList<>();
        String sql = "Select * from Games WHERE finished = false";
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

    @Override
    public void setGameToStarted(GameID gameID) throws DatabaseException {

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

