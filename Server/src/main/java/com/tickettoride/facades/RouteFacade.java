package com.tickettoride.facades;

import com.tickettoride.facades.helpers.GameFacadeHelper;
import com.tickettoride.facades.helpers.PlayerHelper;
import com.tickettoride.facades.helpers.PlayerStateHelper;
import com.tickettoride.facades.helpers.RouteHelper;
import com.tickettoride.models.Color;
import com.tickettoride.models.City;

import com.tickettoride.models.Game;
import com.tickettoride.models.Player;
import com.tickettoride.models.PlayerState;
import com.tickettoride.models.Route;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.UUID;

import command.Command;

public class RouteFacade extends BaseFacade {

    public static final String CONTROLLER_NAME = "RouteController";
    private static RouteFacade SINGLETON = new RouteFacade();

    public static RouteFacade getSingleton() {
        return SINGLETON;
    }
    private static Logger logger = LogManager.getLogger(RouteFacade.class.getName());

    public void incrementToClaimRouteState(UUID connID, Player player) {
        try {
            PlayerState playerState = PlayerStateHelper.getSingleton().getPlayerState(player.getPlayerID());
            PlayerState newPlayerState = playerState.moveToPlaceTrainsState();
            PlayerStateHelper.getSingleton().updatePlayerState(newPlayerState);
            Command command = new Command("GameController", "setGameState", newPlayerState);
            sendResponseToRoom(connID, command);
        } catch (Throwable throwable) {
            logger.error(throwable.getMessage(), throwable);
        }
    }

    public void claim(UUID connID, Route route, Player player, Color color, Integer colorCards, Integer wildCards) {
        try {
            TrainCardFacade.getSingleton().discard(connID, player.getPlayerID(), color, colorCards, wildCards, route.getGameID());
            int cardsDiscarded = colorCards + wildCards;
            route.setClaimedByPlayerID(player.getPlayerID());
            RouteHelper.getSingleton().updateRoute(route);
            player.setTrainCarCount(player.getTrainCarCount() - route.getSpaces());
            PlayerHelper.getSingleton().updateTrainCount(player);
            player.setPoints(player.getPoints() + route.getPoints());
            PlayerHelper.getSingleton().updatePlayerPoints(player);
            Game game = GameFacadeHelper.getSingleton().findGame(route.getGameID());
            game = GameFacadeHelper.getSingleton().updateGameTurn(game);
            List<PlayerState> playerStates = PlayerStateHelper.getSingleton().incrementGamePlayerStates(game);
            Command command = new Command(CONTROLLER_NAME, "claim", route, player, game.getCurTurn(), cardsDiscarded, playerStates);
            sendResponseToRoom(connID, command);
            String event="Claimed route ";
            List<City> cities=route.getCities();
            event+=cities.get(0).toString()+" to "+cities.get(1).toString();
            updateHistory(connID,player.getPlayerID(),event);
        } catch (Throwable t) { logger.error(t.getMessage(), t); }
    }
}
