package com.tickettoride.database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.tickettoride.database.interfaces.IRouteDAO;
import com.tickettoride.models.Route;
import com.tickettoride.models.idtypes.GameID;
import com.tickettoride.models.idtypes.PlayerID;
import com.tickettoride.models.idtypes.RouteID;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import exceptions.DatabaseException;

public class RouteDAO extends Database.DataAccessObject implements IRouteDAO {

    public RouteDAO(MongoDatabase database) {
        super(database);
        collectionName = "routes";
    }

    @Override
    public void initializeData() {

    }

    @Override
    public void addRoute(Route route) throws DatabaseException {

    }

    @Override
    public void updateRoute(Route route) throws DatabaseException {

    }

    @Override
    public Route getRoute(RouteID routeID) throws DatabaseException {
        return null;
    }

    @Override
    public List<Route> getRoutes(GameID gameID) throws DatabaseException {
        return null;
    }

    @Override
    public List<Route> getPlayerRoutes(PlayerID playerID) throws DatabaseException {
        return null;
    }

    @Override
    public Route buildRouteFromQueryResult(ResultSet result) throws SQLException {
        return null;
    }
}
