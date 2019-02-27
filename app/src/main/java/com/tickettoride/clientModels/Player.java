package com.tickettoride.clientModels;

import com.google.gson.internal.LinkedTreeMap;

import java.io.Serializable;
import java.util.UUID;

public class Player implements Serializable {
    private UUID gameID;
    private UUID userID;
    private UUID playerID;

    public Player(UUID gameID, UUID userID, UUID playerID) {
        this.gameID = gameID;
        this.userID = userID;
        this.playerID = playerID;
    }

    public Player(LinkedTreeMap<String, Object> playerMap) {
        this.gameID = UUID.fromString((String) playerMap.get("gameID"));
        this.playerID = UUID.fromString((String) playerMap.get("playerID"));
        this.userID = UUID.fromString((String) playerMap.get("userID"));
    }


    public UUID getGameID() {
        return gameID;
    }

    public void setGameID(UUID gameID) {
        this.gameID = gameID;
    }

    public UUID getUserID() {
        return userID;
    }

    public void setUserID(UUID userID) {
        this.userID = userID;
    }

    public UUID getPlayerID() {
        return playerID;
    }

    public void setPlayerID(UUID playerID) {
        this.playerID = playerID;
    }
}
