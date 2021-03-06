package com.tickettoride.models;


import com.google.gson.internal.LinkedTreeMap;
import com.tickettoride.models.idtypes.GameID;
import com.tickettoride.models.idtypes.PlayerID;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class DestinationCard implements Comparable<DestinationCard> {

    private final City destination1;
    private final City destination2;

    // Only used in Mongo
    private GameID gameID = null;
    private int sequencePosition = 0;
    private CardState state = CardState.IN_DECK;
    private PlayerID playerID = null;

    public City getDestination1() {
        return destination1;
    }

    public City getDestination2() {
        return destination2;
    }

    public Value getPointValue() {
        return pointValue;
    }

    private final Value pointValue;

    public DestinationCard(City destination1, City destination2, Value pointValue) {

        this.destination1 = destination1;
        this.destination2 = destination2;
        this.pointValue = pointValue;
    }

    public DestinationCard(City destination1, City destination2, GameID gameID, int sequencePosition,
                           CardState state, PlayerID playerID, Value pointValue) {
        this.destination1 = destination1;
        this.destination2 = destination2;
        this.gameID = gameID;
        this.sequencePosition = sequencePosition;
        this.state = state;
        this.playerID = playerID;
        this.pointValue = pointValue;
    }

    public GameID getGameID() {
        return gameID;
    }

    public void setGameID(GameID gameID) {
        this.gameID = gameID;
    }

    public int getSequencePosition() {
        return sequencePosition;
    }

    public void setSequencePosition(int sequencePosition) {
        this.sequencePosition = sequencePosition;
    }

    public CardState getState() {
        return state;
    }

    public void setState(CardState state) {
        this.state = state;
    }

    public PlayerID getPlayerID() {
        return playerID;
    }

    public void setPlayerID(PlayerID playerID) {
        this.playerID = playerID;
    }

    @Override
    public int compareTo(DestinationCard o) {

        if (this.pointValue.compareTo(o.pointValue) != 0)
            return this.pointValue.compareTo(o.pointValue);
        else if (this.destination1.compareTo(o.destination1) != 0)
            return this.destination1.compareTo(o.destination1);
        else
            return this.destination2.compareTo(o.destination2);
    }

    public enum Value {
        FOUR(4),
        FIVE(5),
        SIX(6),
        SEVEN(7),
        EIGHT(8),
        NINE(9),
        TEN(10),
        ELEVEN(11),
        TWELVE(12),
        THIRTEEN(13),
        SIXTEEN(16),
        SEVENTEEN(17),
        TWENTY(20),
        TWENTY_ONE(21),
        TWENTY_TWO(22);

        private final int pointValue;
        Value(int pointValue){
            this.pointValue = pointValue;
        }

        public int asInt() {
            return pointValue;
        }

        public static Value fromInt(int intValue) {
            return Arrays.stream(Value.values())
                    .parallel()
                    .filter(value -> intValue == value.asInt())
                    .findAny()
                    .orElseThrow(() -> new IllegalArgumentException("Cannot create Value from  integer: " + intValue));
        }
    }


    public static Deque<DestinationCard> getPointOrderedDeck() {

        List<DestinationCard> deck = getDeckList();


        return new ArrayDeque<>(deck);
    }

    private static List<DestinationCard> getDeckList() {
        final int deckSize = 30;
        ArrayList<DestinationCard> deck = new ArrayList<>(deckSize);

        deck.add(new DestinationCard(City.DENVER, City.EL_PASO, Value.FOUR));

        deck.add(new DestinationCard(City.KANSAS_CITY, City.HOUSTON, Value.FIVE));

        deck.add(new DestinationCard(City.NEW_YORK, City.ATLANTA, Value.SIX));

        deck.add(new DestinationCard(City.CHICAGO, City.NEW_ORLEANS, Value.SEVEN));
        deck.add(new DestinationCard(City.CALGARY, City.SALT_LAKE_CITY, Value.SEVEN));

        deck.add(new DestinationCard(City.HELENA, City.LOS_ANGELES, Value.EIGHT));
        deck.add(new DestinationCard(City.DULUTH, City.HOUSTON, Value.EIGHT));
        deck.add(new DestinationCard(City.SAUL_ST_MARIE, City.NASHVILLE, Value.EIGHT));

        deck.add(new DestinationCard(City.MONTREAL, City.ATLANTA, Value.NINE));
        deck.add(new DestinationCard(City.SAUL_ST_MARIE, City.OKLAHOMA_CITY, Value.NINE));
        deck.add(new DestinationCard(City.SEATTLE, City.LOS_ANGELES, Value.NINE));
        deck.add(new DestinationCard(City.CHICAGO, City.SANTA_FE, Value.NINE));

        deck.add(new DestinationCard(City.DULUTH, City.EL_PASO, Value.TEN));
        deck.add(new DestinationCard(City.TORONTO, City.MIAMI, Value.TEN));

        deck.add(new DestinationCard(City.PORTLAND, City.PHOENIX, Value.ELEVEN));
        deck.add(new DestinationCard(City.DALLAS, City.NEW_YORK, Value.ELEVEN));
        deck.add(new DestinationCard(City.DENVER, City.PITTSBURGH, Value.ELEVEN));
        deck.add(new DestinationCard(City.WINNIPEG, City.LITTLE_ROCK, Value.ELEVEN));

        deck.add(new DestinationCard(City.WINNIPEG, City.HOUSTON, Value.TWELVE));
        deck.add(new DestinationCard(City.BOSTON, City.MIAMI, Value.TWELVE));

        deck.add(new DestinationCard(City.VANCOUVER, City.SANTA_FE, Value.THIRTEEN));
        deck.add(new DestinationCard(City.CALGARY, City.PHOENIX, Value.THIRTEEN));
        deck.add(new DestinationCard(City.MONTREAL, City.NEW_ORLEANS, Value.THIRTEEN));

        deck.add(new DestinationCard(City.LOS_ANGELES, City.CHICAGO, Value.SIXTEEN));

        deck.add(new DestinationCard(City.SAN_FRANCISCO, City.ATLANTA, Value.SEVENTEEN));
        deck.add(new DestinationCard(City.PORTLAND, City.NASHVILLE, Value.SEVENTEEN));

        deck.add(new DestinationCard(City.VANCOUVER, City.MONTREAL, Value.TWENTY));
        deck.add(new DestinationCard(City.LOS_ANGELES, City.MIAMI, Value.TWENTY));

        deck.add(new DestinationCard(City.LOS_ANGELES, City.NEW_YORK, Value.TWENTY_ONE));

        deck.add(new DestinationCard(City.SEATTLE, City.NEW_YORK, Value.TWENTY_TWO));
        return deck;
    }

    public static Deque<DestinationCard> getShuffledDeck() {
        List<DestinationCard> list = getDeckList();
        Collections.shuffle(list);
        return new ArrayDeque<>(list);
    }

    public static List<DestinationCard> unGsonCards(List<LinkedTreeMap> gsonCards){
        List<DestinationCard> cards = new ArrayList<> ();
        for (LinkedTreeMap gsonCard : gsonCards) {
            City destination1 = City.valueOf((String) gsonCard.get("destination1"));
            City destination2 = City.valueOf((String) gsonCard.get("destination2"));
            Value pointValue = Value.valueOf((String) gsonCard.get("pointValue"));

            cards.add(new DestinationCard(destination1, destination2, pointValue));
        }
        return cards;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DestinationCard)) return false;
        DestinationCard that = (DestinationCard) o;
        return getDestination1() == that.getDestination1() &&
                getDestination2() == that.getDestination2() &&
                getPointValue() == that.getPointValue();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDestination1(), getDestination2(), getPointValue());
    }

    @Override
    public String toString() {
        return "DestinationCard{" +
                "destination1=" + destination1.name() +
                ", destination2=" + destination2.name() +
                ", pointValue=" + pointValue +
                '}';
    }
}
