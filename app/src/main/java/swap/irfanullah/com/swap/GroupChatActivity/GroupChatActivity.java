package swap.irfanullah.com.swap.GroupChatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.Gson;

import java.util.ArrayList;

import swap.irfanullah.com.swap.ForwardMessageActivity;
import swap.irfanullah.com.swap.Models.GroupMessages;
import swap.irfanullah.com.swap.Models.RMsg;
import swap.irfanullah.com.swap.R;
import swap.irfanullah.com.swap.Storage.PrefStorage;
import swap.irfanullah.com.swap.SwapWithActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        context = this;

        rv = findViewById(R.id.chatRV);
        messageField = findViewById(R.id.messageField);
        sendBtn = findViewById(R.id.sendBtn);

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

        //refereshMessages();
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
        Gson gson = new Gson();
        RMsg.logHere(gson.toJson(this.messenger.get(0)).toString());
        RMsg.logHere(gson.toJson(this.messenger.get(1)).toString());
        RMsg.logHere(gson.toJson(this.messenger.get(2)).toString());
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
}