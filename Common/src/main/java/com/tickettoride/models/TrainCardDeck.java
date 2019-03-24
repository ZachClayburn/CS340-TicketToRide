package com.tickettoride.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TrainCardDeck {

    // TODO: Change so 0 is bottom of deck
    // Index 0 is top of deck, last index is bottom of deck
    private List<TrainCard> faceDownDeck;
    private List<TrainCard> faceUpDeck;
    private List<TrainCard> discardPile;

    public TrainCardDeck(){
        initializeDecks();
    }

    public TrainCardDeck(List<TrainCard> faceup, List<TrainCard> facedown, List<TrainCard> discard){
        faceUpDeck = faceup;
        faceDownDeck = facedown;
        discardPile = discard;
    }

    public TrainCardDeck(List<TrainCard> faceup, List<TrainCard> facedown){
        faceUpDeck = faceup;
        faceDownDeck = facedown;
        discardPile = new ArrayList<>();
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

        checkForWild();
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

    public Boolean checkForWild(){
        int wildCount = 0;
        for (TrainCard card: faceUpDeck){
            if (card.getColor() == Color.WILD){
                wildCount += 1;
            }
        }

        if (wildCount >= 3){
            discardPile.addAll(faceUpDeck);
            faceUpDeck.clear();
            for (int i = 0; i < 5; i++){
                faceUpDeck.add(faceDownDeck.get(0));
                faceDownDeck.remove(0);
                checkForEmptyDeck();
            }
            return true;
        }

        return false;
    }

    public Hand getInitialHand(){
        Hand hand = new Hand();

        for (int i = 0; i < 4; i++){
            TrainCard card = drawFromFaceDown();
            if (card.getColor() == Color.RED){
                hand.setRed(1);
            }
            if (card.getColor() == Color.GREEN){
                hand.setGreen(1);
            }
            if (card.getColor() == Color.BLUE){
                hand.setBlue(1);
            }
            if (card.getColor() == Color.YELLOW){
                hand.setYellow(1);
            }
            if (card.getColor() == Color.PURPLE){
                hand.setPurple(1);
            }
            if (card.getColor() == Color.ORANGE){
                hand.setOrange(1);
            }
            if (card.getColor() == Color.BLACK){
                hand.setBlack(1);
            }
            if (card.getColor() == Color.WHITE){
                hand.setWhite(1);
            }
            if (card.getColor() == Color.WILD){
                hand.setLocomotive(1);
            }
        }
        return hand;
    }
}
