package com.tickettoride.models;

import java.util.UUID;

public class Player {
    private User user = null;
    private Game game = null;
    private UUID playerID = null;

    public Player(User user, Game game, UUID playerID) {
        this.user = user;
        this.game = game;
        this.playerID = playerID;
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

    public String getPlayerID() {
        return playerID.toString();
    }

    public void setPlayerID(UUID playerID) {
        this.playerID = playerID;
    }
}
