package com.tickettoride.models.idtypes;

import java.util.UUID;


public class PlayerStateID extends AbstractID {
    protected PlayerStateID(UUID uuid) {
        super(uuid);
    }

    public static PlayerStateID randomUUID() {
        return new PlayerStateID(UUID.randomUUID());
    }

    public static PlayerStateID fromString(String string) { return new PlayerStateID(UUID.fromString(string)); }
}