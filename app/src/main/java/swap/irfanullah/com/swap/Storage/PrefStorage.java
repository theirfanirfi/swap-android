package swap.irfanullah.com.swap.Storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import swap.irfanullah.com.swap.Models.User;

public class PrefStorage {
    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;
    public static String PREF_STORAGE_FILE = "users";
    public static String USER_PREF_DETAILS = "user";
    public static String AFTER_STARTUP_ACTIVITY = "startup_activity"; // two values: 1. users 2. follow
    public static String USERS_ACTIVITY = "users_activity";
    public static String INVITES_ACTIVITY = "invites_activity";
    public static String SOC_AVTIVITY = "soc_activity";
    public static String START_NEXT_ACTIVITY = "start_next_activity";
    public static String NEXT_YES = "yes";
    public static String NEXT_NO = "yes";
    public static String WHICH = "which";

    public static SharedPreferences getSharedPreference(Context context)
    {
        return context.getSharedPreferences(PREF_STORAGE_FILE, context.MODE_PRIVATE);
    }

    public static SharedPreferences.Editor getEditor(Context context)
    {
        if(sharedPreferences != null)
        {
            return sharedPreferences.edit();
        }
        else
        {
            sharedPreferences = getSharedPreference(context);
            return sharedPreferences.edit();
        }
    }

    public static String getUserData(Context context)
    {
        String userDetails = "";
        userDetails = getSharedPreference(context).getString(USER_PREF_DETAILS,"");
        return userDetails;
    }

    public static User getUser(Context context)
    {
        Gson gson = new Gson();
        User user = gson.fromJson(PrefStorage.getUserData(context),User.class);
        return user;
    }

    public static Boolean isMe(Context context,int USER_ID){
        return getUser(context).getUSER_ID() == USER_ID ? true : false;
    }
    public static String getAfterStartupActivity(Context context){
        return getSharedPreference(context).getString(AFTER_STARTUP_ACTIVITY,"");
    }

    public static boolean gotoNextActivity(Context context){
        return getSharedPreference(context).getString(START_NEXT_ACTIVITY,"") == NEXT_YES ? true : false;
    }

}
