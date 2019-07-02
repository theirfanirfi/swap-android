package swap.irfanullah.com.swap;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import swap.irfanullah.com.swap.Adapters.StatusFragGridAdapter;
import swap.irfanullah.com.swap.Libraries.RetroLib;
import swap.irfanullah.com.swap.Models.Status;
import swap.irfanullah.com.swap.Storage.PrefStorage;

public class ToBeDeletedActivity extends AppCompatActivity {

    ArrayList<String> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_be_deleted);
//        ArrayList<Media> media = getIntent().getParcelableArrayListExtra("uris");
//        RMsg.logHere("NEW ACTIVITY: "+media.get(0).getUri().toString());
        list = new ArrayList<>();
        GridView gv = findViewById(R.id.gridview);
//        StatusFragGridAdapter statusFragGridAdapter = new StatusFragGridAdapter(this, list);
//        gv.setAdapter(statusFragGridAdapter);


    }

    private void makeRequest() {
        RetroLib.geApiService().getStatuses(PrefStorage.getUser(this).getTOKEN()).enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if(response.isSuccessful()) {

                    Status status = response.body();
                    if(status.getAuthenticated()) {
                        if(status.getFound()) {
                            ArrayList<Status> statuses = new ArrayList<>();
                            statuses = status.getSTATUSES();

                        }
                        else {
                            Toast.makeText(ToBeDeletedActivity.this,status.getMESSAGE(),Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                        Toast.makeText(ToBeDeletedActivity.this,status.getMESSAGE(),Toast.LENGTH_LONG).show();

                    }
                }
                else {
                    Log.i("STATUES: ","NOT SUCCESSFULL "+response.raw().toString());
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Toast.makeText(ToBeDeletedActivity.this,t.getMessage(),Toast.LENGTH_LONG).show();

                Log.i("STATUES: ","NOT SUCCESSFULL "+t.toString());

            }
        });

    }

}
