package com.tickettoride.controllers;
import com.tickettoride.activities.GameRoomActivity;
import com.tickettoride.clientModels.DataManager;
import com.tickettoride.models.Player;
import com.tickettoride.models.Route;

public class RoutesController extends BaseController {
    private static final  RoutesController SINGLETON = new RoutesController();
    private RoutesController() {}

    public static RoutesController getSingleton() {
        return SINGLETON;
    }

    public void claim(Route route, Player player, Integer turn) {
        GameRoomActivity activity = (GameRoomActivity) getCurrentActivity();
        DataManager.SINGLETON.setRouteClaimed(route);
        int spaces = route.getSpaces();
        int playerPoints = DataManager.SINGLETON.getPlayer().getPoints();
        DataManager.SINGLETON.getPlayer().setPoints(playerPoints + spaces);
        if(isUserPlayer(player) ) { activity.onReturnToMap(); } else { activity.getMapFragment().drawExternal(); }
        DataManager.SINGLETON.setTurn(turn);
        activity.setTurn();
    }
}
