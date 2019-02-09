package com.tickettoride.database;

import com.tickettoride.database.Database.DatabaseException;
import com.tickettoride.models.Game;
import com.tickettoride.models.Player;
import com.tickettoride.models.User;

import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
            statement.setString(1, player.getPlayerID());
            statement.setString(2, player.getUser().getUserID().toString());
            statement.setString(3, player.getGame().getGameID());
            Boolean result = statement.execute();
            if (!result) throw new DatabaseException("Could not add new game to Database!");
        } catch (SQLException e) { throw new DatabaseException("Could not add new game to Database!", e); }
    }
}