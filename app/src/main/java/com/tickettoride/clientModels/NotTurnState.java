package com.tickettoride.clientModels;

import com.tickettoride.activities.MapFragment;

public class NotTurnState extends PlayerState {

    public NotTurnState(MapFragment mf) { mf.onNotTurnStart(); }

    @Override
    public void moveToPlayerTurnState(MapFragment mf) { DataManager.SINGLETON.setPlayerState(new PlayerTurnState(mf)); }

    @Override
    public void applyState(MapFragment mf) { mf.onNotTurnStart(); }
}
