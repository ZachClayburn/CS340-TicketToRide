package com.tickettoride.database.interfaces;

import com.tickettoride.models.Route;
import com.tickettoride.models.idtypes.GameID;
import com.tickettoride.models.idtypes.PlayerID;
import com.tickettoride.models.idtypes.RouteID;
import exceptions.DatabaseException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface IRouteDAO {
    void addRoute(Route route) throws DatabaseException;

    void updateRoute(Route route) throws DatabaseException;

    Route getRoute(RouteID routeID) throws DatabaseException;

    List<Route> getRoutes(GameID gameID) throws DatabaseException;

    List<Route> getPlayerRoutes(PlayerID playerID) throws DatabaseException;

    Route buildRouteFromQueryResult(ResultSet result) throws SQLException;
}
