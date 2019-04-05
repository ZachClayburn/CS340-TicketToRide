package com.tickettoride.models;

import com.tickettoride.models.idtypes.GameID;
import com.tickettoride.models.idtypes.PlayerID;
import com.tickettoride.models.idtypes.PlayerStateID;

import java.util.Map;

public class InitializeGameState extends PlayerState {

    public InitializeGameState(PlayerStateID playerStateID, PlayerID playerID, GameID gameID) {
        super(playerStateID, playerID, gameID);
        type = this.getClass().getName();
    }
    public InitializeGameState(PlayerID playerID, GameID gameID) {
        super(playerID, gameID);
        type = this.getClass().getName();
    }

    public InitializeGameState(Map<String, Object> playerStateMap) {
        super(playerStateMap);
        this.type = (String) playerStateMap.get("type");
    }

    @Override
    public PlayerState moveToPlayerTurnState() { return new PlayerTurnState(playerStateID, playerID, gameID); }

    @Override
    public PlayerState moveToNotTurnState() { return new NotTurnState(playerStateID, playerID, gameID); }
}
