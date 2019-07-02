package swap.irfanullah.com.swap;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import swap.irfanullah.com.swap.Libraries.RetroLib;
import swap.irfanullah.com.swap.Models.RMsg;
import swap.irfanullah.com.swap.Models.User;
import swap.irfanullah.com.swap.Storage.PrefStorage;

public class Settings extends AppCompatActivity {
    BootstrapEditText currentPassword, newPassword, confirmPassword,fullName,usernamee,emaill;
    BootstrapButton changePassBtn, updateProfileDetails;
    User user;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setTitle("Settings");

        initializeObjects();
        setInfo();
        updateUserDetails();
        changePassword();
    }

    private void changePassword() {
        changePassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                String current = currentPassword.getText().toString(), newpass = newPassword.getText().toString(), confirm= confirmPassword.getText().toString();
                if(current.equals("") || newpass.equals("") || confirm.equals("")){
                    Snackbar.make(v,RMsg.FIELDS_EMPTY_ERROR_MESSAGE,Snackbar.LENGTH_LONG).show();
                }else if(!newpass.toLowerCase().equals(confirm.toLowerCase())){
                    Snackbar.make(v,RMsg.NEW_CONFIRM_PASS_ERROR_MESSAGE,Snackbar.LENGTH_LONG).show();
                } else if(newpass.length() < 6) {
                    Snackbar.make(v,RMsg.PASS_LEN_ERROR_MESSAGE,Snackbar.LENGTH_LONG).show();
                } else {
                    RetroLib.geApiService().changePassword(user.getTOKEN(),newpass,confirm,current).enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            Log.i("REQUEST_ERROR:",response.raw().toString());
                            if(response.isSuccessful()){
                                User u = response.body();
                                if(u.getIS_AUTHENTICATED()){
                                    if(u.getIS_ERROR()){
                                        Snackbar.make(v,u.getMESSAGE(),Snackbar.LENGTH_LONG).show();
                                    }else {
                                        if(u.getIS_CHANGED()){
                                            Snackbar.make(v,u.getMESSAGE(),Snackbar.LENGTH_LONG).show();
                                        }else {
                                            Snackbar.make(v,u.getMESSAGE(),Snackbar.LENGTH_LONG).show();
                                        }
                                    }
                                }else {
                                    Snackbar.make(v,RMsg.AUTH_ERROR_MESSAGE,Snackbar.LENGTH_LONG).show();
                                }
                            }else {
                                Snackbar.make(v,RMsg.REQ_ERROR_MESSAGE,Snackbar.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {

                        }
                    });
                }
            }
        });
    }

    private void updateUserDetails() {
        updateProfileDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                String name = fullName.getText().toString();
                String usern = usernamee.getText().toString();
                String em = emaill.getText().toString();

                if(name.equals("") || usern.equals("") || em.equals("")){
                    Snackbar.make(v,RMsg.FIELDS_EMPTY_ERROR_MESSAGE,Snackbar.LENGTH_LONG).show();
                } else {
                    RetroLib.geApiService().updateProfileDetails(user.getTOKEN(),name,usern,em).enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if(response.isSuccessful()){
                                User u = response.body();
                                if(u.getIS_AUTHENTICATED()){
                                    if(u.getIS_ERROR()){
                                        Snackbar.make(v,u.getMESSAGE(),Snackbar.LENGTH_LONG).show();
                                    }else if(u.getIS_UPDATED()){
                                        User newUser = u.getUSER();
                                        Gson gson = new Gson();
                                        String userObj = gson.toJson(newUser);
                                        PrefStorage.getEditor(context).putString(PrefStorage.USER_PREF_DETAILS,userObj).commit();
                                        Snackbar.make(v,u.getMESSAGE(),Snackbar.LENGTH_LONG).show();
                                    } else {
                                        Snackbar.make(v,u.getMESSAGE(),Snackbar.LENGTH_LONG).show();
                                    }
                                }else {
                                    Snackbar.make(v,RMsg.AUTH_ERROR_MESSAGE,Snackbar.LENGTH_LONG).show();

                                }
                            }else {
                                Snackbar.make(v,RMsg.REQ_ERROR_MESSAGE,Snackbar.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {

                        }
                    });
                }
            }
        });
    }

    private void setInfo() {
        fullName.setText(user.getFULL_NAME());
        usernamee.setText(user.getUSERNAME());
        emaill.setText(user.getEMAIL());
    }

    private void initializeObjects() {
        context = this;
        currentPassword = findViewById(R.id.currentPassword);
        newPassword = findViewById(R.id.newPassword);
        confirmPassword = findViewById(R.id.confirmpassword);
        fullName = findViewById(R.id.fullName);
        usernamee = findViewById(R.id.username);
        emaill = findViewById(R.id.email);
        changePassBtn = findViewById(R.id.changePassBtn);
        updateProfileDetails = findViewById(R.id.updateProfileBtn);
        user = PrefStorage.getUser(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
