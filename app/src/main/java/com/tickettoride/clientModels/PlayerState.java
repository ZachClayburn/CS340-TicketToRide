package com.tickettoride.clientModels;

import android.util.Log;

import com.tickettoride.activities.MapFragment;

public abstract class PlayerState {
    public void moveToInitializeGameState(MapFragment mf) { Log.i("PlayerState", "Cannot Move to InitializeGameState"); }
    public void moveToNotTurnState(MapFragment mf) { Log.i("PlayerState", "Cannot Move to NotTurnState"); }
    public void moveToPlayerTurnState(MapFragment mf) { Log.i("PlayerState", "Cannot Move to PlayerTurnState"); }
    public void moveToDrawDestinationCardsState(MapFragment mf) { Log.i("PlayerState", "Cannot Move to DrawDestinationCardsState"); }
    public void moveToDrawTrainCardsState(MapFragment mf) { Log.i("PlayerState", "Cannot Move to DrawTrainCardsState"); }
    public void moveToPlaceTrainsState(MapFragment mf) { Log.i("PlayerState", "Cannot Move to PlaceTrainsState"); }
    public void applyState(MapFragment mf) {}
}
