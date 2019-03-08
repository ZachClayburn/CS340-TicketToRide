package com.tickettoride.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tickettoride.R;
import com.tickettoride.clientModels.GameIndex;
import com.tickettoride.facadeProxies.GameFacadeProxy;
import com.tickettoride.facadeProxies.SessionFacadeProxy;
import com.tickettoride.models.Game;

import java.util.ArrayList;

public class JoinGameActivity extends MyBaseActivity {
    private RecyclerView joinGameList;
    private RecyclerView rejoinGameList;
    private Button createGame;
    private Adapter joinAdapter;
    private Adapter rejoinAdapter;
    private ArrayList<Game> joinGames = GameIndex.SINGLETON.getJoinGameIndex();
    private ArrayList<Game> rejoinGames = GameIndex.SINGLETON.getRejoinGameIndex();
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_game);
        setupJoinGameList();
        setupRejoinGameList();
        setupCreateGameButton();
        this.context = this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { killSession(); }
        return super.onOptionsItemSelected(item);
    }

    public Runnable updateUIRunnable = new Runnable() {
        @Override
        public void run() {
            updateJoinGames();
            updateRejoinGames();
        }
    };

    public void updateUI() {
        runOnUiThread(updateUIRunnable);
    }

    public void moveToLobbyJoin(Game game) {
        Intent intent = new Intent(JoinGameActivity.this, LobbyActivity.class);
        startActivity(intent);
    }

    public class Adapter extends RecyclerView.Adapter<Holder> {
        private ArrayList<Game> listOfGames;
        private LayoutInflater inflater;
        public Adapter(Context context, ArrayList<Game> listOfGames) {
            this.listOfGames = listOfGames;
            inflater = LayoutInflater.from(context);
        }
        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.join_recycler_view, parent, false);
            return new Holder(view);
        }
        @Override
        public void onBindViewHolder(Holder holder, int position) {
            Game game = listOfGames.get(position);
            holder.bind(game);
        }
        @Override
        public int getItemCount() {
            return listOfGames.size();
        }

    }
    public class Holder extends RecyclerView.ViewHolder {
        TextView gameName;
        Game game;
        public Holder(View view) {
           super(view);
           gameName = view.findViewById(R.id.join_game_list);
        }
        public void bind(final Game game) {
            this.game = game;
            gameName.setText(game.getGroupName());
            gameName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) { GameFacadeProxy.SINGLETON.join(game.getGameID()); }
            });
        }
    }

    public Runnable joinError = new Runnable() {
        @Override
        public void run() { Toast.makeText(context ,R.string.join_game_error, Toast.LENGTH_SHORT).show(); }
    };

    public void JoinError() {
        runOnUiThread(joinError);
    }

    @Override
    public void onBackPressed(){
        killSession();
    }

    public void killSession(){
        Intent intent = new Intent(JoinGameActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        SessionFacadeProxy.SINGLETON.delete();
    }

    private void setupJoinGameList() {
        joinGameList = findViewById(R.id.join_recycler_view);
        joinGameList.setLayoutManager(new LinearLayoutManager(this));
        joinGames = GameIndex.SINGLETON.getJoinGameIndex();
        joinAdapter = new Adapter(this, joinGames);
        joinGameList.setAdapter(joinAdapter);
    }

    private void setupRejoinGameList() {
        rejoinGameList = findViewById(R.id.rejoin_recycler_view);
        rejoinGameList.setLayoutManager(new LinearLayoutManager(this));
        rejoinGames = GameIndex.SINGLETON.getRejoinGameIndex();
        rejoinAdapter = new Adapter(this, rejoinGames);
        rejoinGameList.setAdapter(rejoinAdapter);
    }

    private void setupCreateGameButton() {
        createGame = findViewById(R.id.create_game);
        createGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(JoinGameActivity.this, CreateGameActivity.class);
                startActivity(intent);
            }
        });
    }

    private void updateJoinGames() {
        joinGames = GameIndex.SINGLETON.getJoinGameIndex();
        joinAdapter = new Adapter(context, joinGames);
        joinGameList.setAdapter(joinAdapter);
    }

    private void updateRejoinGames() {
        rejoinGames = GameIndex.SINGLETON.getRejoinGameIndex();
        rejoinAdapter = new Adapter(context, joinGames);
        rejoinGameList.setAdapter(rejoinAdapter);
    }

}
