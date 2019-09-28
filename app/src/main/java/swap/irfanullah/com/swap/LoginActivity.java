package swap.irfanullah.com.swap;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jaychang.sa.AuthCallback;
import com.jaychang.sa.SocialUser;
import com.jaychang.sa.instagram.SimpleAuth;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import swap.irfanullah.com.swap.Libraries.RetroLib;
import swap.irfanullah.com.swap.Libraries.VolleyLib;
import swap.irfanullah.com.swap.Models.RMsg;
import swap.irfanullah.com.swap.Models.User;
import swap.irfanullah.com.swap.Storage.PrefStorage;

public class LoginActivity extends AppCompatActivity {

    TextView signup;
    Button loginBtn; ImageView fb, twitter, insta, google;
    EditText email,password;
    String emailF = "", passwordF = "";
    final String LOGIN_URL = "auth/login";
    final String TOKEN_URL = "auth/retoken";
    JSONObject userObject;
    ProgressBar progressBar;
    String str;
    Context context;
    private final String ENCODE_TAG = "ENCODING_ERROR";
    private final String JSON_TAG = "JSON_ERROR";
    private static final String TAG = "LoginActivity";
    private boolean IS_LOGIN_SUCCESSFULL = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
context = this;
        checkIsLoggedIn(this);

