package com.tickettoride.database.interfaces;

import com.tickettoride.models.Player;
import com.tickettoride.models.idtypes.GameID;
import com.tickettoride.models.idtypes.PlayerID;
import exceptions.DatabaseException;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public interface IPlayerDAO {
    void addPlayer(Player player) throws DatabaseException;

    List<Player> getGamePlayers(GameID gameID) throws DatabaseException;

    //should return 0 or 1 but never more, just set up to handle unexpected cases
    List<GameID> getGameForPlayer(PlayerID playerID) throws DatabaseException;

    @Nullable
    Player getPlayerByPlayerID(PlayerID playerID) throws DatabaseException;

    void setTurn(PlayerID playerID, int turn) throws DatabaseException;

    void setPoints(PlayerID playerID, int points) throws DatabaseException;

    void setTrainCarCount(PlayerID playerID, int trainCarCount) throws DatabaseException;

    void setPlayersUserName(List<Player> players) throws DatabaseException;

    void deletePlayer(UUID sessionID) throws SQLException;
}
