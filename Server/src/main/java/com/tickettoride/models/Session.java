package com.tickettoride.models;

import java.util.UUID;

import modelInterfaces.ISession;

public class Session implements ISession {

    private User user;
    private UUID sessionID;

    public Session(User user) {
        this.user = user;
        this.sessionID = UUID.randomUUID();
    }

    public Session(UUID sessionID) {
        this.sessionID = sessionID;
    }

    public User getUser() { return user; }

    public UUID getSessionID() { return  sessionID; }
}
