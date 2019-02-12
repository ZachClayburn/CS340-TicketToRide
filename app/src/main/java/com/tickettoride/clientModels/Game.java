package com.tickettoride.clientModels;

import com.google.gson.internal.LinkedTreeMap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

import modelInterfaces.IGame;

public class Game implements IGame, Serializable { //used for when creating a new game
    private UUID gameID = null;
    private String groupName = "";
    private int maxPlayer = 5;
    private int numPlayer = 1;
    private ArrayList<Player> players = new ArrayList();

    public Game(UUID gameID, String groupName, int numPlayer, int maxPlayer, Player player) {
        this.gameID = gameID;
        this.groupName = groupName;
        this.numPlayer = numPlayer;
        this.maxPlayer = maxPlayer;
        players.add(player);
    }

    public Game(){}

    public Game(LinkedTreeMap<String, String> gameMap) {
        this.gameID = UUID.fromString(gameMap.get("gameID"));
        this.groupName = gameMap.get("groupName");
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

    public void addPlayer(Player player){
        players.add(player);
        numPlayer += 1;
    }
}
