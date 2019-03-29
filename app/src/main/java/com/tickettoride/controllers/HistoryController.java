package com.tickettoride.controllers;

import android.util.Log;

import com.google.gson.internal.LinkedTreeMap;
import com.tickettoride.activities.GameRoomActivity;
import com.tickettoride.clientModels.History;
import com.tickettoride.models.Message;
import com.tickettoride.models.idtypes.PlayerID;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HistoryController extends BaseController {
    private static HistoryController SINGLETON=new HistoryController();

    private HistoryController(){

    }

    public static HistoryController getSingleton(){
        return SINGLETON;
    }

    public void addEvent(Message event){
        History history=History.getSingleton();
        history.addEvent(event);
        ((GameRoomActivity)getCurrentActivity()).updateHistory();
    }

    public void setHistory(ArrayList<LinkedTreeMap<String,Object>> messages){
        History history=History.getSingleton();
        List<Message> mess=new ArrayList<>();
        for(LinkedTreeMap<String,Object> instance:messages){
            String message=(String)instance.get("message");
            PlayerID playerID=PlayerID.fromString((String)((Map)(instance.get("playerID"))).get("uuid"));
            String time=(String)instance.get("time");
            mess.add(new Message(message,playerID,time));
        }
        history.setHistory(mess);
        //then have the ui update
        try {
            ((GameRoomActivity) getCurrentActivity()).updateHistory();
        }catch(ClassCastException e){
            Log.e("ChatController",e.getMessage());
        }catch(NullPointerException e){
            Log.e("ChatController",e.getMessage());
            //Log.i("ChatController", "attempting reupdate");
            //if either exception gets thrown it means the gameroomactivity hasn't been set up yet but 
            //the chat has been successfully loaded into the model, as such no need to call update as it will be loaded correctly
        }
    }

    public void getHistoryError(Throwable t){
        GameRoomActivity gameRoomActivity = (GameRoomActivity) getCurrentActivity();
        gameRoomActivity.setChatError();
    }
}
