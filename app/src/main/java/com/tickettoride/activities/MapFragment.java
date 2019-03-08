package com.tickettoride.activities;

import android.content.Context;
import android.content.res.Resources;
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

import com.tickettoride.R;
import com.tickettoride.clientModels.DataManager;
import com.tickettoride.clientModels.Player;
import com.tickettoride.models.TrainCard;

import java.util.ArrayList;
import java.util.Arrays;

import modelAttributes.Color;

public class MapFragment extends Fragment {
    private ArrayList<Player> players = new ArrayList<>();
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
    private int cardsDrawn = 0;
    // UNCOMMENT AFTER MERGE WITH TRAVIS
    //private ArrayList<Route> lines = new ArrayList<>();
    //private PlayerFragmentListener playerListener;
    //public ViewHandListener viewListener;
    //private ArrayList<> dest cards
    private Context context;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        // UNCOMMENT AFTER MERGE WITH TRAVIS
        //playerListener = (PlayerFragmentListener) getActivity();
        //viewListener = (ViewHandListener) getActivity();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.game, container, false);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //requestWindowFeature(Window.FEATURE_ACTION_BAR);
        Resources res = getResources();
        // UNCOMMENT AFTER MERGE WITH TRAVIS
        //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tickettoride);
        //Bitmap actualMap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        //Canvas canvas = new Canvas(actualMap);
        //drawView = new DrawView(getActivity());
        //drawView.draw(canvas);

        // DON'T UNCOMMENT THIS
        //getActivity().setContentView(R.layout.game);
        View decorView = getActivity().getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        android.support.v7.app.ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.hide();
        routeLineInit();
        board = (ImageView) v.findViewById(R.id.game_board);

