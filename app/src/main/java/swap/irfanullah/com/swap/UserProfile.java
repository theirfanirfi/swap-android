package swap.irfanullah.com.swap;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.Calendar;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.util.Random;

import javax.microedition.khronos.opengles.GL;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import swap.irfanullah.com.swap.Adapters.ProfilePagerAdapter;
import swap.irfanullah.com.swap.CustomComponents.PDialog;
import swap.irfanullah.com.swap.Libraries.GLib;
import swap.irfanullah.com.swap.Libraries.RetroLib;
import swap.irfanullah.com.swap.Models.ProfileModel;
import swap.irfanullah.com.swap.Models.RMsg;
import swap.irfanullah.com.swap.Models.Statistics;
import swap.irfanullah.com.swap.Models.Status;
import swap.irfanullah.com.swap.Models.User;
import swap.irfanullah.com.swap.Storage.PrefStorage;

public class UserProfile extends AppCompatActivity {
    private ViewPager viewPager;
    private ProfilePagerAdapter profilePagerAdapter;
    private ImageView profile_image;
    private TabLayout tabLayout;
    private Context context;
    private TextView profileDescription, statuses, swaps, followers;
    private User user;
    private Button followBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        initializeObjects();
        loadStats();
        changeProfilePic();
//        changeProfileDescription();
        startFollowersActivity();
    }

    private void loadStats() {
        RetroLib.geApiService().getStats(user.getTOKEN()).enqueue(new Callback<Statistics>() {
            @Override
            public void onResponse(Call<Statistics> call, Response<Statistics> response) {
                if (response.isSuccessful()) {
                    Statistics stat = response.body();
                    if (stat.getIS_AUTHENTICATED()) {
                        if (stat.getIS_EMPTY()) {
                            //Toast.makeText(context,stat.getMESSAGE(),Toast.LENGTH_LONG).show();
                        } else if (stat.getIS_FOUND()) {
                            statuses.setText(Integer.toString(stat.getSTATUSES_COUNT()));
                            swaps.setText(Integer.toString(stat.getSWAPS_COUNT()));
                            followers.setText(Integer.toString(stat.getFOLLOWERS_COUNT()));
                        } else {

                        }
                    } else {
                        Toast.makeText(context, RMsg.AUTH_ERROR_MESSAGE, Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context, RMsg.REQ_ERROR_MESSAGE, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Statistics> call, Throwable t) {
                Toast.makeText(context, t.toString(), Toast.LENGTH_LONG).show();

            }
        });
    }


    private void changeProfileDescription() {
        if (user.getPROFILE_DESCRIPTION() != null) {
            profileDescription.setText(user.getPROFILE_DESCRIPTION());
        } else {
            profileDescription.setText("Add Bio by clicking me.");
        }


        profileDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PDialog pDialog = new PDialog();
                pDialog.setCancelable(false);
                pDialog.show(getSupportFragmentManager(), "update_profile_description");

                pDialog.mListner = new PDialog.ResultLister() {
                    @Override
                    public void onUpdate(Boolean isUpdated) {
                        RMsg.logHere("updated: "+Boolean.toString(isUpdated));
//                       updateDesc();
                    }

                    @Override
                    public void onFailure(Boolean isUpdate) {
                       RMsg.logHere(Boolean.toString(isUpdate));
                    }
                };
            }
        });
//        updateDesc();

    }

    private void changeProfilePic() {
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
                                Crop.pickImage(UserProfile.this);
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

    private void initializeObjects() {

        context = this;
        user = PrefStorage.getUser(context);
        profileDescription = findViewById(R.id.userProfileDescription);
        viewPager = findViewById(R.id.profileViewPage);
        profilePagerAdapter = new ProfilePagerAdapter(getSupportFragmentManager(), user.getUSER_ID());
        viewPager.setAdapter(profilePagerAdapter);

        tabLayout = findViewById(R.id.profileTabLayout);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        statuses = findViewById(R.id.statusesProfileTextView);
        swaps = findViewById(R.id.swapsNoProfileTextView);
        followers = findViewById(R.id.followerNoProfileTextView);
        followBtn = findViewById(R.id.followBtn);
        followBtn.setVisibility(View.GONE);

        profile_image = findViewById(R.id.profile_image);
        if (user.getPROFILE_IMAGE() != null) {
            GLib.downloadImage(context, user.getPROFILE_IMAGE()).into(profile_image);
        } else {
            profile_image.setImageResource(R.drawable.ic_person);
        }

    }

    @Override
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

    public void updateDesc() {
        User user1 = PrefStorage.getUser(context);
        RMsg.logHere(user1.getPROFILE_DESCRIPTION());
        if (user1.getPROFILE_DESCRIPTION() != null) {
            profileDescription.setText(user1.getPROFILE_DESCRIPTION());
        } else {
            profileDescription.setText("Add Bio by clicking me.");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.settings:
                startUpdateProfileActivity();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);

        if (visible) {
           // Toast.makeText(context, "visible", Toast.LENGTH_LONG).show();
        }
    }

    public void startUpdateProfileActivity(){
        Intent profileAct = new Intent(this,UpdateUserProfileActivity.class);
        startActivity(profileAct);
    }

    public void startFollowersActivity(){
        followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent followersAct = new Intent(context,UserProfileFollowersActivity.class);
                followersAct.putExtra("profile_id",user.getUSER_ID());
                startActivity(followersAct);
            }
        });
    }
}
