package com.tickettoride.clientModels;

import java.util.UUID;

import modelAttributes.Username;
import modelInterfaces.ISession;
import modelInterfaces.IUser;

// Current Session
public class Session implements ISession {
    private UUID sessionId;
    private UUID userID;

    public Session(UUID sessionId, UUID userID) {
        this.sessionId = sessionId;
        this.userID = userID;
    }

    public UUID getSessionId() {
        return sessionId;
    }
    public void setSessionId(UUID sessionId) {
        this.sessionId = sessionId;
    }
    public UUID getUserID() { return  userID; }
    public void setUserID(UUID userID) { this.userID = userID; }
}
