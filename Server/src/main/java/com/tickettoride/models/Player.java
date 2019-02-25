package com.tickettoride.models;

import java.util.*;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player)) return false;
        Player player = (Player) o;
        return userid.equals(player.userid) &&
                gameid.equals(player.gameid) &&
                getPlayerID().equals(player.getPlayerID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(userid, gameid, getPlayerID());
    }

    @Override
    public String toString() {
        return "Player{" +
                "userid=" + userid +
                ", gameid=" + gameid +
                ", playerID=" + playerID +
                '}';
    }
}
