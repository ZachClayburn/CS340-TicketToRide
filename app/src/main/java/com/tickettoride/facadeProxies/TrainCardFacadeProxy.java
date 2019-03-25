package com.tickettoride.facadeProxies;

import com.tickettoride.clientModels.DataManager;
import com.tickettoride.command.ClientCommunicator;
import com.tickettoride.controllers.GameController;
import com.tickettoride.controllers.TrainCardController;
import com.tickettoride.controllers.helpers.GameControllerHelper;
import com.tickettoride.models.Player;
import com.tickettoride.models.TrainCard;
import com.tickettoride.models.TrainCardDeck;

import java.util.UUID;

import com.tickettoride.models.idtypes.GameID;
import com.tickettoride.models.idtypes.PlayerID;
import command.Command;

public class TrainCardFacadeProxy {

    private static final String TAG = "TRAIN_CARD_FACADE_PROXY";

    public static TrainCardFacadeProxy SINGLETON = new TrainCardFacadeProxy();
    public static String FACADE_NAME = "TrainCardFacade";

    private TrainCardFacadeProxy() { }

    public void drawFaceupCard(GameID gameID, PlayerID playerID, int pos){
        try{
            Command command = new Command(FACADE_NAME, "drawFromFaceUp", gameID, playerID, pos);
            ClientCommunicator.SINGLETON.send(command);
        } catch(Throwable t){}
    }

    public void drawFacedownCard(GameID gameID, PlayerID playerID){
        try{
            Command command = new Command(FACADE_NAME, "drawFromFaceDown", gameID, playerID);
            ClientCommunicator.SINGLETON.send(command);
        } catch(Throwable t){}
    }

    public void initialize(GameID gameID){
        try{
            Command command = new Command(FACADE_NAME, "initialize", gameID);
            ClientCommunicator.SINGLETON.send(command);
        } catch(Throwable t){}
    }

}
