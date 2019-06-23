package swap.irfanullah.com.swap.Fragments;

import android.content.Context;
import android.os.Bundle;
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
import swap.irfanullah.com.swap.Adapters.BrowseStatusAdapter;
import swap.irfanullah.com.swap.Libraries.RetroLib;
import swap.irfanullah.com.swap.Models.Status;
import swap.irfanullah.com.swap.R;
import swap.irfanullah.com.swap.Storage.PrefStorage;

public class DiscoverStatusesFragment extends Fragment {

    private RecyclerView sRV;
    private BrowseStatusAdapter statusAdapter;
    private ArrayList<Status> statuses;
    private Context context;
    private ProgressBar progressBar;

    public DiscoverStatusesFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_statuses, container, false);
        sRV = rootView.findViewById(R.id.chatRV);
        progressBar = rootView.findViewById(R.id.statusLoadingProgressbar);
        context = getContext();
        makeRequest();
        statusAdapter= new BrowseStatusAdapter(getActivity(),statuses);
        sRV.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        sRV.setLayoutManager(layoutManager);
        sRV.setAdapter(statusAdapter);
         return rootView;
    }

    private void makeRequest() {
        statuses = new ArrayList<>();
        RetroLib.geApiService().discoverStatuses(PrefStorage.getUser(context).getTOKEN()).enqueue(new Callback<Status>() {
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
                            Toast.makeText(getContext(),status.getMESSAGE(),Toast.LENGTH_LONG).show();
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
