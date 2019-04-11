package com.tickettoride.database;

import com.tickettoride.models.DestinationCard;
import com.tickettoride.models.Game;
import com.tickettoride.models.Player;
import com.tickettoride.models.User;
import com.tickettoride.models.idtypes.GameID;
import exceptions.DatabaseException;
import com.tickettoride.models.Password;
import com.tickettoride.models.Username;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

public class PlayerDAOTest extends AbstractDatabaseTest{

    @Override
    public void setUp() throws Exception {
        super.setUp();
        testGame = new Game("Test Game", 2);
        testUser = new User(new Username("TestUsername"), new Password("TestPassword"));
        testPlayer = new Player(testUser.getUserID(), testGame.getGameID());

        try (var db = new Database()){
            db.getUserDAO().addUser(testUser);
            db.getGameDAO().addGame(testGame);
            db.commit();
        }
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        testGame = null;
        testUser = null;
        testPlayer = null;
    }

    private Player testPlayer;
    private User testUser;
    private Game testGame;

    @Test
    public void PlayerIsAddedToDatabase_IsCorrectlyStoredAndRetrieved() throws DatabaseException {

        var deck = DestinationCard.getShuffledDeck();

        try (var db = new Database()){

            db.getPlayerDAO().addPlayer(testPlayer);
            db.commit();

        }

        Player fromDatabase;

        try (var db = new Database()){

            fromDatabase = db.getPlayerDAO().getPlayerByPlayerID(testPlayer.getPlayerID());

        }

        assertEquals(testPlayer, fromDatabase);
    }
}