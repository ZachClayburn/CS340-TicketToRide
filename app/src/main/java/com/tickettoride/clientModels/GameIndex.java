package com.tickettoride.clientModels;

import java.util.ArrayList;
import java.util.UUID;
import org.jetbrains.annotations.Nullable;


public class GameIndex {
    private ArrayList<Game> joinGameIndex;
    private ArrayList<Game> rejoinGameIndex;
    private ArrayList<Game> fullGames;
    public static GameIndex SINGLETON = new GameIndex();

    private GameIndex(){
        joinGameIndex = new ArrayList<Game>();
        rejoinGameIndex = new ArrayList<Game>();
    }

    public void addGame(Game newGame) {
        fullGames.add(newGame);
        UUID userID = DataManager.getSINGLETON().getSession().getUserID();
        if (!newGame.getIsStarted() && !newGame.forUser(userID)) { joinGameIndex.add(newGame); }
        if (newGame.forUser(userID)) { rejoinGameIndex.add((newGame)); }
    }

    @Nullable
    public Game findGame(String gameID) {
        for (Game game: fullGames) {
            if (game.getGameID().toString().equals(gameID)) {
                return game;
            }
        }
        return null;
    }

    public void setGames(ArrayList<Game> games) {
        this.fullGames = games;
        UUID userID = DataManager.getSINGLETON().getSession().getUserID();
        joinGameIndex = new ArrayList<>();
        for (Game game: games) {
            if (!game.getIsStarted() && !game.forUser(userID)) joinGameIndex.add(game);
        }
        rejoinGameIndex = new ArrayList<>();
        for (Game game: games) { if (game.forUser(userID)) rejoinGameIndex.add(game); }
    }

    public ArrayList<Game> getJoinGameIndex() { return joinGameIndex; }
    public ArrayList<Game> getRejoinGameIndex() { return  rejoinGameIndex; }
}
