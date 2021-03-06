package com.tickettoride.database;

import com.tickettoride.models.idtypes.SessionID;
import com.tickettoride.models.idtypes.UserID;
import exceptions.DatabaseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tickettoride.models.Session;
import com.tickettoride.database.interfaces.ISessionDAO;

import java.sql.Connection;
import java.sql.SQLException;

public class SessionDAO extends Database.DataAccessObject implements ISessionDAO {

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

    @Override
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

    @Override
    public Session findSession(SessionID sessionID) throws DatabaseException {
        String sql = "SELECT * FROM Sessions WHERE sessionID = ?";
        Session session = new Session();
        try (var statement = connection.prepareStatement(sql)) {
            statement.setString(1, sessionID.toString());
            var result = statement.executeQuery();
            while (result.next()) {
                UserID userID = UserID.fromString(result.getString("userID"));
                SessionID databaseSessionID = SessionID.fromString(result.getString("sessionID"));
                session = new Session(databaseSessionID, userID);
            }
        } catch (SQLException e) { throw new DatabaseException("Could not add user to database", e); }
        return session;
    }

    @Override
    public void deleteSession(SessionID sessionID) throws SQLException {
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
