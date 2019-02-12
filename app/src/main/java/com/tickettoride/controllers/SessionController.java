package com.tickettoride.controllers;
import com.google.gson.internal.LinkedTreeMap;
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

    public void create(UUID sessionId, ArrayList<LinkedTreeMap> linkedTreeGames) {
        Session session = new Session(sessionId);
        DataManager.getSINGLETON().setSession(session);
        ArrayList<Game> games = buildGames(linkedTreeGames);
        DataManager.getSINGLETON().getGameIndex().setGames(games);
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

    public ArrayList<Game> buildGames(ArrayList<LinkedTreeMap> gameMap) {
        ArrayList<Game> games = new ArrayList<>();
        for (LinkedTreeMap singleGameHash : gameMap) {
            Game game = new Game(singleGameHash);
            games.add(game);
        }
        return games;
    }
}
