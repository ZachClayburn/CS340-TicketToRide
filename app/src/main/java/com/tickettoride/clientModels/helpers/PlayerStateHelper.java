package com.tickettoride.clientModels.helpers;

import com.tickettoride.activities.MapFragment;
import com.tickettoride.clientModels.DataManager;
import com.tickettoride.models.ClaimRouteState;
import com.tickettoride.models.DrawDestinationState;
import com.tickettoride.models.DrawTrainCardsState;
import com.tickettoride.models.FinalDrawDestinationCardState;
import com.tickettoride.models.FinalDrawTrainCardsState;
import com.tickettoride.models.FinalPlaceTrainsState;
import com.tickettoride.models.FinalPlayerTurnState;
import com.tickettoride.models.InitializeGameState;
import com.tickettoride.models.NotTurnState;
import com.tickettoride.models.PlayerState;
import com.tickettoride.models.PlayerTurnState;

import java.util.List;

public class PlayerStateHelper {
    private static PlayerStateHelper SINGLETON = new PlayerStateHelper();
    public static PlayerStateHelper getSingleton() { return SINGLETON; }
    private PlayerStateHelper() {}

    public void applyPlayerState(PlayerState playerState, MapFragment mf) {
        if (playerState instanceof FinalDrawDestinationCardState) { mf.onDrawDestination();}
        else if (playerState instanceof ClaimRouteState) { mf.onClaimRoute(); }
        else if (playerState instanceof DrawDestinationState) { mf.onDrawDestination(); }
        else if (playerState instanceof DrawTrainCardsState) { mf.onDrawTrainCards(); }
        else if (playerState instanceof FinalDrawTrainCardsState) { mf.onDrawTrainCards(); }
        else if (playerState instanceof FinalPlaceTrainsState) { mf.onClaimRoute(); }
        else if (playerState instanceof InitializeGameState) { mf.onInitializeTurn(); }
        else if (playerState instanceof NotTurnState) { mf.onNotTurnStart(); }
        else if (playerState instanceof PlayerTurnState) { mf.onTurnStart(); }
        else if (playerState instanceof FinalPlayerTurnState) { mf.onTurnStart(); }
    }
}
