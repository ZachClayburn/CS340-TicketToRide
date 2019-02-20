package com.tickettoride.database;

import com.tickettoride.models.City;
import com.tickettoride.models.DestinationCard;
import com.tickettoride.models.Game;
import exceptions.DatabaseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class DestinationCardDAO extends Database.DataAccessObject {

    private static final String tableCreateString =
            // language=PostgreSQL
            "CREATE TYPE cardState AS ENUM ( 'inDeck', 'inPlayerHand' );" +
            "CREATE TABLE destinationCards" +
                    "(" +
                    "gameID TEXT," +
                    "destination1 TEXT," +
                    "destination2 TEXT," +
                    "pointValue INTEGER CHECK " +
                        "( pointValue IN (4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 16, 17, 20, 21, 22) )," +
                    "sequencePosition INTEGER CHECK ( sequencePosition >= 0 AND sequencePosition < 30) NULL ," +
                    "state cardState," +
                    "playerID TEXT REFERENCES players(playerid) NULL ," +
                    "FOREIGN KEY (gameID) REFERENCES games(gameID)," +
                    "CHECK (" +
                        "(state!='inPlayerHand' AND sequencePosition NOTNULL AND playerID ISNULL) OR " +
                        "(state= 'inPlayerHand' AND sequencePosition ISNULL  AND playerID NOTNULL)" +
                    ")" +
                    ");";

    private static final Logger logger = LogManager.getLogger(DestinationCardDAO.class.getName());

    @Override
    String getTableCreateString() {
        return tableCreateString;
    }

    DestinationCardDAO(Connection connection) {
        super(connection);
    }

    public void addDeck(Game game, Queue<DestinationCard> deck) throws DatabaseException {

        deck = new ArrayDeque<>(deck);

        String sql = "INSERT INTO destinationcards " +
                "(gameid, destination1, destination2, pointvalue, sequenceposition, state)" +
                "VALUES (?, ?, ?, ?, ?, 'inDeck')";

        try (var statement = connection.prepareStatement(sql)) {

            statement.setString(1, game.getGameID().toString());

            for (int deckPosition = 0; !deck.isEmpty(); deckPosition++) {

                var card = deck.remove();

                statement.setString(2, card.getDestination1().name());
                statement.setString(3, card.getDestination2().name());
                statement.setInt(4, card.getPointValue().asInt());
                statement.setInt(5, deckPosition);

                statement.executeUpdate();

            }

        } catch (SQLException e) {
            logger.catching(e);
            throw new DatabaseException("Could not add the deck!", e);
        }

    }

    public Queue<DestinationCard> getDeckForGame(Game game) throws DatabaseException {

        Queue<DestinationCard> deck = new ArrayDeque<>();
        String sql = "SELECT " +
                "destination1, destination2, pointvalue FROM destinationcards " +
                "WHERE gameid=? " +
                "ORDER BY sequenceposition";

        try (var statement = connection.prepareStatement(sql)){

            statement.setString(1, game.getGameID().toString());

            var results = statement.executeQuery();

            while (results.next()) {

                var destination1 = City.valueOf(results.getString("destination1"));
                var destination2 = City.valueOf(results.getString("destination2"));
                var pointValue = DestinationCard.Value.fromInt(results.getInt("pointValue"));
                deck.add(new DestinationCard(destination1, destination2, pointValue));

            }

        } catch (SQLException e) {
            logger.catching(e);
            throw new DatabaseException("Could not get the deck!", e);
        }

        return deck;
    }
}
