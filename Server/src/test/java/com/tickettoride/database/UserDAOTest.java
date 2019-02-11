package com.tickettoride.database;

import com.tickettoride.models.User;
import modelAttributes.Password;
import modelAttributes.Username;
import org.junit.Ignore;
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
    public void UserAddedToDatabase_UserExistsInDatabase() throws Database.DatabaseException, SQLException {

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
    public void UserAskedForWithUsernameAndPassword_CorrectUserReturned() throws Database.DatabaseException, SQLException {


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

        } catch (Database.DatabaseException e) {
            fail(e.getMessage());
        }

        try (var db = new Database()){
            db.getUserDAO().addUser(testUser);
        } catch (Database.DatabaseException e) {
            var cause = e.getCause();
            if (cause instanceof PSQLException)
                didFail = true;
            else
                throw cause;
        }

        assertTrue(didFail);

    }

}