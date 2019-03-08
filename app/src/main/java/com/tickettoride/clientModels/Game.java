package com.tickettoride.clientModels;
import com.google.gson.internal.LinkedTreeMap;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

import modelInterfaces.IGame;

public class Game implements IGame, Serializable { //used for when creating a new game
    private UUID gameID = null;
    private String groupName = "";
    private int maxPlayer = 1;
    private int numPlayer = 1;
    private Boolean isStarted;

    public Game(UUID gameID, String groupName, int numPlayer, int maxPlayer, Boolean isStarted) {
        this.gameID = gameID;
        this.groupName = groupName;
        this.numPlayer = numPlayer;
        this.maxPlayer = maxPlayer;
        this.isStarted = isStarted;
    }

    public Game(){}

    public Game(LinkedTreeMap<String, Object> gameMap) {
        this.gameID = UUID.fromString((String) gameMap.get("gameID"));
        this.groupName = (String) gameMap.get("groupName");
        this.numPlayer = ((Double) gameMap.get("numPlayer")).intValue();
        this.maxPlayer = ((Double) gameMap.get("maxPlayer")).intValue();
        this.isStarted = (Boolean) gameMap.get(("isStarted"));
    }


    public void setGameID(UUID gameID) {
        this.gameID = gameID;
    }

    public UUID getGameID() {
        return gameID;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setNumPlayer(int numPlayer) {
        this.numPlayer = numPlayer;
    }

    public int getNumPlayer() {
        return numPlayer;
    }

    public int getMaxPlayer(){return maxPlayer;}

    public void setMaxPlayer(int num) {
        maxPlayer = num;
    }

    public Boolean getIsStarted() { return  isStarted; }

    public static ArrayList<Game> buildGames(ArrayList<LinkedTreeMap> gameMap) {
        ArrayList<Game> games = new ArrayList<>();
        for (LinkedTreeMap singleGameHash : gameMap) {
            Game game = new Game(singleGameHash);
            games.add(game);
        }
        return games;
    }

//    public void setupPlayers(List<Player> updatedPlayers) {
//        for (Player updatedPlayer: updatedPlayers) {
//            for (Player player: players) {
//                if (!player.getPlayerID().equals(updatedPlayer.getPlayerID())) {
//                    continue;
//                }
//                player.setColor(updatedPlayer.getColor());
//                player.setTurn(updatedPlayer.getTurn());
//            }
//        }
//    }
//
//    public Player findPlayer(UUID playerID) {
//        for (Player player : players) {
//            if (player.getPlayerID().equals(playerID)) {
//                return player;
//            }
//        }
//        return null;
//    }
}
