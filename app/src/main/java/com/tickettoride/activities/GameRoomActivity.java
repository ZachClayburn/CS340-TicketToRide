package com.tickettoride.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.tickettoride.R;

public class GameRoomActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_room);
        Toast.makeText(this, R.string.game_welcome, Toast.LENGTH_SHORT).show();
    }
}
