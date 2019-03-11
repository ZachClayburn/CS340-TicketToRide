package com.tickettoride.facadeProxies;

import android.util.Log;
import com.tickettoride.clientModels.DataManager;
import com.tickettoride.controllers.GameController;
import com.tickettoride.models.Session;
import com.tickettoride.command.ClientCommunicator;

import java.util.UUID;

import com.tickettoride.models.Game;
import com.tickettoride.models.TrainCard;
import com.tickettoride.models.TrainCardDeck;

import command.Command;

public class GameFacadeProxy {

    private static final String TAG = "GAME_FACADE_PROXY";

    public static GameFacadeProxy SINGLETON = new GameFacadeProxy();
    public static String FACADE_NAME = "GameFacade";

    private GameFacadeProxy() { }

    public void create(String name, int maxPlayers){
        Session session = DataManager.getSINGLETON().getSession();
        try{
            Command command = new Command(FACADE_NAME, "create", session.getSessionID(), name, maxPlayers);
            ClientCommunicator.SINGLETON.send(command);
        } catch(Throwable t){}
    }

    public void join(UUID gameID){
        Session session = DataManager.getSINGLETON().getSession();
        try {
            Command command = new Command(FACADE_NAME, "join", session.getSessionID(), gameID);
            ClientCommunicator.SINGLETON.send(command);
        } catch(Throwable t) {}
    }

    public void leave() {
        try {
            Session session =  DataManager.getSINGLETON().getSession();
            Command command = new Command(FACADE_NAME, "leave", session.getSessionID());
            ClientCommunicator.SINGLETON.send(command);
        } catch (Throwable t) {}
    }

    public void startGame(Game game) {
        Log.d(TAG, "startGame: Start game called, prepping command to send");
        Command command = new Command(FACADE_NAME, "start", game.getGameID());

        ClientCommunicator.SINGLETON.send(command);
    }

    public void setup(Game game) {
        Command command = new Command(FACADE_NAME, "setup", game.getGameID());
        ClientCommunicator.SINGLETON.send(command);
    }

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

        GameController.getSingleton().drawFaceupCard(playerID, card, deck);

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

        GameController.getSingleton().drawFaceDownCard(playerID, card, deck);

        // Command command = new Command(CONTROLLER_NAME, "drawFacedownCard", playerID, card, deck);
        // Send to controller
    }
}
