package com.tickettoride.activities;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.tickettoride.R;
import com.tickettoride.clientModels.DataManager;
import com.tickettoride.facadeProxies.GameFacadeProxy;

import java.util.UUID;

public class GameRoomActivity extends MyBaseActivity implements OnReturnToMapListener {
    private Context context;
    private PlayerFragment playerFragment;
    private FragmentManager fm;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_room);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        fm = this.getSupportFragmentManager();
        playerFragment = (PlayerFragment)fm.findFragmentById(R.id.player_layout);

        this.context = this;
        GameFacadeProxy.SINGLETON.setup(DataManager.getSINGLETON().getGame());
        DataManager.SINGLETON.setTrainCardDeck();
        Toast.makeText(this, R.string.game_welcome, Toast.LENGTH_SHORT).show();
    }

    public void setupError() {
        runOnUiThread(setupError);
    }

    public Runnable setupError = new Runnable() {
        @Override
        public void run() {
            Toast.makeText(context,R.string.setup_game_error, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onReturnToMap() {
        // The login fragment is removed if it exists (if we log in from the login fragment)
        if(playerFragment != null) {
            getSupportFragmentManager().beginTransaction().remove(playerFragment).commit();
        }
    }

    public void toPlayerFragment(UUID playerID){
        if (playerFragment == null) {
            playerFragment = new PlayerFragment();
            Bundle arguments = new Bundle();
            arguments.putString("player", playerID.toString());
            playerFragment.setArguments(arguments);
            fm.beginTransaction()
                    .add(R.id.player_layout, playerFragment)
                    .commit();
        }
    }
}
