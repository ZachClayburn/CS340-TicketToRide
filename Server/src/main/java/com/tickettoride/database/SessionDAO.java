package com.tickettoride.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tickettoride.database.Database.DatabaseException;
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

    public void createSession(Session session) throws SQLException {
        String sql = "INSERT INTO Sessions (userID, sessionID) VALUES (?, ?)";
        try (var statement = connection.prepareStatement(sql)) {
            statement.setString(1, session.getUser().getUserID().toString());
            statement.setString(2, session.getSessionID().toString());
            statement.executeUpdate();
        }
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
