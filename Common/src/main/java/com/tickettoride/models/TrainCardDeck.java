package com.tickettoride.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TrainCardDeck {

    // Index 0 is top of deck, last index is bottom of deck
    private List<TrainCard> faceDownDeck;
    private List<TrainCard> faceUpDeck;
    private List<TrainCard> discardPile;

    public TrainCardDeck(){
        initializeDecks();
    }

    private void initializeDecks(){
        faceDownDeck = new ArrayList<>();
        faceUpDeck = new ArrayList<>();
        discardPile = new ArrayList<>();

        for (int i = 0; i < 12; i++){
            faceDownDeck.add(new TrainCard(Color.BLACK));
            faceDownDeck.add(new TrainCard(Color.RED));
            faceDownDeck.add(new TrainCard(Color.GREEN));
            faceDownDeck.add(new TrainCard(Color.PURPLE));
            faceDownDeck.add(new TrainCard(Color.WHITE));
            faceDownDeck.add(new TrainCard(Color.BLUE));
            faceDownDeck.add(new TrainCard(Color.YELLOW));
            faceDownDeck.add(new TrainCard(Color.ORANGE));
        }
        for (int i = 0; i < 14; i++) {
            faceDownDeck.add(new TrainCard(Color.WILD));
        }

        Collections.shuffle(faceDownDeck);

        for (int i = 0; i < 5; i++){
            faceUpDeck.add(faceDownDeck.get(0));
            faceDownDeck.remove(0);
        }
    }

    public List<TrainCard> getFaceDownDeck(){return faceDownDeck;}

    public List<TrainCard> getFaceUpDeck(){return faceUpDeck;}

    public List<TrainCard> getDiscardPile() {return discardPile;}

    public TrainCard drawFromFaceUp(int i){
        TrainCard card = faceUpDeck.get(i);
        faceUpDeck.set(i, faceDownDeck.get(0));
        faceDownDeck.remove(0);
        checkForEmptyDeck();
        return card;
    }

    public TrainCard drawFromFaceDown(){
        TrainCard card = faceDownDeck.get(0);
        faceDownDeck.remove(0);
        checkForEmptyDeck();
        return card;
    }

    public void checkForEmptyDeck(){
        if (faceDownDeck.size() == 0){
            Collections.shuffle(discardPile);
            faceDownDeck.addAll(discardPile);
            discardPile.clear();
        }
    }

    // Checks if the card that the player wants to draw is a wild card
    public Boolean isFaceupWild(int i){
        return faceUpDeck.get(i).getColor() == Color.WILD;
    }

    public Color getFaceupColor(int i){
        return faceUpDeck.get(i).getColor();
    }

    // TODO: Check if three or more locomotives in faceup pile and discards
}
