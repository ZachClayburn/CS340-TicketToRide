package com.tickettoride.facades;

import com.tickettoride.command.ServerCommunicator;
import com.tickettoride.models.Session;
import com.tickettoride.models.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import command.Command;
import modelAttributes.Password;
import modelAttributes.Username;

public class SessionFacade {
    public static SessionFacade SINGLETON = new SessionFacade();

    public Session create(Username username, Password password) {
        User user = User.findBy(username, password);
        Session session = new Session(user);
        List<Object> parameters = new ArrayList(Arrays.asList(session));
        Command command = new Command("SessionController", "create", parameters);
        ServerCommunicator.SINGLETON.send(command);
    }
}
