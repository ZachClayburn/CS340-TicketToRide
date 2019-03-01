package com.tickettoride.controllers;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.google.gson.internal.LinkedTreeMap;
import com.tickettoride.R;
import com.tickettoride.activities.CreateGameActivity;
import com.tickettoride.activities.GameRoomActivity;
import com.tickettoride.activities.JoinGameActivity;
import com.tickettoride.activities.LobbyActivity;
import com.tickettoride.activities.MyBaseActivity;
import com.tickettoride.clientModels.DataManager;
import com.tickettoride.clientModels.Game;
import com.tickettoride.clientModels.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GameController extends BaseController {
    private static GameController SINGLETON = new GameController();
    public static GameController getSingleton() { return SINGLETON; }
    private GameController() {}

    public void create(UUID playerID, UUID sessionID, UUID gameID, String groupName, Integer numPlayer, Integer maxPlayer, Boolean isStarted) {
        Game game = new Game(gameID, groupName, numPlayer, maxPlayer, isStarted);
        try { createGameOnJoinActivity(game); return; } catch (ClassCastException e) { }
        try { createGameOnCreateGameActivity(game, sessionID, playerID); return; } catch (ClassCastException e) { }
    }

    private void createGameOnJoinActivity(Game game) throws ClassCastException {
        JoinGameActivity joinGameActivity = (JoinGameActivity) getCurrentActivity();
        DataManager.getSINGLETON().getGameIndex().addJoinGame(game);
        joinGameActivity.updateUI();
    }

    private void createGameOnCreateGameActivity(Game game, UUID sessionID, UUID playerID) throws ClassCastException {
        CreateGameActivity createGameActivity = (CreateGameActivity) getCurrentActivity();
        Player player = new Player(game.getGameID(), sessionID, playerID);
        DataManager.SINGLETON.setGame(game);
        DataManager.SINGLETON.setPlayer(player);
        DataManager.SINGLETON.getGameIndex().addRejoinGame(game);
        createGameActivity.moveToLobbyCreate(game);
    }

    public void join(UUID playerID, UUID sessionID, UUID gameID, String groupName, Integer numPlayer, Integer maxPlayer, Boolean isStarted) {
        Player player = new Player(gameID, sessionID, playerID);
        Game game = new Game(gameID, groupName, numPlayer, maxPlayer, isStarted);
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
    
    public void rejoin(UUID playerID, UUID sessionID, UUID gameID, String groupName, Integer numPlayer, Integer maxPlayer, Boolean isStarted){
        Player player = new Player(gameID, sessionID, playerID);
        Game game = new Game(gameID, groupName, numPlayer, maxPlayer, isStarted);
        if (DataManager.SINGLETON.getSession().getSessionId().equals(sessionID)) {
            DataManager.SINGLETON.setPlayer(player);
            JoinGameActivity joinGameActivity = (JoinGameActivity) getCurrentActivity();
            joinGameActivity.moveToLobbyJoin(game);
        }else if (DataManager.SINGLETON.getPlayer().getGameID().equals(game.getGameID())) {
            LobbyActivity lobbyActivity = (LobbyActivity) getCurrentActivity();
            lobbyActivity.updateUI(game);
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

    public void leave(ArrayList<LinkedTreeMap> linkedTreeJoinGames, ArrayList<LinkedTreeMap> linkedTreeRejoinGames) {
        ArrayList<Game> joinGames = Game.buildGames(linkedTreeJoinGames);
        ArrayList<Game> rejoinGames = Game.buildGames(linkedTreeRejoinGames);
        DataManager.getSINGLETON().getGameIndex().setJoinGameIndex(joinGames);
        DataManager.getSINGLETON().getGameIndex().setRejoinGameIndex(rejoinGames);
        MyBaseActivity baseActivity = (MyBaseActivity) getCurrentActivity();
        baseActivity.moveToJoin();
    }

    public void start() {
        Log.i("GAME_CONTROLLER", "Calling Start");
        LobbyActivity activity = (LobbyActivity) getCurrentActivity();
        activity.moveToGame();
    }

    public void setup(List<Player> players) {
        DataManager.SINGLETON.getGame().setupPlayers(players);
        GameRoomActivity activity = (GameRoomActivity) getCurrentActivity();
    }

    public void errorSetup() {
        GameRoomActivity activity = (GameRoomActivity) getCurrentActivity();
        activity.setupError();
    }

    public void errorLeave(Throwable t){
        Log.i("GameController", "Couldn't leave game");
    }

}
