package com.tickettoride.controllers;

import com.tickettoride.activities.GameRoomActivity;
import com.tickettoride.clientModels.Route;
import com.tickettoride.clientModels.helpers.RouteHelper;
import com.tickettoride.models.Player;

public class RoutesController extends BaseController {
    private static final  RoutesController SINGLETON = new RoutesController();
    private RoutesController() {}

    public static RoutesController getSingleton() {
        return SINGLETON;
    }

    public void claimRoute(Player player, Route route) {
        GameRoomActivity activity = (GameRoomActivity) getCurrentActivity();
        activity.incrementTurn();
        if(isUserPlayer(player) ) {
            RouteHelper.getSingleton().claimRoute(route);
            activity.onReturnToMap();
        } else {
            RouteHelper.getSingleton().claimRoute(route, player);
        }
    }
}
