package com.tickettoride.models;

import java.util.UUID;

public class Session {

    private UUID userID;
    private UUID sessionID;

    public Session(User user) {
        this.userID = user.getUserID();
        this.sessionID = UUID.randomUUID();
    }

    public Session(UUID sessionID, UUID userID) {
        this.userID = userID;
        this.sessionID = sessionID;
    }

    public Session() {}

    public Session(UUID sessionID) {
        this.sessionID = sessionID;
    }

    public UUID getUserID() { return userID; }

    public UUID getSessionID() { return  sessionID; }
}
