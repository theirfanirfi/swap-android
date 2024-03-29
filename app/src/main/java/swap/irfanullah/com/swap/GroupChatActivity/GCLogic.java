package swap.irfanullah.com.swap.GroupChatActivity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import swap.irfanullah.com.swap.Models.GroupMessages;

public interface GCLogic {

    interface View {
        void onChatLoaded(ArrayList<GroupMessages> messenger);
        void onMessageSent(GroupMessages groupMessage);
    }

    interface Presenter {
        GroupChatAdapter setUpRv(RecyclerView rv);
        void fetchGroupMessages(Context context,String group_id);
        void sendMessage(Context context, String group_id, String msg);
    }
}
