package swap.irfanullah.com.swap.Adapters;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;
import swap.irfanullah.com.swap.Libraries.GLib;
import swap.irfanullah.com.swap.Models.Attachments;
import swap.irfanullah.com.swap.R;

public class MediaPager extends PagerAdapter {
    private Context context;
    private ArrayList<Attachments> attachments;
    private VideoView iv;
    private ImageView play;
    private MediaController mediaController;
     CircleIndicator indicator;
     View view;
    private static final String TAG = "MediaPager";
    public MediaPager(Context context,ArrayList<Attachments> attachments) {
        this.context = context;
        this.attachments = attachments;

        Log.i(TAG, "MediaPager: constructor "+this.attachments.size());
    }

    @Override
    public int getCount() {
        return this.attachments.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return (view == (RelativeLayout) o);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Attachments media = this.attachments.get(position);
        if(media.getATTACHMENT_TYPE() == 1) {
            if(mediaController != null)
                mediaController.invalidate();



             view = LayoutInflater.from(context).inflate(R.layout.image_viewer_layout_white, container, false);
            ImageView iv = view.findViewById(R.id.imageView);
            indicator = view.findViewById(R.id.indicator);


            ProgressBar progressBar = view.findViewById(R.id.pagerProgressBar);
            GLib.downloadImage(context,media.getATTACHMENT_URL()).into(iv);

            Log.i(TAG, "MediaPager: Getting View "+media.getATTACHMENT_URL());
            container.addView(view);
            progressBar.setVisibility(View.GONE);
            return view;
        }else {
            View view = LayoutInflater.from(context).inflate(R.layout.video_viewer_layout, container, false);
            iv = view.findViewById(R.id.videoView);
           play = view.findViewById(R.id.playImage);
            mediaController = new MediaController(context);

            mediaController.setAnchorView(iv);
            mediaController.setMediaPlayer(iv);
            iv.setMediaController(mediaController);
            Uri video = Uri.parse(media.getATTACHMENT_URL());
            iv.setVideoURI(video);


            iv.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    play.setVisibility(View.VISIBLE);
                }
            });

            iv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    play.setVisibility(View.VISIBLE);
                }
            });

            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    play.setVisibility(View.GONE);
                    iv.start();
                }
            });


            container.addView(view);
            return view;
        }
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    public void notifyAdapter(ArrayList<Attachments> at){
        this.attachments = at;
        Log.i(TAG, "MediaPager: notify "+at.size());
        notifyDataSetChanged();
    }

    public void setIndicator(ViewPager pager){
       // CircleIndicator indicator = this.view.findViewById(R.id.indicator);
        this.indicator.setViewPager(pager);
    }
}
