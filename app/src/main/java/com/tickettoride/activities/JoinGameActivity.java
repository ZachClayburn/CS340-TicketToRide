package com.tickettoride.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.tickettoride.R;


public class JoinGameActivity extends AppCompatActivity {
    private TextView gameName;
    private RecyclerView gameList;
    private Button createGame;
    private Adapter adapter;
    //private ArrayList<>

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_game);
        gameName = (TextView) findViewById(R.id.game_list);
        gameList = (RecyclerView) findViewById(R.id.recycler_view);
        createGame = (Button) findViewById(R.id.create_game);
        createGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(JoinGameActivity.this, CreateGameActivity.class);
                startActivity(intent);
            }
        });
        //adapter = new Adapter(this, );
    }

    public class Adapter extends RecyclerView.Adapter<Holder> {

        //public Adapter(Context context, "") {

        //}
        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            //return new Holder(view);
            return null;
        }
        @Override
        public void onBindViewHolder(Holder holder, int position) {

        }
        @Override
        public int getItemCount() {
            return 0;
        }
    }
    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public Holder(View view) {
           super(view);
        }
        public void bind() {

        }
        @Override
        public void onClick(View view) {

        }
    }
}
