package com.tickettoride.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import com.tickettoride.R;
import com.tickettoride.models.DestinationCard;

import java.util.ArrayList;
import java.util.Queue;

public class TestingActivity extends AppCompatActivity implements DestinationCardFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);

        Queue<DestinationCard> deck = DestinationCard.getShuffledDeck();

        FragmentManager fm = getSupportFragmentManager();

        Fragment fragment = DestinationCardFragment.newInstance(deck.poll(), deck.poll(), deck.poll(), 2);
        fm.beginTransaction()
                .replace(R.id.fragment_frame, fragment)
                .commit();
    }

    @Override
    public void onAcceptCards(ArrayList<DestinationCard> destinationCards) {
        Toast.makeText(this, destinationCards.toString(), Toast.LENGTH_LONG).show();
    }
}
