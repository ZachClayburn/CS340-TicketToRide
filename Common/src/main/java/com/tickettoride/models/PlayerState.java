package com.tickettoride.models;

import com.google.gson.internal.LinkedTreeMap;
import com.tickettoride.models.idtypes.GameID;
import com.tickettoride.models.idtypes.PlayerID;
import com.tickettoride.models.idtypes.PlayerStateID;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class PlayerState {

    protected PlayerStateID playerStateID;
    protected PlayerID playerID;
    protected GameID gameID;
    protected String type;

    public PlayerState(PlayerStateID playerStateID, PlayerID playerID, GameID gameID) {
        this.playerStateID = playerStateID;
        this.playerID = playerID;
        this.gameID = gameID;
    }

    public PlayerState(PlayerID playerID, GameID gameID) {
        this.playerID = playerID;
        this.playerStateID = PlayerStateID.randomUUID();
        this.gameID = gameID;
    }
    public PlayerState moveToNotTurnState() { return this; }
    public PlayerState moveToPlayerTurnState() { return this; }
    public PlayerState moveToDrawDestinationCardsState() { return this; }
    public PlayerState moveToDrawTrainCardsState() { return this; }
    public PlayerState moveToPlaceTrainsState() { return this; }
    public PlayerState moveToFinalTurnState() { return this; }

    public PlayerStateID getPlayerStateID() { return playerStateID; }
    public PlayerID getPlayerID() { return playerID; }
    public GameID getGameID() { return gameID; }

    public PlayerState(Map<String, Object> playerStateMap) {
        this.playerStateID = PlayerStateID.fromString((String) ((Map) playerStateMap.get("playerStateID")).get("uuid"));
        this.playerID = PlayerID.fromString((String) ((Map) playerStateMap.get("playerID")).get("uuid"));
        this.gameID = GameID.fromString((String) ((Map) playerStateMap.get("gameID")).get("uuid"));
    }

    public static List<PlayerState> buildPlayerStateList(ArrayList<LinkedTreeMap<String, Object>> playerStateMap) throws ClassNotFoundException,
            NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        List<PlayerState> playerStateList = new ArrayList<>();
        for (LinkedTreeMap<String, Object> playerStateHash : playerStateMap) {
            Class playerStateClass = Class.forName((String) playerStateHash.get("type"));
            Constructor playerStateClassConstructor = playerStateClass.getConstructor(Map.class);
            playerStateList.add((PlayerState) playerStateClassConstructor.newInstance(playerStateHash));
        }
        return playerStateList;
    }

}
