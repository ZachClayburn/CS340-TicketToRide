package com.tickettoride.database.interfaces;

import com.tickettoride.models.Password;
import com.tickettoride.models.User;
import com.tickettoride.models.Username;
import com.tickettoride.models.idtypes.SessionID;
import com.tickettoride.models.idtypes.UserID;
import exceptions.DatabaseException;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface IUserDAO {
    void addUser(User user) throws DatabaseException;

    @Nullable
    User getUser(Username userName, Password password) throws DatabaseException;

    default User getUserFromStatementResult(PreparedStatement statement) throws SQLException {
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
    User getUserByID(UserID id) throws DatabaseException;

    User getUserBySessionID(SessionID sessionID) throws DatabaseException;
}
