package com.tickettoride.models.idtypes;

import com.tickettoride.models.Game;

import java.util.UUID;

public class GameID extends AbstractID {

    protected GameID(UUID uuid) {
        super(uuid);
    }

    public static GameID randomUUID() {
        return new GameID(UUID.randomUUID());
    }


    public static GameID fromString(String string) {
        return new GameID(UUID.fromString(string));
    }
}
