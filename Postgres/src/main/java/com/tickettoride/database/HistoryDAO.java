package com.tickettoride.database;

import com.tickettoride.database.interfaces.IHistoryDAO;
import com.tickettoride.models.Message;
import com.tickettoride.models.idtypes.GameID;
import com.tickettoride.models.idtypes.PlayerID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import exceptions.DatabaseException;

public class HistoryDAO extends Database.DataAccessObject implements IHistoryDAO {
    private final String tableCreateString =
            // language=PostgreSQL
            "CREATE TABLE Histories" +
                    "(" +
                    "gameID TEXT NOT NULL CHECK ( length(gameID) > 0 )," +
                    "message BYTEA NOT NULL," +
                    "FOREIGN KEY (gameID) REFERENCES games(gameid)" +
                    ");";

    /**constructor
     *
     * pre: connection is not null and is a valid connection
     * post: creates a working ChatDao to the specific connection and returns it.
     *
     * @param connection the database connection to be used in transactions
     */
    public HistoryDAO(Connection connection) {
        super(connection);
    }

    private Logger logger = LogManager.getLogger(GameDAO.class.getName());

    @Override
    String getTableCreateString() {
        return tableCreateString;
    }

    /** method to add a message to the database for a certain game
     *
     * pre: ChatDao has been initialized, it's connection is still valid, gameId corresponds to a game on the given database, 
     *      and message is a valid message with a non-null time, message and playerid corresponds to a player in the corresponding game
     *
     * post: if something goes wrong with the sql statement a Database exception is thrown
     *      otherwise, the message is recorded relative to the game in the chat database table
     *
     * @param gameID the gameid to associate to the message
     * @param message the message to be stored
     * @throws DatabaseException if the tranaction failed to execute
     */
    @Override
    public void addEvent(GameID gameID, Message message) throws DatabaseException {
        final String sql = "INSERT INTO Histories (gameID, message) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, gameID.toString());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(message);
            byte[] messageByteArray = baos.toByteArray();
            ByteArrayInputStream bais = new ByteArrayInputStream(messageByteArray);
            
            statement.setBinaryStream(2, bais, messageByteArray.length);
            statement.executeUpdate();
        } catch (SQLException e) { throw new DatabaseException("Could not add new message to Database!", e); }
        catch(IOException e){
            throw new DatabaseException("Could not serialize new message into Database!", e);
        }
    }

    /** Method for retrieving the chat for a game from the database
     *
     * pre: gameid corresponds to a game (nad is not null), connection is still valid
     *
     * post: if something goes wrong with the sql statement a Database exception is thrown
     *      otherwise, a list of all the mesages corresponding to the gameid passed in are returned
     *      if there are no messages for the given game an empty list is returned
     *
     * @param gameID the game id to retrieve all the messages associated with it
     * @return a list of the messages associated with the given game. Returns an empty list if no messages
     * @throws DatabaseException if the sql statement throws an exception
     */
    @Override
    public List<Message> getHistory(GameID gameID) throws DatabaseException{
        List<Message> messages=new ArrayList<>();
        String sql = "SELECT message FROM Histories WHERE gameID = ?";
        try (var statement = connection.prepareStatement(sql)) {
            statement.setString(1, gameID.toString());
            var result = statement.executeQuery();
            while (result.next()) {
                byte[] st = (byte[]) result.getObject(1);
                ByteArrayInputStream baip = new ByteArrayInputStream(st);
                ObjectInputStream ois = new ObjectInputStream(baip);
                Message message=(Message)ois.readObject();
                //PlayerID tablePlayerID = PlayerID.fromString(result.getString("playerID"));
                //String message=result.getString("message");
                //LocalDateTime ldt=result.getObject("dateTime", LocalDateTime.class);
                messages.add(message);
            }

        } catch (SQLException e) {
            throw new DatabaseException("Could not retrieve history for game", e);
        } catch (IOException e){
            throw new DatabaseException("Could not parse message for history for game", e);
        } catch (ClassNotFoundException e){
            throw new DatabaseException("Could not deserialize message for history for game", e);
        }
        return messages;
    }
}
