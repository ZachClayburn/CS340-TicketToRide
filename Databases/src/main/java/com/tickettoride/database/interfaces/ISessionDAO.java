package com.tickettoride.database.interfaces;

import com.tickettoride.models.Session;
import com.tickettoride.models.idtypes.SessionID;
import exceptions.DatabaseException;

import java.sql.SQLException;

public interface ISessionDAO {
    void createSession(Session session) throws DatabaseException;

    Session findSession(SessionID sessionID) throws DatabaseException;

    void deleteSession(SessionID sessionID) throws SQLException;
}
