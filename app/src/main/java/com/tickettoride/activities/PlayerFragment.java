package com.tickettoride.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.tickettoride.R;
import com.tickettoride.clientModels.DataManager;
import com.tickettoride.models.Player;

import java.util.List;
import java.util.UUID;

public class PlayerFragment extends Fragment {
    private TextView usernameText;
    private TextView trainCardsText;
    private TextView destCardsText;
    private TextView carsText;
    private TextView turnText;
    private TextView pointsText;
    private Button returnToGameButton;
    private Player player;
    private List<Player> players;
    private OnReturnToMapListener fragmentListener;

    public PlayerFragment(){}

    // Creates the fragment, sets fragmentListener to the MainActivity
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        players = DataManager.getSINGLETON().getGamePlayers();
        this.player = DataManager.SINGLETON.getPlayer();
        fragmentListener = (OnReturnToMapListener) getActivity();
    }

    // Sets the initial view for the fragment and sets up the clickListeners and editTexts
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.player_fragment, container, false);

        usernameText = (TextView) v.findViewById(R.id.player_username);
        trainCardsText = (TextView) v.findViewById(R.id.player_train_cards);
        destCardsText = (TextView) v.findViewById(R.id.player_destination_cards);
        carsText = (TextView) v.findViewById(R.id.player_cars_left);
        turnText = (TextView) v.findViewById(R.id.player_turn);
        pointsText = (TextView) v.findViewById(R.id.player_points);
        returnToGameButton = (Button)v.findViewById(R.id.return_to_game);

        usernameText.setText(getResources().getString(R.string.player_label, player.getUsername()));
        trainCardsText.setText(getResources().getString(R.string.train_card_label, player.getTrainCardCount()));
        destCardsText.setText(getResources().getString(R.string.destination_card_label, player.getDestinationCardCount()));
        carsText.setText(getResources().getString(R.string.cars_label, player.getTrainCarCount()));
        turnText.setText(getResources().getString(R.string.turn_label, player.getTurn()));
        pointsText.setText(getResources().getString(R.string.points_label, player.getPoints()));

        setBackgroundColor(player, v);

        returnToGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentListener.onReturnToMap();
            }
        });

        return v;
    }

    public void setBackgroundColor(Player player, View v){
        switch(player.getColor()){
            case BLACK:
                v.setBackgroundColor(getResources().getColor(R.color.blackPlayer, getActivity().getTheme()));
                break;
            case RED:
                v.setBackgroundColor(getResources().getColor(R.color.redPlayer, getActivity().getTheme()));
                break;
            case BLUE:
                v.setBackgroundColor(getResources().getColor(R.color.bluePlayer, getActivity().getTheme()));
                break;
            case GREEN:
                v.setBackgroundColor(getResources().getColor(R.color.greenPlayer, getActivity().getTheme()));
                break;
            case YELLOW:
                v.setBackgroundColor(getResources().getColor(R.color.yellowPlayer, getActivity().getTheme()));
                break;
        }
    }

}
