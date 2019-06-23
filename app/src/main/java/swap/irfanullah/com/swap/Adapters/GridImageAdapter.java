package swap.irfanullah.com.swap.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.VideoView;

import java.util.ArrayList;
import java.util.List;

import swap.irfanullah.com.swap.CustomComponents.SwapsSquareImageView;
import swap.irfanullah.com.swap.CustomComponents.SwapsSquareImageViewForComposing;
import swap.irfanullah.com.swap.CustomComponents.SwapsSquareVideoViewForComposing;
import swap.irfanullah.com.swap.Models.Media;
import swap.irfanullah.com.swap.Models.RMsg;

public class GridImageAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Media> media;
    ImageView imageView;
    VideoView videoView;

    public GridImageAdapter(Context context, ArrayList<Media> media) {
        this.context = context;
        this.media = media;
    }

    @Override
    public int getCount() {
        return this.media.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Media media = this.media.get(position);
        if(media.getType() == 1) {

                imageView = new SwapsSquareImageView(context);
               imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
 imageView.setPadding(2, 2, 2, 2);

            imageView.setImageURI(this.media.get(position).getUri());
            return imageView;
        }else {
            videoView = new VideoView(context);
            videoView.setLayoutParams(new GridView.LayoutParams(240, 200));
            videoView.setPadding(4, 4, 4, 4);
            videoView.setVideoURI(media.getUri());
            videoView.setFocusable(true);
            videoView.start();
            return videoView;
        }

    }

    public void notifyAdapter(ArrayList<Media> media) {
        this.media = media;
        notifyDataSetChanged();
    }
}
