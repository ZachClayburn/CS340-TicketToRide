package com.tickettoride.database;
import com.tickettoride.models.PlayerState;
import com.tickettoride.models.idtypes.GameID;
import com.tickettoride.models.idtypes.PlayerID;
import com.tickettoride.models.idtypes.PlayerStateID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import exceptions.DatabaseException;

/**
 * Data Access Object for interacting with the chat table of the database
 */
public class PlayerStateDAO extends Database.DataAccessObject {

    private final String tableCreateString =
            // language=PostgreSQL
            "CREATE TABLE playerstates" +
                    "(" +
                    "playerstateID TEXT NOT NULL," +
                    "playerID TEXT NOT NULL," +
                    "gameID TEXT NOT NULL," +
                    "type TEXT NOT NULL" +
                    ");";

    public PlayerStateDAO(Connection connection) {
        super(connection);
    }

    private Logger logger = LogManager.getLogger(PlayerStateDAO.class.getName());

    @Override
    String getTableCreateString() {
        return tableCreateString;
    }

    public void addPlayerState(PlayerState playerState) throws DatabaseException {
        final String sql = "INSERT INTO playerstates (playerStateID, playerID, gameID, type) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, playerState.getPlayerStateID().toString());
            statement.setString(2, playerState.getPlayerID().toString());
            statement.setString(3, playerState.getGameID().toString());
            statement.setString(4, playerState.getClass().getName());
            statement.executeUpdate();
        } catch (SQLException e) { throw new DatabaseException("Could not add new player state to Database!", e); }
    }

    public PlayerState getPlayerState(PlayerStateID playerStateID) throws DatabaseException, ClassNotFoundException,
            NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        String sql = "SELECT * FROM playerstates WHERE playerstateID = ?";
        PlayerState playerState = null;
        try (var statement = connection.prepareStatement(sql)) {
            statement.setString(1, playerStateID.toString());
            var result = statement.executeQuery();
            while (result.next()) { playerState = buildPlayerStateFromResult(result); }
        } catch (SQLException e) {
            throw new DatabaseException("Could not retrieve playerstate", e);
        }
        return playerState;
    }

    public PlayerState getPlayerState(PlayerID playerID) throws DatabaseException, ClassNotFoundException,
            NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        String sql = "SELECT * FROM playerstates WHERE playerID = ?";
        PlayerState playerState = null;
        try (var statement = connection.prepareStatement(sql)) {
            statement.setString(1, playerID.toString());
            var result = statement.executeQuery();
            while (result.next()) { playerState = buildPlayerStateFromResult(result); }
        } catch (SQLException e) {
            throw new DatabaseException("Could not retrieve playerstate", e);
        }
        return playerState;
    }

    public void updatePlayerState(PlayerState playerState) throws DatabaseException {
        String sql = "UPDATE playerstates SET playerID = ?, type = ? WHERE playerStateID = ?";
        try (var statement = connection.prepareStatement(sql)) {
            statement.setString(1, playerState.getPlayerID().toString());
            statement.setString(2, playerState.getClass().getName());
            statement.setString(3, playerState.getPlayerStateID().toString());
            statement.executeUpdate();
        } catch (SQLException e) { throw new DatabaseException("Could not update player state!", e); }
    }

    public List<PlayerState> getGamePlayerStates(GameID gameID) throws DatabaseException, ClassNotFoundException,
            NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        String sql = "SELECT * FROM playerstates WHERE gameID = ?";
        try (var statement = connection.prepareStatement(sql)) {
            statement.setString(1, gameID.toString());
            var result = statement.executeQuery();
            List<PlayerState> playerStateList = new ArrayList<>();
            while (result.next()) { playerStateList.add(buildPlayerStateFromResult(result)); }
            return playerStateList;
        } catch (SQLException e) { throw new DatabaseException("Could not get game player states", e); }
    }

    public PlayerState buildPlayerStateFromResult(ResultSet result) throws SQLException, ClassNotFoundException,
            NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        PlayerStateID playerStateID = PlayerStateID.fromString(result.getString("playerStateID"));
        PlayerID playerID = PlayerID.fromString(result.getString("playerID"));
        GameID gameID = GameID.fromString(result.getString("gameID"));
        String type = result.getString("type");
        Class playerStateClass = Class.forName(type);
        Constructor constructor = playerStateClass.getConstructor(PlayerStateID.class, PlayerID.class, GameID.class);
        return (PlayerState) constructor.newInstance(playerStateID, playerID, gameID);
    }
}
