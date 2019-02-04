package com.tickettoride.database;

import com.tickettoride.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDAO extends Database.DataAccessObject {
    public UserDAO(Connection connection) {
        super(connection);
    }

    public void addUser(User user) throws Database.DatabaseException {

        final String sql = "INSERT INTO Users (userID, userName, password) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setString(1, user.getUserID().toString());
            statement.setString(2, user.getUsername().toString());
            statement.setString(3, user.getPassword().toString());

            statement.execute();

        } catch (SQLException e) {
            throw new Database.DatabaseException("Could not add new user to Database!", e);
        }
    }

}
