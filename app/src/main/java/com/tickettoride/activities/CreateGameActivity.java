package com.tickettoride.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CreateGameActivity extends AppCompatActivity {
    private GameInfo info = new GameInfo();
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
                info.setNumPlayer(2);
                setEnabled();
            }
        });
        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                info.setNumPlayer(3);
                setEnabled();
            }
        });
        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                info.setNumPlayer(4);
                setEnabled();
            }
        });
        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                info.setNumPlayer(5);
                setEnabled();
            }
        });
        createGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //implement web socket call back when ready
                 /*Intent intent = new Intent(CreateGameActivity.this, LobbyActivity.class);
                 startActivity(intent);*/
            }
        });

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
}
