package swap.irfanullah.com.swap.Fragments.Profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
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
import swap.irfanullah.com.swap.Adapters.Profile.SwapsAdapter;
import swap.irfanullah.com.swap.Libraries.RetroLib;
import swap.irfanullah.com.swap.Models.RMsg;
import swap.irfanullah.com.swap.Models.Status;
import swap.irfanullah.com.swap.Models.Swap;
import swap.irfanullah.com.swap.Models.SwapsTab;
import swap.irfanullah.com.swap.R;
import swap.irfanullah.com.swap.Storage.PrefStorage;

public class SwapsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private RecyclerView sRV;
    private SwapsAdapter swapsAdapter;
    private ArrayList<Status> statuses;
    private ProgressBar progressBar;
    SwapsTab swapsTab;
    ArrayList<SwapsTab> swapsTabArrayList;
    private final static String LOGS = "SWAP_TAB_LOGS";
    private int USER_ID = 0;

    public SwapsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        USER_ID = getArguments().getInt("user_id");
        Log.i(RMsg.LOG_MESSAGE+"PROFILE",Integer.toString(USER_ID));
        View rootView = inflater.inflate(R.layout.fragment_swap, container, false);
        sRV = rootView.findViewById(R.id.swapRV);
        progressBar = rootView.findViewById(R.id.progressFragmentSwap);

        makeRequest();
        swapsAdapter = new SwapsAdapter(getActivity(), swapsTabArrayList);
        sRV.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        sRV.setLayoutManager(layoutManager);
        sRV.setAdapter(swapsAdapter);

        return rootView;
    }

    private void makeRequest() {
        swapsTabArrayList = new ArrayList<>();
        RetroLib.geApiService().getUserSwaps(USER_ID).enqueue(new Callback<SwapsTab>() {
            @Override
            public void onResponse(Call<SwapsTab> call, Response<SwapsTab> response) {
                if (response.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    swapsTab = response.body();
                    if (swapsTab != null) {
                        if (swapsTab.getIS_AUTHENTICATED()) {

                            if (swapsTab.getIS_FOUND()) {

                                swapsTabArrayList = swapsTab.getSwapsTabArrayList();
                                notifySwapsAd(swapsTabArrayList);


                            } else {
                               // Toast.makeText(getContext(), "You don't have any swaped status.", Toast.LENGTH_LONG).show();
                            }
                        } else {
                           // Toast.makeText(getContext(), RMsg.AUTH_ERROR_MESSAGE, Toast.LENGTH_LONG).show();
                        }

                    } else {
                       // Log.i(LOGS, "NULL");

                    }

                } else {
                    Toast.makeText(getContext(), RMsg.REQ_ERROR_MESSAGE, Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<SwapsTab> call, Throwable t) {
                Toast.makeText(getContext(), t.toString(), Toast.LENGTH_LONG).show();
            }
        });


    }

    @Override
    public void onRefresh() {
        swapsAdapter.notifyDataSetChanged();
    }

    public void notifySwapsAd(ArrayList<SwapsTab> swapsTabs) {
        swapsAdapter.notifySwapsAdapter(swapsTabs);
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            makeRequest();
           // Toast.makeText(getContext(),"SWAPS fragment working visible",Toast.LENGTH_LONG).show();

        }

    }
}
