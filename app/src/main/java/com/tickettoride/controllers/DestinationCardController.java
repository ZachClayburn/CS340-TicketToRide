package com.tickettoride.controllers;

import com.google.gson.internal.LinkedTreeMap;
import com.tickettoride.activities.GameRoomActivity;
import com.tickettoride.activities.MapFragment;
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

    public void setPlayerAcceptedCards(Player player, ArrayList<LinkedTreeMap> acceptedCards, Integer destinationDeckCount) {
        if (isUserPlayer(player)){
            List<DestinationCard> destinationCards = DestinationCard.unGsonCards(acceptedCards);
            DataManager.getSINGLETON().getPlayerHand().getDestinationCards().addAll(destinationCards);
        } else {
            Player managedPlayer = DataManager.SINGLETON.findPlayerByID(player.getPlayerID());
            managedPlayer.setDestinationCardCount(managedPlayer.getDestinationCardCount() + acceptedCards.size());
        }
        DataManager.getSINGLETON().setDestinationCardDeckSize(destinationDeckCount);
        GameRoomActivity activity = (GameRoomActivity) getCurrentActivity();
        activity.incrementTurn();

        MapFragment fragment = activity.getMapFragment();
        fragment.updateDeckNumbers();
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
}
