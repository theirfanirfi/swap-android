package swap.irfanullah.com.swap;

import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import swap.irfanullah.com.swap.Adapters.ChatAdapter;
import swap.irfanullah.com.swap.Libraries.RetroLib;
import swap.irfanullah.com.swap.Models.Messenger;
import swap.irfanullah.com.swap.Models.RMsg;
import swap.irfanullah.com.swap.Models.User;
import swap.irfanullah.com.swap.Storage.PrefStorage;

public class ChatActivity extends AppCompatActivity {

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
    private ImageView sendBtn;
    private Runnable runnable;
    private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initializeObjects();
        getExtras();
        loadMessages();
        sendMessage();
        refereshMessages();
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
        chatAdapter = new ChatAdapter(context, messengerArrayList);
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
}
