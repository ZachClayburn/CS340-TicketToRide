package database;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;

public class DatabaseTest {

    private final String testDatabasePath = "src/test/resources/Test.DB";

    @Before
    public void setUp() throws Exception {

        Database.createDatabase(testDatabasePath );
    }

    @After
    public void tearDown() throws Exception {

        new File(testDatabasePath).delete();
    }

    @Test
    public void NewDatabaseRequested_NewSQLiteFileExistsOnDisk() {
        var dbFile = new File(testDatabasePath);

        assertTrue(dbFile.exists());
    }

    @Test
    public void NewDatabaseRequestedWithNameOfExistingDatabase_DatabaseExceptionThrown() {

        try {
            Database.createDatabase(testDatabasePath);
        } catch (Database.DatabaseException dbe) {
            return;
        }
        fail("Expected exception not thrown!");
    }
}