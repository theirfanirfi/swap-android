package swap.irfanullah.com.swap;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import swap.irfanullah.com.swap.CustomComponents.ComposeStatusDialog;
import swap.irfanullah.com.swap.Fragments.ChatFragment;
import swap.irfanullah.com.swap.Fragments.DiscoverStatusesFragment;
import swap.irfanullah.com.swap.Fragments.StatusesFragment;
import swap.irfanullah.com.swap.Fragments.SwapsFragment;
import swap.irfanullah.com.swap.Libraries.RetroLib;
import swap.irfanullah.com.swap.Models.Notification;
import swap.irfanullah.com.swap.Models.RMsg;
import swap.irfanullah.com.swap.Services.BackgroundNotificationsService;
import swap.irfanullah.com.swap.Storage.PrefStorage;

public class HomeActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private TextView notificationIconTextView, swap_count;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                fetchNotifications();
                fetchSwapCount();
                handler.postDelayed(this,5000);
            }
        },10000);

        Intent service = new Intent(this, BackgroundNotificationsService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
           // startForegroundService(service);
        }else {
            startService(service);
        }

//        if(PrefStorage.getUserData(this).equals(""))
//        {
//            Intent loginAct = new Intent(this,LoginActivity.class);
//            Toast.makeText(this,"Please Login or Register to continue.",Toast.LENGTH_LONG).show();
//            startActivity(loginAct);
//        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.bluish));
        toolbar.getOverflowIcon().setColorFilter(getResources().getColor(R.color.black) , PorterDuff.Mode.SRC_ATOP);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        View menu_hotlist = menu.findItem(R.id.notification_icon).getActionView();
        notificationIconTextView = (TextView) menu_hotlist.findViewById(R.id.hotlist_hot);
        fetchNotifications();
        notificationIconTextView.setText("");

        menu_hotlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(RMsg.LOG_MESSAGE,"working 1");
                Intent notificationAct = new Intent(HomeActivity.this,NotificationActivity.class);
                startActivity(notificationAct);
            }
        });


        //swap request count

        View swap_menu_hotlist = menu.findItem(R.id.swap_requests_icon).getActionView();
        swap_count = (TextView) swap_menu_hotlist.findViewById(R.id.swap_hotlist_hot);
        fetchSwapCount();
        swap_menu_hotlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(RMsg.LOG_MESSAGE,"working 1");
                Intent swapRequestAct = new Intent(HomeActivity.this,SwapRequestActivity.class);
                startActivity(swapRequestAct);
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        if (id == R.id.logout) {
            PrefStorage.getEditor(this).remove(PrefStorage.USER_PREF_DETAILS).commit();
            PrefStorage.getEditor(this).remove(PrefStorage.START_NEXT_ACTIVITY).commit();
            PrefStorage.getEditor(this).remove(PrefStorage.AFTER_STARTUP_ACTIVITY).commit();
            Intent loginAct = new Intent(this,LoginActivity.class);
            startActivity(loginAct);
            finish();
        }else if(id == R.id.notification_icon){
            //Log.i(RMsg.LOG_MESSAGE,"working");
                Intent notificationAct = new Intent(HomeActivity.this,NotificationActivity.class);
                startActivity(notificationAct);
        }else if(id == R.id.swap_requests_icon){
            //Log.i(RMsg.LOG_MESSAGE,"working");
            Intent swapRequestActivity = new Intent(HomeActivity.this,SwapRequestActivity.class);
            startActivity(swapRequestActivity);
        } else if(id == R.id.profile){
            Intent profileAct = new Intent(HomeActivity.this,UserProfile.class);
            startActivity(profileAct);
        } else if(id == R.id.settings){
            Intent settingsAct = new Intent(HomeActivity.this,Settings.class);
            startActivity(settingsAct);
        }else if(id == R.id.invite){

            sendInvitation();
        }else if(id == R.id.search){
            Intent searchAct = new Intent(HomeActivity.this,SearchActivity.class);
            startActivity(searchAct);
        }

        return true;
    }


    private void sendInvitation(){

        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "Swaps");
            String strShareMessage = "\nLet me recommend you this application\n\n";
            strShareMessage = strShareMessage + "https://play.google.com/store/apps/details?id=" + getPackageName();
//            Uri screenshotUri = Uri.parse("android.resource://"+getPackageName()+"/drawable/person");
//            i.setType("image/png");
//            i.putExtra(Intent.EXTRA_STREAM, screenshotUri);
            i.putExtra(Intent.EXTRA_TEXT, strShareMessage);
            startActivity(Intent.createChooser(i, "Invite via"));
        } catch(Exception e) {
            //e.toString();
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            switch (position)
            {
                case 0:
                    StatusesFragment statusesFragment = new StatusesFragment();
                    return statusesFragment;
                case 1:
                    SwapsFragment swapsFragment = new SwapsFragment();
                    return swapsFragment;
                case 2:
                    ChatFragment chatFragment = new ChatFragment();
                    return chatFragment;
                case 3:
                    DiscoverStatusesFragment discoverStatusesFragment = new DiscoverStatusesFragment();
                    return discoverStatusesFragment;
            }

            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }
    }

    private void fetchNotifications(){
        RetroLib.geApiService().getNotificationsCount(PrefStorage.getUser(this).getTOKEN()).enqueue(new Callback<Notification>() {
            @Override
            public void onResponse(Call<Notification> call, Response<Notification> response) {
                if(response.isSuccessful()){
                    RMsg.logHere(response.raw().toString());
                    Notification notification = response.body();
                    if(notification.getIS_AUTHENTICATED()){
                        notificationIconTextView.setText(Integer.toString(notification.getNOTIFICATIONS_COUNT()));
                    }else {
                        Toast.makeText(HomeActivity.this,RMsg.AUTH_ERROR_MESSAGE,Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(HomeActivity.this,RMsg.REQ_ERROR_MESSAGE,Toast.LENGTH_LONG).show();
                    RMsg.logHere(response.raw().toString());
                }
            }

            @Override
            public void onFailure(Call<Notification> call, Throwable t) {
                Toast.makeText(HomeActivity.this,t.toString(),Toast.LENGTH_LONG).show();

            }
        });
    }

    private void fetchSwapCount(){
        RetroLib.geApiService().getSwapNotificationsCount(PrefStorage.getUser(this).getTOKEN()).enqueue(new Callback<Notification>() {
            @Override
            public void onResponse(Call<Notification> call, Response<Notification> response) {
                if(response.isSuccessful()){
                    Notification notification = response.body();
                    if(notification.getIS_AUTHENTICATED()){
                        swap_count.setText(Integer.toString(notification.getNOTIFICATIONS_COUNT()));
                    }else {
                        Toast.makeText(HomeActivity.this,RMsg.AUTH_ERROR_MESSAGE,Toast.LENGTH_LONG).show();

                    }
                }else {
                    Toast.makeText(HomeActivity.this,RMsg.REQ_ERROR_MESSAGE,Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Notification> call, Throwable t) {
                Toast.makeText(HomeActivity.this,t.toString(),Toast.LENGTH_LONG).show();

            }
        });


    }
}
