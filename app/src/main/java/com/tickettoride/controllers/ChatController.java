package com.tickettoride.controllers;

import com.google.gson.internal.LinkedTreeMap;
import com.tickettoride.activities.GameRoomActivity;
import com.tickettoride.clientModels.Chat;
import com.tickettoride.models.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChatController extends BaseController {
    private static ChatController SINGLETON=new ChatController();
    
    private ChatController(){
        
    }
    
    public static ChatController getSingleton(){
        return SINGLETON;
    }
    
    public void addMessage(Message message){
        Chat chat=Chat.getSingleton();
        chat.addMessage(message);
        ((GameRoomActivity)getCurrentActivity()).updateChat();
    }
    
    public void setChat(ArrayList<LinkedTreeMap<String,String>> messages){
        Chat chat=Chat.getSingleton();
        List<Message> mess=new ArrayList<>();
        for(LinkedTreeMap<String,String> instance:messages){
            String message=instance.get("message");
            UUID playerID=UUID.fromString(instance.get("playerID"));
            String time=instance.get("time");
            mess.add(new Message(message,playerID,time));
        }
        chat.setMessages(mess);
        //then have the ui update
        ((GameRoomActivity)getCurrentActivity()).updateChat();
    }
    
    public void sendMessageError(Throwable t){
        GameRoomActivity gameRoomActivity = (GameRoomActivity) getCurrentActivity();
        gameRoomActivity.addMessageError();
    }
    
    public void getChatError(Throwable t){
        GameRoomActivity gameRoomActivity = (GameRoomActivity) getCurrentActivity();
        gameRoomActivity.setChatError();
    }
}
