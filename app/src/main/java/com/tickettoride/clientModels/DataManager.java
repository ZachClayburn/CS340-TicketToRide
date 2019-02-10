package com.tickettoride.clientModels;

public class DataManager {
    
    public static DataManager SINGLETON=new DataManager();
    private Session session;
    private Player player;
    private GameIndex gameIndex;
    private GameInfo curGame;
    
    
    private DataManager(){
        session = Session.SINGLETON;
    }

    public static DataManager getSINGLETON() {
        return SINGLETON;
    }

    public GameIndex getGameIndex() {
        return gameIndex;
    }

    public GameInfo getGameInfo() {
        return curGame;
    }

    public Player getPlayer() {
        return curPlayer;
    }

    public Session getSession() {
        return session;
    }

    public void setGameIndex(GameIndex gameIndex) {
        this.gameIndex = gameIndex;
    }

    public void setGameInfo(GameInfo gameInfo) {
        this.curGame = gameInfo;
    }

    public void setPlayer(Player player) {
        this.curPlayer = player;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
