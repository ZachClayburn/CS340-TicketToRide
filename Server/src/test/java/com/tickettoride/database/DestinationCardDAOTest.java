package com.tickettoride.database;

import com.tickettoride.models.DestinationCard;
import com.tickettoride.models.Game;
import com.tickettoride.models.Player;
import com.tickettoride.models.User;
import exceptions.DatabaseException;
import modelAttributes.Password;
import modelAttributes.Username;
import org.junit.Test;

import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.Assert.*;

public class DestinationCardDAOTest extends AbstractDatabaseTest {

    private User testUser;
    private Game testGame;
    private Player testPlayer;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        testUser = new User(new Username("testUser"), new Password("testPassword"));
        testGame = new Game("TestGroup", 2);
        testPlayer = new Player(testUser.getUserID(), testGame.getGameID());

        try (var db = new Database()) {
            db.getUserDAO().addUser(testUser);
            db.getGameDAO().addGame(testGame);
            db.getPlayerDAO().addPlayer(testPlayer);
            db.commit();
        }
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        testUser = null;
        testGame = null;
        testPlayer = null;
    }

    @Test
    public void DestinationDeckStoredInDatabase_IsCorrectWhenRetrieved() throws DatabaseException {

        Queue<DestinationCard> deck = DestinationCard.getShuffledDeck();

        try (var db = new Database()) {

            db.getDestinationCardDAO().addDeck(testGame, deck);
            db.commit();

        }

        Queue<DestinationCard> fromDatabase;

        try (var db = new Database()) {

            fromDatabase = db.getDestinationCardDAO().getDeckForGame(testGame);

        }

        assertArrayEquals(deck.toArray(), fromDatabase.toArray());

    }

    @Test
    public void CardMovedToPlayersHand_CorrectlyReflectedInDatabase() throws DatabaseException {

        Queue<DestinationCard> deck = DestinationCard.getShuffledDeck();

        try (var db = new Database()) {

            db.getDestinationCardDAO().addDeck(testGame, deck);
            db.commit();

        }

        TreeSet<DestinationCard> offeredCards = new TreeSet<>();
        Set<DestinationCard> playerHand = new TreeSet<>();

        for (int i = 0; i < 3; i++) {
            offeredCards.add(deck.poll());
        }


        try (var db = new Database()) {

            db.getDestinationCardDAO().offerCardsToPlayer(testPlayer,  offeredCards);
            playerHand.add(offeredCards.pollFirst());
            playerHand.add(offeredCards.pollFirst());
            deck.addAll(offeredCards);
            db.getDestinationCardDAO().acceptCards(testPlayer, playerHand);
            db.commit();

        }

        Queue<DestinationCard> deckFromDatabase;
        Set<DestinationCard> playerHandFromDatabase;

        try (var db = new Database()) {

            deckFromDatabase = db.getDestinationCardDAO().getDeckForGame(testGame);
            playerHandFromDatabase = db.getDestinationCardDAO().getPlayerHand(testPlayer);
            db.commit();

        }


        assertArrayEquals(deck.toArray(), deckFromDatabase.toArray());
        assertArrayEquals(playerHand.toArray(), playerHandFromDatabase.toArray());

    }
}