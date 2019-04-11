package com.tickettoride.database;

import com.tickettoride.database.interfaces.*;
import exceptions.DatabaseException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class DatabaseProviderTest {

    @Before
    public void setup() {
        DatabaseProvider.databaseFactory = null;
    }

    private boolean inputCausesInitFailure(String badString) {
        boolean failed = false;
        try {
            DatabaseProvider.intiDatabasePlugin(badString);
        } catch (DatabaseException e) {
            failed = true;
        }
        return failed;
    }

    @Test
    public void PathGivenToNonJarFile_CorrectlyThrowsException() {
        String badString = "notajar.txt";
        assertTrue("Exception not thrown when a non-jar path is given", inputCausesInitFailure(badString));
    }

    @Test
    public void JarFileDoesntExist_CorrectlyThrowsException(){
        String badString = "nonexistent.jar";
        assertTrue("Exception not thrown when nonexistent jar path is given", inputCausesInitFailure(badString));
    }

    @Test
    public void AttemptToGetDatabaseBeforeInitialization_CorrectlyThrowsException() {
        boolean failed = false;
        try {
            DatabaseProvider.getDatabase();
        } catch (DatabaseException e) {
            failed = true;
        }
        assertTrue("Exception should have been thrown when initDatabasePlugin not called", failed);
    }

    @Test
    public void GoodJarFilePathGiven_AbleToCreateTheDatabase() throws DatabaseException {
        String goodPath = "../libs/Postgres.jar";
        DatabaseProvider.intiDatabasePlugin(goodPath);

        IDatabase db = DatabaseProvider.getDatabase();
        ISessionDAO sessionDAO = db.getSessionDAO();
        IUserDAO userDAO = db.getUserDAO();
        IPlayerDAO playerDAO = db.getPlayerDAO();
        IChatDAO chatDAO = db.getChatDAO();
        IDestinationCardDAO destinationCardDAO = db.getDestinationCardDAO();
        IRouteDAO routeDAO = db.getRouteDAO();
        ILineDAO lineDAO = db.getLineDAO();
        IHistoryDAO historyDAO = db.getHistoryDAO();
        ITrainCardDAO trainCardDAO = db.getTrainCardDAO();
        IPlayerStateDAO playerStateDAO = db.getPlayerStateDAO();

        assertNotNull(db);
        assertNotNull(sessionDAO);
        assertNotNull(userDAO);
        assertNotNull(playerDAO);
        assertNotNull(chatDAO);
        assertNotNull(destinationCardDAO);
        assertNotNull(routeDAO);
        assertNotNull(lineDAO);
        assertNotNull(historyDAO);
        assertNotNull(trainCardDAO);
        assertNotNull(playerStateDAO);
    }
}