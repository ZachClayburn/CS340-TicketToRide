package com.tickettoride.clientModels;

public class DataManager {
    
    public static DataManager SINGLETON=new DataManager();
    private User user;
    private Player player;
    private GameIndex gameIndex;
    private GameInfo gameInfo;
    
    
    private DataManager(){
        user=User.SINGLETON;
    }

    public static DataManager getSINGLETON() {
        return SINGLETON;
    }

    public GameIndex getGameIndex() {
        return gameIndex;
    }

    public GameInfo getGameInfo() {
        return gameInfo;
    }

    public Player getPlayer() {
        return player;
    }

    public User getUser() {
        return user;
    }

    public void setGameIndex(GameIndex gameIndex) {
        this.gameIndex = gameIndex;
    }

    public void setGameInfo(GameInfo gameInfo) {
        this.gameInfo = gameInfo;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
