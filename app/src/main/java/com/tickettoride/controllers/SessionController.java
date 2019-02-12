package com.tickettoride.controllers;
import android.util.Log;

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

    public void create(UUID sessionId, ArrayList<LinkedTreeMap> linkedTreeGames) {
        Session session = new Session(sessionId);
        DataManager.getSINGLETON().setSession(session);
        ArrayList<Game> games = Game.buildGames(linkedTreeGames);
        DataManager.getSINGLETON().getGameIndex().setGames(games);
        Log.i("SessionController", DataManager.getSINGLETON().getGameIndex().getGameIndex().toString());
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
