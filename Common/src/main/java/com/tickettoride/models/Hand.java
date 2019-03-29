package com.tickettoride.models;

import java.util.ArrayList;
import java.util.List;

import static com.tickettoride.models.Color.BLACK;
import static com.tickettoride.models.Color.BLUE;
import static com.tickettoride.models.Color.GREEN;
import static com.tickettoride.models.Color.ORANGE;
import static com.tickettoride.models.Color.PURPLE;
import static com.tickettoride.models.Color.RED;
import static com.tickettoride.models.Color.WHITE;
import static com.tickettoride.models.Color.YELLOW;

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
    public int getColor(Color color) {
        switch(color) {
            case YELLOW: return getYellow();
            case RED: return getRed();
            case BLUE: return getBlue();
            case PURPLE: return getPurple();
            case BLACK: return getBlack();
            case GREEN: return getGreen();
            case WHITE: return getWhite();
            case ORANGE: return getOrange();
        }
        return -1;
    }


    public void discardBlue(int amount) { blue = blue - amount; }
    public void discardGreen(int amount) {green = green - amount; }
    public void discardPurple(int amount) { purple = purple - amount; }
    public void discardRed(int amount) { red = red - amount; }
    public void discardOrange(int amount) { orange = orange - amount; }
    public void discardYellow(int amount) { yellow = yellow - amount; }
    public void discardBlack(int amount) { black = black - amount; }
    public void discardWhite(int amount) { white = white - amount; }
    public void discardLocomotive(int amount) { locomotive = locomotive - amount; }

    public void replaceBlue(int amount) { blue = amount; }
    public void replaceGreen(int amount) {green = amount; }
    public void replacePurple(int amount) { purple = amount; }
    public void replaceRed(int amount) { red = amount; }
    public void replaceOrange(int amount) { orange = amount; }
    public void replaceYellow(int amount) { yellow = amount; }
    public void replaceBlack(int amount) { black = amount; }
    public void replaceWhite(int amount) { white = amount; }
    public void replaceLocomotive(int amount) { locomotive =amount; }


    public int getHandSize(){
        return blue + green + purple + red + orange + yellow + black + white + locomotive;
    }

    public List<DestinationCard> getDestinationCards() {return destinationCards;}

    public void setDestinationCards(List<DestinationCard> destinationCards) { this.destinationCards = destinationCards; }

    public void addCard(TrainCard card){
        switch(card.getColor()){
            case RED: setRed(1); return;
            case BLUE: setBlue(1); return;
            case YELLOW: setYellow(1); return;
            case PURPLE: setPurple(1); return;
            case GREEN: setGreen(1); return;
            case ORANGE: setOrange(1); return;
            case BLACK: setBlack(1); return;
            case WHITE: setWhite(1); return;
            case WILD: setLocomotive(1);return;
        }
    }

    public void replace(Hand tempHand){
        replaceBlue(tempHand.getBlue());
        replaceGreen(tempHand.getGreen());
        replacePurple(tempHand.getPurple());
        replaceRed(tempHand.getRed());
        replaceOrange(tempHand.getOrange());
        replaceYellow(tempHand.getYellow());
        replaceBlack(tempHand.getBlack());
        replaceWhite(tempHand.getWhite());
        replaceLocomotive(tempHand.getLocomotive());
    }
}
