package swap.irfanullah.com.swap.Adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import swap.irfanullah.com.swap.Fragments.Profile.StatusesFragment;
import swap.irfanullah.com.swap.Fragments.Profile.SwapsFragment;

public class ProfilePagerAdapter extends FragmentPagerAdapter {
    int USER_ID = 0;
    public ProfilePagerAdapter(FragmentManager fm, int user_id) {
        super(fm);
        USER_ID = user_id;
    }

    public ProfilePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {

        Bundle bundle = new Bundle();
        bundle.putInt("user_id",USER_ID);
        switch (i)
        {
            case 0:
                StatusesFragment statusesFragment = new StatusesFragment();
                statusesFragment.setArguments(bundle);
                return statusesFragment;
            case 1:
                SwapsFragment swapsFragment = new SwapsFragment();
                swapsFragment.setArguments(bundle);
                return swapsFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
