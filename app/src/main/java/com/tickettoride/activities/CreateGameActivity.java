package com.tickettoride.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.tickettoride.R;


import com.tickettoride.clientModels.SerializableGame;
import com.tickettoride.facadeProxies.GameFacadeProxy;
import com.tickettoride.models.Game;


public class CreateGameActivity extends MyBaseActivity {
    private EditText groupName;
    private RadioButton two;
    private RadioButton three;
    private RadioButton four;
    private RadioButton five;
    private Button createGame;
    private Context context;
    private String groupNameString = "";
    private int maxPlayerNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_game);
        groupName = (EditText) findViewById(R.id.group_name);
        two = (RadioButton) findViewById(R.id.two_player);
        three = (RadioButton) findViewById(R.id.three_player);
        four = (RadioButton) findViewById(R.id.four_player);
        five = (RadioButton) findViewById(R.id.five_player);
        createGame = (Button) findViewById(R.id.create_group);
        createGame.setEnabled(false);
        context=this;
        groupName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                groupNameString = s.toString();
                setEnabled();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                maxPlayerNum = 2;
                setEnabled();
            }
        });
        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                maxPlayerNum = 3;
                setEnabled();
            }
        });
        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                maxPlayerNum = 4;
                setEnabled();
            }
        });
        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                maxPlayerNum = 5;
                setEnabled();
            }
        });


        createGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    GameFacadeProxy.SINGLETON.create(groupNameString, maxPlayerNum);
                } catch (Throwable t) { }

            }
        });

    }
    public void moveToLobbyCreate(Game game) {
        Intent intent = new Intent(CreateGameActivity.this, LobbyActivity.class);
        intent.putExtra("game", new SerializableGame(game));
        startActivity(intent);
    }
    public void setEnabled() {
        //if username and password fields have characters, login and register buttons are enabled
        if (!groupNameString.equals("") && maxPlayerNum > 1) {
            createGame.setEnabled(true);
        }
        else {
            createGame.setEnabled(false);
        }
    }

    public void createError() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, R.string.create_game_error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
