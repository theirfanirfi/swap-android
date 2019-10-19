package swap.irfanullah.com.swap;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import swap.irfanullah.com.swap.Adapters.ForwardChatMessageAdapter;
import swap.irfanullah.com.swap.Adapters.InviteToGroupAdapter;
import swap.irfanullah.com.swap.Libraries.RetroLib;
import swap.irfanullah.com.swap.Models.Followers;
import swap.irfanullah.com.swap.Models.GroupMessages;
import swap.irfanullah.com.swap.Models.RMsg;
import swap.irfanullah.com.swap.Storage.PrefStorage;


public class InviteToGroupActivity extends AppCompatActivity implements InviteToGroupAdapter.InviteClickListener {

    private Toolbar toolbar;
    private RecyclerView sWRV;
    private InviteToGroupAdapter inviteToGroupAdapter;
    private ArrayList<Followers> followersList, filteredArrayList;
    private SearchView searchTextField;
    private int INTENT_MESSAGE_ID = 0;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forward_message_activity);
//        toolbar = findViewById(R.id.swapWithToolbar);
//       // toolbar.setTitle("");
//
//        setSupportActionBar(toolbar);
//
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
        searchTextField = findViewById(R.id.searchView);
        context = this;
        INTENT_MESSAGE_ID = getIntent().getExtras().getInt("message_id");
        followersList = new ArrayList<>();
        inviteToGroupAdapter = new InviteToGroupAdapter(context,followersList);
        inviteToGroupAdapter.setOnInviteClickListener(this);


        sWRV = findViewById(R.id.swapWithRV);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        sWRV.setLayoutManager(layoutManager);
        sWRV.setAdapter(inviteToGroupAdapter);
        RetroLib.geApiService().getFollowers(PrefStorage.getUser(this).getTOKEN(),0).enqueue(new Callback<Followers>() {
            @Override
            public void onResponse(Call<Followers> call, Response<Followers> response) {
                if(response.isSuccessful()){
                    Followers followers = response.body();
                    if(followers.getAuthenticated()){
                        if(followers.getFound()){
                            if(followers.getFollowersFound()){
                                ArrayList<Followers> followersArrayList = followers.getFollowers();
                                followersList = followers.getFollowers();
                                inviteToGroupAdapter.FilterRV(followersList);
                            }else {
                                Toast.makeText(context,followers.getMESSAGE(),Toast.LENGTH_LONG).show();
                            }
                        }else {
                            Toast.makeText(context,followers.getMESSAGE(),Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(context,RMsg.AUTH_ERROR_MESSAGE,Toast.LENGTH_LONG).show();

                    }
                }else {
                    Toast.makeText(context,RMsg.REQ_ERROR_MESSAGE,Toast.LENGTH_LONG).show();
                }

//                Followers followers = response.body();
//                ArrayList<Followers> followersArrayList = followers.getFollowers();
//                followersList = followers.getFollowers();
//                int status_id = followers.getSTATUS_ID();
//                ArrayList<Swap> swaps = followers.getSWAPS();
//
//                swapWithAdapter = new SwapWithAdapter(getApplicationContext(),followersArrayList,swaps,status_id);
//                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
//                sWRV.setLayoutManager(layoutManager);
//                sWRV.setAdapter(swapWithAdapter);
            }

            @Override
            public void onFailure(Call<Followers> call, Throwable t) {
                Log.i("FOLLOWERS: ",t.toString());

            }
        });

        searchTextField.setQuery("Search here",false);
        searchTextField.setQueryHint("Search");
        searchTextField.setEnabled(true);
        searchTextField.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filter(query.toString());
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText.toString());
                return true;
            }
        });



    }

    private void filter(String s) {
        filteredArrayList = new ArrayList<>();
        for(Followers item: followersList)
        {
            if(item.getUSERNAME().toLowerCase().contains(s.toLowerCase()))
            {
                filteredArrayList.add(item);
            }
        }

        inviteToGroupAdapter.FilterRV(filteredArrayList);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    @Override
    public void onInvite(int followed_user_id, final Button button) {
        RetroLib.geApiService().forwardMessageFromGroup(PrefStorage.getUser(context).getTOKEN(), followed_user_id, INTENT_MESSAGE_ID).enqueue(new Callback<GroupMessages>() {
            @Override
            public void onResponse(Call<GroupMessages> call, Response<GroupMessages> response) {
                GroupMessages groupMessages = response.body();
                if (groupMessages.getIS_ERROR()) {

                    Toast.makeText(context, groupMessages.getRESPONSE_MESSAGE(), Toast.LENGTH_LONG).show();
                    // buttonView.setChecked(false);
                } else if (groupMessages.getIS_SENT()) {
                    // Toast.makeText(context, groupMessages.getRESPONSE_MESSAGE(), Toast.LENGTH_LONG).show();
                    button.setText("Invited");

                }
            }

            @Override
            public void onFailure(Call<GroupMessages> call, Throwable t) {
                Toast.makeText(context, t.toString(), Toast.LENGTH_LONG).show();

            }
        });
    }
}
