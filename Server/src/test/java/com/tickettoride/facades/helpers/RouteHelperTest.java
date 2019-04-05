package com.tickettoride.facades.helpers;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.tickettoride.database.Database;
import com.tickettoride.facades.helpers.RouteHelper.WeightedEdge;
import com.tickettoride.models.*;
import exceptions.DatabaseException;
import org.jetbrains.annotations.NotNull;
import org.jgrapht.Graph;
import org.jgrapht.alg.transform.LineGraphConverter;
import org.jgrapht.graph.*;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;

import static com.tickettoride.models.City.*;
import static org.junit.Assert.*;



public class RouteHelperTest {

    private Game testGame;

    private User testUser1;
    private User testUser2;
    private User testUser3;
    private User testUser4;
    private User testUser5;

    private Player testPlayer1;
    private Player testPlayer2;
    private Player testPlayer3;
    private Player testPlayer4;
    private Player testPlayer5;

    private List<Route> routes;

    private Type paramsListType = new TypeToken<List<SetupParamPlayer>>(){}.getType();
    private static final String paramsFile = "RouteHelperTestInputs/RouteHelperTestInput.json";

    @Before
    public void setup() throws DatabaseException, CloneNotSupportedException {

        try (var db = new Database()){
            db.resetDatabase();

            testGame = new Game("Test Game", 5);
            db.getGameDAO().addGame(testGame);
            var pw = new Password("Hi");

            testUser1 = new User(new Username("testUser1"), pw);
            db.getUserDAO().addUser(testUser1);
            testPlayer1 = new Player(testUser1.getUserID(), testGame.getGameID());
            db.getPlayerDAO().addPlayer(testPlayer1);

            testUser2 = new User(new Username("testUser2"), pw);
            db.getUserDAO().addUser(testUser2);
            testPlayer2 = new Player(testUser2.getUserID(), testGame.getGameID());
            db.getPlayerDAO().addPlayer(testPlayer2);

            testUser3 = new User(new Username("testUser3"), pw);
            db.getUserDAO().addUser(testUser3);
            testPlayer3 = new Player(testUser3.getUserID(), testGame.getGameID());
            db.getPlayerDAO().addPlayer(testPlayer3);

            testUser4 = new User(new Username("testUser4"), pw);
            db.getUserDAO().addUser(testUser4);
            testPlayer4 = new Player(testUser4.getUserID(), testGame.getGameID());
            db.getPlayerDAO().addPlayer(testPlayer4);

            testUser5 = new User(new Username("testUser5"), pw);
            db.getUserDAO().addUser(testUser5);
            testPlayer5 = new Player(testUser5.getUserID(), testGame.getGameID());
            db.getPlayerDAO().addPlayer(testPlayer5);

            db.commit();
        }

        RouteHelper.getSingleton().createGameRoutes(testGame.getGameID());

        try (var db = new Database()) {
            routes = db.getRouteDAO().getRoutes(testGame.getGameID());
        }

    }

    private static class SetupParamPlayer {
        public String name;
        public List<SetupParamRoute> routes;
    }

    private static class SetupParamRoute {
        public City city1;
        public City city2;
        public Color color;
    }

