package com.tickettoride.database;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;

public class DatabaseTest extends AbstractDatabaseTest {

    @Test
    public void DatabaseCreatedUsingTryWithBlock_WorksProperly() {

        try (var db = new Database()){
            //If close is called manually, there is no error
            db.close();
        } catch (Database.DatabaseException e) {
            fail();
        }
    }

    @Test
    public void DAOAskedForThenConnectionClosed_DAOHasNullConnections() {

        try (var db = new Database()) {

            var dao = db.getSessionDAO();

            db.close();

            assertNull(dao.connection);
        } catch (Database.DatabaseException e) {
            fail(e.getMessage());
        }
    }
}