package swap.irfanullah.com.swap.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import swap.irfanullah.com.swap.Libraries.CReq;
import swap.irfanullah.com.swap.Libraries.GLib;
import swap.irfanullah.com.swap.Models.Notification;
import swap.irfanullah.com.swap.Models.RMsg;
import swap.irfanullah.com.swap.Models.Swap;
import swap.irfanullah.com.swap.NLUserProfile;
import swap.irfanullah.com.swap.R;
import swap.irfanullah.com.swap.StatusActivity;
import swap.irfanullah.com.swap.Storage.PrefStorage;
import swap.irfanullah.com.swap.UserProfile;

import static java.security.AccessController.getContext;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {
    private Context context;
    ArrayList<Notification> notificationArrayList;
    public NotificationAdapter(Context context, ArrayList<Notification> notificationArrayList) {
        this.context = context;
        this.notificationArrayList = notificationArrayList;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(R.layout.notification_status_custom_row, viewGroup, false);
            return new NotificationViewHolder(view, context, notificationArrayList);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder notificationViewHolder, int i) {
        Notification notification = this.notificationArrayList.get(i);
        if(notification.getPROFIE_IMAGE() == null){
            notificationViewHolder.profile_image.setImageResource(R.drawable.ic_person);
        }else {
            GLib.downloadImage(context,notification.getPROFIE_IMAGE()).into(notificationViewHolder.profile_image);
        }


        if(notification.getIS_STATUS() == 1){

            SpannableString text = new SpannableString(notification.getFULL_NAME()+" wants to swap a status with you.");
            text.setSpan(new TextAppearanceSpan(context, R.style.TextViewBold), 0, notification.getFULL_NAME().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            notificationViewHolder.msg.setText(text,TextView.BufferType.SPANNABLE);
        }else if(notification.getIS_FOLLOW() == 1){

            SpannableString text = new SpannableString(notification.getFULL_NAME()+" followed you.");
            text.setSpan(new TextAppearanceSpan(context, R.style.TextViewBold), 0, notification.getFULL_NAME().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            notificationViewHolder.msg.setText(text,TextView.BufferType.SPANNABLE);
        } else {
            Log.i(RMsg.LOG_MESSAGE,"na kay kar : isStatus: "+Integer.toString(notification.getIS_STATUS())+" isFollow: "+Integer.toString(notification.getIS_FOLLOW()));
        }
    }

    @Override
    public int getItemCount() {
        return notificationArrayList.size();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        private Context context;
        ImageView profile_image;
        TextView msg;
        TextView ntime;
        CardView layout;
        public NotificationViewHolder(@NonNull View itemView, final Context context, final ArrayList<Notification> notificationArrayList) {
            super(itemView);
            this.context = context;
            profile_image = itemView.findViewById(R.id.profile_image);
            msg = itemView.findViewById(R.id.notification_msg);
            ntime = itemView.findViewById(R.id.statusTimeTextView);
            layout = itemView.findViewById(R.id.notification_layout);

            profile_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Notification notification = notificationArrayList.get(position);
                    if(notification.getIS_FOLLOW() == 1) {
                        Log.i(RMsg.LOG_MESSAGE,Integer.toString(notification.getFOLLOWER_ID()));
                        if (PrefStorage.isMe(context, notification.getFOLLOWER_ID())) {
                            Intent profileAct = new Intent(context, UserProfile.class);
                            profileAct.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(profileAct);
                        } else {
                            Intent profileAct = new Intent(context, NLUserProfile.class);
                            profileAct.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            profileAct.putExtra("user_id", notification.getFOLLOWER_ID());
                            context.startActivity(profileAct);
                        }
                    }else if(notification.getIS_STATUS() == 1) {
                        Log.i(RMsg.LOG_MESSAGE,Integer.toString(notification.getSWAPER_ID()));

                    }
                }
            });


            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Notification notification = notificationArrayList.get(position);
                    if(notification.getIS_STATUS() == 1){
                        CReq.getSwap(context, notification.getSWAP_ID(), new CReq.CReqListener() {
                            @Override
                            public void onRecieve(Swap swap) {
                                Log.i(RMsg.LOG_MESSAGE,swap.getMESSAGE());
                                Swap swp = swap.getGET_SWAP();

                                if(swap.getAuthenticated()){
                                    if(swap.getIS_FOUND()){

                                        Intent i = new Intent(context,StatusActivity.class);
                                        i.putExtra("status_id",swp.getSTATUS_ID());
                                        i.putExtra("position",0);
                                        i.putExtra("is_accepted",swp.getIS_ACCEPTED());
                                        i.putExtra("swap_id",swp.getSWAP_ID());
                                        Log.i(RMsg.LOG_MESSAGE,"SWAP_ID: "+Integer.toString(swp.getSWAP_ID())+" : "+"Status_ID: "+Integer.toString(swp.getSTATUS_ID())+ " : isacc : "+Integer.toString(swp.getIS_ACCEPTED()));

                                        context.startActivity(i);
                                    }else {
                                        Toast.makeText(context,RMsg.SWAP_NOT_FOUND_MESSAGE,Toast.LENGTH_LONG).show();
                                    }
                                }else {
                                    Toast.makeText(context,RMsg.AUTH_ERROR_MESSAGE,Toast.LENGTH_LONG).show();

                                }
                            }

                            @Override
                            public void onError(String error) {
                                Toast.makeText(context,RMsg.REQ_ERROR_MESSAGE,Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onException(String ex) {
                                Toast.makeText(context,ex.toString(),Toast.LENGTH_LONG).show();

                            }
                        });

                    }else if(notification.getIS_FOLLOW() == 1){
                        Log.i(RMsg.LOG_MESSAGE,Integer.toString(notification.getFOLLOWER_ID()));
                    }
                }
            });
        }
    }

    public void notifyAdaper(ArrayList<Notification> notificationArrayList){
        this.notificationArrayList = notificationArrayList;
        notifyDataSetChanged();
    }

}