    @NotNull
    private Route getRoute(City city1, City city2, Color color) {
        return routes.stream()
                .filter(r -> r.getCities().contains(city1))
                .filter(r -> r.getCities().contains(city2))
                .filter(r -> color == null || r.getColor().equals(color))
                .filter(route -> !route.getIsClaimed())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Could not find " +
                        (color != null ? color : "any") + " route from " + city1 + " to " + city2));
    }

    private void setupPlayers(String paramsFile) throws DatabaseException {

        var inputJson = Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(paramsFile));
        var reader = new InputStreamReader(inputJson);
        List<SetupParamPlayer> paramPlayers = new Gson().fromJson(reader, paramsListType);

        try (var db = new Database()) {
            for (var paramPlayer : paramPlayers) {
                Player player;
                switch (paramPlayer.name) {
                    case "testPlayer1": player = testPlayer1; break;
                    case "testPlayer2": player = testPlayer2; break;
                    case "testPlayer3": player = testPlayer3; break;
                    case "testPlayer4": player = testPlayer4; break;
                    case "testPlayer5": player = testPlayer5; break;
                    default: throw new JsonParseException("Player's Name " + paramPlayer.name + " incorrect!");
                }
                for (var paramRoute : paramPlayer.routes) {
                    var route = getRoute(paramRoute.city1, paramRoute.city2, paramRoute.color);
                    route.setClaimedByPlayerID(player.getPlayerID());
                    db.getRouteDAO().updateRoute(route);
                }
            }
            db.commit();
        }

    }

    @Test
    public void graphOfPlayersClaimedRoutesAskedFor_CorrectGraphBuilt() throws DatabaseException {

        setupPlayers("RouteHelperTestInputs/Test1Input.json");

        Graph<City, WeightedEdge> expectedGraph = new SimpleWeightedGraph<>(WeightedEdge.class);

        expectedGraph.addVertex(VANCOUVER);
        expectedGraph.addVertex(CALGARY);
        expectedGraph.addVertex(WINNIPEG);
        expectedGraph.addVertex(SAUL_ST_MARIE);
        expectedGraph.addVertex(SEATTLE);
        expectedGraph.addVertex(DULUTH);
        expectedGraph.addVertex(HELENA);

        expectedGraph.setEdgeWeight(expectedGraph.addEdge(VANCOUVER, CALGARY), 3);
        expectedGraph.setEdgeWeight(expectedGraph.addEdge(VANCOUVER, SEATTLE), 1);
        expectedGraph.setEdgeWeight(expectedGraph.addEdge(SEATTLE, CALGARY), 4);
        expectedGraph.setEdgeWeight(expectedGraph.addEdge(CALGARY, WINNIPEG), 6);
        expectedGraph.setEdgeWeight(expectedGraph.addEdge(WINNIPEG, SAUL_ST_MARIE), 6);
        expectedGraph.setEdgeWeight(expectedGraph.addEdge(WINNIPEG, DULUTH), 4);
        expectedGraph.setEdgeWeight(expectedGraph.addEdge(HELENA, WINNIPEG), 4);

        List<Route> routes;
        try (var db = new Database()) {
            routes = db.getRouteDAO().getPlayerRoutes(testPlayer1.getPlayerID());
        }
        var graph = RouteHelper.getSingleton().getPlayerRouteGraph(routes);

        assertEquals(expectedGraph, graph);
    }

    @Test
    public void maxPathLengthOfGraphAskedFor_CorrectValueReturned() throws DatabaseException {

        setupPlayers("RouteHelperTestInputs/maxPathTestInput.json");

        final int player1ExpectedLength = 18;
        final int player2ExpectedLength = 28;
        final int player3ExpectedLength = 5;

        List<Route> player1Routes = RouteHelper.getSingleton().getPlayerRoutes(testPlayer1.getPlayerID());
        List<Route> player2Routes = RouteHelper.getSingleton().getPlayerRoutes(testPlayer2.getPlayerID());
        List<Route> player3Routes = RouteHelper.getSingleton().getPlayerRoutes(testPlayer3.getPlayerID());

        var player1Graph = RouteHelper.getSingleton().getPlayerRouteGraph(player1Routes);
        var player2Graph = RouteHelper.getSingleton().getPlayerRouteGraph(player2Routes);
        var player3Graph = RouteHelper.getSingleton().getPlayerRouteGraph(player3Routes);

        int player1Length = RouteHelper.getSingleton().getLongestRouteInRouteGraph(player1Graph);
        int player2Length = RouteHelper.getSingleton().getLongestRouteInRouteGraph(player2Graph);
        int player3Length = RouteHelper.getSingleton().getLongestRouteInRouteGraph(player3Graph);

        assertEquals(player1ExpectedLength, player1Length);
        assertEquals(player2ExpectedLength, player2Length);
        assertEquals(player3ExpectedLength, player3Length);
    }

    @Test
    public void destinationCardCompletenessChecked_FunctionsProperly() throws DatabaseException {

        setupPlayers("RouteHelperTestInputs/completedRouteTestInput.json");

        int firtsRouteValue = 4;
        int secondRouteValue = 5;
        int thirdRouteValue = 6;
        int fourthRouteValue = 7;
        int expectedPoints = firtsRouteValue - secondRouteValue - thirdRouteValue - fourthRouteValue;

        List<Route> routes;

        try (var db = new Database()){

            var deck = DestinationCard.getPointOrderedDeck();
            db.getDestinationCardDAO().addDeck(testGame, deck);

            List<DestinationCard> cards = new ArrayList<>();
            cards.add(deck.poll());//Denver to El Paso -- Has, worth 4
            cards.add(deck.poll());//Kansas City to Houston -- Doesn't have, worth 5
            cards.add(deck.poll());//New York to Atlanta -- Doesn't have, worth 6
            cards.add(deck.poll());//Chicago to New Orleans -- Doesn't have, worth 7

            db.getDestinationCardDAO().offerCardsToPlayer(testPlayer1, cards);
            db.getDestinationCardDAO().acceptCards(testPlayer1, cards);

            routes = db.getRouteDAO().getPlayerRoutes(testPlayer1.getPlayerID());

            db.commit();
        }

        var routeGraph = RouteHelper.getSingleton().getPlayerRouteGraph(routes);

        RouteHelper.getSingleton().awardDestinationCardPoints(testPlayer1, routeGraph);

        assertEquals(expectedPoints, testPlayer1.getPoints());
    }
}