package com.tickettoride.clientModels;

import com.tickettoride.activities.MapFragment;
import com.tickettoride.facadeProxies.GameFacadeProxy;

public class FinalDrawDestinationCardState extends PlayerState {

    public FinalDrawDestinationCardState(MapFragment mf) { mf.onDrawDestination(); }

    @Override
    public void moveToNotTurnState(MapFragment mf) { GameFacadeProxy.SINGLETON.finish(DataManager.getSINGLETON().getGame()); }
}
