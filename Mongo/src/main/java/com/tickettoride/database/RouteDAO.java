package com.tickettoride.database;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.tickettoride.database.interfaces.IRouteDAO;
import com.tickettoride.models.City;
import com.tickettoride.models.Color;
import com.tickettoride.models.Route;
import com.tickettoride.models.idtypes.GameID;
import com.tickettoride.models.idtypes.PlayerID;
import com.tickettoride.models.idtypes.RouteID;

import org.bson.Document;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import exceptions.DatabaseException;

public class RouteDAO extends Database.DataAccessObject implements IRouteDAO {
    private List<Route> routes;
    public RouteDAO(MongoDatabase database) {
        super(database);
        collectionName = "routes";
        routes = DataManager.getRouteList();
    }

    @Override
    public void initializeData() {
        List<Route> routeList = DataManager.getRouteList();
        List<Route> allRoutes = allRoutes();
        for (Route route: allRoutes) {
            routeList.add(route);
        }
    }

    public List<Route> allRoutes() {
        FindIterable<Document> iterUsers = getCollection().find();
        Iterator iter = iterUsers.iterator();
        List<Route> players = new ArrayList<>();
        while(iter.hasNext()) {
            try {
                ResultSet result = (ResultSet) iter.next();
                players.add(buildRouteFromQueryResult(result));
            } catch (Exception ex) {

            }
        }
        return players;
    }
    @Override
    public void addRoute(Route route) throws DatabaseException {
        Document document = new Document();
        document.append("routeID", route.getRouteID().toString());
        document.append("gameID", route.getGameID().toString());
        if (route.getClaimedByPlayerID() != null) {
            document.append("claimedByPlayerID", route.getClaimedByPlayerID().toString());
        }
        document.append("firstCity", route.getCities().get(0).name());
        document.append("secondCity", route.getCities().get(1).name());
        document.append("color", route.getColor().toString());
        document.append("spaces", route.getSpaces());
        MongoCollection collection = getCollection();
        List<Object> parameters = new ArrayList<>();
        parameters.add(document);
        MongoCommand mongoCommand = new MongoCommand(collection, Database.INSERT_METHOD_NAME, parameters);
        Database.addCommand(mongoCommand);
        routes.add(route);
    }

    @Override
    public void updateRoute(Route route) throws DatabaseException {
        MongoCollection collection = getCollection();
        collection.updateOne(Filters.eq("routeID", route.getRouteID()), Updates.set("claimedByPlayerID", route.getClaimedByPlayerID()));
    }

    @Override
    public Route getRoute(RouteID routeID) throws DatabaseException {
        for (Route route : routes) {
            if (route.getRouteID().equals(routeID)) {
                return route;
            }
        }
        return null;
    }

    @Override
    public List<Route> getRoutes(GameID gameID) throws DatabaseException {
        List<Route> gameRoutes = new ArrayList<>();
        for (Route route : routes) {
            if (route.getGameID().equals(gameID)) {
                gameRoutes.add(route);
            }
        }
        return gameRoutes;
    }

    @Override
    public List<Route> getPlayerRoutes(PlayerID playerID) throws DatabaseException {
        List<Route> playerRoutes = new ArrayList<>();
        for (Route route : routes) {
            if (route.getClaimedByPlayerID().equals(playerID)) {
                playerRoutes.add(route);
            }
        }
        return playerRoutes;
    }

    @Override
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
