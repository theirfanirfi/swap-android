package swap.irfanullah.com.swap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import swap.irfanullah.com.swap.Adapters.InviteToGroupAdapter;
import swap.irfanullah.com.swap.Adapters.TagUsersAdapter;
import swap.irfanullah.com.swap.Libraries.RetroLib;
import swap.irfanullah.com.swap.Models.Followers;
import swap.irfanullah.com.swap.Models.Groups;
import swap.irfanullah.com.swap.Models.RMsg;
import swap.irfanullah.com.swap.Models.User;
import swap.irfanullah.com.swap.Storage.PrefStorage;


public class TagUsersInStatusActivity extends AppCompatActivity implements InviteToGroupAdapter.InviteClickListener {

    private Toolbar toolbar;
    private RecyclerView sWRV;
    private InviteToGroupAdapter inviteToGroupAdapter;
    private ArrayList<Followers> followersList, filteredArrayList;
    private SearchView searchTextField;
    private int GROUP_ID = 0;
    private ArrayList<Integer> TAG_IDS;
    private Context context;


    private TagUsersAdapter tagUsersAdapter;
    private ArrayList<User> tagUsersList, tagUsersListFilteredList;
    private RecyclerView tagedRV;
    private RecyclerView.LayoutManager tagLayoutManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tag_users_in_status_activity);
        TAG_IDS = new ArrayList<>();
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
//        GROUP_ID = getIntent().getExtras().getInt("group_id");
        followersList = new ArrayList<>();
        inviteToGroupAdapter = new InviteToGroupAdapter(context,followersList);
        inviteToGroupAdapter.setOnInviteClickListener(this);


        sWRV = findViewById(R.id.swapWithRV);
        tagedRV = findViewById(R.id.tagUsersRV);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);

        tagLayoutManager = new GridLayoutManager(this,4);
        tagedRV.setLayoutManager(tagLayoutManager);
        tagUsersList = new ArrayList<>();
        tagUsersAdapter = new TagUsersAdapter(context,this.tagUsersList);
        tagedRV.setAdapter(tagUsersAdapter);
        tagedRV.setHasFixedSize(true);

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
        }else if(item.getItemId() == R.id.done_tagging){
            userTagged();
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
        RetroLib.geApiService().getUserToTag(PrefStorage.getUser(context).getTOKEN(),followed_user_id).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                if (user.getIS_ERROR()) {

                    Toast.makeText(context, user.getMESSAGE(), Toast.LENGTH_LONG).show();
                    // buttonView.setChecked(false);
                } else if (user.getISFOUND()) {
                    // Toast.makeText(context, groupMessages.getRESPONSE_MESSAGE(), Toast.LENGTH_LONG).show();
                    tagUser(user.getUSER(),button);


                    RMsg.logHere(TAG_IDS.toString());
//                    if(tagUsersList.size() <= 4){
//                        tagLayoutManager = new GridLayoutManager(context,5);
//                        tagedRV.setLayoutManager(tagLayoutManager);
//
//                    }else {
//                        tagLayoutManager = new GridLayoutManager(context,tagedRV.getWidth()%tagUsersList.size());
//                        tagedRV.setLayoutManager(tagLayoutManager);
//                    }



                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(context, t.toString(), Toast.LENGTH_LONG).show();

            }
        });
    }

    private boolean tagUser(User user, Button button){
        boolean isFound = false;
        int i = 0;

        for(User u : this.tagUsersList){
            if(u.getUSER_ID() == user.getUSER_ID()){
                this.tagUsersList.remove(u);
                TAG_IDS.remove(i);
                tagUsersAdapter.FilterRV(tagUsersList);
                button.setText("Tag");
                isFound =  true;
                break;
            }else{
                isFound =  false;
            }

            i++;
        }

        if(!isFound){
            TAG_IDS.add(user.getUSER_ID());
            tagUsersList.add(user);
            tagUsersAdapter.FilterRV(tagUsersList);
            button.setText("UnTag");
        }

        return !isFound;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tag_user_act_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }


    private void userTagged(){
        final Intent returnIntent = new Intent();
        returnIntent.putExtra("tagged_users",TAG_IDS);
        //returnIntent.putExtra("tagged_users",TAG_IDS);
        if(TAG_IDS.size() > 0) {
            int first_user_id = TAG_IDS.get(0);
            RetroLib.geApiService().getUserToTag(PrefStorage.getUser(context).getTOKEN(), first_user_id).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    User user = response.body();
                    if (user.getIS_ERROR()) {

                        Toast.makeText(context, user.getMESSAGE(), Toast.LENGTH_LONG).show();
                        // buttonView.setChecked(false);
                    } else if (user.getISFOUND()) {
                        String tag_description = TAG_IDS.size() > 1 ? " and " + TAG_IDS.size() + " others " : "";
                        returnIntent.putExtra("tagged_users_detail", "with " + user.getUSER().getFULL_NAME() + tag_description);
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();

                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(context, t.toString(), Toast.LENGTH_LONG).show();

                }
            });
        }else {
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }

    }
}
