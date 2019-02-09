package com.tickettoride.controllers;

public class UserController {
    private static UserController SINGLETON = new UserController();
    public static UserController getSingleton() { return SINGLETON; }
    private UserController() {}
}
