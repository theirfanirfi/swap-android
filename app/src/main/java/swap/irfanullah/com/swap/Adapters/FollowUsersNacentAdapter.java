package swap.irfanullah.com.swap.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import swap.irfanullah.com.swap.Models.PhoneContact;
import swap.irfanullah.com.swap.Models.User;
import swap.irfanullah.com.swap.R;

public class FollowUsersNacentAdapter extends RecyclerView.Adapter<FollowUsersNacentAdapter.FollowHolder> {
    private Context context;
    private ArrayList<User> users;
    private static FollowListener mFollowListener;

    public FollowUsersNacentAdapter(Context context, ArrayList<User> users) {
        this.context = context;
        this.users = users;
    }

    @Override
    public FollowHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.follow_nacent_row,viewGroup,false);
        return new FollowHolder(view,context,users);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowHolder followHolder, int i) {
      //  contactHolder.name.setText(contacts.get(i).getCONTACT_NAME());
        followHolder.name.setText(users.get(i).getFULL_NAME());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class FollowHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;
        Button followBtn;
        public FollowHolder(@NonNull View itemView, final Context context, final ArrayList<User> users) {
            super(itemView);
            name = itemView.findViewById(R.id.username);
            followBtn = itemView.findViewById(R.id.followBtn);
            followBtn.setOnClickListener(this);
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
        FollowUsersNacentAdapter.mFollowListener = followClickListener;
    }
}