        initializeObjects();
        RegisterationLink();
        LoginButton();

    }

    private void checkIsLoggedIn(Context context) {
        if(PrefStorage.getUserData(context).equals(""))
        {
            //stay on the login activity
        }
        else
        {
            Intent homeAct = new Intent(getApplicationContext(),HomeActivity.class);
            startActivity(homeAct);
            finish();

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(IS_LOGIN_SUCCESSFULL){
               finish();
        }
    }

    private void RegisterationLink()
    {
        signup = findViewById(R.id.signupActLink);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(i);
            }
        });
    }

    private void LoginButton()
    {
        loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailF = email.getText().toString().toLowerCase();
                passwordF = password.getText().toString().toLowerCase();
                if(passwordF.isEmpty() || emailF.isEmpty()) {
                    Snackbar.make(v, "None of the Fields can be empty.", Snackbar.LENGTH_LONG).show();
                }
                else
                {
                    try {
                        loginRequest(emailF, passwordF);
                    } catch (JSONException e) {
                        Toast.makeText(context,e.toString(),Toast.LENGTH_LONG).show();

                    } catch (UnsupportedEncodingException e) {
                        Toast.makeText(context,e.toString(),Toast.LENGTH_LONG).show();

                    }
                }
            }
        });
    }

    private void loginRequest(String emailF, String passwordF) throws JSONException, UnsupportedEncodingException {
        progressBar.setVisibility(View.VISIBLE);
        JSONArray dataArray = new JSONArray();
        dataArray.put(0,emailF);
        dataArray.put(1,passwordF);
        str = VolleyLib.encode(dataArray.toString());
        Log.i("MY_SWAP_APP: ",str);

        VolleyLib.getRequest(this, LOGIN_URL+"/"+str, new VolleyLib.VolleyListener() {
            @Override
            public void onRecieve(JSONObject object) throws JSONException {

                Boolean isError = object.getBoolean("isError");
                Boolean isUser = object.getBoolean("isUser");
                Boolean tokenError = object.getBoolean("tokenError");
                String message = object.getString("message");

                if(isError)
                {
                    Toast.makeText(context,message,Toast.LENGTH_LONG).show();
                }
                else if(isUser)
                {
                    if(!tokenError) {
                        userObject = object.getJSONObject("user");
                        PrefStorage.getEditor(context).putString(PrefStorage.USER_PREF_DETAILS, userObject.toString()).commit();
                        if((userObject.getInt("is_followed") == 0 || userObject.getInt("is_followed") == 1) && userObject.getInt("followed") < 5){
                            PrefStorage.getEditor(context).putString(PrefStorage.AFTER_STARTUP_ACTIVITY,PrefStorage.USERS_ACTIVITY).commit();

                            if(userObject.getInt("invites") >= 5){
                                PrefStorage.getEditor(context).putString(PrefStorage.START_NEXT_ACTIVITY,PrefStorage.NEXT_NO).commit();
                            }else {
                                PrefStorage.getEditor(context).putString(PrefStorage.START_NEXT_ACTIVITY,PrefStorage.NEXT_YES).commit();
                            }

                            Intent followUsersAct = new Intent(context, FollowUsersActivity.class);
                            IS_LOGIN_SUCCESSFULL = true;
                            startActivity(followUsersAct);
                        }
                        else if((userObject.getInt("is_invited") == 0 || userObject.getInt("is_invited") == 1) && userObject.getInt("invites") < 5){

                            PrefStorage.getEditor(context).putString(PrefStorage.AFTER_STARTUP_ACTIVITY,PrefStorage.INVITES_ACTIVITY).commit();
                            if(userObject.getInt("followed") >= 5){
                                PrefStorage.getEditor(context).putString(PrefStorage.START_NEXT_ACTIVITY,PrefStorage.NEXT_NO).commit();
                            }else {
                                PrefStorage.getEditor(context).putString(PrefStorage.START_NEXT_ACTIVITY,PrefStorage.NEXT_YES).commit();
                            }
                            RMsg.logHere("invite activity started.");

                            Intent inviteAct = new Intent(context, InvitePhoneContactsActivity.class);
                            IS_LOGIN_SUCCESSFULL = true;
                            startActivity(inviteAct);
                            finish();

                        }else if(userObject.getInt("is_invited") == 1 && userObject.getInt("followed") >= 5 && userObject.getInt("is_soc") == 0){
                            Intent shareApp = new Intent(context, ShareApp.class);
                            IS_LOGIN_SUCCESSFULL = true;
                            startActivity(shareApp);
                            finish();
                        }
                        else {
                             Intent homeAct = new Intent(context, HomeActivity.class);
                            IS_LOGIN_SUCCESSFULL = true;
                            startActivity(homeAct);
                            finish();
                        }
                    }
                }

            }

            @Override
            public void onException(String exception) {
                Toast.makeText(context,exception,Toast.LENGTH_LONG).show();

            }

            @Override
            public void onError(String volleyError) {
                Toast.makeText(context,volleyError,Toast.LENGTH_LONG).show();

            }
        });

        progressBar.setVisibility(View.GONE);

    }

    private void initializeObjects()
    {
        email = findViewById(R.id.emailTextField);
        password = findViewById(R.id.passwordTextField);
        context = this;
        progressBar = findViewById(R.id.progressBarLogin);
        fb = findViewById(R.id.facebookButton);
        twitter = findViewById(R.id.twitterButton);
        insta = findViewById(R.id.instBtn);
        google = findViewById(R.id.googleButton);

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpleFbAuth();
            }
        });

        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpleTwitterAtuh();
            }
        });

        insta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpleInstaAuth();
            }
        });

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpleGoogleAuth();
            }
        });
    }

    private void simpleInstaAuth() {
        List<String> scopes = Arrays.asList("user_birthday", "user_friends");
        SimpleAuth.connectInstagram(scopes, new AuthCallback() {
            @Override
            public void onSuccess(SocialUser socialUser) {
                makeSocialLoginRequest(socialUser.userId,socialUser.fullName,socialUser.profilePictureUrl,"instagram");
            }

            @Override
            public void onError(Throwable error) {
                RMsg.toastHere(context,error.getMessage());

            }

            @Override
            public void onCancel() {

            }
        });
    }

    private void simpleTwitterAtuh() {
        List<String> scopes = Arrays.asList("user_birthday", "user_friends");
//        com.jaychang.sa.twitter.SimpleAuth.connectTwitter(scopes, new AuthCallback(){
//            @Override
//            public void onSuccess(SocialUser socialUser) {
//
//            }
//
//            @Override
//            public void onError(Throwable error) {
//
//            }
//
//            @Override
//            public void onCancel() {
//
//            }
//        });
    }

    private void simpleFbAuth(){
        List<String> scopes = Arrays.asList("user_birthday", "user_friends");
        com.jaychang.sa.facebook.SimpleAuth.connectFacebook(scopes, new AuthCallback() {
            @Override
            public void onSuccess(SocialUser socialUser) {
                RMsg.logHere(socialUser.profilePictureUrl);
                makeSocialLoginRequest(socialUser.userId,socialUser.fullName,socialUser.profilePictureUrl,"fb");
            }

            @Override
            public void onError(Throwable error) {
                RMsg.toastHere(context,error.getMessage());

            }

            @Override
            public void onCancel() {
                Intent loginAct = new Intent(context,LoginActivity.class);
                startActivity(loginAct);
            }
        });

    }

    private void simpleGoogleAuth(){
        List<String> scopes = Arrays.asList("user_birthday", "user_friends");
        com.jaychang.sa.google.SimpleAuth.connectGoogle(scopes, new AuthCallback() {
            @Override
            public void onSuccess(SocialUser socialUser) {
                makeSocialLoginRequest(socialUser.userId,socialUser.fullName,socialUser.profilePictureUrl,"google");
            }

            @Override
            public void onError(Throwable error) {
                RMsg.toastHere(context,error.getMessage());
            }

            @Override
            public void onCancel() {

            }
        });

    }

    private void makeSocialLoginRequest(String id, String name,String profile_image, String network){
        RetroLib.geApiService().socialLogin(id,name,profile_image,network).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                    if(response.isSuccessful()){
                        User user = response.body();

                        if(user.getIS_ERROR()){
                            RMsg.toastHere(context,user.getMESSAGE());
                        }else {
                            if(user.isUserRegistered()){
                                if(user.isUser()){

                                    proceedWithLogin(user.getUSER());
                                }else {
                                    RMsg.toastHere(context,user.getMESSAGE());

                                }
                            }else {
                                RMsg.toastHere(context,user.getMESSAGE());
                            }
                        }

                    }else {
                        RetroLib.toastHere(context,RMsg.REQ_ERROR_MESSAGE);
                    }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                RMsg.toastHere(context,t.getMessage() + " \n "+t.toString());
                RMsg.logHere(t.toString());
            }
        });
    }

    private void proceedWithLogin(User user){
        Gson gson = new Gson();
        String juser = gson.toJson(user).toString();
        PrefStorage.getEditor(context).putString(PrefStorage.USER_PREF_DETAILS,juser).commit();
        if(( user.getIS_FOLLOWED() == 0 || user.getIS_FOLLOWED() == 1) && user.getFOLLOWED() < 5){
            PrefStorage.getEditor(context).putString(PrefStorage.AFTER_STARTUP_ACTIVITY,PrefStorage.USERS_ACTIVITY).commit();

            if( user.getINVITES() >= 5){
                PrefStorage.getEditor(context).putString(PrefStorage.START_NEXT_ACTIVITY,PrefStorage.NEXT_NO).commit();
            }else {
                PrefStorage.getEditor(context).putString(PrefStorage.START_NEXT_ACTIVITY,PrefStorage.NEXT_YES).commit();
            }

            Intent followUsersAct = new Intent(context, FollowUsersActivity.class);
            IS_LOGIN_SUCCESSFULL = true;

            startActivity(followUsersAct);
            finish();

        }
        else if(( user.getIS_INVITED() == 0 || user.getIS_INVITED() == 1) && user.getINVITES() < 5){

            PrefStorage.getEditor(context).putString(PrefStorage.AFTER_STARTUP_ACTIVITY,PrefStorage.INVITES_ACTIVITY).commit();
            if(user.getFOLLOWED() >= 5){
                PrefStorage.getEditor(context).putString(PrefStorage.START_NEXT_ACTIVITY,PrefStorage.NEXT_NO).commit();
            }else {
                PrefStorage.getEditor(context).putString(PrefStorage.START_NEXT_ACTIVITY,PrefStorage.NEXT_YES).commit();
            }
            RMsg.logHere("invite activity started.");

            Intent inviteAct = new Intent(context, InvitePhoneContactsActivity.class);
            IS_LOGIN_SUCCESSFULL = true;

            startActivity(inviteAct);
        }else if(user.getIS_INVITED() == 1 && user.getFOLLOWED() >= 5 && user.getIsSocialMedia() == 0){
            Intent shareApp = new Intent(context, ShareApp.class);
            IS_LOGIN_SUCCESSFULL = true;

            startActivity(shareApp);
            finish();

        }
        else {
            Intent homeAct = new Intent(context, HomeActivity.class);
            IS_LOGIN_SUCCESSFULL = true;

            startActivity(homeAct);
            finish();

        }
    }
}
