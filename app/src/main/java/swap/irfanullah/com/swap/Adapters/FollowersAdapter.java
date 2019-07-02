package swap.irfanullah.com.swap.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import swap.irfanullah.com.swap.Models.Followers;
import swap.irfanullah.com.swap.Models.User;
import swap.irfanullah.com.swap.R;

public class FollowersAdapter  extends RecyclerView.Adapter<FollowersAdapter.FollowersViewHolder> {

    private Context context;
    private ArrayList<Followers> followers;
    private User user;

    public FollowersAdapter(Context context, ArrayList<Followers> followers, User user) {
        this.context = context;
        this.followers = followers;
        this.user = user;
    }

    @NonNull
    @Override
    public FollowersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.users_f_row,viewGroup,false);
        return new FollowersViewHolder(view,context,followers);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowersViewHolder followersViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return followers.size();
    }

    public static class FollowersViewHolder extends RecyclerView.ViewHolder {
        public FollowersViewHolder(@NonNull View itemView, Context context, ArrayList<Followers> followers) {
            super(itemView);
        }
    }

    public void notifyAdapter(ArrayList<Followers> followers){
        this.followers = followers;
        notifyDataSetChanged();
    }
}
