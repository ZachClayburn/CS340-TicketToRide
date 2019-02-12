package com.tickettoride.models;

import java.util.UUID;

public class Player {
    private UUID userid = null;
    private UUID gameid = null;
    private UUID playerID = null;

    public Player(UUID user, UUID game) {
        this.userid = user;
        this.gameid = game;
        this.playerID = UUID.randomUUID();
    }
    
    public Player(UUID user, UUID game, UUID player){
        this.userid=user;
        this.gameid=game;
        this.playerID=player;
    }

    public UUID getUserID() {
        return userid;
    }

    public void setUserID(UUID userid) {
        this.userid = userid;
    }

    public UUID getGameID() {
        return gameid;
    }

    public void setGameID(UUID gameid) {
        this.gameid = gameid;
    }

    public UUID getPlayerID() {
        return playerID;
    }

    public void setPlayerID(UUID playerID) {
        this.playerID = playerID;
    }
}
