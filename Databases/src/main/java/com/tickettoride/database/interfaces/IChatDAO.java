package com.tickettoride.database.interfaces;

import com.tickettoride.models.Message;
import com.tickettoride.models.idtypes.GameID;
import exceptions.DatabaseException;

import java.util.List;

public interface IChatDAO {
    void addMessage(GameID gameID, Message message) throws DatabaseException;

    List<Message> getChat(GameID gameID) throws DatabaseException;
}
