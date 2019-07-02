package swap.irfanullah.com.swap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.robertsimoes.shareable.Shareable;

import swap.irfanullah.com.swap.Storage.PrefStorage;

public class ShareApp extends AppCompatActivity implements View.OnClickListener {

    Button fb,twitter,linkedin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_app);

        fb = findViewById(R.id.facebookButton);
        twitter = findViewById(R.id.twitterButton);
        linkedin = findViewById(R.id.linkedIn);

        fb.setOnClickListener(this);
        twitter.setOnClickListener(this);
        linkedin.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.facebookButton:
                fbShare();
                break;
            case R.id.twitterButton:
                twitterShare();
                break;
            case R.id.linkedIn:
                lnShare();
                break;
        }
    }

    private void fbShare(){
        Shareable shareAction = new Shareable.Builder(this)
                .message("Please download the app. It is a very great app")
                .url("https://www.google.com")
                .socialChannel(Shareable.Builder.FACEBOOK)
                .build();
        shareAction.share();
    }


    private void twitterShare(){
        Shareable shareAction = new Shareable.Builder(this)
                .message("Please download the app. It is a very great app")
                .url("https://www.google.com")
                .socialChannel(Shareable.Builder.TWITTER)
                .build();
        shareAction.share();
    }

    private void lnShare(){
        Shareable shareAction = new Shareable.Builder(this)
                .message("Please download the app. It is a very great app")
                .url("https://www.google.com")
                .socialChannel(Shareable.Builder.LINKED_IN)
                .build();
        shareAction.share();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.next_menu_startup_activities, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.next) {
            Intent homeActivity = new Intent(this, HomeActivity.class);
            startActivity(homeActivity);
            finish();
        } else if (id == R.id.logout) {
            logout();
        } else if (id == R.id.skip) {
            Intent homeActivity = new Intent(this, HomeActivity.class);
            startActivity(homeActivity);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        PrefStorage.getEditor(this).remove(PrefStorage.USER_PREF_DETAILS).commit();
        PrefStorage.getEditor(this).remove(PrefStorage.START_NEXT_ACTIVITY).commit();
        PrefStorage.getEditor(this).remove(PrefStorage.AFTER_STARTUP_ACTIVITY).commit();
        Intent loginAct = new Intent(this, LoginActivity.class);
        startActivity(loginAct);
        finish();
    }

}
