package com.tickettoride.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tickettoride.R;
import com.tickettoride.clientModels.Game;


import com.tickettoride.facadeProxies.GameFacadeProxy;


public class CreateGameActivity extends MyBaseActivity {
    private Game info = new Game();
    private EditText groupName;
    private Button two;
    private Button three;
    private Button four;
    private Button five;
    private Button createGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_game);
        groupName = (EditText) findViewById(R.id.group_name);
        two = (Button) findViewById(R.id.two_player);
        three = (Button) findViewById(R.id.three_player);
        four = (Button) findViewById(R.id.four_player);
        five = (Button) findViewById(R.id.five_player);
        createGame = (Button) findViewById(R.id.create_group);
        createGame.setEnabled(false);
        groupName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                info.setGroupName(s.toString());
                setEnabled();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                info.setMaxPlayer(2);
                setEnabled();
            }
        });
        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                info.setMaxPlayer(3);
                setEnabled();
            }
        });
        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                info.setMaxPlayer(4);
                setEnabled();
            }
        });
        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                info.setMaxPlayer(5);
                setEnabled();
            }
        });


        createGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    GameFacadeProxy.SINGLETON.create(info.getGroupName(), info.getMaxPlayer());
                } catch (Throwable t) { }

            }
        });

    }
    public void moveToLobbyCreate(Game game) {
        Intent intent = new Intent(CreateGameActivity.this, LobbyActivity.class);
        intent.putExtra("game", game);
        startActivity(intent);
    }
    public void setEnabled() {
        //if username and password fields have characters, login and register buttons are enabled
        if (!info.getGroupName().equals("") && info.getNumPlayer() != 0) {
            createGame.setEnabled(true);
        }
        else {
            createGame.setEnabled(false);
        }
    }

    public void createError() {
        Toast.makeText(this, R.string.create_game_error, Toast.LENGTH_SHORT).show();
    }
}
