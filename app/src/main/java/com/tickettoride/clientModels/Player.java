package com.tickettoride.clientModels;

import java.util.UUID;

public class Player {
    private UUID gameID;
    private String sessionID;
    private UUID playerID;

    public Player(UUID gameID, String sessionID, UUID playerID) {
        this.gameID = gameID;
        this.sessionID = sessionID;
        this.playerID = playerID;
    }

    public UUID getGameID() {
        return gameID;
    }

    public void setGameID(UUID gameID) {
        this.gameID = gameID;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public UUID getPlayerID() {
        return playerID;
    }

    public void setPlayerID(UUID playerID) {
        this.playerID = playerID;
    }
}
