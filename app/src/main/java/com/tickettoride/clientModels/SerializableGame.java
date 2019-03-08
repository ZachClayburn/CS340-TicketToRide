package com.tickettoride.clientModels;

import com.tickettoride.models.Game;

import java.io.Serializable;
import java.util.UUID;

public class SerializableGame extends Game implements Serializable {
    public SerializableGame(Game game){
        super(game.getGameID(), game.getGroupName(), game.getNumPlayer(), game.getMaxPlayer(), game.IsStarted(), game.getCurTurn());
    }
}
