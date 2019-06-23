package swap.irfanullah.com.swap.Libraries;

import android.content.Context;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import swap.irfanullah.com.swap.Models.Notification;
import swap.irfanullah.com.swap.Models.Swap;
import swap.irfanullah.com.swap.Storage.PrefStorage;

public class CReq {
    public static void getSwap(Context context, int swap_id, final CReqListener cReqListener){
        RetroLib.geApiService().getSwap(PrefStorage.getUser(context).getTOKEN(),swap_id).enqueue(new Callback<Swap>() {
            @Override
            public void onResponse(Call<Swap> call, Response<Swap> response) {
                if(response.isSuccessful()){
                    cReqListener.onRecieve(response.body());
                }else {
                    cReqListener.onError(response.raw().toString());
                }
            }

            @Override
            public void onFailure(Call<Swap> call, Throwable t) {
                cReqListener.onException(t.toString());
            }
        });
    }

    public interface CReqListener {
        public void onRecieve(Swap swap);
        public void onError(String error);
        public void onException(String ex);
    }

    public interface CReqListenerNotification {
        public void onRecieve(Notification notification);
        public void onError(String error);
        public void onException(String ex);
    }

    public static void clearNotification(Context context, int notification_id, final CReqListenerNotification cReqListener){
        RetroLib.geApiService().clearNotifications(PrefStorage.getUser(context).getTOKEN(),notification_id).enqueue(new Callback<Notification>() {
            @Override
            public void onResponse(Call<Notification> call, Response<Notification> response) {
                if(response.isSuccessful()){
                    Notification notification = response.body();
                    cReqListener.onRecieve(notification);
                }else {
                    cReqListener.onError(response.raw().toString());
                }
            }

            @Override
            public void onFailure(Call<Notification> call, Throwable t) {
                cReqListener.onException(t.toString());
            }
        });
    }


}
