package com.tickettoride.database;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.tickettoride.database.interfaces.IChatDAO;
import com.tickettoride.models.Message;
import com.tickettoride.models.User;
import com.tickettoride.models.idtypes.GameID;
import com.tickettoride.models.idtypes.PlayerID;
import com.tickettoride.models.idtypes.UserID;

import org.bson.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import exceptions.DatabaseException;

public class ChatDAO extends Database.DataAccessObject implements IChatDAO {

    private Map<GameID,List<Message>> chatMap;
    
    public ChatDAO(MongoDatabase database) {
        super(database);
        collectionName = "chats";
        chatMap=DataManager.getChatMap();
    }

    @Override
    public void initializeData() {
        Map<GameID,List<Message>> chatMap = DataManager.getChatMap();
        Map<GameID,List<Message>> allChats = allChats();
        for (Map.Entry<GameID,List<Message>> chat: allChats.entrySet()) {
            chatMap.put(chat.getKey(),chat.getValue());
        }
    }
    
    public Map<GameID,List<Message>> allChats(){
        FindIterable<Document> iterUsers = getCollection().find();
        Iterator iter = iterUsers.iterator();
        Map<GameID,List<Message>> chats = new HashMap<>();
        while(iter.hasNext()) {
            Document doc = (Document) iter.next();
            buildMessageToMap(chats,doc);
        }
        return chats;
    }
    
    private void buildMessageToMap(Map<GameID,List<Message>> map,Document doc){
        GameID gameID=GameID.fromString(doc.getString("gameID"));
        
        PlayerID playerId= PlayerID.fromString(doc.getString("playerID"));
        String innermessage=doc.getString("message");
        String time=doc.getString("time");
        Message message=new Message(innermessage,playerId,time);
        addMessageToMap(map,gameID,message);
    }
    
    private void addMessageToMap(Map<GameID,List<Message>> map,GameID gameID,Message message){
        List<Message> chat;
        if(map.containsKey(gameID)){
            chat=map.get(gameID);
        }else{
            chat=new ArrayList<Message>();
        }
        chat.add(message);
        map.put(gameID,chat);
    }

    @Override
    public void addMessage(GameID gameID, Message message) throws DatabaseException {
        Document document=new Document();
        document.append("gameID",gameID.toString());
        document.append("playerID",message.getPlayerID().toString());
        document.append("time",message.getTimeString());
        document.append("message",message.getMessage());
        MongoCollection collection = getCollection();
        List<Object> parameters = new ArrayList<>();
        parameters.add(document);
        MongoCommand mongoCommand = new MongoCommand(collection, Database.INSERT_METHOD_NAME, parameters);
        Database.addCommand(mongoCommand);
        addMessageToMap(chatMap,gameID,message);
    }

    @Override
    public List<Message> getChat(GameID gameID) throws DatabaseException {
        return chatMap.getOrDefault(gameID,new ArrayList<>());
    }
}
