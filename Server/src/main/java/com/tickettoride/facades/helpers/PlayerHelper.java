package com.tickettoride.facades.helpers;

import com.tickettoride.database.DatabaseProvider;
import com.tickettoride.database.interfaces.IDatabase;
import com.tickettoride.database.interfaces.IPlayerDAO;
import com.tickettoride.facades.BaseFacade;
import com.tickettoride.facades.GameFacade;
import com.tickettoride.models.Game;
import com.tickettoride.models.Player;
import com.tickettoride.models.User;
import com.tickettoride.models.idtypes.GameID;
import com.tickettoride.models.idtypes.UserID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import exceptions.DatabaseException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class PlayerHelper extends BaseFacade {

    private static PlayerHelper SINGLETON = new PlayerHelper();
    public static PlayerHelper getSingleton() { return SINGLETON; }
    private PlayerHelper() {}

    public Player createPlayer(UserID userID, GameID gameID) throws DatabaseException {
        try (IDatabase IDatabase = DatabaseProvider.getDatabase()) {
            Player player = new Player(userID, gameID);
            IPlayerDAO dao = IDatabase.getPlayerDAO();
            dao.addPlayer(player);
            IDatabase.commit();
            return player;
        }
    }

    public List<Player> getGamePlayers(Game game) throws DatabaseException {
        return getGamePlayers(game.getGameID());
    }


    public List<Player> getGamePlayers(GameID gameID) throws DatabaseException {
        try (IDatabase IDatabase = DatabaseProvider.getDatabase()) {
            IPlayerDAO dao = IDatabase.getPlayerDAO();
            return dao.getGamePlayers(gameID);
        }
    }

    public void setUsernames(List<Player> players) throws DatabaseException {
        try (var db = DatabaseProvider.getDatabase()){
            db.getPlayerDAO().setPlayersUserName(players);
        }
    }


    public void deletePlayer(UUID playerID) throws DatabaseException, SQLException {
        try (IDatabase IDatabase = DatabaseProvider.getDatabase()) {
            IPlayerDAO dao = IDatabase.getPlayerDAO();
            dao.deletePlayer(playerID);
            IDatabase.commit();
        }
    }

    public Player isAlreadyPlayer(User user, List<Player> players){
        if( players == null || players.size() == 0) { return null; }
        for (Player p : players) {
            if (user.getUserID().equals(p.getUserID())) { return p; }
        }
        return null;
    }

    public Player isAlreadyPlayer(User user, Game game) throws DatabaseException {
        ArrayList<Player> gamePlayers = (ArrayList) getGamePlayers(game);
        return isAlreadyPlayer(user, gamePlayers);
    }

    public void pickTurnOrder(List<Player> players) throws DatabaseException {
        Collections.shuffle(players);
        try (IDatabase IDatabase = DatabaseProvider.getDatabase()) {
            IPlayerDAO dao = IDatabase.getPlayerDAO();
            for (int i = 0; i < players.size(); i++) {
                Player player = players.get(i);
                player.setTurn(i + 1);
                dao.setTurn(player.getPlayerID(), player.getTurn());
            }
            IDatabase.commit();
        }
    }

    public void pickColors(List<Player> players) { for (Player player:players){ player.setColor(); } }

    public void setTrainCounts(List<Player> players) throws DatabaseException {
            for (Player player: players) {
                player.setTrainCarCount(45);
                updateTrainCount(player);
            }
    }

    public void updateTrainCount(Player player) throws DatabaseException {
        try (IDatabase IDatabase = DatabaseProvider.getDatabase()) {
            IPlayerDAO dao = IDatabase.getPlayerDAO();
            dao.setTrainCarCount(player.getPlayerID(), player.getTrainCarCount());
            IDatabase.commit();
        }
    }

    public void updatePlayerPoints(Player player) throws DatabaseException {
        try (IDatabase IDatabase = DatabaseProvider.getDatabase()) {
            IPlayerDAO dao = IDatabase.getPlayerDAO();
            dao.setPoints(player.getPlayerID(), player.getPoints());
            IDatabase.commit();
        }
    }

}
