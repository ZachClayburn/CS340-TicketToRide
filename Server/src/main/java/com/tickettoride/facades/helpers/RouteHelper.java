package com.tickettoride.facades.helpers;

import com.tickettoride.database.Database;
import com.tickettoride.database.LineDAO;
import com.tickettoride.database.RouteDAO;
import com.tickettoride.facades.BaseFacade;
import com.tickettoride.models.*;
import com.tickettoride.models.idtypes.GameID;
import com.tickettoride.models.idtypes.PlayerID;
import com.tickettoride.models.idtypes.RouteID;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collector;

import exceptions.DatabaseException;
import org.jgrapht.Graph;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

public class RouteHelper extends BaseFacade {
    private static RouteHelper SINGLETON = new RouteHelper();
    public static RouteHelper getSingleton() { return SINGLETON; }
    private RouteHelper() {}

    private static final int LONGEST_ROUTE_POINT_BONUS = 10;

    private static final Route[] staticRoutes = {
        new Route(List.of(new Line(150, 130, 150, 180)), Color.GREY, 1, List.of(City.VANCOUVER, City.SEATTLE)),
        new Route(List.of(new Line(130, 130, 130, 180)), Color.GREY, 1, List.of(City.VANCOUVER, City.SEATTLE)),
        new Route(List.of(new Line(160, 108, 330, 88)), Color.GREY, 3, List.of(City.VANCOUVER, City.CALGARY)),
        new Route(List.of(new Line(350, 98, 495, 273)), Color.GREY, 4, List.of(City.CALGARY, City.HELENA)),
        new Route(List.of(new Line(510, 268, 665, 118)), Color.BLUE, 4, List.of(City.HELENA, City.WINNIPEG)),
        new Route(List.of(new Line(695, 118, 850, 268)), Color.BLACK, 4, List.of(City.WINNIPEG, City.DULUTH)),
        new Route(List.of(new Line(515, 288, 845, 283)), Color.ORANGE, 6, List.of(City.HELENA, City.DULUTH)),
        new Route(List.of(new Line(155, 215, 485, 293)), Color.YELLOW, 6, List.of(City.SEATTLE, City.HELENA)),
        new Route(List.of(new Line(142, 213, 123, 260)), Color.GREY, 1, List.of(City.SEATTLE, City.PORTLAND)),
        new Route(List.of(new Line(122, 208, 103, 255)), Color.GREY, 1, List.of(City.SEATTLE, City.PORTLAND)),
        new Route(List.of(new Line(485, 308, 400, 450)), Color.PURPLE, 3, List.of(City.HELENA, City.SALT_LAKE_CITY)),
        new Route(List.of(new Line(505, 308, 583, 500)), Color.GREEN, 4, List.of(City.HELENA, City.DENVER)),
        new Route(List.of(new Line(525, 305, 783, 410)), Color.RED, 5, List.of(City.HELENA, City.OMAHA)),
        new Route(List.of(new Line(405, 467, 563, 500)), Color.RED, 3, List.of(City.SALT_LAKE_CITY, City.DENVER)),
        new Route(List.of(new Line(405, 487, 563, 520)), Color.YELLOW, 3, List.of(City.SALT_LAKE_CITY, City.DENVER)),
        new Route(List.of(new Line(365, 473, 110, 557)), Color.ORANGE, 5, List.of(City.SAN_FRANCISCO, City.SALT_LAKE_CITY)),
        new Route(List.of(new Line(365, 493, 115, 577)), Color.WHITE, 5, List.of(City.SAN_FRANCISCO, City.SALT_LAKE_CITY)),
        new Route(List.of(new Line(1040, 170, 705, 100)), Color.GREY, 6, List.of(City.WINNIPEG, City.SAUL_ST_MARIE)),
        new Route(List.of(new Line(1070, 180, 1180, 205)), Color.GREY, 2, List.of(City.SAUL_ST_MARIE, City.TORONTO)),
        new Route(List.of(new Line(1040, 190, 885, 255)), Color.GREY, 3, List.of(City.SAUL_ST_MARIE, City.DULUTH)),
        new Route(List.of(new Line(1200, 222, 875, 277)), Color.PURPLE, 6, List.of(City.DULUTH, City.TORONTO)),
        new Route(List.of(new Line(1230, 227, 1235, 327)), Color.GREY, 2, List.of(City.TORONTO, City.PITTSBURGH)),
        new Route(List.of(new Line(1345, 272, 1250, 332)), Color.WHITE, 2, List.of(City.PITTSBURGH, City.NEW_YORK)),
        new Route(List.of(new Line(1360, 287, 1265, 347)), Color.GREEN, 2, List.of(City.PITTSBURGH, City.NEW_YORK)),
        new Route(List.of(new Line(1370, 302, 1375, 407)), Color.ORANGE, 2, List.of(City.NEW_YORK, City.WASHINGTON)),
        new Route(List.of(new Line(1390, 302, 1395, 407)), Color.BLACK, 2, List.of(City.NEW_YORK, City.WASHINGTON)),
        new Route(List.of(new Line(1360, 262, 1330, 100)), Color.BLUE, 3, List.of(City.MONTREAL, City.NEW_YORK)),
        new Route(List.of(new Line(1380, 267, 1435, 180)), Color.YELLOW, 2, List.of(City.NEW_YORK, City.BOSTON)),
        new Route(List.of(new Line(1400, 277, 1455, 190)), Color.RED, 2, List.of(City.NEW_YORK, City.BOSTON)),
        new Route(List.of(new Line(1350, 97, 1435, 165)), Color.GREY, 2, List.of(City.BOSTON, City.MONTREAL)),
        new Route(List.of(new Line(1360, 82, 1445, 150)), Color.GREY, 2, List.of(City.BOSTON, City.MONTREAL)),
        new Route(List.of(new Line(1367, 427, 1300, 505)), Color.GREY, 2, List.of(City.WASHINGTON, City.RALEIGH)),
        new Route(List.of(new Line(1382, 442, 1315, 520)), Color.GREY, 2, List.of(City.WASHINGTON, City.RALEIGH)),
        new Route(List.of(new Line(1292, 538, 1213, 607)), Color.GREY, 2, List.of(City.RALEIGH, City.ATLANTA)),
        new Route(List.of(new Line(1281, 524, 1202, 593)), Color.GREY, 2, List.of(City.RALEIGH, City.ATLANTA)),
        new Route(List.of(new Line(1281, 494, 1258, 385)), Color.GREY, 2, List.of(City.RALEIGH, City.PITTSBURGH)),
        new Route(List.of(new Line(1265, 365, 1365, 415)), Color.GREY, 2, List.of(City.WASHINGTON, City.PITTSBURGH)),
        new Route(List.of(new Line(1230, 365, 995, 502)), Color.GREEN, 5, List.of(City.PITTSBURGH, City.SAINT_LOUIS)),
        new Route(List.of(new Line(990, 524, 1090, 554)), Color.GREY, 2, List.of(City.SAINT_LOUIS, City.NASHVILLE)),
        new Route(List.of(new Line(975, 514, 947, 602)), Color.GREY, 2, List.of(City.KANSAS_CITY, City.LITTLE_ROCK)),
        new Route(List.of(new Line(937, 625, 832, 630)), Color.GREY, 2, List.of(City.LITTLE_ROCK, City.OKLAHOMA_CITY)),
        new Route(List.of(new Line(837, 640, 850, 747)), Color.GREY, 2, List.of(City.OKLAHOMA_CITY, City.DALLAS)),
        new Route(List.of(new Line(817, 640, 830, 747)), Color.GREY, 2, List.of(City.OKLAHOMA_CITY, City.DALLAS)),
        new Route(List.of(new Line(808, 612, 837, 510)), Color.GREY, 2, List.of(City.OKLAHOMA_CITY, City.KANSAS_CITY)),
        new Route(List.of(new Line(828, 615, 857, 513)), Color.GREY, 2, List.of(City.OKLAHOMA_CITY, City.KANSAS_CITY)),
        new Route(List.of(new Line(857, 505, 959, 502)), Color.PURPLE, 2, List.of(City.KANSAS_CITY, City.SAINT_LOUIS)),
        new Route(List.of(new Line(857, 485, 959, 486)), Color.BLUE, 2, List.of(City.KANSAS_CITY, City.SAINT_LOUIS)),
        new Route(List.of(new Line(964, 475, 1025, 382)), Color.GREEN, 2, List.of(City.SAINT_LOUIS, City.CHICAGO)),
        new Route(List.of(new Line(980, 485, 1040, 392)), Color.WHITE, 2, List.of(City.SAINT_LOUIS, City.CHICAGO)),
        new Route(List.of(new Line(937, 640, 867, 740)), Color.GREY, 2, List.of(City.LITTLE_ROCK, City.DALLAS)),
        new Route(List.of(new Line(863, 765, 897, 805)), Color.GREY, 1, List.of(City.DALLAS, City.HOUSTON)),
        new Route(List.of(new Line(848, 780, 882, 820)), Color.GREY, 1, List.of(City.DALLAS, City.HOUSTON)),
        new Route(List.of(new Line(917, 820, 1027, 805)), Color.GREY, 2, List.of(City.HOUSTON, City.NEW_ORLEANS)),
        new Route(List.of(new Line(1037, 795, 957, 650)), Color.GREEN, 3, List.of(City.LITTLE_ROCK, City.NEW_ORLEANS)),
        new Route(List.of(new Line(1130, 566, 1175, 599)), Color.GREY, 1, List.of(City.NASHVILLE, City.ATLANTA)),
        new Route(List.of(new Line(1215, 624, 1325, 630)), Color.GREY, 2, List.of(City.ATLANTA, City.CHARLESTON)),
        new Route(List.of(new Line(1200, 634, 1375, 855)), Color.BLUE, 5, List.of(City.ATLANTA, City.MIAMI)),
        new Route(List.of(new Line(850, 475, 827, 430)), Color.GREY, 1, List.of(City.OMAHA, City.KANSAS_CITY)),
        new Route(List.of(new Line(835, 485, 812, 440)), Color.GREY, 1, List.of(City.OMAHA, City.KANSAS_CITY)),
        new Route(List.of(new Line(859, 303, 822, 400)), Color.GREY, 2, List.of(City.OMAHA, City.DULUTH)),
        new Route(List.of(new Line(840, 300, 804, 398)), Color.GREY, 2, List.of(City.OMAHA, City.DULUTH)),
        new Route(List.of(new Line(822, 772, 610, 805)), Color.RED, 4, List.of(City.EL_PASO, City.DALLAS)),
        new Route(List.of(new Line(550, 792, 400, 747)), Color.GREY, 3, List.of(City.PHOENIX, City.EL_PASO)),
        new Route(List.of(new Line(413, 729, 560, 664)), Color.GREY, 3, List.of(City.PHOENIX, City.SANTA_FE)),
        new Route(List.of(new Line(590, 662, 760, 641)), Color.BLUE, 3, List.of(City.SANTA_FE, City.OKLAHOMA_CITY)),
        new Route(List.of(new Line(575, 647, 580, 540)), Color.GREY, 2, List.of(City.SANTA_FE, City.DENVER)),
        new Route(List.of(new Line(575, 677, 570, 784)), Color.GREY, 2, List.of(City.EL_PASO, City.SANTA_FE)),
        new Route(List.of(new Line(160, 195, 220, 196), new Line(220, 195, 265, 190),
            new Line(265, 190, 320, 150), new Line(320, 150, 340, 105)), Color.GREY, 4, List.of(City.SEATTLE, City.CALGARY)),
        new Route(List.of(new Line(360, 85, 415, 62), new Line(415, 62, 465, 53), new Line(465, 53, 520, 50),
            new Line(520, 50, 580, 53), new Line(580, 53, 625, 65), new Line(625, 65, 675, 90)), Color.WHITE,
            6, List.of(City.CALGARY, City.WINNIPEG)),
        new Route(List.of(new Line(1065, 165, 1100, 132), new Line(1100, 132, 1150, 107),
            new Line(1150, 107, 1205, 87), new Line(1205, 87, 1255, 77), new Line(1255, 77, 1310, 72)),
            Color.BLACK, 5, List.of(City.SAUL_ST_MARIE, City.MONTREAL)),
        new Route(List.of(new Line(1325, 90, 1275, 110), new Line(1275, 110, 1240, 145),
            new Line(1240, 145, 1210, 190)), Color.GREY, 3, List.of(City.MONTREAL, City.TORONTO)),
        new Route(List.of(new Line(1210, 230, 1165, 265), new Line(1165, 265, 1115, 275),
            new Line(1115, 275, 1070, 310), new Line(1070, 310, 1035, 350)), Color.WHITE, 4, List.of(City.TORONTO,
            City.CHICAGO)),
        new Route(List.of(new Line(1055, 350, 1105, 337), new Line(1105, 337, 1160, 330),
            new Line(1160, 330, 1215, 331)), Color.ORANGE, 3, List.of(City.CHICAGO, City.PITTSBURGH)),
        new Route(List.of(new Line(1060, 370, 1115, 355), new Line(1115, 355, 1165, 350),
            new Line(1165, 350, 1220, 351)), Color.BLACK, 3, List.of(City.CHICAGO, City.PITTSBURGH)),
        new Route(List.of(new Line(1025, 352, 965, 337), new Line(965, 337, 915, 320),
            new Line(915, 320, 865, 290)), Color.RED, 3, List.of(City.DULUTH, City.CHICAGO)),
        new Route(List.of(new Line(1025, 375, 920, 355), new Line(915, 360, 830, 415)),
                Color.BLUE, 4, List.of(City.OMAHA, City.CHICAGO)),
        new Route(List.of(new Line(795, 425, 745, 435), new Line(745, 435, 693, 450),
            new Line(693, 450, 643, 470), new Line(643, 470, 593, 510)), Color.PURPLE, 4, List.of(City.OMAHA,
            City.DENVER)),
        new Route(List.of(new Line(613, 520, 663, 525), new Line(663, 525, 718, 526),
            new Line(718, 525, 768, 515), new Line(768, 515, 823, 495)), Color.BLACK, 4, List.of(City.DENVER,
            City.KANSAS_CITY)),
        new Route(List.of(new Line(613, 542, 663, 545), new Line(663, 545, 713, 546),
            new Line(713, 545, 773, 535), new Line(773, 535, 823, 515)), Color.ORANGE, 4, List.of(City.DENVER,
            City.KANSAS_CITY)),
        new Route(List.of(new Line(596, 550, 636, 590), new Line(636, 590, 686, 613),
            new Line(686, 613, 736, 618), new Line(736, 618, 786, 623)), Color.RED, 4, List.of(City.DENVER,
            City.OKLAHOMA_CITY)),
        new Route(List.of(new Line(801, 638, 768, 678), new Line(768, 678, 728, 718),
            new Line(728, 718, 678, 750), new Line(678, 750, 638, 770), new Line(638, 770, 588, 790)),
            Color.YELLOW, 5, List.of(City.EL_PASO, City.OKLAHOMA_CITY)),
        new Route(List.of(new Line(578, 815, 628, 840), new Line(628, 840, 673, 855),
            new Line(673, 855, 728, 863), new Line(728, 863, 778, 865), new Line(778, 865, 828, 860),
            new Line(828, 860, 888, 835)), Color.GREEN, 6, List.of(City.EL_PASO, City.HOUSTON)),
        new Route(List.of(new Line(538, 802, 488, 815), new Line(488, 815, 428, 820),
            new Line(428, 820, 373, 812), new Line(373, 812, 318, 800), new Line(318, 800, 268, 780),
            new Line(268, 780, 218, 740)), Color.BLACK, 6, List.of(City.LOS_ANGELES, City.EL_PASO)),
        new Route(List.of(new Line(218, 725, 268, 717), new Line(268, 717, 318, 718),
                new Line(318, 717, 378, 727)), Color.GREY, 3, List.of(City.LOS_ANGELES, City.PHOENIX)),
        new Route(List.of(new Line(393, 727, 413, 677), new Line(413, 677, 443, 627),
            new Line(443, 627, 478, 577), new Line(478, 577, 518, 547), new Line(518, 547, 563, 537)),
            Color.WHITE, 5, List.of(City.PHOENIX, City.DENVER)),
        new Route(List.of(new Line(193, 720, 158, 678), new Line(158, 678, 123, 628),
            new Line(123, 628, 103, 578)), Color.PURPLE, 3, List.of(City.SAN_FRANCISCO, City.LOS_ANGELES)),
        new Route(List.of(new Line(183, 735, 143, 695), new Line(143, 695, 108, 640),
            new Line(108, 640, 88, 590)), Color.YELLOW, 3, List.of(City.SAN_FRANCISCO, City.LOS_ANGELES)),
        new Route(List.of(new Line(78, 550, 63, 500), new Line(63, 500, 58, 450),
            new Line(58, 450, 63, 395), new Line(63, 395, 68, 340), new Line(68, 340, 88, 290)),
            Color.GREEN, 5, List.of(City.SAN_FRANCISCO, City.PORTLAND)),
        new Route(List.of(new Line(98, 555, 83, 505), new Line(83, 505, 78, 455),
            new Line(78, 455, 78, 400), new Line(78, 400, 88, 350), new Line(88, 350, 108, 295)),
            Color.PURPLE, 5, List.of(City.SAN_FRANCISCO, City.PORTLAND)),
        new Route(List.of(new Line(128, 275, 178, 285), new Line(178, 285, 228, 305),
            new Line(228, 305, 278, 335), new Line(278, 335, 328, 380), new Line(328, 380, 370, 430),
            new Line(370, 430, 380, 460)), Color.BLUE, 6, List.of(City.PORTLAND, City.SALT_LAKE_CITY)),
        new Route(List.of(new Line(385, 495, 380, 545), new Line(380, 545, 360, 600),
            new Line(360, 600, 320, 640)), Color.ORANGE, 3, List.of(City.SALT_LAKE_CITY, City.LAS_VEGAS)),
        new Route(List.of(new Line(285, 645, 235, 655), new Line(235, 655, 205, 705)), Color.GREY, 2, List.of(City.LOS_ANGELES, City.LAS_VEGAS)),
        new Route(List.of(new Line(1080, 810, 1125, 785), new Line(1125, 785, 1180, 768), new Line(1180, 768, 1240, 767),
            new Line(1240, 767, 1290, 797), new Line(1290, 797, 1340, 840), new Line(1340, 840, 1365, 870)),
            Color.RED, 6, List.of(City.NEW_ORLEANS, City.MIAMI)),
        new Route(List.of(new Line(1065, 800, 1085, 750), new Line(1085, 750, 1120, 700),
            new Line(1120, 700, 1160, 653), new Line(1160, 653, 1190, 628)), Color.ORANGE, 4, List.of(City.NEW_ORLEANS,
            City.ATLANTA)),
        new Route(List.of(new Line(1050, 790, 1070, 740), new Line(1070, 740, 1095, 695),
            new Line(1095, 695, 1145, 640), new Line(1145, 640, 1175, 615)), Color.YELLOW, 4, List.of(City.NEW_ORLEANS, City.ATLANTA)),
        new Route(List.of(new Line(1390, 840, 1365, 790), new Line(1365, 790, 1350, 740),
            new Line(1350, 740, 1340, 690), new Line(1340, 690, 1340, 635)), Color.PURPLE, 4, List.of(City.CHARLESTON,
            City.MIAMI)),
        new Route(List.of(new Line(1340, 610, 1365, 565), new Line(1355, 570, 1305, 535)), Color.GREY, 2,
            List.of(City.CHARLESTON, City.RALEIGH)),
        new Route(List.of(new Line(1280, 510, 1230, 500), new Line(1230, 500, 1175, 515),
            new Line(1175, 515, 1125, 545)), Color.BLACK, 3, List.of(City.RALEIGH, City.NASHVILLE)),
        new Route(List.of(new Line(1100, 545, 1125, 495), new Line(1125, 495, 1160, 455), new Line(1160, 455, 1210, 420), new Line(1210, 420, 1240, 380)), Color.YELLOW, 4, List.of(City.NASHVILLE,
            City.PITTSBURGH)),
        new Route(List.of(new Line(1110, 570, 1070, 605), new Line(1070, 605, 1020, 625), new Line(1020, 625, 965, 630)), Color.WHITE, 3, List.of(City.LITTLE_ROCK, City.NASHVILLE)),
    };

