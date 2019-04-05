package com.tickettoride.models;

import com.google.gson.internal.LinkedTreeMap;
import com.tickettoride.models.idtypes.GameID;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Game {
    private GameID gameID;
    private String groupName;
    private int numPlayer;
    private int maxPlayer;
    private int curTurn = 1;

    private boolean finished = false;
    private boolean isStarted = false;

    public Game() {}

    public Game(String groupName, int maxPlayer) {
        this.gameID = GameID.randomUUID();
        this.groupName = groupName;
        this.numPlayer = 1;
        this.maxPlayer = maxPlayer;
    }

    public Game(GameID gameID, String groupName, int numPlayer, int maxPlayer, boolean isStarted, int curTurn, boolean finished) {
        this.gameID = gameID;
        this.groupName = groupName;
        this.maxPlayer = maxPlayer;
        this.numPlayer = numPlayer;
        this.isStarted = isStarted;
        this.curTurn = curTurn;
        this.finished = finished;
    }

    public Game(GameID gameID, String groupName, int numPlayer, int maxPlayer, boolean isStarted) {
        this.gameID = gameID;
        this.groupName = groupName;
        this.maxPlayer = maxPlayer;
        this.numPlayer = numPlayer;
        this.isStarted = isStarted;
    }

    public Game(Map<String, Object> gameMap) {
        this.gameID = GameID.fromString((String) ((Map) gameMap.get("gameID")).get("uuid"));
        this.groupName = (String) gameMap.get("groupName");
        this.numPlayer = ((Double) gameMap.get("numPlayer")).intValue();
        this.maxPlayer = ((Double) gameMap.get("maxPlayer")).intValue();
        this.isStarted = (Boolean) gameMap.get(("isStarted"));
    }

    public Game(GameID gameID, String groupName, boolean isStarted) {
        this.gameID = gameID;
        this.groupName = groupName;
        this.isStarted = isStarted;
    }

    public void setGameID(GameID gameID) {
        this.gameID = gameID;
    }

    public GameID getGameID() {
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

    public Boolean isFinished() { return finished; }

    public void setFinished(boolean finished) { this.finished = finished; }

    public int getCurTurn() {return curTurn;}

    public void setCurTurn(int turn){curTurn = turn;}

    public void setNextTurn() {
        if (curTurn == numPlayer) {
            curTurn = 1;
        }
        else {
            curTurn += 1;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Game)) return false;
        Game game = (Game) o;
        return getMaxPlayer() == game.getMaxPlayer() &&
                Objects.equals(getGameID(), game.getGameID()) &&
                Objects.equals(getGroupName(), game.getGroupName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getGameID(), getGroupName(), getNumPlayer(), getMaxPlayer(), isStarted());
    }

    @Override
    public String toString() {
        return "Game{" +
                "gameID=" + gameID +
                ", groupName='" + groupName + '\'' +
                ", numPlayer=" + numPlayer +
                ", maxPlayer=" + maxPlayer +
                ", isStarted=" + isStarted +
                '}';
    }

    public static ArrayList<Game> buildGames(List<Map<String, Object>> gameMap) {
        ArrayList<Game> games = new ArrayList<>();
        for (Map<String, Object> singleGameHash : gameMap) {
            Game game = new Game(singleGameHash);
            games.add(game);
        }
        return games;
    }
}
