package com.tickettoride.clientModels.helpers;

import com.tickettoride.activities.MapFragment;
import com.tickettoride.clientModels.DataManager;
import com.tickettoride.clientModels.FinalTurnState;
import com.tickettoride.clientModels.InitializeGameState;
import com.tickettoride.clientModels.NotTurnState;
import com.tickettoride.clientModels.PlayerTurnState;
import com.tickettoride.controllers.GameController;
import com.tickettoride.facadeProxies.GameFacadeProxy;

public class PlayerStateHelper {
    private static PlayerStateHelper SINGLETON = new PlayerStateHelper();
    public static PlayerStateHelper getSingleton() { return SINGLETON; }
    private PlayerStateHelper() {}

    public void determinePlayerState(MapFragment mapFragment) {
        if (DataManager.SINGLETON.getPlayerHand().getDestinationCards().size() == 0) {
            DataManager.SINGLETON.setPlayerState(new InitializeGameState(mapFragment));
        }
        else if (DataManager.getSINGLETON().getPlayer().getTurn() == DataManager.getSINGLETON().getTurn()) {
            if (DataManager.getSINGLETON().getPlayer().getTrainCarCount() < 3) {
                DataManager.SINGLETON.setPlayerState(new FinalTurnState(mapFragment)); return;
            }
            DataManager.SINGLETON.setPlayerState(new PlayerTurnState(mapFragment));
        }
        else { DataManager.SINGLETON.setPlayerState(new NotTurnState(mapFragment)); }
    }

    public void incrementTurnState(MapFragment mapFragment) {
        int currentTurn = DataManager.getSINGLETON().getTurn();
        if (currentTurn == DataManager.getSINGLETON().getPlayer().getTurn()) {
            if (DataManager.getSINGLETON().getPlayer().getTrainCarCount() < 3) {
                DataManager.getSINGLETON().getPlayerState().moveToFinalTurnState(mapFragment); return;
            }
            DataManager.getSINGLETON().getPlayerState().moveToPlayerTurnState(mapFragment);
        } else { DataManager.getSINGLETON().getPlayerState().moveToNotTurnState(mapFragment); }
    }
}
