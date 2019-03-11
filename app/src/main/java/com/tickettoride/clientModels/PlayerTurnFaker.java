package com.tickettoride.clientModels;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.tickettoride.controllers.DestinationCardController;
import com.tickettoride.controllers.RoutesController;
import com.tickettoride.controllers.TrainCardController;
import com.tickettoride.models.Player;
import com.tickettoride.models.TrainCard;
import com.tickettoride.models.TrainCardDeck;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlayerTurnFaker {
    public static PlayerTurnFaker SINGLETON = new PlayerTurnFaker();
    public static PlayerTurnFaker getSINGLETON() {
        return SINGLETON;
    }
    private PlayerTurnFaker() {}
    private static Gson gson = new Gson();

    public void fakePlayerturn(Player player) {
        Random rand = new Random();
        int r = rand.nextInt(3);
        fakeCollectTrains(player);
        switch (r) {
            case 0: { fakeCollectTrains(player); return; }
            case 1: { fakeDrawDestinations(player); return; }
            case 2: { fakeClaimRoute(player); return; }
        }
    }

    private void fakeCollectTrains(Player player) {
        while (DataManager.getSINGLETON().getPlayerState().getClass() == NotTurnState.class)
        {
            Random rand = new Random();
            int r = rand.nextInt(1);
            switch (r) {
                case 0: { fakeDrawTrainFaceDown(player); }
                case 1: { fakeDrawTrainFaceUp(player); }
            }
        }
    }

    private void fakeDrawDestinations(Player player) {
        Random rand = new Random();
        int cardsKept = rand.nextInt(2) + 1;
        ArrayList<LinkedTreeMap> destinationCards = new ArrayList<>();
        for (int i = 0; i < cardsKept; i++) { destinationCards.add(new LinkedTreeMap()); }
        int newDeckCount = DataManager.getSINGLETON().getDestinationCardDeckSize() - cardsKept;
        DestinationCardController.getSingleton().setPlayerAcceptedCards(player, destinationCards, newDeckCount);
    }

    private void fakeClaimRoute(Player player) {
        Random rand = new Random();
        List<Route> routes = DataManager.getSINGLETON().getRoutes();
        int routeindex = rand.nextInt(routes.size());
        Route toClaimRoute = routes.get(routeindex);
        toClaimRoute.setIsClaimed(true);
        toClaimRoute.setLineColor(player.getColor());
        player.setPoints(player.getPoints() + toClaimRoute.getSpaces());
        RoutesController.getSingleton().claimRoute(player, toClaimRoute);
    }

    private void fakeDrawTrainFaceDown(Player player) {
        TrainCardDeck trainCardDeck = DataManager.getSINGLETON().getTrainCardDeck();
        TrainCard trainCard = trainCardDeck.drawFromFaceDown();
        TrainCardController.getSingleton().drawFaceDownCard(player.getPlayerID(), trainCard, trainCardDeck);
    }

    private void fakeDrawTrainFaceUp(Player player) {
        TrainCardDeck trainCardDeck = DataManager.getSINGLETON().getTrainCardDeck();
        TrainCard trainCard = trainCardDeck.drawFromFaceDown();
        TrainCardController.getSingleton().drawFaceupCard(player.getPlayerID(), trainCard, trainCardDeck);
    }
}
