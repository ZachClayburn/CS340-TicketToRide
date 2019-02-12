package com.tickettoride.database;

import com.tickettoride.database.Database.DatabaseException;
import com.tickettoride.models.Game;
import com.tickettoride.models.Player;
import com.tickettoride.models.User;

import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class PlayerDAO extends Database.DataAccessObject {
    private final String tableCreateString =
            "CREATE TABLE Players" +
                    "(" +
                    "playerID TEXT PRIMARY KEY NOT NULL," +
                    "userID TEXT NOT NULL," +
                    "gameID TEXT NOT NULL UNIQUE" +
                    ");";

    public PlayerDAO(Connection connection) {
        super(connection);
    }

    @Override
    String getTableCreateString() {
        return tableCreateString;
    }

    public void addNewPlayer(Player player) throws DatabaseException {
        final String sql = "INSERT INTO Players (playerID, userID, gameID) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, player.getPlayerID().toString());
            statement.setString(2, player.getUser().getUserID().toString());
            statement.setString(3, player.getGame().getGameID().toString());
            statement.executeUpdate();
        } catch (SQLException e) { throw new DatabaseException("Could not add new player to Database!", e); }
    }

    public void deletePlayer(UUID sessionID) throws SQLException {
        String sql = "DELETE FROM Players WHERE sessionID = ?";
        try (var statement = connection.prepareStatement(sql)) {
            statement.setString(1, sessionID.toString());
            statement.executeUpdate();
        }
    }
}
