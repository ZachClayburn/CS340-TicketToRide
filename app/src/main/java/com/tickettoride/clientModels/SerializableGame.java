package com.tickettoride.clientModels;

import com.tickettoride.models.Game;

import java.io.Serializable;
import java.util.UUID;

public class SerializableGame extends Game implements Serializable {
    public SerializableGame(String groupName, int maxPlayer) {
        super(groupName, maxPlayer);
    }

    public SerializableGame(UUID gameID, String groupName, int numPlayer, int maxPlayer, boolean isStarted, int curTurn) {
        super(gameID, groupName, numPlayer, maxPlayer, isStarted, curTurn);
    }
}
