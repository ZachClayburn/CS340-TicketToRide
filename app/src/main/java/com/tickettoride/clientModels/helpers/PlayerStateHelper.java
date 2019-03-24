package com.tickettoride.clientModels.helpers;

import com.tickettoride.activities.MapFragment;
import com.tickettoride.clientModels.DataManager;
import com.tickettoride.clientModels.InitializeGameState;
import com.tickettoride.clientModels.NotTurnState;
import com.tickettoride.clientModels.PlayerTurnState;

public class PlayerStateHelper {
    private static PlayerStateHelper SINGLETON = new PlayerStateHelper();
    public static PlayerStateHelper getSingleton() { return SINGLETON; }
    private PlayerStateHelper() {}

    public void determinePlayerState(MapFragment mapFragment) {
        if (DataManager.SINGLETON.getPlayerState() == null) {
            if (DataManager.SINGLETON.getPlayerHand().getDestinationCards().size() == 0) { DataManager.SINGLETON.setPlayerState(new InitializeGameState(mapFragment)); }
            else if (DataManager.getSINGLETON().getPlayer().getTurn() == DataManager.getSINGLETON().getTurn()) { DataManager.SINGLETON.setPlayerState(new PlayerTurnState(mapFragment)); }
            else { DataManager.SINGLETON.setPlayerState(new NotTurnState(mapFragment)); }
        } else { DataManager.SINGLETON.getPlayerState().applyState(mapFragment); }
    }
}
