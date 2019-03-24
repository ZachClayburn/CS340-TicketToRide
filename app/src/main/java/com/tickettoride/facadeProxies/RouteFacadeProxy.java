package com.tickettoride.facadeProxies;

import com.tickettoride.clientModels.ClientRoute;
import com.tickettoride.clientModels.DataManager;
import com.tickettoride.command.ClientCommunicator;
import com.tickettoride.models.Route;

import command.Command;

public class RouteFacadeProxy {
    public static final String TAG = "ROUTE_FACADE_PROXY";
    private static final String FACADE_NAME = "RouteFacade";
    public static RouteFacadeProxy SINGLETON = new RouteFacadeProxy();
    private RouteFacadeProxy(){}

    public void claimRoute(ClientRoute clientRoute) {
        try{
            Route route = clientRoute.toBasicRoute();
            Command command = new Command(FACADE_NAME, "claim", route, DataManager.getSINGLETON().getPlayer());
            ClientCommunicator.SINGLETON.send(command);
        } catch(Throwable t){}
    }
}