    public List<Route> createGameRoutes(GameID gameID) throws CloneNotSupportedException, DatabaseException {
        ArrayList<Route> routes = new ArrayList<>();
        for (Route route: staticRoutes) {
            Route clonedRoute = route.cloning();
            clonedRoute.setGameID(gameID);
            createRoute(clonedRoute);
            for (Line line: clonedRoute.getLines()) {
                line.setRouteID(clonedRoute.getRouteID());
                createLine(line);
            }
            routes.add(clonedRoute);
        }
        return routes;
    }

    public Route createRoute(Route route) throws DatabaseException {
        try (Database database = new Database()) {
            RouteDAO dao = database.getRouteDAO();
            dao.addRoute(route);
            database.commit();
            return route;
        }
    }

    public Route updateRoute(Route route) throws DatabaseException {
        try (Database database = new Database()) {
            RouteDAO dao = database.getRouteDAO();
            dao.updateRoute(route);
            database.commit();
            return route;
        }
    }

    public List<Route> getGameRoutes(GameID gameID) throws DatabaseException {
        try (Database database = new Database()) {
            RouteDAO dao = database.getRouteDAO();
            List<Route> routes = dao.getRoutes(gameID);
            for (Route route: routes) { route.setLines(getRouteLines(route.getRouteID())); }
            return routes;
        }
    }

