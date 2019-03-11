package com.tickettoride.controllers;

import com.tickettoride.activities.GameRoomActivity;
import com.tickettoride.activities.MapFragment;
import com.tickettoride.clientModels.DataManager;
import com.tickettoride.models.Player;
import com.tickettoride.models.TrainCard;
import com.tickettoride.models.TrainCardDeck;
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
        fragment.finishDrawFacedownCard();
    }
}
