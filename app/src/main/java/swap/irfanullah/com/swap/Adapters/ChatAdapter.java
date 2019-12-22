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

import com.rygelouv.audiosensei.player.AudioSenseiListObserver;
import com.rygelouv.audiosensei.player.AudioSenseiPlayerView;

import java.util.ArrayList;

import swap.irfanullah.com.swap.GroupChatActivity.GroupChatAdapter;
import swap.irfanullah.com.swap.Libraries.RetroLib;
import swap.irfanullah.com.swap.Libraries.TimeDiff;
import swap.irfanullah.com.swap.Models.GroupMessages;
import swap.irfanullah.com.swap.Models.Messenger;
import swap.irfanullah.com.swap.Models.RMsg;
import swap.irfanullah.com.swap.Models.User;
import swap.irfanullah.com.swap.R;
import swap.irfanullah.com.swap.Storage.PrefStorage;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private Context context;
    private ArrayList<Messenger> messengerArrayList;
    private User loggedUser;
    int j =0;
    public static MessageClickListener messageClickListener;


    public ChatAdapter(Context context,ArrayList<Messenger> messengerArrayList) {
        this.context = context;
        this.messengerArrayList = messengerArrayList;
        loggedUser = PrefStorage.getUser(context);
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        j++;
        Messenger messenger = this.messengerArrayList.get(i);
        RMsg.logHere("ChatAdapterOnCreatedView Value: "+Integer.toString(i)+" : "+Integer.toString(j));
         View view = LayoutInflater.from(context).inflate(R.layout.chat_row,viewGroup,false);
            return new ChatViewHolder(view,messenger);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder chatViewHolder, int i) {
        Messenger messenger = messengerArrayList.get(i);
        String msg = "";



        if(loggedUser.getUSER_ID() == messenger.getSENDER_ID()){

            if(messenger.getIS_AUDIO() == 0) {
                if (messenger.isForwareded() == 1) {
                    msg = "Forwarded: \n" + messenger.getMESSAGE();
                    //Spannable spannable = new SpannableString(msg);
                   // spannable.setSpan(new ForegroundColorSpan(Color.GRAY), 0, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    chatViewHolder.sender.setText(msg);

                } else {
                    chatViewHolder.sender.setText(messenger.getMESSAGE());
                }

                chatViewHolder.sender.setVisibility(View.VISIBLE);
                chatViewHolder.sender_time.setText(TimeDiff.getTimeDifference(messenger.getCREATED_AT()));
                chatViewHolder.sender_time.setVisibility(View.VISIBLE);
                chatViewHolder.reciever.setVisibility(View.GONE);
                chatViewHolder.reciever_time.setVisibility(View.GONE);

                chatViewHolder.senderAudioPlayer.setVisibility(View.GONE);
                chatViewHolder.recieverAudioPlayer.setVisibility(View.GONE);
            }else {
                chatViewHolder.senderAudioPlayer.setAudioTarget("http://"+ RetroLib.IP +"/swap/public/statuses/audiomessages/"+messenger.getAUDIO());
                chatViewHolder.recieverAudioPlayer.setVisibility(View.GONE);


                chatViewHolder.sender.setVisibility(View.GONE);
                chatViewHolder.sender_time.setText(TimeDiff.getTimeDifference(messenger.getCREATED_AT()));
                chatViewHolder.sender_time.setVisibility(View.GONE);
                chatViewHolder.reciever.setVisibility(View.GONE);
                chatViewHolder.reciever_time.setVisibility(View.GONE);
            }
        }else {

            if (messenger.getIS_AUDIO() == 1) {
                chatViewHolder.recieverAudioPlayer.setAudioTarget("http://"+ RetroLib.IP +"/swap/public/statuses/audiomessages/"+messenger.getAUDIO());
                chatViewHolder.senderAudioPlayer.setVisibility(View.GONE);

                chatViewHolder.reciever.setVisibility(View.GONE);
                chatViewHolder.reciever_time.setText(TimeDiff.getTimeDifference(messenger.getCREATED_AT()));
                chatViewHolder.reciever_time.setVisibility(View.GONE);
                chatViewHolder.sender.setVisibility(View.GONE);
                chatViewHolder.sender_time.setVisibility(View.GONE);

            } else {
                if (messenger.isForwareded() == 1) {
                    msg = "Forwarded: \n" + messenger.getMESSAGE();
                    //Spannable spannable = new SpannableString(msg);
                    //spannable.setSpan(new ForegroundColorSpan(Color.GRAY), 0, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    chatViewHolder.reciever.setText(msg);

                } else {
                    chatViewHolder.reciever.setText(messenger.getMESSAGE());
                }

                chatViewHolder.reciever.setVisibility(View.VISIBLE);
                chatViewHolder.reciever_time.setText(TimeDiff.getTimeDifference(messenger.getCREATED_AT()));
                chatViewHolder.reciever_time.setVisibility(View.VISIBLE);
                chatViewHolder.sender.setVisibility(View.GONE);
                chatViewHolder.sender_time.setVisibility(View.GONE);

                chatViewHolder.senderAudioPlayer.setVisibility(View.GONE);
                chatViewHolder.recieverAudioPlayer.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return messengerArrayList.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        private TextView sender,reciever,sender_time, reciever_time;
        private AudioSenseiPlayerView senderAudioPlayer, recieverAudioPlayer;
        public ChatViewHolder(@NonNull View itemView, final Messenger msg) {
            super(itemView);

                senderAudioPlayer = itemView.findViewById(R.id.sender_audio_player);
                recieverAudioPlayer = itemView.findViewById(R.id.reciever_audio_player);

                sender = itemView.findViewById(R.id.sender);
                reciever = itemView.findViewById(R.id.reciever);
                sender_time = itemView.findViewById(R.id.senderTime);
                reciever_time = itemView.findViewById(R.id.recieverTime);

                reciever.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
//                    messageClickListener.onMessageClicked(messengerArrayList.get(getAdapterPosition()));
                        messageClickListener.onMessageClicked(msg);
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

    @Override
    public int getItemViewType(int position) {
        Messenger msg = this.messengerArrayList.get(position);
        if(msg.getIS_AUDIO() == 1){
            return 1;
        }else {
            return 0;
        }
    }
}
