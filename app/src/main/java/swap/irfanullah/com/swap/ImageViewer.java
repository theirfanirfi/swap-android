package swap.irfanullah.com.swap;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import swap.irfanullah.com.swap.Adapters.MediaPager;
import swap.irfanullah.com.swap.Libraries.RetroLib;
import swap.irfanullah.com.swap.Models.Attachments;
import swap.irfanullah.com.swap.Models.RMsg;
import swap.irfanullah.com.swap.Models.Status;
import swap.irfanullah.com.swap.Models.User;
import swap.irfanullah.com.swap.Storage.PrefStorage;

public class ImageViewer extends AppCompatActivity {

    private ViewPager viewPager;
    private MediaPager pager;
    private String STATUS_ID = null;
    private Context context;
    private ArrayList<Attachments> mediaAttachments;
    private User user;
    private CircleIndicator indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);
        initObjects();
        makeRequest();
    }

    private void initObjects() {
        context = this;
        viewPager = findViewById(R.id.imageViewer);
        indicator = findViewById(R.id.indicator);
        this.STATUS_ID = Integer.toString(getIntent().getExtras().getInt("status_id"));
        mediaAttachments = new ArrayList<>();
        pager = new MediaPager(this,mediaAttachments,this.STATUS_ID);
        viewPager.setAdapter(pager);
        indicator.setViewPager(viewPager);
        user = PrefStorage.getUser(context);
    }

    private void makeRequest(){
        RetroLib.geApiService().getStatus(user.getTOKEN(),Integer.parseInt(this.STATUS_ID)).enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if(response.isSuccessful()){
                    Status status = response.body();
                    if(status.getIS_ERROR()){
                        RMsg.toastHere(context,RMsg.REQ_ERROR_MESSAGE+"err if "+status.getMESSAGE()+" : "+STATUS_ID);
                    }else {
                        if(status.getAuthenticated()){
                            if(status.getFound()){
                                Status st = status.getOBJ_STATUS();
                                if(st.getHAS_ATTACHMENTS() == 1) {
                                    updatePager(st.getATTACHMENTS());
                                }else {
                                    finish();
                                }
                            }else {
                                RMsg.toastHere(context,RMsg.REQ_ERROR_MESSAGE+"found else");
                            }
                        }else {
                            RMsg.toastHere(context,RMsg.REQ_ERROR_MESSAGE+"auth else");
                        }
                    }
                }else {
                    RMsg.toastHere(context,RMsg.REQ_ERROR_MESSAGE+"is successfull");
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                RMsg.toastHere(context,t.getMessage());
            }
        });
    }

    private void updatePager(String attachments){
        Gson gson = new Gson();
        JsonElement json = gson.fromJson(attachments,JsonElement.class);
        if(json.isJsonObject()) {
            JsonObject object = json.getAsJsonObject();
            Attachments att = gson.fromJson(object, Attachments.class);
            mediaAttachments.add(att);
            pager.notifyAdapter(mediaAttachments);
            indicator.setViewPager(viewPager);
            // GLib.downloadImage(context, att.getATTACHMENT_URL()).into(statusMedia);

            RMsg.logHere("Single: "+att.getATTACHMENT_URL());
        }else if(json.isJsonArray()){
            JsonArray jsonArray = json.getAsJsonArray();
            //Attachments att = gson.fromJson(object, Attachments.class);
            Type type = new TypeToken<ArrayList<Attachments>>(){}.getType();
            ArrayList<Attachments> arrayList = gson.fromJson(jsonArray,type);
            pager.notifyAdapter(arrayList);
            indicator.setViewPager(viewPager);
            RMsg.logHere("working");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
