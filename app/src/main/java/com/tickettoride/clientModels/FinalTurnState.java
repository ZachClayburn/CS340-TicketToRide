package com.tickettoride.clientModels;

import com.tickettoride.activities.MapFragment;

public class FinalTurnState extends PlayerState {

    public FinalTurnState(MapFragment mf) { mf.onTurnStart(); }

    @Override
    public void moveToDrawDestinationCardsState(MapFragment mf) { DataManager.SINGLETON.setPlayerState(new FinalDrawDestinationCardState(mf)); }

    @Override
    public void moveToDrawTrainCardsState(MapFragment mf) { DataManager.SINGLETON.setPlayerState(new FinalDrawTrainCardsState(mf)); }

    @Override
    public void moveToPlaceTrainsState(MapFragment mf) { DataManager.SINGLETON.setPlayerState(new FinalPlaceTrainsState(mf)); }
}
