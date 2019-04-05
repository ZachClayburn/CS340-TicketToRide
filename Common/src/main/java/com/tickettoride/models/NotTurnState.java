package com.tickettoride.models;

import com.tickettoride.models.idtypes.GameID;
import com.tickettoride.models.idtypes.PlayerID;
import com.tickettoride.models.idtypes.PlayerStateID;

import java.util.Map;

public class NotTurnState extends PlayerState {

    public NotTurnState(PlayerStateID playerStateID, PlayerID playerID, GameID gameID) {
        super(playerStateID, playerID, gameID);
        type = this.getClass().getName();
    }
    public NotTurnState(PlayerID playerID, GameID gameID) {
        super(playerID, gameID);
        type = this.getClass().getName();
    }

    public NotTurnState(Map<String, Object> playerStateMap) {
        super(playerStateMap);
        this.type = (String) playerStateMap.get("type");
    }

    @Override
    public PlayerState moveToPlayerTurnState() { return new PlayerTurnState(playerStateID, playerID, gameID); }

    @Override
    public PlayerState moveToFinalTurnState() { return new FinalPlayerTurnState(playerStateID, playerID, gameID);}
}
