package swap.irfanullah.com.swap.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import swap.irfanullah.com.swap.Adapters.ParticipantsAdapter;
import swap.irfanullah.com.swap.ComposeStatusActivity;
import swap.irfanullah.com.swap.CustomComponents.CreateGroupDialog;
import swap.irfanullah.com.swap.GroupChatActivity.GroupChatActivity;
import swap.irfanullah.com.swap.Libraries.RetroLib;
import swap.irfanullah.com.swap.Models.Groups;
import swap.irfanullah.com.swap.Models.Participants;
import swap.irfanullah.com.swap.Models.RMsg;
import swap.irfanullah.com.swap.Models.User;
import swap.irfanullah.com.swap.R;
import swap.irfanullah.com.swap.Storage.PrefStorage;

public class ChatFragment extends Fragment implements SearchView.OnQueryTextListener, CreateGroupDialog.CreateGroupCallBack {

    private ProgressBar progressBar;
    private RecyclerView rv;
    private ParticipantsAdapter  participantsAdapter;
    private Context context;
    private ArrayList<Participants> participantsArrayList;
    SearchView searchView;
    CreateGroupDialog dialog;
    User user;
    public ChatFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);
        searchView = rootView.findViewById(R.id.chatSearchView);
        context = getContext();
        searchView.setQueryHint("Search chat");
        searchView.setEnabled(true);
        searchView.setFocusable(true);
        searchView.setOnQueryTextListener(this);
        user = PrefStorage.getUser(context);
        initializeObjects(rootView);
            getParticipants();
         return rootView;
    }

    public void initializeObjects(View rootView){
        participantsArrayList = new ArrayList<>();
        rv = rootView.findViewById(R.id.chatRV);
        progressBar = rootView.findViewById(R.id.participantLoadingProgressbar);
        dialog = new CreateGroupDialog();
        dialog.setOnGroupCreatedListener(this);
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Intent composeAct = new Intent(context, ComposeStatusActivity.class);
//                startActivity(composeAct);


                dialog.show(getFragmentManager(),"create_group");
            }
        });


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        rv.setLayoutManager(layoutManager);
        rv.setHasFixedSize(true);
        participantsAdapter = new ParticipantsAdapter(context,participantsArrayList);
        rv.setAdapter(participantsAdapter);
    }

    public void getParticipants(){
        RetroLib.geApiService().getParticipants(PrefStorage.getUser(context).getTOKEN()).enqueue(new Callback<Participants>() {
            @Override
            public void onResponse(Call<Participants> call, Response<Participants> response) {
                if(response.isSuccessful()){
                    Participants participants = response.body();
                    if(participants.getAuthenticated()){
                        if(participants.getFound()){
                            updateAdapter(participants.getPARTICIPANTS());
                        }else if(participants.getError()) {
                            //RMsg.toastHere(context,participants.getMESSAGE());
                        }else {
                          //  RMsg.toastHere(context,participants.getMESSAGE());
                        }
                    }else {
                        RMsg.toastHere(getContext(),RMsg.AUTH_ERROR_MESSAGE);
                    }

                }else {
                   // RMsg.toastHere(getContext(),RMsg.REQ_ERROR_MESSAGE);
                }
            }

            @Override
            public void onFailure(Call<Participants> call, Throwable t) {
                RMsg.toastHere(getContext(),t.toString());
            }
        });

        progressBar.setVisibility(View.GONE);
    }

    public void updateAdapter(ArrayList<Participants> participantsArrayList1){
        this.participantsArrayList= participantsArrayList1;
        participantsAdapter.notifyAdapter(participantsArrayList1);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
      //  RMsg.toastHere(context,"working");
        filterChat(s);
        return false;
    }

    private void filterChat(String query){
        ArrayList<Participants> filteredArrayList = new ArrayList<>();
        for(int i = 0;i<participantsArrayList.size();i++){
            Participants participants = participantsArrayList.get(i);
            if(participants.getUSER_ONE_ID() != user.getUSER_ID() && participants.getUSER_ONE_NAME().toLowerCase().contains(query.toLowerCase())){
                filteredArrayList.add(participants);
                participantsAdapter.notifyAdapter(filteredArrayList);
            }else if(participants.getUSER_TWO_ID() != user.getUSER_ID() && participants.getUSER_TWO_NAME().toLowerCase().contains(query.toLowerCase())){
                filteredArrayList.add(participants);
                participantsAdapter.notifyAdapter(filteredArrayList);
            }else {
                participantsAdapter.notifyAdapter(filteredArrayList);
            }
        }
    }

    @Override
    public void onGroupCreated(Groups group) {
       // RMsg.toastHere(context,group.getGROUP_NAME());
        getParticipants();
//        Intent groupIntent = new Intent(context, GroupChatActivity.class);
//        groupIntent.putExtra("group_id",Integer.toString(group.getGROUP_ID()));
//        context.startActivity(groupIntent);
    }

    @Override
    public void onGroupNotCreated() {

    }
}
