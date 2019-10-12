package swap.irfanullah.com.swap.GroupChatActivity;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import swap.irfanullah.com.swap.Libraries.RetroLib;
import swap.irfanullah.com.swap.Models.GroupMessages;
import swap.irfanullah.com.swap.Models.RMsg;
import swap.irfanullah.com.swap.Storage.PrefStorage;

public class GCPresenter implements GCLogic.Presenter {

    private GCLogic.View view;
    private Context context;
    private GroupChatAdapter groupChatAdapter;
    private ArrayList<GroupMessages> groupMessages;

    public GCPresenter(GCLogic.View view, Context context) {
        this.view = view;
        this.context = context;
    }

    @Override
    public GroupChatAdapter setUpRv(RecyclerView rv) {
        groupMessages = new ArrayList<>();
        groupChatAdapter = new GroupChatAdapter(context,groupMessages);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        rv.setLayoutManager(layoutManager);
        rv.setHasFixedSize(true);
        rv.setAdapter(groupChatAdapter);
        return groupChatAdapter;
    }

    @Override
    public void fetchGroupMessages(final Context context, String group_id) {
        RetroLib.geApiService().getGroupChat(PrefStorage.getUser(context).getTOKEN(),group_id).enqueue(new Callback<GroupMessages>() {
            @Override
            public void onResponse(Call<GroupMessages> call, Response<GroupMessages> response) {
                if(response.isSuccessful()){
                    GroupMessages groupMessages = response.body();
                    if(groupMessages.getIS_AUTHENTICATED()){
                        if(groupMessages.getIS_ERROR()){
                            RMsg.toastHere(context,groupMessages.getMESSAGE());
                        }else if(groupMessages.getIS_FOUND()){
                            //groupChatAdapter.notifyAdapter(groupMessages.getMESSENGER());
                            view.onChatLoaded(groupMessages.getMESSENGER());
                        }
                    }else {
                        RMsg.toastHere(context,"You are not loggedin.");

                    }
                }else {
                    RMsg.toastHere(context,"Messages could not be loaded. Try again.");
                }
            }

            @Override
            public void onFailure(Call<GroupMessages> call, Throwable t) {
                RMsg.toastHere(context,t.getMessage());

            }
        });
    }
}
