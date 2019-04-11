package com.tickettoride.facades;

import com.tickettoride.command.ServerCommunicator;
import com.tickettoride.database.DatabaseProvider;
import com.tickettoride.database.interfaces.IDatabase;
import com.tickettoride.database.interfaces.IUserDAO;
import com.tickettoride.facades.helpers.GameFacadeHelper;
import com.tickettoride.models.*;
import com.tickettoride.models.idtypes.SessionID;
import command.Command;
import exceptions.DatabaseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.UUID;

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
            ArrayList<Game> games = GameFacadeHelper.getSingleton().allGames();
            ArrayList<Game> joinGames = GameFacadeHelper.getSingleton().determineJoinGames(user, games);
            ArrayList<Game> rejoinGames = GameFacadeHelper.getSingleton().determineRejoinGames(user, games);
            Command command = new Command(CONTROLLER_NAME, "create", session.getSessionID(), user.getUserID(), joinGames, rejoinGames);
            ServerCommunicator.getINSTANCE().moveToMainLobby(connID);
            sendResponseToOne(connID, command);
        } catch (Throwable throwable) {
            logger.error(throwable.getMessage(), throwable);
            Command command = new Command(CONTROLLER_NAME, "createError");
            sendResponseToOne(connID, command);
        }
    }

    public User find_user(Username username, Password password) throws DatabaseException {
        try (IDatabase IDatabase = DatabaseProvider.getDatabase()) {
            IUserDAO dao = IDatabase.getUserDAO();
            return dao.getUser(username, password);
        }
    }

    public User find_user(Session session) throws DatabaseException {
        SessionID sessionID = session.getSessionID();
        return find_user(sessionID);
    }

    public User find_user(SessionID sessionID) throws DatabaseException {
        try (IDatabase db = DatabaseProvider.getDatabase()) {
            return db.getUserDAO().getUserBySessionID(sessionID);
        }
    }


    public User create_user(Username username, Password password) throws DatabaseException {
        try (IDatabase IDatabase = DatabaseProvider.getDatabase()) {
            IUserDAO dao = IDatabase.getUserDAO();
            User user = new User(username, password);
            dao.addUser(user);
            IDatabase.commit();
            return user;
        }
    }
}
