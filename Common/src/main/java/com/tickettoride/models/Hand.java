package com.tickettoride.models;

import java.util.ArrayList;
import java.util.List;

public class Hand {
    private List<DestinationCard> destinationCards = new ArrayList<>();
    private int blue = 0;
    private int green = 0;
    private int purple = 0;
    private int red = 0;
    private int orange = 0;
    private int yellow = 0;
    private int black = 0;
    private int white = 0;
    private int locomotive = 0;

    public Hand(){}

    public void setBlue(int amount) {
        blue = blue + amount;
    }
    public int getBlue() {
        return blue;
    }
    public void setGreen(int amount) {
        green = green + amount;
    }
    public int getGreen() {
        return green;
    }
    public void setPurple(int amount) {
        purple = purple + amount;
    }
    public int getPurple() {
        return purple;
    }
    public void setRed(int amount) {
        red = red + amount;
    }
    public int getRed() {
        return red;
    }
    public void setOrange(int amount) {
        orange = orange + amount;
    }
    public int getOrange() {
        return orange;
    }
    public void setYellow(int amount) {
        yellow = yellow + amount;
    }
    public int getYellow() {
        return yellow;
    }
    public void setBlack(int amount) {
        black = black + amount;
    }
    public int getBlack() {
        return black;
    }
    public void setWhite(int amount) {
        white = white + amount;
    }
    public int getWhite() {
        return white;
    }
    public void setLocomotive(int amount) {
        locomotive = locomotive + amount;
    }
    public int getLocomotive() {
        return locomotive;
    }

    public int getHandSize(){
        return blue + green + purple + red + orange + yellow + black + white + locomotive;
    }

    public List<DestinationCard> getDestinationCards() {
        return destinationCards;
    }
}
