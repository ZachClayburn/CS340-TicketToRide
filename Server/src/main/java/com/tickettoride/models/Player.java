package com.tickettoride.models;

import java.util.UUID;

public class Player {
    private User user = null;
    private Game game = null;
    private UUID playerID = null;

    public Player(User user, Game game) {
        this.user = user;
        this.game = game;
        this.playerID = UUID.randomUUID();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public UUID getPlayerID() {
        return playerID;
    }

    public void setPlayerID(UUID playerID) {
        this.playerID = playerID;
    }
}
