package com.tickettoride.database;

import com.tickettoride.models.User;
import exceptions.DatabaseException;
import com.tickettoride.models.Password;
import com.tickettoride.models.Username;
import org.junit.Test;
import org.postgresql.util.PSQLException;

import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class UserDAOTest extends AbstractDatabaseTest{

    private final Username username = new Username("Username");
    private final Password password = new Password("Password");
    private final User testUser = new User(username, password);


    @Test
    public void UserAddedToDatabase_UserExistsInDatabase() throws DatabaseException, SQLException {

        String address = "jdbc:postgresql://";
        String username;
        String password;

        try (var db = new Database()) {

            address += db.parameters.getServerAddress();
            username = db.parameters.getServerUserName();
            password = db.parameters.getServerPassword();

            db.getUserDAO().addUser(testUser);

            db.commit();

        }

        try (var connection = DriverManager.getConnection(address, username, password)){
            var results = connection.prepareStatement("select * from Users").executeQuery();

            assertTrue(results.next());

            assertEquals(testUser.getUsername().toString(), results.getString("username"));
            assertEquals(testUser.getPassword().toString(), results.getString("password"));
            assertEquals(testUser.getUserID().toString(),   results.getString("userID"));

            assertFalse(results.next());

        }
    }

    @Test
    public void UserAskedForWithUsernameAndPassword_CorrectUserReturned() throws DatabaseException, SQLException {


        try (var db = new Database()) {

            db.getUserDAO().addUser(testUser);
            db.commit();

        }

        try (var db = new Database()) {

            var resultUser = db.getUserDAO().getUser(username, password);

            assertEquals(testUser, resultUser);
        }
    }

    @Test
    public void AttemptedToAddDuplicateUserToDatabase_ThrowsProperException() throws Throwable {

        boolean didFail = false;

        try (var db = new Database()){

            db.getUserDAO().addUser(testUser);
            db.commit();

        } catch (DatabaseException e) {
            fail(e.getMessage());
        }

        try (var db = new Database()){
            db.getUserDAO().addUser(testUser);
        } catch (DatabaseException e) {
            var cause = e.getCause();
            if (cause instanceof PSQLException)
                didFail = true;
            else
                throw cause;
        }

        assertTrue(didFail);

    }

    @Test
    public void AttemptToAddUserWithEmptyUsername_FailsWithExpectedException() throws Throwable {
        var badUsername = new Username("");
        var badUser = new User(badUsername, password);

        boolean didFail = false;

        try (var db = new Database()) {

            db.getUserDAO().addUser(badUser);

        } catch (DatabaseException e) {
            if (e.getCause() instanceof PSQLException)
                didFail = true;
            else
                throw e.getCause();
        }

        assertTrue(didFail);
    }


    @Test
    public void AttemptToAddUserWithEmptyPassword_FailsWithExpectedException() throws Throwable {
        var badPassword = new Password("");
        var badUser = new User(username, badPassword);

        boolean didFail = false;

        try (var db = new Database()) {

            db.getUserDAO().addUser(badUser);

        } catch (DatabaseException e) {
            if (e.getCause() instanceof PSQLException)
                didFail = true;
            else
                throw e.getCause();
        }

        assertTrue(didFail);
    }
}