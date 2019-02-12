package com.tickettoride.clientModels;

import com.google.gson.internal.LinkedTreeMap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

import modelInterfaces.IGame;

public class Game implements IGame, Serializable { //used for when creating a new game
    private UUID gameID = null;
    private String groupName = "";
    private int maxPlayer = 1;
    private int numPlayer = 1;
    private Boolean isStarted;
    private ArrayList<Player> players = new ArrayList();

    public Game(UUID gameID, String groupName, int numPlayer, int maxPlayer, Player player, Boolean isStarted) {
        this.gameID = gameID;
        this.groupName = groupName;
        this.numPlayer = numPlayer;
        this.maxPlayer = maxPlayer;
        this.isStarted = isStarted;
        players.add(player);
    }

    public Game(){}

    public Game(LinkedTreeMap<String, Object> gameMap) {
        this.gameID = UUID.fromString((String) gameMap.get("gameID"));
        this.groupName = (String) gameMap.get("groupName");
        this.numPlayer = ((Double) gameMap.get("numPlayer")).intValue();
        this.maxPlayer = ((Double) gameMap.get("maxPlayer")).intValue();
        this.isStarted = (Boolean) gameMap.get(("isStarted"));
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

    public int getMaxPlayer(){return maxPlayer;}

    public void setMaxPlayer(int num) {
        maxPlayer = num;
    }

    public Boolean getIsStarted() { return  isStarted; }

    public void addPlayer(Player player){
        players.add(player);
        numPlayer += 1;
    }

    public static ArrayList<Game> buildGames(ArrayList<LinkedTreeMap> gameMap) {
        ArrayList<Game> games = new ArrayList<>();
        for (LinkedTreeMap singleGameHash : gameMap) {
            Game game = new Game(singleGameHash);
            games.add(game);
        }
        return games;
    }
}
