package com.tickettoride.clientModels;

import com.tickettoride.activities.MapFragment;
import com.tickettoride.facadeProxies.GameFacadeProxy;

public class FinalPlaceTrainsState extends PlayerState {
    public FinalPlaceTrainsState(MapFragment mf) { mf.onClaimRoute(); }

    @Override
    public void moveToNotTurnState(MapFragment mf) { GameFacadeProxy.SINGLETON.finish(DataManager.getSINGLETON().getGame()); }
}
