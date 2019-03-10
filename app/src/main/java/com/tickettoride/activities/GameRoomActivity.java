package com.tickettoride.activities;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;
import android.widget.Toast;

import com.tickettoride.R;
import com.tickettoride.clientModels.DataManager;
import com.tickettoride.facadeProxies.DestinationCardFacadeProxy;
import com.tickettoride.models.DestinationCard;
import com.tickettoride.models.Game;

import java.util.ArrayList;
import java.util.UUID;

public class GameRoomActivity extends MyBaseActivity implements
        OnReturnToMapListener, DestinationCardFragment.OnFragmentInteractionListener, ViewHandListener{
    private Context context;
    private PlayerFragment playerFragment;
    private ViewHandFragment viewHandFragment;
    private DestinationCardFragment destinationCardFragment;
    private FragmentManager fm;
    private MapFragment mapFragment;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_room);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        fm = this.getSupportFragmentManager();
        mapFragment = (MapFragment) fm.findFragmentById(R.id.fragment_holder);
        if (mapFragment == null) {
            mapFragment = new MapFragment();
            fm.beginTransaction().add(R.id.fragment_holder, mapFragment).commit();
        }
        playerFragment = (PlayerFragment) fm.findFragmentById(R.id.player_layout);
        this.context = this;
        Game game = DataManager.getSINGLETON().getGame();
        DataManager.SINGLETON.setTrainCardDeck();
        Toast.makeText(this, R.string.game_welcome, Toast.LENGTH_SHORT).show();
    }

    public void setupError() {
        runOnUiThread(setupError);
    }

    public Runnable setupError = new Runnable() {
        @Override
        public void run() {
            Toast.makeText(context, R.string.setup_game_error, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onReturnToMap() {
        // The login fragment is removed if it exists (if we log in from the login fragment)
        if(playerFragment != null) {
            getSupportFragmentManager().beginTransaction().remove(playerFragment).commit();
        }
        else if (viewHandFragment != null) {
            //getSupportFragmentManager().beginTransaction().remove(viewHandFragment).commit();
            fm.beginTransaction().replace(R.id.map_fragment, mapFragment).commit();
            viewHandFragment = null;
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
    public void toViewHandFragment(){
        if (viewHandFragment == null) {
            viewHandFragment = new ViewHandFragment();
            Bundle arguments = new Bundle();
            //arguments.p("player", player);
            viewHandFragment.setArguments(arguments);
            fm.beginTransaction()
                    .replace(R.id.map_fragment, viewHandFragment)
                    .commit();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        System.out.print("");
        switch(item.getItemId()) {
            case android.R.id.home:
                onReturnToMap();
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void toDestinationCardFragment(DestinationCard card1, DestinationCard card2, DestinationCard card3,
                                          int requiredToKeep) {

        if (destinationCardFragment == null)
            destinationCardFragment = DestinationCardFragment.newInstance(card1, card2, card3, requiredToKeep);

        fm.beginTransaction()
                .replace(R.id.fragment_holder, destinationCardFragment)
//                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onAcceptCards(ArrayList<DestinationCard> destinationCards) {
        DestinationCardFacadeProxy.acceptDestinationCards(DataManager.getSINGLETON().getPlayer(), destinationCards);
    }
}
