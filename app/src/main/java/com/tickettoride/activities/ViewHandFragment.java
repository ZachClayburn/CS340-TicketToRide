package com.tickettoride.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tickettoride.R;
import com.tickettoride.clientModels.DataManager;
import com.tickettoride.clientModels.Player;
import com.tickettoride.models.DestinationCard;

import java.util.ArrayList;
import java.util.UUID;

public class ViewHandFragment extends Fragment {

    private TextView blueCards;
    private TextView greenCards;
    private TextView purpleCards;
    private TextView redCards;
    private TextView yellowCards;
    private TextView orangeCards;
    private TextView blackCards;
    private TextView whiteCards;
    private TextView wildCards;
    private Player player;
    private ArrayList<DestinationCard> playerDestCards;
    private RecyclerView destCards;
    private Adapter adapter;
    private OnReturnToMapListener fragmentListener;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        UUID playerID = UUID.fromString(args.getString("player", null));
        this.player = DataManager.SINGLETON.getPlayer();
        fragmentListener = (OnReturnToMapListener) getActivity();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.view_hand, container, false);
        blueCards = v.findViewById(R.id.blue);
        blueCards.setText("Blue: " + player.getPlayerHand().getBlue());
        greenCards = v.findViewById(R.id.green);
        greenCards.setText("Green: " + player.getPlayerHand().getGreen());
        purpleCards = v.findViewById(R.id.purple);
        purpleCards.setText("Purple: " + player.getPlayerHand().getPurple());
        redCards = v.findViewById(R.id.red);
        redCards.setText("Red: " + player.getPlayerHand().getRed());
        orangeCards = v.findViewById(R.id.orange);
        orangeCards.setText("Orange: " + player.getPlayerHand().getOrange());
        yellowCards = v.findViewById(R.id.yellow);
        yellowCards.setText("Yellow: " + player.getPlayerHand().getYellow());
        blackCards = v.findViewById(R.id.black);
        blackCards.setText("Black: " + player.getPlayerHand().getBlack());
        whiteCards = v.findViewById(R.id.white);
        whiteCards.setText("White: " + player.getPlayerHand().getWhite());
        wildCards = v.findViewById(R.id.wild);
        wildCards.setText("Locomotive: " + player.getPlayerHand().getLocomotive());
        playerDestCards = player.getDestCards();
        destCards = v.findViewById(R.id.dest_card_list);
        destCards.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new Adapter(getContext(), playerDestCards);
        destCards.setAdapter(adapter);
        return v;
    }

    class Adapter extends RecyclerView.Adapter<Holder> {

        private ArrayList<DestinationCard> cards;
        private LayoutInflater inflater;

        public Adapter(Context context, ArrayList<DestinationCard> cards) {
            this.cards = cards;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.dest_recycler, parent, false);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            DestinationCard dest = cards.get(position);
            holder.bind(dest);
        }

        @Override
        public int getItemCount() {
            return cards.size();
        }

    }

    class Holder extends RecyclerView.ViewHolder {
        private TextView startCity;
        private TextView endCity;
        private TextView points;
        DestinationCard card;

        public Holder(View view) {
            super(view);
            startCity = view.findViewById(R.id.start);
            endCity = view.findViewById(R.id.end);
            points = view.findViewById(R.id.points);
        }

        void bind(DestinationCard card) {
            this.card = card;
            startCity.setText(card.getDestination1().toString());
            endCity.setText(card.getDestination2().toString());
            points.setText(card.getPointValue().toString());
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                fragmentListener.onReturnToMap();
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
