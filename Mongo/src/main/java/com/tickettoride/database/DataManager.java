package com.tickettoride.database;

import com.tickettoride.models.DestinationCard;
import com.tickettoride.models.Game;
import com.tickettoride.models.Line;
import com.tickettoride.models.Player;
import com.tickettoride.models.PlayerState;
import com.tickettoride.models.Route;
import com.tickettoride.models.Session;
import com.tickettoride.models.TrainCard;
import com.tickettoride.models.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataManager {
    private static List<DestinationCard> destinationCardList = Collections.synchronizedList(new ArrayList<>());
    private static List<Game> gameList = Collections.synchronizedList(new ArrayList<>());
    private static List<Line> lineList = Collections.synchronizedList(new ArrayList<>());
    private static List<Player> playerList = Collections.synchronizedList(new ArrayList<>());
    private static List<PlayerState> playerStateList = Collections.synchronizedList(new ArrayList<>());
    private static List<Route> routeList = Collections.synchronizedList(new ArrayList<>());
    private static List<Session> sessionList = Collections.synchronizedList(new ArrayList<>());
    private static List<TrainCard> trainCardList = Collections.synchronizedList(new ArrayList<>());
    private static List<User> userList = Collections.synchronizedList(new ArrayList<>());
    private static boolean dataInitialized = false;
    public static boolean getDataInitialized() { return dataInitialized; }
    public static void setDataInitialized(boolean newadtaInitialized) { dataInitialized = newadtaInitialized; }

    public static List<DestinationCard> getDestinationCardList() { return destinationCardList; }
    public static List<Game> getGameList() { return gameList; }
    public static List<Line> getLineList() { return lineList; }
    public static List<Player> getPlayerList() { return playerList; }
    public static List<PlayerState> getPlayerStateList() { return playerStateList; }
    public static List<Route> getRouteList() { return routeList; }
    public static List<Session> getSessionList() { return sessionList; }
    public static List<TrainCard> getTrainCardList() { return trainCardList; }
    public static List<User> getUserList() { return userList; }

}
