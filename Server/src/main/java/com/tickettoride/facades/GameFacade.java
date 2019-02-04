package com.tickettoride.facades;

import com.tickettoride.models.Game;
import java.util.UUID;

public class GameFacade {
    public void createGame(String name, int numPlayers) {
        // TODO: Database stuff, Send Response?
        String gameID = UUID.randomUUID().toString();
        Game game = new Game(gameID, name, numPlayers);
    }

    public void joinGame(String gameID){
        // TODO: Retrieve Game info from database
    }
}
