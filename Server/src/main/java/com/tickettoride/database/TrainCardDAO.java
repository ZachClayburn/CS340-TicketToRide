package com.tickettoride.database;

import com.tickettoride.models.Color;
import com.tickettoride.models.Game;
import com.tickettoride.models.TrainCard;
import com.tickettoride.models.TrainCardDeck;
import com.tickettoride.models.idtypes.GameID;
import com.tickettoride.models.idtypes.PlayerID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.UUID;

import javax.xml.crypto.Data;

import exceptions.DatabaseException;

// TODO: Refactoring needed, lots of repeated code

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
                    "(state='inDiscard' AND sequencePosition ISNULL AND playerID ISNULL) OR " +
                    "(state='inPlayerHand' AND sequencePosition ISNULL AND playerID NOTNULL) OR " +
                    "(state='inPlayerHand' AND sequencePosition NOTNULL AND playerID NOTNULL)" +
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
        addFaceDown(game.getGameID(), deck.getFaceDownDeck());
        addFaceUp(game.getGameID(), deck.getFaceUpDeck());
    }

    public void addFaceUp(GameID gameID, List<TrainCard> faceUpDeck) throws DatabaseException {
        String sql = "INSERT INTO TrainCards " +
                "(gameID, color, sequencePosition, state)" +
                "VALUES (?, ?, ?, 'faceUp')";

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

    public void addFaceDown(GameID gameID, List<TrainCard> faceDownDeck) throws DatabaseException {
        String sql = "INSERT INTO TrainCards " +
                "(gameID, color, sequencePosition, state)" +
                "VALUES (?, ?, ?, 'inDeck')";

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


    public TrainCardDeck getDeckForGame(GameID gameID) throws DatabaseException {
        List<TrainCard> faceDown = getFaceDownDeck(gameID);
        List<TrainCard> faceUp = getFaceUpDeck(gameID);
        List<TrainCard> discard = getDiscardDeck(gameID);

        return new TrainCardDeck(faceUp, faceDown, discard);
    }

    public List<TrainCard> getFaceUpDeck(GameID gameID) throws DatabaseException {
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
            throw new DatabaseException("Could not get the faceup deck!", e);
        }

        return faceUp;
    }

    public List<TrainCard> getFaceDownDeck(GameID gameID) throws DatabaseException {

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
            throw new DatabaseException("Could not get the facedown deck!", e);
        }

        return faceDown;
    }

    public List<TrainCard> getDiscardDeck(GameID gameID) throws DatabaseException {
        List<TrainCard> discardDeck = new ArrayList<>();

        String sql = "SELECT " +
                "color FROM TrainCards " +
                "WHERE gameid=? AND state='inDiscard' " +
                "ORDER BY sequenceposition";

        try (var statement = connection.prepareStatement(sql)){

            statement.setString(1, gameID.toString());

            var results = statement.executeQuery();

            while (results.next())
                discardDeck.add(buildTrainCardFromResult(results));

        } catch (SQLException e) {
            logger.catching(e);
            throw new DatabaseException("Could not get the discard deck!", e);
        }

        return discardDeck;
    }

    public void discardCards(List<TrainCard> cards, PlayerID playerID) throws DatabaseException {

        String sql = "UPDATE TrainCards " +
                "SET state='inDiscard', playerid=NULL, sequenceposition=NULL " +
                "WHERE playerid=? AND color=? AND " +
                "sequenceposition=(" +
                "SELECT MAX(sequenceposition) FROM TrainCards WHERE playerid=? AND color=?)";

        try (var statement = connection.prepareStatement(sql)) {

            statement.setString(1, playerID.toString());
            statement.setString(3, playerID.toString());

            for (int pos = 0; pos < cards.size(); pos++) {
                var card = cards.get(pos);

                statement.setString(2, getColorAsString(card.getColor()));
                statement.setString(4, getColorAsString(card.getColor()));

                statement.executeUpdate();

            }

        } catch (SQLException e) {
            logger.catching(e);
            throw new DatabaseException("Could not discard Cards!", e);
        }
    }

    private void replaceAllFaceUpCards(GameID gameID) throws DatabaseException {
        moveFaceUpToDiscard(gameID);

        String sql = "UPDATE TrainCards " +
                "SET state='faceUp', sequenceposition=?" +
                "WHERE state='inDeck' AND gameID=? AND " +
                "sequenceposition=(" +
                "SELECT MAX(sequenceposition) FROM TrainCards WHERE state='inDeck' AND gameID=?" +
                ")";

        try (var statement = connection.prepareStatement(sql)) {

            for (int i = 0; i < 5; i++){
                statement.setInt(1, i);
                statement.setString(2, gameID.toString());
                statement.setString(3, gameID.toString());
                statement.executeUpdate();

                if (deckIsEmpty(gameID)){
                    replaceFaceDown(gameID);
                }
            }

        } catch (SQLException e) {
            logger.catching(e);
            throw new DatabaseException("Could not replace faceup cards!", e);
        }
    }

    private void replaceOneFaceUpCard(int pos, GameID gameID) throws DatabaseException {
        String sql = "UPDATE TrainCards " +
                "SET state='faceUp', sequenceposition=? " +
                "WHERE state='inDeck' AND gameID=? AND " +
                "sequenceposition=(" +
                "SELECT MAX(sequenceposition) FROM TrainCards WHERE state='inDeck' AND gameID=?" +
                ")";

        try (var statement = connection.prepareStatement(sql)) {

            statement.setInt(1, pos);
            statement.setString(2, gameID.toString());
            statement.setString(3, gameID.toString());
            statement.executeUpdate();

        } catch (SQLException e) {
            logger.catching(e);
            throw new DatabaseException("Could not replace faceup card!", e);
        }
    }

    private void moveFaceUpToDiscard(GameID gameID) throws DatabaseException {
        String sql = "UPDATE TrainCards " +
                "SET state='inDiscard', sequenceposition=NULL " +
                "WHERE state='faceUp' AND gameID=?";

        try (var statement = connection.prepareStatement(sql)) {

            statement.setString(1, gameID.toString());
            statement.executeUpdate();

        } catch (SQLException e) {
            logger.catching(e);
            throw new DatabaseException("Could not discard faceup cards!", e);
        }
    }

    public void replaceFaceDown(GameID gameID) throws DatabaseException{
        List<TrainCard> discardDeck = getDiscardDeck(gameID);
        clearDiscard(gameID);
        Collections.shuffle(discardDeck);
        addFaceDown(gameID, discardDeck);
    }

    private void clearDiscard(GameID gameID) throws DatabaseException {
        String sql = "DELETE FROM TrainCards WHERE state='inDiscard' AND gameID=?";

        try (var statement = connection.prepareStatement(sql)) {

            statement.setString(1, gameID.toString());
            statement.executeUpdate();

        } catch (SQLException e) {
            logger.catching(e);
            throw new DatabaseException("Could not clear Discard!", e);
        }
    }

    public TrainCard drawFromFaceUp(GameID gameID, PlayerID playerID, int pos) throws DatabaseException {
        String sql = "SELECT " +
                "color FROM TrainCards " +
                "WHERE gameid=? AND sequenceposition=? AND state='faceUp'";

        TrainCard card = drawCard(sql, gameID, pos);

        moveFaceUpToHand(gameID, playerID, card, pos);

        replaceOneFaceUpCard(pos, gameID);

        if (deckIsEmpty(gameID)){
            replaceFaceDown(gameID);
        }

        while (tooManyWilds(gameID)) {
            replaceAllFaceUpCards(gameID);
        }

        return card;
    }

    public TrainCard drawFromFaceDown(GameID gameID, PlayerID playerID) throws DatabaseException {
        String sql = "SELECT " +
                "color FROM TrainCards " +
                "WHERE gameid=? AND state='inDeck' AND " +
                "sequenceposition=(" +
                "SELECT MAX(sequenceposition) FROM TrainCards WHERE state='inDeck' AND gameID=?" +
                ")";

        TrainCard card = drawCard(sql, gameID, -1);

        movefaceDownToHand(gameID, playerID, card);

        if (deckIsEmpty(gameID)){
            replaceFaceDown(gameID);
        }

        return card;
    }

    private void moveFaceUpToHand(GameID gameID, PlayerID playerID, TrainCard card, int pos) throws DatabaseException {
        String sql = "UPDATE TrainCards " +
                "SET state='inPlayerHand', playerid=?, " +
                "sequenceposition=(" +
                "SELECT MAX(sequenceposition) FROM TrainCards WHERE state='inPlayerHand' AND color=?" +
                ") + 1 " +
                "WHERE state='faceUp' AND gameID=? AND sequenceposition=?";

        try (var statement = connection.prepareStatement(sql)) {

            statement.setString(1, playerID.toString());
            statement.setString(2, getColorAsString(card.getColor()));
            statement.setString(3, gameID.toString());
            statement.setInt(4, pos);

            statement.executeUpdate();

        } catch (SQLException e) {
            logger.catching(e);
            throw new DatabaseException("Could not add cards to hand!", e);
        }
    }

    private void movefaceDownToHand(GameID gameID, PlayerID playerID, TrainCard card) throws DatabaseException {
        String sql = "UPDATE TrainCards " +
                "SET state='inPlayerHand', playerid=?, " +
                "sequenceposition=(" +
                "SELECT MAX(sequenceposition) FROM TrainCards WHERE state='inPlayerHand' AND color=?" +
                ") + 1 " +
                "WHERE state='inDeck' AND gameID=? AND " +
                "sequenceposition=(" +
                "SELECT MAX(sequenceposition) FROM TrainCards WHERE state='inDeck' AND gameID=?" +
                ")";

        try (var statement = connection.prepareStatement(sql)) {

            statement.setString(1, playerID.toString());
            statement.setString(2, getColorAsString(card.getColor()));
            statement.setString(3, gameID.toString());
            statement.setString(4, gameID.toString());

            statement.executeUpdate();

        } catch (SQLException e) {
            logger.catching(e);
            throw new DatabaseException("Could not add cards to hand!", e);
        }
    }

    private TrainCard drawCard(String sql, GameID gameID, int pos) throws DatabaseException {
        TrainCard card = null;
        try (var statement = connection.prepareStatement(sql)){
            statement.setString(1, gameID.toString());
            if (pos >= 0) {
                statement.setInt(2, pos);
            }

            var results = statement.executeQuery();

            while (results.next())
                card = buildTrainCardFromResult(results);

        } catch (SQLException e) {
            logger.catching(e);
            throw new DatabaseException("Could not draw card!", e);
        }
        return card;
    }

    private TrainCard buildTrainCardFromResult(ResultSet result) throws SQLException {
        String color = result.getString("color");
        return new TrainCard(getColorFromString(color));
    }

    private boolean deckIsEmpty(GameID gameID) throws DatabaseException {
        int deckSize = 0;

        String sql = "SELECT * FROM TrainCards " +
                "WHERE gameid=? AND state='inDeck'";
        try (var statement = connection.prepareStatement(sql)){

            statement.setString(1, gameID.toString());

            var results = statement.executeQuery();

            while (results.next())
                deckSize += 1;

        } catch (SQLException e) {
            logger.catching(e);
            throw new DatabaseException("Could not get the facedown deck!", e);
        }

        return deckSize == 0;
    }

    private boolean tooManyWilds(GameID gameID) throws DatabaseException {
        int wildCount = 0;

        String sql = "SELECT * FROM TrainCards " +
                "WHERE gameid=? AND state='faceUp' AND color='WILD'";

        try (var statement = connection.prepareStatement(sql)){

            statement.setString(1, gameID.toString());

            var results = statement.executeQuery();

            while (results.next())
                wildCount += 1;

        } catch (SQLException e) {
            logger.catching(e);
            throw new DatabaseException("Could not get the faceup deck!", e);
        }

        return wildCount >= 3;
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
