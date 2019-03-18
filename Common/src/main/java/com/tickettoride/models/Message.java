package com.tickettoride.models;

import java.time.Instant;
import java.util.UUID;

/**
 * 
 */
public class Message {
    private String time;
    private String message;
    private UUID playerID;


//    /**Constructor to be used when sending a message to the other players using the current player's playerid and the current time
//     * @param message the message to be sent
//     */
//    public Message(String message){
//        this.message=message;
//        playerID=DataManager.getSINGLETON().getPlayer().getPlayerID();
//        time=Instant.now();
//    }
    
    /**Constructor to be used when sending a message to the other players using the current time
     * @param message the message to be sent
     * @param playerID the playerID of the sending player
     */
    public Message(String message, UUID playerID){
        this.message=message;
        this.playerID=playerID;
        time=Instant.now().toString();
    }


    /**Constructor to be used when recieving a message from another player
     * @param message the message recieved
     * @param playerID the sender's playerID
     * @param time the time it was sent
     */
    public Message(String message, UUID playerID, String time){
        this.message=message;
        this.playerID=playerID;
        this.time=time;
    }
    
    public Message(String message, UUID playerID, Instant time){
        this.message=message;
        this.playerID=playerID;
        this.time=time.toString();
    }

    public Instant getTime() {
        return Instant.parse(time);
    }
    
    public String getTimeString(){return time;}

    public String getMessage() {
        return message;
    }

    public UUID getPlayerID() {
        return playerID;
    }
    
}
