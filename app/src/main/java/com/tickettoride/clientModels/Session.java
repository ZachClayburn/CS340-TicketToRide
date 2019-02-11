package com.tickettoride.clientModels;

import java.util.UUID;

import modelAttributes.Username;
import modelInterfaces.ISession;
import modelInterfaces.IUser;

// Current Session
public class Session implements ISession {
    private UUID sessionId;

    public Session(UUID sessionId) { this.sessionId = sessionId; }

    public UUID getSessionId() {
        return sessionId;
    }

    public void setSessionId(UUID sessionId) {
        this.sessionId = sessionId;
    }

}
