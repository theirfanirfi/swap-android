package swap.irfanullah.com.swap.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import swap.irfanullah.com.swap.Libraries.GLib;
import swap.irfanullah.com.swap.Libraries.RetroLib;
import swap.irfanullah.com.swap.Libraries.TimeDiff;
import swap.irfanullah.com.swap.Models.Attachments;
import swap.irfanullah.com.swap.Models.Like;
import swap.irfanullah.com.swap.Models.RMsg;
import swap.irfanullah.com.swap.Models.Share;
import swap.irfanullah.com.swap.Models.Status;
import swap.irfanullah.com.swap.Models.SwapsTab;
import swap.irfanullah.com.swap.Models.User;
import swap.irfanullah.com.swap.NLUserProfile;
import swap.irfanullah.com.swap.R;
import swap.irfanullah.com.swap.StatusActivity;
import swap.irfanullah.com.swap.Storage.PrefStorage;
import swap.irfanullah.com.swap.UserProfile;

public class SwapsAdapter extends RecyclerView.Adapter<SwapsAdapter.StatusViewHolder> {
    private Context context, ctx;
    private ArrayList<SwapsTab> swapsTabArrayList;
    private final static String REPRESENTING_LOGGED_USER_IN_TAB = "You";
    private int STATUS_ID = 0;

    public SwapsAdapter(Context context, ArrayList<SwapsTab> swapsTabArrayList) {
        this.context = context;
        this.swapsTabArrayList = swapsTabArrayList;
    }

