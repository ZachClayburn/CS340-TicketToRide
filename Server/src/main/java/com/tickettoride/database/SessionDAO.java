package com.tickettoride.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tickettoride.database.Database.DatabaseException;
import com.tickettoride.models.Session;

import java.sql.Connection;
import java.sql.SQLException;

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
        String sql = "INSERT into Sessions (userID, sessionID) VALUES (?, ?)";
        try (var statement = connection.prepareStatement(sql)) {
            statement.setString(1, session.getUser().getUserID().toString());
            statement.setString(2, session.getSessionID().toString());
            Boolean result = statement.execute();
            if (!result) throw new DatabaseException("Could not add new session to Database!");
        } catch (SQLException e) {
            throw new DatabaseException("Could not add new session to Database!", e);
        }
    }

    @Override
    String getTableCreateString() {
        return tableCreateString;
    }
}