        // UNCOMMENT AFTER MERGE WITH TRAVIS
        //board.setImageBitmap(actualMap);
        cardOne = (ImageView) v.findViewById(R.id.first_card);
        cardTwo = (ImageView) v.findViewById(R.id.second_card);
        cardThree = (ImageView) v.findViewById(R.id.third_card);
        cardFour = (ImageView) v.findViewById(R.id.fourth_card);
        cardFive = (ImageView) v.findViewById(R.id.fifth_card);
        trainDeck = (TextView) v.findViewById(R.id.train_deck);
        destDeck = (TextView) v.findViewById(R.id.dest_deck);
        chatWindow = (EditText) v.findViewById(R.id.chat_room);
        drawTrain = (Button) v.findViewById(R.id.draw_train);
        drawDest = (Button) v.findViewById(R.id.draw_dest);
        viewHand = (Button) v.findViewById(R.id.view_cards);
        claimRoute = (Button) v.findViewById(R.id.claim_route);
        playerList = (RecyclerView) v.findViewById(R.id.player_list);
        context = getActivity();
        //playerList.setAdapter(adapter);
        cardOne.setEnabled(false);
        cardTwo.setEnabled(false);
        cardThree.setEnabled(false);
        cardFour.setEnabled(false);
        cardFive.setEnabled(false);
        trainDeck.setEnabled(false);
        destDeck.setEnabled(false);
        drawTrain.setEnabled(false);
        drawDest.setEnabled(false);
        claimRoute.setEnabled(false);
        playerList = v.findViewById(R.id.player_list);
        playerList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new Adapter(getContext(), players);
        cardOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TrainCard card = DataManager.SINGLETON.getTrainCardDeck().drawFromFaceUp(0);
                // TODO: Add to Hand
                if ((card.getColor() == Color.WILD) || cardsDrawn == 1){
                    endDrawTrainCards();
                }
                else{
                    cardsDrawn++;
                }
            }
        });
        cardTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TrainCard card = DataManager.SINGLETON.getTrainCardDeck().drawFromFaceUp(1);
                // TODO: Add to Hand
                if ((card.getColor() == Color.WILD) || cardsDrawn == 1){
                    endDrawTrainCards();
                }
                else{
                    cardsDrawn++;
                }
            }
        });
        cardThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TrainCard card = DataManager.SINGLETON.getTrainCardDeck().drawFromFaceUp(2);
                // TODO: Add to Hand
                if ((card.getColor() == Color.WILD) || cardsDrawn == 1){
                    endDrawTrainCards();
                }
                else{
                    cardsDrawn++;
                }
            }
        });
        cardFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TrainCard card = DataManager.SINGLETON.getTrainCardDeck().drawFromFaceUp(3);
                // TODO: Add to Hand
                if ((card.getColor() == Color.WILD) || cardsDrawn == 1){
                    endDrawTrainCards();
                }
                else{
                    cardsDrawn++;
                }
            }
        });
        cardFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TrainCard card = DataManager.SINGLETON.getTrainCardDeck().drawFromFaceUp(4);
                // TODO: Add to Hand
                if ((card.getColor() == Color.WILD) || cardsDrawn == 1){
                    endDrawTrainCards();
                }
                else{
                    cardsDrawn++;
                }
            }
        });
        trainDeck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TrainCard card = DataManager.SINGLETON.getTrainCardDeck().drawFromFaceDown();
                // TODO: Add to Hand
                if (cardsDrawn == 1){
                    endDrawTrainCards();
                }
                else{
                    cardsDrawn++;
                }
            }
        });
        drawTrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardsDrawn = 0;
                cardOne.setEnabled(true);
                cardTwo.setEnabled(true);
                cardThree.setEnabled(true);
                cardFour.setEnabled(true);
                cardFive.setEnabled(true);
                trainDeck.setEnabled(true);
                drawTrain.setEnabled(false);
                drawDest.setEnabled(false);
                claimRoute.setEnabled(false);
            }
        });
        drawDest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawTrain.setEnabled(false);
                drawDest.setEnabled(false);
                claimRoute.setEnabled(false);
            }
        });
        claimRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawTrain.setEnabled(false);
                drawDest.setEnabled(false);
                claimRoute.setEnabled(false);
            }
        });
        viewHand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // UNCOMMENT AFTER MERGE WITH TRAVIS
                //viewListener.toViewHandFragment(players.get(0).getPlayerID());//TODO get player ID
            }
        });
        return v;
    }
    class Adapter extends RecyclerView.Adapter<Holder> {

        private ArrayList<Player> players;
        private LayoutInflater inflater;

        public Adapter(Context context, ArrayList<Player> players) {
            this.players = players;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            // UNCOMMENT AFTER MERGE WITH TRAVIS
            //View view = inflater.inflate(R.layout.player_list, parent, false);
            //return new Holder(view);
            return null;
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            Player curPlayer = players.get(position);
            holder.bind(curPlayer);
        }

        @Override
        public int getItemCount() {
            return players.size();
        }

    }

    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView points;
        private TextView playerName;
        private Player curPlayer;

        public Holder(View view) {
            super(view);
            points = view.findViewById(R.id.player_points);
            // UNCOMMENT AFTER MERGE WITH TRAVIS
            //playerName = view.findViewById(R.id.player_name);
        }

        void bind(Player curPlayer) {
            this.curPlayer = curPlayer;
            points.setText(curPlayer.getPoints());
            playerName.setText(curPlayer.getUsername());
        }

        @Override
        public void onClick(View view) {
            // UNCOMMENT AFTER MERGE WITH TRAVIS
            //playerListener.toPlayerFragment(curPlayer.getPlayerID());
        }

    }
    public void routeLineInit() {
        // Updated when merging with Travis's branch
        }
    public void onTurnStart() {
        drawTrain.setEnabled(true);
        drawDest.setEnabled(true);
        claimRoute.setEnabled(true);
    }

    public void endDrawTrainCards(){
        cardOne.setEnabled(false);
        cardTwo.setEnabled(false);
        cardThree.setEnabled(false);
        cardFour.setEnabled(false);
        cardFive.setEnabled(false);
        // TODO: Change Turn
    }
}