package com.tickettoride.clientModels.helpers;

import com.tickettoride.clientModels.ClientLine;
import com.tickettoride.clientModels.ClientRoute;
import com.tickettoride.clientModels.DataManager;
import com.tickettoride.models.City;
import com.tickettoride.models.Color;
import com.tickettoride.models.Hand;
import com.tickettoride.models.Player;
import com.tickettoride.models.Route;
import com.tickettoride.models.idtypes.PlayerID;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.tickettoride.models.Color.GREY;

public class RouteHelper {
    private static RouteHelper SINGLETON = new RouteHelper();
    public static RouteHelper getSingleton() { return SINGLETON; }
    private RouteHelper() {}

    public List<ClientRoute> playerFilteredRoutes() {
        List<ClientRoute> clientRoutes = DataManager.getSINGLETON().getClientRoutes();
        List<ClientRoute> filteredClientRoutes = new ArrayList<>();
        Hand hand = DataManager.getSINGLETON().getPlayerHand();
        for (int i = 0; i < clientRoutes.size(); ++i) {
            if (clientRoutes.get(i).getColor() == GREY) {
                for (Color color : Color.values()) {
                    if (hand.getColor(color) + hand.getLocomotive() >= clientRoutes.get(i).getSpaces() && isAvailable(clientRoutes.get(i))) {
                        filteredClientRoutes.add(clientRoutes.get(i));
                    }
                }
            } else if (hand.getColor(clientRoutes.get(i).getColor()) + hand.getLocomotive() >= clientRoutes.get(i).getSpaces() &&
                    isAvailable(clientRoutes.get(i))) {
                filteredClientRoutes.add(clientRoutes.get(i));
            }
        }
        return filteredClientRoutes;
    }

    public boolean isAvailable(ClientRoute clientRoute) {
        List<ClientRoute> clientRoutes = DataManager.getSINGLETON().getClientRoutes();
        for (ClientRoute matchClientRoute: clientRoutes) {
            boolean citiesMatch = true;
            for (City city : clientRoute.getCities()) {
                if (!matchClientRoute.getCities().contains(city))
                    citiesMatch = false;
            }
            PlayerID currentPlayerId = DataManager.getSINGLETON().getPlayer().getPlayerID();
            PlayerID matchClientRouteId = matchClientRoute.getClaimedByPlayerID();
            if (matchClientRouteId == null) continue;
            if (citiesMatch && currentPlayerId.equals(matchClientRouteId)) return false;
    }
        return true;
    }


    public void scaleRouteLines(double scale) {
        ClientLine.setScale(scale);
        List<ClientRoute> clientRoutes = DataManager.getSINGLETON().getClientRoutes();
        for (ClientRoute route: clientRoutes) { route.resetLines(); }
    }

    private void discardYellow(Hand hand, int colorCards, int routeLength) {
        int requiredLocamotives = routeLength - colorCards;
        if (requiredLocamotives > 0) {
            hand.discardYellow(colorCards);
            hand.discardLocomotive(requiredLocamotives);
        } else {hand.discardYellow(routeLength);}
    }

    private void discardRed(Hand hand, int colorCards, int routeLength) {
        int requiredLocamotives = routeLength - colorCards;
        if (requiredLocamotives > 0) {
            hand.discardRed(colorCards);
            hand.discardLocomotive(requiredLocamotives);
        } else {hand.discardRed(routeLength);}
    }

    private void discardBlue(Hand hand, int colorCards, int routeLength) {
        int requiredLocamotives = routeLength - colorCards;
        if (requiredLocamotives > 0) {
            hand.discardBlue(colorCards);
            hand.discardLocomotive(requiredLocamotives);
        } else {hand.discardBlue(routeLength);}
    }

    private void discardPurple(Hand hand, int colorCards, int routeLength) {
        int requiredLocamotives = routeLength - colorCards;
        if (requiredLocamotives > 0) {
            hand.discardPurple(colorCards);
            hand.discardLocomotive(requiredLocamotives);
        } else {hand.discardPurple(routeLength);}
    }

