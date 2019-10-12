package swap.irfanullah.com.swap.GroupChatActivity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
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


    }

    @Override
    public void onChatLoaded(ArrayList<GroupMessages> messenger) {
        groupChatAdapter.notifyAdapter(messenger);
    }
}
