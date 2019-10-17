package swap.irfanullah.com.swap.GroupChatActivity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import swap.irfanullah.com.swap.Libraries.TimeDiff;
import swap.irfanullah.com.swap.Models.GroupMessages;
import swap.irfanullah.com.swap.Models.Messenger;
import swap.irfanullah.com.swap.Models.User;
import swap.irfanullah.com.swap.R;
import swap.irfanullah.com.swap.Storage.PrefStorage;

public class GroupChatAdapter extends RecyclerView.Adapter<GroupChatAdapter.ChatViewHolder> {
    private Context context;
    private ArrayList<GroupMessages> messengerArrayList;
    private User loggedUser;
    public static MessageClickListener messageClickListener;

    public GroupChatAdapter(Context context, ArrayList<GroupMessages> messengerArrayList) {
        this.context = context;
        this.messengerArrayList = messengerArrayList;
        loggedUser = PrefStorage.getUser(context);
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.group_chat_row,viewGroup,false);
        return new ChatViewHolder(view, this.messengerArrayList);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder chatViewHolder, int i) {
        GroupMessages messenger = messengerArrayList.get(i);
        if(loggedUser.getUSER_ID() == messenger.getSENDER_ID()){
            chatViewHolder.sender.setText(messenger.getMESSAGE());
           // chatViewHolder.sender_username.setText(messenger.getUSERNAME());
            chatViewHolder.sender.setVisibility(View.VISIBLE);
            chatViewHolder.sender_time.setText(TimeDiff.getTimeDifference(messenger.getCREATED_AT()));
            chatViewHolder.sender_time.setVisibility(View.VISIBLE);
            chatViewHolder.reciever.setVisibility(View.GONE);
            chatViewHolder.reciever_time.setVisibility(View.GONE);
            chatViewHolder.reciever_profile_image.setVisibility(View.GONE);
            chatViewHolder.reciever_username.setVisibility(View.GONE);

        }else {
            chatViewHolder.reciever.setText(messenger.getMESSAGE());
            chatViewHolder.reciever_username.setText(messenger.getUSERNAME());
            chatViewHolder.reciever.setVisibility(View.VISIBLE);
            chatViewHolder.reciever_time.setText(TimeDiff.getTimeDifference(messenger.getCREATED_AT()));
            chatViewHolder.reciever_time.setVisibility(View.VISIBLE);
            chatViewHolder.sender.setVisibility(View.GONE);
            chatViewHolder.sender_time.setVisibility(View.GONE);
            chatViewHolder.sender_profile_image.setVisibility(View.GONE);

            if(messenger.isForwareded() == 1){
                chatViewHolder.forwardedRecieverView.setVisibility(View.VISIBLE);
            }else{
                chatViewHolder.forwardedRecieverView.setVisibility(View.GONE);

            }
            //chatViewHolder.sender_username.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return messengerArrayList.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        private TextView sender,reciever,sender_time, reciever_time,
                sender_username,reciever_username,forwardedRecieverView;
        private ImageView sender_profile_image,reciever_profile_image;
        public ChatViewHolder(@NonNull View itemView, final ArrayList<GroupMessages> messengerArrayList) {
            super(itemView);
            sender = itemView.findViewById(R.id.sender);
            reciever = itemView.findViewById(R.id.reciever);
            sender_time = itemView.findViewById(R.id.senderTime);
            reciever_time = itemView.findViewById(R.id.recieverTime);
            sender_profile_image = itemView.findViewById(R.id.sender_profile_image);
            reciever_profile_image = itemView.findViewById(R.id.reciever_profile_image);
            //sender_username = itemView.findViewById(R.id.sender_username);
            reciever_username = itemView.findViewById(R.id.reciever_username);
            forwardedRecieverView = itemView.findViewById(R.id.recieverforwardedView);
            reciever.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    messageClickListener.onMessageClicked(messengerArrayList.get(getAdapterPosition()));
                    return true;
                }
            });
        }
    }

    public void notifyAdapter(ArrayList<GroupMessages> messengerArrayList){
        this.messengerArrayList = messengerArrayList;
        notifyDataSetChanged();
    }

    public interface MessageClickListener {
        void onMessageClicked(GroupMessages groupMessage);
    }

    public void setOnMessageClickListener (MessageClickListener messageListener){
        messageClickListener = messageListener;
    }
}
