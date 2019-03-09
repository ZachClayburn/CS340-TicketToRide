package com.tickettoride.clientModels;

import com.tickettoride.activities.MapFragment;

public class InitializeGameState extends PlayerState {

    public InitializeGameState(MapFragment mf) { mf.onInitializeTurn(); }

    @Override
    public void moveToPlayerTurnState(MapFragment mf) { DataManager.SINGLETON.setPlayerState(new PlayerTurnState(mf)); }

    @Override
    public void moveToNotTurnState(MapFragment mf) { DataManager.SINGLETON.setPlayerState(new NotTurnState(mf)); }

    @Override
    public void applyState(MapFragment mf) { mf.onInitializeTurn(); }
}
