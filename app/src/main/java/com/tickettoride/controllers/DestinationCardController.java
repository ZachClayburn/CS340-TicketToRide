package com.tickettoride.controllers;

import android.widget.Toast;

import com.google.gson.internal.LinkedTreeMap;
import com.tickettoride.activities.GameRoomActivity;
import com.tickettoride.activities.MapFragment;
import com.tickettoride.clientModels.DataManager;
import com.tickettoride.clientModels.helpers.PlayerStateHelper;
import com.tickettoride.models.DestinationCard;
import com.tickettoride.models.Player;
import com.tickettoride.models.PlayerState;

import java.lang.reflect.InvocationTargetException;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;

public class DestinationCardController extends BaseController {

    private static final  DestinationCardController SINGLETON = new DestinationCardController();
    private DestinationCardController() {}

    public static DestinationCardController getSingleton() {
        return SINGLETON;
    }

    public void setPlayerAcceptedCards(Player player, ArrayList<LinkedTreeMap> acceptedCards, Integer destinationDeckCount,
                                       Integer turn, ArrayList<LinkedTreeMap<String, Object>> playerStateMap) throws ClassNotFoundException,
    NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException

    {
        Player managedPlayer = DataManager.SINGLETON.findPlayerByID(player.getPlayerID());
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
        List<PlayerState> playerStates = PlayerState.buildPlayerStateList(playerStateMap);
        DataManager.SINGLETON.setCurrentPLayerState(playerStates);
        GameRoomActivity activity = (GameRoomActivity) getCurrentActivity();
        DataManager.SINGLETON.setTurn(turn);
        activity.updateCards();
        activity.applyPlayerState();
    }

    public void offerDestinationCards(Player player, ArrayList<LinkedTreeMap> gsonCards, Integer requiredToKeep) {
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
