package com.tickettoride.clientModels;

import modelInterfaces.IGame;

public class GameInfo implements IGame{ //used for when creating a new game
    private String gameID = "";
    private String groupName = "";
    private int numPlayer = 0;

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
