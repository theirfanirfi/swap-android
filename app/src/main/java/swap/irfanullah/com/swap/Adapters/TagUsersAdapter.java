package swap.irfanullah.com.swap.Adapters;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
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
import swap.irfanullah.com.swap.Models.Followers;
import swap.irfanullah.com.swap.Models.User;
import swap.irfanullah.com.swap.NLUserProfile;
import swap.irfanullah.com.swap.R;
import swap.irfanullah.com.swap.Storage.PrefStorage;
import swap.irfanullah.com.swap.UserProfile;

public class TagUsersAdapter extends RecyclerView.Adapter<TagUsersAdapter.StatusViewHolder> {
    private Context context;
    private ArrayList<User> users;
    public static TagClickListener taggClickListener;

    public TagUsersAdapter(Context context, ArrayList<User> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public StatusViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.status_tag_users,viewGroup,false);


        return new StatusViewHolder(view, this.context, this.users);
    }

    @Override
    public void onBindViewHolder(@NonNull StatusViewHolder statusViewHolder, int i) {

        User user = this.users.get(i);
       statusViewHolder.username.setText(user.getFULL_NAME());
      // statusViewHolder.user_id.setText(Integer.toString(user.getUSER_ID()));
    }



    @Override
    public int getItemCount() {
        return this.users.size();
    }


    public static class StatusViewHolder extends RecyclerView.ViewHolder {
        Button unTagBtn;
        TextView username,user_id;
        public StatusViewHolder(@NonNull View itemView, final Context context, final ArrayList<User> users) {
            super(itemView);
            unTagBtn = itemView.findViewById(R.id.removeFromTagBtn);
            username = itemView.findViewById(R.id.username);
//            user_id = itemView.findViewById(R.id.user_id);

//            name.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int position = getAdapterPosition();
//                    Followers f = followers.get(position);
//                    //Log.i(RMsg.LOG_MESSAGE,Integer.toString(f.getFOLLOWED_USER_ID()));
//                    if(PrefStorage.isMe(context,f.getFOLLOWED_USER_ID())){
//                        Intent profileAct = new Intent(context,UserProfile.class);
//                        profileAct.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        context.startActivity(profileAct);
//                    }else {
//                        Intent profileAct = new Intent(context,NLUserProfile.class);
//                        profileAct.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        profileAct.putExtra("user_id",f.getFOLLOWED_USER_ID());
//                        context.startActivity(profileAct);
//                    }
//                }
//            });
//
//            profile_image.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int position = getAdapterPosition();
//                    Followers f = followers.get(position);
//                    //Log.i(RMsg.LOG_MESSAGE,Integer.toString(f.getFOLLOWED_USER_ID()));
//                    if(PrefStorage.isMe(context,f.getFOLLOWED_USER_ID())){
//                        Intent profileAct = new Intent(context,UserProfile.class);
//                        profileAct.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        context.startActivity(profileAct);
//                    }else {
//                        Intent profileAct = new Intent(context,NLUserProfile.class);
//                        profileAct.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        profileAct.putExtra("user_id",f.getFOLLOWED_USER_ID());
//                        context.startActivity(profileAct);
//                    }
//                }
//            });




//            inviteBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    int position = getAdapterPosition();
//                    Followers f = followers.get(position);
//                    final int user_id = f.getFOLLOWED_USER_ID();
//                    inviteClickListener.onInvite(user_id,inviteBtn);
//                }
//            });
        }
    }


    public void FilterRV(ArrayList<User> users)
    {
        this.users = users;
        notifyDataSetChanged();
    }

    public interface TagClickListener {
        void onTag(int followed_user_id, Button button);
    }

    public void setOnTagClickListener(TagClickListener tagClickListener){
        taggClickListener = tagClickListener;
    }
}
