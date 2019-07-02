package swap.irfanullah.com.swap.StatusActivityFrags;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;

import java.util.ArrayList;

import swap.irfanullah.com.swap.Libraries.GLib;
import swap.irfanullah.com.swap.Models.Status;
import swap.irfanullah.com.swap.Models.User;
import swap.irfanullah.com.swap.NLUserProfile;
import swap.irfanullah.com.swap.R;
import swap.irfanullah.com.swap.Storage.PrefStorage;
import swap.irfanullah.com.swap.UserProfile;

public class RatersAdapter extends RecyclerView.Adapter<RatersAdapter.StatusViewHolder> {

    private ArrayList<User> raters;
    private Context context;
    public RatersAdapter(Context context, ArrayList<User> raters) {
        this.context = context;
        this.raters = raters;
    }

    @NonNull
    @Override
    public StatusViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_status_custom_row,viewGroup,false);

        return new StatusViewHolder(view,context, raters);
    }

    @Override
    public void onBindViewHolder(@NonNull StatusViewHolder statusViewHolder, int i) {
        User user = this.raters.get(i);
    statusViewHolder.fullname.setText(user.getFULL_NAME());
    statusViewHolder.stars.setText(Float.toString(user.getRATING())+ " stars");
//    if(status.getUSER_ID() == user.getUSER_ID()) {
//        statusViewHolder.authorBtn.setVisibility(View.VISIBLE);
//    }else {
//        statusViewHolder.authorBtn.setVisibility(View.GONE);
//
//    }

    if(user.getPROFILE_IMAGE() == null){
        statusViewHolder.profile_image.setImageResource(R.drawable.ic_person);
    }else {
        GLib.downloadImage(context,user.getPROFILE_IMAGE()).into(statusViewHolder.profile_image);
    }

    }


    @Override
    public int getItemCount() {
        return this.raters.size();
    }


    public static class StatusViewHolder extends RecyclerView.ViewHolder {
        TextView fullname, stars;
        ImageView profile_image;
        BootstrapButton authorBtn;
        public StatusViewHolder(@NonNull View itemView, final Context context, final ArrayList<User> raters) {
            super(itemView);
            fullname = itemView.findViewById(R.id.fullNameTextView);
            stars = itemView.findViewById(R.id.starsGivenTextView);
            profile_image = itemView.findViewById(R.id.profile_image);
          //  authorBtn = itemView.findViewById(R.id.authorBtn);

            profile_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    User user = raters.get(position);
                    if(PrefStorage.isMe(context,user.getUSER_ID())){
                        Intent profileAct = new Intent(context,UserProfile.class);
                        context.startActivity(profileAct);
                    }else {
                        Intent profileAct = new Intent(context,NLUserProfile.class);
                        profileAct.putExtra("user_id",user.getUSER_ID());
                        context.startActivity(profileAct);
                    }
                }
            });

            fullname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    User user = raters.get(position);
                    if(PrefStorage.isMe(context,user.getUSER_ID())){
                        Intent profileAct = new Intent(context,UserProfile.class);
                        profileAct.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(profileAct);
                    }else {
                        Intent profileAct = new Intent(context,NLUserProfile.class);
                        profileAct.putExtra("user_id",user.getUSER_ID());
                        profileAct.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(profileAct);
                    }
                }
            });
        }
    }

    public void notifyAdapter(ArrayList<User> raters){
        this.raters = raters;
        notifyDataSetChanged();
    }
}
