package com.tickettoride.database;

import com.tickettoride.models.User;
import exceptions.DatabaseException;
import com.tickettoride.models.Password;
import com.tickettoride.models.Username;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class UserDAO extends Database.DataAccessObject {

    private static final Logger logger = LogManager.getLogger(UserDAO.class.getName());

    private final String tableCreateString =
            // language=PostgreSQL
            "CREATE TABLE Users" +
            "(" +
            "userID TEXT PRIMARY KEY NOT NULL," +
            "userName TEXT NOT NULL UNIQUE NOT NULL CHECK ( length(userName) > 0 )," +
            "password TEXT NOT NULL CHECK ( length(password) > 0 )" +
            ");";

    public UserDAO(Connection connection) {
        super(connection);
    }

    @Override
    String getTableCreateString() {
        return tableCreateString;
    }

    public void addUser(User user) throws DatabaseException {
        final String sql = "INSERT INTO Users (userID, userName, password) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, user.getUserID().toString());
            statement.setString(2, user.getUsername().toString());
            statement.setString(3, user.getPassword().toString());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Could not add user to database", e);
        }
    }

    @Nullable
    public User getUser(Username userName, Password password) throws DatabaseException {
        User user = null;
        String sql = "SELECT * FROM Users WHERE userName = ? AND password = ?";
        try (var statement = connection.prepareStatement(sql)) {
            statement.setString(1, userName.toString());
            statement.setString(2, password.getPassword());

            user = getUserFromStatementResult(statement);

        } catch (SQLException e) {
            logger.catching(e);
            throw new DatabaseException("Could not retrieve user!", e);
        }
        return user;
    }

    private User getUserFromStatementResult(PreparedStatement statement) throws SQLException {
        User user = null;
        var result = statement.executeQuery();
        if (result.next()) {
            var tableUserName = new Username(result.getString("userName"));
            var tablePassWord = new Password(result.getString("password"));
            var userID = result.getString("userID");
            user = new User(tableUserName, tablePassWord, userID);
        }
        return user;
    }

    @Nullable
    public User getUserByID(UUID id) throws DatabaseException {
        User user = null;
        String sql = "SELECT * FROM Users WHERE userID = ?";
        try (var statement = connection.prepareStatement(sql)) {
            statement.setString(1, id.toString());
            user = getUserFromStatementResult(statement);
        } catch (SQLException e) {
            logger.catching(e);
            throw new DatabaseException("Could not retrieve user!", e);
        }
        return user;
    }

    public User getUserBySessionID(UUID sessionID) throws DatabaseException{
        User user = null;
        UUID userID;
        String sql = "SELECT userID FROM Sessions WHERE sessionID = ?";
        try (var statement = connection.prepareStatement(sql)) {
            statement.setString(1, sessionID.toString());
            var result = statement.executeQuery();
            if (result.next()) {
                var tableUserID = result.getString("userID");
                userID = UUID.fromString(tableUserID);
                user = getUserByID(userID);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not retrieve session!", e);
        }
        return user;
    }
}
