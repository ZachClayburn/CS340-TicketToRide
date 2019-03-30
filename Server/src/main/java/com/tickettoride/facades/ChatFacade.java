package com.tickettoride.facades;

import com.tickettoride.database.ChatDAO;
import com.tickettoride.database.Database;
import com.tickettoride.database.PlayerDAO;
import com.tickettoride.models.Message;


import com.tickettoride.models.idtypes.GameID;
import com.tickettoride.models.idtypes.PlayerID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import command.Command;
import exceptions.DatabaseException;

public class ChatFacade extends BaseFacade {
    private static ChatFacade SINGLETON=new ChatFacade();
    private static String CONTROLLER_NAME = "ChatController";
    private static Logger logger = LogManager.getLogger(ChatFacade.class.getName());
    
    public static ChatFacade getSingleton() { 
        return SINGLETON; 
    }
    
    private ChatFacade() {}
    
    public void sendMessage(UUID connID, Message message){
        try{
            GameID gameID=getGameID(message.getPlayerID());
            try(Database database= new Database()){
                ChatDAO chatDAO=database.getChatDAO();
                chatDAO.addMessage(gameID,message);
                database.commit();
            }
            sendResponseToRoom(connID,new Command(CONTROLLER_NAME, "addMessage", message));
        }catch(Throwable e){
            //send the error command
            logger.error(e.getMessage(),e);
            sendResponseToOne(connID,new Command(CONTROLLER_NAME, "sendMessageError",e));
        }
    }
    
    //helper
    public GameID getGameID(PlayerID playerID) throws DatabaseException, Exception {
        List<GameID> games;
        try (Database database = new Database()) {
            PlayerDAO playerDAO = database.getPlayerDAO();
            games = playerDAO.getGameForPlayer(playerID);
        }
        if(games.size()==0){
            throw new Exception("playerID "+playerID+" was not found in any active games");
        }else if(games.size()>1){
            throw new Exception("playerID "+playerID+" was found in "+games.size()+" active games");
        }
        return games.get(0);
    }
    
    
    public void getChat(UUID connID, GameID gameID){
        List<Message> chat=new ArrayList<>();
        try(Database database=new Database()){
            ChatDAO chatDAO=database.getChatDAO();
            chat=chatDAO.getChat(gameID);
            sendResponseToOne(connID, new Command(CONTROLLER_NAME, "setChat", chat));
        }catch(Throwable e){
            logger.error(e.getMessage(),e);
            sendResponseToOne(connID,new Command(CONTROLLER_NAME, "getChatError", e));
        }
    }
    
}
