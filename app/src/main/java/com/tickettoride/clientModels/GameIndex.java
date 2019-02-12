package com.tickettoride.clientModels;

import android.content.Context;
import android.util.Log;

import com.tickettoride.activities.JoinGameActivity;

import java.util.ArrayList;

import modelInterfaces.IGame;
import org.jetbrains.annotations.Nullable;


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

    @Nullable
    public Game findGame(String gameID) {
        for (Game game: gameIndex) {
            if (game.getGameID().toString().equals(gameID)) {
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
                fullGame = game;
            }
        }
        fullGames.add(fullGame);
        gameIndex.remove(fullGame);
        indexUI.updateUI();
    }

    // Move game to normal gameIndex if someone left
    public void makeGameAvailable(String gameID) {
        Game openGame = null;
        for (Game game: fullGames) {
            if (game.getGameID().toString().equals(gameID)) {
                openGame = game;
                fullGames.remove(openGame);
                gameIndex.add(openGame);
                break;
            }
        }
    }

    public void addGames(ArrayList<IGame> games) {
//        for (IGame game : games) {
//            Game newGame = new Game(game);
//            fullGames.add(newGame);
//        }
    }
  
    public void setGames(ArrayList<Game> games) {
        ArrayList<Game> gameIndex = new ArrayList<>();
        for (Game game: games) { if (game.getNumPlayer() < game.getMaxPlayer()) gameIndex.add(game); }
        this.fullGames = games;
        this.gameIndex = gameIndex;
    }
}
