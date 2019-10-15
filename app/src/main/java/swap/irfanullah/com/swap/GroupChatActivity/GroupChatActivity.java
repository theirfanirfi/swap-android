package swap.irfanullah.com.swap.GroupChatActivity;

import android.content.Context;
import android.os.Handler;
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

import java.util.ArrayList;

import swap.irfanullah.com.swap.Models.GroupMessages;
import swap.irfanullah.com.swap.R;

public class GroupChatActivity extends AppCompatActivity implements GCLogic.View {
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

        presenter.fetchGroupMessages(context,GROUP_ID);


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.sendMessage(context,GROUP_ID,messageField.getText().toString());
            }
        });

        refereshMessages();


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

    }

    private void initiateGroupSettings(){

    }
}
