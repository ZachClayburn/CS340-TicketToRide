package com.tickettoride.activities;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tickettoride.R;
import com.tickettoride.clientModels.*;
import com.tickettoride.clientModels.ClientRoute;
import com.tickettoride.clientModels.helpers.PlayerStateHelper;
import com.tickettoride.clientModels.helpers.RouteHelper;
import com.tickettoride.facadeProxies.DestinationCardFacadeProxy;
import com.tickettoride.facadeProxies.TrainCardFacadeProxy;
import com.tickettoride.models.Player;
import com.tickettoride.models.*;
import java.util.List;

import com.tickettoride.models.Color;

public class MapFragment extends Fragment {//TODO once train cars reach 2 and turn for everyone finishes, go to GameOverFragment
    private DrawView drawView;
    private ImageView board;
    private ImageView cardOne;
    private ImageView cardTwo;
    private ImageView cardThree;
    private ImageView cardFour;
    private ImageView cardFive;
    private TextView trainDeck;
    private TextView destDeck;
    private Button drawTrain;
    private Button drawDest;
    private Button viewHand;
    private Button claimRoute;
    private Button history;
    private RecyclerView playerList;
    private Adapter adapter;
    private Context context;
    private PlayerFragmentListener playerListener;
    private ChatFragment chatFragment;
    private View v;
    private DiscardFragmentListener discardFragmentListener;
    private GameOverFragmentListener gameOverListener;
    private HistoryFragmentListener historyListener;
    MapFragment selfMapFragment = this;

