package com.tickettoride.activities;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.tickettoride.R;
import com.tickettoride.models.City;
import com.tickettoride.models.DestinationCard;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DestinationCardFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DestinationCardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DestinationCardFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM = "argument_parcel";

    private DestinationCard card1;
    private DestinationCard card2;
    private DestinationCard card3;
    private int requiredSelectedCards;

    private OnFragmentInteractionListener mListener;

    public DestinationCardFragment() {
        // Required empty public constructor
    }

    private TextView promptString;

    private ConstraintLayout card1Layout;
    private TextView card1City1Text;
    private TextView card1City2Text;
    private TextView card1PointsText;
    private ImageView card1SelectImageView;
    private CardOnClick card1OnClick;

    private ConstraintLayout card2Layout;
    private TextView card2City1Text;
    private TextView card2City2Text;
    private TextView card2PointsText;
    private ImageView card2SelectImageView;
    private CardOnClick card2OnClick;

    private ConstraintLayout card3Layout;
    private TextView card3City1Text;
    private TextView card3City2Text;
    private TextView card3PointsText;
    private ImageView card3SelectImageView;
    private CardOnClick card3OnClick;

    private Button finishButton;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param card1 A card offered to a player
     * @param card2 A card offered to a player
     * @param card3 A card offered to a player
     * @param numRequiredCards The number of card the player is required to keep
     * @return A new instance of fragment DestinationCardFragment.
     */
    public static DestinationCardFragment newInstance(DestinationCard card1, DestinationCard card2, DestinationCard card3, int numRequiredCards) {
        DestinationCardFragment fragment = new DestinationCardFragment();
        Bundle args = new Bundle();
        CreationArgs creationArgs = new CreationArgs(card1, card2, card3, numRequiredCards);
        args.putParcelable(ARG_PARAM, creationArgs);
        fragment.setArguments(args);
        return fragment;
    }

    private static class CreationArgs implements Parcelable {

        private DestinationCard card1;
        private DestinationCard card2;
        private DestinationCard card3;
        private int requiredSelectedCards;

        public CreationArgs(DestinationCard card1, DestinationCard card2, DestinationCard card3,
                            int requiredSelectedCards) {
            this.card1 = card1;
            this.card2 = card2;
            this.card3 = card3;
            this.requiredSelectedCards = requiredSelectedCards;
        }

        protected CreationArgs(Parcel in) {
            this.card1 = unparcelCard(in);
            this.card2 = unparcelCard(in);
            this.card2 = unparcelCard(in);
        }

        /**
         * Flatten this object in to a Parcel.
         *
         * @param dest  The Parcel in which the object should be written.
         * @param flags Additional flags about how the object should be written.
         *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
         */
        @Override
        public void writeToParcel(Parcel dest, int flags) {
            parcelCard(card1, dest);
            parcelCard(card2, dest);
            parcelCard(card3, dest);

            dest.writeInt(requiredSelectedCards);
        }

        public static final Creator<CreationArgs> CREATOR = new Creator<CreationArgs>() {
            @Override
            public CreationArgs createFromParcel(Parcel in) {
                return new CreationArgs(in);
            }

            @Override
            public CreationArgs[] newArray(int size) {
                return new CreationArgs[size];
            }
        };

        private static DestinationCard unparcelCard(Parcel in){
            City city1 = City.valueOf(in.readString());
            City city2 = City.valueOf(in.readString());
            DestinationCard.Value pointValue = DestinationCard.Value.valueOf(in.readString());
            return new DestinationCard(city1,city2,pointValue);
        }

        private static void parcelCard(DestinationCard card, Parcel dest){
            dest.writeString(card.getDestination1().name());
            dest.writeString(card.getDestination2().name());
            dest.writeString(card.getPointValue().name());

        }

        /**
         * Describe the kinds of special objects contained in this Parcelable
         * instance's marshaled representation. For example, if the object will
         * include a file descriptor in the output of {@link #writeToParcel(Parcel, int)},
         * the return value of this method must include the
         * {@link #CONTENTS_FILE_DESCRIPTOR} bit.
         *
         * @return a bitmask indicating the set of special object types marshaled
         * by this Parcelable object instance.
         */
        @Override
        public int describeContents() {
            return 0;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            CreationArgs args = getArguments().getParcelable(ARG_PARAM);
            card1 = args.card1;
            card2 = args.card2;
            card3 = args.card3;

            requiredSelectedCards = args.requiredSelectedCards;
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_destination_card, container, false);

        promptString = (TextView) v.findViewById(R.id.destination_card_prompt_string);

        card1Layout = (ConstraintLayout) v.findViewById(R.id.destination_card_1);
        card1City1Text = (TextView) v.findViewById(R.id.card_1_city_1);
        card1City2Text = (TextView) v.findViewById(R.id.card_1_city_2);
        card1PointsText = (TextView) v.findViewById(R.id.card_1_points);
        card1SelectImageView = (ImageView) v.findViewById(R.id.card_1_image);

        card2Layout = (ConstraintLayout) v.findViewById(R.id.destination_card_2);
        card2City1Text = (TextView) v.findViewById(R.id.card_2_city_1);
        card2City2Text = (TextView) v.findViewById(R.id.card_2_city_2);
        card2PointsText = (TextView) v.findViewById(R.id.card_2_points);
        card2SelectImageView = (ImageView) v.findViewById(R.id.card_2_image);

        card3Layout = (ConstraintLayout) v.findViewById(R.id.destination_card_3);
        card3City1Text = (TextView) v.findViewById(R.id.card_3_city_1);
        card3City2Text = (TextView) v.findViewById(R.id.card_3_city_2);
        card3PointsText = (TextView) v.findViewById(R.id.card_3_points);
        card3SelectImageView = (ImageView) v.findViewById(R.id.card_3_image);

        finishButton = (Button) v.findViewById(R.id.destination_card_done_button);

        promptString.setText(getString(R.string.dest_draw_hint, requiredSelectedCards));

        card1OnClick = initCardView(card1,
                card1City1Text, card1City2Text, card1PointsText, card1SelectImageView, card1Layout);
        card2OnClick = initCardView(card2,
                card2City1Text, card2City2Text, card2PointsText, card2SelectImageView, card2Layout);
        card3OnClick = initCardView(card3,
                card3City1Text, card3City2Text, card3PointsText, card3SelectImageView, card3Layout);

        finishButton.setEnabled(false);

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<DestinationCard> acceptedCards = new ArrayList<>();
                if (card1OnClick.isSelected())
                    acceptedCards.add(card1);
                if (card2OnClick.isSelected())
                    acceptedCards.add(card2);
                if (card3OnClick.isSelected())
                    acceptedCards.add(card3);

                mListener.onAcceptCards(acceptedCards);
            }
        });

        return v;
    }

    private CardOnClick initCardView(DestinationCard card,
                              TextView city1Text, TextView city2Text, TextView pointsText,
                              ImageView selectImageView, ConstraintLayout cardLayout) {
        city1Text.setText(getCityNameResource(card.getDestination1()));
        city2Text.setText(getCityNameResource(card.getDestination2()));
        pointsText.setText(String.format("%d", card.getPointValue().asInt()));
        selectImageView.setImageDrawable(getResources().getDrawable(R.drawable.x_icon, null));
        CardOnClick onClick = new CardOnClick(selectImageView);
        cardLayout.setOnClickListener(onClick);
        return onClick;
    }

    private class CardOnClick implements View.OnClickListener{
        private Drawable xIcon = getResources().getDrawable(R.drawable.x_icon, null);
        private Drawable checkIcon = getResources().getDrawable(R.drawable.check_icon, null);
        private ImageView selectImageView;
        private boolean selected = false;

        CardOnClick(ImageView selectImage){
            this.selectImageView = selectImage;
        }

        @Override
        public void onClick(View v) {
            this.selected = !this.selected;
            if (this.selected)
                this.selectImageView.setImageDrawable(checkIcon);
            else
                this.selectImageView.setImageDrawable(xIcon);

            updateFinishButton();
        }

        public boolean isSelected(){
            return this.selected;
        }
    }

    private void updateFinishButton(){
        int selectedCards = 0;
        if (card1OnClick.isSelected())
            selectedCards++;
        if (card2OnClick.isSelected())
            selectedCards++;
        if(card3OnClick.isSelected())
            selectedCards++;

        finishButton.setEnabled(selectedCards >= requiredSelectedCards);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {

        void onAcceptCards(ArrayList<DestinationCard> destinationCards);
    }

    private String getCityNameResource(City city){
        int id;
        switch (city){
            case VANCOUVER:         id = R.string.vancouver_name;       break;
            case CALGARY:           id = R.string.calgary_name;         break;
            case WINNIPEG:          id = R.string.winnipeg_name;        break;
            case SAUL_ST_MARIE:     id = R.string.saul_st_marie_name;   break;
            case MONTREAL:          id = R.string.montreal_name;        break;
            case SEATTLE:           id = R.string.seattle_name;         break;
            case HELENA:            id = R.string.helena_name;          break;
            case DULUTH:            id = R.string.duluth_name;          break;
            case TORONTO:           id = R.string.toronto_name;         break;
            case BOSTON:            id = R.string.boston_name;          break;
            case PORTLAND:          id = R.string.portland_name;        break;
            case SALT_LAKE_CITY:    id = R.string.salt_lake_city_name;  break;
            case SAN_FRANCISCO:     id = R.string.san_francisco_name;   break;
            case LOS_ANGELES:       id = R.string.los_angeles_name;     break;
            case PHOENIX:           id = R.string.phoenix_name;         break;
            case LAS_VEGAS:         id = R.string.las_vegas_name;       break;
            case SANTA_FE:          id = R.string.santa_fe_name;        break;
            case EL_PASO:           id = R.string.el_paso_name;         break;
            case DALLAS:            id = R.string.dallas_name;          break;
            case HOUSTON:           id = R.string.houston_name;         break;
            case NEW_ORLEANS:       id = R.string.new_orleans_name;     break;
            case MIAMI:             id = R.string.miami_name;           break;
            case CHARLESTON:        id = R.string.charleston_name;      break;
            case ATLANTA:           id = R.string.atlanta_name;         break;
            case LITTLE_ROCK:       id = R.string.little_rock_name;     break;
            case OKLAHOMA_CITY:     id = R.string.oklahoma_city_name;   break;
            case DENVER:            id = R.string.denver_name;          break;
            case OMAHA:             id = R.string.omaha_name;           break;
            case CHICAGO:           id = R.string.chicago_name;         break;
            case PITTSBURGH:        id = R.string.pittsburgh_name;      break;
            case SAINT_LOUIS:       id = R.string.saint_louis_name;     break;
            case KANSAS_CITY:       id = R.string.kansas_city_name;     break;
            case NASHVILLE:         id = R.string.nashville_name;       break;
            case RALEIGH:           id = R.string.raleigh_name;         break;
            case WASHINGTON:        id = R.string.washington_name;      break;
            case NEW_YORK:          id = R.string.new_york_name;        break;
            default:                id = R.string.invalid_city_name;    break;
        }
        return getString(id);
    }
}
