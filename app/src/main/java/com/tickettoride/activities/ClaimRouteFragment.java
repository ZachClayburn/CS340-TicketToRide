package com.tickettoride.activities;

import android.content.Context;
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
import com.tickettoride.clientModels.Route;
import com.tickettoride.clientModels.helpers.RouteHelper;

import java.util.ArrayList;

public class ClaimRouteFragment extends Fragment {
    private RecyclerView routeList;
    private RecyclerView.Adapter adapter;
    private GameRoomActivity returnMap;
    private Button mapReturn;
    private DiscardFragmentListener discardFragmentListener;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.claim_route, container, false);
        ArrayList<Route> filteredRoutes = (ArrayList) RouteHelper.getSingleton().playerFilteredRoutes();
        routeList = v.findViewById(R.id.claim_recycler_view);
        routeList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new Adapter(getContext(), filteredRoutes);
        routeList.setAdapter(adapter);
        mapReturn = v.findViewById(R.id.return_map);
        mapReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnMap = (GameRoomActivity) getActivity();
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
    public void claimRoute(Route curRoute) {
        RouteHelper.getSingleton().claimRoute(curRoute);
        discardFragmentListener = (DiscardFragmentListener) getActivity();
        discardFragmentListener.moveToDiscard();
        //returnMap = (GameRoomActivity) getActivity();
        //returnMap.incrementTurn();
        //returnMap.onReturnToMap();
    }


}
