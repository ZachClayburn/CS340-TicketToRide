package com.tickettoride.models.idtypes;

import java.util.UUID;

public class SessionID extends AbstractID {
    protected SessionID(UUID uuid) {
        super(uuid);
    }

    public static SessionID randomUUID() {
        return new SessionID(UUID.randomUUID());
    }

    public static SessionID fromString(String string) {
        return new SessionID(UUID.fromString(string));
    }
}
