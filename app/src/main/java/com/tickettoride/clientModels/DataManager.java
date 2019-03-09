package com.tickettoride.clientModels;

import com.tickettoride.activities.MapFragment;
import com.tickettoride.models.*;
import com.tickettoride.models.Player;

import java.util.List;

import java.util.List;

public class DataManager {
    
    public static DataManager SINGLETON = new DataManager();
    private Session session;
    private Player curPlayer;
    private Game curGame;
    private GameIndex gameIndex;
    private List<Player> gamePlayers;
    private TrainCardDeck trainCardDeck;
    private Hand playerHand = new Hand();
    private PlayerState playerState;
    private int trainCardsDrawn;

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

    public void setGamePlayers(List<Player> players) { this.gamePlayers = players; }

    public List<Player> getGamePlayers(List<Player> players) { return players; }

    public void setTrainCardDeck() {
        trainCardDeck = new TrainCardDeck();
    }

    public TrainCardDeck getTrainCardDeck(){return trainCardDeck;}

    public Hand getPlayerHand() {
        return playerHand;
    }

    public void setPlayerState(PlayerState playerState) { this.playerState = playerState; }

    public PlayerState getPlayerState() { return this.playerState; }

    public void setTrainCardsDrawn(int trainCardsDrawn) { this.trainCardsDrawn = trainCardsDrawn; }

    public int getTrainCardsDrawn() { return this.trainCardsDrawn; }


    public void addToHand(TrainCard card){
        Color color = card.getColor();
        switch(color) {
            case GREEN:
                playerHand.setGreen(playerHand.getGreen() + 1);
            case RED:
                playerHand.setRed(playerHand.getRed() + 1);
            case BLUE:
                playerHand.setBlue(playerHand.getBlue() + 1);
            case YELLOW:
                playerHand.setYellow(playerHand.getYellow() + 1);
            case PURPLE:
                playerHand.setPurple(playerHand.getPurple() + 1);
            case ORANGE:
                playerHand.setOrange(playerHand.getOrange() + 1);
            case BLACK:
                playerHand.setBlack(playerHand.getBlack() + 1);
            case WHITE:
                playerHand.setWhite(playerHand.getWhite() + 1);
            case WILD:
                playerHand.setLocomotive(playerHand.getLocomotive() + 1);
        }
    }
}
