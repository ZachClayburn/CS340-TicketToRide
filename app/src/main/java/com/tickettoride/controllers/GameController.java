package com.tickettoride.controllers;

import android.app.Activity;
import android.graphics.Paint;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.tickettoride.R;
import com.tickettoride.activities.CreateGameActivity;
import com.tickettoride.activities.JoinGameActivity;
import com.tickettoride.activities.LobbyActivity;
import com.tickettoride.clientModels.DataManager;
import com.tickettoride.clientModels.Game;
import com.tickettoride.clientModels.GameIndex;
import com.tickettoride.clientModels.Player;

import java.util.UUID;

public class GameController extends BaseController {
    private static GameController SINGLETON = new GameController();
    public static GameController getSingleton() { return SINGLETON; }
    private GameController() {}

    public void create(UUID playerID, UUID sessionID, UUID gameID, String groupName, Integer numPlayer, Integer maxPlayer) {
        Player player = new Player(gameID, sessionID, playerID);
        Game game = new Game(gameID, groupName, numPlayer, maxPlayer, player);
        DataManager.SINGLETON.setGame(game);
        DataManager.SINGLETON.setPlayer(player);
        DataManager.SINGLETON.getGameIndex().addGame(game);
        if (DataManager.SINGLETON.getSession().getSessionId().equals(sessionID)) moveToLobby(player, game);
        else {
            JoinGameActivity joinGameActivity;
            try {
                joinGameActivity = (JoinGameActivity) getCurrentActivity();
                joinGameActivity.updateUI();
            } catch (ClassCastException e) {
                Log.i("GameController", "New Game Created");
            }
        }

    }

    public void join(UUID playerID, UUID sessionID, UUID gameID, String groupName, Integer numPlayer, Integer maxPlayer) {
        Player player = new Player(gameID, sessionID, playerID);
        Game game = new Game(gameID, groupName, numPlayer, maxPlayer, player);
        GameIndex.SINGLETON.findGame(game.getGameID().toString()).addPlayer(player);
        // If user is the one joining game and becoming a player
        if (DataManager.SINGLETON.getSession().getSessionId().equals(sessionID)) {
            DataManager.SINGLETON.setPlayer(player);
            JoinGameActivity joinGameActivity = (JoinGameActivity) getCurrentActivity();
            joinGameActivity.moveToLobbyJoin(game);
        } else if (DataManager.SINGLETON.getPlayer().getGameID().equals(game.getGameID())) {
            LobbyActivity lobbyActivity = (LobbyActivity) getCurrentActivity();
            lobbyActivity.updateUI(game);
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

    public void errorJoin() {
        JoinGameActivity joinGameActivity = (JoinGameActivity) getCurrentActivity();
        joinGameActivity.JoinError();
    }

    private void moveToLobby(Player player, Game game) {
        DataManager.SINGLETON.setPlayer(player);
        CreateGameActivity createGameActivity = (CreateGameActivity) getCurrentActivity();
        createGameActivity.moveToLobbyCreate(game);
    }

    public void start() {
        Log.i("GAME_CONTROLLER", "Calling Start");
        LobbyActivity activity = (LobbyActivity) getCurrentActivity();
        Snackbar.make(activity.findViewById(R.id.lobby_linear_layout), R.string.game_start_message, Snackbar.LENGTH_LONG ).show();
    }

    public void errorLeave(Throwable t){
        Log.i("GameController", "Couldn't leave game");
    }
}
