package swap.irfanullah.com.swap;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import swap.irfanullah.com.swap.Adapters.SwapRequestAdapter;
import swap.irfanullah.com.swap.Libraries.RetroLib;
import swap.irfanullah.com.swap.Models.Notification;
import swap.irfanullah.com.swap.Models.RMsg;
import swap.irfanullah.com.swap.Models.Swap;
import swap.irfanullah.com.swap.Storage.PrefStorage;
import swap.irfanullah.com.swap.SwapRequestActivityFragments.SwapRequestsFragmentPagerAdapter;

public class SwapRequestActivity extends AppCompatActivity {


    private Context context;
    private TabLayout tabLayout;
    private ViewPager fragmentPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swap_request);

        initObjects();
    }

    private void initObjects(){
        context = this;

        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.getTabAt(0).setText("Swap Requests");
        tabLayout.getTabAt(1).setText("Swaps Reviews");
        fragmentPager = findViewById(R.id.fragmentPager);

        SwapRequestsFragmentPagerAdapter swapRequestsFragmentPagerAdapter = new SwapRequestsFragmentPagerAdapter(this,getSupportFragmentManager());
        fragmentPager.setAdapter(swapRequestsFragmentPagerAdapter);
        tabLayout.setupWithViewPager(fragmentPager);
    }


    private void viewStatus(){
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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

}
