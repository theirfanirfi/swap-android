package swap.irfanullah.com.swap;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import swap.irfanullah.com.swap.Adapters.SingleStatusAdapter;
import swap.irfanullah.com.swap.Adapters.StatusFragGridAdapter;
import swap.irfanullah.com.swap.CustomComponents.CommentDialog;
import swap.irfanullah.com.swap.Libraries.GLib;
import swap.irfanullah.com.swap.Libraries.RetroLib;
import swap.irfanullah.com.swap.Models.Attachments;
import swap.irfanullah.com.swap.Models.Like;
import swap.irfanullah.com.swap.Models.RMsg;
import swap.irfanullah.com.swap.Models.Share;
import swap.irfanullah.com.swap.Models.SingleStatusModel;
import swap.irfanullah.com.swap.Models.Status;
import swap.irfanullah.com.swap.Models.Swap;
import swap.irfanullah.com.swap.Models.User;
import swap.irfanullah.com.swap.StatusActivityFrags.CommentsRatersPager;
import swap.irfanullah.com.swap.Storage.PrefStorage;

public class StatusActivity extends AppCompatActivity {

    private ViewPager commentsRatesPager;
    private TextView fullname,statusTextView,status_date,avg_rating,swaps_count, likesCount, sharesCount, commentsCount;
    private ImageView profile_image,trash, likeBtn, commentBtn, shareBtn;
    private RatingBar ratingBar;
    private SingleStatusAdapter singleStatusAdapter;
    private int STATUS_ID, ADAP_POSITION, SWAP_ID, USER_ID, NOTIFICATION_ID = 0;
    private Context context;
    private Status status;
    private SingleStatusModel singleStatusModel;
    private ArrayList<User> raters;
    private static final String ACTION_BAR_TITLE = "Status";
    private int IS_ACCEPTED = 0;
    private Boolean isMe= false;
    private RecyclerView statusMedia;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Attachments> mediaAttachments;
    private StatusFragGridAdapter statusFragGridAdapter;
    private boolean INTENT_RECIEVED_FROM_BROWSE_TAB = false;
    private boolean INTENT_RECIEVED_FROM_SWAP_REQUEST_ACTIVITY = false;
//    private FloatingActionButton commentFloatingActionButton;
    Menu menu;
    private CommentsRatersPager commentsRatersPager;
    private TabLayout tabLayout;
    CommentDialog commentDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        getSupportActionBar().setTitle(ACTION_BAR_TITLE);

        initializeObjects();
        getStatusID();
        statusInteractionBarListeners();
        makeRequest(this);
        deleteStatus();
        rateStatus();
        gotoProfile();
        floatingCommentBtnClickListenerAndStartCommentDialog();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
//        Log.i(RMsg.LOG_MESSAGE,Boolean.toString(isMe));
//        if(this.IS_ACCEPTED == 0 && isMe.equals(false) && INTENT_RECIEVED_FROM_BROWSE_TAB){
//            Log.i("BROWSE TAB:" , "YES");
//            if(menu.hasVisibleItems()){
//                Log.i("BROWSE TABnf:" , "YES");
//                menu.removeGroup(R.id.action_group);
//                //getMenuInflater().inflate(R.menu.status_action_menu, menu);
//                return true;
//            }else {
//                Log.i("BROWSE TABne:" , "YES");
//                // getMenuInflater().inflate(R.menu.status_action_menu, menu);
//                return true;
//            }
//        }else if(this.IS_ACCEPTED == 0 && isMe.equals(false)){
//            Log.i("BROWSE TABnot:" , "YES");
//            if(menu.hasVisibleItems()){
//                menu.removeGroup(R.id.action_group);
//                getMenuInflater().inflate(R.menu.status_action_menu, menu);
//                return true;
//            }else {
//                getMenuInflater().inflate(R.menu.status_action_menu, menu);
//                return true;
//            }
//        }else {
//            menu.removeGroup(R.id.action_group);
//            //getMenuInflater().inflate(R.menu.status_action_menu,menu);
//            return false;
//        }
        //return super.onCreateOptionsMenu(menu);

