package com.tickettoride.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import modelInterfaces.IGame;

public class Game implements IGame {
    private UUID gameID;
    private String groupName;
    private int numPlayer;
    private int maxPlayer;

    private boolean isStarted = false;

    private List<DestinationCard> destinationDeck = new ArrayList<>();

    public List<DestinationCard> getDestinationDeck() {
        return destinationDeck;
    }

    public void setDestinationDeck(List<DestinationCard> destinationDeck) {
        this.destinationDeck = destinationDeck;
    }

    private ArrayList<User> players = new ArrayList<>();
    public Game(String groupName, int maxPlayer, User creator) {
        this.gameID = UUID.randomUUID();
        this.groupName = groupName;
        this.numPlayer = 1;
        this.maxPlayer = maxPlayer;
        players.add(creator);
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Game)) return false;
        Game game = (Game) o;
        return getNumPlayer() == game.getNumPlayer() &&
                getMaxPlayer() == game.getMaxPlayer() &&
                isStarted() == game.isStarted() &&
                Objects.equals(getGameID(), game.getGameID()) &&
                Objects.equals(getGroupName(), game.getGroupName()) &&
                Objects.equals(getDestinationDeck(), game.getDestinationDeck()) &&
                Objects.equals(players, game.players);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getGameID(), getGroupName(), getNumPlayer(), getMaxPlayer(), isStarted(), getDestinationDeck(), players);
    }

    @Override
    public String toString() {
        return "Game{" +
                "gameID=" + gameID +
                ", groupName='" + groupName + '\'' +
                ", numPlayer=" + numPlayer +
                ", maxPlayer=" + maxPlayer +
                ", isStarted=" + isStarted +
                ", destinationDeck=" + destinationDeck +
                ", players=" + players +
                '}';
    }
}
