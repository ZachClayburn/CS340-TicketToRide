package com.tickettoride.facadeProxies;

import android.util.Log;
import com.tickettoride.clientModels.DataManager;
import com.tickettoride.command.ClientCommunicator;
import com.tickettoride.models.DestinationCard;
import com.tickettoride.models.Player;
import com.tickettoride.models.Session;
import command.Command;

import java.util.Collection;

public class DestinationCardFacadeProxy {

    public static final String TAG = "DESTINATION_CARD_FACADE_PROXY";
    private static final String FACADE_NAME = "DestinationCardFacade";

    public static void drawDestinationCards(Player player){

        try {

            Command command = new Command(FACADE_NAME, "drawDestinationCards", player);
            ClientCommunicator.SINGLETON.send(command);

        } catch (Throwable throwable) {

            Log.e(TAG, "drawDestinationCards: ", throwable);

        }
    }

    public static void acceptDestinationCards(Player player, Collection<DestinationCard> acceptedCards) {

        try {

            Command command = new Command(FACADE_NAME, "acceptDestinationCards", player, acceptedCards);
            ClientCommunicator.SINGLETON.send(command);

        } catch (Throwable throwable) {

            Log.e(TAG, "acceptDestinationCards: ", throwable);
        }
    }
}
