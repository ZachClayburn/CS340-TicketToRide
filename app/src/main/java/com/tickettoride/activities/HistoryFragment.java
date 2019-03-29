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
import com.tickettoride.clientModels.DataManager;
import com.tickettoride.clientModels.History;
import com.tickettoride.models.Message;
import com.tickettoride.models.Player;
import com.tickettoride.models.PlayerColor;
import com.tickettoride.models.idtypes.PlayerID;

import java.util.Arrays;
import java.util.List;

import static java.lang.Thread.sleep;

public class HistoryFragment extends Fragment {

    private RecyclerView historyList;
    private List<String> allEvents = Arrays.asList("Programmed 260", "Suffered greatly", "Programmed 312", "Cried",
            "Programming 340", "almost done", "Sleep is so close...."); //TODO dummy test list, replace with necessary list
    private Button mapButton;
    private HistoryAdapter adapter;
    private OnReturnToMapListener mapListener;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        View v = inflater.inflate(R.layout.history, container, false);
        
        historyList = v.findViewById(R.id.history_view);
        historyList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new HistoryAdapter(getContext(), History.getSingleton().getHistory());
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

    public void updateHistory(){
        adapter.newEvent();
    }
    
    class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.Holder> {
        private List<Message> allEvents;
        private Context mContext;

        public HistoryAdapter(Context context, List<Message> allEvents) {
            this.allEvents = allEvents;
            mContext=context;
        }
        
        public void newEvent(){notifyDataSetChanged();}

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater=LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.history_recycler, parent, false);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            Message curEvent = allEvents.get(position);
            holder.bind(curEvent);
        }

        @Override
        public int getItemCount() {
            return allEvents.size();
        }

        Message getItem(int id) {
            return allEvents.get(id);
        }

        class Holder extends RecyclerView.ViewHolder {
            private TextView event;
            private TextView event_time;
            private TextView event_player;


            public Holder(View itemView) {
                super(itemView);
                event = itemView.findViewById(R.id.event_message);
                event_time = itemView.findViewById(R.id.event_time);
                event_player = itemView.findViewById(R.id.event_player_name);
            }

            void bind(Message curEvent) {
                event.setText(curEvent.getMessage());
                event_time.setText(curEvent.getTimeString());
                event_player.setText(getColor(curEvent.getPlayerID()));
            }

            private String getColor(PlayerID player){
                List<Player> players= DataManager.getSINGLETON().getGamePlayers();
                while(players==null){
                    try {
                        sleep(10);
                    }catch (Exception e){}
                    players=DataManager.getSINGLETON().getGamePlayers();
                }
                PlayerColor c=null;
                for(Player p : players){
                    if(p.getPlayerID().equals(player)){
                        c=p.getColor();
                    }
                }
                if(c==null){
                    return "rainbow";
                }
                return c.toString();
            }
        }
    }

    
}
