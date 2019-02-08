package com.tickettoride.clientModels;

import android.content.Context;
import android.icu.text.TimeZoneFormat;

import com.tickettoride.activities.JoinGameActivity;

import java.util.ArrayList;


public class GameIndex {
    private ArrayList<GameInfo> gameIndex;
    private ArrayList<GameInfo> fullGames;
    private JoinGameActivity indexUI;
    public static GameIndex SINGLETON = new GameIndex();

    private GameIndex(){
        gameIndex = new ArrayList<GameInfo>();
        fullGames = new ArrayList<GameInfo>();
    }

    public void setIndexUI(Context c){
        indexUI = (JoinGameActivity)c;
    }

    public ArrayList<GameInfo> getGameIndex() {
        return gameIndex;
    }

    public void addGames(ArrayList<GameInfo> newGames) {
        gameIndex.addAll(newGames);
        indexUI.updateUI();
    }

    public GameInfo findGame(String gameID) {
        for (GameInfo game: gameIndex) {
            if (game.getGameID().equals(gameID)) {
                return game;
            }
        }
        return null;
    }

    // Move game to list of full games so it doesn't show up on the UI
    public void makeGameUnavailable(String gameID) {
        GameInfo fullGame = null;
        for (GameInfo game: gameIndex) {
            if (game.getGameID().equals(gameID)) {
                break;
            }
        }
        fullGames.add(fullGame);
        gameIndex.remove(fullGame);
        indexUI.updateUI();
    }
}
