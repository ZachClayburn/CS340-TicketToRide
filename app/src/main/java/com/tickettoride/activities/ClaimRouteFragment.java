package com.tickettoride.activities;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.tickettoride.R;
import com.tickettoride.clientModels.DataManager;
import com.tickettoride.clientModels.Line;
import com.tickettoride.clientModels.Route;
import com.tickettoride.models.City;
import com.tickettoride.models.Color;
import com.tickettoride.models.Hand;
import com.tickettoride.models.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClaimRouteFragment extends Fragment {
    private RecyclerView routeList;
    private RecyclerView.Adapter adapter;
    private ArrayList<Route> routes;
    private Player curPlayer;
    private Hand hand;
    private OnReturnToMapListener returnMap;
    private Button mapReturn;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.claim_route, container, false);
        routes = DataManager.getSINGLETON().getRoutes();
        curPlayer = DataManager.getSINGLETON().getPlayer();
        hand = DataManager.getSINGLETON().getPlayerHand();
        //filterRoutes();
        ArrayList<Route> filteredRoutes = new ArrayList<>();
        for (int i = 0; i < routes.size(); ++i) {
            switch(routes.get(i).getColor()){
                case YELLOW:
                    if (hand.getYellow() + hand.getLocomotive() >= routes.get(i).getSpaces() && !routes.get(i).getIsClaimed()) {
                        filteredRoutes.add(routes.get(i));
                    }
                    break;
                case RED:
                    if (hand.getRed() + hand.getLocomotive() >= routes.get(i).getSpaces() && !routes.get(i).getIsClaimed()){
                        filteredRoutes.add(routes.get(i));
                    }
                    break;
                case BLUE:
                    if (hand.getBlue() + hand.getLocomotive() >= routes.get(i).getSpaces() && !routes.get(i).getIsClaimed()) {
                        filteredRoutes.add(routes.get(i));
                    }
                    break;
                case PURPLE:
                    if (hand.getPurple() + hand.getLocomotive() >= routes.get(i).getSpaces() && !routes.get(i).getIsClaimed()) {
                        filteredRoutes.add(routes.get(i));
                    }
                    break;
                case BLACK:
                    if (hand.getBlack() + hand.getLocomotive() >= routes.get(i).getSpaces() && !routes.get(i).getIsClaimed()) {
                        filteredRoutes.add(routes.get(i));
                    }
                    break;
                case GREEN:
                    if (hand.getGreen() + hand.getLocomotive() >= routes.get(i).getSpaces() && !routes.get(i).getIsClaimed()) {
                        filteredRoutes.add(routes.get(i));
                    }
                    break;
                case WHITE:
                    if (hand.getWhite() + hand.getLocomotive() >= routes.get(i).getSpaces() && !routes.get(i).getIsClaimed()) {
                        filteredRoutes.add(routes.get(i));
                    }
                    break;
                case ORANGE:
                    if (hand.getOrange() + hand.getLocomotive() >= routes.get(i).getSpaces() && !routes.get(i).getIsClaimed()) {
                        filteredRoutes.add(routes.get(i));
                    }
                    break;
                case GREY:
                    if (hand.getYellow() + hand.getLocomotive() >= routes.get(i).getSpaces() && !routes.get(i).getIsClaimed()) {
                        filteredRoutes.add(routes.get(i));
                        break;
                    }
                    if (hand.getRed() + hand.getLocomotive() >= routes.get(i).getSpaces() && !routes.get(i).getIsClaimed()) {
                        filteredRoutes.add(routes.get(i));
                        break;
                    }
                    if (hand.getBlue() + hand.getLocomotive() >= routes.get(i).getSpaces() && !routes.get(i).getIsClaimed()) {
                        filteredRoutes.add(routes.get(i));
                        break;
                    }
                    if (hand.getPurple() + hand.getLocomotive() >= routes.get(i).getSpaces() && !routes.get(i).getIsClaimed()) {
                        filteredRoutes.add(routes.get(i));
                        break;
                    }
                    if (hand.getBlack() + hand.getLocomotive() >= routes.get(i).getSpaces() && !routes.get(i).getIsClaimed()) {
                        filteredRoutes.add(routes.get(i));
                        break;
                    }
                    if (hand.getGreen() + hand.getLocomotive() >= routes.get(i).getSpaces() && !routes.get(i).getIsClaimed()) {
                        filteredRoutes.add(routes.get(i));
                        break;
                    }
                    if (hand.getWhite() + hand.getLocomotive() >= routes.get(i).getSpaces() && !routes.get(i).getIsClaimed()) {
                        filteredRoutes.add(routes.get(i));
                        break;
                    }
                    if (hand.getOrange() + hand.getLocomotive() >= routes.get(i).getSpaces() && !routes.get(i).getIsClaimed()) {
                        filteredRoutes.add(routes.get(i));
                        break;
                    }
                    break;
            }

        }
        routeList = v.findViewById(R.id.claim_recycler_view);
        routeList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new Adapter(getContext(), filteredRoutes);
        routeList.setAdapter(adapter);
        mapReturn = v.findViewById(R.id.return_map);
        mapReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnMap = (OnReturnToMapListener) getActivity();
                returnMap.onReturnToMap();
            }
        });
        return v;
    }
    class Adapter extends RecyclerView.Adapter<Holder> {
        private ArrayList<Route> routes;
        private LayoutInflater inflater;

        public Adapter(Context context, ArrayList<Route> routes) {
            this.routes = routes;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.claim_route_recycler, parent, false);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            Route curRoute = routes.get(position);
            holder.bind(curRoute);
        }

        @Override
        public int getItemCount() {
            return routes.size();
        }

    }

    class Holder extends RecyclerView.ViewHolder {
        private TextView firstCity;
        private TextView secondCity;
        private Route curRoute;

        public Holder(View view) {
            super(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    claimRoute(curRoute);
                }
            });
            firstCity = view.findViewById(R.id.first_city);
            secondCity = view.findViewById(R.id.second_city);
        }

        void bind(Route curRoute) {
            this.curRoute = curRoute;
            firstCity.setText("First City: " + curRoute.getFirstCity());
            secondCity.setText("Second City: " + curRoute.getSecondCity());
        }
    }
    public void filterRoutes() {

    }
    public void claimRoute(Route curRoute) {

    }
}
