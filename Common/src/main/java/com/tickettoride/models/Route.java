package com.tickettoride.models;

import java.util.List;

import modelAttributes.Color;

public class Route {
    private List<Line> lines;
    private Color color;
    private int spaces;
    private City firstCity;
    private City secondCity;
    private boolean isClaimed;
    private Color lineColor;
    public Route(List<Line> lines, Color color, int spaces, City firstCity, City secondCity, boolean isClaimed, Color lineColor) {
        this.lines = lines;
        this.color = color;
        this.spaces = spaces;
        this.firstCity = firstCity;
        this.secondCity = secondCity;
        this.isClaimed = isClaimed;
        this.lineColor = lineColor;
    }
    public List<Line> getLines() {
        return lines;
    }
    public void setIsClaimed(boolean isClaimed) {
        this.isClaimed = isClaimed;
    }
    public boolean getIsClaimed() {
        return isClaimed;
    }
    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }
    public Color getPlayer() {
        return lineColor;
    }
    public City getFirstCity() {
        return firstCity;
    }
    public City getSecondCity() {
        return secondCity;
    }
    public Color getColor() {
        return color;
    }
    public int getSpaces() {
        return spaces;
    }
}
