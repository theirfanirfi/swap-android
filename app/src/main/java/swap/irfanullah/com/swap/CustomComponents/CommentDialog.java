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
import swap.irfanullah.com.swap.Models.RMsg;
import swap.irfanullah.com.swap.R;
import swap.irfanullah.com.swap.Storage.PrefStorage;

public class CommentDialog extends AppCompatDialogFragment {
    EditText composeTextArea;
    private Context context;
    Button post;
    ImageView cancel;
    ProgressBar progressBar;
    int STATUS_ID;
    Bundle bundle;
    CommentCallBack commentCallBack;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.compose_status,null);
        context = getActivity();
        composeTextArea = view.findViewById(R.id.group_description);
        post = view.findViewById(R.id.createGroupBtn);
        cancel = view.findViewById(R.id.btnCancel);
        progressBar = view.findViewById(R.id.postProgressBar);
        bundle = getArguments();

        STATUS_ID = Integer.parseInt(bundle.getString("status_id"));
//        composeTextArea.setText(Integer.toString(STATUS_ID));
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String status = composeTextArea.getText().toString();
                commentRequest(status);
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

    private void commentRequest(String status)  {
        RetroLib.geApiService().commentOnStatus(PrefStorage.getUser(context).getTOKEN(),Integer.toString(STATUS_ID),status).enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                if(response.isSuccessful()){
                    Comment comment = response.body();
                    if(comment.isError() || !comment.isAuthenticated()){
                        RMsg.toastHere(context,comment.getMESSAGE());
                    }else if(comment.isAlreadyCommented()){
                        RMsg.toastHere(context,comment.getMESSAGE());
                    }else if(comment.isCommented()){
                     commentCallBack.onCommented();
                        RMsg.toastHere(context,comment.getMESSAGE());
                        getDialog().dismiss();
                    }
                }else {
                    RMsg.logHere(response.raw().toString());
                    RMsg.toastHere(context,response.message());
                }
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                RMsg.toastHere(context,t.getMessage());
            }
        });

    }

    public interface CommentCallBack {
        void onCommented();
        void onCommentFail();
    }

    public void setOnCommentClickListener(CommentCallBack commentCallBack){
        this.commentCallBack = commentCallBack;
    }


}
