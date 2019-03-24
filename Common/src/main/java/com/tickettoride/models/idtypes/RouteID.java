package com.tickettoride.models.idtypes;

import java.util.UUID;

public class RouteID extends AbstractID {
    protected RouteID(UUID uuid) {
        super(uuid);
    }

    public static RouteID randomUUID() {
        return new RouteID(UUID.randomUUID());
    }


    public static RouteID fromString(String string) {
        return new RouteID(UUID.fromString(string));
    }
}
