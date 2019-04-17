package com.tickettoride.models;

import com.google.gson.internal.LinkedTreeMap;
import com.tickettoride.models.idtypes.GameID;
import com.tickettoride.models.idtypes.PlayerID;

import java.util.ArrayList;
import java.util.List;

public class TrainCard {
    private Color color;
    // Only used for Mongo
    private GameID gameID = null;
    private int sequencePosition = 0;
    private CardState state = CardState.IN_DECK;
    private PlayerID playerID = null;

    public TrainCard(Color color){
        this.color = color;
    }

    public TrainCard(Color color, GameID gameID, int sequencePosition, CardState state, PlayerID playerID) {
        this.color = color;
        this.gameID = gameID;
        this.sequencePosition = sequencePosition;
        this.state = state;
        this.playerID = playerID;
    }

    public void setColor(Color color){this.color = color;}

    public Color getColor() {return color;}

    public static List<TrainCard> unGsonCards(List<LinkedTreeMap> gsonCards){
        List<TrainCard> cards = new ArrayList<>();
        for (LinkedTreeMap gsonCard : gsonCards) {
            Color color = Color.valueOf((String)gsonCard.get("color"));
            cards.add(new TrainCard(color));
        }
        return cards;
    }

    public GameID getGameID() {
        return gameID;
    }

    public void setGameID(GameID gameID) {
        this.gameID = gameID;
    }

    public int getSequencePosition() {
        return sequencePosition;
    }

    public void setSequencePosition(int sequencePosition) {
        this.sequencePosition = sequencePosition;
    }

    public CardState getState() {
        return state;
    }

    public void setState(CardState state) {
        this.state = state;
    }

    public PlayerID getPlayerID() {
        return playerID;
    }

    public void setPlayerID(PlayerID playerID) {
        this.playerID = playerID;
    }
}
