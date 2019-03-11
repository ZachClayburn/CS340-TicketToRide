package com.tickettoride.facadeProxies;

import com.tickettoride.clientModels.DataManager;
import com.tickettoride.clientModels.Route;
import com.tickettoride.controllers.RoutesController;
import com.tickettoride.controllers.TrainCardController;
import com.tickettoride.models.Player;

public class RoutesFacadeProxy {
    public static final String TAG = "ROUTES_FACADE_PROXY";
    private static final String FACADE_NAME = "RoutesFacade";
    public static RoutesFacadeProxy SINGLETON = new RoutesFacadeProxy();
    private RoutesFacadeProxy(){}

    public void claimRoute(Route route) {
        RoutesController.getSingleton().claimRoute(DataManager.getSINGLETON().getPlayer(), route);
    }
}
