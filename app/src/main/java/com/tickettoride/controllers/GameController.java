package com.tickettoride.controllers;

import android.app.Activity;

import com.tickettoride.activities.CreateGameActivity;
import com.tickettoride.activities.JoinGameActivity;
import com.tickettoride.activities.LobbyActivity;
import com.tickettoride.activities.MyBaseActivity;
import com.tickettoride.application.MyApplication;
import com.tickettoride.clientModels.DataManager;
import com.tickettoride.clientModels.GameIndex;
import com.tickettoride.clientModels.GameInfo;
import com.tickettoride.clientModels.Player;

import java.util.UUID;

public class GameController extends BaseController {
    private static GameController SINGLETON = new GameController();
    public static GameController getSingleton() { return SINGLETON; }
    private GameController() {}

    public void create(UUID playerID, UUID sessionID, UUID gameID, String groupName, int numPlayer, int maxPlayer) {
        Player player = new Player(gameID, sessionID, playerID);
        GameInfo game = new GameInfo(gameID, groupName, numPlayer, maxPlayer, player);
        DataManager.SINGLETON.setGameInfo(game);
        DataManager.SINGLETON.setPlayer(player);
        DataManager.SINGLETON.getGameIndex().addGame(game);
        // If this user is the creator of the game, change activities
        if (DataManager.SINGLETON.getSession().getSessionId().equals(sessionID.toString())) {
            CreateGameActivity createGameActivity = (CreateGameActivity) getCurrentActivity();
            createGameActivity.moveToLobbyCreate();
            LobbyActivity lobbyActivity = (LobbyActivity) getCurrentActivity();
            lobbyActivity.updateUI();
        }
        // Update game index for all other players
        else {
            JoinGameActivity joinGameActivity = (JoinGameActivity) getCurrentActivity();
            joinGameActivity.updateUI();
        }

    }

    public void errorCreate(Throwable t) {
        CreateGameActivity createGameActivity = (CreateGameActivity) getCurrentActivity();
        createGameActivity.createError();
    }
}
