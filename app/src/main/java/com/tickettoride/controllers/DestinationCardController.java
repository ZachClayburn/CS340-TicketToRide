package com.tickettoride.controllers;

import android.widget.Toast;

import com.google.gson.internal.LinkedTreeMap;
import com.tickettoride.activities.GameRoomActivity;
import com.tickettoride.clientModels.DataManager;
import com.tickettoride.models.DestinationCard;
import com.tickettoride.models.Player;

import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;

public class DestinationCardController extends BaseController {

    private static final  DestinationCardController SINGLETON = new DestinationCardController();
    private DestinationCardController() {}

    public static DestinationCardController getSingleton() {
        return SINGLETON;
    }

    public void setPlayerAcceptedCards(Player player, ArrayList<LinkedTreeMap> acceptedCards, Integer destinationDeckCount, Integer turn) {
        Player managedPlayer = DataManager.SINGLETON.findPlayerByID(player.getPlayerID());
        boolean hasInitialized = true;
        if (managedPlayer.getDestinationCardCount() == 0) {
            hasInitialized = false;
        }
        if (isUserPlayer(player)){
            List<DestinationCard> destinationCards = DestinationCard.unGsonCards(acceptedCards);
            DataManager.getSINGLETON().getPlayerHand().getDestinationCards().addAll(destinationCards);
            player.incrementDestinationCardCount(acceptedCards.size());
            managedPlayer.incrementDestinationCardCount(acceptedCards.size());
        } else {
            try {
                Toast.makeText(getCurrentActivity(), "DRAW DESTINATION CARDS", Toast.LENGTH_LONG).show();
            } catch (Throwable t) {}
            managedPlayer.incrementDestinationCardCount(acceptedCards.size());
        }
        DataManager.getSINGLETON().setDestinationCardDeckSize(destinationDeckCount);
        GameRoomActivity activity = (GameRoomActivity) getCurrentActivity();
        DataManager.SINGLETON.setTurn(turn);
        if (hasInitialized) {
            activity.incrementTurnState();
        }
        activity.updateCards();
    }

    public void offerDestinationCards(Player player,ArrayList<LinkedTreeMap> gsonCards, Integer requiredToKeep) {

        if (isUserPlayer(player)) {
            List<DestinationCard> offeredCards = DestinationCard.unGsonCards(gsonCards);
            DataManager.getSINGLETON().setOfferedCards(requiredToKeep, offeredCards);
            GameRoomActivity activity = (GameRoomActivity) getCurrentActivity();
            activity.toDestinationCardFragment();
        } else {
            Player otherPlayer = DataManager.getSINGLETON().findPlayerByID(player.getPlayerID());
            otherPlayer.incrementDestinationCardCount(gsonCards.size());
        }
    }
}
