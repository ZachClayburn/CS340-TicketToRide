package com.tickettoride.models;

import java.util.ArrayList;
import java.util.UUID;

import modelInterfaces.IGame;

public class Game implements IGame {
    private UUID gameID = null;
    private String groupName = "";
    private int numPlayer = 0;
    private int maxPlayer = 5;
    private ArrayList<User> players = new ArrayList<>();

    public Game(UUID gameID, String groupName, int numPlayer, int maxPlayer, User creator) {
        this.gameID = gameID;
        this.groupName = groupName;
        this.numPlayer = numPlayer;
        this.maxPlayer = maxPlayer;
        players.add(creator);
    }

    public Game(UUID gameID, String groupName, int numPlayer, int maxPlayer) {
        this.gameID = gameID;
        this.groupName = groupName;
        this.numPlayer = numPlayer;
        this.maxPlayer = maxPlayer;
    }

    public void setGameID(UUID gameID) {
        this.gameID = gameID;
    }

    public UUID getGameID() {
        return gameID;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setNumPlayer(int numPlayer) {
        this.numPlayer = numPlayer;
    }

    public int getNumPlayer() {
        return numPlayer;
    }

    public void setMaxPlayer(int num) {
        maxPlayer = num;
    }

    public int getMaxPlayer() {
        return maxPlayer;
    }
}
