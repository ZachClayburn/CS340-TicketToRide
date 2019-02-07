package com.tickettoride.facades;
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
            User user = new User(username, password);
            Command command = buildCommandFromParameters(CONTROLLER_NAME, "create", user);
            sendResponseToOne(roomID, command);
        } catch (Throwable t) {
            Command command = buildCommandFromParameters(CONTROLLER_NAME, "error", t);
            sendResponseToOne(roomID, command);
        }
    }
}
