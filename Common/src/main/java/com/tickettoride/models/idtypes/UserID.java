package com.tickettoride.models.idtypes;

import java.util.UUID;

public class UserID extends AbstractID {
    protected UserID(UUID uuid) {
        super(uuid);
    }

    public static UserID fromString(String string) {
        return new UserID(UUID.fromString(string));
    }

    public static UserID randomUUID() {
        return new UserID(UUID.randomUUID());
    }
}
