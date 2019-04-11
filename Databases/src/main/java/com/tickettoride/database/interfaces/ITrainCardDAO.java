package com.tickettoride.database.interfaces;

import com.tickettoride.models.Color;
import com.tickettoride.models.Hand;
import com.tickettoride.models.TrainCard;
import com.tickettoride.models.TrainCardDeck;
import com.tickettoride.models.idtypes.GameID;
import com.tickettoride.models.idtypes.PlayerID;
import exceptions.DatabaseException;

import java.util.List;

public interface ITrainCardDAO {
    void addDeck(GameID gameID, TrainCardDeck deck) throws DatabaseException;

    void addFaceUp(GameID gameID, List<TrainCard> faceUpDeck) throws DatabaseException;

    Hand makeHand(GameID gameID, PlayerID playerID) throws DatabaseException;

    void addFaceDown(GameID gameID, List<TrainCard> faceDownDeck) throws DatabaseException;

    TrainCardDeck getDeckForGame(GameID gameID) throws DatabaseException;

    List<TrainCard> getFaceUpDeck(GameID gameID) throws DatabaseException;

    List<TrainCard> getFaceDownDeck(GameID gameID) throws DatabaseException;

    List<TrainCard> getDiscardDeck(GameID gameID) throws DatabaseException;

    Hand getPlayerHand(PlayerID playerID) throws DatabaseException;

    void discardCards(Color color, int colorCards, int wildCards, PlayerID playerID) throws DatabaseException;

    void replaceFaceDown(GameID gameID) throws DatabaseException;

    TrainCard drawFromFaceUp(GameID gameID, PlayerID playerID, int pos) throws DatabaseException;

    TrainCard drawFromFaceDown(GameID gameID, PlayerID playerID) throws DatabaseException;

    int getFaceDownDeckSize(GameID gameID) throws DatabaseException;

    boolean hasGameInfo(GameID gameID) throws DatabaseException;
}
