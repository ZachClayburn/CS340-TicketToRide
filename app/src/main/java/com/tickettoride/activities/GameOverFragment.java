package com.tickettoride.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.tickettoride.R;
import com.tickettoride.clientModels.DataManager;
import com.tickettoride.models.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameOverFragment extends Fragment {

    private RecyclerView playerList;
    private TextView winner;
    private TextView longestRoute;
    private List<Player> finalPlayers;
    private Button lobbyButton;
    private Adapter adapter;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.game_complete, container, false);
        playerList = v.findViewById(R.id.score_recycler_view);
        playerList.setLayoutManager(new LinearLayoutManager(getContext()));
        finalPlayers = DataManager.getSINGLETON().getGamePlayers();
        adapter = new Adapter(getContext(), finalPlayers);
        playerList.setAdapter(adapter);
        winner = v.findViewById(R.id.winner);
        longestRoute = v.findViewById(R.id.longest_route);
        lobbyButton = v.findViewById(R.id.return_lobby);
        winner.setText("Winner: ");//TODO set winner username here
        longestRoute.setText("Longest Route: ");//TODO set longest route holder username or none
        lobbyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO remove game from database
                getActivity().finish();
            }
        });
        return v;
    }
    class Adapter extends RecyclerView.Adapter<Holder> {
        private List<Player> players;
        private LayoutInflater inflater;

        public Adapter(Context context, List<Player> players) {
            this.players = players;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.score_recycler, parent, false);
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

    class Holder extends RecyclerView.ViewHolder {
        private TextView username;
        private TextView points;
        private TextView lostPoints;
        private TextView gainedPoints;
        private Player curPlayer;

        public Holder(View view) {
            super(view);
            username = view.findViewById(R.id.score_username);
            points = view.findViewById(R.id.score_points);
            lostPoints = view.findViewById(R.id.score_lost);
            gainedPoints = view.findViewById(R.id.score_gained);
        }

        void bind(Player curPlayer) {
            this.curPlayer = curPlayer;
            username.setText(curPlayer.getUsername());
            points.setText("Points: " + curPlayer.getPoints());
            lostPoints.setText("Lost Points: " + curPlayer.getPoints());
            gainedPoints.setText("Gained Points: " + curPlayer.getPoints());
        }
    }
}
