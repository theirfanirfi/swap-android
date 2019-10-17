package swap.irfanullah.com.swap.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapLabel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import swap.irfanullah.com.swap.ChatActivity;
import swap.irfanullah.com.swap.GroupChatActivity.GroupChatActivity;
import swap.irfanullah.com.swap.Libraries.GLib;
import swap.irfanullah.com.swap.Libraries.RetroLib;
import swap.irfanullah.com.swap.Models.Messenger;
import swap.irfanullah.com.swap.Models.Participants;
import swap.irfanullah.com.swap.Models.RMsg;
import swap.irfanullah.com.swap.Models.User;
import swap.irfanullah.com.swap.NLUserProfile;
import swap.irfanullah.com.swap.R;
import swap.irfanullah.com.swap.Storage.PrefStorage;

public class ParticipantsAdapter extends RecyclerView.Adapter<ParticipantsAdapter.ParticipantsViewHolder> {

    private Context context;
    private ArrayList<Participants> participantsArrayList;
    public ParticipantsAdapter(Context context,ArrayList<Participants> participantsArrayList) {
        this.context = context;
        this.participantsArrayList = participantsArrayList;
    }

    @NonNull
    @Override
    public ParticipantsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.participants_row,viewGroup,false);

        return new ParticipantsViewHolder(view,context,this.participantsArrayList);
    }

    @Override
    public void onBindViewHolder(@NonNull ParticipantsViewHolder participantsViewHolder, int i) {
        Participants participants = participantsArrayList.get(i);
        if(participants.getIS_GROUP() == 0) {
            if (participants.getAM_I_USER_ONE() == 1) {
                participantsViewHolder.chatWithUsername.setText(participants.getUSER_TWO_NAME());
                if (participants.getUSER_TWO_PROFILE_IMAGE() == null) {
                    participantsViewHolder.profile_image.setImageResource(R.drawable.ic_person);
                } else {
                    GLib.downloadImage(context, participants.getUSER_TWO_PROFILE_IMAGE()).into(participantsViewHolder.profile_image);
                }
            } else {
                participantsViewHolder.chatWithUsername.setText(participants.getUSER_ONE_NAME());
                if (participants.getUSER_ONE_PROFILE_IMAGE() == null) {
                    participantsViewHolder.profile_image.setImageResource(R.drawable.ic_person);
                } else {
                    GLib.downloadImage(context, participants.getUSER_ONE_PROFILE_IMAGE()).into(participantsViewHolder.profile_image);
                }
            }
        }else {
            participantsViewHolder.chatWithUsername.setText(participants.getGROUP_NAME());
            participantsViewHolder.profile_image.setImageResource(R.drawable.ic_person);
        }

       // loadLastMessageAndUnReadMessagesCount(participants, participantsViewHolder);

    }

    @Override
    public int getItemCount() {
        return participantsArrayList.size();
    }

    public static class ParticipantsViewHolder extends RecyclerView.ViewHolder {
        private Context context;
        private TextView chatWithUsername, last_message, last_message_time;
        private ImageView profile_image;
        private ConstraintLayout pLayout;
        private TextView unread_messages_count;
        private final String LOGGEDIN_USER_INTENT_KEY = "loggedin_user_id";
        private final String TO_CHAT_WITH_USER_INTENT_KEY = "to_chat_with_user_id";
        private final String CHAT_ID_INTENT_KEY = "chat_id";

        public ParticipantsViewHolder(@NonNull View itemView,final Context context,final ArrayList<Participants> participantsArrayList) {
            super(itemView);
            this.context = context;
            chatWithUsername = itemView.findViewById(R.id.chatWithUsername);
            profile_image = itemView.findViewById(R.id.profile_image);
            pLayout = itemView.findViewById(R.id.participantLayout);
            last_message = itemView.findViewById(R.id.last_message_textView);
            last_message_time = itemView.findViewById(R.id.last_msg_time);
            unread_messages_count = itemView.findViewById(R.id.unread_msgs_count);
            unread_messages_count.setVisibility(View.GONE);

            gotoChat(participantsArrayList);

            profile_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Participants participants = participantsArrayList.get(position);
                    int USER_ID = 0;
                    User user = PrefStorage.getUser(context);
                    if(user.getUSER_ID() == participants.getUSER_TWO()) {
                        USER_ID = participants.getUSER_ONE_ID();
                    }else if(user.getUSER_ID() == participants.getUSER_ONE()){
                        USER_ID = participants.getUSER_TWO_ID();
                    }else {
                        USER_ID = 0;
                    }

                    Intent profileAct = new Intent(context, NLUserProfile.class);
                    profileAct.putExtra("user_id",USER_ID);
                    context.startActivity(profileAct);
                }
            });
        }



        private void gotoChat(final ArrayList<Participants> participantsArrayList){
            pLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    User user = PrefStorage.getUser(context);
                    Participants participants = participantsArrayList.get(position);
                    int CHAT_ID = participants.getCHAT_ID();

                    if (participants.getIS_GROUP() == 0) {
                        Intent chatAct = new Intent(context, ChatActivity.class);
                        if (user.getUSER_ID() == participants.getUSER_TWO()) {
                            int TO_CHAT_WITH_ID = participants.getUSER_ONE();
                            int LOGGEDIN_USER_ID = participants.getUSER_TWO();
                            chatAct.putExtra(LOGGEDIN_USER_INTENT_KEY, LOGGEDIN_USER_ID);
                            chatAct.putExtra(TO_CHAT_WITH_USER_INTENT_KEY, TO_CHAT_WITH_ID);
                            chatAct.putExtra(CHAT_ID_INTENT_KEY, CHAT_ID);
                            context.startActivity(chatAct);
                        } else {
                            int TO_CHAT_WITH_ID = participants.getUSER_TWO();
                            int LOGGEDIN_USER_ID = participants.getUSER_ONE();
                            chatAct.putExtra(LOGGEDIN_USER_INTENT_KEY, LOGGEDIN_USER_ID);
                            chatAct.putExtra(TO_CHAT_WITH_USER_INTENT_KEY, TO_CHAT_WITH_ID);
                            chatAct.putExtra(CHAT_ID_INTENT_KEY, CHAT_ID);
                            context.startActivity(chatAct);
                        }
                    } else {
                        //group

                        Intent groupAct = new Intent(context, GroupChatActivity.class);
                        groupAct.putExtra("group_id", Integer.toString(participants.getGROUP_ID()));
                        context.startActivity(groupAct);


                        //RMsg.toastHere(context,participants.getGROUP_NAME());
                    }
                }
            });
        }
    }

    public void notifyAdapter(ArrayList<Participants> participantsArrayList){
        this.participantsArrayList = participantsArrayList;
        notifyDataSetChanged();
    }

    private void loadLastMessageAndUnReadMessagesCount(final Participants participants, final ParticipantsViewHolder participantsViewHolder){
        final User user = PrefStorage.getUser(context);
        RetroLib.geApiService().getUnReadAndLast(user.getTOKEN(),participants.getCHAT_ID()).enqueue(new Callback<Messenger>() {
            @Override
            public void onResponse(Call<Messenger> call, Response<Messenger> response) {
                if(response.isSuccessful()){
                    Messenger msg = response.body();
                    if(msg.getIS_AUTHENTICATED()){
                        if(msg.getIS_ERROR()){
                            RMsg.logHere(msg.getMESSAGE());
                        }else {
                            Messenger last_msg = msg.getLAST_MESSAGE();
                            int count = msg.getCOUNT_READ_MESSAGES();

                            if(msg.getLAST_MESSAGE_COUNT() > 0) {
                                String who_sent = "";
                                if(msg.getSENDER_ID() == user.getUSER_ID()){
                                    who_sent = "Me: ";
                                }else {
                                    if(user.getUSER_ID() == participants.getUSER_ONE()) {
                                        who_sent = participants.getUSER_TWO_NAME() + ": ";
                                    }else {
                                        who_sent = participants.getUSER_ONE_NAME() + ": ";
                                    }
                                }

                                participantsViewHolder.last_message.setText(who_sent+last_msg.getMESSAGE());
                                participantsViewHolder.last_message.setVisibility(View.VISIBLE);
                            }


                            if(msg.getCOUNT_READ_MESSAGES() > 0) {
                                participantsViewHolder.unread_messages_count.setText(Integer.toString(count));
                                participantsViewHolder.unread_messages_count.setVisibility(View.VISIBLE);
                            }
                        }
                    }else {
                        RMsg.logHere(msg.getMESSAGE());
                    }
                }else {
                    RMsg.logHere(response.raw().toString());
                }
            }

            @Override
            public void onFailure(Call<Messenger> call, Throwable t) {

            }
        });
    }

}
