package com.tickettoride.models;

import com.tickettoride.models.idtypes.GameID;
import com.tickettoride.models.idtypes.PlayerID;
import com.tickettoride.models.idtypes.PlayerStateID;

import java.util.Map;

public class FinalPlaceTrainsState extends PlayerState {
    public FinalPlaceTrainsState(PlayerStateID playerStateID, PlayerID playerID, GameID gameID) {
        super(playerStateID, playerID, gameID);
        type = this.getClass().getName();
    }
    public FinalPlaceTrainsState(PlayerID playerID, GameID gameID) {
        super(playerID, gameID);
        type = this.getClass().getName();
    }

    public FinalPlaceTrainsState(Map<String, Object> playerStateMap) {
        super(playerStateMap);
        this.type = (String) playerStateMap.get("type");
    }

    @Override
    public PlayerState moveToNotTurnState() { return new CompleteGameState(playerStateID, playerID, gameID); }
}
