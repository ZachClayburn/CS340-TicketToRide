package com.tickettoride.facades;

import com.tickettoride.database.Database;
import com.tickettoride.database.PlayerDAO;
import com.tickettoride.models.Game;
import com.tickettoride.models.Player;
import com.tickettoride.models.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import exceptions.DatabaseException;

public class PlayerFacade extends BaseFacade {

    private static PlayerFacade SINGLETON = new PlayerFacade();
    public static PlayerFacade getSingleton() { return SINGLETON; }

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

}
