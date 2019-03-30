package com.tickettoride.clientModels;

import com.tickettoride.activities.MapFragment;

public class DrawTrainCardsState extends PlayerState {
    public DrawTrainCardsState(MapFragment mf) { mf.onDrawTrainCards(); }

    @Override
    public void moveToNotTurnState(MapFragment mf) { DataManager.SINGLETON.setPlayerState(new NotTurnState(mf));}
}
