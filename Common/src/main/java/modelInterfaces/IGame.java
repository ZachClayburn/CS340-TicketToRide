package modelInterfaces;

import java.util.UUID;

public interface IGame {
    UUID gameID = null;
    String groupName = "";
    int numPlayer = 0;
    int maxPlayer = 5;

    void setGameID(UUID gameID);
    UUID getGameID();
    void setGroupName(String groupName);

    String getGroupName();
    void setNumPlayer(int numPlayer);

    int getNumPlayer();
}
