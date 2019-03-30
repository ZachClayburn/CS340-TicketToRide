package com.tickettoride.models;

import com.google.gson.internal.LinkedTreeMap;
import com.tickettoride.models.idtypes.GameID;
import com.tickettoride.models.idtypes.PlayerID;
import com.tickettoride.models.idtypes.RouteID;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Route implements Cloneable {
    protected Color color;
    protected int spaces;
    protected int points;
    protected List<City> cities;
    protected PlayerID claimedByPlayerID;
    protected GameID gameID;
    protected RouteID routeID;
    protected List<Line> lines;

    public Route( List<Line> lines, Color color, int spaces, List<City> cities) {
        this.color = color;
        this.spaces = spaces;
        setPoints();
        this.cities = cities;
        this.routeID = RouteID.randomUUID();
        this.lines = lines;
    }

    public Route(RouteID routeID, GameID gameID, PlayerID claimedByPlayerID, List<City> cities, Color color, int spaces) {
        this.routeID = routeID;
        this.gameID = gameID;
        this.claimedByPlayerID = claimedByPlayerID;
        this.cities = cities;
        this.color = color;
        this.spaces = spaces;
        setPoints();
    }

    public Route() {}

    public Route cloning() throws CloneNotSupportedException {
        Route clonedRoute = (Route) this.clone();
        clonedRoute.setRouteID(RouteID.randomUUID());
        List<Line> newLines = new ArrayList<>();
        for (Line line : lines) { newLines.add(line.cloning()); }
        lines = newLines;
        return clonedRoute;
    }

    public void claimedByPlayerID(PlayerID claimedByPlayerID) { this.claimedByPlayerID = claimedByPlayerID; }
    public PlayerID getClaimedByPlayerID() {
        return claimedByPlayerID;
    }
    public Color getColor() {
        return color;
    }
    public int getSpaces() {
        return spaces;
    }
    public int getPoints(){
        return points;
    }
    private void setPoints(){
        switch(spaces){
            case 1:
                points=1;
                return;
            case 2:
                points=2;
                return;
            case 3:
                points=4;
                return;
            case 4:
                points=7;
                return;
            case 5:
                points=10;
                return;
            case 6:
                points=15;
                return;
            default:
                points=0;
        }
    }
    public void setGameID(GameID gameID) { this.gameID = gameID; }
    public GameID getGameID() { return this.gameID; }
    public RouteID getRouteID() { return this.routeID; }
    public void setRouteID(RouteID routeID) { this.routeID = routeID; }
    public List<City> getCities() { return this.cities; }
    public List<Line> getLines() { return this.lines; }
    public void setLines(List<Line> lines) { this.lines = lines; }
    public Boolean getIsClaimed() { return claimedByPlayerID != null; }
    public void setClaimedByPlayerID(PlayerID playerID) { this.claimedByPlayerID = playerID; }
}
