package com.tickettoride.facades.helpers;

import com.tickettoride.database.Database;
import com.tickettoride.database.PlayerDAO;
import com.tickettoride.facades.BaseFacade;
import com.tickettoride.models.Game;
import com.tickettoride.models.Player;
import com.tickettoride.models.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import exceptions.DatabaseException;

public class PlayerHelper extends BaseFacade {

    private static PlayerHelper SINGLETON = new PlayerHelper();
    public static PlayerHelper getSingleton() { return SINGLETON; }

    public Player createPlayer(UUID user, UUID game) throws DatabaseException {
        try (Database database = new Database()) {
            Player player = new Player(user, game);
            PlayerDAO dao = database.getPlayerDAO();
            dao.addPlayer(player);
            database.commit();
            return player;
        }
    }

    public List<Player> getGamePlayers(Game game) throws DatabaseException {
        return getGamePlayers(game.getGameID());
    }


    public List<Player> getGamePlayers(UUID gameID) throws DatabaseException {
        try (Database database = new Database()) {
            PlayerDAO dao = database.getPlayerDAO();
            return dao.getGamePlayers(gameID);
        }
    }

    public void setUsernames(List<Player> players) throws DatabaseException {
        try (var db = new Database()){
            db.getPlayerDAO().setPlayersUserName(players);
        }
    }


    public void deletePlayer(UUID sessionID) throws DatabaseException, SQLException {
        try (Database database = new Database()) {
            PlayerDAO dao = database.getPlayerDAO();
            dao.deletePlayer(sessionID);
            database.commit();
        }
    }

    public Player isAlreadyPlayer(User user, List<Player> players){
        if( players == null || players.size() == 0) { return null; }
        for (Player p : players) { if (user.getUserID().equals(p.getUserID())) { return p; } }
        return null;
    }

    public Player isAlreadyPlayer(User user, Game game) throws DatabaseException {
        ArrayList<Player> gamePlayers = (ArrayList) getGamePlayers(game);
        return isAlreadyPlayer(user, gamePlayers);
    }

    public void pickTurnOrder(List<Player> players) throws DatabaseException {
        Collections.shuffle(players);
        try (Database database = new Database()) {
            PlayerDAO dao = database.getPlayerDAO();
            for (int i = 0; i < players.size(); i++) {
                Player player = players.get(i);
                player.setTurn(i + 1);
                dao.setTurn(player.getPlayerID(), player.getTurn());
            }
            database.commit();
        }
    }

    public void pickColors(List<Player> players) {
        for (Player player:players){
            player.setColor();
        }
    }
}
