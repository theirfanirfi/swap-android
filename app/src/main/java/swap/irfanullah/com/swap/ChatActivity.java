package swap.irfanullah.com.swap;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.devlomi.record_view.OnBasketAnimationEnd;
import com.devlomi.record_view.OnRecordClickListener;
import com.devlomi.record_view.OnRecordListener;
import com.devlomi.record_view.RecordButton;
import com.devlomi.record_view.RecordView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import swap.irfanullah.com.swap.Adapters.ChatAdapter;
import swap.irfanullah.com.swap.Libraries.RetroLib;
import swap.irfanullah.com.swap.Models.Attachments;
import swap.irfanullah.com.swap.Models.Messenger;
import swap.irfanullah.com.swap.Models.RMsg;
import swap.irfanullah.com.swap.Models.User;
import swap.irfanullah.com.swap.Storage.PrefStorage;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class ChatActivity extends AppCompatActivity  implements ChatAdapter.MessageClickListener {

    private RecyclerView rv;
    private ChatAdapter chatAdapter;
    private Context context;
    private final String LOGGEDIN_USER_INTENT_KEY = "loggedin_user_id";
    private final String TO_CHAT_WITH_USER_INTENT_KEY = "to_chat_with_user_id";
    private final String CHAT_ID_INTENT_KEY = "chat_id";
    private int LOGGED_IN_USER = 0;
    private int CHAT_WITH_USER = 0;
    private int CHAT_ID = 0, START =0;
    private User user;
    private ArrayList<Messenger> messengerArrayList;
    private EditText messageField;
    private Button sendBtn;
    private Runnable runnable;
    private Handler handler;
    RecordView recordView;
    RecordButton recordButton;
    public static final int RequestPermissionCode = 1;


    String AudioSavePathInDevice = null;
    MediaRecorder mediaRecorder ;
    Random random ;
    MediaPlayer mediaPlayer ;
    String RandomAudioFileName = "ABCDEFGHIJKLMNOP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initializeObjects();
        getExtras();
        loadMessages();
        sendMessage();
        refereshMessages();
        recorder();


//        random = new Random();
//        AudioSavePathInDevice = Environment.getExternalStorageDirectory().getAbsolutePath() + "/AudioRecording.3gp";
//        mediaRecorder=new MediaRecorder();
//        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_2_TS);
//        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
//        mediaRecorder.setOutputFile(AudioSavePathInDevice);
    }

    private void refereshMessages() {
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadMessages();
                handler.postDelayed(this,5000);
            }
        },5000);
    }

    private void sendMessage() {
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String message = messageField.getText().toString();
                RetroLib.geApiService().sendMessage(user.getTOKEN(),CHAT_WITH_USER,message).enqueue(new Callback<Messenger>() {
                    @Override
                    public void onResponse(Call<Messenger> call, Response<Messenger> response) {
                        if(response.isSuccessful()){
                            Messenger messenger = response.body();
                            if(messenger.getIS_ERROR()){
                                RMsg.toastHere(context,messenger.getRESPONSE_MESSAGE());
                            }else {
                                if(messenger.getIS_AUTHENTICATED()){
                                    if(messenger.getIS_SENT()){
                                       // RMsg.toastHere(context,messenger.getRESPONSE_MESSAGE());
                                        messageField.setText("");
                                        START = 0;
                                        loadMessages();
                                    }else {
                                        RMsg.toastHere(context,messenger.getRESPONSE_MESSAGE());
                                    }
                                }else {
                                    RMsg.toastHere(context,RMsg.AUTH_ERROR_MESSAGE);
                                }
                            }
                        }else {
                            RMsg.toastHere(context,RMsg.REQ_ERROR_MESSAGE);
                            RMsg.logHere(RMsg.REQ_ERROR_MESSAGE);
                            RMsg.logHere(response.raw().toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<Messenger> call, Throwable t) {

                    }
                });
            }
        });
    }

    private void getExtras() {
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            LOGGED_IN_USER = bundle.getInt(LOGGEDIN_USER_INTENT_KEY);
            CHAT_WITH_USER = bundle.getInt(TO_CHAT_WITH_USER_INTENT_KEY);
            CHAT_ID = bundle.getInt(CHAT_ID_INTENT_KEY);
        }else {
            finish();
        }

    }

    private void initializeObjects() {
        context = this;
        user = PrefStorage.getUser(context);
        messageField = findViewById(R.id.messageField);
        sendBtn = findViewById(R.id.sendBtn);
        messengerArrayList = new ArrayList<>();
        rv = findViewById(R.id.chatRV);

        recordView = (RecordView) findViewById(R.id.record_view);
        recordButton = (RecordButton) findViewById(R.id.record_button);
        recordButton.setRecordView(recordView);


        chatAdapter = new ChatAdapter(context, messengerArrayList);
        chatAdapter.setOnMessageClickListener(this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        rv.setLayoutManager(layoutManager);
        rv.setHasFixedSize(true);
        rv.smoothScrollToPosition(messengerArrayList.size() > 0 ? messengerArrayList.size() - 1 : messengerArrayList.size());
        rv.setAdapter(chatAdapter);
    }

    private void recyclerViewSetup(ArrayList<Messenger> messengerArrayList) {
        chatAdapter = new ChatAdapter(context, messengerArrayList);
        //rv.smoothScrollToPosition(messengerArrayList.size() - 1);
        rv.setAdapter(chatAdapter);
    }

    private void notifyAdapter(ArrayList<Messenger> messengerArrayList){
        chatAdapter.notifyAdapter(messengerArrayList);
    }
    private void loadMessages(){
        RMsg.logHere("called");
        RetroLib.geApiService().getMessages(user.getTOKEN(),CHAT_WITH_USER).enqueue(new Callback<Messenger>() {
            @Override
            public void onResponse(Call<Messenger> call, Response<Messenger> response) {
                    if(response.isSuccessful()){
                        Messenger messenger = response.body();
                        if(messenger.getIS_ERROR()){
                            RMsg.logHere(messenger.getRESPONSE_MESSAGE());
                        }else {
                            if(messenger.getIS_AUTHENTICATED()){
                                if(messenger.getIS_FOUND()){
                                    //messengerArrayList = messenger.getMESSENGER();
                                        //messengerArrayList.addAll(messenger.getMESSENGER());
                                    //recyclerViewSetup(messenger.getMESSENGER());

                                    RMsg.ilogHere(messenger.getRESPONSE_MESSAGE().length());

                                    notifyAdapter(messenger.getMESSENGER());
                                    //if(START == 0) {
                                        //START++;
                                        rv.smoothScrollToPosition(messenger.getMESSENGER().size() > 0 ? messenger.getMESSENGER().size() - 1 : messenger.getMESSENGER().size());
                                   // }
                                }else {
                                    RMsg.logHere(messenger.getRESPONSE_MESSAGE());

                                }
                            }else {
                                RMsg.logHere(RMsg.AUTH_ERROR_MESSAGE);
                            }
                        }
                    }else {
                        RMsg.logHere(RMsg.REQ_ERROR_MESSAGE);
                        RMsg.logHere(response.raw().toString());
                    }
            }

            @Override
            public void onFailure(Call<Messenger> call, Throwable t) {
                RMsg.logHere(t.toString());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onMessageClicked(final Messenger msg) {

        //RMsg.toastHere(context,msg.getMESSAGE()+" : "+Integer.toString(msg.getMESSAGE_ID()));
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        String[] choices = {"Forward","Cancel"};

        builder.setItems(choices, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0){
                    // RMsg.toastHere(context,Integer.toString(groupMessage.getMESSAGE_ID()));
                    Intent forwardToAct = new Intent(context, ForwardMessageActivity.class);
                    forwardToAct.putExtra("message_id",msg.getMESSAGE_ID());
                    startActivity(forwardToAct);
                }else{
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }


    private void recorder(){

        recordView.setOnRecordListener(new OnRecordListener() {
            @Override
            public void onStart() {
                //Start Recording..
                //Log.d("RecordView", "onStart");


                if(checkPermission()) {

                    AudioSavePathInDevice = Environment.getExternalStorageDirectory().getAbsolutePath() + "/swapaudiochat.3gp";

//                        Toast.makeText(ChatActivity.this, "Recording PATH: "+AudioSavePathInDevice,
//                                Toast.LENGTH_LONG).show();
                        MediaRecorderReady();

                        try {
                            mediaRecorder.prepare();
                            mediaRecorder.start();
                        } catch (IllegalStateException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }



//
//                    Toast.makeText(ChatActivity.this, "Recording started",
//                            Toast.LENGTH_LONG).show();
                } else {
                    requestPermission();
                }
            }

            @Override
            public void onCancel() {
                //On Swipe To Cancel
                Log.d("RecordView", "onCancel");

            }

            @Override
            public void onFinish(long recordTime) {
                //Stop Recording..
             //   String time = getHumanTimeText(recordTime);

                mediaRecorder.stop();
                mediaRecorder.release();
//
//                Toast.makeText(ChatActivity.this, "Recording Completed",
//                        Toast.LENGTH_LONG).show();
                File file = new File(AudioSavePathInDevice);
                uploadAudioRequest(file);
//                mediaPlayer = new MediaPlayer();
//                try {
//                    mediaPlayer.setDataSource(AudioSavePathInDevice);
//                    mediaPlayer.prepare();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                mediaPlayer.start();
////                Toast.makeText(ChatActivity.this, "Recording Playing",
////                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onLessThanSecond() {
                //When the record time is less than One Second
                //Log.d("RecordView", "onLessThanSecond");
            }
        });


        recordButton.setListenForRecord(true);


        //ListenForRecord must be false ,otherwise onClick will not be called
        recordButton.setOnRecordClickListener(new OnRecordClickListener() {
            @Override
            public void onClick(View v) {

//                Toast.makeText(ChatActivity.this, "RECORD BUTTON CLICKED", Toast.LENGTH_SHORT).show();
//                Log.d("RecordButton","RECORD BUTTON CLICKED");
            }
        });



        recordView.setOnBasketAnimationEndListener(new OnBasketAnimationEnd() {
            @Override
            public void onAnimationEnd() {
               // Log.d("RecordView", "Basket Animation Finished");
            }
        });


    }


    public void MediaRecorderReady(){
        mediaRecorder=new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(AudioSavePathInDevice);
    }

    public String CreateRandomAudioFileName(int string){
        StringBuilder stringBuilder = new StringBuilder( string );
        int i = 0 ;
        while(i < string ) {
            stringBuilder.append(RandomAudioFileName.
                    charAt(random.nextInt(RandomAudioFileName.length())));

            i++ ;
        }
        return stringBuilder.toString();
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(ChatActivity.this, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length> 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(this, "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this,"Permission Denied",Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }




    /////////////////////// upload audio file


    public void uploadAudioRequest(File file){
        User user = PrefStorage.getUser(this);
        RequestBody tokenBody = RequestBody.create(MediaType.parse("multipart/form-data"),user.getTOKEN());
        RequestBody id = RequestBody.create(MediaType.parse("multipart/form-data"),Integer.toString(CHAT_WITH_USER));
//        RequestBody attachment_type = RequestBody.create(MediaType.parse("multipart/form-data"),"1");
        RequestBody aud = RequestBody.create(MediaType.parse("multipart/form-data"),file);
        MultipartBody.Part audio = MultipartBody.Part.createFormData("audio",file.getName(),aud);

        RetroLib.geApiService().sendAudioMessage(tokenBody,audio,id).enqueue(new Callback<Messenger>() {
            @Override
            public void onResponse(Call<Messenger> call, Response<Messenger> response) {

                if(response.isSuccessful()){
                    Messenger med = response.body();
                    if(med.getIS_ERROR()){
                        //if there was anykind of error. Roll back the status
                        RMsg.toastHere(context,med.getMESSAGE());
                    }else {
                        if(med.getIS_AUTHENTICATED()){
                            RMsg.toastHere(context,med.getMESSAGE());

                        }else {
                            RMsg.toastHere(context,med.getMESSAGE());

                        }
                    }
                }else {
                    //statusRollBack();
//                    mAsynRequests.cancel(true);
                   // RMsg.toastHere(context,med.getMESSAGE());

                   // RMsg.toastHere(context,RMsg.REQ_ERROR_MESSAGE);
                }

                //debug logs
                RMsg.logHere(response.raw().toString());
                // RMsg.logHere(response.body().getMESSAGE());
            }

            @Override
            public void onFailure(Call<Messenger> call, Throwable t) {
                RMsg.logHere(t.toString());
            }
        });
    }


    private String getRealPathFromURIPath(Uri contentURI, Context activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }
}
