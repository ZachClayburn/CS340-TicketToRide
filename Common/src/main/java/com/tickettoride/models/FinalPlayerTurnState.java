package com.tickettoride.models;

import com.tickettoride.models.idtypes.GameID;
import com.tickettoride.models.idtypes.PlayerID;
import com.tickettoride.models.idtypes.PlayerStateID;

import java.util.Map;

public class FinalPlayerTurnState extends PlayerState {

    public FinalPlayerTurnState(PlayerStateID playerStateID, PlayerID playerID, GameID gameID) {
        super(playerStateID, playerID, gameID);
        type = this.getClass().getName();
    }
    public FinalPlayerTurnState(PlayerID playerID, GameID gameID) {
        super(playerID, gameID);
        type = this.getClass().getName();
    }

    public FinalPlayerTurnState(Map<String, Object> playerStateMap) {
        super(playerStateMap);
        this.type = (String) playerStateMap.get("type");
    }

    @Override
    public PlayerState moveToDrawDestinationCardsState() { return new FinalDrawDestinationCardState(playerStateID, playerID, gameID); }

    @Override
    public PlayerState moveToDrawTrainCardsState() { return new FinalDrawTrainCardsState(playerStateID, playerID, gameID); }

    @Override
    public PlayerState moveToPlaceTrainsState() { return new FinalPlaceTrainsState(playerStateID, playerID, gameID); }
}
