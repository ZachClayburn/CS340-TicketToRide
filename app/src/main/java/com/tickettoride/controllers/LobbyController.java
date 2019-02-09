package com.tickettoride.controllers;

public class LobbyController {
    private static LobbyController SINGLETON = new LobbyController();
    public static LobbyController getSingleton() { return SINGLETON; }
    private LobbyController() {}
}
