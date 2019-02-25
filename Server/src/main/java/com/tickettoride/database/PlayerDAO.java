package com.tickettoride.database;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tickettoride.models.DestinationCard;
import com.tickettoride.models.Player;
import exceptions.DatabaseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
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
                    "FOREIGN KEY (gameID) REFERENCES games(gameid)," +
                    "FOREIGN KEY (userID) REFERENCES users(userid) " +
                    ");";

    private static final Type destinationHandType = new TypeToken<ArrayList<DestinationCard>>(){}.getType();

    public PlayerDAO(Connection connection) {
        super(connection);
    }

    private static Logger logger = LogManager.getLogger(PlayerDAO.class.getName());

    @Override
    String getTableCreateString() {
        return tableCreateString;
    }

    public void addPlayer(Player player) throws DatabaseException {

        final String sql = "INSERT INTO Players (playerID, userID, gameID) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setString(1, player.getPlayerID().toString());
            statement.setString(2, player.getUserID().toString());
            statement.setString(3, player.getGameID().toString());

            statement.executeUpdate();

        } catch (SQLException e) {

            throw new DatabaseException("Could not add new player to Database!", e);
        }
    }
    
    public List<Player> getGamePlayers(UUID gameID) throws DatabaseException{
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

    @NotNull
    private Player buildPlayerFromResult(ResultSet result) throws SQLException {

        Player player;

        UUID tableGameID = UUID.fromString(result.getString("GameID"));
        UUID tablePlayerID = UUID.fromString(result.getString("PlayerID"));
        UUID tableUserID = UUID.fromString(result.getString("UserID"));
        player = new Player(tableUserID, tableGameID, tablePlayerID);

        return player;
    }

    @Nullable
    public Player getPlayerByPlayerID(UUID playerID) throws DatabaseException {

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

    public void deletePlayer(UUID sessionID) throws SQLException {
        String sql = "DELETE FROM Players WHERE sessionID = ?";
        //FIXME This is broken, no such field sessionID, remove or fix
        try (var statement = connection.prepareStatement(sql)) {

            statement.setString(1, sessionID.toString());
            statement.executeUpdate();

        }
    }
}
