package com.tickettoride.database;

import org.intellij.lang.annotations.Language;
import org.junit.After;
import org.junit.Before;

import java.sql.DriverManager;
import java.sql.SQLException;

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

        try (var db = new Database()) {
            String sql = "DO $$ DECLARE" +
                    "    r RECORD;" +
                    "BEGIN" +
                    "    FOR r IN (SELECT tablename FROM pg_tables WHERE schemaname = current_schema()) LOOP" +
                    "        EXECUTE 'DROP TABLE IF EXISTS ' || quote_ident(r.tablename) || ' CASCADE';" +
                    "    END LOOP;" +
                    "END $$;";

            try (var connection = db.connection) {
                connection.prepareStatement(sql).execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