    public List<Route> getPlayerRoutes(PlayerID playerID) throws DatabaseException {
        try (var db = new Database()){
            return db.getRouteDAO().getPlayerRoutes(playerID);
        }
    }

    public Line createLine(Line line) throws DatabaseException {
        try (Database database = new Database()) {
            LineDAO dao = database.getLineDAO();
            dao.createLine(line);
            database.commit();
            return line;
        }
    }

    public List<Line> getRouteLines(RouteID routeID) throws DatabaseException {
        try (Database database = new Database()) {
            LineDAO dao = database.getLineDAO();
            List<Line> lines = dao.getLines(routeID);
            return lines;
        }
    }

    public List<Player> awardEndOfGamePoints(Game game) throws DatabaseException {

        List<Player> players = PlayerHelper.getSingleton().getGamePlayers(game);
        List<Player> winners = new ArrayList<>();

        double longestPath = -1;
        for (var player : players) {

            var routes = getPlayerRoutes(player.getPlayerID());
            var routeGraph = getPlayerRouteGraph(routes);
            double playersLongestPath = getLongestRouteInRouteGraph(routeGraph);

            awardDestinationCardPoints(player, routeGraph);

            if (playersLongestPath == longestPath)
                winners.add(player);
            if (playersLongestPath > longestPath) {
                longestPath = playersLongestPath;
                winners.clear();
                winners.add(player);
            }
        }

        winners.forEach((player -> player.givePoints(LONGEST_ROUTE_POINT_BONUS)));
        for (Player player : players) PlayerHelper.getSingleton().updatePlayerPoints(player);
        return players;
    }

