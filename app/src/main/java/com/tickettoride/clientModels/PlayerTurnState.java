package com.tickettoride.clientModels;

import com.tickettoride.activities.MapFragment;

public class PlayerTurnState extends PlayerState {

    public PlayerTurnState(MapFragment mf) { mf.onTurnStart(); }

    @Override
    public void moveToDrawDestinationCardsState(MapFragment mf) { DataManager.SINGLETON.setPlayerState(new DrawDestinationState(mf)); }

    @Override
    public void moveToDrawTrainCardsState(MapFragment mf) { DataManager.SINGLETON.setPlayerState(new DrawTrainCardsState(mf)); }

    @Override
    public void moveToPlaceTrainsState(MapFragment mf) { DataManager.SINGLETON.setPlayerState(new ClaimRouteState(mf)); }
}
