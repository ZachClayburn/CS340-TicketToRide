package com.tickettoride.controllers;
import android.util.Log;

import com.google.gson.internal.LinkedTreeMap;
import com.tickettoride.activities.*;
import com.tickettoride.clientModels.DataManager;
import com.tickettoride.models.Game;
import com.tickettoride.models.Player;
import com.tickettoride.controllers.helpers.GameControllerHelper;
import com.tickettoride.models.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GameController extends BaseController {
    private static GameController SINGLETON = new GameController();
    public static GameController getSingleton() { return SINGLETON; }
    private GameController() {}

    public void create(UUID playerID, UUID sessionID, UUID gameID, String groupName, Integer numPlayer, Integer maxPlayer, Boolean isStarted) {
        Game game = new Game(gameID, groupName, numPlayer, maxPlayer, isStarted);
        try { createGameOnJoinActivity(game); return; } catch (ClassCastException e) { }
        try { createGameOnCreateGameActivity(game, playerID); return; } catch (ClassCastException e) {
            Log.i("GAME_CONTROLLER", e.getMessage(), e);
        }
    }

    private void createGameOnJoinActivity(Game game) throws ClassCastException {
        JoinGameActivity joinGameActivity = (JoinGameActivity) getCurrentActivity();
        DataManager.getSINGLETON().getGameIndex().addJoinGame(game);
        joinGameActivity.updateUI();
    }

    private void createGameOnCreateGameActivity(Game game, UUID playerID) throws ClassCastException {
        CreateGameActivity createGameActivity = (CreateGameActivity) getCurrentActivity();
        Session session = DataManager.getSINGLETON().getSession();
        Player player = new Player(session.getUserID(), game.getGameID(), playerID);
        DataManager.SINGLETON.setGame(game);
        DataManager.SINGLETON.setPlayer(player);
        DataManager.SINGLETON.getGameIndex().addRejoinGame(game);
        createGameActivity.moveToLobbyCreate(game);
    }

    public void join(UUID playerID, UUID sessionID, UUID gameID, String groupName, Integer numPlayer, Integer maxPlayer, Boolean isStarted) {
        Session session = DataManager.getSINGLETON().getSession();
        Player player = new Player(session.getUserID(), gameID, playerID);
        Game game = new Game(gameID, groupName, numPlayer, maxPlayer, isStarted);
        // If user is the one joining game and becoming a player
        if (DataManager.SINGLETON.getSession().getSessionID().equals(sessionID)) {
            DataManager.SINGLETON.setPlayer(player);
            DataManager.SINGLETON.setGame(game);
            JoinGameActivity joinGameActivity = (JoinGameActivity) getCurrentActivity();
            joinGameActivity.moveToLobbyJoin(game);
        }
        else if (DataManager.SINGLETON.getGame().getGameID().equals(game.getGameID())) {
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
        Session session = DataManager.getSINGLETON().getSession();
        Player player = new Player(session.getUserID(), gameID, playerID);
        Game game = new Game(gameID, groupName, numPlayer, maxPlayer, isStarted);
        if (DataManager.SINGLETON.getSession().getSessionID().equals(sessionID)) {
            DataManager.SINGLETON.setPlayer(player);
            DataManager.SINGLETON.setGame(game);
            JoinGameActivity joinGameActivity = (JoinGameActivity) getCurrentActivity();
            joinGameActivity.moveToLobbyJoin(game);
        } else if (DataManager.SINGLETON.getPlayer().getGameID().equals(game.getGameID())) {
            LobbyActivity lobbyActivity = (LobbyActivity) getCurrentActivity();
            lobbyActivity.updateUI(game);
        }
    }

    public void errorCreate(String errorMessage) {
        CreateGameActivity createGameActivity = (CreateGameActivity) getCurrentActivity();
        createGameActivity.createError();
    }

    public void errorJoin() {
        JoinGameActivity joinGameActivity = (JoinGameActivity) getCurrentActivity();
        joinGameActivity.JoinError();
    }

    public void leave(ArrayList<Map<String, Object>> linkedTreeJoinGames, ArrayList<Map<String, Object>> linkedTreeRejoinGames) {
        ArrayList<Game> joinGames = Game.buildGames(linkedTreeJoinGames);
        ArrayList<Game> rejoinGames = Game.buildGames(linkedTreeRejoinGames);
        DataManager.getSINGLETON().getGameIndex().setJoinGameIndex(joinGames);
        DataManager.getSINGLETON().getGameIndex().setRejoinGameIndex(rejoinGames);
//        JoinGameActivity joinGameActivity = (JoinGameActivity) getCurrentActivity();
//        joinGameActivity.updateUI();
    }

    public void start(ArrayList<LinkedTreeMap<String, Object>> players) {
        Log.i("GAME_CONTROLLER", "Calling Start");
        List<Player> playerList = GameControllerHelper.getSingleton().buildPlayerList(players);
        DataManager.SINGLETON.setGamePlayers(playerList);
        setPlayerInfo(playerList);
        LobbyActivity activity = (LobbyActivity) getCurrentActivity();
        activity.moveToGame();
    }

    public void setPlayerInfo(List<Player> playerList) {
        for (Player player : playerList) {
            if (player.getPlayerID().equals(DataManager.SINGLETON.getPlayer().getPlayerID())) {
                DataManager.SINGLETON.setPlayer(player);
            }
        }
    }


    public void errorSetup() {
        GameRoomActivity activity = (GameRoomActivity) getCurrentActivity();
        activity.setupError();
    }

    public void errorLeave(Throwable t){
        Log.i("GameController", "Couldn't leave game");
    }

}
