package com.tickettoride.database;

import com.tickettoride.models.Player;
import exceptions.DatabaseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PlayerDAO extends Database.DataAccessObject {
    private final String tableCreateString =
            // language=PostgreSQL
            "CREATE TABLE Players" +
                    "(" +
                    "playerID TEXT PRIMARY KEY NOT NULL," +
                    "userID TEXT NOT NULL," +
                    "gameID TEXT NOT NULL" + //FIXME Add foreign key constraint and insure correct creation order
                    ");";

    public PlayerDAO(Connection connection) {
        super(connection);
    }

    private static Logger logger = LogManager.getLogger(PlayerDAO.class.getName());

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
        } catch (SQLException e) {

            throw new DatabaseException("Could not add new player to Database!", e);
        }
    }
}
