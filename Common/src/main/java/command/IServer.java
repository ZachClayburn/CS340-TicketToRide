package command;

import modelAttributes.*;

public interface IServer {
    void login(Username user, Password pass);
    void register(Username user, Password pass);
    void createGame(String name, int numPlayers);
    void joinGame();
}
