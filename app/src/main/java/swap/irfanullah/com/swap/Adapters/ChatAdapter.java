package swap.irfanullah.com.swap.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import swap.irfanullah.com.swap.GroupChatActivity.GroupChatAdapter;
import swap.irfanullah.com.swap.Libraries.TimeDiff;
import swap.irfanullah.com.swap.Models.GroupMessages;
import swap.irfanullah.com.swap.Models.Messenger;
import swap.irfanullah.com.swap.Models.User;
import swap.irfanullah.com.swap.R;
import swap.irfanullah.com.swap.Storage.PrefStorage;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private Context context;
    private ArrayList<Messenger> messengerArrayList;
    private User loggedUser;
    public static MessageClickListener messageClickListener;


    public ChatAdapter(Context context,ArrayList<Messenger> messengerArrayList) {
        this.context = context;
        this.messengerArrayList = messengerArrayList;
        loggedUser = PrefStorage.getUser(context);
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_row,viewGroup,false);
        return new ChatViewHolder(view,messengerArrayList);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder chatViewHolder, int i) {
        Messenger messenger = messengerArrayList.get(i);
        String msg = "";



        if(loggedUser.getUSER_ID() == messenger.getSENDER_ID()){

            if(messenger.isForwareded() == 1){
                msg = "Forwarded: \n"+messenger.getMESSAGE();
                Spannable spannable = new SpannableString(msg);
                spannable.setSpan(new ForegroundColorSpan(Color.GRAY),0,10,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                chatViewHolder.sender.setText(spannable, TextView.BufferType.SPANNABLE);

            }else {
                chatViewHolder.sender.setText(messenger.getMESSAGE());
            }

            chatViewHolder.sender.setVisibility(View.VISIBLE);
            chatViewHolder.sender_time.setText(TimeDiff.getTimeDifference(messenger.getCREATED_AT()));
            chatViewHolder.sender_time.setVisibility(View.VISIBLE);
            chatViewHolder.reciever.setVisibility(View.GONE);
            chatViewHolder.reciever_time.setVisibility(View.GONE);
        }else {

            if(messenger.isForwareded() == 1){
                msg = "Forwarded: \n"+messenger.getMESSAGE();
                Spannable spannable = new SpannableString(msg);
                spannable.setSpan(new ForegroundColorSpan(Color.GRAY),0,10,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                chatViewHolder.reciever.setText(spannable, TextView.BufferType.SPANNABLE);

            }else {
                chatViewHolder.reciever.setText(messenger.getMESSAGE());
            }

            chatViewHolder.reciever.setVisibility(View.VISIBLE);
            chatViewHolder.reciever_time.setText(TimeDiff.getTimeDifference(messenger.getCREATED_AT()));
            chatViewHolder.reciever_time.setVisibility(View.VISIBLE);
            chatViewHolder.sender.setVisibility(View.GONE);
            chatViewHolder.sender_time.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return messengerArrayList.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        private TextView sender,reciever,sender_time, reciever_time;
        public ChatViewHolder(@NonNull View itemView, final ArrayList<Messenger> messengerArrayList) {
            super(itemView);
            sender = itemView.findViewById(R.id.sender);
            reciever = itemView.findViewById(R.id.reciever);
            sender_time = itemView.findViewById(R.id.senderTime);
            reciever_time = itemView.findViewById(R.id.recieverTime);

            reciever.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
//                    messageClickListener.onMessageClicked(messengerArrayList.get(getAdapterPosition()));
                    messageClickListener.onMessageClicked(messengerArrayList.get(getAdapterPosition()));
                    return true;
                }
            });
        }
    }

    public void notifyAdapter(ArrayList<Messenger> messengerArrayList){
        this.messengerArrayList = messengerArrayList;
        notifyDataSetChanged();
    }

    public interface MessageClickListener {
        void onMessageClicked(Messenger msg);
    }

    public void setOnMessageClickListener (MessageClickListener messageListener){
        messageClickListener = messageListener;
    }
}
