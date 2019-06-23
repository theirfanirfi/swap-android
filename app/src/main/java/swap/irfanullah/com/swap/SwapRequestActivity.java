package swap.irfanullah.com.swap;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import swap.irfanullah.com.swap.Adapters.SwapRequestAdapter;
import swap.irfanullah.com.swap.Libraries.RetroLib;
import swap.irfanullah.com.swap.Models.Notification;
import swap.irfanullah.com.swap.Models.RMsg;
import swap.irfanullah.com.swap.Models.Swap;
import swap.irfanullah.com.swap.Storage.PrefStorage;

public class SwapRequestActivity extends AppCompatActivity implements SwapRequestAdapter.RequestListener{

    private RecyclerView rv;
    private SwapRequestAdapter swapRequestAdapter;
    private Context context;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Notification> swapRequestsList;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swap_request);

        initObjects();
    }

    private void initObjects(){
        context = this;
        rv = findViewById(R.id.swapReqRV);
        progressBar = findViewById(R.id.requestProgressBar);
        swapRequestsList = new ArrayList<>();
        swapRequestAdapter = new SwapRequestAdapter(context,swapRequestsList);
        layoutManager = new LinearLayoutManager(context);
        rv.setAdapter(swapRequestAdapter);
        rv.setLayoutManager(layoutManager);
        rv.setHasFixedSize(true);
        swapRequestAdapter.setRequestListener(this);
        getSupportActionBar().setTitle("Swap Requests");
        swapRequests();
    }

    @Override
    public void onApprove(View view, int position, Notification notification) {
       // RMsg.logHere(notification.toString());
        approveSwap(notification,position);
    }

    @Override
    public void onDecline(View view, int position, Notification notification) {
        declineSwap(notification,position);
    }

    @Override
    public void onProfile(View view, int position, Notification notification) {

    }

    @Override
    public void onStatus(View view, int position, Notification notification) {
        startStatusActivity(notification,position, false);
    }

    private void startStatusActivity(Notification notification, int position, boolean isAccepted){
        Intent statusActivity = new Intent(context,StatusActivity.class);
        statusActivity.putExtra("status_id",notification.getSTATUS_ID());
        statusActivity.putExtra("position",position);
        statusActivity.putExtra("notification_id",notification.getNOTIFICATION_ID());
        statusActivity.putExtra("is_swap_request","yes");
        if(isAccepted) {
            statusActivity.putExtra("is_accepted", 1);
        }else {
            statusActivity.putExtra("is_accepted",notification.getIS_ACCEPTED());
        }
        statusActivity.putExtra("is_browse_status","");
        startActivity(statusActivity);
    }

    private void swapRequests(){
        RetroLib.geApiService().getSwapRequestNotifications(PrefStorage.getUser(context).getTOKEN()).enqueue(new Callback<Notification>() {
            @Override
            public void onResponse(Call<Notification> call, Response<Notification> response) {
                if(response.isSuccessful()){
                    Notification notification = response.body();
                    if(notification.getIS_AUTHENTICATED()){
                        if(notification.getIS_FOUND()){
                            swapRequestsList = notification.getNotifications();
                            notifyAdaper(swapRequestsList);

                        }else {
                            Toast.makeText(context,RMsg.NOTIFICATIONS_NOT_FOUND_MESSAGE,Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Toast.makeText(context,RMsg.AUTH_ERROR_MESSAGE,Toast.LENGTH_LONG).show();

                    }
                }else {
                    Toast.makeText(context,RMsg.REQ_ERROR_MESSAGE,Toast.LENGTH_LONG).show();
                }

                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<Notification> call, Throwable t) {
                Toast.makeText(context,t.toString(),Toast.LENGTH_LONG).show();

            }
        });

    }

    private void notifyAdaper(ArrayList<Notification> swapRequestsList) {
        swapRequestAdapter.notifyAdapter(swapRequestsList);
    }

    public void approveSwap(final Notification notification, final int position){
        RetroLib.geApiService().approveSwap(PrefStorage.getUser(context).getTOKEN(),notification.getNOTIFICATION_ID()).enqueue(new Callback<Swap>() {
            @Override
            public void onResponse(Call<Swap> call, Response<Swap> response) {
                if(response.isSuccessful()){
                    Swap swap = response.body();
                    if(swap.getAuthenticated()){
                        if(swap.getError()){
                            RMsg.toastHere(context,swap.getMESSAGE());
                        }else if(swap.getApproved()){
                          //  menu.setGroupVisible(R.id.action_group,false);
                            swapRequestsList.remove(position);
                            swapRequestAdapter.notifyAdapter(swapRequestsList);
                            RMsg.toastHere(context,swap.getMESSAGE());
                            startStatusActivity(notification,position, true);
                        }else {
                            RMsg.toastHere(context,swap.getMESSAGE());
                        }
                    }else {
                        RMsg.toastHere(context,RMsg.AUTH_ERROR_MESSAGE);
                    }

                }else {
                    RMsg.logHere(response.raw().toString());
                    RMsg.toastHere(context,RMsg.REQ_ERROR_MESSAGE);
                }
            }

            @Override
            public void onFailure(Call<Swap> call, Throwable t) {
                RMsg.toastHere(context,t.toString());
            }
        });
    }


    public void declineSwap(final Notification notification, final int position){
        RetroLib.geApiService().declineSwap(PrefStorage.getUser(context).getTOKEN(),notification.getNOTIFICATION_ID()).enqueue(new Callback<Swap>() {
            @Override
            public void onResponse(Call<Swap> call, Response<Swap> response) {
                if(response.isSuccessful()){
                    Swap swap = response.body();
                    if(swap.getAuthenticated()){
                        if(swap.getError()){
                            RMsg.toastHere(context,swap.getMESSAGE());
                        }else if(swap.getDeclined()){
                            //  menu.setGroupVisible(R.id.action_group,false);
                            swapRequestsList.remove(position);
                            swapRequestAdapter.notifyAdapter(swapRequestsList);
                            RMsg.toastHere(context,swap.getMESSAGE());
                        }else {
                            RMsg.toastHere(context,swap.getMESSAGE());
                        }
                    }else {
                        RMsg.toastHere(context,RMsg.AUTH_ERROR_MESSAGE);
                    }

                }else {
                    RMsg.logHere(response.raw().toString());
                    RMsg.toastHere(context,RMsg.REQ_ERROR_MESSAGE);
                }
            }

            @Override
            public void onFailure(Call<Swap> call, Throwable t) {
                RMsg.toastHere(context,t.toString());
            }
        });
    }


    private void viewStatus(){
    }
}
