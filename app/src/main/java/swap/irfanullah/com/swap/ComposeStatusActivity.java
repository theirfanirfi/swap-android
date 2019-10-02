package swap.irfanullah.com.swap;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.util.Consumer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.iceteck.silicompressorr.SiliCompressor;
import com.soundcloud.android.crop.Crop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import swap.irfanullah.com.swap.Adapters.GridImageAdapter;
import swap.irfanullah.com.swap.Libraries.Compressor;
import swap.irfanullah.com.swap.Libraries.RealPathUtils;
import swap.irfanullah.com.swap.Libraries.RetroLib;
import swap.irfanullah.com.swap.Models.Media;
import swap.irfanullah.com.swap.Models.RMsg;
import swap.irfanullah.com.swap.Models.Status;
import swap.irfanullah.com.swap.Models.User;
import swap.irfanullah.com.swap.Services.StatusMediaService;
import swap.irfanullah.com.swap.Storage.PrefStorage;

import static swap.irfanullah.com.swap.AppClasses.App.CHANNEL_ID;

public class ComposeStatusActivity extends AppCompatActivity {

    private Context context;
    private final int PICK_IMAGE_REQUEST_CODE = 1;
    private User user;
    private GridView gv;
    private GridImageAdapter gridImageAdapter;
    private ArrayList<Media> uris;
    private ProgressBar progressBar;
    private final int IMAGE_ACTION = 1;
    private final int VIDEO_ACTION = 2;
    private final int OPEN_CAMERA_FOR_IMAGE = 0;
    private final int OPEN_FILES_DIR = 1;
    private final int OPEN_CAMERA_FOR_VIDEO = 0;
    private final int OPEN_FILES_DIR_VIDEO = 1;
    private final int REQUEST_IMAGE_CAPTURE = 111;
    String mCurrentPhotoPath;
    static final int REQUEST_VIDEO_CAPTURE = 1;
    private static final int REQUEST_TAKE_GALLERY_VIDEO = 333;
    private static final int NUM_GRIDS = 2;
    private static int GRID_WIDTH;
    private EditText status;
    //proposed attachment policy, but currently not followed
    //if video is added, no images should be selected
    // because videos size might be larger. So,there should be only 1 video and 0 images
    //but duration of the video is currently set to @2 minutes
    private Boolean IS_VIDEO_ADDED = false;
    //proposed image limit but currently removed.
    private int PICTURES_LIMIT = 5;
    //Number of requests to be made, it will determine the number
    //of attachments as well.
    private int NUMBER_OF_REQUESTS = 0;
    private int STATUS_ID = 0;
    //ATTACHMENTS ARE: images and Videos
    private Boolean IS_ATTACHMENT_REQUEST = false;
    private final String _SERVICE_INTENT_STATUS_ID = "post_id";


    public static final int WRITE_PERMISSIONS_REQUEST_CODE = 0346;
    public static final int VIDEO_CAMERA_PERMISSIONS_REQUEST_CODE = 872;
    public static final int CAMERA_PERMISSIONS_REQUEST_CODE = 39;

