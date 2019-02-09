package com.tickettoride.clientModels;

import android.content.Context;

import com.tickettoride.activities.LobbyActivity;

import java.util.ArrayList;

import modelInterfaces.IGame;

public class GameInfo implements IGame{ //used for when creating a new game
    private String gameID = "";
    private String groupName = "";
    private int maxPlayer = 5;
    private int numPlayer = 1;
    private ArrayList<Player> players = new ArrayList();
    private LobbyActivity lobbyUI = new LobbyActivity();

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

    public int getMaxPlayer(){return maxPlayer;}

    public void setMaxPlayer(int num) {
        maxPlayer = num;
    }

    public void addPlayer(Player player){
        players.add(player);
        numPlayer += 1;
        lobbyUI.updateUI();
    }

    public void setLobbyUI(Context c){
        lobbyUI = (LobbyActivity)c;
    }
}
