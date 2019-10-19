package swap.irfanullah.com.swap.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;
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
import swap.irfanullah.com.swap.Models.Swap;
import swap.irfanullah.com.swap.Models.User;
import swap.irfanullah.com.swap.NLUserProfile;
import swap.irfanullah.com.swap.R;
import swap.irfanullah.com.swap.StatusActivity;
import swap.irfanullah.com.swap.Storage.PrefStorage;
import swap.irfanullah.com.swap.UserProfile;

public class BrowseStatusAdapter extends RecyclerView.Adapter<BrowseStatusAdapter.StatusViewHolder> {
    private Context context;
    private ArrayList<Status> statuses;
    private ArrayList<String> attachmentsArrayList;
    private User user;
    int STATUS_ID;
    MediaPager pager;
    private ArrayList<Attachments> mediaAttachments;
    public BrowseStatusAdapter(Context context, ArrayList<Status> st) {
        this.context = context;
        this.statuses = st;
        user = PrefStorage.getUser(this.context);
    }

    @NonNull
    @Override
    public StatusViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.status_custom_row,viewGroup,false);


        return new StatusViewHolder(view, this.context, this.statuses);
    }

    @Override
    public void onBindViewHolder(@NonNull StatusViewHolder statusViewHolder, int i) {
        Status e = statuses.get(i);
        statusViewHolder.statusDescription.setText(e.getSTATUS());
    statusViewHolder.ratingBar.setRating(e.getRATTING());
    statusViewHolder.statusTime.setText(TimeDiff.getTimeDifference(e.getTIME()));

//        statusViewHolder.likesCount.setText(e.getLIKESCOUNT().toString());
        statusViewHolder.likesCount.setText(e.getLIKESCOUNT());

        if(e.isLiked()){
            statusViewHolder.likeBtn.setImageResource(R.drawable.heartred);
        }else {
            statusViewHolder.likeBtn.setImageResource(R.drawable.likehear);
        }
        statusViewHolder.sharesCount.setText(e.getSHARESCOUNT());
        statusViewHolder.commentsCount.setText(e.getCOMMENTSCOUONT());
        ArrayList<Attachments> attachments = new ArrayList<>();
        pager = new MediaPager(context,attachments, Integer.toString(e.getSTATUS_ID()));
    if(e.getPROFILE_IMAGE() == null) {
        statusViewHolder.profile_image.setImageResource(R.drawable.ic_person);
    }
    else {
        //statusViewHolder.profile_image.setImageURI(Uri.parse(PrefStorage.getUser(context).getPROFILE_IMAGE()));
        GLib.downloadImage(context,e.getPROFILE_IMAGE()).into(statusViewHolder.profile_image);
    }
    //RMsg.logHere(Integer.toString(e.getHAS_ATTACHMENTS()));

    if(e.getHAS_ATTACHMENTS() == 1) {
        //loadStatusMedia(statusViewHolder,e.getATTACHMENTS(),e.getSTATUS_ID());
        updatePager(e.getATTACHMENTS(),Integer.toString(e.getSTATUS_ID()),statusViewHolder.indicator,statusViewHolder.mediaView,e.getSTATUS_ID());

    }else {
        statusViewHolder.mediaView.setVisibility(View.GONE);
    }


    }



    @Override
    public int getItemCount() {
        return this.statuses.size();
    }


    public static class StatusViewHolder extends RecyclerView.ViewHolder {
        ImageView profile_image, swapWithBtn, likeBtn, commentBtn, shareBtn;
        TextView username, statusDescription, likesCount, sharesCount, commentsCount;
        ConstraintLayout layout;
        RatingBar ratingBar;
        TextView statusTime;
        ViewPager mediaView;
        ProgressBar mediaProgressBar;
        Context context;
        RelativeLayout rl;
        CircleIndicator indicator;



        public StatusViewHolder(@NonNull final View itemView, final Context context, final ArrayList<Status> st) {
            super(itemView);
            this.context = context;
            layout = itemView.findViewById(R.id.statusLayout);
            profile_image = itemView.findViewById(R.id.profile_image);
            username = itemView.findViewById(R.id.usernameTextView);
            statusDescription = itemView.findViewById(R.id.statusTextView);

            ratingBar = itemView.findViewById(R.id.ratingBar);
            swapWithBtn = itemView.findViewById(R.id.swapPlusIvBtn);
            statusTime = itemView.findViewById(R.id.statusTimeTextView);
            mediaView = itemView.findViewById(R.id.gridViewStatus);
         //   mediaProgressBar = itemView.findViewById(R.id.mediaProgressBar);
            rl = itemView.findViewById(R.id.rl);
            indicator = itemView.findViewById(R.id.indicator);

            likeBtn = itemView.findViewById(R.id.likeBtn);
            commentBtn = itemView.findViewById(R.id.commentBtn);
            shareBtn = itemView.findViewById(R.id.shareBtn);
            likesCount = itemView.findViewById(R.id.likeCountTextView);
            sharesCount = itemView.findViewById(R.id.shareCountTextView);
            commentsCount = itemView.findViewById(R.id.commentsCountTextView);

            swapWithBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int status_id = st.get(getAdapterPosition()).getSTATUS_ID();
                    int user_id = st.get(getAdapterPosition()).getUSER_ID();
                    RMsg.ilogHere(status_id);
                    RMsg.ilogHere(user_id);
                    requestForStatusSwap(context,user_id,status_id);
//                    Intent swapWith = new Intent(context,SwapWithActivity.class);
//                    swapWith.putExtra("status_id",status_id);
//                    context.startActivity(swapWith);
                }
            });

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
                    singleStatusAct.putExtra("is_browse_status",1);
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




            likeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Status status = st.get(position);
                    ImageView iv = v.findViewById(R.id.likeBtn);
                    //TextView likescount = v.findViewById(R.id.likeCountTextView);
                    likeOrDislikeStatusRequest(Integer.toString(status.getSTATUS_ID()), PrefStorage.getUser(context).getTOKEN(), iv,likesCount);
                }
            });

            shareBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Status status = st.get(position);
                    shareStatusRequest(Integer.toString(status.getSTATUS_ID()),PrefStorage.getUser(context).getTOKEN());
                }
            });


