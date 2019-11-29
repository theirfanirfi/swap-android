package swap.irfanullah.com.swap.Services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.view.View;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import swap.irfanullah.com.swap.Libraries.RetroLib;
import swap.irfanullah.com.swap.LoginActivity;
import swap.irfanullah.com.swap.Models.RMsg;
import swap.irfanullah.com.swap.NotificationActivity;
import swap.irfanullah.com.swap.R;
import swap.irfanullah.com.swap.Storage.PrefStorage;
import swap.irfanullah.com.swap.SwapRequestActivity;

import static swap.irfanullah.com.swap.AppClasses.App.CHANNEL_ID;

public class BackgroundNotificationsService extends Service {

    private Boolean IS_RUNNING = false;
    private final int DELAY = 300000;
    private Handler handler;
    private Runnable runnable;
    private String NotificationContent = "You will be notified about new notifications here.";
    private String NotificationContentSwap = "";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        if(!IS_RUNNING){
            handler = new Handler();
            runnable = new Runnable() {
                @Override
                public void run() {
                    getNotifications(getApplicationContext());
                    //RMsg.logHere("service called notification");
                    swapRequests(getApplicationContext());
                    handler.postDelayed(this,5000);
                }
            };

            handler.postDelayed(runnable,5000);
            IS_RUNNING = true;
        }


        Intent notificationIntent = new Intent(this, LoginActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Swap")
                .setContentText("")
                .setSmallIcon(R.drawable.login_bg_gradient)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_MIN)
                .build();
        startForeground(1,notification);
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void notification(int id){
        Intent notificationIntent = new Intent(this, NotificationActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Notification notification = new Notification.Builder(this)
                .setContentTitle("Swap")
                .setContentText(NotificationContent)
                .setSmallIcon(R.drawable.login_bg_gradient)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_LOW)
                .setOnlyAlertOnce(true)
                .build();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
//        SC.logHere("id: "+Integer.toString(id));
//        SC.logHere("working");
        notificationManager.notify(2, notification);
    }

    private void swapNotification(int id){
        Intent notificationIntent = new Intent(this, SwapRequestActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        notificationIntent.putExtra("is_background_notification","yes");
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification.Builder(this)
                .setContentTitle("Swap")
                .setContentText(NotificationContentSwap)
                .setSmallIcon(R.drawable.login_bg_gradient)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_LOW)
                .setOnlyAlertOnce(true)
                .build();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
//        SC.logHere("id: "+Integer.toString(id));
//        SC.logHere("working");
        notificationManager.notify(3, notification);
    }


    private void getNotifications(final Context context){
        RetroLib.geApiService().getNotifications(PrefStorage.getUser(context).getTOKEN()).enqueue(new Callback<swap.irfanullah.com.swap.Models.Notification>() {
            @Override
            public void onResponse(Call<swap.irfanullah.com.swap.Models.Notification> call, Response<swap.irfanullah.com.swap.Models.Notification> response) {
                if(response.isSuccessful()){
                    swap.irfanullah.com.swap.Models.Notification notification = response.body();
                    if(notification.getIS_AUTHENTICATED()){
                        if(notification.getIS_FOUND()){
                            NotificationContent = "You have "+Integer.toString(notification.getNOTIFICATIONSCOUNT())+" new notifications";
                            notification(2);

                        }else {
                            //Toast.makeText(context, RMsg.NOTIFICATIONS_NOT_FOUND_MESSAGE,Toast.LENGTH_LONG).show();
                        }
                    }else {
//                        Toast.makeText(context,RMsg.AUTH_ERROR_MESSAGE,Toast.LENGTH_LONG).show();

                    }
                }else {
  //                  Toast.makeText(context,RMsg.REQ_ERROR_MESSAGE,Toast.LENGTH_LONG).show();
                }

              //  progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<swap.irfanullah.com.swap.Models.Notification> call, Throwable t) {
               // Toast.makeText(context,t.toString(),Toast.LENGTH_LONG).show();

            }
        });
    }

    private void swapRequests(Context context){
        RetroLib.geApiService().getSwapRequestNotificationsBackground(PrefStorage.getUser(context).getTOKEN()).enqueue(new Callback<swap.irfanullah.com.swap.Models.Notification>() {
            @Override
            public void onResponse(Call<swap.irfanullah.com.swap.Models.Notification> call, Response<swap.irfanullah.com.swap.Models.Notification> response) {
                if(response.isSuccessful()){
                    swap.irfanullah.com.swap.Models.Notification notification = response.body();
                    if(notification.getIS_AUTHENTICATED()){
                        if(notification.getIS_FOUND()){
                            NotificationContentSwap = "You have "+Integer.toString(notification.getNOTIFICATIONSCOUNT())+" Swap requests";
                            swapNotification(3);
                        }else {
                            //  Toast.makeText(context,RMsg.NOTIFICATIONS_NOT_FOUND_MESSAGE,Toast.LENGTH_LONG).show();
                        }
                    }else {
//                        Toast.makeText(context,RMsg.AUTH_ERROR_MESSAGE,Toast.LENGTH_LONG).show();

                    }
                }else {
  //                  Toast.makeText(context,RMsg.REQ_ERROR_MESSAGE,Toast.LENGTH_LONG).show();
                }

           //     progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<swap.irfanullah.com.swap.Models.Notification> call, Throwable t) {
//                Toast.makeText(context,t.toString(),Toast.LENGTH_LONG).show();

            }
        });

    }

}
