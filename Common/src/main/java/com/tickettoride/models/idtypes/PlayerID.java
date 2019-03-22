package com.tickettoride.models.idtypes;

import java.util.UUID;

public class PlayerID extends AbstractID {

    protected PlayerID(UUID uuid) {
        super(uuid);
    }

    public static PlayerID randomUUID() {
        return new PlayerID(UUID.randomUUID());
    }

    public static PlayerID fromString(String string) {
        return new PlayerID(UUID.fromString(string));
    }
}
