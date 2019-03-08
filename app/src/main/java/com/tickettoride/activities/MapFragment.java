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
import com.tickettoride.models.City;
import com.tickettoride.models.Line;
import com.tickettoride.models.Route;
import com.tickettoride.models.TrainCard;
import java.util.ArrayList;
import java.util.Arrays;
import modelAttributes.Color;
public class MapFragment extends Fragment {
    private ArrayList<Player> players = new ArrayList<>();
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
    private int cardsDrawn = 0;
    private ArrayList<Route> lines = new ArrayList<>();
    private PlayerFragmentListener playerListener;
    public ViewHandListener viewListener;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        playerListener = (PlayerFragmentListener) getActivity();
        viewListener = (ViewHandListener) getActivity();
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.game, container, false);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //requestWindowFeature(Window.FEATURE_ACTION_BAR);
        Resources res = getResources();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tickettoride);
        Bitmap actualMap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(actualMap);
        drawView = new DrawView(getActivity());
        drawView.draw(canvas);
        // DON'T UNCOMMENT THIS
        //getActivity().setContentView(R.layout.game);
        View decorView = getActivity().getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        android.support.v7.app.ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.hide();
        routeLineInit();
        board = (ImageView) v.findViewById(R.id.game_board);
        board.setImageBitmap(actualMap);
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
                viewListener.toViewHandFragment(players.get(0).getPlayerID());//TODO get player ID
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
            playerName = view.findViewById(R.id.player_name);
        }

        void bind(Player curPlayer) {
            this.curPlayer = curPlayer;
            points.setText(curPlayer.getPoints());
            playerName.setText(curPlayer.getUsername());
        }

        @Override
        public void onClick(View view) {
            playerListener.toPlayerFragment(curPlayer.getPlayerID());
        }

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
    public void routeLineInit() {
        lines.add(new Route(Arrays.asList(new Line(150, 130, 150, 180)), Color.GREY, 1, City.VANCOUVER, City.SEATTLE, false, null));
        lines.add(new Route(Arrays.asList(new Line(130, 130, 130, 180)), Color.GREY, 1, City.VANCOUVER, City.SEATTLE, false, null));
        lines.add(new Route(Arrays.asList(new Line(160, 108, 330, 88)), Color.GREY, 3, City.VANCOUVER, City.CALGARY, false, null));
        lines.add(new Route(Arrays.asList(new Line(350, 98, 495, 273)), Color.GREY, 4, City.CALGARY, City.HELENA, false, null));
        lines.add(new Route(Arrays.asList(new Line(510, 268, 665, 118)), Color.BLUE, 4, City.HELENA, City.WINNIPEG, false, null));
        lines.add(new Route(Arrays.asList(new Line(695, 118, 850, 268)), Color.BLACK, 4, City.WINNIPEG, City.DULUTH, false, null));
        lines.add(new Route(Arrays.asList(new Line(515, 288, 845, 283)), Color.ORANGE, 6, City.HELENA, City.DULUTH, false, null));
        lines.add(new Route(Arrays.asList(new Line(155, 215, 485, 293)), Color.YELLOW, 6, City.SEATTLE, City.HELENA, false, null));
        lines.add(new Route(Arrays.asList(new Line(142, 213, 123, 260)), Color.GREY, 1, City.SEATTLE, City.PORTLAND, false, null));
        lines.add(new Route(Arrays.asList(new Line(122, 208, 103, 255)), Color.GREY, 1, City.SEATTLE, City.PORTLAND, false, null));
        lines.add(new Route(Arrays.asList(new Line(485, 308, 400, 450)), Color.PURPLE, 3, City.HELENA, City.SALT_LAKE_CITY, false, null));
        lines.add(new Route(Arrays.asList(new Line(505, 308, 583, 500)), Color.GREEN, 4, City.HELENA, City.DENVER, false, null));
        lines.add(new Route(Arrays.asList(new Line(525, 305, 783, 410)), Color.RED, 5, City.HELENA, City.OMAHA, false, null));
        lines.add(new Route(Arrays.asList(new Line(405, 467, 563, 500)), Color.RED, 3, City.SALT_LAKE_CITY, City.DENVER, false, null));
        lines.add(new Route(Arrays.asList(new Line(405, 487, 563, 520)), Color.YELLOW, 3, City.SALT_LAKE_CITY, City.DENVER, false, null));
        lines.add(new Route(Arrays.asList(new Line(365, 473, 110, 557)), Color.ORANGE, 5, City.SAN_FRANCISCO, City.SALT_LAKE_CITY, false, null));
        lines.add(new Route(Arrays.asList(new Line(365, 493, 115, 577)), Color.WHITE, 5, City.SAN_FRANCISCO, City.SALT_LAKE_CITY, false, null));
        lines.add(new Route(Arrays.asList(new Line(1040, 170, 705, 100)), Color.GREY, 6, City.WINNIPEG, City.SAUL_ST_MARIE, false, null));
        lines.add(new Route(Arrays.asList(new Line(1070, 180, 1180, 205)), Color.GREY, 2, City.SAUL_ST_MARIE, City.TORONTO, false, null));
        lines.add(new Route(Arrays.asList(new Line(1040, 190, 885, 255)), Color.GREY, 3, City.SAUL_ST_MARIE, City.DENVER, false, null));
        lines.add(new Route(Arrays.asList(new Line(1200, 222, 875, 277)), Color.PURPLE, 6, City.DULUTH, City.TORONTO, false, null));
        lines.add(new Route(Arrays.asList(new Line(1230, 227, 1235, 327)), Color.GREY, 2, City.TORONTO, City.PITTSBURGH, false, null));
        lines.add(new Route(Arrays.asList(new Line(1345, 272, 1250, 332)), Color.WHITE, 2, City.PITTSBURGH, City.NEW_YORK, false, null));
        lines.add(new Route(Arrays.asList(new Line(1360, 287, 1265, 347)), Color.GREEN, 2, City.PITTSBURGH, City.NEW_YORK, false, null));
        lines.add(new Route(Arrays.asList(new Line(1370, 302, 1375, 407)), Color.ORANGE, 2, City.NEW_YORK, City.WASHINGTON, false, null));
        lines.add(new Route(Arrays.asList(new Line(1390, 302, 1395, 407)), Color.BLACK, 2, City.NEW_YORK, City.WASHINGTON, false, null));
        lines.add(new Route(Arrays.asList(new Line(1360, 262, 1330, 100)), Color.BLUE, 3, City.MONTREAL, City.NEW_YORK, false, null));
        lines.add(new Route(Arrays.asList(new Line(1380, 267, 1435, 180)), Color.YELLOW, 2, City.NEW_YORK, City.BOSTON, false, null));
        lines.add(new Route(Arrays.asList(new Line(1400, 277, 1455, 190)), Color.RED, 2, City.NEW_YORK, City.BOSTON, false, null));
        lines.add(new Route(Arrays.asList(new Line(1350, 97, 1435, 165)), Color.GREY, 2, City.BOSTON, City.MONTREAL, false, null));
        lines.add(new Route(Arrays.asList(new Line(1360, 82, 1445, 150)), Color.GREY, 2, City.BOSTON, City.MONTREAL, false, null));
        lines.add(new Route(Arrays.asList(new Line(1367, 427, 1300, 505)), Color.GREY, 2, City.WASHINGTON, City.RALEIGH, false, null));
        lines.add(new Route(Arrays.asList(new Line(1382, 442, 1315, 520)), Color.GREY, 2, City.WASHINGTON, City.RALEIGH, false, null));
        lines.add(new Route(Arrays.asList(new Line(1292, 538, 1213, 607)), Color.GREY, 2, City.RALEIGH, City.ATLANTA, false, null));
        lines.add(new Route(Arrays.asList(new Line(1281, 524, 1202, 593)), Color.GREY, 2, City.RALEIGH, City.ATLANTA, false, null));
        lines.add(new Route(Arrays.asList(new Line(1281, 494, 1258, 385)), Color.GREY, 2, City.RALEIGH, City.PITTSBURGH, false, null));
        lines.add(new Route(Arrays.asList(new Line(1265, 365, 995, 502)), Color.GREY, 2, City.WASHINGTON, City.PITTSBURGH, false, null));
        lines.add(new Route(Arrays.asList(new Line(1230, 365, 1365, 415)), Color.GREEN, 5, City.PITTSBURGH, City.SAINT_LOUIS, false, null));
        lines.add(new Route(Arrays.asList(new Line(990, 524, 1090, 554)), Color.GREY, 2, City.SAINT_LOUIS, City.NASHVILLE, false, null));
        lines.add(new Route(Arrays.asList(new Line(975, 514, 947, 602)), Color.GREY, 2, City.KANSAS_CITY, City.LITTLE_ROCK, false, null));
        lines.add(new Route(Arrays.asList(new Line(937, 625, 832, 630)), Color.GREY, 2, City.LITTLE_ROCK, City.OKLAHOMA_CITY, false, null));
        lines.add(new Route(Arrays.asList(new Line(837, 640, 850, 747)), Color.GREY, 2, City.OKLAHOMA_CITY, City.DALLAS, false, null));
        lines.add(new Route(Arrays.asList(new Line(817, 640, 830, 747)), Color.GREY, 2, City.OKLAHOMA_CITY, City.DALLAS, false, null));
        lines.add(new Route(Arrays.asList(new Line(808, 612, 837, 510)), Color.GREY, 2, City.OKLAHOMA_CITY, City.KANSAS_CITY, false, null));
        lines.add(new Route(Arrays.asList(new Line(828, 615, 857, 513)), Color.GREY, 2, City.OKLAHOMA_CITY, City.KANSAS_CITY, false, null));
        lines.add(new Route(Arrays.asList(new Line(857, 505, 959, 502)), Color.PURPLE, 2, City.KANSAS_CITY, City.SAINT_LOUIS, false, null));
        lines.add(new Route(Arrays.asList(new Line(857, 485, 1040, 392)), Color.BLUE, 2, City.KANSAS_CITY, City.SAINT_LOUIS, false, null));
        lines.add(new Route(Arrays.asList(new Line(964, 475, 1025, 382)), Color.GREEN, 2, City.SAINT_LOUIS, City.CHICAGO, false, null));
        lines.add(new Route(Arrays.asList(new Line(980, 485, 1040, 392)), Color.WHITE, 2, City.SAINT_LOUIS, City.CHICAGO, false, null));
        lines.add(new Route(Arrays.asList(new Line(937, 640, 867, 740)), Color.GREY, 2, City.LITTLE_ROCK, City.DALLAS, false, null));
        lines.add(new Route(Arrays.asList(new Line(863, 765, 897, 805)), Color.GREY, 1, City.DALLAS, City.HOUSTON, false, null));
        lines.add(new Route(Arrays.asList(new Line(848, 780, 882, 820)), Color.GREY, 1, City.DALLAS, City.HOUSTON, false, null));
        lines.add(new Route(Arrays.asList(new Line(917, 820, 1027, 805)), Color.GREY, 2, City.HOUSTON, City.NEW_ORLEANS, false, null));
        lines.add(new Route(Arrays.asList(new Line(1037, 795, 957, 650)), Color.GREEN, 3, City.LITTLE_ROCK, City.NEW_ORLEANS, false, null));
        lines.add(new Route(Arrays.asList(new Line(1130, 566, 1175, 599)), Color.GREY, 1, City.NASHVILLE, City.ATLANTA, false, null));
        lines.add(new Route(Arrays.asList(new Line(1215, 624, 1325, 630)), Color.GREY, 2, City.ATLANTA, City.CHARLESTON, false, null));
        lines.add(new Route(Arrays.asList(new Line(1200, 634, 1375, 855)), Color.BLUE, 5, City.ATLANTA, City.MIAMI, false, null));
        lines.add(new Route(Arrays.asList(new Line(850, 475, 827, 430)), Color.GREY, 1, City.OMAHA, City.KANSAS_CITY, false, null));
        lines.add(new Route(Arrays.asList(new Line(835, 485, 812, 440)), Color.GREY, 1, City.OMAHA, City.KANSAS_CITY, false, null));
        lines.add(new Route(Arrays.asList(new Line(859, 303, 822, 400)), Color.GREY, 2, City.OMAHA, City.DULUTH, false, null));
        lines.add(new Route(Arrays.asList(new Line(840, 300, 804, 398)), Color.GREY, 2, City.OMAHA, City.DULUTH, false, null));
        lines.add(new Route(Arrays.asList(new Line(822, 772, 610, 805)), Color.RED, 4, City.EL_PASO, City.DALLAS, false, null));
        lines.add(new Route(Arrays.asList(new Line(550, 792, 400, 747)), Color.GREY, 3, City.PHOENIX, City.EL_PASO, false, null));
        lines.add(new Route(Arrays.asList(new Line(413, 729, 560, 664)), Color.GREY, 3, City.PHOENIX, City.SANTA_FE, false, null));
        lines.add(new Route(Arrays.asList(new Line(590, 662, 760, 641)), Color.BLUE, 3, City.SANTA_FE, City.OKLAHOMA_CITY, false, null));
        lines.add(new Route(Arrays.asList(new Line(575, 647, 580, 540)), Color.GREY, 2, City.SANTA_FE, City.DENVER, false, null));
        lines.add(new Route(Arrays.asList(new Line(575, 677, 570, 784)), Color.GREY, 2, City.EL_PASO, City.SANTA_FE, false, null));
        lines.add(new Route(Arrays.asList(new Line(160, 195, 220, 195), new Line(220, 195, 265, 190),
                new Line(265, 190, 320, 150), new Line(320, 150, 340, 105)), Color.GREY, 4, City.SEATTLE, City.CALGARY, false, null));
        lines.add(new Route(Arrays.asList(new Line(360, 85, 415, 62), new Line(415, 62, 465, 53),
                new Line(520, 50, 580, 53), new Line(580, 53, 625, 65), new Line(625, 65, 675, 90)), Color.WHITE,
                6, City.CALGARY, City.WINNIPEG, false, null));
        lines.add(new Route(Arrays.asList(new Line(1065, 165, 1100, 132), new Line(1100, 132, 1150, 107),
                new Line(1150, 107, 1205, 87), new Line(1205, 87, 1255, 77), new Line(1255, 77,1310,72)),
                Color.BLACK, 5, City.SAUL_ST_MARIE, City.MONTREAL, false, null));
        lines.add(new Route(Arrays.asList(new Line(1325, 90, 1275, 110), new Line(1275, 110, 1240, 145),
                new Line(1240, 145, 1210, 190)), Color.GREY, 3, City.MONTREAL, City.TORONTO, false, null));
        lines.add(new Route(Arrays.asList(new Line(1210, 230, 1165, 265), new Line(1165, 265, 1115, 275),
                new Line(1115, 275, 1070, 310), new Line(1070, 310, 1035, 350)), Color.WHITE, 4, City.TORONTO,
                City.CHICAGO, false, null));
        lines.add(new Route(Arrays.asList(new Line(1055, 350, 1105, 337), new Line(1105, 337, 1160, 330),
                new Line(1160, 330, 1215, 330)), Color.ORANGE, 3, City.CHICAGO, City.PITTSBURGH, false, null));
        lines.add(new Route(Arrays.asList(new Line(1060, 370, 1115, 355), new Line(1115, 355, 1165, 350),
                new Line(1165, 350, 1220, 350)), Color.BLACK, 3, City.CHICAGO, City.PITTSBURGH, false, null));
        lines.add(new Route(Arrays.asList(new Line(1025, 352, 965, 337), new Line(965, 337, 915, 320),
                new Line(915, 320, 865, 290)), Color.RED, 3, City.DULUTH, City.CHICAGO, false, null));
        lines.add(new Route(Arrays.asList(new Line(1025, 375, 920, 355), new Line(915, 360, 830, 415)),
                Color.BLUE, 4, City.OMAHA, City.CHICAGO, false, null));
        lines.add(new Route(Arrays.asList(new Line(795, 425, 745, 435), new Line(745, 435, 693, 450),
                new Line(693, 450, 643, 470), new Line(643, 470, 593, 510)), Color.PURPLE, 4, City.OMAHA,
                City.DENVER, false, null));
        lines.add(new Route(Arrays.asList(new Line(613, 520, 663, 525), new Line(663, 525, 718, 525),
                new Line(718, 525, 768, 515), new Line(768, 515, 823, 495)), Color.BLACK, 4, City.DENVER,
                City.KANSAS_CITY, false, null));
        lines.add(new Route(Arrays.asList(new Line(613, 542, 663, 545), new Line(663, 545, 713, 545),
                new Line(713, 545, 773, 535), new Line(773, 535, 823, 515)), Color.ORANGE, 4, City.DENVER,
                City.KANSAS_CITY, false, null));
        lines.add(new Route(Arrays.asList(new Line(596, 550, 636, 590), new Line(636, 590, 686, 613),
                new Line(686, 613, 736, 618), new Line(736, 618, 786, 623)), Color.RED, 4, City.DENVER,
                City.OKLAHOMA_CITY, false, null));
        lines.add(new Route(Arrays.asList(new Line(801, 638, 768, 678), new Line(768, 678, 728, 718),
                new Line(728, 718, 678, 750), new Line(678, 750, 638, 770), new Line(638, 770, 588, 790)),
                Color.YELLOW, 5, City.EL_PASO, City.OKLAHOMA_CITY, false, null));
        lines.add(new Route(Arrays.asList(new Line(578, 815, 628, 840), new Line(628, 840, 673, 855),
                new Line(673, 855, 728, 863), new Line(728, 863, 778, 865), new Line(778, 865, 828, 860),
                new Line(828, 860, 888, 835)), Color.GREEN, 6, City.EL_PASO, City.HOUSTON, false, null));
        lines.add(new Route(Arrays.asList(new Line(538, 802, 488, 815), new Line(488, 815, 428, 820),
                new Line(428, 820, 373, 812), new Line(373, 812, 318, 800), new Line(318, 800, 268, 780),
                new Line(268, 780, 218, 740)), Color.BLACK, 6, City.LOS_ANGELES, City.EL_PASO, false, null));
        lines.add(new Route(Arrays.asList(new Line(218, 725, 268, 717), new Line(268, 717, 318, 717),
                new Line(318, 717, 378, 727)), Color.GREY, 3, City.LOS_ANGELES, City.PHOENIX, false, null));
        lines.add(new Route(Arrays.asList(new Line(393, 727, 413, 677), new Line(413, 677, 443, 627),
                new Line(443, 677, 443, 627), new Line(478, 577, 518, 547), new Line(518, 547, 563, 537)),
                Color.WHITE, 5, City.PHOENIX, City.DENVER, false, null));
        lines.add(new Route(Arrays.asList(new Line(193, 720, 158, 678), new Line(158, 678, 123, 628),
                new Line(123, 628, 103, 578)), Color.PURPLE, 3, City.SAN_FRANCISCO, City.LOS_ANGELES, false, null));
        lines.add(new Route(Arrays.asList(new Line(183, 735, 143, 695), new Line(143, 695, 108, 640),
                new Line(108, 640, 88, 590)), Color.YELLOW, 3, City.SAN_FRANCISCO, City.LOS_ANGELES, false, null));
        lines.add(new Route(Arrays.asList(new Line(78, 550, 63, 500), new Line(63, 500, 58, 450),
                new Line(58, 450, 63, 395), new Line(63, 395, 68, 340), new Line(68, 340, 88, 290)),
                Color.GREEN, 5, City.SAN_FRANCISCO, City.PORTLAND, false, null));
        lines.add(new Route(Arrays.asList(new Line(98, 555, 83, 505), new Line(83, 505, 78, 455),
                new Line(78, 455, 78, 400), new Line(78, 400, 88, 350), new Line(88, 350, 108, 295)),
                Color.PURPLE, 5, City.SAN_FRANCISCO, City.PORTLAND, false, null));
        lines.add(new Route(Arrays.asList(new Line(128, 275, 178, 285), new Line(178, 285, 228, 305),
                new Line(228, 305, 278, 335), new Line(278, 335, 328, 380), new Line(328, 380, 370, 430),
                new Line(370, 430, 380, 460)), Color.BLUE, 6, City.PORTLAND, City.SALT_LAKE_CITY, false, null));
        lines.add(new Route(Arrays.asList(new Line(385, 495, 380, 545), new Line(380, 545, 360, 600),
                new Line(360, 600, 320, 640)), Color.ORANGE, 3, City.SALT_LAKE_CITY, City.LAS_VEGAS, false, null));
        lines.add(new Route(Arrays.asList(new Line(285, 645, 235, 655), new Line(235, 655, 205, 705)), Color.GREY, 2,
                City.LOS_ANGELES, City.LAS_VEGAS, false, null));
        lines.add(new Route(Arrays.asList(new Line(1080, 810, 1125, 785), new Line(1125, 785, 1180, 768),
                new Line(1180, 768, 1240, 767), new Line(1240, 767, 1290, 797), new Line(1290, 797, 1340, 840),
                new Line(1340, 840, 1365, 870)), Color.RED, 6, City.NEW_ORLEANS, City.MIAMI, false, null));
        lines.add(new Route(Arrays.asList(new Line(1065, 800, 1085, 750), new Line(1085, 750, 1120, 700),
                new Line(1120, 700, 1160, 653), new Line(1160, 653, 1190, 628)), Color.ORANGE, 4, City.NEW_ORLEANS,
                City.ATLANTA, false, null));
        lines.add(new Route(Arrays.asList(new Line(1050, 790, 1070, 740), new Line(1070, 740, 1095, 695),
                new Line(1095, 695,1145, 640), new Line(1145, 640, 1175, 615)), Color.YELLOW, 4, City.NEW_ORLEANS,
                City.ATLANTA, false, null));
        lines.add(new Route(Arrays.asList(new Line(1390, 840, 1365, 790), new Line(1365, 790, 1350, 740),
                new Line(1350, 740, 1340, 690), new Line(1340, 690, 1340, 635)), Color.PURPLE, 4, City.CHARLESTON,
                City.MIAMI, false, null));
        lines.add(new Route(Arrays.asList(new Line(1340, 610, 1365, 565), new Line(1355, 570, 1305, 535)), Color.GREY, 2,
                City.CHARLESTON, City.RALEIGH, false, null));
        lines.add(new Route(Arrays.asList(new Line(1280, 510, 1230, 500), new Line(1230, 500, 1175, 515),
                new Line(1175, 515, 1125, 545)), Color.BLACK, 3, City.RALEIGH, City.NASHVILLE, false, null));
        lines.add(new Route(Arrays.asList(new Line(1100, 545, 1125, 495), new Line(1125, 495, 1160, 455),
                new Line(1160, 455, 1210, 420), new Line(1210, 420, 1240, 380)), Color.YELLOW, 4, City.NASHVILLE,
                City.PITTSBURGH, false, null));
        lines.add(new Route(Arrays.asList(new Line(1110, 570, 1070, 605), new Line(1070, 605, 1020, 625),
                new Line(1020, 625, 965, 630)), Color.WHITE, 3, City.LITTLE_ROCK, City.NASHVILLE, false, null));
    }
}