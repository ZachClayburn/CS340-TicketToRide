package com.tickettoride.facadeProxies;

import android.util.Log;
import com.tickettoride.clientModels.DataManager;
import com.tickettoride.clientModels.InitializeGameState;
import com.tickettoride.command.ClientCommunicator;
import com.tickettoride.models.DestinationCard;
import com.tickettoride.models.Player;
import com.tickettoride.models.Session;
import command.Command;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;

public class DestinationCardFacadeProxy {

    public static final String TAG = "DESTINATION_CARD_FACADE_PROXY";
    private static final String FACADE_NAME = "DestinationCardFacade";

    public static void drawDestinationCards(Player player, int cardsToKeep){

        try {

            Command command = new Command(FACADE_NAME, "drawDestinationCards", player, cardsToKeep);
            ClientCommunicator.SINGLETON.send(command);

        } catch (Throwable throwable) {

            Log.e(TAG, "drawDestinationCards: ", throwable);
        }
    }

    public static void acceptDestinationCards(Player player, ArrayList<DestinationCard> acceptedCards) {
        try {
            Boolean incrementTurn = true;
            if (DataManager.SINGLETON.getPlayerState().getClass() == InitializeGameState.class) { incrementTurn = false; }
            Command command = new Command(FACADE_NAME, "acceptDestinationCards", player, acceptedCards, incrementTurn);
            ClientCommunicator.SINGLETON.send(command);
        } catch (Throwable throwable) { Log.e(TAG, "acceptDestinationCards: ", throwable); }
    }

    public static void getOfferedCards(Player player, int requiredToKeep) {

        try {

            ClientCommunicator.SINGLETON.send(
                    new Command(FACADE_NAME, "getOfferedCards", player, requiredToKeep)
            );

        } catch (Throwable throwable) {

            Log.e(TAG, "getOfferedCards: Could not get offered cards", throwable);
        }
    }

}
