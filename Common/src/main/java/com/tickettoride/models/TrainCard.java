package com.tickettoride.models;

import modelAttributes.Color;

public class TrainCard {
    private Color color;

    public TrainCard(Color color){
        this.color = color;
    }

    public void setColor(Color color){this.color = color;}

    public Color getColor() {return color;}
}
