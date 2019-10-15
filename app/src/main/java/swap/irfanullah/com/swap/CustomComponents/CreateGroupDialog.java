package swap.irfanullah.com.swap.CustomComponents;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import swap.irfanullah.com.swap.Libraries.RetroLib;
import swap.irfanullah.com.swap.Models.Comment;
import swap.irfanullah.com.swap.Models.Groups;
import swap.irfanullah.com.swap.Models.RMsg;
import swap.irfanullah.com.swap.R;
import swap.irfanullah.com.swap.Storage.PrefStorage;

public class CreateGroupDialog extends AppCompatDialogFragment {
    EditText groupDescription,groupName;
    private Context context;
    Button createGroupBtn;
    ImageView cancel;
    ProgressBar progressBar;
    int STATUS_ID;
    Bundle bundle;
    CreateGroupCallBack groupCallBack;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.create_group_layout,null);
        context = getActivity();
        groupDescription = view.findViewById(R.id.group_description);
        groupName = view.findViewById(R.id.group_name_textview);
        createGroupBtn = view.findViewById(R.id.createGroupBtn);
        cancel = view.findViewById(R.id.btnCancel);
        progressBar = view.findViewById(R.id.postProgressBar);
        bundle = getArguments();

        //STATUS_ID = Integer.parseInt(bundle.getString("status_id"));
//        composeTextArea.setText(Integer.toString(STATUS_ID));
        createGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String desc = groupDescription.getText().toString();
                String name = groupName.getText().toString();
                createGroupRequest(name,desc);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        builder.setView(view)
                .setCancelable(false);
               // .setTitle("Comment on Post");

        return builder.create();

    }

    private void createGroupRequest(String group_name,String group_description)  {
        RetroLib.geApiService().createGroup(PrefStorage.getUser(context).getTOKEN(),group_name,group_description).enqueue(new Callback<Groups>() {
            @Override
            public void onResponse(Call<Groups> call, Response<Groups> response) {
                if(response.isSuccessful()){
                    Groups grp = response.body();
                    if(grp.getIS_ERROR() || !grp.getIS_AUTHENTICATED()){
                        RMsg.toastHere(context,grp.getMESSAGE());

                    }else if(grp.isGroupCreated()){
                        RMsg.toastHere(context,grp.getMESSAGE());
                        groupCallBack.onGroupCreated(grp.getGROUP());
                        getDialog().dismiss();
                    }
//                    else if(comment.isCommented()){
//                     commentCallBack.onCommented();
//                        RMsg.toastHere(context,comment.getMESSAGE());
//                        getDialog().dismiss();
//                    }
                }else {
                    RMsg.logHere(response.raw().toString());
                    RMsg.toastHere(context,response.message());
                }
            }

            @Override
            public void onFailure(Call<Groups> call, Throwable t) {
                RMsg.toastHere(context,t.getMessage());
            }
        });

    }

    public interface CreateGroupCallBack {
        void onGroupCreated(Groups group);
        void onGroupNotCreated();
    }

    public void setOnGroupCreatedListener(CreateGroupCallBack createGroupCallBack){
        this.groupCallBack = createGroupCallBack;
    }


}
