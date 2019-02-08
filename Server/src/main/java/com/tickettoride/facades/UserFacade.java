package com.tickettoride.facades;
        import com.tickettoride.database.Database;
        import com.tickettoride.database.SessionDAO;
        import com.tickettoride.database.UserDAO;
        import com.tickettoride.models.User;
        import java.util.UUID;
        import command.Command;
        import modelAttributes.Password;
        import modelAttributes.Username;

public class UserFacade extends BaseFacade {
    public static UserFacade SINGLETON = new UserFacade();
    public static String CONTROLLER_NAME = "UserController";

    public void create(UUID roomID, Username username, Password password) {
        try {
            User user = create_user(username , password);
            Command command = new Command(CONTROLLER_NAME, "create", user);
            sendResponseToOne(roomID, command);
        } catch (Throwable t) {
            Command command = new Command(CONTROLLER_NAME, "error", t);
            sendResponseToOne(roomID, command);
        }
    }

    public User find_user(Username username, Password password) throws Database.DatabaseException {
        Database database = new Database();
        UserDAO dao = database.getUserDAO();
        User user = dao.getUser(username, password);
        database.close();
        return user;
    }

    public User create_user(Username username, Password password) throws Database.DatabaseException {
        Database database = new Database();
        UserDAO dao = database.getUserDAO();
        User user = new User(username, password);
        dao.addUser(user);
        database.commit();
        database.close();
        return user;
    }
}
