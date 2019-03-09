package com.tickettoride.clientModels;

import com.tickettoride.activities.MapFragment;

public class PlaceTrainsState extends PlayerState {
    PlaceTrainsState(MapFragment mf) {
//        mf.onPlaceTrains();
    }

    @Override
    public void moveToNotTurnState(MapFragment mf) {
        DataManager.SINGLETON.setPlayerState(new NotTurnState(mf));
    }
}
