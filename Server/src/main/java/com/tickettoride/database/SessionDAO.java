package com.tickettoride.database;

import exceptions.DatabaseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tickettoride.models.Session;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

public class SessionDAO extends Database.DataAccessObject {

    private static final Logger logger = LogManager.getLogger(SessionDAO.class.getName());

    private final String tableCreateString =
            // language=PostgreSQL
            "Create TABLE Sessions(" +
            "sessionID TEXT PRIMARY KEY NOT NULL ," +
            "userID TEXT NOT NULL," +
            "FOREIGN KEY (userID) REFERENCES Users(userID)" +
            ");";


    public SessionDAO(Connection connection) {
        super(connection);
    }

    public void createSession(Session session) throws DatabaseException {
        String sql = "INSERT INTO Sessions (userID, sessionID) VALUES (?, ?)";
        try (var statement = connection.prepareStatement(sql)) {
            statement.setString(1, session.getUserID().toString());
            statement.setString(2, session.getSessionID().toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.catching(e);
            throw new DatabaseException("Error adding session to the database", e);
        }
    }

    public Session findSession(UUID sessionID) throws DatabaseException {
        String sql = "SELECT * FROM Sessions WHERE sessionID = ?";
        Session session = new Session();
        try (var statement = connection.prepareStatement(sql)) {
            statement.setString(1, sessionID.toString());
            var result = statement.executeQuery();
            while (result.next()) {
                UUID userID = UUID.fromString(result.getString("userID"));
                UUID databaseSessionID = UUID.fromString(result.getString("sessionID"));
                session = new Session(databaseSessionID, userID);
            }
        } catch (SQLException e) { throw new DatabaseException("Could not add user to database", e); }
        return session;
    }

    public void deleteSession(UUID sessionID) throws SQLException {
        String sql = "DELETE FROM Sessions WHERE sessionID = ?";
        try (var statement = connection.prepareStatement(sql)) {
            statement.setString(1, sessionID.toString());
            statement.executeUpdate();
        }
    }

    @Override
    String getTableCreateString() {
        return tableCreateString;
    }
}
