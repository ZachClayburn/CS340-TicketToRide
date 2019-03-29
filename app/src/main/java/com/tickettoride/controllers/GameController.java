package com.tickettoride.controllers;
import android.util.Log;

import com.google.gson.internal.LinkedTreeMap;
import com.tickettoride.activities.*;
import com.tickettoride.clientModels.ClientRoute;
import com.tickettoride.clientModels.DataManager;
import com.tickettoride.facadeProxies.ChatFacadeProxy;
import com.tickettoride.facadeProxies.TrainCardFacadeProxy;
import com.tickettoride.facadeProxies.HistoryFacadeProxy;
import com.tickettoride.models.DestinationCard;
import com.tickettoride.models.Game;
import com.tickettoride.models.Hand;
import com.tickettoride.models.Player;
import com.tickettoride.controllers.helpers.GameControllerHelper;
import com.tickettoride.models.Session;
import com.tickettoride.models.TrainCard;
import com.tickettoride.models.TrainCardDeck;
import com.tickettoride.models.idtypes.GameID;
import com.tickettoride.models.idtypes.PlayerID;
import com.tickettoride.models.idtypes.SessionID;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GameController extends BaseController {
    private static GameController SINGLETON = new GameController();
    public static GameController getSingleton() { return SINGLETON; }
    private GameController() {}

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

    public void rejoinIsStarted(SessionID sessionID, GameID gameID,
                                String groupName, ArrayList<LinkedTreeMap<String, Object>> playersMap,
                                ArrayList<LinkedTreeMap> playerHandMap, Integer deckCount,
                                ArrayList<LinkedTreeMap<String, Object>> routes, Integer turn) {
        if (DataManager.SINGLETON.getSession().getSessionID().equals(sessionID)) {
            List<Player> players = GameControllerHelper.getSingleton().buildPlayerList(playersMap);
            List<DestinationCard> playerHand = DestinationCard.unGsonCards(playerHandMap);
            Game game = new Game(gameID, groupName,true);
            GameControllerHelper.getSingleton().setPlayerInfo(players);
            DataManager.SINGLETON.setGame(game);
            DataManager.SINGLETON.setGamePlayers(players);
            DataManager.SINGLETON.getPlayerHand().getDestinationCards().addAll(playerHand);
            DataManager.SINGLETON.setDestinationCardDeckSize(deckCount);
            DataManager.SINGLETON.setTurn(turn);
            DataManager.SINGLETON.setClientRoutes(ClientRoute.buildClientRoutes(routes));
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

    public void start(ArrayList<LinkedTreeMap<String, Object>> players, ArrayList<LinkedTreeMap<String, Object>> routes, Integer turn) {
        Log.i("GAME_CONTROLLER", "Calling Start");
        List<Player> playerList = GameControllerHelper.getSingleton().buildPlayerList(players);
        DataManager.SINGLETON.setGamePlayers(playerList);
        GameControllerHelper.getSingleton().setPlayerInfo(playerList);
        DataManager.SINGLETON.setDestinationCardDeckSize(30);
        DataManager.SINGLETON.setClientRoutes(ClientRoute.buildClientRoutes(routes));
        DataManager.SINGLETON.setTurn(turn);
        LobbyActivity activity = (LobbyActivity) getCurrentActivity();
        activity.moveToGame();
        Game game = DataManager.getSINGLETON().getGame();
        ChatFacadeProxy.SINGLETON.getChat(game.getGameID());
        HistoryFacadeProxy.SINGLETON.getHistory(game.getGameID());
        TrainCardFacadeProxy.SINGLETON.initialize(game.getGameID());
    }

    public void errorSetup() {
        GameRoomActivity activity = (GameRoomActivity) getCurrentActivity();
        activity.setupError();
    }

    public void errorLeave(Throwable t){
        Log.i("GameController", "Couldn't leave game");
    }

}
