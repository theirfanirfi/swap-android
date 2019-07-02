package swap.irfanullah.com.swap.PlaceHolders;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

public class StatusMediaPlaceHolder extends Fragment {

public static final String FRAG_KEY = "status_media";
private int _POSITION;
private int _TYPE;

   public static StatusMediaPlaceHolder newInstance(String key, int position, int type) {

        Bundle args = new Bundle();
        args.putString(FRAG_KEY,key);
        StatusMediaPlaceHolder fragment = new StatusMediaPlaceHolder();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View getView() {
        return super.getView();
    }
}
