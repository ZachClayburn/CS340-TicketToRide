package com.tickettoride.clientModels;

import com.tickettoride.activities.MapFragment;
import com.tickettoride.facadeProxies.GameFacadeProxy;

public class FinalDrawTrainCardsState extends PlayerState {

    public FinalDrawTrainCardsState(MapFragment mf) { mf.onDrawTrainCards(); }

    @Override
    public void moveToNotTurnState(MapFragment mf) { GameFacadeProxy.SINGLETON.finish(DataManager.getSINGLETON().getGame());}
}
