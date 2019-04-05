package com.tickettoride.models;

import com.tickettoride.models.idtypes.GameID;
import com.tickettoride.models.idtypes.PlayerID;
import com.tickettoride.models.idtypes.PlayerStateID;

import java.util.Map;

public class PlayerTurnState extends PlayerState {

    public PlayerTurnState(PlayerStateID playerStateID, PlayerID playerID, GameID gameID) {
        super(playerStateID, playerID, gameID);
        type = this.getClass().getName();
    }
    public PlayerTurnState(PlayerID playerID, GameID gameID) {
        super(playerID, gameID);
        type = this.getClass().getName();
    }

    public PlayerTurnState(Map<String, Object> playerStateMap) {
        super(playerStateMap);
        this.type = (String) playerStateMap.get("type");
    }

    @Override
    public PlayerState moveToDrawDestinationCardsState() { return new DrawDestinationState(playerStateID, playerID, gameID); }

    @Override
    public PlayerState moveToDrawTrainCardsState() { return new DrawTrainCardsState(playerStateID, playerID, gameID); }

    @Override
    public PlayerState moveToPlaceTrainsState() { return new ClaimRouteState(playerStateID, playerID, gameID); }
}