    private void discardBlack(Hand hand, int colorCards, int routeLength) {
        int requiredLocamotives = routeLength - colorCards;
        if (requiredLocamotives > 0) {
            hand.discardBlack(colorCards);
            hand.discardLocomotive(requiredLocamotives);
        } else {hand.discardBlack(routeLength);}
    }

    private void discardGreen(Hand hand, int colorCards, int routeLength) {
        int requiredLocamotives = routeLength - colorCards;
        if (requiredLocamotives > 0) {
            hand.discardGreen(colorCards);
            hand.discardLocomotive(requiredLocamotives);
        } else {hand.discardGreen(routeLength);}
    }

    private void discardWhite(Hand hand, int colorCards, int routeLength) {
        int requiredLocamotives = routeLength - colorCards;
        if (requiredLocamotives > 0) {
            hand.discardWhite(colorCards);
            hand.discardLocomotive(requiredLocamotives);
        } else {hand.discardWhite(routeLength);}
    }

    private void discardOrange(Hand hand, int colorCards, int routeLength) {
        int requiredLocamotives = routeLength - colorCards;
        if (requiredLocamotives > 0) {
            hand.discardOrange(colorCards);
            hand.discardLocomotive(requiredLocamotives);
        } else {hand.discardOrange(routeLength);}
    }

    private void payForRoute(ClientRoute curClientRoute) {
        Color color = curClientRoute.getColor();
        int colorCards;
        Hand hand = DataManager.getSINGLETON().getPlayerHand();
        int routeLength = curClientRoute.getSpaces();
        int requiredLocamotives;
        switch(color) {
            case YELLOW:
                colorCards = hand.getYellow();
                discardYellow(hand, colorCards, routeLength);
                break;
            case RED:
                colorCards = hand.getRed();
                discardRed(hand, colorCards, routeLength);
                break;
            case BLUE:
                colorCards = hand.getBlue();
                discardBlue(hand, colorCards, routeLength);
                break;
            case PURPLE:
                colorCards = hand.getPurple();
                discardPurple(hand, colorCards, routeLength);
                break;
            case BLACK:
                colorCards = hand.getBlack();
                discardBlack(hand, colorCards, routeLength);
                break;
            case GREEN:
                colorCards = hand.getGreen();
                discardGreen(hand, colorCards, routeLength);
                break;
            case WHITE:
                colorCards = hand.getWhite();
                discardWhite(hand, colorCards, routeLength);
                break;
            case ORANGE:
                colorCards = hand.getOrange();
                discardOrange(hand, colorCards, routeLength);
                break;
            case GREY:
                int locamotivesInHand = hand.getLocomotive();
                colorCards = hand.getYellow();
                if ((colorCards + locamotivesInHand) >= routeLength) { discardYellow(hand, colorCards, routeLength); return; }
                colorCards = hand.getRed();
                if ((colorCards + locamotivesInHand) >= routeLength) { discardRed(hand, colorCards, routeLength); return; }
                colorCards = hand.getBlue();
                if ((colorCards + locamotivesInHand) >= routeLength) { discardBlue(hand, colorCards, routeLength); return; }
                colorCards = hand.getPurple();
                if ((colorCards + locamotivesInHand) >= routeLength) { discardPurple(hand, colorCards, routeLength); return; }
                colorCards = hand.getBlack();
                if ((colorCards + locamotivesInHand) >= routeLength) { discardBlack(hand, colorCards, routeLength); return; }
                colorCards = hand.getGreen();
                if ((colorCards + locamotivesInHand) >= routeLength) { discardGreen(hand, colorCards, routeLength); return; }
                colorCards = hand.getWhite();
                if ((colorCards + locamotivesInHand) >= routeLength) { discardWhite(hand, colorCards, routeLength); return; }
                colorCards = hand.getOrange();
                if ((colorCards + locamotivesInHand) >= routeLength) { discardOrange(hand, colorCards, routeLength); return; }
        }
    }

    public void claimRoute(ClientRoute curClientRoute, Player player) {
//        curClientRoute.setIsClaimed(true);
//        curClientRoute.setLineColor(player.getColor());
        int spaces = curClientRoute.getSpaces();
        player.givePoints(spaces);
    }

}
