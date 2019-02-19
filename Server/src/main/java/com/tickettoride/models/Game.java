package com.tickettoride.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import modelInterfaces.IGame;

public class Game implements IGame {
    private UUID gameID = null;
    private String groupName = "";
    private int numPlayer = 0;
    private int maxPlayer = 5;
    private boolean isStarted = false;

    private ArrayList<Player> players = new ArrayList<>();

    public Game(String groupName, int maxPlayer) {
        this.gameID = UUID.randomUUID();
        this.groupName = groupName;
        this.numPlayer = 1;
        this.maxPlayer = maxPlayer;
    }

    public Game(UUID gameID, String groupName, int numPlayer, int maxPlayer, boolean isStarted) {
        this.gameID = gameID;
        this.groupName = groupName;
        this.maxPlayer = maxPlayer;
        this.numPlayer = numPlayer;
        this.isStarted = isStarted;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public List<Player> getPlayers() { return players; }

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

    public boolean isStarted() {
        return isStarted;
    }

    public void setStarted(boolean started) {
        isStarted = started;
    }

    public Boolean IsStarted() { return isStarted; }

    public void addPLayer(Player player) {
        players.add(player);
    }

}
