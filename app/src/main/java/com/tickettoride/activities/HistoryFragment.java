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

import java.util.Arrays;
import java.util.List;

public class HistoryFragment extends Fragment {

    private RecyclerView historyList;
    private List<String> allEvents = Arrays.asList("Programmed 260", "Suffered greatly", "Programmed 312", "Cried",
            "Programming 340", "almost done", "Sleep is so close...."); //TODO dummy test list, replace with necessary list
    private Button mapButton;
    private Adapter adapter;
    private OnReturnToMapListener mapListener;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.history, container, false);
        historyList = v.findViewById(R.id.history_view);
        historyList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new Adapter(getContext(), allEvents);
        historyList.setAdapter(adapter);
        mapButton = v.findViewById(R.id.return_map);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapListener = (OnReturnToMapListener) getActivity();
                mapListener.onReturnToMap();
            }
        });
        return v;
    }
    class Adapter extends RecyclerView.Adapter<Holder> {
        private List<String> allEvents;
        private LayoutInflater inflater;

        public Adapter(Context context, List<String> allEvents) {
            this.allEvents = allEvents;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.history_recycler, parent, false);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            String curEvent = allEvents.get(position);
            holder.bind(curEvent);
        }

        @Override
        public int getItemCount() {
            return allEvents.size();
        }

    }

    class Holder extends RecyclerView.ViewHolder {
        private TextView event;

        public Holder(View view) {
            super(view);
            event = view.findViewById(R.id.event);
        }

        void bind(String curEvent) {
            event.setText(curEvent);
        }
    }
}