//            mediaView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
        }

        private void requestForStatusSwap(final Context context, final int user_id, final int status_id){
            new AsyncTask<Void,Void,Void>(){
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    //Toast.makeText(context,"loading...",Toast.LENGTH_LONG).show();
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    // Toast.makeText(context,"Done.",Toast.LENGTH_LONG).show();

                }

                @Override
                protected Void doInBackground(Void... voids) {
                    RetroLib.geApiService().swapStatus(PrefStorage.getUser(context).getTOKEN(), user_id, status_id).enqueue(new Callback<Swap>() {
                        @Override
                        public void onResponse(Call<Swap> call, Response<Swap> response) {
                            Swap swap = response.body();
                            if (swap.getAuthenticated()) {
                                if (swap.getError()) {
                                    Toast.makeText(context, swap.getMESSAGE(), Toast.LENGTH_LONG).show();
                                    //buttonView.setChecked(false);
                                } else if (swap.getAlready()) {
                                    Toast.makeText(context, swap.getMESSAGE(), Toast.LENGTH_LONG).show();
                                } else if (swap.getSwaped()) {
                                    Toast.makeText(context, swap.getMESSAGE(), Toast.LENGTH_LONG).show();
                                } else if (swap.getEmpty()) {
                                    Toast.makeText(context, swap.getMESSAGE(), Toast.LENGTH_LONG).show();
                                    // buttonView.setChecked(false);
                                }
                            } else {
                                Toast.makeText(context, swap.getMESSAGE(), Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Swap> call, Throwable t) {
                            Toast.makeText(context, t.toString(), Toast.LENGTH_LONG).show();

                        }
                    });
                    return  null;
                }
            }.execute();

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
    }

    public void notifyAdapter(ArrayList<Status> statuses){
        this.statuses = statuses;
        notifyDataSetChanged();
    }

    private void loadStatusMedia(final StatusViewHolder viewHolder,  String attachments, int status_id){
        ArrayList<Attachments> mediaAttachments = new ArrayList<>();
        StatusFragGridAdapter statusFragGridAdapter = new StatusFragGridAdapter(context,mediaAttachments,status_id);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context,4);
//        viewHolder.mediaView.setHasFixedSize(true);
//        viewHolder.mediaView.setLayoutManager(layoutManager);
//        viewHolder.mediaView.setAdapter(statusFragGridAdapter);
//        viewHolder.mediaView.setVisibility(View.VISIBLE);

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

    private void updatePager(String attachments, String st_id, CircleIndicator indicator , ViewPager mediaPager, int status_id){

        mediaPager.setVisibility(View.VISIBLE);
        ArrayList<Attachments> mediaAttachments = new ArrayList<>();
        pager = new MediaPager(context,mediaAttachments,Integer.toString(status_id));
        Gson gson = new Gson();
        JsonElement json = gson.fromJson(attachments,JsonElement.class);

        if(json.isJsonObject()) {
            JsonObject object = json.getAsJsonObject();
            Attachments att = gson.fromJson(object, Attachments.class);
            mediaAttachments.add(att);
            mediaPager.setAdapter(pager);
            indicator.setViewPager(mediaPager);
            pager.notifyAdapter(mediaAttachments);
            //pager.setIndicator(mediaPager);

            // GLib.downloadImage(context, att.getATTACHMENT_URL()).into(statusMedia);

            RMsg.logHere("Single: "+att.getATTACHMENT_URL());
        }else if(json.isJsonArray()){
            JsonArray jsonArray = json.getAsJsonArray();
            //Attachments att = gson.fromJson(object, Attachments.class);
            Type type = new TypeToken<ArrayList<Attachments>>(){}.getType();
            ArrayList<Attachments> arrayList = gson.fromJson(jsonArray,type);
            pager = new MediaPager(context,arrayList,Integer.toString(status_id));
            mediaPager.setAdapter(pager);
            indicator.setViewPager(mediaPager);
            // pager.setIndicator(mediaPager);
            //   indicator.setViewPager(mediaPager);


            //indicator.setViewPager(viewPager);
            //pager.notifyAdapter(arrayList);
//
//            RMsg.logHere("working");
//            RMsg.logHere("multiple: "+Integer.toString(pager.getCount()));

        }
    }



}
