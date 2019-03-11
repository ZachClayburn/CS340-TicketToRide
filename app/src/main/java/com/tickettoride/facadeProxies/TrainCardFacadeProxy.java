package com.tickettoride.facadeProxies;

import com.tickettoride.clientModels.DataManager;
import com.tickettoride.controllers.GameController;
import com.tickettoride.controllers.TrainCardController;
import com.tickettoride.controllers.helpers.GameControllerHelper;
import com.tickettoride.models.TrainCard;
import com.tickettoride.models.TrainCardDeck;

import java.util.UUID;

import command.Command;

public class TrainCardFacadeProxy {

    private static final String TAG = "TRAIN_CARD_FACADE_PROXY";

    public static TrainCardFacadeProxy SINGLETON = new TrainCardFacadeProxy();
    public static String FACADE_NAME = "TrainCardFacade";

    private TrainCardFacadeProxy() { }

    public void drawFaceupCard(int index, UUID playerID){
        Command command = new Command(FACADE_NAME, "drawFaceupCard", index, playerID);
        // ClientCommunicator.SINGLETON.send(command);
        // Send to server
        tempDrawFaceupCardLogic(index, playerID);
    }

    public void tempDrawFaceupCardLogic(int index, UUID playerID){
        // Add card to player hand in database

        TrainCard card = DataManager.SINGLETON.getTrainCardDeck().drawFromFaceUp(index);
        TrainCardDeck deck = DataManager.SINGLETON.getTrainCardDeck();

        TrainCardController.getSingleton().drawFaceupCard(playerID, card, deck);

        //Command command = new Command(CONTROLLER_NAME, "drawFaceupCard", playerID, card, deck);
        //sendResponseToRoom(command);
        // Send to controller
    }

    public void drawFacedownCard(UUID playerID){
        Command command = new Command(FACADE_NAME, "drawFacedownCard", playerID);
        // ClientCommunicator.SINGLETON.send(command);
        // Send to server
    }

    public void tempDrawFacedownCardLogic(UUID playerID){
        // Add card to player hand in database

        TrainCard card = DataManager.getSINGLETON().getTrainCardDeck().drawFromFaceDown();
        TrainCardDeck deck = DataManager.SINGLETON.getTrainCardDeck();

        TrainCardController.getSingleton().drawFaceDownCard(playerID, card, deck);

        // Command command = new Command(CONTROLLER_NAME, "drawFacedownCard", playerID, card, deck);
        // Send to controller
    }
}
