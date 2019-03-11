package com.tickettoride.clientModels;

import com.tickettoride.activities.MapFragment;

public class ClaimRouteState extends PlayerState {
    public ClaimRouteState(MapFragment mf) { mf.onClaimRoute(); }

    @Override
    public void moveToNotTurnState(MapFragment mf) { DataManager.SINGLETON.setPlayerState(new NotTurnState(mf)); }

    @Override
    public void applyState(MapFragment mf) { mf.onNotTurnStart(); }
}
