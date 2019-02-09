package com.tickettoride.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.tickettoride.R;
import com.tickettoride.clientModels.GameIndex;
import com.tickettoride.clientModels.GameInfo;
import com.tickettoride.clientModels.User;
import com.tickettoride.facadeProxies.GameFacadeProxy;

import java.util.ArrayList;


public class JoinGameActivity extends AppCompatActivity {
    private RecyclerView gameList;
    private Button createGame;
    private Adapter adapter;
    private ArrayList<GameInfo> games = GameIndex.SINGLETON.getGameIndex();

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
        private ArrayList<GameInfo> listOfGames;
        private LayoutInflater inflater;
        public Adapter(Context context, ArrayList<GameInfo> listOfGames) {
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
            GameInfo game = listOfGames.get(position);
            holder.bind(game);
        }
        @Override
        public int getItemCount() {
            return listOfGames.size();
        }
    }
    public class Holder extends RecyclerView.ViewHolder {
        TextView gameName;
        GameInfo game;
        public Holder(View view) {
           super(view);
           gameName = (TextView) view.findViewById(R.id.game_list);
        }
        public void bind(final GameInfo game) {
            this.game = game;
            gameName.setText(game.getGroupName());
            gameName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    GameFacadeProxy.SINGLETON.joinGame(User.SINGLETON.getUserID(), game.getGameID());
                    Intent intent = new Intent(JoinGameActivity.this, LobbyActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("gameID", game.getGameID());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }
    }
}
