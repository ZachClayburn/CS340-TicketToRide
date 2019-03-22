package com.tickettoride.database;

import com.tickettoride.models.DestinationCard;
import com.tickettoride.models.Game;
import exceptions.DatabaseException;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

public class GameDAOTest extends AbstractDatabaseTest{

    @Test
    public void GameStoredInDatabase_IsTheSameWhenPulledOutOfTheDatabase() throws DatabaseException {
        Game testGame = new Game("Test Group", 4);

        try (var db = new Database()) {

            db.getGameDAO().addGame(testGame);
            db.commit();

        }

        Game fromDatabase;

        try (var db = new Database()){
            fromDatabase = db.getGameDAO().getGame(testGame.getGameID());
        }

        assertEquals(testGame, fromDatabase);
    }

}