package swap.irfanullah.com.swap.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import swap.irfanullah.com.swap.Adapters.StatusAdapter;
import swap.irfanullah.com.swap.ComposeStatusActivity;
import swap.irfanullah.com.swap.HomeActivity;
import swap.irfanullah.com.swap.Libraries.RetroLib;
import swap.irfanullah.com.swap.Models.Status;
import swap.irfanullah.com.swap.R;
import swap.irfanullah.com.swap.Storage.PrefStorage;

public class StatusesFragment extends Fragment {

    private RecyclerView sRV;
    private StatusAdapter statusAdapter;
    private ArrayList<Status> statuses;
    private Context context;
    private ProgressBar progressBar;


    public StatusesFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_statuses, container, false);
        sRV = rootView.findViewById(R.id.chatRV);
        progressBar = rootView.findViewById(R.id.statusLoadingProgressbar);
        context = getContext();

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent composeAct = new Intent(context, ComposeStatusActivity.class);
                startActivity(composeAct);
            }
        });


        makeRequest();
        statusAdapter= new StatusAdapter(getActivity(),statuses,1);
        sRV.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        sRV.setLayoutManager(layoutManager);
        sRV.setAdapter(statusAdapter);
         return rootView;
    }

    private void makeRequest() {
        statuses = new ArrayList<>();
        RetroLib.geApiService().getStatuses(PrefStorage.getUser(context).getTOKEN()).enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if(response.isSuccessful()) {

                    Status status = response.body();
                    if(status.getAuthenticated()) {
                        if(status.getFound()) {
                            progressBar.setVisibility(View.GONE);
                            statuses = status.getSTATUSES();
                            notifyAdapter(statuses);
                        }
                        else {
                            //Toast.makeText(getContext(),status.getMESSAGE(),Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                        Toast.makeText(getContext(),status.getMESSAGE(),Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Log.i("STATUES: ","NOT SUCCESSFULL "+response.raw().toString());
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Toast.makeText(getContext(),"ERROR: "+t.toString(),Toast.LENGTH_LONG).show();
                Log.i("STATUES: ","NOT SUCCESSFULL "+t.toString());

            }
        });
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            //makeRequest();
        }
    }

    public void notifyAdapter(ArrayList<Status> statuses){
        statusAdapter.notifyAdapter(statuses);
    }
}
