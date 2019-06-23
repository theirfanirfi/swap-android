package swap.irfanullah.com.swap;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import swap.irfanullah.com.swap.Adapters.NotificationAdapter;
import swap.irfanullah.com.swap.Libraries.CReq;
import swap.irfanullah.com.swap.Libraries.RetroLib;
import swap.irfanullah.com.swap.Models.Notification;
import swap.irfanullah.com.swap.Models.RMsg;
import swap.irfanullah.com.swap.Models.Swap;
import swap.irfanullah.com.swap.Storage.PrefStorage;

public class NotificationActivity extends AppCompatActivity {

    RecyclerView rv;
    ProgressBar progressBar;
    NotificationAdapter notificationAdapter;
    ArrayList<Notification> notificationArrayList;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        getSupportActionBar().setTitle("Notifications");
        initializeObjects();
        makeRequest();
        implementSwipe();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    private void initializeObjects(){
        rv = findViewById(R.id.notificationsRV);
        progressBar = findViewById(R.id.progressBar2);
        notificationArrayList = new ArrayList<>();
        notificationAdapter = new NotificationAdapter(this,notificationArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(notificationAdapter);
        context = this;
    }
    private void makeRequest(){
        RetroLib.geApiService().getNotifications(PrefStorage.getUser(context).getTOKEN()).enqueue(new Callback<Notification>() {
            @Override
            public void onResponse(Call<Notification> call, Response<Notification> response) {
                if(response.isSuccessful()){
                    Notification notification = response.body();
                    if(notification.getIS_AUTHENTICATED()){
                        if(notification.getIS_FOUND()){
                            notificationArrayList = notification.getNotifications();
                            notifyAdaper(notificationArrayList);

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

    public void notifyAdaper(ArrayList<Notification> notificationArrayList){
      notificationAdapter.notifyAdaper(notificationArrayList);

    }

    public void implementSwipe(){
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                final int position = viewHolder.getAdapterPosition();
                Notification notification = notificationArrayList.get(position);
                int swap_id = notification.getSWAP_ID();
                if(notification.getIS_STATUS() == 1){
                   // RMsg.toastHere(context,"working "+Integer.toString(swap_id));
                    clearNotification(notification,position);
                }

                if(notification.getIS_FOLLOW() == 1){
                  clearNotification(notification,position);
                    //RMsg.toastHere(context,"working follow "+Integer.toString(notification.getNOTIFICATION_ID()));
                }
            }
        }).attachToRecyclerView(rv);
    }

    public void clearNotification(Notification notification,final int position){
        CReq.clearNotification(context, notification.getNOTIFICATION_ID(), new CReq.CReqListenerNotification() {
            @Override
            public void onRecieve(Notification notification) {
                if(notification.getIS_AUTHENTICATED()){
                    if(notification.getIS_ERROR()){
                        RMsg.toastHere(context,notification.getMESSAGE());
                        notificationAdapter.notifyItemChanged(position);
                    }else if(notification.getIS_CLEARED()){
                        RMsg.toastHere(context,notification.getMESSAGE());
                        //notificationAdapter.notifyItemChanged(position);
                        notificationArrayList.remove(position);
                        notificationAdapter.notifyItemRemoved(position);
                    }
                }else{
                    notificationAdapter.notifyItemChanged(position);
                    RMsg.toastHere(context,RMsg.AUTH_ERROR_MESSAGE);
                }
            }

            @Override
            public void onError(String error) {
                notificationAdapter.notifyItemChanged(position);
                RMsg.toastHere(context,error);
            }

            @Override
            public void onException(String ex) {
                notificationAdapter.notifyItemChanged(position);
                RMsg.toastHere(context,ex);

            }
        });
    }
}
