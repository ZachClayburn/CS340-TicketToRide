package com.tickettoride.controllers;

import com.tickettoride.activities.GameRoomActivity;
import com.tickettoride.clientModels.DataManager;
import com.tickettoride.models.DestinationCard;
import com.tickettoride.models.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DestinationCardController extends BaseController {

    private static final  DestinationCardController SINGLETON = new DestinationCardController();
    private DestinationCardController() {}

    public static DestinationCardController getSingleton() {
        return SINGLETON;
    }

    public void setPlayerAcceptedCards(Player player, Collection<DestinationCard> acceptedCards) {
        if (isUserPlayer(player)){
            //TODO Update players hand
        } else {
            //TODO Update card count of other player
        }
    }

    public void offerDestinationCards(Player player,
                                      DestinationCard card1, DestinationCard card2, DestinationCard card3,
                                      Integer requiredToKeep) {
        if (isUserPlayer(player)) {
            GameRoomActivity activity = (GameRoomActivity) getCurrentActivity();
            activity.toDestinationCardFragment(card1, card2, card3, requiredToKeep);
        } else {
            //TODO Notify that other player is drawing destination cards
        }
    }

    public void updateDestinationDeck(int deckSize) {
        DataManager.getSINGLETON().setDestinationCardDeckSize(deckSize);
    }
}