    public void awardDestinationCardPoints(Player player, Graph<City, WeightedEdge> graph)
            throws DatabaseException {

        var destinationCards = DestinationCardFacadeHelper.getSingleton().destinationCardsInPlayersHand(player);

        ConnectivityInspector<City, WeightedEdge> inspector = new ConnectivityInspector<>(graph);

        for (var card : destinationCards) {
            if (inspector.pathExists(card.getDestination1(), card.getDestination2()))
                player.givePoints(card.getPointValue().asInt());
            else
                player.takePoints(card.getPointValue().asInt());
        }

    }

    protected int getLongestRouteInRouteGraph(Graph<City, WeightedEdge> graph) {

        Set<WeightedEdge> visited = new HashSet<>();
        int maxLength = 0;

        for (var vertex : graph.vertexSet()) {

            int length = recursivePathLengthCalculate(graph, vertex, visited);
            maxLength = (length > maxLength)? length : maxLength;

        }

        return maxLength;
    }

    private int recursivePathLengthCalculate(Graph<City, WeightedEdge> graph, City currentVertex, Set<WeightedEdge> visited) {

        int pathLength = 0;

        for (var edge : graph.edgesOf(currentVertex)) {

            if (visited.contains(edge))
                continue;

            var currentLenght = (int) edge.weight();
            var nextVertex = (graph.getEdgeSource(edge) != currentVertex) ?
                    graph.getEdgeSource(edge) : graph.getEdgeTarget(edge);

            visited.add(edge);
            currentLenght += recursivePathLengthCalculate(graph, nextVertex, visited);
            visited.remove(edge);

            if (currentLenght > pathLength) pathLength = currentLenght;
        }

        return pathLength;
    }

