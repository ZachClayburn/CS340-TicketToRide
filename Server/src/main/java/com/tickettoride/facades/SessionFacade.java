package com.tickettoride.facades;

import com.tickettoride.database.Database;
import com.tickettoride.database.SessionDAO;
import com.tickettoride.models.Session;
import com.tickettoride.models.User;
import java.util.UUID;

import command.Command;
import modelAttributes.Password;
import modelAttributes.Username;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SessionFacade extends BaseFacade {

    public static final String CONTROLLER_NAME = "SessionController";
    private static SessionFacade SINGLETON = new SessionFacade();
    public static SessionFacade getSingleton() {
        return SINGLETON;
    }

    public void create(UUID roomID, Username username, Password password) {
        try {
            User user = UserFacade.getSingleon().find_user(username, password);
            Session session = create_session(user);
            Command command = new Command(CONTROLLER_NAME, "create", session);
            sendResponseToOne(roomID, command);
        } catch (Throwable throwable) {
            Command command = new Command(CONTROLLER_NAME, "error", throwable);
            sendResponseToOne(roomID, command);
        }
    }

    public Session create_session(User user) throws Database.DatabaseException {
        try (Database database = new Database()) {
            Session session = new Session(user);
            SessionDAO dao = database.getSessionDAO();
            dao.createSession(session);
            database.commit();
            return session;
        }
    }
}
