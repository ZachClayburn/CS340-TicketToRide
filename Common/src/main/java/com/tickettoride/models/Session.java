package com.tickettoride.models;

import com.tickettoride.models.idtypes.SessionID;
import com.tickettoride.models.idtypes.UserID;

import java.util.UUID;

public class Session {

    private UserID userID;
    private SessionID sessionID;

    public Session(User user) {
        this.userID = user.getUserID();
        this.sessionID = SessionID.randomUUID();
    }

    public Session(SessionID sessionID, UserID userID) {
        this.userID = userID;
        this.sessionID = sessionID;
    }

    public Session() {}

    public Session(SessionID sessionID) {
        this.sessionID = sessionID;
    }

    public UserID getUserID() { return userID; }

    public SessionID getSessionID() { return  sessionID; }
}
