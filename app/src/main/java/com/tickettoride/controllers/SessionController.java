package com.tickettoride.controllers;

import com.tickettoride.facadeProxies.SessionFacadeProxy;

public class SessionController {
    private static SessionController SINGLETON = new SessionController();
    public static SessionController getSingleton() { return SINGLETON; }
    private SessionController() {}
}
