package com.tickettoride.database;

import com.tickettoride.models.Color;
import com.tickettoride.models.Game;
import com.tickettoride.models.TrainCard;
import com.tickettoride.models.TrainCardDeck;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.UUID;

import exceptions.DatabaseException;

public class TrainCardDAO extends Database.DataAccessObject {
    private static final String tableCreateString =
            // language=PostgreSQL
            "DROP TYPE IF EXISTS cardstate; " +
            "CREATE TYPE cardState AS ENUM ( 'inDeck', 'inPlayerHand', 'offeredToPlayer', 'inDiscard', 'faceUp' );" +
            "CREATE TABLE TrainCards" +
                    "(" +
                    "gameID TEXT," +
                    "color TEXT," +
                    "sequencePosition INTEGER CONSTRAINT validPosition CHECK ( sequencePosition >= 0 AND sequencePosition < 110) NULL ," +
                    "state cardState," +
                    "playerID TEXT REFERENCES players(playerid) NULL ," +
                    "FOREIGN KEY (gameID) REFERENCES games(gameID)," +
                    "CONSTRAINT validState CHECK (" +
                    "(state!='inPlayerHand' AND sequencePosition NOTNULL AND playerID ISNULL) OR " +
                    "(state='inPlayerHand' AND sequencePosition ISNULL AND playerID NOTNULL)" +
                    ")" +
                    ");";

    private static final Logger logger = LogManager.getLogger(DestinationCardDAO.class.getName());

    @Override
    String getTableCreateString() {
        return tableCreateString;
    }

    TrainCardDAO(Connection connection) {
        super(connection);
    }

    public void addDeck(Game game, TrainCardDeck deck) throws DatabaseException {
        addFaceDown(game.getGameID(), deck);
    }

    public void addFaceUp(UUID gameID, TrainCardDeck deck) throws DatabaseException {
        String sql = "INSERT INTO TrainCards " +
                "(gameID, color, sequencePosition, state)" +
                "VALUES (?, ?, ?, 'faceUp')";

        List<TrainCard> faceUpDeck = deck.getFaceUpDeck();

        try (var statement = connection.prepareStatement(sql)) {

            statement.setString(1, gameID.toString());

            for (int deckPosition = 0; deckPosition < faceUpDeck.size(); deckPosition++) {

                var card = faceUpDeck.get(deckPosition);

                statement.setString(2, getColorAsString(card.getColor()));
                statement.setInt(3, deckPosition);

                statement.executeUpdate();

            }

        } catch (SQLException e) {
            logger.catching(e);
            throw new DatabaseException("Could not add the deck!", e);
        }
    }

    public void addFaceDown(UUID gameID, TrainCardDeck deck) throws DatabaseException {
        String sql = "INSERT INTO TrainCards " +
                "(gameID, color, sequencePosition, state)" +
                "VALUES (?, ?, ?, 'inDeck')";

        List<TrainCard> faceDownDeck = deck.getFaceDownDeck();

        try (var statement = connection.prepareStatement(sql)) {

            statement.setString(1, gameID.toString());

            for (int deckPosition = 0; deckPosition < faceDownDeck.size(); deckPosition++) {

                var card = faceDownDeck.get(deckPosition);

                statement.setString(2, getColorAsString(card.getColor()));
                statement.setInt(3, deckPosition);

                statement.executeUpdate();

            }

        } catch (SQLException e) {
            logger.catching(e);
            throw new DatabaseException("Could not add the deck!", e);
        }
    }


    public TrainCardDeck getDeckForGame(UUID gameID) throws DatabaseException {
        List<TrainCard> faceDown = getFaceDownDeck(gameID);
        List<TrainCard> faceUp = getFaceUpDeck(gameID);

        return new TrainCardDeck(faceUp, faceDown);
    }

    public List<TrainCard> getFaceUpDeck(UUID gameID) throws DatabaseException {
        List<TrainCard> faceUp = new ArrayList<>();

        String sql = "SELECT " +
                "color FROM TrainCards " +
                "WHERE gameid=? AND state='faceUp' " +
                "ORDER BY sequenceposition";

        try (var statement = connection.prepareStatement(sql)){

            statement.setString(1, gameID.toString());

            var results = statement.executeQuery();

            while (results.next())
                faceUp.add(buildTrainCardFromResult(results));

        } catch (SQLException e) {
            logger.catching(e);
            throw new DatabaseException("Could not get the deck!", e);
        }

        return faceUp;
    }

    public List<TrainCard> getFaceDownDeck(UUID gameID) throws DatabaseException {

        List<TrainCard> faceDown = new ArrayList<>();

        String sql = "SELECT " +
                "color FROM TrainCards " +
                "WHERE gameid=? AND state='inDeck' " +
                "ORDER BY sequenceposition";

        try (var statement = connection.prepareStatement(sql)){

            statement.setString(1, gameID.toString());

            var results = statement.executeQuery();

            while (results.next())
                faceDown.add(buildTrainCardFromResult(results));

        } catch (SQLException e) {
            logger.catching(e);
            throw new DatabaseException("Could not get the deck!", e);
        }

        return faceDown;
    }

    public void discardCards(List<TrainCard> cards){
        // TODO: When claiming route, discard the necessary cards from your hand
    }

    public void replaceFaceUpCards(){
        // TODO: Move all cards in FaceUp to the discard pile and add new cards to the faceUP
    }

    public void replaceFaceDown(){
        // TODO: Reshuffle discard pile and set it as faceDown pile
    }

    public void getDiscardDeck(){
        // TODO: Get all cards from the discard deck
    }

    public void clearDiscard(){
        // TODO: Delete all cards in the discard
    }

    public TrainCard drawFromFaceUp(int i){
        // TODO: Get the corresponding triain card
        return null;
    }

    public TrainCard drawFromFaceDown(){
        // TODO: Get the train card at position 0
        return null;
    }

    private TrainCard buildTrainCardFromResult(ResultSet result) throws SQLException {
        String color = result.getString("color");
        return new TrainCard(getColorFromString(color));
    }

    private Color getColorFromString(String color){
        switch(color){
            case "RED":
                return Color.RED;
            case "BLUE":
                return Color.BLUE;
            case "YELLOW":
                return Color.YELLOW;
            case "PURPLE":
                return Color.PURPLE;
            case "GREEN":
                return Color.GREEN;
            case "ORANGE":
                return Color.ORANGE;
            case "BLACK":
                return Color.BLACK;
            case "WHITE":
                return Color.WHITE;
            case "WILD":
                return Color.WILD;
        }
        return Color.WILD;
    }

    private String getColorAsString(Color color){
        switch(color){
            case RED:
                return "RED";
            case BLUE:
                return "BLUE";
            case YELLOW:
                return "YELLOW";
            case PURPLE:
                return "PURPLE";
            case GREEN:
                return "GREEN";
            case ORANGE:
                return "ORANGE";
            case BLACK:
                return "BLACK";
            case WHITE:
                return "WHITE";
            case WILD:
                return "WILD";
        }
        return null;
    }
}
