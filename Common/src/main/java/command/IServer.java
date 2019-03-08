package command;

import com.tickettoride.models.Password;
import com.tickettoride.models.Username;

public interface IServer {
    void login(Username user, Password pass);
    void register(Username user, Password pass);
    void createGame(String name, int numPlayers);
    void joinGame();
}
