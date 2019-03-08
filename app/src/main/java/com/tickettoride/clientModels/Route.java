package com.tickettoride.clientModels;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.tickettoride.models.City;

import java.util.List;

import modelAttributes.Color;

import static modelAttributes.Color.BLACK;
import static modelAttributes.Color.BLUE;

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
    private int convertLineColor(Color lineColor) {
        switch(lineColor) {
            case BLUE:
                return android.graphics.Color.BLUE;
            case GREEN:
                return android.graphics.Color.GREEN;
            case BLACK:
                return android.graphics.Color.BLACK;
            case RED:
                return android.graphics.Color.RED;
            case YELLOW:
                return android.graphics.Color.YELLOW;
            default:
                return android.graphics.Color.TRANSPARENT;
        }
    }
    public void drawRoute(Canvas canvas) {
        Paint paint = new Paint();
        int color = convertLineColor(lineColor);
        if (color == android.graphics.Color.TRANSPARENT) {
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(android.graphics.Color.BLACK);
        }
        else {
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(color);
        }
        for (Line line : lines) {
            line.drawPath(canvas, paint);
        }
    }
    public boolean contains(int x, int y) {
        for (Line line : lines) {
            if (line.contains(x, y)) {
                return true;
            }
        }
        return false;
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
