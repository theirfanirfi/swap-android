package swap.irfanullah.com.swap.Libraries;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class Compressor {

    public static File getVideoCacheDir(Context context) {
        File dir = context.getExternalCacheDir();
        if (dir == null)
            dir = context.getCacheDir();

        File file = new File(dir.getAbsolutePath() + "/Swaps/Media/Videos/");

        if (!file.exists())
            file.mkdirs();

        return file;
    }

    public static File getImagesCacheDir(Context context) {
        File dir = context.getExternalCacheDir();
        if (dir == null)
            dir = context.getCacheDir();

        String rootDir = dir.getAbsolutePath() + "/Swaps/Media/Images/";
        File file = new File(rootDir);

        if (!file.exists())
            file.mkdirs();

        return file;
    }


    public static class ImageCompressor extends AsyncTask<Bitmap,Integer,Byte[]>{
        @Override
        protected Byte[] doInBackground(Bitmap... bitmaps) {
            return new Byte[0];
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Byte[] bytes) {
            super.onPostExecute(bytes);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }

    public static Bitmap uri2bitmap(Context context,Uri uri) throws IOException {
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
        return bitmap;
    }

}
