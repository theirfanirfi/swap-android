package swap.irfanullah.com.swap.SwapRequestActivityFragments;

import android.app.AlertDialog;
import android.content.Context;
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
import swap.irfanullah.com.swap.Adapters.SwapsAdapter;
import swap.irfanullah.com.swap.CustomComponents.ReviewDialog;
import swap.irfanullah.com.swap.Libraries.RetroLib;
import swap.irfanullah.com.swap.Models.RMsg;
import swap.irfanullah.com.swap.Models.Status;
import swap.irfanullah.com.swap.Models.Swap;
import swap.irfanullah.com.swap.Models.SwapsTab;
import swap.irfanullah.com.swap.R;
import swap.irfanullah.com.swap.Storage.PrefStorage;

public class SwapReviewFragment extends Fragment implements SwapsReviewNotificationsAdapter.ReviewListener, SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView sRV;
    private SwapsReviewNotificationsAdapter swapsAdapter;
    private ArrayList<Status> statuses;
    private ProgressBar progressBar;
    SwapsTab swapsTab;
    ArrayList<SwapsTab> swapsTabArrayList;
    private final static String LOGS = "SWAP_TAB_LOGS";
    SwipeRefreshLayout swipeRefreshLayout;
    ReviewDialog reviewDialog;
    Context context;

    public SwapReviewFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_swap_requests, container, false);
        context = getContext();
        sRV = rootView.findViewById(R.id.swapReqRV);
        swipeRefreshLayout = rootView.findViewById(R.id.swipeRefreshLayout);

        progressBar = rootView.findViewById(R.id.requestProgressBar);
        reviewDialog = new ReviewDialog();
        makeRequest();
        swapsAdapter = new SwapsReviewNotificationsAdapter(getActivity(), swapsTabArrayList);
        swapsAdapter.setOnReviewClickListenr(this);
        sRV.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        sRV.setLayoutManager(layoutManager);
        sRV.setAdapter(swapsAdapter);

        return rootView;
    }

    private void makeRequest() {
        swapsTabArrayList = new ArrayList<>();
        RetroLib.geApiService().getSwapsForReview(PrefStorage.getUser(context).getTOKEN()).enqueue(new Callback<SwapsTab>() {
            @Override
            public void onResponse(Call<SwapsTab> call, Response<SwapsTab> response) {
                if (response.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    swapsTab = response.body();
                        if (swapsTab.getIS_AUTHENTICATED()) {

                            if (swapsTab.getIS_FOUND()) {

                                swapsTabArrayList = swapsTab.getSwapsTabArrayList();
                               // notifySwapsAd(swapsTabArrayList);
                                swapsAdapter.notifySwapsAdapter(swapsTabArrayList);
                                if(swipeRefreshLayout.isRefreshing()){
                                    swipeRefreshLayout.setRefreshing(false);
                                }

                            } else {
                                // Toast.makeText(getContext(), "You don't have any swaped status.", Toast.LENGTH_LONG).show();
                                if(swipeRefreshLayout.isRefreshing()){
                                    swipeRefreshLayout.setRefreshing(false);
                                }
                            }
                        } else {
                           // Toast.makeText(getContext(), RMsg.AUTH_ERROR_MESSAGE, Toast.LENGTH_LONG).show();
                            if(swipeRefreshLayout.isRefreshing()){
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        }

                } else {
                    //  Toast.makeText(getContext(), RMsg.REQ_ERROR_MESSAGE, Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                    if(swipeRefreshLayout.isRefreshing()){
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
            }

            @Override
            public void onFailure(Call<SwapsTab> call, Throwable t) {
               // Toast.makeText(getContext(), t.toString(), Toast.LENGTH_LONG).show();
                if(swipeRefreshLayout.isRefreshing()){
                    swipeRefreshLayout.setRefreshing(false);
                  //  swipeRefreshLayout.set
                }
            }
        });


    }

    @Override
    public void onRefresh() {
        makeRequest();
    }

    public void notifySwapsAd(ArrayList<SwapsTab> swapsTabs) {
        swapsAdapter.notifySwapsAdapter(swapsTabs);
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            makeRequest();
        }

    }

    @Override
    public void onReviewClicked(SwapsTab swap) {
        reviewDialog = new ReviewDialog();
        Bundle bundle = new Bundle();
        bundle.putString("swap_id",Integer.toString(swap.getSWAP_ID()));
        reviewDialog.setArguments(bundle);
        reviewDialog.show(getFragmentManager(),"rev_d");
    }
}
