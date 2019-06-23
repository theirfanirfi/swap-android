package swap.irfanullah.com.swap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import swap.irfanullah.com.swap.Adapters.FollowUsersNacentAdapter;
import swap.irfanullah.com.swap.Adapters.UserProfileFollowersAdapter;
import swap.irfanullah.com.swap.Libraries.RetroLib;
import swap.irfanullah.com.swap.Models.Followers;
import swap.irfanullah.com.swap.Models.RMsg;
import swap.irfanullah.com.swap.Models.User;
import swap.irfanullah.com.swap.Storage.PrefStorage;

public class UserProfileFollowersActivity extends AppCompatActivity {

    private RecyclerView usersRV;
    private UserProfileFollowersAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<User> users;
    private Context context;
    private int PROFILE_ID = 0;
    private User user;
    ConstraintLayout layout;
    private boolean NEXT = false;
    private Menu menu;
    private boolean IS_MENU_DISPLAYED = true;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_phone_contacts);

        context = this;
        user = PrefStorage.getUser(context);
       // getSupportActionBar().setTitle("Follow atleast 5 Users");
       // getSupportActionBar().setSubtitle(Integer.toString(TOTAL_TO_FOLLOW - FOLLOWED)+" remaining");
        layout = findViewById(R.id.clayout);
        usersRV = findViewById(R.id.contactPhoneNumber);
        layoutManager = new LinearLayoutManager(context);
        usersRV.setLayoutManager(layoutManager);
        users = new ArrayList<>();
        adapter = new UserProfileFollowersAdapter(context,users);
        getUserIDFromIntent();
//        adapter.setOnFollowClickListener(new FollowUsersNacentAdapter.FollowListener() {
//            @Override
//            public void onFollow(View v, final int position) {
//                final Button fBtn = v.findViewById(R.id.followBtn);
//                RetroLib.geApiService().follow(user.getTOKEN(),users.get(position).getUSER_ID()).enqueue(new Callback<Followers>() {
//                    @Override
//                    public void onResponse(Call<Followers> call, Response<Followers> response) {
//                        if(response.isSuccessful()){
//                            Followers follow = response.body();
//                            if(follow.getAuthenticated()){
//                                if(follow.getError()){
//                                    RMsg.toastHere(context,follow.getMESSAGE());
//                                }else if(follow.getAlreadyFollowed()){
//                                    users.remove(position);
//                                    adapter.notifyAdapter(users);
//                                    RMsg.toastHere(context,follow.getMESSAGE());
//                                } else if(follow.getFollowed()){
//                                    //fBtn.setText("Followed");
//                                    Snackbar.make(getCurrentFocus(),"User followed",Snackbar.LENGTH_LONG).show();
//                                    adapter.notifyAdapter(users);
//                                }else {
//                                    //finish();
//                                    Snackbar.make(getCurrentFocus(),follow.getMESSAGE(),Snackbar.LENGTH_LONG).show();
//                                }
//                            }else {
//                                RMsg.toastHere(context,RMsg.AUTH_ERROR_MESSAGE);
//                            }
//                        }else {
//                            RMsg.toastHere(context,RMsg.REQ_ERROR_MESSAGE);
//                            RMsg.logHere(response.raw().toString());
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<Followers> call, Throwable t) {
//                        Snackbar.make(getCurrentFocus(),t.getMessage(),Snackbar.LENGTH_LONG).show();
//                    }
//                });
//              //  Snackbar.make(getCurrentFocus(),users.get(position).getFULL_NAME(),Snackbar.LENGTH_LONG).show();
//            }
//        });

        usersRV.setAdapter(adapter);
        fetchUsers();
    }

    private void fetchUsers(){
        RetroLib.geApiService().getUserFollowers(user.getTOKEN(), PROFILE_ID).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    User resUser = response.body();
                    if(resUser.getIS_AUTHENTICATED()){
                        if(resUser.getIS_EMPTY()){
                            RMsg.toastHere(context,resUser.getMESSAGE());
                        }else if(resUser.getISFOUND()){
                            users = resUser.getUSERS();
                            adapter.notifyAdapter(users);
                        }else {
                            //finish();
                        }
                    }else {
                        RMsg.toastHere(context,RMsg.AUTH_ERROR_MESSAGE);
                    }
                }else {
                    RMsg.toastHere(context,RMsg.REQ_ERROR_MESSAGE);
                    RMsg.logHere(response.raw().toString());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                RMsg.toastHere(context, t.getMessage());
            }
        });
    }

    public void logout(){
        PrefStorage.getEditor(this).remove(PrefStorage.USER_PREF_DETAILS).commit();
        PrefStorage.getEditor(this).remove(PrefStorage.START_NEXT_ACTIVITY).commit();
        PrefStorage.getEditor(this).remove(PrefStorage.AFTER_STARTUP_ACTIVITY).commit();
        Intent loginAct = new Intent(this,LoginActivity.class);
        startActivity(loginAct);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    private void getUserIDFromIntent(){
        PROFILE_ID = getIntent().getExtras().getInt("profile_id",0);
    }
}
