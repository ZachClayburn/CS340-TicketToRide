package com.tickettoride.controllers.helpers;

import com.google.gson.internal.LinkedTreeMap;
import com.tickettoride.models.Player;

import java.util.ArrayList;
import java.util.List;

public class GameControllerHelper {
    private static GameControllerHelper SINGLETON = new GameControllerHelper();
    public static GameControllerHelper getSingleton() { return SINGLETON; }
    private GameControllerHelper() {}

    public List<Player> buildPlayerList(List<LinkedTreeMap<String, Object>> players) {
        List<Player> playerList = new ArrayList<>();
        for (LinkedTreeMap<String, Object> playerMap : players) {
            Player player = new Player(playerMap);
            playerList.add(player);
        }
        return playerList;
    }
}
