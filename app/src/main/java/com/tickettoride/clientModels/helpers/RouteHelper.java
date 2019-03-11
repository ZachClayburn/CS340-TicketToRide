package com.tickettoride.clientModels.helpers;

import com.tickettoride.clientModels.DataManager;
import com.tickettoride.clientModels.Line;
import com.tickettoride.clientModels.Route;
import com.tickettoride.models.City;
import com.tickettoride.models.Color;
import com.tickettoride.models.Hand;
import com.tickettoride.models.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RouteHelper {
    private static RouteHelper SINGLETON = new RouteHelper();
    public static RouteHelper getSingleton() { return SINGLETON; }
    private RouteHelper() {}

    public List<Route> playerFilteredRoutes() {
        List<Route> routes = DataManager.getSINGLETON().getRoutes();
        List<Route> filteredRoutes = new ArrayList<>();
        Hand hand = DataManager.getSINGLETON().getPlayerHand();
        for (int i = 0; i < routes.size(); ++i) {
            switch(routes.get(i).getColor()){
                case YELLOW:
                    if (hand.getYellow() + hand.getLocomotive() >= routes.get(i).getSpaces() && !routes.get(i).getIsClaimed()) {
                        filteredRoutes.add(routes.get(i));
                    }
                    break;
                case RED:
                    if (hand.getRed() + hand.getLocomotive() >= routes.get(i).getSpaces() && !routes.get(i).getIsClaimed()){
                        filteredRoutes.add(routes.get(i));
                    }
                    break;
                case BLUE:
                    if (hand.getBlue() + hand.getLocomotive() >= routes.get(i).getSpaces() && !routes.get(i).getIsClaimed()) {
                        filteredRoutes.add(routes.get(i));
                    }
                    break;
                case PURPLE:
                    if (hand.getPurple() + hand.getLocomotive() >= routes.get(i).getSpaces() && !routes.get(i).getIsClaimed()) {
                        filteredRoutes.add(routes.get(i));
                    }
                    break;
                case BLACK:
                    if (hand.getBlack() + hand.getLocomotive() >= routes.get(i).getSpaces() && !routes.get(i).getIsClaimed()) {
                        filteredRoutes.add(routes.get(i));
                    }
                    break;
                case GREEN:
                    if (hand.getGreen() + hand.getLocomotive() >= routes.get(i).getSpaces() && !routes.get(i).getIsClaimed()) {
                        filteredRoutes.add(routes.get(i));
                    }
                    break;
                case WHITE:
                    if (hand.getWhite() + hand.getLocomotive() >= routes.get(i).getSpaces() && !routes.get(i).getIsClaimed()) {
                        filteredRoutes.add(routes.get(i));
                    }
                    break;
                case ORANGE:
                    if (hand.getOrange() + hand.getLocomotive() >= routes.get(i).getSpaces() && !routes.get(i).getIsClaimed()) {
                        filteredRoutes.add(routes.get(i));
                    }
                    break;
                case GREY:
                    if (hand.getYellow() + hand.getLocomotive() >= routes.get(i).getSpaces() && !routes.get(i).getIsClaimed()) {
                        filteredRoutes.add(routes.get(i));
                        break;
                    }
                    if (hand.getRed() + hand.getLocomotive() >= routes.get(i).getSpaces() && !routes.get(i).getIsClaimed()) {
                        filteredRoutes.add(routes.get(i));
                        break;
                    }
                    if (hand.getBlue() + hand.getLocomotive() >= routes.get(i).getSpaces() && !routes.get(i).getIsClaimed()) {
                        filteredRoutes.add(routes.get(i));
                        break;
                    }
                    if (hand.getPurple() + hand.getLocomotive() >= routes.get(i).getSpaces() && !routes.get(i).getIsClaimed()) {
                        filteredRoutes.add(routes.get(i));
                        break;
                    }
                    if (hand.getBlack() + hand.getLocomotive() >= routes.get(i).getSpaces() && !routes.get(i).getIsClaimed()) {
                        filteredRoutes.add(routes.get(i));
                        break;
                    }
                    if (hand.getGreen() + hand.getLocomotive() >= routes.get(i).getSpaces() && !routes.get(i).getIsClaimed()) {
                        filteredRoutes.add(routes.get(i));
                        break;
                    }
                    if (hand.getWhite() + hand.getLocomotive() >= routes.get(i).getSpaces() && !routes.get(i).getIsClaimed()) {
                        filteredRoutes.add(routes.get(i));
                        break;
                    }
                    if (hand.getOrange() + hand.getLocomotive() >= routes.get(i).getSpaces() && !routes.get(i).getIsClaimed()) {
                        filteredRoutes.add(routes.get(i));
                        break;
                    }
                    break;
            }
        }
        return filteredRoutes;
    }

    public void buildRoutes() {
        if (DataManager.getSINGLETON().getRoutes() == null) {
            ArrayList<Route> routes = new ArrayList<>();
            routes.add(new Route(Arrays.asList(new Line(150, 130, 150, 180)), Color.GREY, 1, City.VANCOUVER, City.SEATTLE, false, null));
            routes.add(new Route(Arrays.asList(new Line(130, 130, 130, 180)), Color.GREY, 1, City.VANCOUVER, City.SEATTLE, false, null));
            routes.add(new Route(Arrays.asList(new Line(160, 108, 330, 88)), Color.GREY, 3, City.VANCOUVER, City.CALGARY, false, null));
            routes.add(new Route(Arrays.asList(new Line(350, 98, 495, 273)), Color.GREY, 4, City.CALGARY, City.HELENA, false, null));
            routes.add(new Route(Arrays.asList(new Line(510, 268, 665, 118)), Color.BLUE, 4, City.HELENA, City.WINNIPEG, false, null));
            routes.add(new Route(Arrays.asList(new Line(695, 118, 850, 268)), Color.BLACK, 4, City.WINNIPEG, City.DULUTH, false, null));
            routes.add(new Route(Arrays.asList(new Line(515, 288, 845, 283)), Color.ORANGE, 6, City.HELENA, City.DULUTH, false, null));
            routes.add(new Route(Arrays.asList(new Line(155, 215, 485, 293)), Color.YELLOW, 6, City.SEATTLE, City.HELENA, false, null));
            routes.add(new Route(Arrays.asList(new Line(142, 213, 123, 260)), Color.GREY, 1, City.SEATTLE, City.PORTLAND, false, null));
            routes.add(new Route(Arrays.asList(new Line(122, 208, 103, 255)), Color.GREY, 1, City.SEATTLE, City.PORTLAND, false, null));
            routes.add(new Route(Arrays.asList(new Line(485, 308, 400, 450)), Color.PURPLE, 3, City.HELENA, City.SALT_LAKE_CITY, false, null));
            routes.add(new Route(Arrays.asList(new Line(505, 308, 583, 500)), Color.GREEN, 4, City.HELENA, City.DENVER, false, null));
            routes.add(new Route(Arrays.asList(new Line(525, 305, 783, 410)), Color.RED, 5, City.HELENA, City.OMAHA, false, null));
            routes.add(new Route(Arrays.asList(new Line(405, 467, 563, 500)), Color.RED, 3, City.SALT_LAKE_CITY, City.DENVER, false, null));
            routes.add(new Route(Arrays.asList(new Line(405, 487, 563, 520)), Color.YELLOW, 3, City.SALT_LAKE_CITY, City.DENVER, false, null));
            routes.add(new Route(Arrays.asList(new Line(365, 473, 110, 557)), Color.ORANGE, 5, City.SAN_FRANCISCO, City.SALT_LAKE_CITY, false, null));
            routes.add(new Route(Arrays.asList(new Line(365, 493, 115, 577)), Color.WHITE, 5, City.SAN_FRANCISCO, City.SALT_LAKE_CITY, false, null));
            routes.add(new Route(Arrays.asList(new Line(1040, 170, 705, 100)), Color.GREY, 6, City.WINNIPEG, City.SAUL_ST_MARIE, false, null));
            routes.add(new Route(Arrays.asList(new Line(1070, 180, 1180, 205)), Color.GREY, 2, City.SAUL_ST_MARIE, City.TORONTO, false, null));
            routes.add(new Route(Arrays.asList(new Line(1040, 190, 885, 255)), Color.GREY, 3, City.SAUL_ST_MARIE, City.DULUTH, false, null));
            routes.add(new Route(Arrays.asList(new Line(1200, 222, 875, 277)), Color.PURPLE, 6, City.DULUTH, City.TORONTO, false, null));
            routes.add(new Route(Arrays.asList(new Line(1230, 227, 1235, 327)), Color.GREY, 2, City.TORONTO, City.PITTSBURGH, false, null));
            routes.add(new Route(Arrays.asList(new Line(1345, 272, 1250, 332)), Color.WHITE, 2, City.PITTSBURGH, City.NEW_YORK, false, null));
            routes.add(new Route(Arrays.asList(new Line(1360, 287, 1265, 347)), Color.GREEN, 2, City.PITTSBURGH, City.NEW_YORK, false, null));
            routes.add(new Route(Arrays.asList(new Line(1370, 302, 1375, 407)), Color.ORANGE, 2, City.NEW_YORK, City.WASHINGTON, false, null));
            routes.add(new Route(Arrays.asList(new Line(1390, 302, 1395, 407)), Color.BLACK, 2, City.NEW_YORK, City.WASHINGTON, false, null));
            routes.add(new Route(Arrays.asList(new Line(1360, 262, 1330, 100)), Color.BLUE, 3, City.MONTREAL, City.NEW_YORK, false, null));
            routes.add(new Route(Arrays.asList(new Line(1380, 267, 1435, 180)), Color.YELLOW, 2, City.NEW_YORK, City.BOSTON, false, null));
            routes.add(new Route(Arrays.asList(new Line(1400, 277, 1455, 190)), Color.RED, 2, City.NEW_YORK, City.BOSTON, false, null));
            routes.add(new Route(Arrays.asList(new Line(1350, 97, 1435, 165)), Color.GREY, 2, City.BOSTON, City.MONTREAL, false, null));
            routes.add(new Route(Arrays.asList(new Line(1360, 82, 1445, 150)), Color.GREY, 2, City.BOSTON, City.MONTREAL, false, null));
            routes.add(new Route(Arrays.asList(new Line(1367, 427, 1300, 505)), Color.GREY, 2, City.WASHINGTON, City.RALEIGH, false, null));
            routes.add(new Route(Arrays.asList(new Line(1382, 442, 1315, 520)), Color.GREY, 2, City.WASHINGTON, City.RALEIGH, false, null));
            routes.add(new Route(Arrays.asList(new Line(1292, 538, 1213, 607)), Color.GREY, 2, City.RALEIGH, City.ATLANTA, false, null));
            routes.add(new Route(Arrays.asList(new Line(1281, 524, 1202, 593)), Color.GREY, 2, City.RALEIGH, City.ATLANTA, false, null));
            routes.add(new Route(Arrays.asList(new Line(1281, 494, 1258, 385)), Color.GREY, 2, City.RALEIGH, City.PITTSBURGH, false, null));
            routes.add(new Route(Arrays.asList(new Line(1265, 365, 1365, 415)), Color.GREY, 2, City.WASHINGTON, City.PITTSBURGH, false, null));
            routes.add(new Route(Arrays.asList(new Line(1230, 365, 995, 502)), Color.GREEN, 5, City.PITTSBURGH, City.SAINT_LOUIS, false, null));
            routes.add(new Route(Arrays.asList(new Line(990, 524, 1090, 554)), Color.GREY, 2, City.SAINT_LOUIS, City.NASHVILLE, false, null));
            routes.add(new Route(Arrays.asList(new Line(975, 514, 947, 602)), Color.GREY, 2, City.KANSAS_CITY, City.LITTLE_ROCK, false, null));
            routes.add(new Route(Arrays.asList(new Line(937, 625, 832, 630)), Color.GREY, 2, City.LITTLE_ROCK, City.OKLAHOMA_CITY, false, null));
            routes.add(new Route(Arrays.asList(new Line(837, 640, 850, 747)), Color.GREY, 2, City.OKLAHOMA_CITY, City.DALLAS, false, null));
            routes.add(new Route(Arrays.asList(new Line(817, 640, 830, 747)), Color.GREY, 2, City.OKLAHOMA_CITY, City.DALLAS, false, null));
            routes.add(new Route(Arrays.asList(new Line(808, 612, 837, 510)), Color.GREY, 2, City.OKLAHOMA_CITY, City.KANSAS_CITY, false, null));
            routes.add(new Route(Arrays.asList(new Line(828, 615, 857, 513)), Color.GREY, 2, City.OKLAHOMA_CITY, City.KANSAS_CITY, false, null));
            routes.add(new Route(Arrays.asList(new Line(857, 505, 959, 502)), Color.PURPLE, 2, City.KANSAS_CITY, City.SAINT_LOUIS, false, null));
            routes.add(new Route(Arrays.asList(new Line(857, 485, 959, 486)), Color.BLUE, 2, City.KANSAS_CITY, City.SAINT_LOUIS, false, null));
            routes.add(new Route(Arrays.asList(new Line(964, 475, 1025, 382)), Color.GREEN, 2, City.SAINT_LOUIS, City.CHICAGO, false, null));
            routes.add(new Route(Arrays.asList(new Line(980, 485, 1040, 392)), Color.WHITE, 2, City.SAINT_LOUIS, City.CHICAGO, false, null));
            routes.add(new Route(Arrays.asList(new Line(937, 640, 867, 740)), Color.GREY, 2, City.LITTLE_ROCK, City.DALLAS, false, null));
            routes.add(new Route(Arrays.asList(new Line(863, 765, 897, 805)), Color.GREY, 1, City.DALLAS, City.HOUSTON, false, null));
            routes.add(new Route(Arrays.asList(new Line(848, 780, 882, 820)), Color.GREY, 1, City.DALLAS, City.HOUSTON, false, null));
            routes.add(new Route(Arrays.asList(new Line(917, 820, 1027, 805)), Color.GREY, 2, City.HOUSTON, City.NEW_ORLEANS, false, null));
            routes.add(new Route(Arrays.asList(new Line(1037, 795, 957, 650)), Color.GREEN, 3, City.LITTLE_ROCK, City.NEW_ORLEANS, false, null));
            routes.add(new Route(Arrays.asList(new Line(1130, 566, 1175, 599)), Color.GREY, 1, City.NASHVILLE, City.ATLANTA, false, null));
            routes.add(new Route(Arrays.asList(new Line(1215, 624, 1325, 630)), Color.GREY, 2, City.ATLANTA, City.CHARLESTON, false, null));
            routes.add(new Route(Arrays.asList(new Line(1200, 634, 1375, 855)), Color.BLUE, 5, City.ATLANTA, City.MIAMI, false, null));
            routes.add(new Route(Arrays.asList(new Line(850, 475, 827, 430)), Color.GREY, 1, City.OMAHA, City.KANSAS_CITY, false, null));
            routes.add(new Route(Arrays.asList(new Line(835, 485, 812, 440)), Color.GREY, 1, City.OMAHA, City.KANSAS_CITY, false, null));
            routes.add(new Route(Arrays.asList(new Line(859, 303, 822, 400)), Color.GREY, 2, City.OMAHA, City.DULUTH, false, null));
            routes.add(new Route(Arrays.asList(new Line(840, 300, 804, 398)), Color.GREY, 2, City.OMAHA, City.DULUTH, false, null));
            routes.add(new Route(Arrays.asList(new Line(822, 772, 610, 805)), Color.RED, 4, City.EL_PASO, City.DALLAS, false, null));
            routes.add(new Route(Arrays.asList(new Line(550, 792, 400, 747)), Color.GREY, 3, City.PHOENIX, City.EL_PASO, false, null));
            routes.add(new Route(Arrays.asList(new Line(413, 729, 560, 664)), Color.GREY, 3, City.PHOENIX, City.SANTA_FE, false, null));
            routes.add(new Route(Arrays.asList(new Line(590, 662, 760, 641)), Color.BLUE, 3, City.SANTA_FE, City.OKLAHOMA_CITY, false, null));
            routes.add(new Route(Arrays.asList(new Line(575, 647, 580, 540)), Color.GREY, 2, City.SANTA_FE, City.DENVER, false, null));
            routes.add(new Route(Arrays.asList(new Line(575, 677, 570, 784)), Color.GREY, 2, City.EL_PASO, City.SANTA_FE, false, null));
            routes.add(new Route(Arrays.asList(new Line(160, 195, 220, 196), new Line(220, 195, 265, 190),
                    new Line(265, 190, 320, 150), new Line(320, 150, 340, 105)), Color.GREY, 4, City.SEATTLE, City.CALGARY, false, null));
            routes.add(new Route(Arrays.asList(new Line(360, 85, 415, 62), new Line(415, 62, 465, 53), new Line(465, 53, 520, 50),
                    new Line(520, 50, 580, 53), new Line(580, 53, 625, 65), new Line(625, 65, 675, 90)), Color.WHITE,
                    6, City.CALGARY, City.WINNIPEG, false, null));
            routes.add(new Route(Arrays.asList(new Line(1065, 165, 1100, 132), new Line(1100, 132, 1150, 107),
                    new Line(1150, 107, 1205, 87), new Line(1205, 87, 1255, 77), new Line(1255, 77, 1310, 72)),
                    Color.BLACK, 5, City.SAUL_ST_MARIE, City.MONTREAL, false, null));
            routes.add(new Route(Arrays.asList(new Line(1325, 90, 1275, 110), new Line(1275, 110, 1240, 145),
                    new Line(1240, 145, 1210, 190)), Color.GREY, 3, City.MONTREAL, City.TORONTO, false, null));
            routes.add(new Route(Arrays.asList(new Line(1210, 230, 1165, 265), new Line(1165, 265, 1115, 275),
                    new Line(1115, 275, 1070, 310), new Line(1070, 310, 1035, 350)), Color.WHITE, 4, City.TORONTO,
                    City.CHICAGO, false, null));
            routes.add(new Route(Arrays.asList(new Line(1055, 350, 1105, 337), new Line(1105, 337, 1160, 330),
                    new Line(1160, 330, 1215, 331)), Color.ORANGE, 3, City.CHICAGO, City.PITTSBURGH, false, null));
            routes.add(new Route(Arrays.asList(new Line(1060, 370, 1115, 355), new Line(1115, 355, 1165, 350),
                    new Line(1165, 350, 1220, 351)), Color.BLACK, 3, City.CHICAGO, City.PITTSBURGH, false, null));
            routes.add(new Route(Arrays.asList(new Line(1025, 352, 965, 337), new Line(965, 337, 915, 320),
                    new Line(915, 320, 865, 290)), Color.RED, 3, City.DULUTH, City.CHICAGO, false, null));
            routes.add(new Route(Arrays.asList(new Line(1025, 375, 920, 355), new Line(915, 360, 830, 415)),
                    Color.BLUE, 4, City.OMAHA, City.CHICAGO, false, null));
            routes.add(new Route(Arrays.asList(new Line(795, 425, 745, 435), new Line(745, 435, 693, 450),
                    new Line(693, 450, 643, 470), new Line(643, 470, 593, 510)), Color.PURPLE, 4, City.OMAHA,
                    City.DENVER, false, null));
            routes.add(new Route(Arrays.asList(new Line(613, 520, 663, 525), new Line(663, 525, 718, 526),
                    new Line(718, 525, 768, 515), new Line(768, 515, 823, 495)), Color.BLACK, 4, City.DENVER,
                    City.KANSAS_CITY, false, null));
            routes.add(new Route(Arrays.asList(new Line(613, 542, 663, 545), new Line(663, 545, 713, 546),
                    new Line(713, 545, 773, 535), new Line(773, 535, 823, 515)), Color.ORANGE, 4, City.DENVER,
                    City.KANSAS_CITY, false, null));
            routes.add(new Route(Arrays.asList(new Line(596, 550, 636, 590), new Line(636, 590, 686, 613),
                    new Line(686, 613, 736, 618), new Line(736, 618, 786, 623)), Color.RED, 4, City.DENVER,
                    City.OKLAHOMA_CITY, false, null));
            routes.add(new Route(Arrays.asList(new Line(801, 638, 768, 678), new Line(768, 678, 728, 718),
                    new Line(728, 718, 678, 750), new Line(678, 750, 638, 770), new Line(638, 770, 588, 790)),
                    Color.YELLOW, 5, City.EL_PASO, City.OKLAHOMA_CITY, false, null));
            routes.add(new Route(Arrays.asList(new Line(578, 815, 628, 840), new Line(628, 840, 673, 855),
                    new Line(673, 855, 728, 863), new Line(728, 863, 778, 865), new Line(778, 865, 828, 860),
                    new Line(828, 860, 888, 835)), Color.GREEN, 6, City.EL_PASO, City.HOUSTON, false, null));
            routes.add(new Route(Arrays.asList(new Line(538, 802, 488, 815), new Line(488, 815, 428, 820),
                    new Line(428, 820, 373, 812), new Line(373, 812, 318, 800), new Line(318, 800, 268, 780),
                    new Line(268, 780, 218, 740)), Color.BLACK, 6, City.LOS_ANGELES, City.EL_PASO, false, null));
            routes.add(new Route(Arrays.asList(new Line(218, 725, 268, 717), new Line(268, 717, 318, 718),
                    new Line(318, 717, 378, 727)), Color.GREY, 3, City.LOS_ANGELES, City.PHOENIX, false, null));
            routes.add(new Route(Arrays.asList(new Line(393, 727, 413, 677), new Line(413, 677, 443, 627),
                    new Line(443, 627, 478, 577), new Line(478, 577, 518, 547), new Line(518, 547, 563, 537)),
                    Color.WHITE, 5, City.PHOENIX, City.DENVER, false, null));
            routes.add(new Route(Arrays.asList(new Line(193, 720, 158, 678), new Line(158, 678, 123, 628),
                    new Line(123, 628, 103, 578)), Color.PURPLE, 3, City.SAN_FRANCISCO, City.LOS_ANGELES, false, null));
            routes.add(new Route(Arrays.asList(new Line(183, 735, 143, 695), new Line(143, 695, 108, 640),
                    new Line(108, 640, 88, 590)), Color.YELLOW, 3, City.SAN_FRANCISCO, City.LOS_ANGELES, false, null));
            routes.add(new Route(Arrays.asList(new Line(78, 550, 63, 500), new Line(63, 500, 58, 450),
                    new Line(58, 450, 63, 395), new Line(63, 395, 68, 340), new Line(68, 340, 88, 290)),
                    Color.GREEN, 5, City.SAN_FRANCISCO, City.PORTLAND, false, null));
            routes.add(new Route(Arrays.asList(new Line(98, 555, 83, 505), new Line(83, 505, 78, 455),
                    new Line(78, 455, 78, 400), new Line(78, 400, 88, 350), new Line(88, 350, 108, 295)),
                    Color.PURPLE, 5, City.SAN_FRANCISCO, City.PORTLAND, false, null));
            routes.add(new Route(Arrays.asList(new Line(128, 275, 178, 285), new Line(178, 285, 228, 305),
                    new Line(228, 305, 278, 335), new Line(278, 335, 328, 380), new Line(328, 380, 370, 430),
                    new Line(370, 430, 380, 460)), Color.BLUE, 6, City.PORTLAND, City.SALT_LAKE_CITY, false, null));
            routes.add(new Route(Arrays.asList(new Line(385, 495, 380, 545), new Line(380, 545, 360, 600),
                    new Line(360, 600, 320, 640)), Color.ORANGE, 3, City.SALT_LAKE_CITY, City.LAS_VEGAS, false, null));
            routes.add(new Route(Arrays.asList(new Line(285, 645, 235, 655), new Line(235, 655, 205, 705)), Color.GREY, 2,
                    City.LOS_ANGELES, City.LAS_VEGAS, false, null));
            routes.add(new Route(Arrays.asList(new Line(1080, 810, 1125, 785), new Line(1125, 785, 1180, 768),
                    new Line(1180, 768, 1240, 767), new Line(1240, 767, 1290, 797), new Line(1290, 797, 1340, 840),
                    new Line(1340, 840, 1365, 870)), Color.RED, 6, City.NEW_ORLEANS, City.MIAMI, false, null));
            routes.add(new Route(Arrays.asList(new Line(1065, 800, 1085, 750), new Line(1085, 750, 1120, 700),
                    new Line(1120, 700, 1160, 653), new Line(1160, 653, 1190, 628)), Color.ORANGE, 4, City.NEW_ORLEANS,
                    City.ATLANTA, false, null));
            routes.add(new Route(Arrays.asList(new Line(1050, 790, 1070, 740), new Line(1070, 740, 1095, 695),
                    new Line(1095, 695, 1145, 640), new Line(1145, 640, 1175, 615)), Color.YELLOW, 4, City.NEW_ORLEANS,
                    City.ATLANTA, false, null));
            routes.add(new Route(Arrays.asList(new Line(1390, 840, 1365, 790), new Line(1365, 790, 1350, 740),
                    new Line(1350, 740, 1340, 690), new Line(1340, 690, 1340, 635)), Color.PURPLE, 4, City.CHARLESTON,
                    City.MIAMI, false, null));
            routes.add(new Route(Arrays.asList(new Line(1340, 610, 1365, 565), new Line(1355, 570, 1305, 535)), Color.GREY, 2,
                    City.CHARLESTON, City.RALEIGH, false, null));
            routes.add(new Route(Arrays.asList(new Line(1280, 510, 1230, 500), new Line(1230, 500, 1175, 515),
                    new Line(1175, 515, 1125, 545)), Color.BLACK, 3, City.RALEIGH, City.NASHVILLE, false, null));
            routes.add(new Route(Arrays.asList(new Line(1100, 545, 1125, 495), new Line(1125, 495, 1160, 455),
                    new Line(1160, 455, 1210, 420), new Line(1210, 420, 1240, 380)), Color.YELLOW, 4, City.NASHVILLE,
                    City.PITTSBURGH, false, null));
            routes.add(new Route(Arrays.asList(new Line(1110, 570, 1070, 605), new Line(1070, 605, 1020, 625),
                    new Line(1020, 625, 965, 630)), Color.WHITE, 3, City.LITTLE_ROCK, City.NASHVILLE, false, null));
            DataManager.getSINGLETON().setRoutes(routes);
        }
    }

    private void discardYellow(Hand hand, int colorCards, int routeLength) {
        int requiredLocamotives = routeLength - colorCards;
        if (requiredLocamotives > 0) {
            hand.discardYellow(colorCards);
            hand.discardLocomotive(requiredLocamotives);
        } else {hand.discardYellow(routeLength);}
    }

    private void discardRed(Hand hand, int colorCards, int routeLength) {
        int requiredLocamotives = routeLength - colorCards;
        if (requiredLocamotives > 0) {
            hand.discardRed(colorCards);
            hand.discardLocomotive(requiredLocamotives);
        } else {hand.discardRed(routeLength);}
    }

    private void discardBlue(Hand hand, int colorCards, int routeLength) {
        int requiredLocamotives = routeLength - colorCards;
        if (requiredLocamotives > 0) {
            hand.discardBlue(colorCards);
            hand.discardLocomotive(requiredLocamotives);
        } else {hand.discardBlue(routeLength);}
    }

    private void discardPurple(Hand hand, int colorCards, int routeLength) {
        int requiredLocamotives = routeLength - colorCards;
        if (requiredLocamotives > 0) {
            hand.discardPurple(colorCards);
            hand.discardLocomotive(requiredLocamotives);
        } else {hand.discardPurple(routeLength);}
    }

    private void discardBlack(Hand hand, int colorCards, int routeLength) {
        int requiredLocamotives = routeLength - colorCards;
        if (requiredLocamotives > 0) {
            hand.discardBlack(colorCards);
            hand.discardLocomotive(requiredLocamotives);
        } else {hand.discardBlack(routeLength);}
    }

    private void discardGreen(Hand hand, int colorCards, int routeLength) {
        int requiredLocamotives = routeLength - colorCards;
        if (requiredLocamotives > 0) {
            hand.discardGreen(colorCards);
            hand.discardLocomotive(requiredLocamotives);
        } else {hand.discardGreen(routeLength);}
    }

    private void discardWhite(Hand hand, int colorCards, int routeLength) {
        int requiredLocamotives = routeLength - colorCards;
        if (requiredLocamotives > 0) {
            hand.discardWhite(colorCards);
            hand.discardLocomotive(requiredLocamotives);
        } else {hand.discardWhite(routeLength);}
    }

    private void discardOrange(Hand hand, int colorCards, int routeLength) {
        int requiredLocamotives = routeLength - colorCards;
        if (requiredLocamotives > 0) {
            hand.discardOrange(colorCards);
            hand.discardLocomotive(requiredLocamotives);
        } else {hand.discardOrange(routeLength);}
    }

    private void payForRoute(Route curRoute) {
        Color color = curRoute.getColor();
        int colorCards;
        Hand hand = DataManager.getSINGLETON().getPlayerHand();
        int routeLength = curRoute.getSpaces();
        int requiredLocamotives;
        switch(color) {
            case YELLOW:
                colorCards = hand.getYellow();
                discardYellow(hand, colorCards, routeLength);
                break;
            case RED:
                colorCards = hand.getRed();
                discardRed(hand, colorCards, routeLength);
                break;
            case BLUE:
                colorCards = hand.getBlue();
                discardBlue(hand, colorCards, routeLength);
                break;
            case PURPLE:
                colorCards = hand.getPurple();
                discardPurple(hand, colorCards, routeLength);
                break;
            case BLACK:
                colorCards = hand.getBlack();
                discardBlack(hand, colorCards, routeLength);
                break;
            case GREEN:
                colorCards = hand.getGreen();
                discardGreen(hand, colorCards, routeLength);
                break;
            case WHITE:
                colorCards = hand.getWhite();
                discardWhite(hand, colorCards, routeLength);
                break;
            case ORANGE:
                colorCards = hand.getOrange();
                discardOrange(hand, colorCards, routeLength);
                break;
            case GREY:
                int locamotivesInHand = hand.getLocomotive();
                colorCards = hand.getYellow();
                if ((colorCards + locamotivesInHand) >= routeLength) { discardYellow(hand, colorCards, routeLength); return; }
                colorCards = hand.getRed();
                if ((colorCards + locamotivesInHand) >= routeLength) { discardRed(hand, colorCards, routeLength); return; }
                colorCards = hand.getBlue();
                if ((colorCards + locamotivesInHand) >= routeLength) { discardBlue(hand, colorCards, routeLength); return; }
                colorCards = hand.getPurple();
                if ((colorCards + locamotivesInHand) >= routeLength) { discardPurple(hand, colorCards, routeLength); return; }
                colorCards = hand.getBlack();
                if ((colorCards + locamotivesInHand) >= routeLength) { discardBlack(hand, colorCards, routeLength); return; }
                colorCards = hand.getGreen();
                if ((colorCards + locamotivesInHand) >= routeLength) { discardGreen(hand, colorCards, routeLength); return; }
                colorCards = hand.getWhite();
                if ((colorCards + locamotivesInHand) >= routeLength) { discardWhite(hand, colorCards, routeLength); return; }
                colorCards = hand.getOrange();
                if ((colorCards + locamotivesInHand) >= routeLength) { discardOrange(hand, colorCards, routeLength); return; }
        }
    }

    public void claimRoute(Route curRoute) {
        curRoute.setIsClaimed(true);
        curRoute.setLineColor(DataManager.getSINGLETON().getPlayer().getColor());
        int spaces = curRoute.getSpaces();
        int playerPoints = DataManager.SINGLETON.getPlayer().getPoints();
        DataManager.SINGLETON.getPlayer().setPoints(playerPoints + spaces);
    }

    public void claimRoute(Route curRoute, Player player) {
        curRoute.setIsClaimed(true);
        curRoute.setLineColor(player.getColor());
        int spaces = curRoute.getSpaces();
        player.setPoints(player.getPoints() + spaces);
    }

}
