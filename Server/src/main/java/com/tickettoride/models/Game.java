package com.tickettoride.models;

import java.util.ArrayList;

import modelInterfaces.IGame;

public class Game implements IGame {
    private String gameID = "";
    private String groupName = "";
    private int numPlayer = 0;
    private ArrayList<User> players = new ArrayList<>();

    public Game(String gameID, String groupName, int numPlayer, User creator) {
        this.gameID = gameID;
        this.groupName = groupName;
        this.numPlayer = numPlayer;
        players.add(creator);
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }
    public String getGameID() {
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
}
