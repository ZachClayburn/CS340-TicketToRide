package com.tickettoride.database;

import org.intellij.lang.annotations.Language;
import org.junit.After;
import org.junit.Before;

import java.sql.DriverManager;

public abstract class AbstractDatabaseTest {
    protected final String testDatabasePath = "localhost:5432/ttrtest";

    @Before
    public void setUp() throws Exception {

        try (var db = new Database()) {
            db.createDatabase();
        }

    }

    @After
    public void tearDown() throws Exception {

        String sql = "DO $$ DECLARE" +
                "    r RECORD;" +
                "BEGIN" +
                "    FOR r IN (SELECT tablename FROM pg_tables WHERE schemaname = current_schema()) LOOP" +
                "        EXECUTE 'DROP TABLE IF EXISTS ' || quote_ident(r.tablename) || ' CASCADE';" +
                "    END LOOP;" +
                "END $$;";

        try (var connection = DriverManager.getConnection("jdbc:postgresql://" + this.testDatabasePath,
                Database.parameters.getServerUserName(), Database.parameters.getServerPassword())) {
            connection.prepareStatement(sql).execute();
        }
    }
}
