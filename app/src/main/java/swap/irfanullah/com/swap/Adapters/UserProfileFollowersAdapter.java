package swap.irfanullah.com.swap.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import swap.irfanullah.com.swap.Libraries.GLib;
import swap.irfanullah.com.swap.Models.User;
import swap.irfanullah.com.swap.NLUserProfile;
import swap.irfanullah.com.swap.R;

public class UserProfileFollowersAdapter extends RecyclerView.Adapter<UserProfileFollowersAdapter.FollowHolder> {
    private Context context;
    private ArrayList<User> users;
    private static FollowListener mFollowListener;

    public UserProfileFollowersAdapter(Context context, ArrayList<User> users) {
        this.context = context;
        this.users = users;
    }

    @Override
    public FollowHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_profile_followers_custom_row,viewGroup,false);
        return new FollowHolder(view,context,users);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowHolder followHolder, int i) {
      //  contactHolder.name.setText(contacts.get(i).getCONTACT_NAME());
        User user = users.get(i);
        followHolder.name.setText(user.getFULL_NAME());

        if(user.getPROFILE_IMAGE() == null){
            followHolder.profile_image.setImageResource(R.drawable.ic_person);
        }else {
            GLib.downloadImage(context,user.getPROFILE_IMAGE()).into(followHolder.profile_image);
        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class FollowHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;
        ImageView profile_image;
        //Button followBtn;
        public FollowHolder(@NonNull View itemView, final Context context, final ArrayList<User> users) {
            super(itemView);
            name = itemView.findViewById(R.id.username);
            profile_image = itemView.findViewById(R.id.profile_image);
           // followBtn = itemView.findViewById(R.id.followBtn);
           // followBtn.setOnClickListener(this);

            profile_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    User user = users.get(getAdapterPosition());
                    Intent profileAct = new Intent(context, NLUserProfile.class);
                    profileAct.putExtra("user_id",user.getUSER_ID());
                    context.startActivity(profileAct);
                }
            });
        }

        @Override
        public void onClick(View v) {
            mFollowListener.onFollow(v,getAdapterPosition());
        }
    }

    public void notifyAdapter(ArrayList<User> users){
        this.users = users;
        notifyDataSetChanged();
    }

    public interface FollowListener{
        void onFollow(View v, int position);
    }

    public void setOnFollowClickListener(FollowListener followClickListener){
        UserProfileFollowersAdapter.mFollowListener = followClickListener;
    }
}
