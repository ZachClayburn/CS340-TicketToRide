package com.tickettoride.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tickettoride.R;
import com.tickettoride.clientModels.DataManager;
import com.tickettoride.clientModels.Route;
import com.tickettoride.models.Color;
import com.tickettoride.models.Hand;

import java.util.ArrayList;

public class DiscardFragment extends Fragment {
    private ImageView blueCards;
    private ImageView greenCards;
    private ImageView purpleCards;
    private ImageView redCards;
    private ImageView yellowCards;
    private ImageView orangeCards;
    private ImageView blackCards;
    private ImageView whiteCards;
    private ImageView wildCards;
    private TextView numberBlue;
    private TextView numberGreen;
    private TextView numberPurple;
    private TextView numberRed;
    private TextView numberYellow;
    private TextView numberOrange;
    private TextView numberBlack;
    private TextView numberWhite;
    private TextView numberWild;
    private Button cancel;
    private Button discard;
    private Hand currentHand;
    private Route currentRoute;
    private Color currentColor;
    private int discardedColor = 0;
    private int discardedWild = 0;
    private int discardedCards = 0;
    private TextView discarded;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.discard_fragment, container, false);
        currentHand = DataManager.getSINGLETON().getPlayerHand();
        currentRoute = DataManager.getSINGLETON().getCurrentRoute();
        blueCards = v.findViewById(R.id.blue_card);
        greenCards = v.findViewById(R.id.green_card);
        purpleCards = v.findViewById(R.id.purple_card);
        redCards = v.findViewById(R.id.red_card);
        orangeCards = v.findViewById(R.id.orange_card);
        yellowCards = v.findViewById(R.id.yellow_card);
        blackCards = v.findViewById(R.id.black_card);
        whiteCards = v.findViewById(R.id.white_card);
        wildCards = v.findViewById(R.id.wild_card);
        numberBlue = v.findViewById(R.id.blue);
        numberGreen = v.findViewById(R.id.green);
        numberPurple = v.findViewById(R.id.purple);
        numberRed = v.findViewById(R.id.red);
        numberOrange = v.findViewById(R.id.orange);
        numberYellow = v.findViewById(R.id.yellow);
        numberBlack = v.findViewById(R.id.black);
        numberWhite = v.findViewById(R.id.white);
        numberWild = v.findViewById(R.id.wild);
        discarded = v.findViewById(R.id.discard);
        cancel = v.findViewById(R.id.cancel_button);
        discard = v.findViewById(R.id.discard_button);
        numberBlue.setText("Blue: " + currentHand.getBlue());
        numberGreen.setText("Green: " + currentHand.getGreen());
        numberPurple.setText("Purple: " + currentHand.getPurple());
        numberRed.setText("Red: " + currentHand.getRed());
        numberOrange.setText("Orange: " + currentHand.getOrange());
        numberYellow.setText("Yellow: " + currentHand.getYellow());
        numberBlack.setText("Black: " + currentHand.getBlack());
        numberWhite.setText("White: " + currentHand.getWhite());
        numberWild.setText("Wild: " + currentHand.getLocomotive());

        blueCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (currentRoute.getColor()) {
                    case GREY:
                        if (currentColor == null && (currentHand.getBlue() + currentHand.getLocomotive()) > currentRoute.getSpaces() && (currentHand.getBlue() > 0)) {
                            currentColor = Color.BLUE;
                            ++discardedColor;
                            ++discardedCards;
                            currentHand.setBlue(-1);
                            numberBlue.setText("Blue: " + currentHand.getBlue());
                            discarded.setText("Discarded: " + discardedCards);
                            break;
                        }
                        else if (currentColor == Color.BLUE && currentHand.getBlue() > 0) {
                            ++discardedCards;
                            ++discardedColor;
                            currentHand.setBlue(-1);
                            numberBlue.setText("Blue: " + currentHand.getBlue());
                            discarded.setText("Discarded: " + discardedCards);
                            break;
                        }
                        else {
                            Toast.makeText(getContext(), "Can't discard this color", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    default:
                        if (discardedCards != currentRoute.getSpaces() && (currentRoute.getColor() == Color.BLUE) && currentHand.getBlue() > 0) {
                            ++discardedCards;
                            ++discardedColor;
                            currentHand.setBlue(-1);
                            numberBlue.setText("Blue: " + currentHand.getBlue());
                            discarded.setText("Discarded: " + discardedCards);
                            break;
                        }
                        else {
                            Toast.makeText(getContext(), "You don't have anymore blue cards", Toast.LENGTH_SHORT).show();
                            break;
                        }
                }
            }
        });
        greenCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (currentRoute.getColor()) {
                    case GREY:
                        if (discardedCards != currentRoute.getSpaces() && currentColor == null && (currentHand.getGreen() + currentHand.getLocomotive()) > currentRoute.getSpaces() && (currentHand.getGreen() > 0)) {
                            currentColor = Color.GREEN;
                            ++discardedColor;
                            ++discardedCards;
                            currentHand.setGreen(-1);
                            numberGreen.setText("Green: " + currentHand.getGreen());
                            discarded.setText("Discarded: " + discardedCards);
                            break;
                        }
                        else if (discardedCards != currentRoute.getSpaces() && currentColor == Color.GREEN && currentHand.getGreen() > 0) {
                            ++discardedCards;
                            ++discardedColor;
                            currentHand.setGreen(-1);
                            numberGreen.setText("Green: " + currentHand.getGreen());
                            discarded.setText("Discarded: " + discardedCards);
                            break;
                        }
                        else {
                            Toast.makeText(getContext(), "Can't discard this color", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    default:
                        if (discardedCards != currentRoute.getSpaces() && (currentRoute.getColor() == Color.GREEN) && currentHand.getGreen() > 0) {
                            ++discardedCards;
                            ++discardedColor;
                            currentHand.setGreen(-1);
                            numberGreen.setText("Green: " + currentHand.getGreen());
                            discarded.setText("Discarded: " + discardedCards);
                            break;
                        }
                        else {
                            Toast.makeText(getContext(), "You don't have anymore green cards", Toast.LENGTH_SHORT).show();
                            break;
                        }
                }
            }
        });
        purpleCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (currentRoute.getColor()) {
                    case GREY:
                        if (discardedCards != currentRoute.getSpaces() && currentColor == null && (currentHand.getPurple() + currentHand.getLocomotive()) > currentRoute.getSpaces() && (currentHand.getPurple() > 0)) {
                            currentColor = Color.PURPLE;
                            ++discardedColor;
                            ++discardedCards;
                            currentHand.setPurple(-1);
                            numberPurple.setText("Purple: " + currentHand.getPurple());
                            discarded.setText("Discarded: " + discardedCards);
                            break;
                        }
                        else if (discardedCards != currentRoute.getSpaces() && currentColor == Color.PURPLE && currentHand.getPurple() > 0) {
                            ++discardedCards;
                            ++discardedColor;
                            currentHand.setPurple(-1);
                            numberPurple.setText("Purple: " + currentHand.getPurple());
                            discarded.setText("Discarded: " + discardedCards);
                            break;
                        }
                        else {
                            Toast.makeText(getContext(), "Can't discard this color", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    default:
                        if (discardedCards != currentRoute.getSpaces() && (currentRoute.getColor() == Color.PURPLE) && currentHand.getPurple() > 0) {
                            ++discardedCards;
                            ++discardedColor;
                            currentHand.setPurple(-1);
                            numberPurple.setText("Purple: " + currentHand.getPurple());
                            discarded.setText("Discarded: " + discardedCards);
                            break;
                        }
                        else {
                            Toast.makeText(getContext(), "You don't have anymore purple cards", Toast.LENGTH_SHORT).show();
                            break;
                        }
                }
            }
        });
        redCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (currentRoute.getColor()) {
                    case GREY:
                        if (discardedCards != currentRoute.getSpaces() && currentColor == null && (currentHand.getRed() + currentHand.getLocomotive()) > currentRoute.getSpaces() && (currentHand.getRed() > 0)) {
                            currentColor = Color.RED;
                            ++discardedColor;
                            ++discardedCards;
                            currentHand.setRed(-1);
                            numberRed.setText("Red: " + currentHand.getRed());
                            discarded.setText("Discarded: " + discardedCards);
                            break;
                        }
                        else if (discardedCards != currentRoute.getSpaces() && currentColor == Color.RED && currentHand.getRed() > 0) {
                            ++discardedCards;
                            ++discardedColor;
                            currentHand.setRed(-1);
                            numberRed.setText("Red: " + currentHand.getRed());
                            discarded.setText("Discarded: " + discardedCards);
                            break;
                        }
                        else {
                            Toast.makeText(getContext(), "Can't discard this color", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    default:
                        if (discardedCards != currentRoute.getSpaces() && (currentRoute.getColor() == Color.RED) && currentHand.getRed() > 0) {
                            ++discardedCards;
                            ++discardedColor;
                            currentHand.setRed(-1);
                            numberRed.setText("Red: " + currentHand.getRed());
                            discarded.setText("Discarded: " + discardedCards);
                            break;
                        }
                        else {
                            Toast.makeText(getContext(), "You don't have anymore red cards", Toast.LENGTH_SHORT).show();
                            break;
                        }
                }
            }
        });
        orangeCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (currentRoute.getColor()) {
                    case GREY:
                        if (discardedCards != currentRoute.getSpaces() && currentColor == null && (currentHand.getOrange() + currentHand.getLocomotive()) > currentRoute.getSpaces() && (currentHand.getOrange() > 0)) {
                            currentColor = Color.ORANGE;
                            ++discardedColor;
                            ++discardedCards;
                            currentHand.setOrange(-1);
                            numberOrange.setText("Blue: " + currentHand.getOrange());
                            discarded.setText("Discarded: " + discardedCards);
                            break;
                        }
                        else if (discardedCards != currentRoute.getSpaces() && currentColor == Color.ORANGE && currentHand.getOrange() > 0) {
                            ++discardedCards;
                            ++discardedColor;
                            currentHand.setOrange(-1);
                            numberOrange.setText("Orange: " + currentHand.getOrange());
                            discarded.setText("Discarded: " + discardedCards);
                            break;
                        }
                        else {
                            Toast.makeText(getContext(), "Can't discard this color", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    default:
                        if (discardedCards != currentRoute.getSpaces() && (currentRoute.getColor() == Color.ORANGE) && currentHand.getOrange() > 0) {
                            ++discardedCards;
                            ++discardedColor;
                            currentHand.setOrange(-1);
                            numberOrange.setText("Orange: " + currentHand.getOrange());
                            discarded.setText("Discarded: " + discardedCards);
                            break;
                        }
                        else {
                            Toast.makeText(getContext(), "You don't have anymore orange cards", Toast.LENGTH_SHORT).show();
                            break;
                        }
                }
            }
        });
        yellowCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (currentRoute.getColor()) {
                    case GREY:
                        if (currentColor == null && (currentHand.getYellow() + currentHand.getLocomotive()) > currentRoute.getSpaces() && (currentHand.getYellow() > 0)) {
                            currentColor = Color.YELLOW;
                            ++discardedColor;
                            ++discardedCards;
                            currentHand.setYellow(-1);
                            numberYellow.setText("Yellow: " + currentHand.getYellow());
                            discarded.setText("Discarded: " + discardedCards);
                            break;
                        }
                        else if (discardedCards != currentRoute.getSpaces() && currentColor == Color.YELLOW && currentHand.getYellow() > 0) {
                            ++discardedCards;
                            ++discardedColor;
                            currentHand.setYellow(-1);
                            numberYellow.setText("Yellow: " + currentHand.getYellow());
                            discarded.setText("Discarded: " + discardedCards);
                            break;
                        }
                        else {
                            Toast.makeText(getContext(), "Can't discard this color", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    default:
                        if (discardedCards != currentRoute.getSpaces() && (currentRoute.getColor() == Color.YELLOW) && currentHand.getYellow() > 0) {
                            ++discardedCards;
                            ++discardedColor;
                            currentHand.setYellow(-1);
                            numberYellow.setText("Blue: " + currentHand.getYellow());
                            discarded.setText("Discarded: " + discardedCards);
                            break;
                        }
                        else {
                            Toast.makeText(getContext(), "You don't have anymore yellow cards", Toast.LENGTH_SHORT).show();
                            break;
                        }
                }
            }
        });
        blackCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (currentRoute.getColor()) {
                    case GREY:
                        if (discardedCards != currentRoute.getSpaces() && currentColor == null && (currentHand.getBlack() + currentHand.getLocomotive()) > currentRoute.getSpaces() && (currentHand.getBlack() > 0)) {
                            currentColor = Color.BLACK;
                            ++discardedColor;
                            ++discardedCards;
                            currentHand.setBlack(-1);
                            numberBlack.setText("Black: " + currentHand.getBlack());
                            discarded.setText("Discarded: " + discardedCards);
                            break;
                        }
                        else if (discardedCards != currentRoute.getSpaces() && currentColor == Color.BLACK && currentHand.getBlack() > 0) {
                            ++discardedCards;
                            ++discardedColor;
                            currentHand.setBlack(-1);
                            numberBlack.setText("Black: " + currentHand.getBlack());
                            discarded.setText("Discarded: " + discardedCards);
                            break;
                        }
                        else {
                            Toast.makeText(getContext(), "Can't discard this color", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    default:
                        if (discardedCards != currentRoute.getSpaces() && (currentRoute.getColor() == Color.BLACK) && currentHand.getBlack() > 0) {
                            ++discardedCards;
                            ++discardedColor;
                            currentHand.setBlack(-1);
                            numberBlack.setText("Black: " + currentHand.getBlack());
                            discarded.setText("Discarded: " + discardedCards);
                            break;
                        }
                        else {
                            Toast.makeText(getContext(), "You don't have anymore black cards", Toast.LENGTH_SHORT).show();
                            break;
                        }
                }
            }
        });
        whiteCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (currentRoute.getColor()) {
                    case GREY:
                        if (discardedCards != currentRoute.getSpaces() && currentColor == null && (currentHand.getWhite() + currentHand.getLocomotive()) > currentRoute.getSpaces() && (currentHand.getWhite() > 0)) {
                            currentColor = Color.WHITE;
                            ++discardedColor;
                            ++discardedCards;
                            currentHand.setWhite(-1);
                            numberWhite.setText("White: " + currentHand.getWhite());
                            discarded.setText("Discarded: " + discardedCards);
                            break;
                        }
                        else if (discardedCards != currentRoute.getSpaces() && currentColor == Color.WHITE && currentHand.getWhite() > 0) {
                            ++discardedCards;
                            ++discardedColor;
                            currentHand.setWhite(-1);
                            numberWhite.setText("White: " + currentHand.getWhite());
                            discarded.setText("Discarded: " + discardedCards);
                            break;
                        }
                        else {
                            Toast.makeText(getContext(), "Can't discard this color", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    default:
                        if (discardedCards != currentRoute.getSpaces() && (currentRoute.getColor() == Color.WHITE) && currentHand.getWhite() > 0) {
                            ++discardedCards;
                            ++discardedColor;
                            currentHand.setWhite(-1);
                            numberWhite.setText("Blue: " + currentHand.getWhite());
                            discarded.setText("Discarded: " + discardedCards);
                            break;
                        }
                        else {
                            Toast.makeText(getContext(), "You don't have anymore white cards", Toast.LENGTH_SHORT).show();
                            break;
                        }
                }
            }
        });
        wildCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (discardedCards != currentRoute.getSpaces() && currentHand.getLocomotive() > 0) {
                    ++discardedCards;
                    ++discardedWild;
                    currentHand.setLocomotive(-1);
                    numberWild.setText("Locomotive: " + currentHand.getLocomotive());
                    discarded.setText("Discarded: " + discardedCards);
                }
                else {
                    Toast.makeText(getContext(), "You can't discard anymore wild cards", Toast.LENGTH_SHORT).show();
                }

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                discardedCards = 0;
                if (currentColor == null) {
                    currentHand.setLocomotive(discardedWild);
                    discardedColor = 0;
                    discardedWild = 0;
                    numberWild.setText("Locomotive: " + currentHand.getLocomotive());
                    discarded.setText("Discarded: " + discardedCards);
                    return;
                }
                switch(currentColor) {
                    case BLUE:
                        currentColor = null;
                        currentHand.setBlue(discardedColor);
                        currentHand.setLocomotive(discardedWild);
                        discardedColor = 0;
                        discardedWild = 0;
                        numberBlue.setText("Blue: " + currentHand.getBlue());
                        numberWild.setText("Locomotive: " + currentHand.getLocomotive());
                        discarded.setText("Discarded: " + discardedCards);
                        break;
                    case GREEN:
                        currentColor = null;
                        currentHand.setGreen(discardedColor);
                        currentHand.setLocomotive(discardedWild);
                        discardedColor = 0;
                        discardedWild = 0;
                        numberGreen.setText("Green: " + currentHand.getGreen());
                        numberWild.setText("Locomotive: " + currentHand.getLocomotive());
                        discarded.setText("Discarded: " + discardedCards);
                        break;
                    case PURPLE:
                        currentColor = null;
                        currentHand.setPurple(discardedColor);
                        currentHand.setLocomotive(discardedWild);
                        discardedColor = 0;
                        discardedWild = 0;
                        numberPurple.setText("Purple: " + currentHand.getPurple());
                        numberWild.setText("Locomotive: " + currentHand.getLocomotive());
                        discarded.setText("Discarded: " + discardedCards);
                        break;
                    case RED:
                        currentColor = null;
                        currentHand.setRed(discardedColor);
                        currentHand.setLocomotive(discardedWild);
                        numberRed.setText("Red: " + currentHand.getRed());
                        numberWild.setText("Locomotive: " + currentHand.getLocomotive());
                        discarded.setText("Discarded: " + discardedCards);
                        break;
                    case ORANGE:
                        currentColor = null;
                        currentHand.setOrange(discardedColor);
                        currentHand.setLocomotive(discardedWild);
                        discardedColor = 0;
                        discardedWild = 0;
                        numberOrange.setText("Orange: " + currentHand.getOrange());
                        numberWild.setText("Locomotive: " + currentHand.getLocomotive());
                        discarded.setText("Discarded: " + discardedCards);
                        break;
                    case YELLOW:
                        currentColor = null;
                        currentHand.setYellow(discardedColor);
                        currentHand.setLocomotive(discardedWild);
                        discardedColor = 0;
                        discardedWild = 0;
                        numberYellow.setText("Yellow: " + currentHand.getYellow());
                        numberWild.setText("Locomotive: " + currentHand.getLocomotive());
                        discarded.setText("Discarded: " + discardedCards);
                        break;
                    case BLACK:
                        currentColor = null;
                        currentHand.setBlack(discardedColor);
                        currentHand.setLocomotive(discardedWild);
                        discardedColor = 0;
                        discardedWild = 0;
                        numberBlack.setText("Black: " + currentHand.getBlack());
                        numberWild.setText("Locomotive: " + currentHand.getLocomotive());
                        discarded.setText("Discarded: " + discardedCards);
                        break;
                    case WHITE:
                        currentColor = null;
                        currentHand.setWhite(discardedColor);
                        currentHand.setLocomotive(discardedWild);
                        discardedColor = 0;
                        discardedWild = 0;
                        numberWhite.setText("White: " + currentHand.getWhite());
                        numberWild.setText("Locomotive: " + currentHand.getLocomotive());
                        discarded.setText("Discarded: " + discardedCards);
                        break;
                }
            }
        });
        discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (discardedWild != currentRoute.getSpaces() && discardedCards == currentRoute.getSpaces()) {
                    //go paint the line
                    Toast.makeText(getContext(), "Congratulations", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getContext(), "Not yet", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return v;
    }

}


