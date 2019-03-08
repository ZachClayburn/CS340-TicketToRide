package com.tickettoride.models;

import java.util.Objects;

public class Username {
    private String username = "";

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public Username(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Username)) return false;
        Username username1 = (Username) o;
        return Objects.equals(getUsername(), username1.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername());
    }
}
