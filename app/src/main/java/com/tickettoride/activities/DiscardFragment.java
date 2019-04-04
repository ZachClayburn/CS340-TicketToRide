package com.tickettoride.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tickettoride.R;
import com.tickettoride.clientModels.ClientRoute;
import com.tickettoride.clientModels.DataManager;
import com.tickettoride.clientModels.helpers.DiscardHelper;
import com.tickettoride.facadeProxies.RouteFacadeProxy;
import com.tickettoride.models.Color;
import com.tickettoride.models.Hand;
import static com.tickettoride.models.Color.BLACK;
import static com.tickettoride.models.Color.BLUE;
import static com.tickettoride.models.Color.GREEN;
import static com.tickettoride.models.Color.ORANGE;
import static com.tickettoride.models.Color.PURPLE;
import static com.tickettoride.models.Color.RED;
import static com.tickettoride.models.Color.WHITE;
import static com.tickettoride.models.Color.WILD;
import static com.tickettoride.models.Color.YELLOW;

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
    private TextView infoMessage;
    private TextView numberBlue;
    private TextView numberGreen;
    private TextView numberPurple;
    private TextView numberRed;
    private TextView numberYellow;
    private TextView numberOrange;
    private TextView numberBlack;
    private TextView numberWhite;
    private TextView numberWild;
    private DiscardHelper discardHelper= new DiscardHelper();
    private Button cancel;
    private Button discard;
    private Hand currentHand;
    private ClientRoute currentClientRoute;
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
        currentClientRoute = DataManager.getSINGLETON().getCurrentClientRoute();
        blueCards = v.findViewById(R.id.blue_card);
        greenCards = v.findViewById(R.id.green_card);
        purpleCards = v.findViewById(R.id.purple_card);
        redCards = v.findViewById(R.id.red_card);
        orangeCards = v.findViewById(R.id.orange_card);
        yellowCards = v.findViewById(R.id.yellow_card);
        blackCards = v.findViewById(R.id.black_card);
        whiteCards = v.findViewById(R.id.white_card);
        wildCards = v.findViewById(R.id.wild_card);
        infoMessage = v.findViewById(R.id.discard_hint);
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
        numberWild.setText("Locomotive: " + currentHand.getLocomotive());
        setInfoMessage();

        blueCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (discardHelper.discardCard(BLUE)) {
                    discardedCards++;
                    numberBlue.setText("Blue: " + currentHand.getBlue());
                    discarded.setText("Discarded: " + discardedCards);
                }
                else {
                    Toast.makeText(getContext(), "Invalid discard. Choose different color or cancel selection", Toast.LENGTH_SHORT).show();
                }
            }
        });
        greenCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (discardHelper.discardCard(GREEN)) {
                    discardedCards++;
                    numberGreen.setText("Green: " + currentHand.getGreen());
                    discarded.setText("Discarded: " + discardedCards);
                }
                else {
                    Toast.makeText(getContext(), "Invalid discard. Choose different color or cancel selection", Toast.LENGTH_SHORT).show();
                }
            }
        });
        purpleCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (discardHelper.discardCard(PURPLE)) {
                    discardedCards++;
                    numberPurple.setText("Purple: " + currentHand.getPurple());
                    discarded.setText("Discarded: " + discardedCards);
                }
                else {
                    Toast.makeText(getContext(), "Invalid discard. Choose different color or cancel selection", Toast.LENGTH_SHORT).show();
                }
            }
        });
        redCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (discardHelper.discardCard(RED)) {
                    discardedCards++;
                    numberRed.setText("Red: " + currentHand.getRed());
                    discarded.setText("Discarded: " + discardedCards);
                }
                else {
                    Toast.makeText(getContext(), "Invalid discard. Choose different color or cancel selection", Toast.LENGTH_SHORT).show();
                }
            }
        });
        orangeCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (discardHelper.discardCard(ORANGE)) {
                    discardedCards++;
                    numberOrange.setText("Orange: " + currentHand.getOrange());
                    discarded.setText("Discarded: " + discardedCards);
                }
                else {
                    Toast.makeText(getContext(), "Invalid discard. Choose different color or cancel selection", Toast.LENGTH_SHORT).show();
                }
            }
        });
        yellowCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (discardHelper.discardCard(YELLOW)) {
                    discardedCards++;
                    numberYellow.setText("Yellow: " + currentHand.getYellow());
                    discarded.setText("Discarded: " + discardedCards);
                }
                else {
                    Toast.makeText(getContext(), "Invalid discard. Choose different color or cancel selection", Toast.LENGTH_SHORT).show();
                }
            }
        });
        blackCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (discardHelper.discardCard(BLACK)) {
                    discardedCards++;
                    numberBlack.setText("Black: " + currentHand.getBlack());
                    discarded.setText("Discarded: " + discardedCards);
                }
                else {
                    Toast.makeText(getContext(), "Invalid discard. Choose different color or cancel selection", Toast.LENGTH_SHORT).show();
                }
            }
        });
        whiteCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (discardHelper.discardCard(WHITE)) {
                    discardedCards++;
                    numberWhite.setText("White: " + currentHand.getWhite());
                    discarded.setText("Discarded: " + discardedCards);
                }
                else {
                    Toast.makeText(getContext(), "Invalid discard. Choose different color or cancel selection", Toast.LENGTH_SHORT).show();
                }
            }
        });
        wildCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (discardHelper.discardCard(WILD)) {
                    discardedCards++;
                    numberWild.setText("Locomotive: " + currentHand.getLocomotive());
                    discarded.setText("Discarded: " + discardedCards);
                }
                else {
                    Toast.makeText(getContext(), "Invalid discard. Choose different color or cancel selection", Toast.LENGTH_SHORT).show();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                discardedCards = 0;
                discardHelper.cancel();
                numberBlue.setText("Blue: " + currentHand.getBlue());
                numberGreen.setText("Green: " + currentHand.getGreen());
                numberPurple.setText("Purple: " + currentHand.getPurple());
                numberRed.setText("Red: " + currentHand.getRed());
                numberOrange.setText("Orange: " + currentHand.getOrange());
                numberYellow.setText("Yellow: " + currentHand.getYellow());
                numberBlack.setText("Black: " + currentHand.getBlack());
                numberWhite.setText("White: " + currentHand.getWhite());
                numberWild.setText("Locomotive: " + currentHand.getLocomotive());
                discarded.setText("Discarded: " + discardedCards);
            }
        });
        discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (discardHelper.finalDiscard()) {
                    RouteFacadeProxy.SINGLETON.claimRoute(
                            currentClientRoute, discardHelper.getCurrentColor(),
                            discardHelper.getDiscardedColor(), discardHelper.getDiscardedWild());
                }
                else { Toast.makeText(getContext(), "Invalid discard", Toast.LENGTH_SHORT).show(); }
            }
        });
        return v;
    }
    public void setInfoMessage() {
        switch(currentClientRoute.getColor()) {
            case BLUE:
                infoMessage.setText("Please discard " + currentClientRoute.getSpaces() + " blue cards");
                break;
            case GREEN:
                infoMessage.setText("Please discard " + currentClientRoute.getSpaces() + " green cards");
                break;
            case RED:
                infoMessage.setText("Please discard " + currentClientRoute.getSpaces() + " red cards");
                break;
            case PURPLE:
                infoMessage.setText("Please discard " + currentClientRoute.getSpaces() + " purple cards");
                break;
            case ORANGE:
                infoMessage.setText("Please discard " + currentClientRoute.getSpaces() + " orange cards");
                break;
            case YELLOW:
                infoMessage.setText("Please discard " + currentClientRoute.getSpaces() + " yellow cards");
                break;
            case WHITE:
                infoMessage.setText("Please discard " + currentClientRoute.getSpaces() + " white cards");
                break;
            case BLACK:
                infoMessage.setText("Please discard " + currentClientRoute.getSpaces() + " black cards");
                break;
            default:
                infoMessage.setText("Please discard " + currentClientRoute.getSpaces() + " matching cards");
        }
    }


}


