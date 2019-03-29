package com.tickettoride.clientModels.helpers;

import com.tickettoride.clientModels.ClientRoute;
import com.tickettoride.clientModels.DataManager;
import com.tickettoride.models.Color;
import com.tickettoride.models.Hand;
import static com.tickettoride.models.Color.WILD;

public class DiscardHelper {
    private Hand currentHand = DataManager.getSINGLETON().getPlayerHand();
    private ClientRoute currentRoute = DataManager.getSINGLETON().getCurrentClientRoute();
    private Color currentColor = null;
    private int discardedColor = 0;
    private int discardedWild = 0;

    public boolean discardCard (Color color) {
        switch (currentRoute.getColor()) {
            case GREY:
                if (currentColor == null) {
                    setColor(color);
                    if (!checkHand(color)) { return false; }
                    break;
                }
                else if (currentColor == color) { if (!checkHand(color)) { return false; } break; }
                else if (color == WILD) { if (!checkHand(color)) { return false; } }
                else { return false; }
            default:
                currentColor = currentRoute.getColor();
                if (color == currentRoute.getColor()) { if (!checkHand(color)) { return false; } }
                else if (color == WILD) { if (!checkHand(color)) { return false; } }
                else { return false; }
        }
        return true;
    }
    public void setColor(Color color) { if (color != WILD) { currentColor = color;} }

    private boolean checkHand(Color color) {
        switch(color) {
            case BLUE:
                if (currentHand.getBlue() > 0) {
                    currentHand.discardBlue(1);
                    ++discardedColor;
                    return true;
                }
                break;
            case GREEN:
                if (currentHand.getGreen() > 0) {
                    currentHand.discardGreen(1);
                    ++discardedColor;
                    return true;
                }
                break;
            case RED:
                if (currentHand.getRed() > 0) {
                    currentHand.discardRed(1);
                    ++discardedColor;
                    return true;
                }
                break;
            case ORANGE:
                if (currentHand.getOrange() > 0) {
                    currentHand.discardOrange(1);
                    ++discardedColor;
                    return true;
                }
                break;
            case YELLOW:
                if (currentHand.getYellow() > 0) {
                    currentHand.discardYellow(1);
                    ++discardedColor;
                    return true;
                }
                break;
            case BLACK:
                if (currentHand.getBlack() > 0) {
                    currentHand.discardBlack(1);
                    ++discardedColor;
                    return true;
                }
                break;
            case WHITE:
                if (currentHand.getWhite() > 0) {
                    currentHand.discardWhite(1);
                    ++discardedColor;
                    return true;
                }
                break;
            case PURPLE:
                if (currentHand.getPurple() > 0) {
                    currentHand.discardPurple(1);
                    ++discardedColor;
                    return true;
                };
                break;
            case WILD:
                if (currentHand.getLocomotive() > 0) {
                    currentHand.discardLocomotive(1);
                    ++discardedWild;
                    return true;
                }
                break;
        }
        return false;
    }
    public void cancel() {
        if (currentColor == null) {
            currentHand.setLocomotive(discardedWild);
        }
        else {
            switch (currentColor) {
                case BLUE:
                    currentHand.setBlue(discardedColor);
                    currentHand.setLocomotive(discardedWild);
                    break;
                case GREEN:
                    currentHand.setGreen(discardedColor);
                    currentHand.setLocomotive(discardedWild);
                    break;
                case PURPLE:
                    currentHand.setPurple(discardedColor);
                    currentHand.setLocomotive(discardedWild);
                    break;
                case RED:
                    currentHand.setRed(discardedColor);
                    currentHand.setLocomotive(discardedWild);
                    break;
                case ORANGE:
                    currentHand.setOrange(discardedColor);
                    currentHand.setLocomotive(discardedWild);
                    break;
                case YELLOW:
                    currentHand.setYellow(discardedColor);
                    currentHand.setLocomotive(discardedWild);
                    break;
                case BLACK:
                    currentHand.setBlack(discardedColor);
                    currentHand.setLocomotive(discardedWild);
                    break;
                case WHITE:
                    currentHand.setWhite(discardedColor);
                    currentHand.setLocomotive(discardedWild);
                    break;
                default:
                    currentHand.setLocomotive(discardedWild);
                    break;
            }
        }
        discardedColor = 0;
        discardedWild = 0;
        currentColor = null;
    }
    public boolean wildCheck() {
        if (discardedWild == currentRoute.getSpaces()) { return false; }
        return true;
    }
    public boolean finalDiscard() {
        if (discardedColor + discardedWild == currentRoute.getSpaces()) { return true; }
        return false;
    }

    public Color getCurrentColor() {
        return currentColor;
    }

    public int getDiscardedColor() {
        return discardedColor;
    }

    public int getDiscardedWild() {
        return discardedWild;
    }
}
