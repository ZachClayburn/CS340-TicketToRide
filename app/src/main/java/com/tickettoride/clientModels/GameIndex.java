package com.tickettoride.clientModels;

import com.tickettoride.models.Game;

import java.util.ArrayList;


public class GameIndex {
    private ArrayList<Game> joinGameIndex;
    private ArrayList<Game> rejoinGameIndex;
    public static GameIndex SINGLETON = new GameIndex();

    private GameIndex(){
        joinGameIndex = new ArrayList<>();
        rejoinGameIndex = new ArrayList<>();
    }

    public void addJoinGame(Game game) { joinGameIndex.add(game); }
    public void addRejoinGame(Game game) { rejoinGameIndex.add(game); }

    public void setJoinGameIndex(ArrayList<Game> games) { this.joinGameIndex = games;
    }

    public void setRejoinGameIndex(ArrayList<Game> games) { this.rejoinGameIndex = games; }

    public ArrayList<Game> getJoinGameIndex() { return joinGameIndex; }
    public ArrayList<Game> getRejoinGameIndex() { return  rejoinGameIndex; }
}
