package com.tickettoride.controllers;

import com.google.gson.internal.LinkedTreeMap;
import com.tickettoride.activities.GameRoomActivity;
import com.tickettoride.clientModels.DataManager;
import com.tickettoride.models.DestinationCard;
import com.tickettoride.models.Player;

import java.util.ArrayList;
import java.util.List;

public class DestinationCardController extends BaseController {

    private static final  DestinationCardController SINGLETON = new DestinationCardController();
    private DestinationCardController() {}

    public static DestinationCardController getSingleton() {
        return SINGLETON;
    }

    public void setPlayerAcceptedCards(Player player, ArrayList<LinkedTreeMap> acceptedCards) {

        List<DestinationCard> destinationCards = DestinationCard.unGsonCards(acceptedCards);

        if (isUserPlayer(player)){
            DataManager.getSINGLETON().getPlayerHand().getDestinationCards().addAll(destinationCards);
        } else {
            //TODO Update card count of other player
        }
    }

    public void offerDestinationCards(Player player,ArrayList<LinkedTreeMap> gsonCards, Integer requiredToKeep) {

        List<DestinationCard> offeredCards = DestinationCard.unGsonCards(gsonCards);
        if (isUserPlayer(player)) {
            DataManager.getSINGLETON().setOfferedCards(requiredToKeep, offeredCards);
            GameRoomActivity activity = (GameRoomActivity) getCurrentActivity();
            activity.toDestinationCardFragment();
        } else {
            //TODO Notify that other player is drawing destination cards
        }
    }

    public void updateDestinationDeck(int deckSize) {
        DataManager.getSINGLETON().setDestinationCardDeckSize(deckSize);
    }
}
