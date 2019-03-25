package com.tickettoride.controllers;

import android.widget.Toast;

import com.google.gson.internal.LinkedTreeMap;
import com.tickettoride.activities.GameRoomActivity;
import com.tickettoride.activities.MapFragment;
import com.tickettoride.clientModels.DataManager;
import com.tickettoride.models.Hand;
import com.tickettoride.models.Player;
import com.tickettoride.models.TrainCard;
import com.tickettoride.models.TrainCardDeck;
import com.tickettoride.models.idtypes.PlayerID;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.transform.TransformerConfigurationException;

public class TrainCardController extends BaseController {
    private static TrainCardController SINGLETON = new TrainCardController();
    public static TrainCardController getSingleton() { return SINGLETON; }
    private TrainCardController() {}

    public void initializeHand(PlayerID playerID, Hand hand){
        if (playerID == DataManager.SINGLETON.getPlayer().getPlayerID()){ DataManager.SINGLETON.setPlayerHand(hand); }
        else{
            Player player = DataManager.SINGLETON.findPlayerByID(playerID);
            player.setTrainCardCount(hand.getHandSize());
        }
    }

    public void initializeDecks(ArrayList<LinkedTreeMap> gsonCards){
        List<TrainCard> faceUp = TrainCard.unGsonCards(gsonCards);
        DataManager.SINGLETON.setTrainCardDeck(new TrainCardDeck(faceUp));
        GameRoomActivity activity = (GameRoomActivity) getCurrentActivity();
        MapFragment fragment = activity.getMapFragment();
        fragment.setAllColors();
    }

    public void drawFromFaceUp(PlayerID playerID, TrainCard card, ArrayList<LinkedTreeMap> gsonCards, int deckSize){
        GameRoomActivity activity = (GameRoomActivity) getCurrentActivity();
        MapFragment fragment = activity.getMapFragment();
        if (playerID == DataManager.SINGLETON.getPlayer().getPlayerID()){ DataManager.SINGLETON.addTrainCardToHand(card); }
        else{
            /*
            try {
                Toast.makeText(getCurrentActivity(), "DRAW FACE UP TRAIN CARD", Toast.LENGTH_LONG).show();
            } catch (Throwable t) {}
            */
            Player player = DataManager.SINGLETON.findPlayerByID(playerID);
            player.setTrainCardCount(player.getTrainCardCount() + 1);
        }

        List<TrainCard> faceUp = TrainCard.unGsonCards(gsonCards);
        DataManager.SINGLETON.updateFaceUpDeck(faceUp);
        DataManager.SINGLETON.updateTrainCardDeckSize(deckSize);
        fragment.setAllColors();
        fragment.updateDeckNumbers();
        fragment.finishDrawFaceUpTrainCard(card);
    }

    public void drawFromFaceDown(PlayerID playerID, TrainCard card, int deckSize){
        GameRoomActivity activity = (GameRoomActivity) getCurrentActivity();
        MapFragment fragment = activity.getMapFragment();
        if (playerID == DataManager.SINGLETON.getPlayer().getPlayerID()){ DataManager.SINGLETON.addTrainCardToHand(card); }
        else{
            /*
            try {
                Toast.makeText(getCurrentActivity(), "DRAW FACE DOWN TRAIN CARD", Toast.LENGTH_LONG).show();
            } catch (Throwable t) {}
            */
            Player player = DataManager.SINGLETON.findPlayerByID(playerID);
            player.setTrainCardCount(player.getTrainCardCount() + 1);
        }
        DataManager.SINGLETON.updateTrainCardDeckSize(deckSize);
        fragment.updateDeckNumbers();
        fragment.finishDrawFacedownCard();
    }

}