    private View.OnClickListener destDeckViewListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            destDeck.setEnabled(false);
            int requiredKeep = 1;
            if (DataManager.SINGLETON.getPlayerState().getClass() == InitializeGameState.class) {
                requiredKeep = 2;
            }
            DestinationCardFacadeProxy.drawDestinationCards(DataManager.getSINGLETON().getPlayer(), requiredKeep);
        }
    };

    private View.OnClickListener drawTrainViewListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            DataManager.SINGLETON.getPlayerState().moveToDrawTrainCardsState(selfMapFragment);
        }
    };
    private View.OnClickListener card1ViewListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (DataManager.SINGLETON.getTrainCardDeck().isFaceupWild(0) && DataManager.SINGLETON.getTrainCardsDrawn() == 1) {
                makeWildCardToast();
                return;
            }
            TrainCardFacadeProxy.SINGLETON.drawFaceupCard(
                    DataManager.SINGLETON.getGame().getGameID(),
                    DataManager.SINGLETON.getPlayer().getPlayerID(), 0);
        }
    };
    private View.OnClickListener card2ViewListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (DataManager.SINGLETON.getTrainCardDeck().isFaceupWild(1) && DataManager.SINGLETON.getTrainCardsDrawn() == 1) {
                makeWildCardToast();
                return;
            }
            TrainCardFacadeProxy.SINGLETON.drawFaceupCard(
                    DataManager.SINGLETON.getGame().getGameID(),
                    DataManager.SINGLETON.getPlayer().getPlayerID(), 1);
        }
    };
    private View.OnClickListener card3ViewListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (DataManager.SINGLETON.getTrainCardDeck().isFaceupWild(2) && DataManager.SINGLETON.getTrainCardsDrawn() == 1) {
                makeWildCardToast();
                return;
            }
            TrainCardFacadeProxy.SINGLETON.drawFaceupCard(
                    DataManager.SINGLETON.getGame().getGameID(),
                    DataManager.SINGLETON.getPlayer().getPlayerID(), 2);
        }
    };
    private View.OnClickListener card4ViewListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (DataManager.SINGLETON.getTrainCardDeck().isFaceupWild(3) && DataManager.SINGLETON.getTrainCardsDrawn() == 1) {
                makeWildCardToast();
                return;
            }
            TrainCardFacadeProxy.SINGLETON.drawFaceupCard(
                    DataManager.SINGLETON.getGame().getGameID(),
                    DataManager.SINGLETON.getPlayer().getPlayerID(), 3);
        }
    };
    private View.OnClickListener card5ViewListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (DataManager.SINGLETON.getTrainCardDeck().isFaceupWild(4) && DataManager.SINGLETON.getTrainCardsDrawn() == 1) {
                makeWildCardToast();
                return;
            }
            TrainCardFacadeProxy.SINGLETON.drawFaceupCard(
                    DataManager.SINGLETON.getGame().getGameID(),
                    DataManager.SINGLETON.getPlayer().getPlayerID(), 4);
        }
    };
    private View.OnClickListener faceDownCardViewListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            TrainCardFacadeProxy.SINGLETON.drawFacedownCard(
                    DataManager.SINGLETON.getGame().getGameID(),
                    DataManager.SINGLETON.getPlayer().getPlayerID());
        }
    };

    private View.OnClickListener drawDestinationListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            DataManager.SINGLETON.getPlayerState().moveToDrawDestinationCardsState(selfMapFragment);
            //List<Player> players = DataManager.getSINGLETON().getGamePlayers();
        }
    };

    private View.OnClickListener claimRouteListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) { DataManager.SINGLETON.getPlayerState().moveToPlaceTrainsState(selfMapFragment); }
    };
    private View.OnClickListener historyViewListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            historyListener = (HistoryFragmentListener) getActivity();
            historyListener.moveToHistory();
            /*gameOverListener = (GameOverFragmentListener) getActivity();//TODO use when game is over
            gameOverListener.moveToGameOver();*/
        }
    };
    public ViewHandListener viewListener;

    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    private View.OnTouchListener handleTouch = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (DataManager.getSINGLETON().getPlayerState().getClass() != ClaimRouteState.class) { return false; }
            int x = (int) event.getX();
            int y = (int) event.getY();
            Log.i("TAG", "touch: (" + x + ", " + y + ")");
            ClientRoute clientRoute = drawView.clickRoute(x, y);
            if (clientRoute != null) {
                DataManager.getSINGLETON().setCurrentClientRoute(clientRoute);
                discardFragmentListener = (DiscardFragmentListener) getActivity();
                discardFragmentListener.moveToDiscard();
            }
            return false;
        }
    };

    public Bitmap draw() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tickettoride);
        Bitmap actualMap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(actualMap);
        this.drawView.draw(canvas);
        return actualMap;
    }

    public void drawExternal() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tickettoride);
        Bitmap actualMap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(actualMap);
        this.drawView.draw(canvas);
        board.setImageBitmap(actualMap);
        this.v.invalidate();
    }

    public double calcScale(View v, View b) {
        ViewGroup.LayoutParams params = b.getLayoutParams();
        System.out.println("layout width: " + params.width);

        int width;
        int height;

        v.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

        //before direct measurement (still with layout params)
        width = b.getMeasuredWidth();
        height = b.getMeasuredHeight();

        //after direct measurement (what the image is expecting)
        b.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int mwidth = b.getMeasuredWidth();
        int mheight = b.getMeasuredHeight();
        float density = getResources().getDisplayMetrics().density;
        int xoffset = 0;
        int yoffset = 0;

        //one of them should not be equal, that one is part of the scale we need
        double scale = 1.0;
        if (mheight != height) {
            scale = (((double) height) / mheight);
            xoffset = (int) ((((double) width) - width * scale) / 2);
        } else if (mwidth != width) {
            scale = (((double) width) / mwidth);
            yoffset = (int) ((((double) height) - height * scale) / 2);
        }

        //need to set scale used for putting in coordinates for touch
        System.out.println("y: " + yoffset + ", x: " + xoffset);
        System.out.println("scale: " + scale);
        ClientRoute.setXoffset(xoffset);
        ClientRoute.setYoffset(yoffset);
        ClientRoute.setScale(scale);
        scale *= density;
        return scale;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        this.v = inflater.inflate(R.layout.game, container, false);
        board = v.findViewById(R.id.game_board);
        double scale = calcScale(v, board);
        this.drawView = new DrawView(getActivity());
        RouteHelper.getSingleton().scaleRouteLines(scale);
        Bitmap actualMap = draw();
        board.setImageBitmap(actualMap);

        View decorView = getActivity().getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        android.support.v7.app.ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.hide();
        PlayerState state = DataManager.getSINGLETON().getPlayerState();
        int turn = DataManager.getSINGLETON().getTurn();
        cardOne = v.findViewById(R.id.first_card);
        cardTwo = v.findViewById(R.id.second_card);
        cardThree = v.findViewById(R.id.third_card);
        cardFour = v.findViewById(R.id.fourth_card);
        cardFive = v.findViewById(R.id.fifth_card);
        setAllColors();
        playerListener = (PlayerFragmentListener) getActivity();
        trainDeck = v.findViewById(R.id.train_deck);
        destDeck = v.findViewById(R.id.dest_deck);
        updateDeckNumbers();
        drawTrain = v.findViewById(R.id.draw_train);
        drawDest = v.findViewById(R.id.draw_dest);
        viewHand = v.findViewById(R.id.view_cards);
        claimRoute = v.findViewById(R.id.claim_route);
        history = v.findViewById(R.id.history_button);
        playerList = v.findViewById(R.id.player_recycler_view);
        context = getActivity();
        getContext();
        playerList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new Adapter(getContext(), DataManager.getSINGLETON().getGamePlayers());
        playerList.setAdapter(adapter);
        setListeners();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            PlayerStateHelper.getSingleton().determinePlayerState(selfMapFragment);
        drawExternal();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        chatFragment = new ChatFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.chat_fragment_container, chatFragment).commit();
    }

    public void updateChat() { chatFragment.updateChat(); }

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
        public void onClick(View view) { }

    }

    public void finishDrawFaceUpTrainCard(TrainCard card) {
//        if (DataManager.SINGLETON.getTrainCardDeck().checkForWild()) {
//            setCardColor(0);
//            setCardColor(1);
//            setCardColor(2);
//            setCardColor(3);
//            setCardColor(4);
//        }

        int trainCardsDrawn = DataManager.SINGLETON.getTrainCardsDrawn();
        if ((card.getColor() == Color.WILD) || trainCardsDrawn == 1) {
            DataManager.getSINGLETON().setTrainCardsDrawn(0);
            TrainCardFacadeProxy.SINGLETON.finish(DataManager.SINGLETON.getGame().getGameID());
        } else {
            DataManager.SINGLETON.setTrainCardsDrawn(++trainCardsDrawn);
        }
    }

    public void finishDrawFacedownCard() {
        int trainCardsDrawn = DataManager.SINGLETON.getTrainCardsDrawn();
        if (trainCardsDrawn == 1) {
            DataManager.getSINGLETON().setTrainCardsDrawn(0);
            TrainCardFacadeProxy.SINGLETON.finish(DataManager.SINGLETON.getGame().getGameID());
        } else {
            DataManager.SINGLETON.setTrainCardsDrawn(++trainCardsDrawn);
        }
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
                destDeck.setEnabled(false);
                Player userPlayer = DataManager.getSINGLETON().getPlayer();
                DestinationCardFacadeProxy.getOfferedCards(userPlayer, 2);
            }
        });
    }

    public void makeWildCardToast() {
        Toast.makeText(this.context, R.string.wild_card_error, Toast.LENGTH_SHORT).show();
    }

    public void onNotTurnStart() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                disableDrawTrainCards();
                drawDest.setEnabled(false);
                drawTrain.setEnabled(false);
                destDeck.setBackgroundResource(R.drawable.whitedeckbackground);
                destDeck.setEnabled(false);
                claimRoute.setEnabled(false);
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

    public void setAllColors() { for (int i = 0; i < 5; i++) { setCardColor(i); } }

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

    public void updateDeckNumbers() {
        trainDeck.setText(getResources().getString(R.string.train_deck, DataManager.SINGLETON.getTrainCardDeckSize()));
        destDeck.setText(getResources().getString(R.string.dest_deck, DataManager.SINGLETON.getDestinationCardDeckSize()));
    }

    public void setCardColor(int i) {
        if (DataManager.SINGLETON.getTrainCardDeck() == null){
            return;
        }
        switch (i) {
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

    public int findCardColor(Color color) {
        switch (color) {
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
        board.setOnTouchListener(handleTouch);
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
        history.setOnClickListener(historyViewListener);
    }
}