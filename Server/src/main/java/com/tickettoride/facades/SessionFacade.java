package com.tickettoride.facades;

import com.tickettoride.command.ServerCommunicator;
import com.tickettoride.models.Session;
import com.tickettoride.models.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import command.Command;
import command.Response;
import modelAttributes.Password;
import modelAttributes.Username;

public class SessionFacade extends BaseFacade {

    public static final String CONTROLLER_NAME = "SessionController";
    public static SessionFacade SINGLETON = new SessionFacade();

    public void create(UUID roomID, Username username, Password password) {
        try {
            User user = User.findBy(username, password);
            Session session = new Session(user);
            Command command = new Command(CONTROLLER_NAME, "create", session);
            sendResponseToOne(roomID, command);
        } catch (Throwable throwable) {
            Command command = new Command(CONTROLLER_NAME, "error", throwable);
            sendResponseToOne(roomID, command);
        }
    }
}
