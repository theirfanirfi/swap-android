package swap.irfanullah.com.swap.SwapRequestActivityFragments;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import swap.irfanullah.com.swap.Adapters.MediaPager;
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

public class SwapsReviewNotificationsAdapter extends RecyclerView.Adapter<SwapsReviewNotificationsAdapter.StatusViewHolder> {
    private Context context, ctx;
    private ArrayList<SwapsTab> swapsTabArrayList;
    private final static String REPRESENTING_LOGGED_USER_IN_TAB = "You";
    private int STATUS_ID = 0;
    MediaPager pager;

    public SwapsReviewNotificationsAdapter(Context context, ArrayList<SwapsTab> swapsTabArrayList) {
        this.context = context;
        this.swapsTabArrayList = swapsTabArrayList;
    }

    @NonNull
    @Override
    public StatusViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.review_swap_custom_row, viewGroup, false);


        return new StatusViewHolder(view, this.context, this.swapsTabArrayList, STATUS_ID);
    }

    @Override
    public void onBindViewHolder(@NonNull StatusViewHolder statusViewHolder, int i) {
        SwapsTab swap = this.swapsTabArrayList.get(i);

        ArrayList<Attachments> attachments = new ArrayList<>();
        pager = new MediaPager(context,attachments, Integer.toString(STATUS_ID));

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
        statusViewHolder.swapTime.setText(TimeDiff.getTimeDifference(swap.getSWAP_DATE()));


        if(swap.getHAS_ATTACHMENTS() == 1) {
//            loadStatusMedia(statusViewHolder,swap.getATTACHMENTS(), swap.getSTATUS_ID());
            statusViewHolder.rl.setVisibility(View.VISIBLE);
            statusViewHolder.mediaView.setVisibility(View.VISIBLE);
//            RMsg.logHere("MEDIA has media: "+Integer.toString(swap.getHAS_ATTACHMENTS()) + " : "+swap.getSTATUS());
            updatePager(swap.getATTACHMENTS(),Integer.toString(STATUS_ID),statusViewHolder.indicator,statusViewHolder.mediaView,STATUS_ID);
        }else {
            statusViewHolder.mediaView.setVisibility(View.GONE);
            statusViewHolder.rl.setVisibility(View.GONE);
//            RMsg.logHere("MEDIA no media: "+Integer.toString(swap.getHAS_ATTACHMENTS()) + " : "+swap.getSTATUS());
        }
    }


    @Override
    public int getItemCount() {
        return this.swapsTabArrayList.size();
    }


    public static class StatusViewHolder extends RecyclerView.ViewHolder {
        ImageView profile_image, unswap;
        Button reviewBtn;
        TextView username, statusDescription, withTextV, swapTime;
        ConstraintLayout layout;
        ViewPager mediaView;
        Context context;
        RelativeLayout rl;
        CircleIndicator indicator;

        public StatusViewHolder(@NonNull View itemView, final Context context, final ArrayList<SwapsTab> swapsTabs, int status_id) {
            super(itemView);
            this.context = context;
            layout = itemView.findViewById(R.id.statusLayout);
            profile_image = itemView.findViewById(R.id.profile_image);
            username = itemView.findViewById(R.id.usernameTextView);
            withTextV = itemView.findViewById(R.id.withTextView);
            statusDescription = itemView.findViewById(R.id.statusTextView);
            swapTime = itemView.findViewById(R.id.statusTimeTextView);
            layout = itemView.findViewById(R.id.statusLayout);
            mediaView = itemView.findViewById(R.id.gridViewStatus);


            reviewBtn = itemView.findViewById(R.id.reviewBtn);

            rl = itemView.findViewById(R.id.rl);
            indicator = itemView.findViewById(R.id.indicator);

            //unswap = itemView.findViewById(R.id.cancelViewImgBtn);

                gotoProfile(context,swapsTabs);
            //rate the status


            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    int position = getAdapterPosition();
//                    SwapsTab swap = swapsTabs.get(position);
//                    int status_id = swap.getSTATUS_ID();
//                    int is_accepted = swap.getIS_ACCEPTED();
//                    int swap_id = swap.getSWAP_ID();
//                    Intent singleStatusAct = new Intent(context,StatusActivity.class);
//                    singleStatusAct.putExtra("status_id",status_id);
//                    singleStatusAct.putExtra("position",position);
//                    singleStatusAct.putExtra("is_accepted",is_accepted);
//                    singleStatusAct.putExtra("swap_id",swap_id);
//                    context.startActivity(singleStatusAct);
//                    Log.i(RMsg.LOG_MESSAGE,Integer.toString(swap_id)+" : "+Integer.toString(swap.getIS_ACCEPTED()));
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

    private void loadStatusMedia(final SwapsReviewNotificationsAdapter.StatusViewHolder viewHolder, String attachments, int status_id){
        RMsg.logHere("SWAPS: "+attachments);

        ArrayList<Attachments> mediaAttachments = new ArrayList<>();
//        StatusFragGridAdapter statusFragGridAdapter = new StatusFragGridAdapter(context,mediaAttachments, status_id);
//        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context,4);
//        viewHolder.mediaView.setHasFixedSize(true);
//        viewHolder.mediaView.setLayoutManager(layoutManager);
//        viewHolder.mediaView.setAdapter(statusFragGridAdapter);
        viewHolder.mediaView.setVisibility(View.VISIBLE);

        Gson gson = new Gson();
        JsonElement json = gson.fromJson(attachments,JsonElement.class);
        if(json.isJsonObject()) {
            JsonObject object = json.getAsJsonObject();
            Attachments att = gson.fromJson(object, Attachments.class);
            mediaAttachments.add(att);
         //   statusFragGridAdapter.notifyAdapter(mediaAttachments);
            // GLib.downloadImage(context, att.getATTACHMENT_URL()).into(statusMedia);

            RMsg.logHere("Single: "+att.getATTACHMENT_URL());
        }else if(json.isJsonArray()){
            JsonArray jsonArray = json.getAsJsonArray();
            //Attachments att = gson.fromJson(object, Attachments.class);
            Type type = new TypeToken<ArrayList<Attachments>>(){}.getType();
            ArrayList<Attachments> arrayList = gson.fromJson(jsonArray,type);
           // statusFragGridAdapter.notifyAdapter(arrayList);
            RMsg.logHere("working");
        }
    }

    private void updatePager(String attachments, String st_id, CircleIndicator indicator , ViewPager mediaPager, int status_id) {

        mediaPager.setVisibility(View.VISIBLE);
        ArrayList<Attachments> mediaAttachments = new ArrayList<>();
        pager = new MediaPager(context, mediaAttachments, Integer.toString(status_id));
        Gson gson = new Gson();
        JsonElement json = gson.fromJson(attachments, JsonElement.class);

        if (json.isJsonObject()) {
            JsonObject object = json.getAsJsonObject();
            Attachments att = gson.fromJson(object, Attachments.class);
            mediaAttachments.add(att);
            mediaPager.setAdapter(pager);
            indicator.setViewPager(mediaPager);
            pager.notifyAdapter(mediaAttachments);
            //pager.setIndicator(mediaPager);

            // GLib.downloadImage(context, att.getATTACHMENT_URL()).into(statusMedia);

            RMsg.logHere("Single: " + att.getATTACHMENT_URL());
        } else if (json.isJsonArray()) {
            JsonArray jsonArray = json.getAsJsonArray();
            //Attachments att = gson.fromJson(object, Attachments.class);
            Type type = new TypeToken<ArrayList<Attachments>>() {
            }.getType();
            ArrayList<Attachments> arrayList = gson.fromJson(jsonArray, type);
            pager = new MediaPager(context, arrayList, Integer.toString(status_id));
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
