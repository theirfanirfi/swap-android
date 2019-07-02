package swap.irfanullah.com.swap.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.VideoView;

import java.util.ArrayList;

import swap.irfanullah.com.swap.CustomComponents.SwapsSquareImageView;
import swap.irfanullah.com.swap.Libraries.GLib;
import swap.irfanullah.com.swap.Models.Attachments;
import swap.irfanullah.com.swap.Models.Media;

public class GridMediaStatusAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Attachments> media;
    ImageView imageView;
    VideoView videoView;
    int STATUS_ID = 0;
    private static final String TAG = "GridMediaStatusAdapter";
    public GridMediaStatusAdapter(Context context, ArrayList<Attachments> media, int status_id) {
        this.context = context;
        this.media = media;
        this.STATUS_ID = status_id;
       // Log.i(TAG, "GridMediaStatusAdapter: constructor "+media.size());
    }

    @Override
    public int getCount() {
        return this.media.size();
    }

    @Override
    public Object getItem(int position) {
        return this.media.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Attachments mediaa = this.media.get(position);
        //Log.i(TAG, "getView: "+mediaa.getATTACHMENT_URL());
        if(mediaa.getATTACHMENT_TYPE() == 1) {
            Log.i(TAG, "getView: "+ position+" : "+mediaa.getATTACHMENT_URL() );

            imageView = new SwapsSquareImageView(context);

               imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

//                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(2, 2, 2, 2);

            //imageView.setImageURI(this.media.get(position).getUri());
            GLib.downloadImage(context,mediaa.getATTACHMENT_URL()).into(imageView);
            return imageView;
        }else {
            videoView = new VideoView(context);
            videoView.setLayoutParams(new GridView.LayoutParams(240, 200));
            videoView.setPadding(4, 4, 4, 4);
         //   videoView.setVideoURI(media.getUri());
            videoView.setFocusable(true);
            videoView.start();
            return videoView;
        }

    }

    public void notifyAdapter(ArrayList<Attachments> media) {
        this.media = media;
        Log.i(TAG, "GridMediaStatusAdapter: constructor "+media.size() + " : "+this.media.size());
        notifyDataSetChanged();
    }
}
