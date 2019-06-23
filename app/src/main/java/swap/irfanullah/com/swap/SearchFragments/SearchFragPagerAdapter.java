package swap.irfanullah.com.swap.SearchFragments;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class SearchFragPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> fragmentTitles = new ArrayList<>();

    public SearchFragPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        return fragmentList.get(i);
    }

    @Override
    public int getCount() {
        return fragmentTitles.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitles.get(position);
    }

    public void addFragment(Fragment frag, String title){
        fragmentList.add(frag);
        fragmentTitles.add(title);
       // notifyDataSetChanged();
    }

    public void clearFrags(){
        if(sizeOfFragList() > 0) {

            for (int i = 0; i < fragmentTitles.size(); i++) {
                fragmentTitles.remove(i);
                fragmentList.remove(i);
            }
        }
//
//        fragmentList.clear();
//        fragmentTitles.clear();
    }

    public int sizeOfFragList(){
        return fragmentList.size();
    }
}
