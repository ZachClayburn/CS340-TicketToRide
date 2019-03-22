package com.tickettoride.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tickettoride.R;
import com.tickettoride.clientModels.Chat;
import com.tickettoride.clientModels.DataManager;
import com.tickettoride.facadeProxies.ChatFacadeProxy;
import com.tickettoride.models.Message;
import com.tickettoride.models.Player;
import com.tickettoride.models.PlayerColor;
import com.tickettoride.models.idtypes.PlayerID;

import java.util.List;
import java.util.UUID;

import static java.lang.Thread.sleep;

public class ChatFragment extends Fragment {
    
    private RecyclerView chatmessages;
    private EditText message;
    private Button sendButton;
    private String mess=null;
    private MessageListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        View v=inflater.inflate(R.layout.chat_fragment, container, false);
        chatmessages = v.findViewById(R.id.chat_view);
        chatmessages.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MessageListAdapter(getContext(), Chat.getSingleton().getMessages());
        chatmessages.setAdapter(adapter);
        message= v.findViewById(R.id.chat_room);
        sendButton = v.findViewById(R.id.send_button);
        sendButton.setEnabled(false);
        
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatFacadeProxy chatFacadeProxy= new ChatFacadeProxy();
                Message msg = new Message(mess, DataManager.getSINGLETON().getPlayer().getPlayerID());
                chatFacadeProxy.sendMessage(msg);
                mess="";
                message.setText(null);
                sendButton.setEnabled(false);
            }
        });
        
        message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mess = s.toString();
                checkMessage();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        
        return v;
    }
    
    public void updateChat(){
        adapter.newMessage();
    }
    
    public void checkMessage(){
        if(mess==null || mess == ""){
            sendButton.setEnabled(false);
        }else {
            sendButton.setEnabled(true);
        }
    }

    public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MessageHolder> {
        private Context mContext;
        private List<Message> mMessageList;

        public MessageListAdapter(Context context, List<Message> messageList) {
            mContext = context;
            mMessageList = messageList;
        }

        @Override
        public MessageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater mInflater = LayoutInflater.from(mContext);
            View view = mInflater.inflate(R.layout.chat_recycler_view, parent, false);
            return new MessageHolder(view);
        }

        @Override
        public int getItemCount() {
            return mMessageList.size();
        }
        
        public void newMessage(){
            notifyDataSetChanged();
        }

        // binds the data to the TextView in each row
        @Override
        public void onBindViewHolder(MessageHolder holder, int position) {
            Message message = mMessageList.get(position);
            PlayerID playerID=message.getPlayerID();
            String name = getColor(playerID);
            holder.mName.setText(name);
            holder.mMessage.setText(message.getMessage());
            holder.mTime.setText(message.getTimeString());
        }
        
        private String getColor(PlayerID player){
            List<Player> players=DataManager.getSINGLETON().getGamePlayers();
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

        private class MessageHolder extends RecyclerView.ViewHolder{
            TextView mName;
            TextView mTime;
            TextView mMessage;
            
            public MessageHolder(View itemView){
                super(itemView);
                mName = itemView.findViewById(R.id.chat_player_name);
                mMessage = itemView.findViewById(R.id.chat_message);
                mTime = itemView.findViewById(R.id.chat_time);
            }
        }

        Message getItem(int id) {
            return mMessageList.get(id);
        }
        
        
        
        
        
    }
}
