package com.tickettoride.models;


import java.util.*;

public class DestinationCard implements Comparable<DestinationCard> {

    private final Destination destination1;
    private final Destination destination2;

    public Destination getDestination1() {
        return destination1;
    }

    public Destination getDestination2() {
        return destination2;
    }

    public Value getPointValue() {
        return pointValue;
    }

    private final Value pointValue;

    private DestinationCard(Destination destination1, Destination destination2, Value pointValue) {

        this.destination1 = destination1;
        this.destination2 = destination2;
        this.pointValue = pointValue;
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

    enum Value {
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
    }


    public static Deque<DestinationCard> getShuffledDeck() {

        final int deckSize = 30;
        ArrayList<DestinationCard> deck = new ArrayList<>(deckSize);

        deck.add(new DestinationCard(Destination.DENVER, Destination.EL_PASO, Value.FOUR));

        deck.add(new DestinationCard(Destination.KANSAS_CITY, Destination.HOUSTON, Value.FIVE));

        deck.add(new DestinationCard(Destination.NEW_YORK, Destination.ATLANTA, Value.SIX));

        deck.add(new DestinationCard(Destination.CHICAGO, Destination.NEW_ORLEANS, Value.SEVEN));
        deck.add(new DestinationCard(Destination.CALGARY, Destination.SALT_LAKE_CITY, Value.SEVEN));

        deck.add(new DestinationCard(Destination.HELENA, Destination.LOS_ANGELES, Value.EIGHT));
        deck.add(new DestinationCard(Destination.DULUTH, Destination.HOUSTON, Value.EIGHT));
        deck.add(new DestinationCard(Destination.SAUL_ST_MARIE, Destination.NASHVILLE, Value.EIGHT));

        deck.add(new DestinationCard(Destination.MONTREAL, Destination.ATLANTA, Value.NINE));
        deck.add(new DestinationCard(Destination.SAUL_ST_MARIE, Destination.OKLAHOMA_CITY, Value.NINE));
        deck.add(new DestinationCard(Destination.SEATTLE, Destination.LOS_ANGELES, Value.NINE));
        deck.add(new DestinationCard(Destination.CHICAGO, Destination.SANTA_FE, Value.NINE));

        deck.add(new DestinationCard(Destination.DULUTH, Destination.EL_PASO, Value.TEN));
        deck.add(new DestinationCard(Destination.TORONTO, Destination.MIAMI, Value.TEN));

        deck.add(new DestinationCard(Destination.PORTLAND, Destination.PHOENIX, Value.ELEVEN));
        deck.add(new DestinationCard(Destination.DALLAS, Destination.NEW_YORK, Value.ELEVEN));
        deck.add(new DestinationCard(Destination.DENVER, Destination.PITTSBURGH, Value.ELEVEN));
        deck.add(new DestinationCard(Destination.WINNIPEG, Destination.LITTLE_ROCK, Value.ELEVEN));

        deck.add(new DestinationCard(Destination.WINNIPEG, Destination.HOUSTON, Value.TWELVE));
        deck.add(new DestinationCard(Destination.BOSTON, Destination.MIAMI, Value.TWELVE));

        deck.add(new DestinationCard(Destination.VANCOUVER, Destination.SANTA_FE, Value.THIRTEEN));
        deck.add(new DestinationCard(Destination.CALGARY, Destination.PHOENIX, Value.THIRTEEN));
        deck.add(new DestinationCard(Destination.MONTREAL, Destination.NEW_ORLEANS, Value.THIRTEEN));

        deck.add(new DestinationCard(Destination.LOS_ANGELES, Destination.CHICAGO, Value.SIXTEEN));

        deck.add(new DestinationCard(Destination.SAN_FRANCISCO, Destination.ATLANTA, Value.SEVENTEEN));
        deck.add(new DestinationCard(Destination.PORTLAND, Destination.NASHVILLE, Value.SEVENTEEN));

        deck.add(new DestinationCard(Destination.VANCOUVER, Destination.MONTREAL, Value.TWENTY));
        deck.add(new DestinationCard(Destination.LOS_ANGELES, Destination.MIAMI, Value.TWENTY));

        deck.add(new DestinationCard(Destination.LOS_ANGELES, Destination.NEW_YORK, Value.TWENTY_ONE));

        deck.add(new DestinationCard(Destination.SEATTLE, Destination.NEW_YORK, Value.TWENTY_TWO));

        Collections.shuffle(deck);

        return new ArrayDeque<>(deck);
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
                "destination1=" + destination1 +
                ", destination2=" + destination2 +
                ", pointValue=" + pointValue +
                '}';
    }
}
