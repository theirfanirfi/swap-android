package swap.irfanullah.com.swap.StatusActivityFrags;

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
import swap.irfanullah.com.swap.Adapters.SingleStatusAdapter;
import swap.irfanullah.com.swap.Adapters.StatusAdapter;
import swap.irfanullah.com.swap.Libraries.RetroLib;
import swap.irfanullah.com.swap.Models.SingleStatusModel;
import swap.irfanullah.com.swap.Models.Status;
import swap.irfanullah.com.swap.Models.User;
import swap.irfanullah.com.swap.R;
import swap.irfanullah.com.swap.Storage.PrefStorage;

public class RatersFragment extends Fragment {

    private RecyclerView sRV;
    private RatersAdapter ratersAdapter;
    private ArrayList<User> ratersList;
    private Context context;
    private ProgressBar progressBar;
    String STATUS_ID = "";
    Bundle bundle;

    public RatersFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_statuses, container, false);
        sRV = rootView.findViewById(R.id.chatRV);
        progressBar = rootView.findViewById(R.id.statusLoadingProgressbar);
        context = getContext();
        bundle = getArguments();
        STATUS_ID = bundle.getString("status_id");
        makeRequest();
        ratersAdapter= new RatersAdapter(getActivity(),ratersList);
        sRV.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        sRV.setLayoutManager(layoutManager);
        sRV.setAdapter(ratersAdapter);
         return rootView;
    }

    private void makeRequest() {
        ratersList = new ArrayList<>();
        RetroLib.geApiService().getStatusRaters(PrefStorage.getUser(context).getTOKEN(),STATUS_ID).enqueue(new Callback<SingleStatusModel>() {
            @Override
            public void onResponse(Call<SingleStatusModel> call, Response<SingleStatusModel> response) {
                if(response.isSuccessful()) {

                    SingleStatusModel raters = response.body();

                    if(raters.getIS_ERROR() || !raters.getIS_AUTHENTICATED()){

                    }else if(raters.getIS_FOUND()){
                        progressBar.setVisibility(View.GONE);
                        ratersList = raters.getRATERS();
                        notifyAdapter(ratersList);
                    }else {
                        //do nothing..
                    }

                }
                else {
                    Log.i("STATUES: ","NOT SUCCESSFULL "+response.raw().toString());
                }
            }

            @Override
            public void onFailure(Call<SingleStatusModel> call, Throwable t) {
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

    public void notifyAdapter(ArrayList<User> raters){
        ratersAdapter.notifyAdapter(raters);
    }
}