    @NonNull
    @Override
    public StatusViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.swap_custom_row, viewGroup, false);


        return new StatusViewHolder(view, this.context, this.swapsTabArrayList, STATUS_ID);
    }

    @Override
    public void onBindViewHolder(@NonNull StatusViewHolder statusViewHolder, int i) {
        SwapsTab swap = this.swapsTabArrayList.get(i);


        statusViewHolder.likesCount.setText(swap.getLIKESCOUNT().toString());

        if(swap.isLiked()){
            statusViewHolder.likeBtn.setImageResource(R.drawable.heartred);
        }else {
            statusViewHolder.likeBtn.setImageResource(R.drawable.likehear);
        }

        statusViewHolder.sharesCount.setText(swap.getSHARESCOUNT().toString());
        statusViewHolder.commentsCount.setText(swap.getCOMMENTSCOUONT().toString());

        if (swap.getIS_ME()) {
            statusViewHolder.username.setText(REPRESENTING_LOGGED_USER_IN_TAB);
            statusViewHolder.withTextV.setText(swap.getSWAPED_WITH_FULLNAME());



            User user = PrefStorage.getUser(context);
            if(user.getPROFILE_IMAGE() == null){
                statusViewHolder.profile_image.setImageResource(R.drawable.ic_person);
            }else {
                GLib.downloadImage(context,user.getPROFILE_IMAGE()).into(statusViewHolder.profile_image);
            }
        } else {
            statusViewHolder.username.setText(swap.getPOSTER_FULLNAME());
            statusViewHolder.withTextV.setText(REPRESENTING_LOGGED_USER_IN_TAB);
            if(swap.getPOSTER_PROFILE_IMAGE() == null){
                statusViewHolder.profile_image.setImageResource(R.drawable.ic_person);
            }else {
                GLib.downloadImage(context,swap.getPOSTER_PROFILE_IMAGE()).into(statusViewHolder.profile_image);
            }
        }

        statusViewHolder.statusDescription.setText(swap.getSTATUS());
        statusViewHolder.ratingBar.setRating(swap.getAVG_RATTING());
        statusViewHolder.swapTime.setText(TimeDiff.getTimeDifference(swap.getSWAP_DATE()));


        if(swap.getHAS_ATTACHMENTS() == 1) {
            loadStatusMedia(statusViewHolder,swap.getATTACHMENTS(), swap.getSTATUS_ID());
        }else {
            statusViewHolder.mediaView.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount() {
        return this.swapsTabArrayList.size();
    }


    public static class StatusViewHolder extends RecyclerView.ViewHolder {
        ImageView profile_image, unswap,likeBtn, commentBtn, shareBtn;
        TextView username, statusDescription, withTextV, swapTime, likesCount, sharesCount, commentsCount;
        ConstraintLayout layout;
        RatingBar ratingBar;
        RecyclerView mediaView;
        Context context;

        public StatusViewHolder(@NonNull View itemView, final Context context, final ArrayList<SwapsTab> swapsTabs, int status_id) {
            super(itemView);
            this.context = context;
            layout = itemView.findViewById(R.id.statusLayout);
            profile_image = itemView.findViewById(R.id.profile_image);
            username = itemView.findViewById(R.id.usernameTextView);
            withTextV = itemView.findViewById(R.id.withTextView);
            statusDescription = itemView.findViewById(R.id.statusTextView);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            swapTime = itemView.findViewById(R.id.statusTimeTextView);
            layout = itemView.findViewById(R.id.statusLayout);
            mediaView = itemView.findViewById(R.id.gridViewStatus);


            likeBtn = itemView.findViewById(R.id.likeBtn);
            commentBtn = itemView.findViewById(R.id.commentBtn);

            shareBtn = itemView.findViewById(R.id.imageView5);

            likesCount = itemView.findViewById(R.id.likeCountTextView);
            sharesCount = itemView.findViewById(R.id.shareCountTextView);
            commentsCount = itemView.findViewById(R.id.CommentCountTextView);

            //unswap = itemView.findViewById(R.id.cancelViewImgBtn);

                gotoProfile(context,swapsTabs);
            //rate the status

            ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBarr, float rating, boolean fromUser) {
                    if (fromUser) {
                        int position = getAdapterPosition();
                        SwapsTab swap = swapsTabs.get(position);


                        RetroLib.geApiService().rateStatus(PrefStorage.getUser(context).getTOKEN(), swap.getSTATUS_ID(), rating).enqueue(new Callback<Status>() {
                            @Override
                            public void onResponse(Call<Status> call, Response<Status> response) {

                                if (response.isSuccessful()) {

                                    Status status = response.body();
                                    if (status.getAuthenticated()) {
                                        if (status.getIS_EMPTY()) {
                                            Toast.makeText(context, status.getMESSAGE(), Toast.LENGTH_LONG).show();
                                        } else if (status.getIS_RATED()) {
                                            Toast.makeText(context, status.getMESSAGE(), Toast.LENGTH_LONG).show();
                                        } else if (status.getIS_ALREADY_RATED()) {
                                            Toast.makeText(context, status.getMESSAGE(), Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(context, status.getMESSAGE(), Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        Toast.makeText(context, RMsg.AUTH_ERROR_MESSAGE, Toast.LENGTH_LONG).show();
                                    }

                                } else {
                                    Toast.makeText(context, RMsg.REQ_ERROR_MESSAGE, Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Status> call, Throwable t) {
                                Toast.makeText(context, t.toString(), Toast.LENGTH_LONG).show();

                            }
                        });

                    }
                }
            });

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    SwapsTab swap = swapsTabs.get(position);
                    int status_id = swap.getSTATUS_ID();
                    int is_accepted = swap.getIS_ACCEPTED();
                    int swap_id = swap.getSWAP_ID();
                    Intent singleStatusAct = new Intent(context,StatusActivity.class);
                    singleStatusAct.putExtra("status_id",status_id);
                    singleStatusAct.putExtra("position",position);
                    singleStatusAct.putExtra("is_accepted",is_accepted);
                    singleStatusAct.putExtra("swap_id",swap_id);
                    context.startActivity(singleStatusAct);
                    Log.i(RMsg.LOG_MESSAGE,Integer.toString(swap_id)+" : "+Integer.toString(swap.getIS_ACCEPTED()));
                }
            });

            likeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    SwapsTab swap = swapsTabs.get(position);
                    ImageView iv = v.findViewById(R.id.likeBtn);
                    //TextView likescount = v.findViewById(R.id.likeCountTextView);
                    likeOrDislikeStatusRequest(Integer.toString(swap.getSTATUS_ID()), PrefStorage.getUser(context).getTOKEN(), iv,likesCount);
                }
            });

            shareBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    SwapsTab swap = swapsTabs.get(position);
                    shareStatusRequest(Integer.toString(swap.getSTATUS_ID()),PrefStorage.getUser(context).getTOKEN());
                }
            });

        }

        public void likeOrDislikeStatusRequest(String status_id, String token, final ImageView iv, final TextView likesCountTextView) {
            RetroLib.geApiService().likeOrDislikeStatus(token, status_id).enqueue(new Callback<Like>() {
                @Override
                public void onResponse(Call<Like> call, Response<Like> response) {
                    if (response.isSuccessful()) {
                        Like like = response.body();
                        if (like.isError() || !like.isAuthenticated()) {
                            RMsg.toastHere(context, like.getMessage());
                            RMsg.logHere(like.getMessage());
                        } else if(like.isLiked()) {
                            iv.setImageResource(R.drawable.heartred);
                            likesCountTextView.setText(Integer.toString(like.getStatusLikes()));
                            RMsg.toastHere(context, like.getMessage());
                            RMsg.logHere(like.getMessage());
                        }else if(like.isUnliked()){
                            iv.setImageResource(R.drawable.likehear);
                            likesCountTextView.setText(Integer.toString(like.getStatusLikes()));
                            RMsg.toastHere(context, like.getMessage());
                            RMsg.logHere(like.getMessage());
                        }else {
                            RMsg.toastHere(context, like.getMessage());
                            RMsg.logHere(like.getMessage());
                        }
                    } else {
                        RMsg.toastHere(context, response.raw().toString());
                        RMsg.logHere(response.raw().toString());
                    }
                }

                @Override
                public void onFailure(Call<Like> call, Throwable t) {
                    RMsg.toastHere(context, t.getMessage());
                    RMsg.logHere(t.getMessage());
                }
            });
        }


        public void shareStatusRequest(String status_id, String token) {
            RetroLib.geApiService().shareStatus(token, status_id).enqueue(new Callback<Share>() {
                @Override
                public void onResponse(Call<Share> call, Response<Share> response) {
                    if (response.isSuccessful()) {
                        Share share = response.body();
                        if (share.isError() || !share.isAuthenticated()) {
                            RMsg.toastHere(context, share.getMessage());
                            RMsg.logHere(share.getMessage());
                        } else {
                            RMsg.toastHere(context, share.getMessage());
                            RMsg.logHere(share.getMessage());
                        }
                    } else {
                        RMsg.toastHere(context, response.raw().toString());
                        RMsg.logHere(response.raw().toString());
                    }
                }

                @Override
                public void onFailure(Call<Share> call, Throwable t) {
                    RMsg.toastHere(context, t.getMessage());
                    RMsg.logHere(t.getMessage());
                }
            });
        }

        private void gotoProfile(final Context context, final ArrayList<SwapsTab> swapsTabs) {

            profile_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    final SwapsTab swap = swapsTabs.get(position);

                    Log.i(RMsg.LOG_MESSAGE,Integer.toString(swap.getPOSTER_USER_ID()));

                    if(PrefStorage.isMe(context,swap.getPOSTER_USER_ID())){

                        Intent profileAct = new Intent(context,UserProfile.class);
                        profileAct.putExtra("user_id",swap.getPOSTER_USER_ID());
                        profileAct.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        context.startActivity(profileAct);
                    }else {
                        Intent profileAct = new Intent(context,NLUserProfile.class);
                        profileAct.putExtra("user_id",swap.getPOSTER_USER_ID());
                        profileAct.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(profileAct);
                    }
                }
            });
        }
    }

    public void notifySwapsAdapter(ArrayList<SwapsTab> swapsTabs) {
        this.swapsTabArrayList = swapsTabs;
        notifyDataSetChanged();
    }

    public void notifyRemovSwapsAdapter() {
        notifyDataSetChanged();
    }

    private void loadStatusMedia(final SwapsAdapter.StatusViewHolder viewHolder, String attachments, int status_id){
        RMsg.logHere("SWAPS: "+attachments);

        ArrayList<Attachments> mediaAttachments = new ArrayList<>();
        StatusFragGridAdapter statusFragGridAdapter = new StatusFragGridAdapter(context,mediaAttachments, status_id);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context,4);
        viewHolder.mediaView.setHasFixedSize(true);
        viewHolder.mediaView.setLayoutManager(layoutManager);
        viewHolder.mediaView.setAdapter(statusFragGridAdapter);
        viewHolder.mediaView.setVisibility(View.VISIBLE);

        Gson gson = new Gson();
        JsonElement json = gson.fromJson(attachments,JsonElement.class);
        if(json.isJsonObject()) {
            JsonObject object = json.getAsJsonObject();
            Attachments att = gson.fromJson(object, Attachments.class);
            mediaAttachments.add(att);
            statusFragGridAdapter.notifyAdapter(mediaAttachments);
            // GLib.downloadImage(context, att.getATTACHMENT_URL()).into(statusMedia);

            RMsg.logHere("Single: "+att.getATTACHMENT_URL());
        }else if(json.isJsonArray()){
            JsonArray jsonArray = json.getAsJsonArray();
            //Attachments att = gson.fromJson(object, Attachments.class);
            Type type = new TypeToken<ArrayList<Attachments>>(){}.getType();
            ArrayList<Attachments> arrayList = gson.fromJson(jsonArray,type);
            statusFragGridAdapter.notifyAdapter(arrayList);
            RMsg.logHere("working");
        }
    }

}
