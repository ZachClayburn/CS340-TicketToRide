package com.tickettoride.models;

import java.util.*;

import modelAttributes.PlayerColor;

public class Player {
    private int turn = 0;
    private PlayerColor color = null;
    private String username = "";
    private UUID userID;
    private UUID gameID;
    private UUID playerID;

    public Player(UUID user, UUID game) {
        this.userID = user;
        this.gameID = game;
        this.playerID = UUID.randomUUID();
    }
    
    public Player(UUID user, UUID game, UUID player, int turn){
        this.userID=user;
        this.gameID=game;
        this.playerID=player;
        this.turn = turn;
    }

    public Player(UUID user, UUID game, UUID player, int turn, PlayerColor color){
        this.userID=user;
        this.gameID=game;
        this.playerID=player;
        this.turn = turn;
        this.color = color;
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

    public int getTurn() {return turn;}

    public void setTurn(int turn) {this.turn = turn;}

    public PlayerColor getColor() {
        return color;
    }

    public String getUsername(){return username;}

    public void setUsername(String username){this.username = username;}

    public void setColor() {
        switch (turn) {
            case 1:
                color = PlayerColor.RED;
                break;
            case 2:
                color = PlayerColor.BLUE;
                break;
            case 3:
                color = PlayerColor.GREEN;
                break;
            case 4:
                color = PlayerColor.BLACK;
                break;
            case 5:
                color = PlayerColor.YELLOW;
                break;
            default:
                break;
        }
    }

    public UUID getPlayerID() { return playerID; }

    public void setPlayerID(UUID playerID) { this.playerID = playerID; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player)) return false;
        Player player = (Player) o;
        return userID.equals(player.userID) &&
                gameID.equals(player.gameID) &&
                getPlayerID().equals(player.getPlayerID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(userID, gameID, getPlayerID());
    }

    @Override
    public String toString() {
        return "Player{" +
                "userID=" + userID +
                ", gameID=" + gameID +
                ", playerID=" + playerID +
                '}';
    }
}
