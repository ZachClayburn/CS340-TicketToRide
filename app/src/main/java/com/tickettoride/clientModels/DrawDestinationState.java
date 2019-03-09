package com.tickettoride.clientModels;

import com.tickettoride.activities.MapFragment;

public class DrawDestinationState extends PlayerState {
    public DrawDestinationState(MapFragment mf) { mf.onDrawDestination(); }

    @Override
    public void moveToNotTurnState(MapFragment mf) { DataManager.SINGLETON.setPlayerState(new NotTurnState(mf)); }

    @Override
    public void moveToPlayerTurnState(MapFragment mf) { DataManager.SINGLETON.setPlayerState(new NotTurnState(mf)); }
}
