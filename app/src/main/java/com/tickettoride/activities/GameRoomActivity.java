package com.tickettoride.activities;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Toast;

import com.tickettoride.R;
import com.tickettoride.clientModels.DataManager;
import com.tickettoride.facadeProxies.DestinationCardFacadeProxy;
import com.tickettoride.models.DestinationCard;
import com.tickettoride.models.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GameRoomActivity extends MyBaseActivity implements
        OnReturnToMapListener, DestinationCardFragment.OnFragmentInteractionListener, ViewHandListener, PlayerFragmentListener, ClaimRouteListener{
    private Context context;
    private PlayerFragment playerFragment;
    private ViewHandFragment viewHandFragment;
    private DestinationCardFragment destinationCardFragment;
    private FragmentManager fm;
    private MapFragment mapFragment;
    private ClaimRouteFragment claimRouteFragment;

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
            fm.beginTransaction().replace(R.id.map_fragment, mapFragment).commit();
            playerFragment = null;
        }
        else if (viewHandFragment != null) {
            //getSupportFragmentManager().beginTransaction().remove(viewHandFragment).commit();
            fm.beginTransaction().replace(R.id.fragment_holder, mapFragment).commit();
            viewHandFragment = null;
        } else if (destinationCardFragment != null) {
            fm.beginTransaction().replace(R.id.fragment_holder, mapFragment).commit();
            destinationCardFragment = null;
        }
        else if (claimRouteFragment != null) {
            fm.beginTransaction().replace(R.id.map_fragment, mapFragment).commit();
            claimRouteFragment = null;
        }
    }

    public void toPlayerFragment(UUID playerID){
        if (playerFragment == null) {
            playerFragment = new PlayerFragment();
            Bundle arguments = new Bundle();
            arguments.putString("player", playerID.toString());
            playerFragment.setArguments(arguments);
            fm.beginTransaction()
                    .replace(R.id.map_fragment, playerFragment)
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
    public void moveToClaimRoute() {
        if (claimRouteFragment == null) {
            claimRouteFragment = new ClaimRouteFragment();
            fm.beginTransaction().replace(R.id.map_fragment, claimRouteFragment).commit();
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

    public void toDestinationCardFragment() {

        List<DestinationCard> offeredCards = DataManager.getSINGLETON().getOfferedCards();
        Integer requiredToKeep = DataManager.getSINGLETON().getDestCardsRequiredToKeep();

        if (destinationCardFragment == null)
            destinationCardFragment = DestinationCardFragment
                    .newInstance(offeredCards.get(0), offeredCards.get(1), offeredCards.get(2), requiredToKeep);

        fm.beginTransaction()
                .replace(R.id.fragment_holder, destinationCardFragment)
                .addToBackStack(null)
                //FIXME addToBackStack This should let you look back at the map by hitting the back button,
                // but it isn't working for some reason, figure that out later
                .commit();
    }

    @Override
    public void onAcceptCards(ArrayList<DestinationCard> destinationCards) {
        DestinationCardFacadeProxy.acceptDestinationCards(DataManager.getSINGLETON().getPlayer(), destinationCards);
        onReturnToMap();
        DataManager.getSINGLETON().getPlayerState().moveToPlayerTurnState(mapFragment);
        //FIXME This should eventually check if it is the players turn or not and move them to the appropriate state

    }
}
