package com.tickettoride.models;

import java.util.UUID;

import modelInterfaces.ISession;

public class Session implements ISession {

    private UUID userID;
    private UUID sessionID;

    public Session(User user) {
        this.userID = user.getUserID();
        this.sessionID = UUID.randomUUID();
    }

    public Session(UUID sessionID) {
        this.sessionID = sessionID;
    }

    public UUID getUserID() { return userID; }

    public UUID getSessionID() { return  sessionID; }
}
