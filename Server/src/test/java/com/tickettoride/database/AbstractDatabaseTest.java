package com.tickettoride.database;

import org.junit.After;
import org.junit.Before;

import java.io.File;

public abstract class AbstractDatabaseTest {
    protected final String testDatabasePath = "src/test/resources/Test.DB";

    @Before
    public void setUp() throws Exception {

        Database.setDatabaseFile(testDatabasePath);

        try (var db = new Database()) {
            db.createDatabase(testDatabasePath);
        }

    }

    @After
    public void tearDown() throws Exception {

        new File(testDatabasePath).delete();
    }


}
