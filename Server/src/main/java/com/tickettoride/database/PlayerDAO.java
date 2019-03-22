package com.tickettoride.database;

import com.tickettoride.models.Player;
import com.tickettoride.models.PlayerColor;

import com.tickettoride.models.idtypes.GameID;
import com.tickettoride.models.idtypes.PlayerID;
import com.tickettoride.models.idtypes.UserID;
import exceptions.DatabaseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerDAO extends Database.DataAccessObject {
    private final String tableCreateString =
            // language=PostgreSQL
            "CREATE TABLE Players" +
                    "(" +
                    "playerID TEXT PRIMARY KEY NOT NULL," +
                    "userID TEXT NOT NULL," +
                    "gameID TEXT NOT NULL," +
                    "turn NUMERIC NOT NULL ," +
                    "FOREIGN KEY (gameID) REFERENCES games(gameid)," +
                    "FOREIGN KEY (userID) REFERENCES users(userid) " +
                    ");";

    public PlayerDAO(Connection connection) {
        super(connection);
    }

    private static Logger logger = LogManager.getLogger(PlayerDAO.class.getName());

    @Override
    String getTableCreateString() {
        return tableCreateString;
    }

    public void addPlayer(Player player) throws DatabaseException {
        final String sql = "INSERT INTO Players (playerID, userID, gameID, turn) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setString(1, player.getPlayerID().toString());
            statement.setString(2, player.getUserID().toString());
            statement.setString(3, player.getGameID().toString());
            statement.setInt(4, player.getTurn());

            statement.executeUpdate();

        } catch (SQLException e) {

            throw new DatabaseException("Could not add new player to Database!", e);
        }
    }
    
    public List<Player> getGamePlayers(GameID gameID) throws DatabaseException{
        List<Player> players=new ArrayList<>();
        Player player;
        String sql = "SELECT * FROM Players WHERE gameID = ?";
        try (var statement = connection.prepareStatement(sql)) {
            statement.setString(1, gameID.toString());
            var result = statement.executeQuery();
            while (result.next()) {
                player = buildPlayerFromResult(result);
                players.add(player);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not retrieve game players!", e);
        }
        return players;
    }
    
    //should return 0 or 1 but never more, just set up to handle unexpected cases
    public List<GameID> getGameForPlayer(PlayerID playerID) throws DatabaseException{
        List<GameID> games= new ArrayList<>();
        String sql = "SELECT * FROM Players WHERE playerID = ?";
        try (var statement = connection.prepareStatement(sql)) {
            statement.setString(1, playerID.toString());
            var result = statement.executeQuery();
            while (result.next()) {
                GameID tableGameID = GameID.fromString(result.getString("GameID"));
                games.add(tableGameID);
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Could not retrieve game id!", e);
        }
        return games;
    }

    @NotNull
    private Player buildPlayerFromResult(ResultSet result) throws SQLException {

        Player player;

        GameID tableGameID = GameID.fromString(result.getString("GameID"));
        PlayerID tablePlayerID = PlayerID.fromString(result.getString("PlayerID"));
        UserID tableUserID = UserID.fromString(result.getString("UserID"));
        int turn = result.getInt("turn");
        player = new Player(tableUserID, tableGameID, tablePlayerID);
        player.setTurn(turn);

        return player;
    }

    @Nullable
    public Player getPlayerByPlayerID(PlayerID playerID) throws DatabaseException {

        String sql = "SELECT * FROM players WHERE playerid=?";
        Player player = null;

        try (var statement = connection.prepareStatement(sql)){

            statement.setString(1, playerID.toString());
            var result = statement.executeQuery();

            if (result.next())
                player = buildPlayerFromResult(result);

        } catch (SQLException e) {
            logger.catching(e);
            throw new DatabaseException("Could not get the player from the database!", e);
        }
        return player;
    }

    public void setTurn(PlayerID playerID, int turn) throws DatabaseException {
        String sql = "UPDATE Players SET turn = ? WHERE playerID = ?";
        try (var statement = connection.prepareStatement(sql)) {
            statement.setInt(1, turn);
            statement.setString(2, playerID.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Could not set turn!", e);
        }
    }

    public void deletePlayer(UUID sessionID) throws SQLException {
        String sql = "DELETE FROM Players WHERE sessionID = ?";
        //FIXME This is broken, no such field sessionID, remove or fix
        try (var statement = connection.prepareStatement(sql)) {

            statement.setString(1, sessionID.toString());
            statement.executeUpdate();

        }
    }

    public void setPlayersUserName(List<Player> players) throws DatabaseException {

        String sql = "SELECT username FROM users WHERE userid=?";

        try (var statement = connection.prepareStatement(sql)) {

            for (var player : players) {

                statement.setString(1, player.getUserID().toString());
                var result = statement.executeQuery();

                result.next();

                player.setUsername(result.getString("username"));
            }

        } catch (SQLException e) {
            logger.catching(e);
            throw new DatabaseException("Could not set the name of Player!", e);
        }

    }
}
