package com.tickettoride.clientModels;

import com.tickettoride.models.Message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Chat {
    private static Chat singleton = new Chat();
    private List<Message> messages;
    
    public Chat(){
        messages=new ArrayList<Message>();
    }

    public static Chat getSingleton() {
        return singleton;
    }
    
    public void addMessage(Message message){
        messages.add(message);
    }
    
    public void setMessages(List<Message> messages){
        this.messages=messages;
        Collections.sort(this.messages, new sortMessage());
    }
    
    public List<Message> getMessages(){
        return messages;
    }
    
    private class sortMessage implements Comparator<Message>{
        @Override
        public int compare(Message o1, Message o2) {
            if(o1.getTime().equals(o2.getTime())){
                if(o1.getPlayerID().equals(o2.getPlayerID())){
                    return o1.getMessage().compareTo(o2.getMessage());
                }else{
                    return o1.getPlayerID().compareTo(o2.getPlayerID());
                }
            }else{
                return o1.getTime().compareTo(o2.getTime());
            }
        }
    }
    
}
