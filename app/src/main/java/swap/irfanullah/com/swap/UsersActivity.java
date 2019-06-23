package swap.irfanullah.com.swap;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import swap.irfanullah.com.swap.Adapters.FollowersAdapter;
import swap.irfanullah.com.swap.Libraries.RetroLib;
import swap.irfanullah.com.swap.Models.Followers;
import swap.irfanullah.com.swap.Models.RMsg;
import swap.irfanullah.com.swap.Models.User;
import swap.irfanullah.com.swap.Storage.PrefStorage;

public class UsersActivity extends AppCompatActivity {
    private Context context;
    private RecyclerView rv;
    private FollowersAdapter followersAdapter;
    private User user;
    private ProgressBar progressBar;
    private ArrayList<Followers> followers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        getSupportActionBar().setTitle("Swap Users");
        initializeObjects();
        getUsers();
    }

    private void getUsers() {
        RetroLib.geApiService().getUsers(user.getTOKEN()).enqueue(new Callback<Followers>() {
            @Override
            public void onResponse(Call<Followers> call, Response<Followers> response) {
                if(response.isSuccessful()){
                    Followers followers = response.body();
                    if(followers.getError()){
                        RMsg.toastHere(context,followers.getMESSAGE());
                    }else {
                        if(followers.getAuthenticated()){
                            if(followers.getFound()){
                                notifyAdapter(followers.getFollowers());
                            }else {
                                RMsg.toastHere(context,followers.getMESSAGE());

                            }
                        }else {
                            RMsg.toastHere(context,followers.getMESSAGE());
                        }
                    }
                }else {
                    RMsg.toastHere(context,RMsg.REQ_ERROR_MESSAGE);
                }
            }

            @Override
            public void onFailure(Call<Followers> call, Throwable t) {
                RMsg.toastHere(context,t.toString());
            }
        });
        progressBar.setVisibility(View.GONE);
    }

    private void initializeObjects() {
        context = this;
        user = PrefStorage.getUser(context);
        followers = new ArrayList<>();
        rv = findViewById(R.id.usersRecyclerView);
        progressBar = findViewById(R.id.progressBar);
        followersAdapter = new FollowersAdapter(context,followers, user);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(followersAdapter);

    }

    public void notifyAdapter(ArrayList<Followers> followers){
        followersAdapter.notifyAdapter(followers);
    }
}
