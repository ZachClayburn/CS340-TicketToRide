package com.tickettoride.models.idtypes;

import java.util.UUID;

public class LineID extends AbstractID {
    protected LineID(UUID uuid) {
        super(uuid);
    }

    public static LineID randomUUID() {
        return new LineID(UUID.randomUUID());
    }


    public static LineID fromString(String string) {
        return new LineID(UUID.fromString(string));
    }
}
