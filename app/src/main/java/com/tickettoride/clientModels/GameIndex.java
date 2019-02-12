package com.tickettoride.clientModels;

import android.content.Context;

import com.tickettoride.activities.JoinGameActivity;

import java.util.ArrayList;

import modelInterfaces.IGame;


public class GameIndex {
    private ArrayList<Game> gameIndex;
    private ArrayList<Game> fullGames;
    private JoinGameActivity indexUI;
    public static GameIndex SINGLETON = new GameIndex();

    private GameIndex(){
        gameIndex = new ArrayList<Game>();
        fullGames = new ArrayList<Game>();
    }

    public void setIndexUI(Context c){
        indexUI = (JoinGameActivity)c;
    }

    public ArrayList<Game> getGameIndex() {
        return gameIndex;
    }

    public void addGame(Game newGame) { gameIndex.add(newGame); }

    public Game findGame(String gameID) {
        for (Game game: gameIndex) {
            if (game.getGameID().equals(gameID)) {
                return game;
            }
        }
        return null;
    }

    // Move game to list of full games so it doesn't show up on the UI
    public void makeGameUnavailable(String gameID) {
        Game fullGame = null;
        for (Game game: gameIndex) {
            if (game.getGameID().equals(gameID)) {
                break;
            }
        }
        fullGames.add(fullGame);
        gameIndex.remove(fullGame);
        indexUI.updateUI();
    }

    public void setGames(ArrayList<Game> games) {
        fullGames = games;
        gameIndex = games;
    }
}
