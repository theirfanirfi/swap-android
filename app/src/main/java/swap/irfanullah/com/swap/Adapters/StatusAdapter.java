package swap.irfanullah.com.swap.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import swap.irfanullah.com.swap.Models.User;
import swap.irfanullah.com.swap.NLUserProfile;
import swap.irfanullah.com.swap.R;
import swap.irfanullah.com.swap.StatusActivity;
import swap.irfanullah.com.swap.Storage.PrefStorage;
import swap.irfanullah.com.swap.SwapWithActivity;
import swap.irfanullah.com.swap.UserProfile;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.StatusViewHolder> {
    private Context context;
    private ArrayList<Status> statuses;
    private ArrayList<String> attachmentsArrayList;
    private ArrayList<Attachments> mediaAttachments;
    private User user;
    int STATUS_ID;
    int TYPE = 0;
    MediaPager pager;
    private static int GRID_WIDTH;
    private static final int NUM_GRIDS = 2;
    private static final String TAG = "StatusAdapter";
    public StatusAdapter(Context context, ArrayList<Status> st, int type) {
        this.context = context;
        this.statuses = st;
        user = PrefStorage.getUser(this.context);
        this.TYPE = type;
    }

    @NonNull
    @Override
    public StatusViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.status_custom_row,viewGroup,false);
        return new StatusViewHolder(view, this.context, this.statuses, this.TYPE);
    }


    @Override
    public void onBindViewHolder(@NonNull StatusViewHolder statusViewHolder, int i) {
        Status e = statuses.get(i);
    statusViewHolder.statusDescription.setText(e.getSTATUS());
    statusViewHolder.ratingBar.setRating(e.getRATTING());
    statusViewHolder.statusTime.setText(TimeDiff.getTimeDifference(e.getTIME()));
    statusViewHolder.likesCount.setText(e.getLIKESCOUNT().toString());

    if(e.isLiked()){
        statusViewHolder.likeBtn.setImageResource(R.drawable.heartred);
        statusViewHolder.likesCount.setTextColor(context.getResources().getColor(R.color.favColor));
    }else {
        statusViewHolder.likeBtn.setImageResource(R.drawable.likehear);
    }
        statusViewHolder.sharesCount.setText(e.getSHARESCOUNT().toString());
        statusViewHolder.commentsCount.setText(e.getCOMMENTSCOUONT().toString());
    ArrayList<Attachments> attachments = new ArrayList<>();
    pager = new MediaPager(context,attachments,Integer.toString(e.getSTATUS_ID()));

    if(user.getPROFILE_IMAGE() == null) {
        statusViewHolder.profile_image.setImageResource(R.drawable.ic_person);
    }
    else {
        //statusViewHolder.profile_image.setImageURI(Uri.parse(PrefStorage.getUser(context).getPROFILE_IMAGE()));
        GLib.downloadImage(context,user.getPROFILE_IMAGE()).into(statusViewHolder.profile_image);
    }
    //RMsg.logHere(Integer.toString(e.getHAS_ATTACHMENTS()));

    if(e.getHAS_ATTACHMENTS() == 1) {

//        ViewGroup.LayoutParams layoutParams = statusViewHolder.mediaView.getLayoutParams();
//        layoutParams.height = 150; //this is in pixels
//        statusViewHolder.mediaView.setLayoutParams(layoutParams);


        statusViewHolder.rl.setVisibility(View.VISIBLE);
        statusViewHolder.mediaView.setVisibility(View.VISIBLE);
        RMsg.logHere("STATUS MEDIA no media: "+Integer.toString(e.getHAS_ATTACHMENTS()) + " : "+e.getSTATUS());
       // loadStatusMedia(statusViewHolder,e.getATTACHMENTS(),e.getSTATUS_ID());
        updatePager(e.getATTACHMENTS(),Integer.toString(e.getSTATUS_ID()),statusViewHolder.indicator,statusViewHolder.mediaView,e.getSTATUS_ID());
        //statusViewHolder.mediaViewPager.setAdapter(pager);
//        updatePager(e.getATTACHMENTS(), statusViewHolder.mediaViewPager);
//        statusViewHolder.mediaViewPager.setVisibility(View.VISIBLE);
//        RMsg.logHere("ATTACHEMENTS WORKING WOW");
    }else {
        statusViewHolder.rl.setVisibility(View.GONE);
        statusViewHolder.mediaView.setVisibility(View.GONE);

        RMsg.logHere("STATUS MEDIA no media: "+Integer.toString(e.getHAS_ATTACHMENTS()) + " : "+e.getSTATUS());
//        ViewGroup.LayoutParams layoutParams = statusViewHolder.mediaView.getLayoutParams();
//        layoutParams.height = 0; //this is in pixels
//        statusViewHolder.mediaView.setLayoutParams(layoutParams);
    }


    }



    @Override
    public int getItemCount() {
        return this.statuses.size();
    }


    public static class StatusViewHolder extends RecyclerView.ViewHolder {
        ImageView profile_image, swapWithBtn, likeBtn, commentBtn, shareBtn;
        TextView username, statusDescription;
        ConstraintLayout layout;
        RatingBar ratingBar;
        TextView statusTime, likesCount, sharesCount, commentsCount;
        ViewPager mediaViewPager;
        ViewPager mediaView;
        ProgressBar mediaProgressBar;
        CircleIndicator indicator;
        int Type = 0;
        Context context;
        int position = 0;
        RelativeLayout rl;


        public StatusViewHolder(@NonNull final View itemView, final Context context, final ArrayList<Status> st, int type) {
            super(itemView);
            this.Type = type;
            this.context = context;
            position = getAdapterPosition();
            layout = itemView.findViewById(R.id.statusLayout);
            profile_image = itemView.findViewById(R.id.profile_image);
            username = itemView.findViewById(R.id.usernameTextView);
            statusDescription = itemView.findViewById(R.id.statusTextView);
            mediaView = itemView.findViewById(R.id.gridViewStatus);
            rl = itemView.findViewById(R.id.rl);

//
//            GRID_WIDTH = context.getResources().getDisplayMetrics().widthPixels;
//            int imgWidht = GRID_WIDTH/NUM_GRIDS;
//            Log.i(TAG, "onBindViewHolder: "+Integer.toString(imgWidht));
//            mediaView.setNumColumns(imgWidht);
//            Log.i(TAG, "onBindViewHolder: column width"+Integer.toString(mediaView.getColumnWidth()));

            // mediaViewPager = itemView.findViewById(R.id.imageViewer);
            indicator = itemView.findViewById(R.id.indicator);

            ratingBar = itemView.findViewById(R.id.ratingBar);
            swapWithBtn = itemView.findViewById(R.id.swapPlusIvBtn);

            likeBtn = itemView.findViewById(R.id.likeBtn);
            commentBtn = itemView.findViewById(R.id.commentBtn);
            shareBtn = itemView.findViewById(R.id.shareBtn);
            likesCount = itemView.findViewById(R.id.likeCountTextView);
            sharesCount = itemView.findViewById(R.id.shareCountTextView);
            commentsCount = itemView.findViewById(R.id.commentsCountTextView);

            ArrayList<Attachments> mediaAttachments = new ArrayList<>();

            //indicator.setViewPager(mediaViewPager);

            if (Type == 1) {
                swapWithBtn.setVisibility(View.GONE);
            } else {
                swapWithBtn.setVisibility(View.VISIBLE);
            }

            statusTime = itemView.findViewById(R.id.statusTimeTextView);
            //mediaView = itemView.findViewById(R.id.gridViewStatus);
            //   mediaProgressBar = itemView.findViewById(R.id.mediaProgressBar);

            swapWithBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int status_id = st.get(getAdapterPosition()).getSTATUS_ID();
                    Intent swapWith = new Intent(context, SwapWithActivity.class);
                    swapWith.putExtra("status_id", status_id);
                    context.startActivity(swapWith);
                }
            });

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Status status = st.get(position);
                    int status_id = status.getSTATUS_ID();
                    Intent singleStatusAct = new Intent(context, StatusActivity.class);
                    singleStatusAct.putExtra("status_id", status_id);
                    singleStatusAct.putExtra("position", position);
                    singleStatusAct.putExtra("is_accepted", 0);
                    singleStatusAct.putExtra("swap_id", 0);
                    context.startActivity(singleStatusAct);
                }
            });

            profile_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Status status = st.get(position);
                    if (PrefStorage.isMe(context, status.getUSER_ID())) {
                        Intent profileAct = new Intent(context, UserProfile.class);
                        profileAct.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(profileAct);
                    } else {
                        Intent profileAct = new Intent(context, NLUserProfile.class);
                        profileAct.putExtra("user_id", status.getUSER_ID());
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

//            RMsg.logHere("position of the adapter: "+Integer.toString(getAdapterPosition()));
//            if(st.get(0).getHAS_ATTACHMENTS() == 1) {
//                updatePager(st.get(0).getATTACHMENTS());
//            }


//            mediaView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent pager = new Intent(context,ImageViewer.class);
//                    int position = getAdapterPosition();
//                    Status status = st.get(position);
//                    pager.putExtra("status_id",status.getSTATUS_ID());
//                    context.startActivity(pager);
//                }
//            });
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

    private void loadStatusMedia(final StatusViewHolder viewHolder,String attachments, int status_id){
      mediaAttachments = new ArrayList<>();
        GridMediaStatusAdapter statusFragGridAdapter = new GridMediaStatusAdapter(context,mediaAttachments,status_id);

//        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context,2);
//        viewHolder.mediaView.setHasFixedSize(true);
//        viewHolder.mediaView.setLayoutManager(layoutManager);
//        viewHolder.mediaView.setAdapter(statusFragGridAdapter);
//        viewHolder.mediaView.setAdapter(statusFragGridAdapter);
//        viewHolder.mediaView.setVisibility(View.VISIBLE);

        Gson gson = new Gson();
        JsonElement json = gson.fromJson(attachments,JsonElement.class);

        if(json.isJsonObject()) {
            JsonObject object = json.getAsJsonObject();
            Attachments att = gson.fromJson(object, Attachments.class);
            mediaAttachments.add(att);


           // viewHolder.mediaView.setLayoutManager(layoutManager);
            statusFragGridAdapter.notifyAdapter(mediaAttachments);
            // GLib.downloadImage(context, att.getATTACHMENT_URL()).into(statusMedia);

        }else if(json.isJsonArray()){
            JsonArray jsonArray = json.getAsJsonArray();
            //Attachments att = gson.fromJson(object, Attachments.class);
            Type type = new TypeToken<ArrayList<Attachments>>(){}.getType();
            ArrayList<Attachments> arrayList = gson.fromJson(jsonArray,type);
            statusFragGridAdapter.notifyAdapter(arrayList);
            RMsg.logHere("working array "+arrayList.size());
        }

    }


    private void updatePager(String attachments,String st_id,CircleIndicator indicator , ViewPager mediaPager,int status_id){

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
