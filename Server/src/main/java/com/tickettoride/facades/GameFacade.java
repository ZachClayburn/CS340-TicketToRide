package com.tickettoride.facades;

import com.tickettoride.models.Game;
import java.util.UUID;

public class GameFacade {
    public void create(String name, int numPlayers) {
        // TODO: Database stuff, Send Response?
        String gameID = UUID.randomUUID().toString();
        Game game = new Game(gameID, name, numPlayers);
    }

    public void join(String gameID){
        // TODO: Retrieve Game info from database
    }
}
