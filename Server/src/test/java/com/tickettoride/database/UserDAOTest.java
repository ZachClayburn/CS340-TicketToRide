package com.tickettoride.database;

import com.tickettoride.models.User;
import modelAttributes.Password;
import modelAttributes.Username;
import org.junit.Test;

import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class UserDAOTest extends AbstractDatabaseTest{

    @Test
    public void UserAddedToDatabase_UserExistsInDatabase() throws Database.DatabaseException, SQLException {

        final String userName = "UName";
        final String passWord = "PWord";
        final var testUser = new User(new Username(userName), new Password(passWord));

        try (var db = new Database()) {

            db.getUserDAO().addUser(testUser);

            var con = db.connection;

            db.commit();

        }

        try (var connection = DriverManager.getConnection("jdbc:sqlite:" + this.testDatabasePath)){
            var results = connection.prepareStatement("select * from Users").executeQuery();

            assertTrue(results.next());

            assertEquals(testUser.getUsername().toString(), results.getString("username"));
            assertEquals(testUser.getPassword().toString(), results.getString("password"));
            assertEquals(testUser.getUserID().toString(), results.getString("userID"));

            assertFalse(results.next());

        }
    }

    @Test
    public void UserAskedForWithUsernameAndPassword_CorrectUserReturned() throws Database.DatabaseException {

        final var username = new Username("Username");
        final var password = new Password("Password");
        final var testUser = new User(username, password);

        try (var db = new Database()) {

            db.getUserDAO().addUser(testUser);
            db.commit();

        }

        try (var db = new Database()) {

            var resultUser = db.getUserDAO().getUser(username, password);

            assertEquals(testUser, resultUser);
        }
    }

}