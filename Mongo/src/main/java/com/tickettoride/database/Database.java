package com.tickettoride.database;

import com.tickettoride.database.interfaces.*;
import exceptions.DatabaseException;

public class Database implements IDatabase {
    @Override
    public void resetDatabase() throws DatabaseException {
        
    }

    @Override
    public void close() throws DatabaseException {

    }

    @Override
    public ISessionDAO getSessionDAO() {
        return null;
    }

    @Override
    public IUserDAO getUserDAO() {
        return null;
    }

    @Override
    public IGameDAO getGameDAO() {
        return null;
    }

    @Override
    public IPlayerDAO getPlayerDAO() {
        return null;
    }

    @Override
    public IChatDAO getChatDAO() {
        return null;
    }

    @Override
    public IDestinationCardDAO getDestinationCardDAO() {
        return null;
    }

    @Override
    public IRouteDAO getRouteDAO() {
        return null;
    }

    @Override
    public ILineDAO getLineDAO() {
        return null;
    }

    @Override
    public IHistoryDAO getHistoryDAO() {
        return null;
    }

    @Override
    public ITrainCardDAO getTrainCardDAO() {
        return null;
    }

    @Override
    public IPlayerStateDAO getPlayerStateDAO() {
        return null;
    }

    @Override
    public void commit() {

    }
}
