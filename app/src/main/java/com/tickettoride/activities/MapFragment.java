package com.tickettoride.activities;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tickettoride.R;
import com.tickettoride.clientModels.*;
import com.tickettoride.clientModels.helpers.PlayerStateHelper;
import com.tickettoride.clientModels.helpers.RouteHelper;
import com.tickettoride.facadeProxies.DestinationCardFacadeProxy;
import com.tickettoride.facadeProxies.TrainCardFacadeProxy;
import com.tickettoride.models.Player;
import com.tickettoride.models.*;
import java.util.List;

import com.tickettoride.models.Color;

public class MapFragment extends Fragment {
    private DrawView drawView;
    private ImageView board;
    private ImageView cardOne;
    private ImageView cardTwo;
    private ImageView cardThree;
    private ImageView cardFour;
    private ImageView cardFive;
    private TextView trainDeck;
    private TextView destDeck;
    private EditText chatWindow;
    private Button drawTrain;
    private Button drawDest;
    private Button viewHand;
    private Button claimRoute;
    private RecyclerView playerList;
    private Adapter adapter;
    private Context context;
    private PlayerFragmentListener playerListener;
    private View v;
    private ClaimRouteListener claimListener;
    MapFragment selfMapFragment = this;

    private View.OnClickListener destDeckViewListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            destDeck.setEnabled(false);
            int requiredKeep = 1;
            if (DataManager.SINGLETON.getPlayerState().getClass() == InitializeGameState.class) { requiredKeep = 2; }
            DestinationCardFacadeProxy.drawDestinationCards(DataManager.getSINGLETON().getPlayer(), requiredKeep);
        }
    };

    private View.OnClickListener drawTrainViewListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) { DataManager.SINGLETON.getPlayerState().moveToDrawTrainCardsState(selfMapFragment); }
    };
    private View.OnClickListener card1ViewListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (DataManager.SINGLETON.getTrainCardDeck().isFaceupWild(0) && DataManager.SINGLETON.getTrainCardsDrawn() == 1){
                makeWildCardToast();
                return;
            }
            TrainCardFacadeProxy.SINGLETON.drawFaceupCard(0, DataManager.getSINGLETON().getPlayer().getPlayerID());
        }
    };
    private View.OnClickListener card2ViewListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (DataManager.SINGLETON.getTrainCardDeck().isFaceupWild(1) && DataManager.SINGLETON.getTrainCardsDrawn() == 1){
                makeWildCardToast();
                return;
            }
            TrainCardFacadeProxy.SINGLETON.drawFaceupCard(1, DataManager.getSINGLETON().getPlayer().getPlayerID());
        }
    };
    private View.OnClickListener card3ViewListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (DataManager.SINGLETON.getTrainCardDeck().isFaceupWild(2) && DataManager.SINGLETON.getTrainCardsDrawn() == 1){
                makeWildCardToast();
                return;
            }
            TrainCardFacadeProxy.SINGLETON.drawFaceupCard(2, DataManager.getSINGLETON().getPlayer().getPlayerID());
        }
    };
    private View.OnClickListener card4ViewListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (DataManager.SINGLETON.getTrainCardDeck().isFaceupWild(3) && DataManager.SINGLETON.getTrainCardsDrawn() == 1){
                makeWildCardToast();
                return;
            }
            TrainCardFacadeProxy.SINGLETON.drawFaceupCard(3, DataManager.getSINGLETON().getPlayer().getPlayerID());
        }
    };
    private View.OnClickListener card5ViewListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (DataManager.SINGLETON.getTrainCardDeck().isFaceupWild(4) && DataManager.SINGLETON.getTrainCardsDrawn() == 1){
                makeWildCardToast();
                return;
            }
            TrainCardFacadeProxy.SINGLETON.drawFaceupCard(4, DataManager.getSINGLETON().getPlayer().getPlayerID());
        }
    };
    private View.OnClickListener faceDownCardViewListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            TrainCardFacadeProxy.SINGLETON.drawFacedownCard(DataManager.getSINGLETON().getPlayer().getPlayerID());
        }
    };

    private View.OnClickListener drawDestinationListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) { DataManager.SINGLETON.getPlayerState().moveToDrawDestinationCardsState(selfMapFragment); }
    };

    private View.OnClickListener claimRouteListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            DataManager.SINGLETON.getPlayerState().moveToPlaceTrainsState(selfMapFragment);
            claimListener = (ClaimRouteListener) getActivity();
            claimListener.moveToClaimRoute();
        }
    };
    public ViewHandListener viewListener;

    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    public Bitmap draw() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tickettoride);
        Bitmap actualMap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(actualMap);
        this.drawView.draw(canvas);
        return actualMap;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        this.v = inflater.inflate(R.layout.game, container, false);
        this.drawView = new DrawView(getActivity());
        RouteHelper.getSingleton().buildRoutes();
        drawView.setRoutes(DataManager.getSINGLETON().getRoutes());
        Bitmap actualMap = draw();
        View decorView = getActivity().getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        android.support.v7.app.ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.hide();
        board = v.findViewById(R.id.game_board);
        board.setImageBitmap(actualMap);
        cardOne = v.findViewById(R.id.first_card);
        setCardColor(0);
        cardTwo = v.findViewById(R.id.second_card);
        setCardColor(1);
        cardThree = v.findViewById(R.id.third_card);
        setCardColor(2);
        cardFour = v.findViewById(R.id.fourth_card);
        setCardColor(3);
        cardFive = v.findViewById(R.id.fifth_card);
        setCardColor(4);
        playerListener = (PlayerFragmentListener) getActivity();
        trainDeck = v.findViewById(R.id.train_deck);
        destDeck = v.findViewById(R.id.dest_deck);
        chatWindow = v.findViewById(R.id.chat_room);
        drawTrain = v.findViewById(R.id.draw_train);
        drawDest = v.findViewById(R.id.draw_dest);
        viewHand = v.findViewById(R.id.view_cards);
        claimRoute = v.findViewById(R.id.claim_route);
        playerList = v.findViewById(R.id.player_recycler_view);
        context = getActivity();
        getContext();
        playerList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new Adapter(getContext(), DataManager.getSINGLETON().getGamePlayers());
        playerList.setAdapter(adapter);
        PlayerStateHelper.getSingleton().determinePlayerState(selfMapFragment);
        setListeners();
        return v;
    }

    class Adapter extends RecyclerView.Adapter<Holder> {
        private List<Player> players;
        private LayoutInflater inflater;
        private int count = 0;

        public Adapter(Context context, List<Player> players) {
            this.players = players;
            inflater = LayoutInflater.from(context);
            for (Player player : players) {
                ++count;
            }
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.player_list, parent, false);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            Player curPlayer = players.get(position);
            holder.bind(curPlayer);
        }

        @Override
        public int getItemCount() {
            return count;
        }

    }

    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView points;
        private TextView playerName;
        private Player curPlayer;

        public Holder(View view) {
            super(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    playerListener.toPlayerFragment(curPlayer.getPlayerID());
                }
            });
            playerName = view.findViewById(R.id.player_name);
            points = view.findViewById(R.id.player_points);
        }

        void bind(Player curPlayer) {
            this.curPlayer = curPlayer;
            points.setText("Points: " + curPlayer.getPoints());
            playerName.setText("Username: " + curPlayer.getUsername());
        }

        @Override
        public void onClick(View view) {}

    }

    public void finishDrawFaceUpTrainCard(TrainCard card) {
        if (DataManager.SINGLETON.getTrainCardDeck().checkForWild()){
            setCardColor(0);
            setCardColor(1);
            setCardColor(2);
            setCardColor(3);
            setCardColor(4);
        }

        int trainCardsDrawn = DataManager.SINGLETON.getTrainCardsDrawn();
        if ((card.getColor() == Color.WILD) || trainCardsDrawn == 1) {
            GameRoomActivity activity = (GameRoomActivity) getActivity();
            activity.incrementTurn();
        }
        else { DataManager.SINGLETON.setTrainCardsDrawn(++trainCardsDrawn); }
    }

    public void finishDrawFacedownCard(){
        int trainCardsDrawn = DataManager.SINGLETON.getTrainCardsDrawn();
        if (trainCardsDrawn == 1) {
            GameRoomActivity activity = (GameRoomActivity) getActivity();
            activity.incrementTurn();
        }
        else { DataManager.SINGLETON.setTrainCardsDrawn(++trainCardsDrawn);}
    }

    public void onTurnStart() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                disableDrawTrainCards();
                drawTrain.setEnabled(true);
                drawDest.setEnabled(true);
                claimRoute.setEnabled(true);
                destDeck.setBackgroundResource(R.drawable.whitedeckbackground);
                destDeck.setEnabled(false);
            }
        });
    }

    public void onInitializeTurn() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                disableDrawTrainCards();
                drawDest.setEnabled(false);
                claimRoute.setEnabled(false);
                drawTrain.setEnabled(false);
                destDeck.setBackgroundResource(R.drawable.yellowdeckbackground);
                trainDeck.setBackgroundResource(R.drawable.whitedeckbackground);
                destDeck.setEnabled(true);
            }
        });
    }
    public void makeWildCardToast() { Toast.makeText(this.context, R.string.wild_card_error, Toast.LENGTH_SHORT).show(); }

    public void onNotTurnStart() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                disableDrawTrainCards();
                drawDest.setEnabled(false);
                drawTrain.setEnabled(false);
                destDeck.setBackgroundResource(R.drawable.whitedeckbackground);
                destDeck.setEnabled(false);
            }
        });
    }

    public void onDrawDestination() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                disableDrawTrainCards();
                drawDest.setEnabled(false);
                drawTrain.setEnabled(false);
                claimRoute.setEnabled(false);
                destDeck.setBackgroundResource(R.drawable.yellowdeckbackground);
                destDeck.setEnabled(true);
            }
        });
    }

    public void onDrawTrainCards() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DataManager.SINGLETON.setTrainCardsDrawn(0);
                enableDrawTrainCards();
                drawTrain.setEnabled(false);
                drawDest.setEnabled(false);
                claimRoute.setEnabled(false);
                destDeck.setBackgroundResource(R.drawable.whitedeckbackground);
                destDeck.setEnabled(false);
            }
        });

    }

    public void onClaimRoute() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                disableDrawTrainCards();
                drawTrain.setEnabled(false);
                drawDest.setEnabled(false);
                claimRoute.setEnabled(false);
                destDeck.setBackgroundResource(R.drawable.whitedeckbackground);
                destDeck.setEnabled(false);
            }
        });

    }

    public void enableDrawTrainCards() {
        cardOne.setEnabled(true);
        cardTwo.setEnabled(true);
        cardThree.setEnabled(true);
        cardFour.setEnabled(true);
        cardFive.setEnabled(true);
        trainDeck.setBackgroundResource(R.drawable.yellowdeckbackground);
        cardOne.setBackgroundResource(R.drawable.yellowbackground);
        cardTwo.setBackgroundResource(R.drawable.yellowbackground);
        cardThree.setBackgroundResource(R.drawable.yellowbackground);
        cardFour.setBackgroundResource(R.drawable.yellowbackground);
        cardFive.setBackgroundResource(R.drawable.yellowbackground);
        trainDeck.setEnabled(true);
    }

    public void setAllColors(){ for (int i = 0; i < 5; i++){ setCardColor(i); } }

    public void disableDrawTrainCards() {
        cardOne.setEnabled(false);
        cardTwo.setEnabled(false);
        cardThree.setEnabled(false);
        cardFour.setEnabled(false);
        cardFive.setEnabled(false);
        trainDeck.setBackgroundResource(R.drawable.whitedeckbackground);
        cardOne.setBackgroundResource(R.drawable.whitebackground);
        cardTwo.setBackgroundResource(R.drawable.whitebackground);
        cardThree.setBackgroundResource(R.drawable.whitebackground);
        cardFour.setBackgroundResource(R.drawable.whitebackground);
        cardFive.setBackgroundResource(R.drawable.whitebackground);
        trainDeck.setEnabled(false);
    }

    public void setCardColor(int i){
        switch(i){
            case 0:
                cardOne.setImageResource(findCardColor(DataManager.SINGLETON.getTrainCardDeck().getFaceupColor(0)));
                return;
            case 1:
                cardTwo.setImageResource(findCardColor(DataManager.SINGLETON.getTrainCardDeck().getFaceupColor(1)));
                return;
            case 2:
                cardThree.setImageResource(findCardColor(DataManager.SINGLETON.getTrainCardDeck().getFaceupColor(2)));
                return;
            case 3:
                cardFour.setImageResource(findCardColor(DataManager.SINGLETON.getTrainCardDeck().getFaceupColor(3)));
                return;
            case 4:
                cardFive.setImageResource(findCardColor(DataManager.SINGLETON.getTrainCardDeck().getFaceupColor(4)));
                return;
        }
    }

    public int findCardColor(Color color){
        switch(color) {
            case GREEN:
                return R.drawable.green_card;
            case RED:
                return R.drawable.red_card;
            case BLUE:
                return R.drawable.blue_card;
            case YELLOW:
                return R.drawable.yellow_card;
            case PURPLE:
                return R.drawable.purple_card;
            case ORANGE:
                return R.drawable.orange_card;
            case BLACK:
                return R.drawable.black_card;
            case WHITE:
                return R.drawable.white_card;
            case WILD:
                return R.drawable.locomotive;
            default:
                return R.drawable.locomotive;
        }
    }

    private void setListeners() {
        cardOne.setOnClickListener(card1ViewListener);
        cardTwo.setOnClickListener(card2ViewListener);
        cardThree.setOnClickListener(card3ViewListener);
        cardFour.setOnClickListener(card4ViewListener);
        cardFive.setOnClickListener(card5ViewListener);
        trainDeck.setOnClickListener(faceDownCardViewListener);
        destDeck.setOnClickListener(destDeckViewListener);
        drawTrain.setOnClickListener(drawTrainViewListener);
        drawDest.setOnClickListener(drawDestinationListener);
        claimRoute.setOnClickListener(claimRouteListener);
        viewHand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewListener = (ViewHandListener) getActivity();
                viewListener.toViewHandFragment();
            }
        });
    }
}