package com.tickettoride.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tickettoride.R;
import com.tickettoride.clientModels.DataManager;
import com.tickettoride.clientModels.Game;
import com.tickettoride.clientModels.GameIndex;
import com.tickettoride.facadeProxies.GameFacadeProxy;
import com.tickettoride.facadeProxies.SessionFacadeProxy;

public class LobbyActivity extends MyBaseActivity{
    private Game game;
    private GameIndex index = GameIndex.SINGLETON;
    private TextView gameName;
    private TextView gameID;
    private TextView numPlayers;
    private Button startGame;
    private Context context;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lobby);
        this.game = (Game) getIntent().getSerializableExtra("game");

        gameName = (TextView) findViewById(R.id.group_name);
        gameID = (TextView) findViewById(R.id.game_id);
        numPlayers = (TextView) findViewById(R.id.num_player);
        startGame = (Button) findViewById(R.id.start_game);
        gameName.setText("Group Name: " + game.getGroupName());
        gameID.setText("Game ID: " + game.getGameID());
        numPlayers.setText("Number of Players: " + game.getNumPlayer() + "/" + game.getMaxPlayer());
        context = this;

        setEnabled();
        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameFacadeProxy.SINGLETON.startGame(game); //FIXME Only let the player start a game if there is at least 2 players
            }
        });
    }
    public void setEnabled() {
        if (game.getNumPlayer() <= 1 || game.getNumPlayer() > 5) {
            startGame.setEnabled(false);
        }
        else {
            startGame.setEnabled(true);
        }
    }

    public Runnable updateUIRunnable = new Runnable() {
        @Override
        public void run() {
            setEnabled();
            numPlayers.setText("Number of Players: " + game.getNumPlayer() + "/" + game.getMaxPlayer());
        }
    };

    public void updateUI(Game game) {
        this.game = game;
        runOnUiThread(updateUIRunnable);
    }

    public void moveToGame() {
        Intent intent = new Intent(LobbyActivity.this, GameRoomActivity.class);
        startActivity(intent);
    }
    public void startGameError() {
        Toast.makeText(this ,R.string.start_game_error, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBackPressed(){
        leaveGame();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            leaveGame();
        }
        return super.onOptionsItemSelected(item);
    }

    public void moveToJoin() {
        Intent intent = new Intent(LobbyActivity.this, JoinGameActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void leaveGame() {
        GameFacadeProxy.SINGLETON.leave();
        moveToJoin();
    }

}
