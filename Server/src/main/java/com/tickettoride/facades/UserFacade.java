package com.tickettoride.facades;

import com.tickettoride.command.ServerCommunicator;
import com.tickettoride.database.Database;
import com.tickettoride.database.UserDAO;
import com.tickettoride.models.Session;
import com.tickettoride.models.User;

import java.sql.SQLException;
import java.util.UUID;
import command.Command;
import modelAttributes.Password;
import modelAttributes.Username;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserFacade extends BaseFacade {
    private static UserFacade SINGLETON = new UserFacade();
    private static String CONTROLLER_NAME = "SessionController";
    public static UserFacade getSingleton() {
        return SINGLETON;
    }
    private UserFacade() {}
    private static Logger logger = LogManager.getLogger(UserFacade.class.getName());

    public void create(UUID connID, Username username, Password password) {
        try {
            User user = create_user(username , password);
            Session session = SessionFacade.getSingleton().create_session(user);
            Command command = new Command(CONTROLLER_NAME, "create", session.getSessionID());
            ServerCommunicator.getINSTANCE().moveToMainLobby(connID);
            sendResponseToOne(connID, command);
        } catch (Throwable throwable) {
            logger.error(throwable.getMessage(), throwable);
            Command command = new Command(CONTROLLER_NAME, "createError");
            sendResponseToOne(connID, command);
        }
    }

    public User find_user(Username username, Password password) throws Database.DatabaseException {
        try (Database database = new Database()) {
            UserDAO dao = database.getUserDAO();
            return dao.getUser(username, password);
        }
    }

    public User find_user(Session session) throws Database.DatabaseException {
        try (Database database = new Database()) {
            UUID sessionID = session.getSessionID();
            UserDAO dao = database.getUserDAO();
            return dao.getUserBySessionID(sessionID);
        }
    }

    public User create_user(Username username, Password password) throws Database.DatabaseException, SQLException {
        try (Database database = new Database()) {
            UserDAO dao = database.getUserDAO();
            User user = new User(username, password);
            dao.addUser(user);
            database.commit();
            return user;
        }
    }
}
