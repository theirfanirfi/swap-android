package swap.irfanullah.com.swap;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import swap.irfanullah.com.swap.Adapters.SwapWithAdapter;
import swap.irfanullah.com.swap.Libraries.RetroLib;
import swap.irfanullah.com.swap.Models.Followers;
import swap.irfanullah.com.swap.Models.RMsg;
import swap.irfanullah.com.swap.Models.Swap;
import swap.irfanullah.com.swap.Storage.PrefStorage;


public class UserFollowersActivityToBeBeleted extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView sWRV;
    private SwapWithAdapter swapWithAdapter;
    private ArrayList<Followers> followersList, filteredArrayList;
    private EditText searchTextField;
    private int INTENT_STATUS_ID = 0;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swap_with);
        toolbar = findViewById(R.id.swapWithToolbar);
       // toolbar.setTitle("");
        setSupportActionBar(toolbar);
        searchTextField = findViewById(R.id.searchSwapedWithField);
        context = this;
        INTENT_STATUS_ID = getIntent().getExtras().getInt("status_id");
        followersList = new ArrayList<>();

        sWRV = findViewById(R.id.swapWithRV);

        RetroLib.geApiService().getFollowers(PrefStorage.getUser(this).getTOKEN(),INTENT_STATUS_ID).enqueue(new Callback<Followers>() {
            @Override
            public void onResponse(Call<Followers> call, Response<Followers> response) {
                if(response.isSuccessful()){
                    Followers followers = response.body();
                    if(followers.getAuthenticated()){
                        if(followers.getFound()){
                            if(followers.getFollowersFound()){
                                ArrayList<Followers> followersArrayList = followers.getFollowers();
                                followersList = followers.getFollowers();
                                int status_id = followers.getSTATUS_ID();
                                ArrayList<Swap> swaps = followers.getSWAPS();

                                swapWithAdapter = new SwapWithAdapter(getApplicationContext(),followersArrayList,swaps,status_id);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                                sWRV.setLayoutManager(layoutManager);
                                sWRV.setAdapter(swapWithAdapter);
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


        searchTextField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //Toast.makeText(getApplicationContext(),s.toString(),Toast.LENGTH_LONG).show();
                filter(s.toString());
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

        swapWithAdapter.FilterRV(filteredArrayList);

    }
}
