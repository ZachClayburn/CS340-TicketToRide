package com.tickettoride.facades.helpers;

import com.tickettoride.database.DatabaseProvider;
import com.tickettoride.database.interfaces.IDatabase;
import com.tickettoride.database.interfaces.IPlayerStateDAO;
import com.tickettoride.facades.BaseFacade;
import com.tickettoride.models.Game;
import com.tickettoride.models.InitializeGameState;
import com.tickettoride.models.Player;
import com.tickettoride.models.PlayerState;
import com.tickettoride.models.idtypes.GameID;
import com.tickettoride.models.idtypes.PlayerID;
import exceptions.DatabaseException;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class PlayerStateHelper extends BaseFacade {
    private static PlayerStateHelper SINGLETON = new PlayerStateHelper();
    public static PlayerStateHelper getSingleton() { return SINGLETON; }
    private PlayerStateHelper() {}

    public List<PlayerState> initializePlayerStates(List<Player> players, GameID gameID) throws DatabaseException{
        List<PlayerState> playerStateList = new ArrayList<>();
        for (Player player: players) {
            InitializeGameState initializeGameState = new InitializeGameState(player.getPlayerID(), gameID);
            createPlayerState(initializeGameState);
            playerStateList.add(initializeGameState);
        }
        return playerStateList;
    }

    public void createPlayerState(PlayerState playerState) throws DatabaseException {
        try (IDatabase IDatabase = DatabaseProvider.getDatabase()) {
            IPlayerStateDAO dao = IDatabase.getPlayerStateDAO();
            dao.addPlayerState(playerState);
            IDatabase.commit();
        }
    }

    public List<PlayerState> gamePlayerStates(GameID gameID) throws DatabaseException, ClassNotFoundException,
            NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        try (IDatabase IDatabase = DatabaseProvider.getDatabase()) {
            IPlayerStateDAO dao = IDatabase.getPlayerStateDAO();
            return dao.getGamePlayerStates(gameID);
        }
    }

    public PlayerState getPlayerState(PlayerID playerID) throws DatabaseException, ClassNotFoundException,
            NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        try (IDatabase IDatabase = DatabaseProvider.getDatabase()) {
            IPlayerStateDAO dao = IDatabase.getPlayerStateDAO();
            return dao.getPlayerState(playerID);
        }
    }

    public void updatePlayerState(PlayerState playerState) throws DatabaseException {
        try (IDatabase IDatabase = DatabaseProvider.getDatabase()) {
            IPlayerStateDAO dao = IDatabase.getPlayerStateDAO();
            dao.updatePlayerState(playerState);
            IDatabase.commit();
        }
    }

    public List<PlayerState> incrementGamePlayerStates(Game game) throws DatabaseException, ClassNotFoundException,
            NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        List<PlayerState> playerStateList = gamePlayerStates(game.getGameID());
        List<Player> players = PlayerHelper.getSingleton().getGamePlayers(game);
        List<PlayerState> newPlayerStateList = new ArrayList<>();
        for (PlayerState playerState : playerStateList) {
            if (playerState instanceof InitializeGameState) { continue; }
            for (Player player : players) {
                if (player.getPlayerID().equals(playerState.getPlayerID())) {
                    if (player.getTurn() == game.getCurTurn()) {
                        if (player.getTrainCarCount() < 3) { newPlayerStateList.add(playerState.moveToFinalTurnState()); }
                        else { newPlayerStateList.add(playerState.moveToPlayerTurnState()); }
                    } else {
                        newPlayerStateList.add(playerState.moveToNotTurnState());
                    }
                }
            }
        }
        for (PlayerState playerState : newPlayerStateList) { updatePlayerState(playerState); }
        return newPlayerStateList;
    }

    public List<PlayerState> incrementInitialPlayerState(List<PlayerState> playerStates, Player player, int turnCount) throws DatabaseException {
        for (int i = 0; i < playerStates.size(); i++) {
            PlayerState playerState = playerStates.get(i);
            if (playerState.getPlayerID().equals(player.getPlayerID()) && playerState instanceof InitializeGameState) {
                if (player.getTurn() == turnCount) {
                    playerStates.set(i, playerState.moveToPlayerTurnState());
                } else { playerStates.set(i, playerState.moveToNotTurnState()); }
                updatePlayerState(playerStates.get(i));
            }
        }
        return playerStates;
    }
}
