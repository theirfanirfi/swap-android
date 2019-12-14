package swap.irfanullah.com.swap.SwapRequestActivityFragments;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class SwapRequestsFragmentPagerAdapter extends FragmentPagerAdapter {


    public SwapRequestsFragmentPagerAdapter(Context context,FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0:
                return new SwapRequestsFragment();
            case 1:
                return new SwapReviewFragment();

        }
        return new SwapRequestsFragment();
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Swap Requests";
            case 1:
                return "Swaps Reviews";
        }
        return super.getPageTitle(position);
    }
}