        if(INTENT_RECIEVED_FROM_SWAP_REQUEST_ACTIVITY && this.IS_ACCEPTED == 0 && isMe.equals(false)){
            if(menu.hasVisibleItems()){
                menu.removeGroup(R.id.action_group);
                getMenuInflater().inflate(R.menu.status_action_menu, menu);
                return true;
            }else {
                getMenuInflater().inflate(R.menu.status_action_menu, menu);
                return true;
            }
        }else {
            getMenuInflater().inflate(R.menu.status_action_menu, menu);
            menu.removeGroup(R.id.action_group);
        }
        return true;
    }

    private void rateStatus() {
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if(fromUser){
                    RetroLib.geApiService().rateStatus(PrefStorage.getUser(context).getTOKEN(), STATUS_ID, rating).enqueue(new Callback<Status>() {
                        @Override
                        public void onResponse(Call<Status> call, Response<Status> response) {

                            if (response.isSuccessful()) {

                                Status status = response.body();
                                if (status.getAuthenticated()) {
                                    if (status.getIS_EMPTY()) {
                                        Toast.makeText(context, status.getMESSAGE(), Toast.LENGTH_LONG).show();
                                    } else if (status.getIS_RATED()) {
                                        Toast.makeText(context, status.getMESSAGE(), Toast.LENGTH_LONG).show();
                                        makeRequest(context);
                                    } else if (status.getIS_ALREADY_RATED()) {
                                        Toast.makeText(context, status.getMESSAGE(), Toast.LENGTH_LONG).show();
                                        makeRequest(context);
                                    } else {
                                        Toast.makeText(context, status.getMESSAGE(), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(context, "You are not logged in. Please login and try again.", Toast.LENGTH_LONG).show();
                                }

                            } else {
                                Toast.makeText(context, "Request was unsuccessful.", Toast.LENGTH_LONG).show();
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
    }

    private void deleteStatus() {
        trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete Status");
                builder.setMessage("Are you sure to delete the status?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       // Toast.makeText(context,Integer.toString(status.getSTATUS_ID()),Toast.LENGTH_LONG).show();
                    RetroLib.geApiService().deleteStatus(PrefStorage.getUser(context).getTOKEN(),status.getSTATUS_ID()).enqueue(new Callback<Status>() {
                        @Override
                        public void onResponse(Call<Status> call, Response<Status> response) {
                            if(response.isSuccessful()){
                                Status st = response.body();
                                if(st.getAuthenticated()){
                                    if(st.getIS_EMPTY()){
                                        Toast.makeText(context,st.getMESSAGE(),Toast.LENGTH_LONG).show();
                                    }else if(st.getFound()){
                                        if(st.getIS_DELETED()){
                                            Intent sf = new Intent(context,HomeActivity.class);
                                            sf.putExtra("delete_position",ADAP_POSITION);
                                            startActivity(sf);
                                            //finish();
                                            Toast.makeText(context,st.getMESSAGE(),Toast.LENGTH_LONG).show();
                                        }else {
                                            Toast.makeText(context,st.getMESSAGE(),Toast.LENGTH_LONG).show();
                                        }
                                    }else {
                                        Toast.makeText(context,st.getMESSAGE(),Toast.LENGTH_LONG).show();
                                    }
                                }else {
                                    Toast.makeText(context,"You are not logged in. Please login and try again.",Toast.LENGTH_LONG).show();

                                }
                            }else {
                                Toast.makeText(context,"The request to delete the status was not successful.",Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Status> call, Throwable t) {

                        }
                    });

                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
            }
        });
    }


    private void getStatusID() {
        Bundle bundle = getIntent().getExtras();
        this.STATUS_ID = bundle.getInt("status_id");
        this.ADAP_POSITION = bundle.getInt("position");
        this.IS_ACCEPTED = bundle.getInt("is_accepted");


        mediaAttachments = new ArrayList<>();
        statusFragGridAdapter = new StatusFragGridAdapter(context,mediaAttachments,this.STATUS_ID);
        RMsg.logHere("status_id: "+Integer.toString(this.STATUS_ID));
        layoutManager = new GridLayoutManager(context,4);
        //statusMedia.setHasFixedSize(true);
        statusMedia.setAdapter(statusFragGridAdapter);
        statusMedia.setLayoutManager(layoutManager);


//        this.NOTIFICATION_ID = bundle.getInt("notification_id");

//        if(bundle.getString("is_browse_status","").equals("")){
//            INTENT_RECIEVED_FROM_BROWSE_TAB = false;
//        }else
//
        if(bundle.getString("is_swap_request","").equals("yes")){

            //INTENT_RECIEVED_FROM_BROWSE_TAB = false;
            INTENT_RECIEVED_FROM_SWAP_REQUEST_ACTIVITY = true;
            this.NOTIFICATION_ID = bundle.getInt("notification_id");

        }else {
            //INTENT_RECIEVED_FROM_BROWSE_TAB = true;
            INTENT_RECIEVED_FROM_SWAP_REQUEST_ACTIVITY = false;
        }

        Log.i(RMsg.LOG_MESSAGE,"IS ACCEPTED: "+Integer.toString(IS_ACCEPTED));
        //menu.setGroupVisible(R.id.action_group,false);
        //onCreateOptionsMenu(menu);

        commentsRatersPager = new CommentsRatersPager(getSupportFragmentManager(),STATUS_ID);
        commentsRatesPager.setAdapter(commentsRatersPager);
        tabLayout.setupWithViewPager(commentsRatesPager);
        tabLayout.getTabAt(0).setText("Comments");
        tabLayout.getTabAt(1).setText("Raters");


    }

    private void statusInteractionBarListeners(){
        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeOrDislikeStatusRequest(Integer.toString(STATUS_ID),PrefStorage.getUser(context).getTOKEN());
            }
        });

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareStatusRequest(Integer.toString(STATUS_ID),PrefStorage.getUser(context).getTOKEN());
            }
        });
    }


    public void initializeObjects(){

        commentsRatesPager = findViewById(R.id.commentsRatersPager);
        fullname = findViewById(R.id.fullNameTextView);
        statusTextView = findViewById(R.id.statusTextView);
        status_date = findViewById(R.id.status_posted_date);
        avg_rating = findViewById(R.id.avg_ratingTextView);
        swaps_count = findViewById(R.id.totalSwaps);
        profile_image = findViewById(R.id.profile_image);
        trash = findViewById(R.id.deleteStatusIcon);
        ratingBar = findViewById(R.id.ratingBar);
        statusMedia = findViewById(R.id.gridViewStatus);
//        commentFloatingActionButton = findViewById(R.id.floatingActionButton);
        tabLayout = findViewById(R.id.commentsAndRatersTabLayout);

        likeBtn = findViewById(R.id.likeBtn);
        commentBtn = findViewById(R.id.commentBtn);
        shareBtn = findViewById(R.id.shareBtn);
        likesCount = findViewById(R.id.likeCountTextView);
        sharesCount = findViewById(R.id.shareCountTextView);
        commentsCount = findViewById(R.id.commentsCountTextView);
        context = this;
        status = new Status();

        raters = new ArrayList<>();
        singleStatusAdapter = new SingleStatusAdapter(context,raters, status);
        RecyclerView.LayoutManager layoutManagerr = new LinearLayoutManager(context);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(layoutManagerr);
//        recyclerView.setAdapter(singleStatusAdapter);



    }

    private void makeRequest(final Context context) {
        RetroLib.geApiService().getRaters(PrefStorage.getUser(this).getTOKEN(),this.STATUS_ID).enqueue(new Callback<SingleStatusModel>() {
            @Override
            public void onResponse(Call<SingleStatusModel> call, Response<SingleStatusModel> response) {
                if(response.isSuccessful()){
                    singleStatusModel = response.body();
                    status = singleStatusModel.getSTATUS();

                    if(status.getHAS_ATTACHMENTS() == 1){
                        loadStatusMedia(status.getATTACHMENTS());
                    }


                    fullname.setText(status.getName());
                    statusTextView.setText(status.getSTATUS());
                    status_date.setText(status.getTIME());
                    likesCount.setText(status.getLIKESCOUNT().toString());
                    commentsCount.setText(status.getCOMMENTSCOUONT().toString());
                    sharesCount.setText(status.getSHARESCOUNT().toString());

                    if(status.isLiked() == 1){
                        likeBtn.setImageResource(R.drawable.heartred);
                    }else {
                        likeBtn.setImageResource(R.drawable.likehear);
                    }

                    USER_ID= status.getUSER_ID();
                    if(status.getPROFILE_IMAGE() == null ){
                        profile_image.setImageResource(R.drawable.ic_person);
                    }else {
                        GLib.downloadImage(context,status.getPROFILE_IMAGE()).into(profile_image);
                    }

                    if(status.getUSER_ID() == PrefStorage.getUser(context).getUSER_ID()){
                        trash.setVisibility(View.VISIBLE);
                        isMe = true;
                        onCreateOptionsMenu(menu);
                    }else {
                        onCreateOptionsMenu(menu);
                        isMe = false;
                    }

                    if(singleStatusModel.getIS_AUTHENTICATED()){
                        if(singleStatusModel.getIS_EMPTY()){
                            Toast.makeText(context, singleStatusModel.getMESSAGE(), Toast.LENGTH_LONG).show();
                            finish();
                        } else if(singleStatusModel.getIS_ERROR()){
                            Toast.makeText(context, singleStatusModel.getMESSAGE(), Toast.LENGTH_LONG).show();
                        } else if(singleStatusModel.getIS_FOUND()){

                            ratingBar.setRating(singleStatusModel.getAVERAGE_RATING());
                            avg_rating.setText(Float.toString(singleStatusModel.getAVERAGE_RATING()));
                            swaps_count.setText("Swaps: "+Integer.toString(singleStatusModel.getSWAPS_COUNT()));

                            raters = singleStatusModel.getRATERS();

                            singleStatusAdapter = new SingleStatusAdapter(context,raters, status);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
//                            recyclerView.setHasFixedSize(true);
//                            recyclerView.setLayoutManager(layoutManager);
//                            recyclerView.setAdapter(singleStatusAdapter);

                        } else {
                           // finish();
                            ratingBar.setRating(singleStatusModel.getAVERAGE_RATING());
                            avg_rating.setText(Float.toString(singleStatusModel.getAVERAGE_RATING()));
                            swaps_count.setText("Swaps: "+Integer.toString(singleStatusModel.getSWAPS_COUNT()));
                           // Toast.makeText(context, singleStatusModel.getMESSAGE(), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(context, RMsg.AUTH_ERROR_MESSAGE, Toast.LENGTH_LONG).show();

                    }

                } else {
                    Toast.makeText(context, RMsg.REQ_ERROR_MESSAGE, Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<SingleStatusModel> call, Throwable t) {
                Toast.makeText(context, t.toString() + " "+t.getMessage(), Toast.LENGTH_LONG).show();
                RMsg.logHere(t.toString() + " "+t.getMessage());

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    public void notifyAdapter(){

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.accept:
                Log.i(RMsg.LOG_MESSAGE,"accepted");
                approve();
                break;
            case R.id.decline:
                Log.i(RMsg.LOG_MESSAGE,"declined");
                //menu.setGroupVisible(R.id.action_group,false);
                declineSwap();
                break;
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void approve(){
        RetroLib.geApiService().approveSwap(PrefStorage.getUser(context).getTOKEN(),this.NOTIFICATION_ID).enqueue(new Callback<Swap>() {
            @Override
            public void onResponse(Call<Swap> call, Response<Swap> response) {
                if(response.isSuccessful()){
                    Swap swap = response.body();
                    if(swap.getAuthenticated()){
                        if(swap.getError()){
                            RMsg.toastHere(context,swap.getMESSAGE());
                        }else if(swap.getApproved()){
                            menu.setGroupVisible(R.id.action_group,false);
                            RMsg.toastHere(context,swap.getMESSAGE());
                        }else {
                            RMsg.toastHere(context,swap.getMESSAGE());
                        }
                    }else {
                        RMsg.toastHere(context,RMsg.AUTH_ERROR_MESSAGE);
                    }

                }else {
                    RMsg.toastHere(context,RMsg.REQ_ERROR_MESSAGE);
                }
            }

            @Override
            public void onFailure(Call<Swap> call, Throwable t) {
                RMsg.toastHere(context,t.toString());
            }
        });
    }

    public void declineSwap(){
        RetroLib.geApiService().declineSwap(PrefStorage.getUser(context).getTOKEN(),this.NOTIFICATION_ID).enqueue(new Callback<Swap>() {
            @Override
            public void onResponse(Call<Swap> call, Response<Swap> response) {
                if(response.isSuccessful()){
                    Swap swap = response.body();
                    if(swap.getAuthenticated()){
                        if(swap.getError()){
                            RMsg.toastHere(context,swap.getMESSAGE());
                        }else if(swap.getDeclined()){
                            menu.setGroupVisible(R.id.action_group,false);
                            menu.removeGroup(R.id.action_group);
                            RMsg.toastHere(context,swap.getMESSAGE());
                        }else {
                            RMsg.toastHere(context,swap.getMESSAGE());
                        }
                    }else {
                        RMsg.toastHere(context,RMsg.AUTH_ERROR_MESSAGE);
                    }

                }else {
                    RMsg.logHere(response.raw().toString());
                    RMsg.toastHere(context,RMsg.REQ_ERROR_MESSAGE);
                }
            }

            @Override
            public void onFailure(Call<Swap> call, Throwable t) {
                RMsg.toastHere(context,t.toString());
            }
        });
    }



    public void gotoProfile(){
        fullname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PrefStorage.isMe(context,USER_ID)){
                    Intent profileAct = new Intent(context,UserProfile.class);
                   context.startActivity(profileAct);
                }else {
                    Intent profileAct = new Intent(context,NLUserProfile.class);
                    profileAct.putExtra("user_id",USER_ID);
                    context.startActivity(profileAct);
                }
            }
        });

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PrefStorage.isMe(context,USER_ID)){
                    Intent profileAct = new Intent(context,UserProfile.class);

                    context.startActivity(profileAct);
                }else {
                    Intent profileAct = new Intent(context,NLUserProfile.class);
                    profileAct.putExtra("user_id",USER_ID);
                    context.startActivity(profileAct);
                }
            }
        });
    }

    private void loadStatusMedia(String attachments){

        // viewHolder.mediaProgressBar.setVisibility(View.VISIBLE);

//        viewHolder.mediaProgressBar.setVisibility(View.GONE);
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

    public void likeOrDislikeStatusRequest(String status_id, String token) {
        RetroLib.geApiService().likeOrDislikeStatus(token, status_id).enqueue(new Callback<Like>() {
            @Override
            public void onResponse(Call<Like> call, Response<Like> response) {
                if (response.isSuccessful()) {
                    Like like = response.body();
                    if (like.isError() || !like.isAuthenticated()) {
                        RMsg.toastHere(context, like.getMessage());
                        RMsg.logHere(like.getMessage());
                    } else if(like.isLiked()) {
                        likeBtn.setImageResource(R.drawable.heartred);
                        likesCount.setText(Integer.toString(like.getStatusLikes()));
                        RMsg.toastHere(context, like.getMessage());
                        RMsg.logHere(like.getMessage());
                    }else if(like.isUnliked()){
                        likeBtn.setImageResource(R.drawable.likehear);
                        likesCount.setText(Integer.toString(like.getStatusLikes()));
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


    private void floatingCommentBtnClickListenerAndStartCommentDialog(){
//        commentDialog = new CommentDialog();
//        commentDialog.setOnCommentClickListener(this);
//        commentFloatingActionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
////                dialog.setTitle("Post a comment");
//
//                Bundle bundle = new Bundle();
//                bundle.putString("status_id",Integer.toString(STATUS_ID));
//                commentDialog.setArguments(bundle);
//                commentDialog.setCancelable(false);
//                commentDialog.show(getSupportFragmentManager(),"comment_on_post");
//            }
//        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
