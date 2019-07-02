package swap.irfanullah.com.swap.StatusActivityFrags;

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

import java.util.ArrayList;

import swap.irfanullah.com.swap.Libraries.GLib;
import swap.irfanullah.com.swap.Libraries.TimeDiff;
import swap.irfanullah.com.swap.Models.Comment;
import swap.irfanullah.com.swap.NLUserProfile;
import swap.irfanullah.com.swap.R;
import swap.irfanullah.com.swap.Storage.PrefStorage;
import swap.irfanullah.com.swap.UserProfile;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.StatusViewHolder> {
    private Context context;
    private ArrayList<Comment> comments;
    public CommentsAdapter(Context context, ArrayList<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public StatusViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.comments_custom_row,viewGroup,false);
        return new StatusViewHolder(view, this.context,this.comments);
    }


    @Override
    public void onBindViewHolder(@NonNull StatusViewHolder statusViewHolder, int i) {
        Comment comment = comments.get(i);
        statusViewHolder.username.setText(comment.getUSERNAME());
        statusViewHolder.statusDescription.setText(comment.getCOMMENT());
        statusViewHolder.commentTime.setText(TimeDiff.getTimeDifference(comment.getTIME()));



    if(comment.getPROFILE_IMAGE() == null) {
        statusViewHolder.profile_image.setImageResource(R.drawable.ic_person);
    }
    else {
        //statusViewHolder.profile_image.setImageURI(Uri.parse(PrefStorage.getUser(context).getPROFILE_IMAGE()));
        GLib.downloadImage(context,comment.getPROFILE_IMAGE()).into(statusViewHolder.profile_image);
    }
    //RMsg.logHere(Integer.toString(e.getHAS_ATTACHMENTS()));



    }



    @Override
    public int getItemCount() {
        return this.comments.size();
    }


    public static class StatusViewHolder extends RecyclerView.ViewHolder {
        ImageView profile_image;
        TextView username, statusDescription, commentTime;
        ConstraintLayout layout;
        ArrayList<Comment> comments;

        public StatusViewHolder(@NonNull final View itemView, final Context context, final ArrayList<Comment> comments) {
            super(itemView);
            this.comments = comments;
            layout = itemView.findViewById(R.id.statusLayout);
            profile_image = itemView.findViewById(R.id.profile_image);
            username = itemView.findViewById(R.id.usernameTextView);
            statusDescription = itemView.findViewById(R.id.statusTextView);
            commentTime = itemView.findViewById(R.id.statusTimeTextView);

            profile_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Comment comment = comments.get(position);
                    if (PrefStorage.isMe(context, comment.getUSER_ID())) {
                        Intent profileAct = new Intent(context, UserProfile.class);
                        profileAct.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(profileAct);
                    } else {
                        Intent profileAct = new Intent(context, NLUserProfile.class);
                        profileAct.putExtra("user_id", comment.getUSER_ID());
                        profileAct.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(profileAct);
                    }
                }
            });




        }



    }
    public void notifyAdapter(ArrayList<Comment> comments){
        this.comments = comments;
        notifyDataSetChanged();
    }


}
