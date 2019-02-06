package com.tickettoride.facades;

        import com.tickettoride.command.ServerCommunicator;
        import com.tickettoride.models.User;

        import java.util.ArrayList;
        import java.util.Arrays;
        import java.util.List;
        import java.util.UUID;

        import command.Command;
        import command.Response;
        import modelAttributes.Password;
        import modelAttributes.Username;

public class UserFacade extends BaseFacade {
    public static UserFacade SINGLETON = new UserFacade();

    public void create(UUID roomID, Username username, Password password) {
        try {
            User user = new User(username, password);
            List<Object> parameters = new ArrayList();
            parameters.add(user);
            Command command = new Command("UserController", "create", parameters);
            sendResponseToOne(roomID, command);
        } catch (Throwable t) {
            sendResponseToOne(roomID, t);
        }
    }
}
