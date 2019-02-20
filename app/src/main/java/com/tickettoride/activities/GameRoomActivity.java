package com.tickettoride.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.tickettoride.R;
import com.tickettoride.clientModels.DataManager;
import com.tickettoride.facadeProxies.GameFacadeProxy;

public class GameRoomActivity extends MyBaseActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_room);
        GameFacadeProxy.SINGLETON.setup(DataManager.getSINGLETON().getGame());
        Toast.makeText(this, R.string.game_welcome, Toast.LENGTH_SHORT).show();
    }
}
