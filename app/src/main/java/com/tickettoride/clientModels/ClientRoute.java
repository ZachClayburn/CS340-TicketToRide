package com.tickettoride.clientModels;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.google.gson.internal.LinkedTreeMap;
import com.tickettoride.models.City;
import com.tickettoride.models.Color;
import com.tickettoride.models.Line;
import com.tickettoride.models.Player;
import com.tickettoride.models.PlayerColor;
import com.tickettoride.models.Route;
import com.tickettoride.models.idtypes.GameID;
import com.tickettoride.models.idtypes.PlayerID;
import com.tickettoride.models.idtypes.RouteID;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClientRoute extends Route {
    private static int xoffset=0;
    private static int yoffset=0;
    private static double scale=1.0;//not the same scale as in ClientLine

    public static void setXoffset(int newXoffset){
        xoffset=newXoffset;
    }

    public static void setYoffset(int yoffset) {
        ClientRoute.yoffset = yoffset;
    }
    
    public static void setScale(double scale){
        ClientRoute.scale = 1.0/scale;
    }

    public void resetLines() {for (Line line: lines) { ((ClientLine) line).resetLine(); }}

    private int convertLineColor(PlayerColor lineColor) {
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
        resetLines();
        Paint paint = new Paint();
        paint.setStrokeWidth(3);
        Player player = DataManager.getSINGLETON().findPlayerByID(getClaimedByPlayerID());
        int color;
        if (player != null) {
            color = convertLineColor(player.getColor());
        }
        else { color = android.graphics.Color.TRANSPARENT; }
        if (color == android.graphics.Color.TRANSPARENT) {
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(android.graphics.Color.BLACK);
        }
        else {
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(color);
        }
        for (Line line : lines) {
            ClientLine clientLine = (ClientLine) line;
            clientLine.drawPath(canvas, paint);
        }
    }


    public ClientRoute(Map<String, Object> clientRouteHash) {
        super();
        gameID = GameID.fromString((String) ((Map) clientRouteHash.get("gameID")).get("uuid"));
        routeID = RouteID.fromString((String) ((Map) clientRouteHash.get("routeID")).get("uuid"));
        if (clientRouteHash.get("claimedByPlayerID") != null) {
            claimedByPlayerID = PlayerID.fromString((String) ((Map) clientRouteHash.get("claimedByPlayerID")).get("uuid"));
        }
        List<String> stringCities =  (List) clientRouteHash.get("cities");
        List<City> newCities = new ArrayList<>();
        for (String stringCity : stringCities) { newCities.add(City.valueOf(stringCity)); }
        cities = newCities;
        List<LinkedTreeMap<String, Object>> linkedTreeLines = (List) clientRouteHash.get("lines");
        List<Line> newLines = new ArrayList<>();
        for (LinkedTreeMap<String, Object> linkedTreeLine : linkedTreeLines) { newLines.add(new ClientLine(linkedTreeLine)); }
        lines = newLines;
        color = Color.valueOf((String) clientRouteHash.get("color"));
        spaces = ((Double) clientRouteHash.get("spaces")).intValue();
    }

    public boolean contains(int x, int y) {
        int scaleX = (int)((x-xoffset)*scale);
        int scaleY = (int)((y-yoffset)*scale);
        for (Line line : lines) {
            ClientLine clientLine = (ClientLine) line;
            if (clientLine.contains(scaleX, scaleY)) {
                return true;
            }
        }
        return false;
    }
    public List<Line> getClientLines() {
        return lines;
    }

    public static ArrayList<ClientRoute> buildClientRoutes(ArrayList<LinkedTreeMap<String, Object>> clientRouteMap) {
        ArrayList<ClientRoute> clientRoutes = new ArrayList<>();
        for (Map<String, Object> singleClientRouteHash : clientRouteMap) {
            ClientRoute route = new ClientRoute(singleClientRouteHash);
            clientRoutes.add(route);
        }
        return clientRoutes;
    }

    public Route toBasicRoute() {
        return new Route(routeID, gameID, claimedByPlayerID, cities, color, spaces);
    }
}
