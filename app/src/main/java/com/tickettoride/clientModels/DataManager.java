package com.tickettoride.clientModels;

public class DataManager {
    
    public static DataManager SINGLETON = new DataManager();
    private Session session;
    private Player curPlayer;
    private Game curGame;
    private GameIndex gameIndex;
    
    
    private DataManager () {
        this.gameIndex = GameIndex.SINGLETON;
    }


    public static DataManager getSINGLETON() {
        return SINGLETON;
    }

    public GameIndex getGameIndex() {
        return gameIndex;
    }

    public Game getGame() {
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

    public void setGame(Game game) {
        this.curGame = game;
    }

    public void setPlayer(Player player) {
        this.curPlayer = player;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
