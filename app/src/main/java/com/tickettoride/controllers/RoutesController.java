package com.tickettoride.controllers;

import android.widget.Toast;

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
            try {
                Toast.makeText(getCurrentActivity(), "CLAIM ROUTE", Toast.LENGTH_LONG).show();
            } catch (Throwable t) {}
            RouteHelper.getSingleton().claimRoute(route, player);
            activity.getMapFragment().externalDraw();
        }
    }
}
