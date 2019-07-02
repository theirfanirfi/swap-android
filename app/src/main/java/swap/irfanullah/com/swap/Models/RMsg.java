package swap.irfanullah.com.swap.Models;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.Random;

public class RMsg {
    public static final String AUTH_ERROR_MESSAGE = "You are not loggedin. Please login and try again.";
    public static final String REQ_ERROR_MESSAGE = "Request was not successful.";
    public static final String FIELDS_EMPTY_ERROR_MESSAGE = "None of the fields can be empty.";
    public static final String NEW_CONFIRM_PASS_ERROR_MESSAGE = "New and Confirm password do not match.";
    public static final String PASS_LEN_ERROR_MESSAGE = "New Password Length must be at least characters Long.";
    public static final String STATUS_SWAP_MESSAGE = "Status swap request is sent.";
    public static final String LOG_MESSAGE = "MY_SWAP_APP";
    public static final String NOTIFICATIONS_NOT_FOUND_MESSAGE = "We do not have any notifications for you at the moment.";
    public static final String SWAP_NOT_FOUND_MESSAGE = "Swap not found.";

    public static void toastHere(Context context, String msg){
        Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
    }
    public static void logHere(String msg){
        Log.i(LOG_MESSAGE,msg);
    }

    public static void ilogHere(int msg){
        Log.i(LOG_MESSAGE,Integer.toString(msg));
    }

    public static int getRandom(){
        return new Random().nextInt(1000000 - 1000);
    }
}
