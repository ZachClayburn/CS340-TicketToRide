package com.tickettoride.clientModels;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.tickettoride.models.City;
import com.tickettoride.models.Color;
import com.tickettoride.models.PlayerColor;

import java.util.List;

public class Route {
    private List<Line> lines;
    private Color color;
    private int spaces;
    private City firstCity;
    private City secondCity;
    private boolean isClaimed;
    private PlayerColor lineColor;
    private static int xoffset=0;
    private static int yoffset=0;
    private static double scale=1.0;//not the same scale as in Line
    
    public Route(List<Line> lines, Color color, int spaces, City firstCity, City secondCity, boolean isClaimed, PlayerColor lineColor) {
        this.lines = lines;
        this.color = color;
        this.spaces = spaces;
        this.firstCity = firstCity;
        this.secondCity = secondCity;
        this.isClaimed = isClaimed;
        this.lineColor = lineColor;
    }
    
    public static void setXoffset(int newXoffset){
        xoffset=newXoffset;
    }

    public static void setYoffset(int yoffset) {
        Route.yoffset = yoffset;
    }
    
    public static void setScale(double scale){
        Route.scale = 1.0/scale;
    }

    private int convertLineColor(PlayerColor lineColor) {
        if (lineColor == null) { return android.graphics.Color.TRANSPARENT; }
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
        paint.setStrokeWidth(3);
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
        int scaleX = (int)((x-xoffset)*scale);
        int scaleY = (int)((y-yoffset)*scale);
        for (Line line : lines) {
            if (line.contains(scaleX, scaleY)) {
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
    public void setLineColor(PlayerColor lineColor) {
        this.lineColor = lineColor;
    }
    public PlayerColor getPlayer() {
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
