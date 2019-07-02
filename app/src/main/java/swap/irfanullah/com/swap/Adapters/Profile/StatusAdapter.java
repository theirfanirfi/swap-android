package swap.irfanullah.com.swap.Adapters.Profile;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import swap.irfanullah.com.swap.Libraries.GLib;
import swap.irfanullah.com.swap.Libraries.TimeDiff;
import swap.irfanullah.com.swap.Models.Status;
import swap.irfanullah.com.swap.NLUserProfile;
import swap.irfanullah.com.swap.R;
import swap.irfanullah.com.swap.StatusActivity;
import swap.irfanullah.com.swap.Storage.PrefStorage;
import swap.irfanullah.com.swap.SwapWithActivity;
import swap.irfanullah.com.swap.UserProfile;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.StatusViewHolder> {
    private Context context;
    private ArrayList<Status> statuses;

    public StatusAdapter(Context context, ArrayList<Status> st) {
        this.context = context;
        this.statuses = st;
    }

    @NonNull
    @Override
    public StatusViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.status_profile_row,viewGroup,false);


        return new StatusViewHolder(view, this.context, this.statuses);
    }

    @Override
    public void onBindViewHolder(@NonNull StatusViewHolder statusViewHolder, int i) {
        Status e = statuses.get(i);
    statusViewHolder.statusDescription.setText(e.getSTATUS());
    statusViewHolder.ratingBar.setRating(e.getRATTING());
    statusViewHolder.statusTime.setText(TimeDiff.getTimeDifference(e.getTIME()));
    if(e.getPROFILE_IMAGE() == null) {
        statusViewHolder.profile_image.setImageResource(R.drawable.ic_person);
    }
    else {
        //statusViewHolder.profile_image.setImageURI(Uri.parse(PrefStorage.getUser(context).getPROFILE_IMAGE()));
        GLib.downloadImage(context,e.getPROFILE_IMAGE()).into(statusViewHolder.profile_image);
    }
    }



    @Override
    public int getItemCount() {
        return this.statuses.size();
    }


    public static class StatusViewHolder extends RecyclerView.ViewHolder {

        ImageView profile_image;
        TextView username, statusDescription;
        ConstraintLayout layout;
        RatingBar ratingBar;
        TextView statusTime;

        public StatusViewHolder(@NonNull final View itemView, final Context context, final ArrayList<Status> st) {
            super(itemView);

            layout = itemView.findViewById(R.id.statusLayout);
            profile_image = itemView.findViewById(R.id.profile_image);
            username = itemView.findViewById(R.id.usernameTextView);
            statusDescription = itemView.findViewById(R.id.statusTextView);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            statusTime = itemView.findViewById(R.id.statusTimeTextView);


            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Status status = st.get(position);
                    int status_id = status.getSTATUS_ID();
                    Intent singleStatusAct = new Intent(context,StatusActivity.class);
                    singleStatusAct.putExtra("status_id",status_id);
                    singleStatusAct.putExtra("position",position);
                    singleStatusAct.putExtra("is_accepted",0);
                    singleStatusAct.putExtra("swap_id",0);
                    context.startActivity(singleStatusAct);
                }
            });

            profile_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Status status = st.get(position);
                    if(PrefStorage.isMe(context,status.getUSER_ID())){
                        Intent profileAct = new Intent(context,UserProfile.class);
                        profileAct.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(profileAct);
                    }else {
                        Intent profileAct = new Intent(context,NLUserProfile.class);
                        profileAct.putExtra("user_id",status.getUSER_ID());
                        profileAct.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(profileAct);
                    }
                }
            });
        }
    }

    public void notifyAdapter(ArrayList<Status> statuses){
        this.statuses = statuses;
        notifyDataSetChanged();
    }
}
