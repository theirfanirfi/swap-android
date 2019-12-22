package swap.irfanullah.com.swap.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rygelouv.audiosensei.player.AudioSenseiPlayerView;

import java.util.ArrayList;

import swap.irfanullah.com.swap.Libraries.RetroLib;
import swap.irfanullah.com.swap.Libraries.TimeDiff;
import swap.irfanullah.com.swap.Models.Messenger;
import swap.irfanullah.com.swap.Models.User;
import swap.irfanullah.com.swap.R;
import swap.irfanullah.com.swap.Storage.PrefStorage;

public class ChatAdapterWithMultipleViews extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static ArrayList<Messenger> messengerArrayList;
    private Context context;
    private User loggedUser;
    public static MessageClickListener messageClickListener;

    public ChatAdapterWithMultipleViews(ArrayList<Messenger> messenger, Context context) {
        messengerArrayList = messenger;
        this.context = context;
        this.loggedUser = PrefStorage.getUser(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //1 : load Audio View
        //0: load TextMessage View
        //here -> i  has 0 or 1 based on the number returned form getItemViewType();
        switch (i){
            case 0:
                View textMessageview = LayoutInflater.from(context).inflate(R.layout.chat_row,viewGroup,false);
                return new TextMessageViewHolder(textMessageview);
            case 1:
                View audioMessageView = LayoutInflater.from(context).inflate(R.layout.audio_chat_row,viewGroup,false);
                return new AudioMessageViewHolder(audioMessageView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        int viewType = viewHolder.getItemViewType();
        Messenger messenger = messengerArrayList.get(i);
        String msg = "";
        switch (viewType){
            case 0:
                //textMessageView
                TextMessageViewHolder textMessageViewHolder = (TextMessageViewHolder) viewHolder;
                if(loggedUser.getUSER_ID() == messenger.getSENDER_ID()) {
                    if (messenger.isForwareded() == 1) {
                        msg = "Forwarded: \n" + messenger.getMESSAGE();
                        //Spannable spannable = new SpannableString(msg);
                        // spannable.setSpan(new ForegroundColorSpan(Color.GRAY), 0, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        textMessageViewHolder.sender.setText(msg);

                    } else {
                        textMessageViewHolder.sender.setText(messenger.getMESSAGE());
                    }

                    textMessageViewHolder.sender.setVisibility(View.VISIBLE);
                    textMessageViewHolder.sender_time.setText(TimeDiff.getTimeDifference(messenger.getCREATED_AT()));
                    textMessageViewHolder.sender_time.setVisibility(View.VISIBLE);
                    textMessageViewHolder.reciever.setVisibility(View.GONE);
                    textMessageViewHolder.reciever_time.setVisibility(View.GONE);

                }else {
                    if (messenger.isForwareded() == 1) {
                        msg = "Forwarded: \n" + messenger.getMESSAGE();
                        //Spannable spannable = new SpannableString(msg);
                        //spannable.setSpan(new ForegroundColorSpan(Color.GRAY), 0, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        textMessageViewHolder.reciever.setText(msg);

                    } else {
                        textMessageViewHolder.reciever.setText(messenger.getMESSAGE());
                    }

                    textMessageViewHolder.reciever.setVisibility(View.VISIBLE);
                    textMessageViewHolder.reciever_time.setText(TimeDiff.getTimeDifference(messenger.getCREATED_AT()));
                    textMessageViewHolder.reciever_time.setVisibility(View.VISIBLE);
                    textMessageViewHolder.sender.setVisibility(View.GONE);
                    textMessageViewHolder.sender_time.setVisibility(View.GONE);
                }
                break;
            case 1:
                AudioMessageViewHolder audioMessageViewHolder = (AudioMessageViewHolder) viewHolder;
                if(loggedUser.getUSER_ID() == messenger.getSENDER_ID()) {
                    audioMessageViewHolder.senderAudioPlayer.setAudioTarget("http://"+ RetroLib.IP +"/swap/public/statuses/audiomessages/"+messenger.getAUDIO());
                    audioMessageViewHolder.recieverAudioPlayer.setVisibility(View.GONE);
                }else {
                    audioMessageViewHolder.recieverAudioPlayer.setAudioTarget("http://"+ RetroLib.IP +"/swap/public/statuses/audiomessages/"+messenger.getAUDIO());
                    audioMessageViewHolder.senderAudioPlayer.setVisibility(View.GONE);
                }
                break;

                //audio message view
        }
    }

    @Override
    public int getItemCount() {
        return this.messengerArrayList.size();
    }

    public static class TextMessageViewHolder extends RecyclerView.ViewHolder {
        private TextView sender,reciever,sender_time, reciever_time;
        public TextMessageViewHolder(@NonNull View itemView) {
            super(itemView);

            final int position = getAdapterPosition();
            sender = itemView.findViewById(R.id.sender);
            reciever = itemView.findViewById(R.id.reciever);
            sender_time = itemView.findViewById(R.id.senderTime);
            reciever_time = itemView.findViewById(R.id.recieverTime);

            reciever.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
//                    messageClickListener.onMessageClicked(messengerArrayList.get(getAdapterPosition()));
                    messageClickListener.onMessageClicked(messengerArrayList.get(position));
                    return true;
                }
            });
        }
    }

    public static class AudioMessageViewHolder extends RecyclerView.ViewHolder {
        private AudioSenseiPlayerView senderAudioPlayer, recieverAudioPlayer;
        public AudioMessageViewHolder(@NonNull View itemView) {
            super(itemView);

            senderAudioPlayer = itemView.findViewById(R.id.sender_audio_player);
            recieverAudioPlayer = itemView.findViewById(R.id.reciever_audio_player);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Messenger messenger = this.messengerArrayList.get(position);

        if(messenger.getIS_AUDIO() == 1){
            return 1;
        }else {
            return 0;
        }
    }

    public interface MessageClickListener {
        void onMessageClicked(Messenger msg);
    }

    public void setOnMessageClickListener (MessageClickListener messageListener){
        messageClickListener = messageListener;
    }

    public void notifyAdapter(ArrayList<Messenger> messenger){
        int index = messengerArrayList.size();
//        messengerArrayList.add(index,);
        notifyItemRangeInserted(messengerArrayList.size(),messenger.size());
        messengerArrayList = messenger;
        notifyDataSetChanged();
       // notifyItemRangeInserted(messengerArrayList.size(),messenger.size());
    }


    public int notifyAdapter2(Messenger messenger){
        int index = messengerArrayList.size();
        messengerArrayList.add(index,messenger);
//        notifyItemRangeInserted(messengerArrayList.size(),messenger.size());
//        messengerArrayList = messenger;
        notifyDataSetChanged();
        return messengerArrayList.size();
        // notifyItemRangeInserted(messengerArrayList.size(),messenger.size());
    }

    @Override
    public long getItemId(int position) {
        Messenger messenger = this.messengerArrayList.get(position);
        return messenger.getMESSAGE_ID();
    }
}
