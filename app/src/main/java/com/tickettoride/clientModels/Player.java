package com.tickettoride.clientModels;

import java.io.Serializable;
import java.util.UUID;

import modelAttributes.PlayerColor;

public class Player implements Serializable {
    private UUID gameID;
    private UUID sessionID;
    private UUID playerID;
    private int turn = 0;
    private PlayerColor color = null;
    private int points = 0;
    private String username = "";

    public Player(UUID gameID, UUID sessionID, UUID playerID) {
        this.gameID = gameID;
        this.sessionID = sessionID;
        this.playerID = playerID;
    }

    public Player(UUID gameID, UUID sessionID, UUID playerID, int turn, PlayerColor color) {
        this.gameID = gameID;
        this.sessionID = sessionID;
        this.playerID = playerID;
        this.turn = turn;
        this.color = color;
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

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public PlayerColor getColor() {
        return color;
    }

    public void setColor(PlayerColor color) {
        this.color = color;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getUsername() {return username;}
}
