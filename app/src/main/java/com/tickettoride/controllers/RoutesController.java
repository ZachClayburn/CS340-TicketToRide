package com.tickettoride.controllers;
import com.tickettoride.activities.GameRoomActivity;
import com.tickettoride.clientModels.DataManager;
import com.tickettoride.models.Player;
import com.tickettoride.models.Route;

public class RoutesController extends BaseController {
    private static final  RoutesController SINGLETON = new RoutesController();
    private RoutesController() {}

    public static RoutesController getSingleton() {
        return SINGLETON;
    }

    public void claim(Route route, Player player, Integer turn) {
        GameRoomActivity activity = (GameRoomActivity) getCurrentActivity();
        DataManager.SINGLETON.setRouteClaimed(route);
        int spaces = route.getSpaces();
        Player managedPlayer = DataManager.getSINGLETON().findPlayerByID(player.getPlayerID());
        int playerPoints = managedPlayer.getPoints();
        managedPlayer.setTrainCarCount(managedPlayer.getTrainCarCount() - route.getSpaces());
        managedPlayer.setPoints(playerPoints + spaces);
        //DataManager.SINGLETON.getPlayer().setPoints(playerPoints + spaces);
        //DataManager.SINGLETON.getPlayer().setTrainCarCount(DataManager.SINGLETON.getPlayer().getTrainCarCount() - route.getSpaces());
        if(isUserPlayer(player) ) { activity.onReturnToMap(); } else { activity.getMapFragment().drawExternal(); }
        DataManager.SINGLETON.setTurn(turn);
        activity.setTurn();
    }
}
