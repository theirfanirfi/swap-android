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
import swap.irfanullah.com.swap.NLUserProfile;
import swap.irfanullah.com.swap.R;
import swap.irfanullah.com.swap.Storage.PrefStorage;
import swap.irfanullah.com.swap.UserProfile;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.StatusViewHolder> {
    private Context context;
    private ArrayList<Followers> followers;
    public static TagClickListener tagClickListener;

    public TagAdapter(Context context, ArrayList<Followers> followers) {
        this.context = context;
        this.followers = followers;
    }

    @NonNull
    @Override
    public StatusViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.tag_adapter_custom_row,viewGroup,false);


        return new StatusViewHolder(view, this.context, this.followers);
    }

    @Override
    public void onBindViewHolder(@NonNull StatusViewHolder statusViewHolder, int i) {

        Followers follower = this.followers.get(i);
       statusViewHolder.name.setText(follower.getFULLNAME());
       if(follower.getPROFILE_IMAGE() == null){
           statusViewHolder.profile_image.setImageResource(R.drawable.ic_person);
       }else {
           GLib.downloadImage(context,follower.getPROFILE_IMAGE()).into(statusViewHolder.profile_image);
       }

//       for(Swap item : this.swaps){
//           if(item.getSWAPED_WITH_USER_ID() == follower.getFOLLOWED_USER_ID()){
//               //this.swaps.get(i).getSWAP_ID() > 0 ? true : false
//               statusViewHolder.sendBtn.setChecked(true);
//               break;
//           }
//       }
    }



    @Override
    public int getItemCount() {
        return this.followers.size();
    }


    public static class StatusViewHolder extends RecyclerView.ViewHolder {
        Button tagBtn;
        TextView name;
        ImageView profile_image;
        public StatusViewHolder(@NonNull View itemView, final Context context, final ArrayList<Followers> followers) {
            super(itemView);
            tagBtn = itemView.findViewById(R.id.tagBtn);
            name = itemView.findViewById(R.id.usernameTextView);
            profile_image = itemView.findViewById(R.id.profile_image);

            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Followers f = followers.get(position);
                    //Log.i(RMsg.LOG_MESSAGE,Integer.toString(f.getFOLLOWED_USER_ID()));
                    if(PrefStorage.isMe(context,f.getFOLLOWED_USER_ID())){
                        Intent profileAct = new Intent(context,UserProfile.class);
                        profileAct.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(profileAct);
                    }else {
                        Intent profileAct = new Intent(context,NLUserProfile.class);
                        profileAct.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        profileAct.putExtra("user_id",f.getFOLLOWED_USER_ID());
                        context.startActivity(profileAct);
                    }
                }
            });

            profile_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Followers f = followers.get(position);
                    //Log.i(RMsg.LOG_MESSAGE,Integer.toString(f.getFOLLOWED_USER_ID()));
                    if(PrefStorage.isMe(context,f.getFOLLOWED_USER_ID())){
                        Intent profileAct = new Intent(context,UserProfile.class);
                        profileAct.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(profileAct);
                    }else {
                        Intent profileAct = new Intent(context,NLUserProfile.class);
                        profileAct.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        profileAct.putExtra("user_id",f.getFOLLOWED_USER_ID());
                        context.startActivity(profileAct);
                    }
                }
            });




            tagBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Followers f = followers.get(position);
                    final int user_id = f.getFOLLOWED_USER_ID();
                    tagClickListener.onTagClicked(user_id,tagBtn);
                }
            });
        }
    }


    public void FilterRV(ArrayList<Followers> followers)
    {
        this.followers = followers;
        notifyDataSetChanged();
    }

    public interface TagClickListener {
        void onTagClicked(int followed_user_id, Button button);
    }

    public void setOnTagClickListener(TagClickListener onTagClickListener){
        tagClickListener = onTagClickListener;
    }
}
