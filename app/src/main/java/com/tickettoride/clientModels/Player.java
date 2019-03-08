package com.tickettoride.clientModels;

import com.google.gson.internal.LinkedTreeMap;
import com.tickettoride.models.DestinationCard;
import com.tickettoride.models.Hand;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

import modelAttributes.PlayerColor;

public class Player implements Serializable {
    private UUID gameID;
    private UUID userID;
    private UUID playerID;
    private UUID sessionID;
    private int turn = 0;
    private PlayerColor color = null;
    private int points = 0;
    private String username = "";
    private Hand playerHand = new Hand();
    private ArrayList<DestinationCard> destCards = new ArrayList<>();

    public Player(UUID gameID, UUID userID, UUID playerID) {
        this.gameID = gameID;
        this.userID = userID;
        this.playerID = playerID;
    }

    public Player(UUID gameID, UUID sessionID, UUID playerID, int turn, PlayerColor color) {
        this.gameID = gameID;
        this.sessionID = sessionID;
        this.playerID = playerID;
        this.turn = turn;
        this.color = color;
    }


    public Player(LinkedTreeMap<String, Object> playerMap) {
        this.gameID = UUID.fromString((String) playerMap.get("gameID"));
        this.playerID = UUID.fromString((String) playerMap.get("playerID"));
        this.userID = UUID.fromString((String) playerMap.get("userID"));
        this.turn = ((Double) playerMap.get("turn")).intValue();
        this.color = PlayerColor.valueOf((String) playerMap.get("color"));
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

    public Hand getPlayerHand() {
        return playerHand;
    }

    public ArrayList<DestinationCard> getDestCards() {
        return destCards;
    }
}
