package swap.irfanullah.com.swap;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import swap.irfanullah.com.swap.Libraries.VolleyLib;
import swap.irfanullah.com.swap.Models.RMsg;
import swap.irfanullah.com.swap.Models.User;
import swap.irfanullah.com.swap.Storage.PrefStorage;

public class RegisterActivity extends AppCompatActivity {

    TextView loginLink;
    Button regBtn;
    EditText email,password, name, username;
    String emailF = "", passwordF = "", nameF, usernameF;
    Context context;
    final String REGISTERATION_URL = "auth/register";
    ArrayList<User> users;
    JSONObject userObject;
    ProgressBar progressBar;
    String str;
    private final String ENCODE_TAG = "ENCODING_ERROR";
    private final String JSON_TAG = "JSON_ERROR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        initializeCbjects();
        LoginLink();
        RegisterButton();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    private void RegisterButton()
    {
        regBtn = findViewById(R.id.loginBtn);
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);

                emailF = email.getText().toString().toLowerCase();
                passwordF = password.getText().toString();
                nameF = name.getText().toString();
                usernameF = username.getText().toString().toLowerCase();

                if(passwordF.isEmpty() || emailF.isEmpty() || nameF.isEmpty() || usernameF.isEmpty()) {
                    progressBar.setVisibility(View.GONE);

                    Snackbar.make(v, "None of the Fields can be empty.", Snackbar.LENGTH_LONG).show();
                }
                else if(passwordF.length() < 6)
                {
                    progressBar.setVisibility(View.GONE);

                    Snackbar.make(v, "Password length must be at least 6 characters long.", Snackbar.LENGTH_LONG).show();

                }
                else if(usernameF.length() < 4)
                {
                    progressBar.setVisibility(View.GONE);

                    Snackbar.make(v, "Username length must be at least 4 characters long.", Snackbar.LENGTH_LONG).show();

                }
                else
                {
                    try {
                        requestRegisteration(emailF,passwordF, nameF, usernameF);
                    } catch (UnsupportedEncodingException e) {
                       Log.e(ENCODE_TAG,e.toString());
                    } catch (JSONException e) {
                        Log.e(JSON_TAG,e.toString());

                    }
                }
            }
        });
    }

    private void requestRegisteration(final String email, String password, String nameF, String usernameF) throws UnsupportedEncodingException, JSONException {


        JSONArray dataArray = new JSONArray();
        dataArray.put(0,nameF);
        dataArray.put(1,usernameF);
        dataArray.put(2,email);
        dataArray.put(3,password);


        str = VolleyLib.encode(dataArray.toString());

        VolleyLib.getRequest(context, REGISTERATION_URL+"/"+str,new VolleyLib.VolleyListener() {
            @Override
            public void onRecieve(JSONObject data) throws JSONException {

                Boolean isError = data.getBoolean("isError");
                Boolean isUserRegistered = data.getBoolean("isUserRegistered");
                Boolean isFieldEmpty = data.getBoolean("isFieldEmpty");
                Boolean isPasswordError = data.getBoolean("isPasswordError");
                Boolean isEmailTaken = data.getBoolean("isEmailTaken");
                Boolean isUser = data.getBoolean("isUser");
                Boolean isUserNameTaken = data.getBoolean("isUserNameTaken");
                Boolean isUsernameLengthError = data.getBoolean("isUsernameLengthError");
                String message = data.getString("message");

                if(isError)
                {
                   if(isFieldEmpty)
                   {
                       progressBar.setVisibility(View.GONE);
                       Toast.makeText(context,message,Toast.LENGTH_LONG).show();

                   }
                   else if(isPasswordError)
                   {
                       progressBar.setVisibility(View.GONE);

                       Toast.makeText(context,message,Toast.LENGTH_LONG).show();
                   }
                   else if(isEmailTaken)
                   {
                       progressBar.setVisibility(View.GONE);

                       Toast.makeText(context,message,Toast.LENGTH_LONG).show();

                   }
                   else if(isUsernameLengthError)
                   {
                       progressBar.setVisibility(View.GONE);

                       Toast.makeText(context,message,Toast.LENGTH_LONG).show();

                   }
                   else if(isUserNameTaken)
                   {
                       progressBar.setVisibility(View.GONE);

                       Toast.makeText(context,message,Toast.LENGTH_LONG).show();

                   }
                   else if(!isUserRegistered)
                   {
                       progressBar.setVisibility(View.GONE);

                       Toast.makeText(context,message,Toast.LENGTH_LONG).show();
                   }

                }
                else if(isUser)
                {
                    userObject = data.getJSONObject("user");
                    RMsg.logHere(userObject.toString());
                    PrefStorage.getEditor(context).putString(PrefStorage.USER_PREF_DETAILS, userObject.toString()).commit();
                   if((userObject.getInt("is_followed") == 0 || userObject.getInt("is_followed") == 1) && userObject.getInt("followed") < 5){
                        PrefStorage.getEditor(context).putString(PrefStorage.AFTER_STARTUP_ACTIVITY,PrefStorage.USERS_ACTIVITY).commit();


                       if(userObject.getInt("invites") >= 5){
                           PrefStorage.getEditor(context).putString(PrefStorage.START_NEXT_ACTIVITY,PrefStorage.NEXT_NO).commit();
                       }else {
                           PrefStorage.getEditor(context).putString(PrefStorage.START_NEXT_ACTIVITY,PrefStorage.NEXT_YES).commit();
                       }
                       RMsg.logHere("followers activity started.");

                        Intent followUsersAct = new Intent(context, FollowUsersActivity.class);
                        startActivity(followUsersAct);

                    }  else if((userObject.getInt("is_invited") == 0 || userObject.getInt("is_invited") == 1) && userObject.getInt("invites") < 5){
                    PrefStorage.getEditor(context).putString(PrefStorage.AFTER_STARTUP_ACTIVITY,PrefStorage.INVITES_ACTIVITY).commit();

                       if(userObject.getInt("followed") >= 5){
                           PrefStorage.getEditor(context).putString(PrefStorage.START_NEXT_ACTIVITY,PrefStorage.NEXT_NO).commit();
                       }else {
                           PrefStorage.getEditor(context).putString(PrefStorage.START_NEXT_ACTIVITY,PrefStorage.NEXT_YES).commit();
                       }

                    Intent inviteAct = new Intent(context, InvitePhoneContactsActivity.class);
                    startActivity(inviteAct);
                }
                else {
                         Intent homeAct = new Intent(context, HomeActivity.class);
                        startActivity(homeAct);
                    }

                }
            }

            @Override
            public void onException(String exception) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(context,exception,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(String volleyError) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(context,volleyError,Toast.LENGTH_LONG).show();

            }
        });
    }

    private void LoginLink()
    {
        loginLink = findViewById(R.id.loginActLink);

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i);
            }
        });
    }

    private void initializeCbjects()
    {
        email = findViewById(R.id.emailTextField);
        password = findViewById(R.id.passwordTextField);
        name = findViewById(R.id.fullname);
        username = findViewById(R.id.username);
        progressBar = findViewById(R.id.progressBar);
        context = this;
        users = new ArrayList<>();
    }
}
