package com.tickettoride.database.interfaces;

import com.tickettoride.models.PlayerState;
import com.tickettoride.models.idtypes.GameID;
import com.tickettoride.models.idtypes.PlayerID;
import com.tickettoride.models.idtypes.PlayerStateID;
import exceptions.DatabaseException;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface IPlayerStateDAO {
    void addPlayerState(PlayerState playerState) throws DatabaseException;

    PlayerState getPlayerState(PlayerStateID playerStateID) throws DatabaseException, ClassNotFoundException,
            NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException;

    PlayerState getPlayerState(PlayerID playerID) throws DatabaseException, ClassNotFoundException,
                    NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException;

    void updatePlayerState(PlayerState playerState) throws DatabaseException;

    List<PlayerState> getGamePlayerStates(GameID gameID) throws DatabaseException, ClassNotFoundException,
                            NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException;

    PlayerState buildPlayerStateFromResult(ResultSet result) throws SQLException, ClassNotFoundException,
                                    NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException;
}
