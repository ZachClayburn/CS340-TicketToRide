package com.tickettoride.controllers;

import com.tickettoride.clientModels.DataManager;
import com.tickettoride.models.DestinationCard;
import com.tickettoride.models.Player;

import java.util.Collection;
import java.util.List;

public class DestinationCardController extends BaseController {

    public void setPlayerAcceptedCards(Player player, Collection<DestinationCard> acceptedCards) {
        if (isUserPlayer(player)){
            //TODO Update players hand
        } else {
            //TODO Update card count of other player
        }
    }

    public void offerDestinationCards(Player player, List<DestinationCard> destinationCards, Integer requiredToKeep) {
        if (isUserPlayer(player)) {
            //TODO Begin Draw destination card
        } else {
            //TODO Notify that other player is drawing destination cards
        }
    }

    public void updateDestinationDeck(int deckSize) {
        DataManager.getSINGLETON().setDestinationCardDeckSize(deckSize);
    }
}
