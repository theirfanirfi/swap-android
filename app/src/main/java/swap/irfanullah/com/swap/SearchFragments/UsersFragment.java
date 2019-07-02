package swap.irfanullah.com.swap.SearchFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import swap.irfanullah.com.swap.Adapters.BrowseStatusAdapter;
import swap.irfanullah.com.swap.Adapters.SearchFragmentUsersAdapter;
import swap.irfanullah.com.swap.Adapters.SwapWithAdapter;
import swap.irfanullah.com.swap.Libraries.RetroLib;
import swap.irfanullah.com.swap.Models.Followers;
import swap.irfanullah.com.swap.Models.RMsg;
import swap.irfanullah.com.swap.Models.Status;
import swap.irfanullah.com.swap.Models.Swap;
import swap.irfanullah.com.swap.Models.User;
import swap.irfanullah.com.swap.R;
import swap.irfanullah.com.swap.Storage.PrefStorage;

public class UsersFragment extends Fragment implements SearchFragmentUsersAdapter.FollowListener {

    private RecyclerView sRV;
    private SearchFragmentUsersAdapter searchFragmentUsersAdapter;
    private ArrayList<User> users;
    private Context context;
    private ProgressBar progressBar;
    TextView no_user_found;
    String keyword = "###";

    public UsersFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.search_users_fragment, container, false);
        sRV = rootView.findViewById(R.id.chatRV);
        progressBar = rootView.findViewById(R.id.statusLoadingProgressbar);
        no_user_found = rootView.findViewById(R.id.user_not_found);
        context = getContext();
        keyword = getArguments().getString("search_keyword");
        users = new ArrayList<>();
        searchFragmentUsersAdapter= new SearchFragmentUsersAdapter(getActivity(),users);
        searchFragmentUsersAdapter.setOnFollowClickListener(this);
        sRV.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        sRV.setLayoutManager(layoutManager);
        sRV.setAdapter(searchFragmentUsersAdapter);
        makeRequest(keyword);
        return rootView;
    }

    private void makeRequest(String keyword) {
        RetroLib.geApiService().searchUsers(PrefStorage.getUser(context).getTOKEN(),keyword).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    User user = response.body();
                    if(user.getIS_AUTHENTICATED()){
                        if(user.getISFOUND()){
                                users = user.getUSERS();
                                notifyAdapter(users);
                                no_user_found.setVisibility(View.GONE);
                                progressBar.setVisibility(View.GONE);

                        }else {
                            no_user_found.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            //Toast.makeText(context,user.getMESSAGE(),Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(context,RMsg.AUTH_ERROR_MESSAGE,Toast.LENGTH_LONG).show();

                    }
                }else {
                    Toast.makeText(context,RMsg.REQ_ERROR_MESSAGE,Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.i("Users: ",t.toString());

            }
        });

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            //makeRequest();
        }
    }

    public void notifyAdapter(ArrayList<User> users){
        searchFragmentUsersAdapter.notifyAdapter(users);
    }

    @Override
    public void onFollow(View v, int position, User user) {

        if(user.getFID() > 0) {
            unFollowUser(user,position,v);
        }else {
            followUser(user,position, v);
        }

    }

    private void followUser(User user, final int position,final View v){
        RetroLib.geApiService().follow(PrefStorage.getUser(context).getTOKEN(),user.getUSER_ID()).enqueue(new Callback<Followers>() {
            @Override
            public void onResponse(Call<Followers> call, Response<Followers> response) {
                if(response.isSuccessful()){
                    Followers follow = response.body();
                    if(follow.getAuthenticated()){
                        if(follow.getError()){
                            RMsg.toastHere(context,follow.getMESSAGE());
                        }else if(follow.getAlreadyFollowed()){
                            Button btn = v.findViewById(R.id.followBtn);
                            btn.setText("Unfollow");
//                            users.remove(position);
//                            searchFragmentUsersAdapter.notifyAdapter(users);
                            RMsg.toastHere(context,follow.getMESSAGE());
                        } else if(follow.getFollowed()){
                            Button btn = v.findViewById(R.id.followBtn);
                            btn.setText("UnFollow");
//                            users.remove(position);
//                            searchFragmentUsersAdapter.notifyAdapter(users);
                            RMsg.toastHere(context,follow.getMESSAGE());
                        }else {
                            RMsg.toastHere(context,follow.getMESSAGE());
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
            public void onFailure(Call<Followers> call, Throwable t) {
                RMsg.toastHere(context,t.getMessage());

            }
        });
    }

    private void unFollowUser(User user, final int position, final View v){
        RetroLib.geApiService().unfollow(PrefStorage.getUser(context).getTOKEN(),user.getUSER_ID()).enqueue(new Callback<Followers>() {
            @Override
            public void onResponse(Call<Followers> call, Response<Followers> response) {
                if(response.isSuccessful()){
                    Followers fo = response.body();
                    if(fo.getAuthenticated()){
                        if(fo.getError()){
                            RMsg.toastHere(context,fo.getMESSAGE());
                        }else {
                            if(fo.getUnFollowed()){
//                                users.remove(position);
//                                searchFragmentUsersAdapter.notifyAdapter(users);
                                Button btn = v.findViewById(R.id.followBtn);
                                btn.setText("Follow");

                                RMsg.toastHere(context,fo.getMESSAGE());
                            }else if(fo.getAlreadyUnFollowed()){
                                Button btn = v.findViewById(R.id.followBtn);
                                btn.setText("Follow");

//                                users.remove(position);
//                                searchFragmentUsersAdapter.notifyAdapter(users);
                                RMsg.toastHere(context,fo.getMESSAGE());
                            }else {
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
