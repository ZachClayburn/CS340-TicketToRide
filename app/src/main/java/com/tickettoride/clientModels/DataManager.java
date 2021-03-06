package com.tickettoride.clientModels;

import android.util.Log;
import com.tickettoride.facadeProxies.GameFacadeProxy;
import com.tickettoride.models.*;
import com.tickettoride.models.Player;
import com.tickettoride.models.idtypes.PlayerID;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DataManager {
    
    public static DataManager SINGLETON = new DataManager();
    private Session session;
    private Player curPlayer;
    private Game curGame;
    private GameIndex gameIndex;
    private List<Player> gamePlayers;
    private TrainCardDeck trainCardDeck;
    private int trainCardDeckSize = 0;
    private Integer destinationCardDeckSize = 0;
    private Hand playerHand = new Hand();
    private PlayerState playerState;
    private int trainCardsDrawn;
    private List<DestinationCard> offeredCards = null;
    private Integer destCardsRequiredToKeep = null;
    private ArrayList<ClientRoute> clientRoutes;
    private int turn = 1;
    private ClientRoute currentClientRoute;
    private List<TrainCard> faceUpDeck;
    private Map<String, Double> lostPoints = null;
    private List<Player> longestPathWinners = null;

    private static final String TAG = "DATA_MANAGER";

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

    public void setTrainCardDeck(TrainCardDeck deck) {
        trainCardDeck = deck;
    }
    public List<Player> getGamePlayers() { return this.gamePlayers; }

    public TrainCardDeck getTrainCardDeck() { return trainCardDeck; }

    public Hand getPlayerHand() {
        return playerHand;
    }

    public void setPlayerHand(Hand hand) {this.playerHand = hand;}

    public void setPlayerState(PlayerState playerState) {
        this.playerState = playerState;
    }

    public PlayerState getPlayerState() { return this.playerState; }

    public void setTrainCardsDrawn(int trainCardsDrawn) { this.trainCardsDrawn = trainCardsDrawn; }

    public int getTrainCardsDrawn() { return this.trainCardsDrawn; }

    public void updateFaceUpDeck(List<TrainCard> faceUpDeck){trainCardDeck.setFaceUpDeck(faceUpDeck);}

    public void setClientRoutes(ArrayList<ClientRoute> clientRoutes) {
        this.clientRoutes = clientRoutes;
    }
    public ArrayList<ClientRoute> getClientRoutes() {
        return clientRoutes;
    }
    public void setCurrentClientRoute(ClientRoute currentClientRoute) {
        this.currentClientRoute = currentClientRoute;
    }

    public ClientRoute getCurrentClientRoute() {
        return currentClientRoute;
    }
    public void addTrainCardToHand(TrainCard card){
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

    public int getTrainCardDeckSize(){return trainCardDeckSize;}

    public void updateTrainCardDeckSize(int size){trainCardDeckSize = size;}

    public boolean hasOfferedCards() {
        return offeredCards != null;
    }

    public List<DestinationCard> getOfferedCards() {
        return offeredCards;
    }

    public void setOfferedCards(Integer requiredToKeep, List<DestinationCard> offeredCards) {
        assert this.offeredCards == null;

        this.offeredCards = offeredCards;
        this.destCardsRequiredToKeep = requiredToKeep;
    }

    public void removeOfferedCards() {
        offeredCards = null;
        destCardsRequiredToKeep = null;
    }

    public Integer getDestCardsRequiredToKeep() {
        return destCardsRequiredToKeep;
    }

    public Player findPlayerByID(PlayerID playerID){
        for (Player player: gamePlayers) {
            if (player.getPlayerID().equals(playerID)){
                return player;
            }
        }
        return null;
    }

    public Player findPlayerByTurn(int turn){
        for (Player player:gamePlayers) {
            if (player.getTurn() == turn){
                return player;
            }
        }
        return null;
    }

    public void setTurn(int turn) { this.turn = turn; }

    public int getTurn() { return this.turn; }


    public void setRouteClaimed(Route route) {
        for (ClientRoute clientRoute : clientRoutes) {
            if (clientRoute.getRouteID().equals(route.getRouteID())) {
                clientRoute.setClaimedByPlayerID(route.getClaimedByPlayerID());
            }
        }
    }

    public Player selectWinner() {
        Player max_player = gamePlayers.get(0);
        for (Player player: gamePlayers) {
            if (player.getPoints() > max_player.getPoints()) { max_player = player; }
        }
        return max_player;
    }

    public void setLostPoints(Map<String, Double> lostPoints) {
        this.lostPoints = lostPoints;
    }

    public int getPlayerLostPoints(PlayerID playerID) {

        Double value =  lostPoints.get(playerID.toString());
        if (value == null) {
            Log.e(TAG, "getPlayerLostPoints: No lost points for Player " + playerID, new NullPointerException());
            return 0;
        }
        return value.intValue();
    }

    public List<Player> getLongestPathWinners() {
        return longestPathWinners;
    }

    public void setLongestPathWinners(List<Player> longestPathWinners) {
        this.longestPathWinners = longestPathWinners;
    }

    public void setCurrentPLayerState(List<PlayerState> playerStateList) {
        for (PlayerState playerState : playerStateList) {
            if (playerState instanceof CompleteGameState) {
                GameFacadeProxy.SINGLETON.finish(DataManager.getSINGLETON().getGame());
                return;
            }
        }
        for (PlayerState playerState : playerStateList) {
            if (playerState.getPlayerID().equals(getPlayer().getPlayerID())) {
                this.playerState = playerState;
                return;
            }
        }
    }
}
