package com.tickettoride.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LobbyActivity extends AppCompatActivity{
    private GameInfo gameInfo; //needs game info from database
    private TextView gameName;
    private TextView gameID;
    private TextView numPlayers;
    private Button startGame;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lobby);
        Intent i = getIntent();
        gameName = (TextView) findViewById(R.id.group_name);
        gameID = (TextView) findViewById(R.id.game_id);
        numPlayers = (TextView) findViewById(R.id.num_player);
        startGame = (Button) findViewById(R.id.start_game);
        gameName.setText("Group Name: " + gameInfo.getGroupName());
        gameID.setText("Game ID: " + gameInfo.getGameID());
        numPlayers.setText("Number of Players: " +gameInfo.getPersonList().size() + "/" + gameInfo.getNumPlayer());
        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LobbyActivity.this, GameRoom.class);
                startActivity(intent);
            }
        });
    }

}
