package com.tickettoride.controllers;

import com.google.gson.internal.LinkedTreeMap;
import com.tickettoride.activities.GameRoomActivity;
import com.tickettoride.activities.MapFragment;
import com.tickettoride.clientModels.DataManager;
import com.tickettoride.models.Hand;
import com.tickettoride.models.Player;
import com.tickettoride.models.PlayerState;
import com.tickettoride.models.TrainCard;
import com.tickettoride.models.TrainCardDeck;
import com.tickettoride.models.idtypes.PlayerID;


import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class TrainCardController extends BaseController {
    private static TrainCardController SINGLETON = new TrainCardController();
    public static TrainCardController getSingleton() { return SINGLETON; }
    private TrainCardController() {}


    public void initializeHand(PlayerID playerID, Hand hand){
        if (playerID.equals(DataManager.SINGLETON.getPlayer().getPlayerID())){
            DataManager.SINGLETON.getPlayerHand().replace(hand);
        }
        DataManager.SINGLETON.findPlayerByID(playerID).setTrainCardCount(hand.getHandSize());
        //else{
        //    Player player = DataManager.SINGLETON.findPlayerByID(playerID).setTrainCardCount(hand.getHandSize());
        //    player;
        //}
    }

    public void initializeDecks(ArrayList<LinkedTreeMap> gsonCards, Integer deckSize){
        List<TrainCard> faceUp = TrainCard.unGsonCards(gsonCards);
        DataManager.SINGLETON.setTrainCardDeck(new TrainCardDeck(faceUp));
        GameRoomActivity activity = (GameRoomActivity) getCurrentActivity();
        DataManager.SINGLETON.updateTrainCardDeckSize(deckSize);
        if (activity != null) {
            MapFragment fragment = activity.getMapFragment();
            fragment.setAllColors();
            fragment.updateDeckNumbers();
            activity.updateCards();
        }
    }

    public void drawFromFaceUp(PlayerID playerID, TrainCard card, ArrayList<LinkedTreeMap> gsonCards, Integer deckSize){
        GameRoomActivity activity = (GameRoomActivity) getCurrentActivity();
        MapFragment fragment = activity.getMapFragment();
        if (playerID.equals(DataManager.SINGLETON.getPlayer().getPlayerID())){
            DataManager.SINGLETON.addTrainCardToHand(card);
        }
        Player player = DataManager.SINGLETON.findPlayerByID(playerID);
        player.setTrainCardCount(player.getTrainCardCount() + 1);

        List<TrainCard> faceUp = TrainCard.unGsonCards(gsonCards);
        DataManager.SINGLETON.updateFaceUpDeck(faceUp);
        DataManager.SINGLETON.updateTrainCardDeckSize(deckSize);
        //fragment.setAllColors();
        //fragment.updateDeckNumbers();
        fragment.finishDrawFaceUpTrainCard(card);
        activity.updateCards();
    }

    public void drawFromFaceDown(PlayerID playerID, TrainCard card, Integer deckSize){
        GameRoomActivity activity = (GameRoomActivity) getCurrentActivity();
        MapFragment fragment = activity.getMapFragment();
        if (playerID.equals(DataManager.SINGLETON.getPlayer().getPlayerID())){
            DataManager.SINGLETON.addTrainCardToHand(card);
        }
        Player player = DataManager.SINGLETON.findPlayerByID(playerID);
        player.setTrainCardCount(player.getTrainCardCount() + 1);

        DataManager.SINGLETON.updateTrainCardDeckSize(deckSize);
        //fragment.updateDeckNumbers();
        fragment.finishDrawFacedownCard();
        activity.updateCards();
    }

    public void finish(Integer turn, ArrayList<LinkedTreeMap<String, Object>> playerStateMap) throws ClassNotFoundException,
            NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        List<PlayerState> playerStates = PlayerState.buildPlayerStateList(playerStateMap);
        DataManager.SINGLETON.setCurrentPLayerState(playerStates);
        DataManager.SINGLETON.setTurn(turn);
        GameRoomActivity activity = (GameRoomActivity) getCurrentActivity();
        activity.applyPlayerState();
    }

    public void updateDecks(ArrayList<LinkedTreeMap> gsonCards, Integer deckSize) {
        GameRoomActivity activity = (GameRoomActivity) getCurrentActivity();
        MapFragment fragment = activity.getMapFragment();
        List<TrainCard> faceUp = TrainCard.unGsonCards(gsonCards);
        DataManager.SINGLETON.updateFaceUpDeck(faceUp);
        DataManager.SINGLETON.updateTrainCardDeckSize(deckSize);
        activity.updateCards();
    }

}
