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
    private Integer destinationCardDeckSize;
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
                playerHand.setGreen(1);
                return;
            case RED:
                playerHand.setRed(1);
                return;
            case BLUE:
                playerHand.setBlue(1);
                return;
            case YELLOW:
                playerHand.setYellow(1);
                return;
            case PURPLE:
                playerHand.setPurple(1);
                return;
            case ORANGE:
                playerHand.setOrange(1);
                return;
            case BLACK:
                playerHand.setBlack(1);
                return;
            case WHITE:
                playerHand.setWhite(1);
                return;
            case WILD:
                playerHand.setLocomotive(1);
                return;
        }
    }

    public Integer getDestinationCardDeckSize() {
        return destinationCardDeckSize;
    }

    public void setDestinationCardDeckSize(Integer destinationCardDeckSize) {
        this.destinationCardDeckSize = destinationCardDeckSize;
    }
}
