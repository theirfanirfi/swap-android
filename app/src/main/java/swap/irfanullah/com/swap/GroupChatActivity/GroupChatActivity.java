package swap.irfanullah.com.swap.GroupChatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
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
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
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
import com.google.gson.Gson;

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
import swap.irfanullah.com.swap.ChatActivity;
import swap.irfanullah.com.swap.ForwardMessageActivity;
import swap.irfanullah.com.swap.InviteToGroupActivity;
import swap.irfanullah.com.swap.Libraries.RetroLib;
import swap.irfanullah.com.swap.Models.GroupMessages;
import swap.irfanullah.com.swap.Models.Messenger;
import swap.irfanullah.com.swap.Models.RMsg;
import swap.irfanullah.com.swap.Models.User;
import swap.irfanullah.com.swap.R;
import swap.irfanullah.com.swap.Storage.PrefStorage;
import swap.irfanullah.com.swap.SwapWithActivity;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class GroupChatActivity extends AppCompatActivity implements GCLogic.View, GroupChatAdapter.MessageClickListener {
    private RecyclerView rv;
    private GroupChatAdapter groupChatAdapter;
    private GCPresenter presenter;
    private Context context;
    private EditText messageField;
    private Button sendBtn;
    private String GROUP_ID = "0";

    private Runnable runnable;
    private Handler handler;

    ArrayList<GroupMessages> messenger;

    RecordView recordView;
    RecordButton recordButton;
    public static String AudioSavePathInDevice = "";

    public static final int RequestPermissionCode = 12;


    MediaRecorder mediaRecorder ;
    Random random ;
    MediaPlayer mediaPlayer ;
    String RandomAudioFileName = "ABCDEFGHIJKLMNOP";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        context = this;

        rv = findViewById(R.id.chatRV);
        messageField = findViewById(R.id.messageField);
        sendBtn = findViewById(R.id.sendBtn);

        ///

        recordView = (RecordView) findViewById(R.id.record_view);
        recordButton = (RecordButton) findViewById(R.id.record_button);
        recordButton.setRecordView(recordView);

        //get sent group id

        GROUP_ID = getIntent().getExtras().getString("group_id");

        //initialize presenter
        presenter = new GCPresenter(this,context);
        groupChatAdapter = presenter.setUpRv(rv);
        groupChatAdapter.setOnMessageClickListener(this);

        presenter.fetchGroupMessages(context,GROUP_ID);


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.sendMessage(context,GROUP_ID,messageField.getText().toString());
            }
        });

        refereshMessages();


        recorder();

    }


    private void refereshMessages() {
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
               presenter.fetchGroupMessages(context,GROUP_ID);
                handler.postDelayed(this,5000);
            }
        },5000);
    }

    @Override
    public void onChatLoaded(ArrayList<GroupMessages> messenger) {
        this.messenger = messenger;
        groupChatAdapter.notifyAdapter(messenger);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home){
            finish();
            return true;
        }else {
            switch (id){
                case R.id.invite_mem:
                    inviteMembersToGroup();
                    break;
                case R.id.group_settings:
                    initiateGroupSettings();
                    break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMessageSent(GroupMessages groupMessage) {
        this.messenger.add(groupMessage);
        groupChatAdapter.notifyAdapter(this.messenger);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.group_chat_menu,menu);
        return true;
    }

    private void inviteMembersToGroup(){
        Intent inviteToGroupAct = new Intent(context, InviteToGroupActivity.class);
        inviteToGroupAct.putExtra("group_id",Integer.parseInt(GROUP_ID));
        startActivity(inviteToGroupAct);
    }

    private void initiateGroupSettings(){

    }

    @Override
    public void onMessageClicked(final GroupMessages groupMessage) {
        //RMsg.toastHere(context,groupMessage.getMESSAGE());
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        String[] choices = {"Forward","Cancel"};
//        builder.setSingleChoiceItems(choices, -1, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                if(which == 0){
//                    RMsg.toastHere(context,"foward");
//                }else{
//                    dialog.dismiss();
//                }
//            }
//        });

        builder.setItems(choices, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0){
                   // RMsg.toastHere(context,Integer.toString(groupMessage.getMESSAGE_ID()));
                    Intent forwardToAct = new Intent(context, ForwardMessageActivity.class);
                    forwardToAct.putExtra("message_id",groupMessage.getMESSAGE_ID());
                    startActivity(forwardToAct);
                }else{
                    dialog.dismiss();
                }
            }
        });
        builder.show();
        //builder.setMultiChoiceItems(choices,)
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
        ActivityCompat.requestPermissions(GroupChatActivity.this, new
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
        RequestBody id = RequestBody.create(MediaType.parse("multipart/form-data"),GROUP_ID);
//        RequestBody attachment_type = RequestBody.create(MediaType.parse("multipart/form-data"),"1");
        RequestBody aud = RequestBody.create(MediaType.parse("multipart/form-data"),file);
        MultipartBody.Part audio = MultipartBody.Part.createFormData("audio",file.getName(),aud);

        RetroLib.geApiService().sendAudioMessageToGroup(tokenBody,audio,id).enqueue(new Callback<GroupMessages>() {
            @Override
            public void onResponse(Call<GroupMessages> call, Response<GroupMessages> response) {

                if(response.isSuccessful()){
                    GroupMessages med = response.body();
                    if(med.getIS_ERROR()){
                        //if there was anykind of error. Roll back the status
                        RMsg.toastHere(context,med.getMESSAGE());
                    }else {
                        if(med.getIS_AUTHENTICATED()){
                            if(med.getIS_SENT()){
//                                messengerArrayList.add(med.getMSG());
//                                chatAdapter.notifyAdapter(messengerArrayList);

                                RMsg.toastHere(context,med.getMESSAGE());

                            }
                            RMsg.toastHere(context,med.getMESSAGE());

                        }else {
                            RMsg.toastHere(context,med.getMESSAGE());

                        }
                    }
                }else {
                    //statusRollBack();
//                    mAsynRequests.cancel(true);
                     //RMsg.toastHere(context,med.getMESSAGE());

                    RMsg.toastHere(context,response.raw().toString());
                }

                //debug logs
                RMsg.logHere(response.raw().toString());
                 RMsg.logHere(response.body().getMESSAGE());
            }

            @Override
            public void onFailure(Call<GroupMessages> call, Throwable t) {
                RMsg.logHere(t.toString());
                RMsg.toastHere(context,t.toString());

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
