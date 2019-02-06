package modelInterfaces;

public interface IGame {
    String gameID = "";
    String groupName = "";
    int numPlayer = 0;

    void setGameID(String gameID);
    String getGameID();
    void setGroupName(String groupName);

    String getGroupName();
    void setNumPlayer(int numPlayer);

    int getNumPlayer();
}