    protected Graph<City, WeightedEdge> getPlayerRouteGraph(List<Route> routes) {
        Graph<City, WeightedEdge> graph = new SimpleWeightedGraph<>(WeightedEdge.class);

        routes.stream().collect(Collector.of(
                (Supplier<TreeSet<City>>) TreeSet::new,
                (result, route) -> result.addAll(route.getCities()),
                (result1, result2) -> {
                    result1.addAll(result2);
                    return result1;
                },
                ArrayList::new
        )).forEach(graph::addVertex);

        routes.forEach(route -> graph.setEdgeWeight(
                graph.addEdge(route.getCities().get(0), route.getCities().get(1)),
                route.getSpaces()
        ));
        return graph;
    }

    public static class WeightedEdge extends DefaultWeightedEdge {
        @Override
        public boolean equals(Object obj) {
            if (obj == null || obj.getClass() != this.getClass())
                return false;
            WeightedEdge edge = (WeightedEdge) obj;
            if (getSource().equals(edge.getSource()) && getTarget().equals(edge.getTarget()))
                return true;
            else return getSource().equals(edge.getTarget()) && getTarget().equals(edge.getSource());
        }

        @Override
        public int hashCode() {
            return getSource().hashCode() + getTarget().hashCode();
        }

        public double weight() {
            return getWeight();
        }
    }
}
