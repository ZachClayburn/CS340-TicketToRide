package com.tickettoride.clientModels;

import com.tickettoride.models.Message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class History {
    private static final History ourInstance = new History();
    private List<Message> history=new ArrayList<>();

    public static History getSingleton() {
        return ourInstance;
    }

    private History() {
    }

    public void setHistory(List<Message> messages){
        this.history=messages;
        Collections.sort(this.history, new sortMessage());
    }
    
    public void addEvent(Message event){
        history.add(event);
    }

    public List<Message> getHistory() {
        return history;
    }

    private class sortMessage implements Comparator<Message> {
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
