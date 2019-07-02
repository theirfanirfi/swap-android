package swap.irfanullah.com.swap.Libraries;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;

public class GLib {

    public static RequestBuilder<Drawable> downloadImage(Context context, String url)
    {
        return Glide.with(context)
                .load(url);
    }
}
