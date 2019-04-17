package com.tickettoride.database;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.tickettoride.database.interfaces.IHistoryDAO;
import com.tickettoride.models.Message;
import com.tickettoride.models.idtypes.GameID;
import com.tickettoride.models.idtypes.PlayerID;

import org.bson.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import exceptions.DatabaseException;

public class HistoryDAO extends Database.DataAccessObject implements IHistoryDAO {

    private Map<GameID,List<Message>> historyMap;
    
    public HistoryDAO(MongoDatabase database) {
        super(database);
        collectionName = "histories";
        historyMap = DataManager.getHistoryMap();
    }

    @Override
    public void initializeData() {
        Map<GameID,List<Message>> historyMap = DataManager.getHistoryMap();
        Map<GameID,List<Message>> allHistories = allHistories();
        for (Map.Entry<GameID,List<Message>> history: allHistories.entrySet()) {
            historyMap.put(history.getKey(),history.getValue());
        }
    }
    
    public Map<GameID,List<Message>> allHistories(){
        FindIterable<Document> iterUsers = getCollection().find();
        Iterator iter = iterUsers.iterator();
        Map<GameID,List<Message>> histories = new HashMap<>();
        while(iter.hasNext()) {
            Document doc = (Document) iter.next();
            buildMessageToMap(histories,doc);
        }
        return histories;
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
        List<Message> history;
        if(map.containsKey(gameID)){
            history=map.get(gameID);
        }else{
            history=new ArrayList<Message>();
        }
        history.add(message);
        map.put(gameID,history);
    }
    
    @Override
    public void addEvent(GameID gameID, Message message) throws DatabaseException {
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
        addMessageToMap(historyMap,gameID,message);
    }

    @Override
    public List<Message> getHistory(GameID gameID) throws DatabaseException {
        return historyMap.getOrDefault(gameID,new ArrayList<>());
    }
}
