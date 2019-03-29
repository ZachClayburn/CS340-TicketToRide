package com.tickettoride.activities;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.Toast;

import com.tickettoride.R;
import com.tickettoride.clientModels.DataManager;
import com.tickettoride.clientModels.InitializeGameState;
import com.tickettoride.facadeProxies.DestinationCardFacadeProxy;
import com.tickettoride.models.DestinationCard;
import com.tickettoride.models.idtypes.PlayerID;

import java.util.ArrayList;
import java.util.List;

public class GameRoomActivity extends MyBaseActivity implements
        OnReturnToMapListener, DestinationCardFragment.OnFragmentInteractionListener, ViewHandListener, PlayerFragmentListener,
        DiscardFragmentListener, GameOverFragmentListener, HistoryFragmentListener{
    private Context context;
    private PlayerFragment playerFragment;
    private ViewHandFragment viewHandFragment;
    private DestinationCardFragment destinationCardFragment;
    private FragmentManager fm;
    private MapFragment mapFragment;
    private GameOverFragment gameOverFragment;
    private DiscardFragment discardFragment;
    private HistoryFragment historyFragment;

    public MapFragment getMapFragment() { return mapFragment; }

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
        fm.beginTransaction().replace(R.id.fragment_holder, mapFragment).commit();
        playerFragment = null;
        viewHandFragment = null;
        destinationCardFragment = null;
        discardFragment = null;
        historyFragment = null;
    }

    public void toPlayerFragment(int playerTurn){
        if (playerFragment == null) {
            playerFragment = new PlayerFragment();
            Bundle arguments = new Bundle();
            arguments.putInt("player", playerTurn);
            playerFragment.setArguments(arguments);
            fm.beginTransaction().replace(R.id.fragment_holder, playerFragment).commit();
        }
    }
    public void toGameOverFragment() {
        if (gameOverFragment == null) {
            gameOverFragment = new GameOverFragment();
            fm.beginTransaction().replace(R.id.fragment_holder, gameOverFragment).commit();
        }
    }
    public void toViewHandFragment(){
        if (viewHandFragment == null) {
            viewHandFragment = new ViewHandFragment();
            Bundle arguments = new Bundle();
            viewHandFragment.setArguments(arguments);
            fm.beginTransaction().replace(R.id.fragment_holder, viewHandFragment).commit();
        }
    }
    public void moveToHistory() {
        if (historyFragment == null) {
            historyFragment = new HistoryFragment();
            fm.beginTransaction().replace(R.id.fragment_holder, historyFragment).commit();
        }
    }
    public void moveToDiscard() {
        if (mapFragment != null) {
            discardFragment = new DiscardFragment();
            fm.beginTransaction().replace(R.id.fragment_holder, discardFragment).commit();
        }
    }
    public void moveToGameOver() {
        if (gameOverFragment == null) {
            gameOverFragment = new GameOverFragment();
            fm.beginTransaction().replace(R.id.fragment_holder, gameOverFragment).commit();
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

    public Runnable updateChatRunnable = new Runnable() {
        @Override
        public void run() {
            mapFragment.updateChat();
        }
    };

    public void updateChat() {
        runOnUiThread(updateChatRunnable);
    }

    public Runnable updateHistoryRunnable = new Runnable() {
        @Override
        public void run() {
            if(historyFragment!=null){
                historyFragment.updateHistory();
            }
        }
    };

    public void updateHistory() {
        runOnUiThread(updateHistoryRunnable);
    }

    public void updateCards(){
        runOnUiThread(updateCardsRunnable);
    }

    public Runnable updateCardsRunnable = new Runnable() {
        @Override
        public void run() {
            if (mapFragment != null) {
                mapFragment.setAllColors();
                mapFragment.updateDeckNumbers();
            }
        }
    };

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
    }

    public void activateTurn() {
        int currentTurn = DataManager.getSINGLETON().getTurn();
        if (currentTurn == DataManager.getSINGLETON().getPlayer().getTurn()) {
            DataManager.getSINGLETON().getPlayerState().moveToPlayerTurnState(mapFragment);
        } else { DataManager.getSINGLETON().getPlayerState().moveToNotTurnState(mapFragment); }
    }
    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
    }
    public void addMessageError(){
        //figure out what message to display
    }

    public void setChatError(){
        //figure out what message to display
    }
}
