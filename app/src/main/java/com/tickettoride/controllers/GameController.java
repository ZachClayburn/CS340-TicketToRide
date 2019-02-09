package com.tickettoride.controllers;

import com.tickettoride.clientModels.GameIndex;

public class GameController  {
    private static GameController SINGLETON = new GameController();
    public static GameController getSingleton() { return SINGLETON; }
    private GameController() {}
}
