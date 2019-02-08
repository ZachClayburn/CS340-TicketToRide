package modelInterfaces;

public interface ILobby {
    String gameID = "";
    String groupName = "";
    int numPlayer = 0;
    boolean isGameStarted = false;

    void setGameID(String gameID);
    String getGameID();

    void setGroupName(String groupName);
    String getGroupName();

    void setNumPlayer(int numPlayer);
    int getNumPlayer();
}
