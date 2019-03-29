package com.tickettoride.models;

import com.tickettoride.models.idtypes.GameID;
import com.tickettoride.models.idtypes.PlayerID;
import com.tickettoride.models.idtypes.UserID;

import java.util.*;

public class Player {
    private int turn = 0;
    private PlayerColor color = null;
    private String username = "";
    private UserID userID;
    private GameID gameID;
    private PlayerID playerID;
    private int trainCarCount = 45;
    private int trainCardCount = 4;
    private int destinationCardCount = 0;

    //FIXME Add this functionality
    private int points;

    public Player(UserID userID, GameID gameID) {
        this.userID = userID;
        this.gameID = gameID;
        this.playerID = PlayerID.randomUUID();
    }
    
    public Player(UserID user, GameID game, PlayerID player, int turn, int trainCarCount, int points){
        this.userID=user;
        this.gameID=game;
        this.playerID=player;
        this.turn = turn;
        this.trainCarCount = trainCarCount;
        this.points = points;
    }

    public Player(UserID user, GameID game, PlayerID player, int turn, PlayerColor color){
        this.userID=user;
        this.gameID=game;
        this.playerID=player;
        this.turn = turn;
        this.color = color;
    }


    public Player(UserID userID, GameID gameID, PlayerID playerID){
        this.userID = userID;
        this.gameID = gameID;
        this.playerID = playerID;
    }

    public Player(Map<String, Object> playerMap) {
        this.gameID = GameID.fromString((String) ((Map) playerMap.get("gameID")).get("uuid"));
        this.playerID = PlayerID.fromString((String) ((Map)playerMap.get("playerID")).get("uuid"));
        this.userID = UserID.fromString((String) ((Map) playerMap.get("userID")).get("uuid"));
        this.turn = ((Double) playerMap.get("turn")).intValue();
        this.color = PlayerColor.valueOf((String) playerMap.get("color"));
        this.username = (String) playerMap.get("username");
        this.trainCarCount = ((Double) playerMap.get("trainCarCount")).intValue();
    }

    public UserID getUserID() { return userID; }

    public void setUserID(UserID userID) { this.userID = userID; }

    public GameID getGameID() { return gameID; }

    public void setGameID(GameID gameID) { this.gameID = gameID; }

    public int getTurn() {return turn;}

    public void setTurn(int turn) {this.turn = turn;}

    public PlayerColor getColor() {
        return color;
    }

    public String getUsername(){return username;}

    public void setUsername(String username){this.username = username;}

    public void setColor() {
        switch (turn) {
            case 1:
                color = PlayerColor.RED;
                break;
            case 2:
                color = PlayerColor.BLUE;
                break;
            case 3:
                color = PlayerColor.GREEN;
                break;
            case 4:
                color = PlayerColor.BLACK;
                break;
            case 5:
                color = PlayerColor.YELLOW;
                break;
            default:
                break;
        }
    }

    public PlayerID getPlayerID() { return playerID; }

    public void setPlayerID(PlayerID playerID) { this.playerID = playerID; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player)) return false;
        Player player = (Player) o;
        return userID.equals(player.userID) &&
                gameID.equals(player.gameID) &&
                getPlayerID().equals(player.getPlayerID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(userID, gameID, getPlayerID());
    }

    @Override
    public String toString() {
        return "Player{" +
                "userID=" + userID +
                ", gameID=" + gameID +
                ", playerID=" + playerID +
                '}';
    }

    public int getPoints() {
        return points;
    }

    public int getTrainCarCount() {
        return trainCarCount;
    }

    public void setTrainCarCount(int trainCarCount) {
        this.trainCarCount = trainCarCount;
    }

    public int getTrainCardCount() {
        return trainCardCount;
    }

    public void setTrainCardCount(int trainCardCount) {
        this.trainCardCount = trainCardCount;
    }

    public int getDestinationCardCount() {
        return destinationCardCount;
    }

    public void setDestinationCardCount(int destinationCardCount) {
        this.destinationCardCount = destinationCardCount;
    }

    public void setPoints(int points) { this.points = points; }

    public void givePoints(int points) {
        this.points += points;
    }

    public void takePoints(int points) {
        this.points -= points;
    }
}
