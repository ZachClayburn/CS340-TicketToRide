package com.tickettoride.facades;
import com.tickettoride.command.ServerCommunicator;
import com.tickettoride.database.Database;
import exceptions.DatabaseException;
import com.tickettoride.database.SessionDAO;
import com.tickettoride.models.Game;
import com.tickettoride.models.Player;
import com.tickettoride.models.Session;
import com.tickettoride.models.User;

import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;
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

    public void create(UUID connID, Username username, Password password) {
        try {
            User user = UserFacade.getSingleton().find_user(username, password);
            Session session = create_session(user);
            ArrayList<Game> games = GameFacade.getSingleton().allGames();
            ArrayList<Game> joinGames = GameFacade.getSingleton().determineJoinGames(user, games);
            ArrayList<Game> rejoinGames = GameFacade.getSingleton().determineRejoinGames(user, games);
            Command command = new Command(CONTROLLER_NAME, "create", session.getSessionID(), user.getUserID(), joinGames, rejoinGames);
            ServerCommunicator.getINSTANCE().moveToMainLobby(connID);
            sendResponseToOne(connID, command);
        } catch (Throwable throwable) {
            logger.error(throwable.getMessage(), throwable);
            Command command = new Command(CONTROLLER_NAME, "loginError");
            sendResponseToOne(connID, command);
        }
    }

    public Session create_session(User user) throws DatabaseException {
        try (Database database = new Database()) {
            Session session = new Session(user);
            SessionDAO dao = database.getSessionDAO();
            dao.createSession(session);
            database.commit();
            return session;
        }
    }

    public void delete(UUID connID, UUID sessionID) throws DatabaseException, SQLException {
        try (Database database = new Database()) {
            ServerCommunicator.getINSTANCE().moveToLogin(connID);
            SessionDAO dao = database.getSessionDAO();
            dao.deleteSession(sessionID);
            database.commit();
        }
    }

    public Session find_session(UUID sessionID) throws DatabaseException {
        try (Database database = new Database()) {
            SessionDAO dao = database.getSessionDAO();
            return dao.findSession(sessionID);
        }
    }
}
