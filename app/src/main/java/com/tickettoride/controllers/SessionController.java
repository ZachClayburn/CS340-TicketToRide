package com.tickettoride.controllers;

import com.google.gson.internal.LinkedTreeMap;
import com.tickettoride.activities.LoginActivity;
import com.tickettoride.clientModels.DataManager;
import com.tickettoride.clientModels.Game;
import com.tickettoride.clientModels.Session;

import java.util.ArrayList;
import java.util.UUID;

public class SessionController extends BaseController {
    private static SessionController SINGLETON = new SessionController();
    public static SessionController getSingleton() { return SINGLETON; }
    private SessionController() {}

    public void create(UUID sessionID, UUID userID, ArrayList<LinkedTreeMap> linkedTreeJoinGames, ArrayList<LinkedTreeMap> linkedTreeRejoinGames) {
        Session session = new Session(sessionID, userID);
        DataManager.getSINGLETON().setSession(session);
        ArrayList<Game> joinGames = Game.buildGames(linkedTreeJoinGames);
        ArrayList<Game> rejoinGames = Game.buildGames(linkedTreeRejoinGames);
        DataManager.getSINGLETON().getGameIndex().setJoinGameIndex(joinGames);
        DataManager.getSINGLETON().getGameIndex().setRejoinGameIndex(rejoinGames);
        LoginActivity loginActivity = (LoginActivity) getCurrentActivity();
        loginActivity.moveToJoin();
    }

    public void createError() {
        LoginActivity loginActivity = (LoginActivity) getCurrentActivity();
        loginActivity.createError();
    }

    public void loginError() {
        LoginActivity loginActivity = (LoginActivity) getCurrentActivity();
        loginActivity.loginError();
    }
}
