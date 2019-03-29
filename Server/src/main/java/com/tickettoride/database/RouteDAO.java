package com.tickettoride.database;

import com.tickettoride.models.City;
import com.tickettoride.models.Color;
import com.tickettoride.models.Route;
import com.tickettoride.models.idtypes.GameID;
import com.tickettoride.models.idtypes.PlayerID;
import com.tickettoride.models.idtypes.RouteID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import exceptions.DatabaseException;

public class RouteDAO extends Database.DataAccessObject {
    private final String tableCreateString =
        // language=PostgreSQL
        "CREATE TABLE Routes" +
            "(" +
            "routeID TEXT PRIMARY KEY NOT NULL CHECK ( length(routeID) > 0 )," +
            "gameID TEXT NOT NULL CHECK ( length(gameID) > 0 )," +
            "claimedByPlayerID TEXT," +
            "firstCity TEXT NOT NULL," +
            "secondCity TEXT NOT NULL," +
            "color TEXT NOT NULL," +
            "spaces INTEGER CONSTRAINT validSpaces CHECK ( spaces >= 0 AND spaces < 7) NULL ," +
            "FOREIGN KEY (gameID) REFERENCES games(gameid)," +
            "FOREIGN KEY (claimedByPlayerID) REFERENCES players(playerID) " +
            ");";

    public RouteDAO(Connection connection) {
        super(connection);
    }

    private Logger logger = LogManager.getLogger(GameDAO.class.getName());

    @Override
    String getTableCreateString() {
        return tableCreateString;
    }

    public void addRoute(Route route) throws DatabaseException {
        final String sql = "INSERT INTO Routes (routeID, gameID, claimedByPlayerID, firstCity, secondCity, color, spaces) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, route.getRouteID().toString());
            statement.setString(2, route.getGameID().toString());
            if (route.getClaimedByPlayerID() != null) { statement.setString(3, route.getClaimedByPlayerID().toString()); }
            else { statement.setString(3,null); }
            statement.setString(4, route.getCities().get(0).toString());
            statement.setString(5, route.getCities().get(1).toString());
            statement.setString(6, route.getColor().toString());
            statement.setInt(7, route.getSpaces());
            statement.executeUpdate();
        } catch (SQLException e) { throw new DatabaseException("Could not add new route to Database!", e); }
    }

    public void updateRoute(Route route) throws DatabaseException {
        final String sql = "UPDATE Routes SET gameId = ?, claimedByPlayerId = ?, firstCity = ?, secondCity = ?, color = ?, spaces = ? WHERE routeId = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, route.getGameID().toString());
            statement.setString(2, route.getClaimedByPlayerID().toString());
            statement.setString(3, route.getCities().get(0).toString());
            statement.setString(4, route.getCities().get(1).toString());
            statement.setString(5, route.getColor().toString());
            statement.setInt(6, route.getSpaces());
            statement.setString(7, route.getRouteID().toString());
            statement.executeUpdate();
        } catch (SQLException e) { throw new DatabaseException("Could not update route in Database!", e); }
    }

    public Route getRoute(RouteID routeID) throws DatabaseException{
        Route route = null;
        String sql = "SELECT * FROM Routes WHERE routeID = ?";
        try (var statement = connection.prepareStatement(sql)) {
            statement.setString(1, routeID.toString());
            var result = statement.executeQuery();
            while (result.next()) { route = buildRouteFromQueryResult(result); }
        } catch (SQLException e) { throw new DatabaseException("Could not retrieve route", e); }
        return route;
    }

    public List<Route> getRoutes(GameID gameID) throws DatabaseException {
        List<Route> routes = new ArrayList<>();
        String sql = "Select * from Routes WHERE gameID = ?";
        try (var statement = connection.prepareStatement(sql)) {
            statement.setString(1, gameID.toString());
            var results = statement.executeQuery();
            while (results.next()) { routes.add(buildRouteFromQueryResult(results)); }
        } catch (SQLException e) { throw new DatabaseException(("Could Not Retrieve routes"), e); }
        return routes;
    }

    public List<Route> getPlayerRoutes(PlayerID playerID) throws DatabaseException {

        List<Route> routes = new ArrayList<>();
        String sql = "select * from routes where claimedbyplayerid=?";

        try (var statement = connection.prepareStatement(sql)){

            statement.setString(1, playerID.toString());
            var results = statement.executeQuery();

            while (results.next()) {
                routes.add(buildRouteFromQueryResult(results));
            }

        } catch (SQLException e) {
            throw new DatabaseException("Could not retrieve routes for player: " + playerID, e);
        }
        return routes;
    }

    public Route buildRouteFromQueryResult(ResultSet result) throws SQLException {
        RouteID routeID = RouteID.fromString(result.getString("routeID"));
        GameID gameID = GameID.fromString(result.getString("gameID"));
        PlayerID claimedByPlayerId = null;
        if (result.getString("claimedByPlayerID") != null)
            claimedByPlayerId = PlayerID.fromString(result.getString("claimedByPlayerID"));
        String firstCityString = result.getString("firstCity");
        String secondCityString = result.getString("secondCity");
        String colorString = result.getString("color");
        int spaces = result.getInt("spaces");
        List<City> cities = List.of(City.valueOf(firstCityString), City.valueOf(secondCityString));
        Color color = Color.valueOf(colorString);
        return new Route(routeID, gameID, claimedByPlayerId, cities, color, spaces);
    }

}
