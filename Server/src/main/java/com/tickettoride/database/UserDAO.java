package com.tickettoride.database;

import com.tickettoride.database.Database.DatabaseException;
import com.tickettoride.models.User;
import modelAttributes.Password;
import modelAttributes.Username;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import org.postgresql.core.SqlCommand;

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
            "userName TEXT NOT NULL UNIQUE," +
            "password TEXT NOT NULL" +
            ");";

    public UserDAO(Connection connection) {
        super(connection);
    }

    @Override
    String getTableCreateString() {
        return tableCreateString;
    }

    public void addUser(User user) throws SQLException {
        final String sql = "INSERT INTO Users (userID, userName, password) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, user.getUserID().toString());
            statement.setString(2, user.getUsername().toString());
            statement.setString(3, user.getPassword().toString());

            statement.executeUpdate();
        }
    }

    @Nullable
    public User getUser(Username userName, Password password) throws DatabaseException {
        User user = null;
        String sql = "SELECT * FROM Users WHERE userName = ? AND password = ?";
        try (var statement = connection.prepareStatement(sql)) {
            statement.setString(1, userName.toString());
            statement.setString(2, password.getPassword());
            var result = statement.executeQuery();
            if (result.next()) {
                var tableUserName = new Username(result.getString("userName"));
                var tablePassWord = new Password(result.getString("password"));
                var userID = result.getString("userID");
                user = new User(tableUserName, tablePassWord, userID);
            }
        } catch (SQLException e) {
            logger.catching(e);
            throw new Database.DatabaseException("Could not retrieve user!", e);
        }
        return user;
    }

    @Nullable
    public User getUser(String id) throws DatabaseException {
        User user = null;
        String sql = "SELECT * FROM Users WHERE userID = ?";
        try (var statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            var result = statement.executeQuery();
            if (result.next()) {
                var tableUserName = new Username(result.getString("userName"));
                var tablePassWord = new Password(result.getString("password"));
                var userID = result.getString("userID");
                user = new User(tableUserName, tablePassWord, userID);
            }
        } catch (SQLException e) {
            logger.catching(e);
            throw new Database.DatabaseException("Could not retrieve user!", e);
        }
        return user;
    }

    public User getUser(UUID sessionID) throws DatabaseException{
        String userID = "";
        String sql = "SELECT userID FROM Sessions WHERE sessionID = ?";
        try (var statement = connection.prepareStatement(sql)) {
            statement.setString(1, sessionID.toString());
            var result = statement.executeQuery();
            if (result.next()) {
                var tableUserID = result.getString("userID");
                userID = tableUserID;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not retrieve session!", e);
        }
        return getUser(userID);
    }
}
