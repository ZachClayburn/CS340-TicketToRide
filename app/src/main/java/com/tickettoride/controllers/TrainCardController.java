package com.tickettoride.controllers;

import com.tickettoride.activities.GameRoomActivity;
import com.tickettoride.activities.MapFragment;
import com.tickettoride.clientModels.DataManager;
import com.tickettoride.models.Hand;
import com.tickettoride.models.Player;
import com.tickettoride.models.TrainCard;
import com.tickettoride.models.TrainCardDeck;

import java.util.List;
import java.util.UUID;

public class TrainCardController extends BaseController {
    private static TrainCardController SINGLETON = new TrainCardController();
    public static TrainCardController getSingleton() { return SINGLETON; }
    private TrainCardController() {}

    public void drawFaceupCard(UUID playerID, TrainCard card, TrainCardDeck deck){
        DataManager.SINGLETON.setTrainCardDeck(deck);
        GameRoomActivity activity = (GameRoomActivity) getCurrentActivity();
        MapFragment fragment = activity.getMapFragment();
        if (playerID == DataManager.SINGLETON.getPlayer().getPlayerID()){ DataManager.SINGLETON.addTrainCardToHand(card); }
        else{
            Player player = DataManager.SINGLETON.findPlayerByID(playerID);
            player.setTrainCardCount(player.getTrainCardCount() + 1);
        }
        fragment.setAllColors();
        DataManager.SINGLETON.updateTrainCardDeckSize();
        fragment.updateDeckNumbers();
        fragment.finishDrawFaceUpTrainCard(card);
    }


    public void drawFaceDownCard(UUID playerID, TrainCard card, TrainCardDeck deck){
        DataManager.SINGLETON.setTrainCardDeck(deck);
        GameRoomActivity activity = (GameRoomActivity) getCurrentActivity();
        MapFragment fragment = activity.getMapFragment();
        if (playerID == DataManager.SINGLETON.getPlayer().getPlayerID()){ DataManager.SINGLETON.addTrainCardToHand(card); }
        else{
            Player player = DataManager.SINGLETON.findPlayerByID(playerID);
            player.setTrainCardCount(player.getTrainCardCount() + 1);
        }
        DataManager.SINGLETON.updateTrainCardDeckSize();
        fragment.updateDeckNumbers();
        fragment.finishDrawFacedownCard();
    }

    public void initializeHand(UUID playerID, Hand hand){
        if (playerID == DataManager.SINGLETON.getPlayer().getPlayerID()){ DataManager.SINGLETON.setPlayerHand(hand); }
        else{
            Player player = DataManager.SINGLETON.findPlayerByID(playerID);
            player.setTrainCardCount(hand.getHandSize());
        }
    }

    public void initializeDecks(List<TrainCard> faceupDeck, List<TrainCard> faceDownDeck, List<TrainCard> discard){
        DataManager.SINGLETON.setTrainCardDeck(new TrainCardDeck(faceupDeck, faceDownDeck, discard));
    }
}
