package com.tickettoride.controllers.helpers;

import com.google.gson.internal.LinkedTreeMap;
import com.tickettoride.activities.CreateGameActivity;
import com.tickettoride.activities.JoinGameActivity;
import com.tickettoride.clientModels.DataManager;
import com.tickettoride.controllers.BaseController;
import com.tickettoride.models.Game;
import com.tickettoride.models.Hand;
import com.tickettoride.models.Player;
import com.tickettoride.models.Session;
import com.tickettoride.models.idtypes.PlayerID;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GameControllerHelper extends BaseController {
    private static GameControllerHelper SINGLETON = new GameControllerHelper();
    public static GameControllerHelper getSingleton() { return SINGLETON; }
    private GameControllerHelper() {}

    public List<Player> buildPlayerList(List<LinkedTreeMap<String, Object>> players) {
        List<Player> playerList = new ArrayList<>();
        for (LinkedTreeMap<String, Object> playerMap : players) {
            Player player = new Player(playerMap);
            playerList.add(player);
        }
        return playerList;
    }

    public void setPlayerInfo(List<Player> playerList) {
        for (Player player : playerList) {
            if (player.getUserID().equals(DataManager.SINGLETON.getSession().getUserID())) {
                DataManager.SINGLETON.setPlayer(player);
            }
        }
    }

    public void createGameOnJoinActivity(Game game) throws ClassCastException {
        JoinGameActivity joinGameActivity = (JoinGameActivity) getCurrentActivity();
        DataManager.getSINGLETON().getGameIndex().addJoinGame(game);
        joinGameActivity.updateUI();
    }

    public void createGameOnCreateGameActivity(Game game, PlayerID playerID) throws ClassCastException {
        CreateGameActivity createGameActivity = (CreateGameActivity) getCurrentActivity();
        Session session = DataManager.getSINGLETON().getSession();
        Player player = new Player(session.getUserID(), game.getGameID(), playerID);
        DataManager.SINGLETON.setGame(game);
        DataManager.SINGLETON.setPlayer(player);
        DataManager.SINGLETON.getGameIndex().addRejoinGame(game);
        createGameActivity.moveToLobbyCreate(game);
    }
}
