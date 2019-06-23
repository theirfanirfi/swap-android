package swap.irfanullah.com.swap.Adapters;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import swap.irfanullah.com.swap.PlaceHolders.StatusMediaPlaceHolder;

public class StatusMediaPagerAdapter extends FragmentStatePagerAdapter {
List<String> medias = new ArrayList<>();
    int _LAST_FRAG;
    public StatusMediaPagerAdapter(FragmentManager fm, int LAST_FRAG) {
        super(fm);
        this._LAST_FRAG = LAST_FRAG;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public Fragment getItem(int i) {
        return StatusMediaPlaceHolder.newInstance(medias.get(i),i,1);
    }

    @Override
    public int getCount() {
        return medias.size();
    }

    public void addMedia(int position,String title){
        medias.add(position,title);
        notifyDataSetChanged();
    }

    public int getFragmentSize(){
        return medias.size();
    }
}
