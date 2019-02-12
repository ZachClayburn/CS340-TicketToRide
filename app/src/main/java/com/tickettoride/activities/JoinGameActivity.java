package com.tickettoride.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tickettoride.R;
import com.tickettoride.clientModels.DataManager;
import com.tickettoride.clientModels.GameIndex;
import com.tickettoride.clientModels.Game;
import com.tickettoride.facadeProxies.GameFacadeProxy;
import com.tickettoride.facadeProxies.SessionFacadeProxy;

import java.util.ArrayList;

public class JoinGameActivity extends MyBaseActivity {
    private RecyclerView gameList;
    private Button createGame;
    private Adapter adapter;
    private ArrayList<Game> games = GameIndex.SINGLETON.getGameIndex();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_game);
        gameList = (RecyclerView) findViewById(R.id.recycler_view);
        gameList.setLayoutManager(new LinearLayoutManager(this));
        createGame = (Button) findViewById(R.id.create_game);
        createGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(JoinGameActivity.this, CreateGameActivity.class);
                startActivity(intent);
            }
        });
        adapter = new Adapter(this, games);
        gameList.setAdapter(adapter);
    }

    public void updateUI() {
        games = GameIndex.SINGLETON.getGameIndex();
        adapter = new Adapter(this, games);
        gameList.setAdapter(adapter);
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
           gameName = (TextView) view.findViewById(R.id.game_list);
        }
        public void bind(final Game game) {
            this.game = game;
            gameName.setText(game.getGroupName());
            gameName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    GameFacadeProxy.SINGLETON.join(game.getGameID());
                }
            });
        }
        public void moveToLobbyJoin() {
            Intent intent = new Intent(JoinGameActivity.this, LobbyActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("gameID", game.getGameID().toString());
            intent.putExtras(bundle);
            startActivity(intent);
        }

    }
    public void JoinError() {
        Toast.makeText(this ,R.string.join_game_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed(){
        SessionFacadeProxy.SINGLETON.delete(DataManager.SINGLETON.getSession().getSessionId());
        Intent intent = new Intent(JoinGameActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}
