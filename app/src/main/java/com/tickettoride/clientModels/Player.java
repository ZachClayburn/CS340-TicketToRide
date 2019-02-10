package com.tickettoride.clientModels;

import java.util.UUID;

public class Player {
    private UUID gameID;
    private UUID sessionID;
    private UUID playerID;

    public Player(UUID gameID, UUID sessionID, UUID playerID) {
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

    public UUID getSessionID() {
        return sessionID;
    }

    public void setSessionID(UUID sessionID) {
        this.sessionID = sessionID;
    }

    public UUID getPlayerID() {
        return playerID;
    }

    public void setPlayerID(UUID playerID) {
        this.playerID = playerID;
    }
}
