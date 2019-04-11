package com.tickettoride.database.interfaces;

import com.tickettoride.models.DestinationCard;
import com.tickettoride.models.Game;
import com.tickettoride.models.Player;
import com.tickettoride.models.idtypes.GameID;
import exceptions.DatabaseException;

import java.util.Collection;
import java.util.List;
import java.util.Queue;

public interface IDestinationCardDAO {
    void addDeck(Game game, Queue<DestinationCard> deck) throws DatabaseException;

    void addDeck(GameID gameID, Queue<DestinationCard> deck) throws DatabaseException;

    Queue<DestinationCard> getDeckForGame(Game game) throws DatabaseException;

    Queue<DestinationCard> getDeckForGame(GameID gameID) throws DatabaseException;

    void offerCardsToPlayer(Player player, Collection<DestinationCard> cards) throws DatabaseException;

    List<DestinationCard> getPlayerHand(Player player) throws DatabaseException;

    void acceptCards(Player player, Collection<DestinationCard> acceptedCards)
            throws DatabaseException;

    List<DestinationCard> getOfferedCards(Player player) throws DatabaseException;
}
