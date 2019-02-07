package com.tickettoride.database;

import com.tickettoride.models.User;
import modelAttributes.Password;
import modelAttributes.Username;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDAO extends Database.DataAccessObject {

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

    public void addUser(User user) throws Database.DatabaseException {

        final String sql = "INSERT INTO Users (userID, userName, password) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setString(1, user.getUserID().toString());
            statement.setString(2, user.getUsername().toString());
            statement.setString(3, user.getPassword().toString());

            statement.execute();//FIXME Check if this returns false and throw an error

        } catch (SQLException e) {
            throw new Database.DatabaseException("Could not add new user to Database!", e);
        }
    }

    @Nullable
    public User getUser(Username userName, Password password) throws Database.DatabaseException {

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
            throw new Database.DatabaseException("Could not retrieve user!", e);
        }

        return user;
    }
}
