package swap.irfanullah.com.swap.StatusActivityFrags;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;

public class CommentsRatersPager extends FragmentPagerAdapter {
    private int STATUS_ID = 0;
    private Bundle bundle;
    public CommentsRatersPager(FragmentManager fm, int STATUS_ID) {
        super(fm);
        this.STATUS_ID = STATUS_ID;
        bundle = new Bundle();
        bundle.putString("status_id",Integer.toString(this.STATUS_ID));
    }

    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0:
                CommentsFragment commentsFragment = new CommentsFragment();
                commentsFragment.setArguments(bundle);
                return commentsFragment;
            case 1:
                RatersFragment ratersFragment = new RatersFragment();
                ratersFragment.setArguments(bundle);
                return ratersFragment;

        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
}
