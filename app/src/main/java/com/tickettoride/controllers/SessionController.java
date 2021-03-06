package com.tickettoride.controllers;

import com.google.gson.internal.LinkedTreeMap;
import com.tickettoride.activities.LoginActivity;
import com.tickettoride.clientModels.DataManager;
import com.tickettoride.models.Session;
import com.tickettoride.models.Game;
import com.tickettoride.models.idtypes.SessionID;
import com.tickettoride.models.idtypes.UserID;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SessionController extends BaseController {
    private static SessionController SINGLETON = new SessionController();
    public static SessionController getSingleton() { return SINGLETON; }
    private SessionController() {}

    public void create(SessionID sessionID, UserID userID, ArrayList<LinkedTreeMap> linkedTreeJoinGames, ArrayList<LinkedTreeMap> linkedTreeRejoinGames) {
        Session session = new Session(sessionID, userID);
        DataManager.getSINGLETON().setSession(session);
        ArrayList<Game> joinGames = Game.buildGames((List) linkedTreeJoinGames);
        ArrayList<Game> rejoinGames = Game.buildGames((List) linkedTreeRejoinGames);
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
