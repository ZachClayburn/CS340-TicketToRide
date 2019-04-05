package com.tickettoride.models;

import com.tickettoride.models.idtypes.GameID;
import com.tickettoride.models.idtypes.PlayerID;
import com.tickettoride.models.idtypes.PlayerStateID;

import java.util.Map;

public class CompleteGameState extends PlayerState {
    public CompleteGameState(PlayerStateID playerStateID, PlayerID playerID, GameID gameID) {
        super(playerStateID, playerID, gameID);
        type = this.getClass().getName();
    }
    public CompleteGameState(PlayerID playerID, GameID gameID) {
        super(playerID, gameID);
        type = this.getClass().getName();
    }

    public CompleteGameState(Map<String, Object> playerStateMap) {
        super(playerStateMap);
        this.type = (String) playerStateMap.get("type");
    }
}
