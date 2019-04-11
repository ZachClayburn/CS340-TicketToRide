package com.tickettoride.facades;

import com.tickettoride.command.ServerCommunicator;
import com.tickettoride.database.DatabaseProvider;
import com.tickettoride.database.interfaces.IDatabase;
import com.tickettoride.database.interfaces.ISessionDAO;
import com.tickettoride.facades.helpers.GameFacadeHelper;
import com.tickettoride.models.*;
import com.tickettoride.models.idtypes.SessionID;
import command.Command;
import exceptions.DatabaseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

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
            ArrayList<Game> games = GameFacadeHelper.getSingleton().allGames();
            ArrayList<Game> joinGames = GameFacadeHelper.getSingleton().determineJoinGames(user, games);
            ArrayList<Game> rejoinGames = GameFacadeHelper.getSingleton().determineRejoinGames(user, games);
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
        try (IDatabase IDatabase = DatabaseProvider.getDatabase()) {
            Session session = new Session(user);
            ISessionDAO dao = IDatabase.getSessionDAO();
            dao.createSession(session);
            IDatabase.commit();
            return session;
        }
    }

    public void delete(UUID connID, SessionID sessionID) throws DatabaseException, SQLException {
        try (IDatabase IDatabase = DatabaseProvider.getDatabase()) {
            ServerCommunicator.getINSTANCE().moveToLogin(connID);
            ISessionDAO dao = IDatabase.getSessionDAO();
            dao.deleteSession(sessionID);
            IDatabase.commit();
        }
    }

    public Session find_session(SessionID sessionID) throws DatabaseException {
        try (IDatabase IDatabase = DatabaseProvider.getDatabase()) {
            ISessionDAO dao = IDatabase.getSessionDAO();
            return dao.findSession(sessionID);
        }
    }
}
