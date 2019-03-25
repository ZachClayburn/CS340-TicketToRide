package com.tickettoride.models;

import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.List;

public class TrainCard {
    private Color color;

    public TrainCard(Color color){
        this.color = color;
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
}
