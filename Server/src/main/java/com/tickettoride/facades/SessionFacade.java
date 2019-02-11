package com.tickettoride.facades;

import com.tickettoride.command.ServerCommunicator;
import com.tickettoride.database.Database;
import com.tickettoride.database.SessionDAO;
import com.tickettoride.models.Session;
import com.tickettoride.models.User;

import java.sql.SQLException;
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
    private static Logger logger = LogManager.getLogger(SessionFacade.class.getName());

    public void create(UUID roomID, Username username, Password password) {
        try {
            User user = UserFacade.getSingleton().find_user(username, password);
            Session session = create_session(user);
            Command command = new Command(CONTROLLER_NAME, "create", session.getSessionID());
            sendResponseToOne(roomID, command);
        } catch (Throwable throwable) {
            logger.error(throwable.getMessage(), throwable);
            Command command = new Command(CONTROLLER_NAME, "error", throwable);
            sendResponseToOne(roomID, command);
        }
    }

    public Session create_session(User user) throws Database.DatabaseException, SQLException {
        try (Database database = new Database()) {
            Session session = new Session(user);
            SessionDAO dao = database.getSessionDAO();
            dao.createSession(session);
            database.commit();
            return session;
        }
    }
}
