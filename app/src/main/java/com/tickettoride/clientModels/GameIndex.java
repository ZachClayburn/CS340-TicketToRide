package com.tickettoride.clientModels;

import java.util.ArrayList;
import java.util.List;

public class GameIndex {
    private List<GameInfo> gameIndex;

    private static GameIndex SINGLETON = new GameIndex();

    private GameIndex(){gameIndex = new ArrayList<GameInfo>();}

    public List<GameInfo> getGameIndex() {
        return gameIndex;
    }

    public void addGames(ArrayList<GameInfo> newGames) {
        for (GameInfo game : newGames) {
            gameIndex.add(game);
        }
    }
}
