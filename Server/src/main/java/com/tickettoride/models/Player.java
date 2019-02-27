package com.tickettoride.models;

import java.util.UUID;

public class Player {
    private UUID userID;
    private UUID gameID;
    private UUID playerID;

    public Player(UUID user, UUID game) {
        this.userID = user;
        this.gameID = game;
        this.playerID = UUID.randomUUID();
    }

    public Player(UUID userID, UUID gameID, UUID playerID){
        this.userID = userID;
        this.gameID = gameID;
        this.playerID = playerID;
    }

    public UUID getUserID() { return userID; }

    public void setUserID(UUID userID) { this.userID = userID; }

    public UUID getGameID() { return gameID; }

    public void setGameID(UUID gameID) { this.gameID = gameID; }

    public UUID getPlayerID() { return playerID; }

    public void setPlayerID(UUID playerID) { this.playerID = playerID; }
}
