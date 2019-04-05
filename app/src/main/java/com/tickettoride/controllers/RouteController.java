package com.tickettoride.controllers;
import com.google.gson.internal.LinkedTreeMap;
import com.tickettoride.activities.GameRoomActivity;
import com.tickettoride.clientModels.DataManager;
import com.tickettoride.models.Player;
import com.tickettoride.models.PlayerState;
import com.tickettoride.models.Route;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class RouteController extends BaseController {
    private static final RouteController SINGLETON = new RouteController();
    private RouteController() {}

    public static RouteController getSingleton() {
        return SINGLETON;
    }


    public void claim(Route route, Player player, Integer turn, Integer cardsDiscarded, ArrayList<LinkedTreeMap<String, Object>> playerStateMap) throws ClassNotFoundException,
            NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        GameRoomActivity activity = (GameRoomActivity) getCurrentActivity();
        DataManager.SINGLETON.setRouteClaimed(route);
        int spaces = route.getSpaces();
        Player dataManagerPlayer = DataManager.SINGLETON.findPlayerByID(player.getPlayerID());
        dataManagerPlayer.setPoints(dataManagerPlayer.getPoints() + route.getPoints());
        dataManagerPlayer.setTrainCardCount(dataManagerPlayer.getTrainCardCount() - spaces);
        dataManagerPlayer.setTrainCarCount(dataManagerPlayer.getTrainCarCount() - spaces);
        if (isUserPlayer(player) ) { activity.onReturnToMap(); }
        else { activity.getMapFragment().drawExternal(); }

        DataManager.SINGLETON.setTurn(turn);
        List<PlayerState> playerStates = PlayerState.buildPlayerStateList(playerStateMap);
        DataManager.SINGLETON.setCurrentPLayerState(playerStates);
        activity.applyPlayerState();
    }
}
