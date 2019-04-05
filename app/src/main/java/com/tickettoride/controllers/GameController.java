package com.tickettoride.controllers;
import android.util.Log;

import com.google.gson.internal.LinkedTreeMap;
import com.tickettoride.activities.*;
import com.tickettoride.clientModels.ClientRoute;
import com.tickettoride.clientModels.DataManager;
import com.tickettoride.clientModels.helpers.PlayerStateHelper;
import com.tickettoride.facadeProxies.ChatFacadeProxy;
import com.tickettoride.facadeProxies.TrainCardFacadeProxy;
import com.tickettoride.facadeProxies.HistoryFacadeProxy;
import com.tickettoride.models.ClaimRouteState;
import com.tickettoride.models.DestinationCard;
import com.tickettoride.models.DrawDestinationState;
import com.tickettoride.models.DrawTrainCardsState;
import com.tickettoride.models.FinalDrawDestinationCardState;
import com.tickettoride.models.FinalDrawTrainCardsState;
import com.tickettoride.models.FinalPlaceTrainsState;
import com.tickettoride.models.Game;
import com.tickettoride.models.Hand;
import com.tickettoride.models.Player;
import com.tickettoride.controllers.helpers.GameControllerHelper;
import com.tickettoride.models.PlayerState;
import com.tickettoride.models.Session;
import com.tickettoride.models.TrainCard;
import com.tickettoride.models.TrainCardDeck;
import com.tickettoride.models.idtypes.GameID;
import com.tickettoride.models.idtypes.PlayerID;
import com.tickettoride.models.idtypes.SessionID;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class GameController extends BaseController {
    private static GameController SINGLETON = new GameController();
    public static GameController getSingleton() { return SINGLETON; }
    private GameController() {}

    public void setGameState(DrawTrainCardsState playerState) {
        if (playerState.getPlayerID().equals(DataManager.getSINGLETON().getPlayer().getPlayerID())) {
            DataManager.SINGLETON.setPlayerState(playerState);
            GameRoomActivity activity = (GameRoomActivity) getCurrentActivity();
            activity.applyPlayerState();
        }
    }

    public void setGameState(FinalDrawTrainCardsState playerState) {
        if (playerState.getPlayerID().equals(DataManager.getSINGLETON().getPlayer().getPlayerID())) {
            DataManager.SINGLETON.setPlayerState(playerState);
            GameRoomActivity activity = (GameRoomActivity) getCurrentActivity();
            MapFragment mf = activity.getMapFragment();
            PlayerStateHelper.getSingleton().applyPlayerState(playerState, mf);
        }
    }

    public void setGameState(DrawDestinationState playerState) {
        if (playerState.getPlayerID().equals(DataManager.getSINGLETON().getPlayer().getPlayerID())) {
            DataManager.SINGLETON.setPlayerState(playerState);
            GameRoomActivity activity = (GameRoomActivity) getCurrentActivity();
            MapFragment mf = activity.getMapFragment();
            PlayerStateHelper.getSingleton().applyPlayerState(playerState, mf);
        }
    }

    public void setGameState(FinalDrawDestinationCardState playerState) {
        if (playerState.getPlayerID().equals(DataManager.getSINGLETON().getPlayer().getPlayerID())) {
            DataManager.SINGLETON.setPlayerState(playerState);
            GameRoomActivity activity = (GameRoomActivity) getCurrentActivity();
            MapFragment mf = activity.getMapFragment();
            PlayerStateHelper.getSingleton().applyPlayerState(playerState, mf);
        }
    }

    public void setGameState(ClaimRouteState playerState) {
        if (playerState.getPlayerID().equals(DataManager.getSINGLETON().getPlayer().getPlayerID())) {
            DataManager.SINGLETON.setPlayerState(playerState);
            GameRoomActivity activity = (GameRoomActivity) getCurrentActivity();
            MapFragment mf = activity.getMapFragment();
            PlayerStateHelper.getSingleton().applyPlayerState(playerState, mf);
        }
    }

    public void create(PlayerID playerID, SessionID sessionID, GameID gameID, String groupName, Integer numPlayer, Integer maxPlayer, Boolean isStarted) {
        Game game = new Game(gameID, groupName, numPlayer, maxPlayer, isStarted);
        try { GameControllerHelper.getSingleton().createGameOnJoinActivity(game); return; } catch (ClassCastException e) { }
        try { GameControllerHelper.getSingleton().createGameOnCreateGameActivity(game, playerID); return; } catch (ClassCastException e) {
            Log.i("GAME_CONTROLLER", e.getMessage(), e);
        }
    }

    public void join(PlayerID playerID, SessionID sessionID, GameID gameID, String groupName, Integer numPlayer, Integer maxPlayer) {
        Session session = DataManager.getSINGLETON().getSession();
        Player player = new Player(session.getUserID(), gameID, playerID);
        Game game = new Game(gameID, groupName, numPlayer, maxPlayer, false);
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
        else {
            JoinGameActivity joinGameActivity = (JoinGameActivity) getCurrentActivity();
            joinGameActivity.updateUI();
        }
    }
    
    public void rejoinNotStarted(PlayerID playerID, SessionID sessionID, GameID gameID, String groupName, Integer numPlayer, Integer maxPlayer){
        Session session = DataManager.getSINGLETON().getSession();
        Player player = new Player(session.getUserID(), gameID, playerID);
        Game game = new Game(gameID, groupName, numPlayer, maxPlayer, false);
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

    public void rejoinIsStarted(SessionID sessionID, GameID gameID, String groupName, ArrayList<LinkedTreeMap<String, Object>> playersMap, ArrayList<LinkedTreeMap> playerHandMap, Integer deckCount,
                                ArrayList<LinkedTreeMap<String, Object>> routes, Integer turn, ArrayList<LinkedTreeMap<String, Object>> playerStateMap) throws ClassNotFoundException,
            NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        if (DataManager.SINGLETON.getSession().getSessionID().equals(sessionID)) {
            List<Player> players = GameControllerHelper.getSingleton().buildPlayerList(playersMap);
            List<DestinationCard> playerDestinationCards = DestinationCard.unGsonCards(playerHandMap);
            List<PlayerState> playerStates = PlayerState.buildPlayerStateList(playerStateMap);
            Game game = new Game(gameID, groupName,true);
            GameControllerHelper.getSingleton().setPlayerInfo(players);
            DataManager.SINGLETON.setGame(game);
            DataManager.SINGLETON.setGamePlayers(players);
            DataManager.SINGLETON.getPlayerHand();
            DataManager.SINGLETON.getPlayerHand().setDestinationCards(playerDestinationCards);
            DataManager.SINGLETON.setDestinationCardDeckSize(deckCount);
            DataManager.SINGLETON.setTurn(turn);
            DataManager.SINGLETON.setClientRoutes(ClientRoute.buildClientRoutes(routes));
            List<PlayerState> playerStateList = PlayerState.buildPlayerStateList(playerStateMap);
            DataManager.SINGLETON.setCurrentPLayerState(playerStateList);
            JoinGameActivity joinGameActivity = (JoinGameActivity) getCurrentActivity();
            joinGameActivity.moveToGame();
            ChatFacadeProxy.SINGLETON.getChat(gameID);
            HistoryFacadeProxy.SINGLETON.getHistory(gameID);
            TrainCardFacadeProxy.SINGLETON.rejoin(gameID);
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
    }

    public void start(ArrayList<LinkedTreeMap<String, Object>> players, ArrayList<LinkedTreeMap<String, Object>> routes, Integer turn,
                      ArrayList<LinkedTreeMap<String, Object>> playerStateMap) throws ClassNotFoundException,
            NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Log.i("GAME_CONTROLLER", "Calling Start");
        List<Player> playerList = GameControllerHelper.getSingleton().buildPlayerList(players);
        DataManager.SINGLETON.setGamePlayers(playerList);
        GameControllerHelper.getSingleton().setPlayerInfo(playerList);
        DataManager.SINGLETON.setDestinationCardDeckSize(30);
        DataManager.SINGLETON.setClientRoutes(ClientRoute.buildClientRoutes(routes));
        DataManager.SINGLETON.setTurn(turn);
        List<PlayerState> playerStateList = PlayerState.buildPlayerStateList(playerStateMap);
        DataManager.SINGLETON.setCurrentPLayerState(playerStateList);
        LobbyActivity activity = (LobbyActivity) getCurrentActivity();
        activity.moveToGame();
        Game game = DataManager.getSINGLETON().getGame();
        ChatFacadeProxy.SINGLETON.getChat(game.getGameID());
        HistoryFacadeProxy.SINGLETON.getHistory(game.getGameID());
    }

    public void errorSetup() {
        GameRoomActivity activity = (GameRoomActivity) getCurrentActivity();
        activity.setupError();
    }

    public void finish(ArrayList<LinkedTreeMap<String, Object>> players,
                       ArrayList<LinkedTreeMap<String, Object>> longestPathWinners,
                       HashMap<String, Double> lostPoints) {
        List<Player> playerList = GameControllerHelper.getSingleton().buildPlayerList(players);
        DataManager.SINGLETON.setLongestPathWinners(
                GameControllerHelper.getSingleton().buildPlayerList(longestPathWinners)
        );
        DataManager.SINGLETON.setGamePlayers(playerList);
        DataManager.SINGLETON.setLostPoints(lostPoints);
        GameRoomActivity activity = (GameRoomActivity) getCurrentActivity();
        activity.moveToGameOver();
    }

    public void errorLeave(Throwable t){
        Log.i("GameController", "Couldn't leave game");
    }

}
