package com.tickettoride.controllers;

import android.widget.Toast;

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

    public void setPlayerAcceptedCards(Player player, ArrayList<LinkedTreeMap> acceptedCards, Integer destinationDeckCount, Integer turn) {
        if (isUserPlayer(player)){
            List<DestinationCard> destinationCards = DestinationCard.unGsonCards(acceptedCards);
            DataManager.getSINGLETON().getPlayerHand().getDestinationCards().addAll(destinationCards);
            player.setDestinationCardCount(player.getDestinationCardCount() + acceptedCards.size());
            Player managedPlayer = DataManager.SINGLETON.findPlayerByID(player.getPlayerID());
            managedPlayer.setDestinationCardCount(managedPlayer.getDestinationCardCount() + acceptedCards.size());
        } else {
            try {
                Toast.makeText(getCurrentActivity(), "DRAW DESTINATION CARDS", Toast.LENGTH_LONG).show();
            } catch (Throwable t) {}
            Player managedPlayer = DataManager.SINGLETON.findPlayerByID(player.getPlayerID());
            managedPlayer.setDestinationCardCount(managedPlayer.getDestinationCardCount() + acceptedCards.size());
        }
        DataManager.getSINGLETON().setDestinationCardDeckSize(destinationDeckCount);
        GameRoomActivity activity = (GameRoomActivity) getCurrentActivity();
        DataManager.SINGLETON.setTurn(turn);
        activity.activateTurn();
        activity.updateCards();
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
