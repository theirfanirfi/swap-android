package swap.irfanullah.com.swap;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import swap.irfanullah.com.swap.Models.RMsg;
import swap.irfanullah.com.swap.SearchFragments.SearchFragPagerAdapter;
import swap.irfanullah.com.swap.SearchFragments.StatusesFragment;
import swap.irfanullah.com.swap.SearchFragments.UsersFragment;

public class SearchActivity extends AppCompatActivity {
    String text = "";
    private ViewPager pager;
    private TabLayout tabLayout;
    private SearchFragPagerAdapter searchFragPagerAdapter;
    private TextView searchText, SearchDesc;
    StatusesFragment statusesFragment = null;
    UsersFragment usersFragment = null;
    Bundle bundle = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        tabLayout = findViewById(R.id.search_frag_tabs);
        pager = findViewById(R.id.search_frag_pager);
        searchText = findViewById(R.id.search_textView);
        SearchDesc = findViewById(R.id.search_textView_desc);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_act_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search_view);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search");
        searchView.setIconified(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchFragPagerAdapter = new SearchFragPagerAdapter(getSupportFragmentManager());

                if(statusesFragment == null  && usersFragment == null && bundle == null ){
                    statusesFragment = new StatusesFragment();
                    usersFragment = new UsersFragment();
                    bundle = new Bundle();
                    bundle.putString("search_keyword",query);
                    RMsg.logHere("It was null");
                }else {
//                    statusesFragment = null;
//                    usersFragment = null;
//                    bundle = null;
//                    bundle = new Bundle();
                    bundle.putString("search_keyword",query);
                    statusesFragment = new StatusesFragment();
                    usersFragment = new UsersFragment();
                    RMsg.logHere("made null");

                }


                statusesFragment.setArguments(bundle);
                usersFragment.setArguments(bundle);

                //if(searchFragPagerAdapter.sizeOfFragList() > 0) {
                    searchFragPagerAdapter.clearFrags();
               // }



                searchFragPagerAdapter.addFragment(statusesFragment,"Statuses");
                searchFragPagerAdapter.addFragment(usersFragment,"Users");

                pager.setAdapter(searchFragPagerAdapter);

                //statusesFragment.makeRequest(query, SearchActivity.this);

                tabLayout.setupWithViewPager(pager);

                searchText.setVisibility(View.GONE);
                SearchDesc.setVisibility(View.GONE);

                tabLayout.setVisibility(View.VISIBLE);
                pager.setVisibility(View.VISIBLE);

               // RMsg.toastHere(SearchActivity.this,Integer.toString(searchFragPagerAdapter.sizeOfFragList()));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                text = newText;
                return false;
            }
        });

        searchView.setEnabled(true);
        return super.onCreateOptionsMenu(menu);
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
