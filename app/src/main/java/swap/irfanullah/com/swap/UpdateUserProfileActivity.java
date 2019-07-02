package swap.irfanullah.com.swap;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.soundcloud.android.crop.Crop;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import swap.irfanullah.com.swap.Libraries.GLib;
import swap.irfanullah.com.swap.Libraries.RetroLib;
import swap.irfanullah.com.swap.Models.ProfileModel;
import swap.irfanullah.com.swap.Models.RMsg;
import swap.irfanullah.com.swap.Models.User;
import swap.irfanullah.com.swap.Storage.PrefStorage;

public class UpdateUserProfileActivity extends AppCompatActivity {

    ImageView profile_image;
    Context context;
    User user;
    AppCompatButton update;
    AppCompatEditText profile_description;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_profile);
        initObjects();
    }

    private void initObjects(){
        user = PrefStorage.getUser(this);
        profile_image = findViewById(R.id.profile_image);
        context = this;
        changeProfilePic();
        update = findViewById(R.id.updateProfileBtn);
        profile_description = findViewById(R.id.userProfileDescription);
        updateProfileBio();
    }

    private void updateProfileBio(){

        if(!user.getPROFILE_DESCRIPTION().equals("")){
            profile_description.setText(user.getPROFILE_DESCRIPTION());
        }

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeRequest();
            }
        });
    }
    private void changeProfilePic() {

        if(!user.getPROFILE_IMAGE().equals("")){
            GLib.downloadImage(context,user.getPROFILE_IMAGE()).into(profile_image);
        }

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Change Profile Picture")
                        .setMessage("Do you want to change profile picture?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Crop.pickImage(UpdateUserProfileActivity.this);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });
    }


    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            Uri source_uri = data.getData();
            Long tsLong = System.currentTimeMillis() / 1000;
            String file_name = Integer.toString(RMsg.getRandom()) + user.getFULL_NAME() + tsLong.toString();
            Uri destination_uri = Uri.fromFile(new File(getCacheDir(), file_name));
            Crop.of(source_uri, destination_uri).withAspect(50, 50).start(this);
            profile_image.setImageURI(Crop.getOutput(data));

        } else if (requestCode == Crop.REQUEST_CROP && resultCode == RESULT_OK) {
            profile_image.setImageURI(Crop.getOutput(data));
            Toast.makeText(context, "Profile picture is being updated.", Toast.LENGTH_LONG).show();
            Uri loc = Crop.getOutput(data);
            File file = new File(loc.getPath());


            RequestBody tokenBody = RequestBody.create(MediaType.parse("multipart/form-data"), PrefStorage.getUser(context).getTOKEN());
            RequestBody image = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part img = MultipartBody.Part.createFormData("image", file.getName(), image);

            RetroLib.geApiService().updateProfilePicture(tokenBody, img).enqueue(new Callback<ProfileModel>() {
                @Override
                public void onResponse(Call<ProfileModel> call, Response<ProfileModel> response) {
                    if (response.isSuccessful()) {
                        ProfileModel profile = response.body();
                        if (profile.getIS_AUTHENTICATED()) {
                            if (profile.getIS_EMPTY()) {
                                Toast.makeText(context, profile.getMESSAGE(), Toast.LENGTH_LONG).show();
                            } else if (profile.getIS_ERROR()) {
                                Toast.makeText(context, profile.getMESSAGE(), Toast.LENGTH_LONG).show();
                            } else if (profile.getIS_SAVED()) {
                                User user = profile.getUSER();
                                Gson gson = new Gson();
                                String object = gson.toJson(user);
                                PrefStorage.getEditor(context).putString(PrefStorage.USER_PREF_DETAILS, object).commit();
                                Toast.makeText(context, profile.getMESSAGE(), Toast.LENGTH_LONG).show();
                                Log.i("PROFILEUPDATED:", object);
                            }
                        } else {
                            Toast.makeText(context, "You are not loggedin. Login and try again.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(context, "Request was not successful", Toast.LENGTH_LONG).show();
                    }
                    Log.i("NOTPROFILE: ", response.raw().toString() + " : " + response.body().getMESSAGE());
                }

                @Override
                public void onFailure(Call<ProfileModel> call, Throwable t) {
                    Log.i("NOTPROFILE: Exception ", t.toString());

                }
            });

        } else {
            Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
        }
    }



    public void makeRequest(){
        String description = profile_description.getText().toString();
        RetroLib.geApiService().updateProfileDescription(PrefStorage.getUser(context).getTOKEN(),description).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()) {
                    User user = response.body();
                    if(user.getIS_AUTHENTICATED()){
                        if(user.getIS_ERROR()){
                            Toast.makeText(context,user.getMESSAGE(),Toast.LENGTH_LONG).show();
                        }else if(user.getIS_UPDATED()){

                            User updatedUser = user.getUSER();
                            Gson gson = new Gson();
                            String newUser = gson.toJson(updatedUser);
                            PrefStorage.getEditor(context).putString(PrefStorage.USER_PREF_DETAILS,newUser).commit();
                            RMsg.logHere(PrefStorage.getUser(context).getPROFILE_DESCRIPTION());
                            Toast.makeText(context,user.getMESSAGE(),Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(context,user.getMESSAGE(),Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Toast.makeText(context,RMsg.AUTH_ERROR_MESSAGE,Toast.LENGTH_LONG).show();

                    }
                }else {
                    Toast.makeText(context,RMsg.REQ_ERROR_MESSAGE,Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(context,t.toString(),Toast.LENGTH_LONG).show();

            }
        });


    }
}
