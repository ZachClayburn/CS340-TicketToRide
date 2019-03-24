package com.tickettoride.models;
import com.tickettoride.models.idtypes.LineID;
import com.tickettoride.models.idtypes.RouteID;
import java.util.Map;

public class Line implements Cloneable  {
    protected int startX;
    protected int startY;
    protected int endX;
    protected int endY;
    protected LineID lineID;
    protected RouteID routeID;

    public Line(int startX, int startY, int endX, int endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.lineID = LineID.randomUUID();
    }

    public Line(LineID lineID, RouteID routeID, int startX, int startY, int endX, int endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.lineID = lineID;
        this.routeID = routeID;
    }

    public Line(Map<String, Object> lineHash) {
        lineID = LineID.fromString((String) ((Map) lineHash.get("lineID")).get("uuid"));
        routeID = RouteID.fromString((String) ((Map) lineHash.get("routeID")).get("uuid"));
        startX = ((Double) lineHash.get("startX")).intValue();
        startY = ((Double) lineHash.get("startY")).intValue();
        endX = ((Double) lineHash.get("endX")).intValue();
        endY = ((Double) lineHash.get("endY")).intValue();
    }

    public Line cloning() throws CloneNotSupportedException {
        Line clonedLine = (Line) this.clone();
        clonedLine.setLineId(LineID.randomUUID());
        return clonedLine;
    }

    public Line() {}

    public LineID getLineID() { return lineID; }

    public int getEndX() { return endX; }

    public int getEndY() { return endY; }

    public int getStartX() { return startX; }

    public int getStartY() { return startY; }

    public void setRouteID(RouteID routeID) { this.routeID = routeID; }

    public RouteID getRouteID() { return this.routeID; }

    public void setLineId(LineID lineId) { this.lineID = lineId; }
}
