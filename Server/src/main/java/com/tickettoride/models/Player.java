package com.tickettoride.models;

import java.util.UUID;

import modelAttributes.PlayerColor;

public class Player {
    private UUID userid = null;
    private UUID gameid = null;
    private UUID playerID = null;
    private int turn = 0;
    private PlayerColor color = null;

    public Player(UUID user, UUID game) {
        this.userid = user;
        this.gameid = game;
        this.playerID = UUID.randomUUID();
    }
    
    public Player(UUID user, UUID game, UUID player, int turn){
        this.userid=user;
        this.gameid=game;
        this.playerID=player;
        this.turn = turn;
    }

    public Player(UUID user, UUID game, UUID player, int turn, PlayerColor color){
        this.userid=user;
        this.gameid=game;
        this.playerID=player;
        this.turn = turn;
        this.color = color;
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

    public int getTurn() {return turn;}

    public void setTurn(int turn) {this.turn = turn;}

    public PlayerColor getColor() {
        return color;
    }

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
}
