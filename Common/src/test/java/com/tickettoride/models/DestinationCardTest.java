package com.tickettoride.models;

import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNot;
import org.junit.Test;

import java.util.*;

import static com.tickettoride.models.DestinationCard.*;
import static org.junit.Assert.*;

public class DestinationCardTest {

    @Test
    public void NewRandomDeckAskedFor_IsCorrectLength() {

        Queue<DestinationCard> shuffledDeck = getShuffledDeck();
        int expectedLength = 30;

        assertEquals(expectedLength, shuffledDeck.size());
    }

    @Test
    public void NewRandomDeckAskedFor_DeckContainsAmountOfEachPointValue() {

        int numFOURexpected = 1;
        int numFIVEexpected = 1;
        int numSIXexpected = 1;
        int numSEVENexpected = 2;
        int numEIGHTexpected = 3;
        int numNINEexpected = 4;
        int numTENexpected = 2;
        int numELEVENexpected = 4;
        int numTWELVEexpected = 2;
        int numTHIRTEENexpected = 3;
        int numSIXTEENexpected = 1;
        int numSEVENTEENexpected = 2;
        int numTWENTYexpected = 2;
        int numTWENTY_ONEexpected = 1;
        int numTWENTY_TWOexpected = 1;

        int numFOURactual = 0;
        int numFIVEactual = 0;
        int numSIXactual = 0;
        int numSEVENactual = 0;
        int numEIGHTactual = 0;
        int numNINEactual = 0;
        int numTENactual = 0;
        int numELEVENactual = 0;
        int numTWELVEactual = 0;
        int numTHIRTEENactual = 0;
        int numSIXTEENactual = 0;
        int numSEVENTEENactual = 0;
        int numTWENTYactual = 0;
        int numTWENTY_ONEactual = 0;
        int numTWENTY_TWOactual = 0;

        for (DestinationCard card : getShuffledDeck()) {
            switch (card.getPointValue()){
                case FOUR: numFOURactual++;break;
                case FIVE: numFIVEactual++;break;
                case SIX: numSIXactual++;break;
                case SEVEN: numSEVENactual++;break;
                case EIGHT: numEIGHTactual++;break;
                case NINE: numNINEactual++;break;
                case TEN: numTENactual++;break;
                case ELEVEN: numELEVENactual++;break;
                case TWELVE: numTWELVEactual++;break;
                case THIRTEEN: numTHIRTEENactual++;break;
                case SIXTEEN: numSIXTEENactual++;break;
                case SEVENTEEN: numSEVENTEENactual++;break;
                case TWENTY: numTWENTYactual++;break;
                case TWENTY_ONE: numTWENTY_ONEactual++;break;
                case TWENTY_TWO: numTWENTY_TWOactual++;break;
            }
        }

        assertEquals(numFOURexpected, numFOURactual);
        assertEquals(numFIVEexpected, numFIVEactual);
        assertEquals(numSIXexpected, numSIXactual);
        assertEquals(numSEVENexpected, numSEVENactual);
        assertEquals(numEIGHTexpected, numEIGHTactual);
        assertEquals(numNINEexpected, numNINEactual);
        assertEquals(numTENexpected, numTENactual);
        assertEquals(numELEVENexpected, numELEVENactual);
        assertEquals(numTWELVEexpected, numTWELVEactual);
        assertEquals(numTHIRTEENexpected, numTHIRTEENactual);
        assertEquals(numSIXTEENexpected, numSIXTEENactual);
        assertEquals(numSEVENTEENexpected, numSEVENTEENactual);
        assertEquals(numTWENTYexpected, numTWENTYactual);
        assertEquals(numTWENTY_ONEexpected, numTWENTY_ONEactual);
        assertEquals(numTWENTY_TWOexpected, numTWENTY_TWOactual);
    }

    @Test
    public void NewRandomDeckAskedFor_AllCardsUnique() {

        Queue<DestinationCard> shuffledDeck = getShuffledDeck();
        Set<DestinationCard> uniqueCards = new TreeSet<>(shuffledDeck);

        assertEquals(shuffledDeck.size(), uniqueCards.size());
    }

    @Test
    public void TwoNewRandomDeckAskedFor_EachIsInAUniqueOrder() {

        Queue<DestinationCard> deckOne = getShuffledDeck();
        Queue<DestinationCard> deckTwo = getShuffledDeck();

        assertThat(deckOne, IsNot.not(IsEqual.equalTo(deckTwo)));

    }

}