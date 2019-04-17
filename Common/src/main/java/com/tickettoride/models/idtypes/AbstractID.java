package com.tickettoride.models.idtypes;

import java.io.Serializable;
import java.util.UUID;

abstract class AbstractID implements Comparable<AbstractID>, Serializable {

    private UUID uuid;

    protected AbstractID(UUID uuid) {
        this.uuid = uuid;
    }


    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AbstractID)) return false;
        AbstractID id = (AbstractID) obj;
        return uuid.equals(id.uuid);
    }

    @Override
    public String toString() {
        return uuid.toString();
    }

    @Override
    public int compareTo(AbstractID o) {
        return uuid.compareTo(o.uuid);
    }
}
