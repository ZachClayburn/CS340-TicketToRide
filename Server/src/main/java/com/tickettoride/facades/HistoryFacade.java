package com.tickettoride.facades;

import com.tickettoride.database.DatabaseProvider;
import com.tickettoride.database.interfaces.IDatabase;
import com.tickettoride.database.interfaces.IHistoryDAO;
import com.tickettoride.database.interfaces.IPlayerDAO;
import com.tickettoride.models.Message;
import com.tickettoride.models.idtypes.GameID;
import com.tickettoride.models.idtypes.PlayerID;
import command.Command;
import exceptions.DatabaseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HistoryFacade extends BaseFacade {
    private static HistoryFacade SINGLETON=new HistoryFacade();
    private static String CONTROLLER_NAME = "HistoryController";
    private static Logger logger = LogManager.getLogger(ChatFacade.class.getName());

    public static HistoryFacade getSingleton() {
        return SINGLETON;
    }

    private HistoryFacade() {}
    
    public void sendEvent(UUID connID, Message message){
        try{
            GameID gameID=getGameID(message.getPlayerID());
            try(IDatabase IDatabase = DatabaseProvider.getDatabase()){
                IHistoryDAO IHistoryDAO = IDatabase.getHistoryDAO();
                IHistoryDAO.addEvent(gameID,message);
                IDatabase.commit();
            }
            sendResponseToRoom(connID,new Command(CONTROLLER_NAME, "addEvent", message));
        }catch(Throwable e){
            //send the error command not quite sure where to send this one,
            logger.error(e.getMessage(),e);
            //sendResponseToOne(connID,new Command(CONTROLLER_NAME, "sendMessageError",e));
        }
    }

    //helper
    public GameID getGameID(PlayerID playerID) throws DatabaseException, Exception {
        List<GameID> games;
        try (IDatabase IDatabase = DatabaseProvider.getDatabase()) {
            IPlayerDAO playerDAO = IDatabase.getPlayerDAO();
            games = playerDAO.getGameForPlayer(playerID);
        }
        if(games.size()==0){
            throw new Exception("playerID "+playerID+" was not found in any active games");
        }else if(games.size()>1){
            throw new Exception("playerID "+playerID+" was found in "+games.size()+" active games");
        }
        return games.get(0);
    }


    public void getHistory(UUID connID, GameID gameID){
        List<Message> events=new ArrayList<>();
        try(IDatabase IDatabase = DatabaseProvider.getDatabase()){
            IHistoryDAO IHistoryDAO = IDatabase.getHistoryDAO();
            events= IHistoryDAO.getHistory(gameID);
            sendResponseToOne(connID, new Command(CONTROLLER_NAME, "setHistory", events));
        }catch(Throwable e){
            logger.error(e.getMessage(),e);
            sendResponseToOne(connID,new Command(CONTROLLER_NAME, "getHistoryError", e));
        }
    }
}
