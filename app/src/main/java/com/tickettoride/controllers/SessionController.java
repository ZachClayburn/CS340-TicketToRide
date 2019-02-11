package com.tickettoride.controllers;
import com.tickettoride.activities.LoginActivity;
import com.tickettoride.clientModels.DataManager;
import com.tickettoride.clientModels.Game;
import com.tickettoride.clientModels.Session;

import java.util.ArrayList;
import java.util.UUID;

import modelInterfaces.IGame;

public class SessionController extends BaseController {
    private static SessionController SINGLETON = new SessionController();
    public static SessionController getSingleton() { return SINGLETON; }
    private SessionController() {}

    public void create(UUID sessionId, ArrayList<Game> games) {
        Session session = new Session(sessionId);
        DataManager.getSINGLETON().setSession(session);
//        DataManager.getSINGLETON().getGameIndex().addGames(games);
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
