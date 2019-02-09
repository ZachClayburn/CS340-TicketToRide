package com.tickettoride.facades;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LobbyFacade {
    private static LobbyFacade SINGLETON = new LobbyFacade();
    public static LobbyFacade getSingleton() {
        return SINGLETON;
    }
    private LobbyFacade() {}

}
