package swap.irfanullah.com.swap.Adapters;

import android.content.Context;
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
import swap.irfanullah.com.swap.R;

public class SearchFragmentUsersAdapter extends RecyclerView.Adapter<SearchFragmentUsersAdapter.SearchUsersHolder> {
    private Context context;
    public ArrayList<User> users;
    static FollowListener mFollowListener;
    public SearchFragmentUsersAdapter(Context context, ArrayList<User> users) {
        this.context = context;
        this.users = users;
    }

    @Override
    public SearchUsersHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_users_custom_row,viewGroup,false);
        return new SearchUsersHolder(view,context,users);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchUsersHolder searchUsersHolder, int i) {
      //  contactHolder.name.setText(contacts.get(i).getCONTACT_NAME());
        User user = users.get(i);

        searchUsersHolder.name.setText(user.getFULL_NAME());

        if(user.getPROFILE_IMAGE() != null){
            GLib.downloadImage(context,user.getPROFILE_IMAGE()).into(searchUsersHolder.profile_image);
        }

        if(user.getFID() > 0){
            searchUsersHolder.followBtn.setText("Unfollow");
        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class SearchUsersHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;
        Button followBtn;
        ImageView profile_image;
        ArrayList<User> ViewHolderUser;
        public SearchUsersHolder(@NonNull View itemView, final Context context, final ArrayList<User> users) {
            super(itemView);
            name = itemView.findViewById(R.id.username);
            followBtn = itemView.findViewById(R.id.followBtn);
            profile_image = itemView.findViewById(R.id.profile_image);
            followBtn.setOnClickListener(this);
            ViewHolderUser = users;
        }

        @Override
        public void onClick(View v) {
            User user = ViewHolderUser.get(getAdapterPosition());
            mFollowListener.onFollow(v,getAdapterPosition(),user);
        }
    }

    public void notifyAdapter(ArrayList<User> users){
        this.users = users;
        notifyDataSetChanged();
    }

    public interface FollowListener{
        void onFollow(View v, int position, User user);
    }

    public void setOnFollowClickListener(FollowListener followClickListener){
        SearchFragmentUsersAdapter.mFollowListener = followClickListener;
    }
}
