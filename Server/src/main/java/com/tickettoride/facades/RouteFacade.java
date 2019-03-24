package com.tickettoride.facades;

import com.tickettoride.facades.helpers.GameFacadeHelper;
import com.tickettoride.facades.helpers.RouteHelper;
import com.tickettoride.models.Game;
import com.tickettoride.models.Player;
import com.tickettoride.models.Route;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

import command.Command;

public class RouteFacade extends BaseFacade {

    public static final String CONTROLLER_NAME = "RoutesController";
    private static RouteFacade SINGLETON = new RouteFacade();

    public static RouteFacade getSingleton() {
        return SINGLETON;
    }
    private static Logger logger = LogManager.getLogger(RouteFacade.class.getName());

    public void claim(UUID connID, Route route, Player player) {
        try {
            route.setClaimedByPlayerID(player.getPlayerID());
            RouteHelper.getSingleton().updateRoute(route);
            Game game = GameFacadeHelper.getSingleton().findGame(route.getGameID());
            game = GameFacadeHelper.getSingleton().updateGameTurn(game);
            Command command = new Command(CONTROLLER_NAME, "claim", route, player, game.getCurTurn());
            sendResponseToRoom(connID, command);
        } catch (Throwable t) { logger.error(t.getMessage(), t); }
    }
}
