package com.tickettoride.database.interfaces;

import exceptions.DatabaseException;

public interface IDatabase extends AutoCloseable {

    void resetDatabase() throws DatabaseException;

    void close() throws DatabaseException;

    ISessionDAO getSessionDAO();

    IUserDAO getUserDAO();

    IGameDAO getGameDAO();

    IPlayerDAO getPlayerDAO();

    IChatDAO getChatDAO();

    IDestinationCardDAO getDestinationCardDAO();

    IRouteDAO getRouteDAO();

    ILineDAO getLineDAO();

    IHistoryDAO getHistoryDAO();

    ITrainCardDAO getTrainCardDAO();

    IPlayerStateDAO getPlayerStateDAO();

    void commit();
}