    public static final int WRITE_CAMERA_PERMISSIONS_REQUEST_CODE = 98;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_status);
        getSupportActionBar().setTitle("New");
        initializeObjects();
    }

    private void initializeObjects() {
        context = this;
        user = PrefStorage.getUser(context);
        gv = findViewById(R.id.composeStatusGridView);
        status = findViewById(R.id.composeProfileDescTextArea);

        GRID_WIDTH = getResources().getDisplayMetrics().widthPixels;
        int imgWidht = GRID_WIDTH/NUM_GRIDS;
        gv.setColumnWidth(imgWidht);

        uris = new ArrayList<>();
        progressBar = findViewById(R.id.progressBarStatus);
        gridImageAdapter = new GridImageAdapter(context, uris);
        gv.setAdapter(gridImageAdapter);

        //when item in the grid view is long clicked
        // so the item will be removed.
        removeItemsFormTheGridView();
    }

    private void removeItemsFormTheGridView() {

        gv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //when item in the grid view is long clicked
                // so the item will be removed.
                uris.remove(position);
               //gv.removeView(view);
                gridImageAdapter.notifyAdapter(uris);
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.compose_status_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.camera:
                ChooseAction(IMAGE_ACTION);
                break;
            case R.id.video:
                ChooseAction(VIDEO_ACTION);
                break;
            case R.id.postStatus:
                String nStatus = status.getText().toString();
                if(nStatus.equals("")
                        && uris.size() <= 0
                        ){
                    RMsg.toastHere(context,"Status cannot be empty.");
                }else {
                    postStatus(nStatus);
                }
                break;
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void postStatus(String nStatus) {
        //the status has no media
       if(uris.size() <= 0){
            statusComposeRequest(nStatus);
        }else {
            //if the status had media attachments,
            // the @IS_ATTACHMENT_REQUEST variable will have true
            // and the service will be initiated to upload medias
            IS_ATTACHMENT_REQUEST = true;
            NUMBER_OF_REQUESTS = uris.size();
            //RMsg.logHere(getRealPathFromURIPath(uris.get(0).getUri(),ComposeStatusActivity.this));

          // File file = new File(path);
            statusComposeRequest(nStatus);
            //initiateMediaUploadServiceRequests();

        }
    }

    //request will be made to the server, after successful posting
    // the status_id will be obtained, which will be used to
    // upload status images/videos
    private void statusComposeRequest(String status)  {
        RMsg.logHere(user.getTOKEN());
        RetroLib.geApiService().composeStatus(user.getTOKEN(),status).enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if(response.isSuccessful())
                {
                    if(response.body().getAuthenticated()) {
                        if (response.body().getPosted()) {
                           // progressBar.setVisibility(View.GONE);

                            STATUS_ID = response.body().getOBJ_STATUS().getSTATUS_ID();

                            if(IS_ATTACHMENT_REQUEST) {
                                //BACKGROUND SERVICE WILL BE STARTED FOR UPLOADING IMAGES
                                // OF THE STATUS
                                initiateMediaUploadServiceRequests();
                            }else {
                                RMsg.toastHere(context,"Status Posted");
                                finish();
                            }

                           // Toast.makeText(context, response.body().getMESSAGE() + ", now uploading attachments.", Toast.LENGTH_LONG).show();
                        } else {
                            //progressBar.setVisibility(View.GONE);
                            Toast.makeText(context, response.body().getMESSAGE(), Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        //progressBar.setVisibility(View.GONE);
                        Toast.makeText(context, response.body().getMESSAGE(), Toast.LENGTH_LONG).show();

                    }
                }
                else
                {
                    //progressBar.setVisibility(View.GONE);
                    Toast.makeText(context,response.raw().toString(),Toast.LENGTH_LONG).show();
                    RMsg.logHere(response.raw().toString());

                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Toast.makeText(context,t.toString(),Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK){
            Uri source_uri = data.getData();
            //image will be compressed, added to @URIs list and @GridView
            // @adapter will be updated.
                new ImageCompressor(source_uri,context).execute(source_uri);
            //Uri destination_uri = Uri.fromFile(Compressor.getImagesCacheDir(context));
            Long tsLong = System.currentTimeMillis()/1000;
//            String file_name = Integer.toString(RMsg.getRandom())+ user.getFULL_NAME()+tsLong.toString()+"pick";
            //Uri destination_uri = Uri.fromFile(new File(getCacheDir(),file_name));

            RMsg.logHere(source_uri.toString());

        }else if(requestCode == Crop.REQUEST_CROP && resultCode == RESULT_OK){
        //currently there is no cropping functionality in the status media attachment
        }else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            //@bitmap uri will be obtained here.
          Uri uri = getImageUri(context,bitmap);
            //image will be compressed, added to @URIs list and @GridView
            // @adapter will be updated.
          new ImageCompressor(uri,context).execute(uri);
        }
        else if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Uri videoUri = data.getData();
            uris.add(new Media(videoUri,2));



            gridImageAdapter.notifyAdapter(uris);
            IS_VIDEO_ADDED = true;
           // mVideoView.setVideoURI(videoUri);
        }

        else if (requestCode == REQUEST_TAKE_GALLERY_VIDEO && resultCode == RESULT_OK) {
            Uri videoUri = data.getData();

            String path = null;
            if (Build.VERSION.SDK_INT < 11)
                path = RealPathUtils.getRealPathFromURI_BelowAPI11(context, videoUri);

                // SDK >= 11 && SDK < 19
            else if (Build.VERSION.SDK_INT < 19)
                path = RealPathUtils.getRealPathFromURI_API11to18(context, videoUri);

                // SDK > 19 (Android 4.4)
            else
                path = RealPathUtils.getRealPathFromURI_API19(context, videoUri);
            Log.d(RMsg.LOG_MESSAGE, "File Path: " + path);
            // Get the file instance

            File file = new File(path);

            try {
                new VideoCompressor().execute(path);
            }catch (Exception e){
                RMsg.toastHere(context,e.toString());
            }

            // mVideoView.setVideoURI(videoUri);
        }

        else {
           finish();
        }
    }

    //to choose, whether Image or Video button is clicked
    //then to choose whether the image should be captured or taken from
    // gallery photos.
    private void ChooseAction(int ACTION){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);

        //if image action was initiated
        if(ACTION == IMAGE_ACTION) {
            final String[] action = {"Take Photo", "Choose from files"};

                builder.setItems(action, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == OPEN_CAMERA_FOR_IMAGE) {

                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(ComposeStatusActivity.this,new String[]{Manifest.permission.CAMERA},CAMERA_PERMISSIONS_REQUEST_CODE);
                                }else {
                                    startCameraForImageCapture();
                                }
                            }else {
                                startCameraForImageCapture();
                            }


                        } else {
                            //HERE THE PERMISSIONS SHOULD BE REQUESTED.
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(ComposeStatusActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSIONS_REQUEST_CODE);
                                } else {
                                    //already granted:
                                    Crop.pickImage(ComposeStatusActivity.this);
                                }
                            } else {
                                Crop.pickImage(ComposeStatusActivity.this);
                            }
                        }
                    }
                });


            builder.create().show();
        }
        //else, if the action was of video
        else {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if(ContextCompat.checkSelfPermission(context,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(ComposeStatusActivity.this,new String[]{Manifest.permission.CAMERA},VIDEO_CAMERA_PERMISSIONS_REQUEST_CODE);
                }else {
                    startVideoCameraForRecording();
                }
            }else {
                startVideoCameraForRecording();
            }
//            final String[] action = {"Record Video", "Choose from files"};
//            builder.setItems(action, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    if(which == OPEN_CAMERA_FOR_VIDEO) {
//                        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//                        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
//                            //takeVideoIntent.putExtra("android.intent.extra.durationLimit", 30000);
//                            takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT,5);
//                            //takeVideoIntent.putExtra("EXTRA_VIDEO_QUALITY", 0);
//                            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
//                        }
//                    }else {
//                        Intent intent = new Intent();
//                        intent.setType("video/*");
//                        intent.setAction(Intent.ACTION_GET_CONTENT);
//                        startActivityForResult(Intent.createChooser(intent,"Select Video"),REQUEST_TAKE_GALLERY_VIDEO);
//
//                    }
//                }
//            });

            builder.create().show();
        }


    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    //to get bitmap URI
    public Uri getImageUri(Context context, Bitmap inImage) {


            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
            return Uri.parse(path);

    }

    public class ImageCompressor extends AsyncTask<Uri,Integer,byte[]> {
        private Uri uri;
        private Context context;

        public ImageCompressor(Uri uri,Context context) {
            this.uri = uri;
            this.context = context;
        }

        @Override
        protected byte[] doInBackground(Uri... params) {
            byte[] bytes = null;
            try {
                Bitmap bitmap = Compressor.uri2bitmap(context,params[0]);
                bytes = getBytesFromBitmap(bitmap,80);
            } catch (IOException e) {
                RMsg.logHere(e.getMessage());
            }

            return bytes;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(byte[] bytes) {
            super.onPostExecute(bytes);
            if(bytes != null){
                Bitmap compressedBitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                Uri uri = getImageUri(context,compressedBitmap);
                uris.add(new Media(uri,1));
                gridImageAdapter.notifyAdapter(uris);
                progressBar.setVisibility(View.GONE);
                if(PICTURES_LIMIT > 0){
                    PICTURES_LIMIT--;
                }
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setProgress(values[0]);

        }
    }

    public static byte[] getBytesFromBitmap(Bitmap bitmap,int quality){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality,stream);
        return stream.toByteArray();
    }

    public class VideoCompressor extends AsyncTask<String,Integer,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String path) {
            super.onPostExecute(path);
            Uri uri = Uri.fromFile(new File(path));
            uris.add(new Media(uri,2));
            gridImageAdapter.notifyAdapter(uris);
            RMsg.logHere(path);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... uris) {
            String file_path = null;
            try {
                //file_path = SiliCompressor.with(context).compressVideo(uris[0],Compressor.getVideoCacheDir(context).getAbsolutePath());

                RMsg.logHere("compressed: "+file_path);
            } catch (Exception e) {
                e.printStackTrace();
                RMsg.logHere(e.toString());
            }

            return file_path;
        }
    }

    //FOR SENDING URIS and to the @StatusMediaService for
    //background uploading.
    public void initiateMediaUploadServiceRequests(){
        Intent i = new Intent(context,StatusMediaService.class);
        Bundle bundle = new Bundle();
        bundle.putInt(_SERVICE_INTENT_STATUS_ID,STATUS_ID);
        bundle.putParcelableArrayList("uris",uris);
        bundle.putString("token",user.getTOKEN());
        i.putExtras(bundle);
        startService(i);
        finish();
        RMsg.toastHere(context,"Status is being uploaded in the background.");

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == WRITE_PERMISSIONS_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Crop.pickImage(ComposeStatusActivity.this);
            RMsg.logHere("Permission granted result called.");
        }else if(requestCode == VIDEO_CAMERA_PERMISSIONS_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
         startVideoCameraForRecording();
        }else if(requestCode == CAMERA_PERMISSIONS_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            cameraWriteExStoragePermission();

        }else if(requestCode == WRITE_CAMERA_PERMISSIONS_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startCameraForImageCapture();

        } else {
            RMsg.toastHere(context,"Permission denied.");
            RMsg.logHere("Permission denied");
        }

    }

    private void cameraWriteExStoragePermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ComposeStatusActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_CAMERA_PERMISSIONS_REQUEST_CODE);
            } else {
                //already granted:
                startCameraForImageCapture();

            }
        } else {
            startCameraForImageCapture();

        }
    }

    private void startVideoCameraForRecording(){

        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            //takeVideoIntent.putExtra("android.intent.extra.durationLimit", 30000);
            takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 120);
            takeVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
            // takeVideoIntent.putExtra("EXTRA_VIDEO_QUALITY", 50);
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    private void startCameraForImageCapture(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
