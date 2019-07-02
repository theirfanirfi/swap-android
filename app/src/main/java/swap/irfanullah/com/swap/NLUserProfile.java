package swap.irfanullah.com.swap;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.soundcloud.android.crop.Crop;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import swap.irfanullah.com.swap.Adapters.ProfilePagerAdapter;
import swap.irfanullah.com.swap.CustomComponents.PDialog;
import swap.irfanullah.com.swap.Libraries.GLib;
import swap.irfanullah.com.swap.Libraries.RetroLib;
import swap.irfanullah.com.swap.Models.Followers;
import swap.irfanullah.com.swap.Models.ProfileModel;
import swap.irfanullah.com.swap.Models.RMsg;
import swap.irfanullah.com.swap.Models.Statistics;
import swap.irfanullah.com.swap.Models.User;
import swap.irfanullah.com.swap.Storage.PrefStorage;

public class NLUserProfile extends AppCompatActivity {
    private ViewPager viewPager;
    private ProfilePagerAdapter profilePagerAdapter;
    private ImageView profile_image;
    private TabLayout tabLayout;
    private Context context;
    private TextView profileDescription, statuses,swaps,followers;
    private String PROFILE_IMAGE = null, DESCRIPTION;
    private int USER_ID = 0, IS_FOLLOW = 0;
    private Boolean isFollowed = false;
    private User user;
    private Button followBtn;
    private final String LOGGEDIN_USER_INTENT_KEY = "loggedin_user_id";
    private final String TO_CHAT_WITH_USER_INTENT_KEY = "to_chat_with_user_id";
    private final String CHAT_ID_INTENT_KEY = "chat_id";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        initializeObjects();
        loadStats();
        startFollowersActivity();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nl_user_profile_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.messenger){
            User loggedUser = PrefStorage.getUser(context);
            if( loggedUser == null){
                RMsg.toastHere(context,RMsg.AUTH_ERROR_MESSAGE);
                finish();
            }else {
                Intent chatAct = new Intent(context,ChatActivity.class);
                chatAct.putExtra(LOGGEDIN_USER_INTENT_KEY,loggedUser.getUSER_ID());
                chatAct.putExtra(TO_CHAT_WITH_USER_INTENT_KEY,USER_ID);
                chatAct.putExtra(CHAT_ID_INTENT_KEY,0);
                startActivity(chatAct);
            }
        }
        return false;
    }

    private void loadStats() {
        RetroLib.geApiService().getUserStats(USER_ID, PrefStorage.getUser(context).getTOKEN()).enqueue(new Callback<Statistics>() {
            @Override
            public void onResponse(Call<Statistics> call, Response<Statistics> response) {
                if(response.isSuccessful()){
                    Statistics stat = response.body();
                        if(stat.getIS_EMPTY()){
                            finish();
                            //Toast.makeText(context,stat.getMESSAGE(),Toast.LENGTH_LONG).show();
                        }else if(stat.getIS_FOUND()){
                            statuses.setText(Integer.toString(stat.getSTATUSES_COUNT()));
                            swaps.setText(Integer.toString(stat.getSWAPS_COUNT()));
                            followers.setText(Integer.toString(stat.getFOLLOWERS_COUNT()));
                            checkFollow(stat.getIsFollow());
                            Log.i(RMsg.LOG_MESSAGE,Integer.toString(stat.getIsFollow()));
                            user = stat.getUSER();
                            PROFILE_IMAGE = user.getPROFILE_IMAGE();
                            DESCRIPTION = user.getPROFILE_DESCRIPTION();
                            profileDescription.setText(DESCRIPTION);
                            loadProfilePicture();
                        }else {
finish();
                        }

                }else {
                    Toast.makeText(context,RMsg.REQ_ERROR_MESSAGE,Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Statistics> call, Throwable t) {
                Toast.makeText(context,t.toString(),Toast.LENGTH_LONG).show();

            }
        });
    }

    private void initializeObjects() {
        context = this;
        USER_ID=getIntent().getExtras().getInt("user_id");
        ///user = PrefStorage.getSharedPreference(context).contains(PrefStorage.USER_PREF_DETAILS) ? PrefStorage.getUser(context) : null ;
        if(USER_ID == 0){
            finish();
        }

        viewPager = findViewById(R.id.profileViewPage);
        profilePagerAdapter = new ProfilePagerAdapter(getSupportFragmentManager(),USER_ID);
        viewPager.setAdapter(profilePagerAdapter);

        tabLayout = findViewById(R.id.profileTabLayout);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        statuses = findViewById(R.id.statusesProfileTextView);
        swaps = findViewById(R.id.swapsNoProfileTextView);
        followers = findViewById(R.id.followerNoProfileTextView);
        followBtn = findViewById(R.id.followBtn);
        followBtn.setText("Following");
        followUser();

        profile_image = findViewById(R.id.profile_image);

        profileDescription = findViewById(R.id.userProfileDescription);
        loadProfilePicture();

    }

   private void loadProfilePicture(){
       if(PROFILE_IMAGE == null) {
           profile_image.setImageResource(R.drawable.ic_person);
       } else {
           GLib.downloadImage(context,PROFILE_IMAGE).into(profile_image);
       }
    }

    private void checkFollow(int isfollow){
        this.IS_FOLLOW = isfollow;
        if(this.IS_FOLLOW > 0){
            isFollowed = true;
            followBtn.setText("Following");
        }else {
            isFollowed = false;
            followBtn.setText("Follow");
        }

    }

    private void followUser(){
        followBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isFollowed) {
                    RetroLib.geApiService().follow(PrefStorage.getUser(context).getTOKEN(), USER_ID).enqueue(new Callback<Followers>() {
                        @Override
                        public void onResponse(Call<Followers> call, Response<Followers> response) {
                            if (response.isSuccessful()) {
                                Followers followers = response.body();
                                if (followers.getAuthenticated()) {
                                    if (followers.getError()) {
                                        RMsg.toastHere(context, followers.getMESSAGE());
                                    } else if (followers.getAlreadyFollowed()) {
                                        RMsg.toastHere(context, followers.getMESSAGE());
                                        followBtn.setText("Following");
                                    } else if (followers.getFollowed()) {
                                        loadStats();
                                        followBtn.setText("Following");
                                        RMsg.toastHere(context, followers.getMESSAGE());
                                    } else {
                                        RMsg.toastHere(context, followers.getMESSAGE());
                                    }
                                } else {
                                    RMsg.toastHere(context, RMsg.AUTH_ERROR_MESSAGE);
                                }
                            } else {
                                Toast.makeText(context, RMsg.REQ_ERROR_MESSAGE, Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Followers> call, Throwable t) {
                            RMsg.toastHere(context, t.toString());

                        }
                    });
                }else {
                    RetroLib.geApiService().unfollow(PrefStorage.getUser(context).getTOKEN(),USER_ID).enqueue(new Callback<Followers>() {
                        @Override
                        public void onResponse(Call<Followers> call, Response<Followers> response) {
                            if(response.isSuccessful()){
                                Followers fo = response.body();
                                if(fo.getAuthenticated()){
                                    if(fo.getError()){
                                        RMsg.toastHere(context,fo.getMESSAGE());
                                    }else {
                                        if(fo.getUnFollowed()){

                                            isFollowed = false;
                                            //change the button text back to unfollow
                                            checkFollow(0);
                                            loadStats();
                                            RMsg.toastHere(context,fo.getMESSAGE());
                                        }else if(fo.getAlreadyUnFollowed()){
                                            isFollowed = false;
                                            //change the button text back to unfollow
                                            checkFollow(0);
                                            loadStats();
                                            RMsg.toastHere(context,fo.getMESSAGE());
                                        }else {
                                            isFollowed = false;
                                            //change the button text back to unfollow
                                            checkFollow(0);
                                            RMsg.toastHere(context,fo.getMESSAGE());
                                        }
                                    }
                                }else {
                                    RMsg.toastHere(context,RMsg.AUTH_ERROR_MESSAGE);
                                }
                            }else{
                                RMsg.toastHere(context,RMsg.REQ_ERROR_MESSAGE);
                            }
                        }

                        @Override
                        public void onFailure(Call<Followers> call, Throwable t) {
                            RMsg.toastHere(context,t.toString());
                        }
                    });
                }
                }
        });
    }

    public void startFollowersActivity(){
        followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent followersAct = new Intent(context,UserProfileFollowersActivity.class);
                followersAct.putExtra("profile_id",USER_ID);
                startActivity(followersAct);
              //  RMsg.toastHere(context,"working");
            }
        });
    }
}
