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
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import swap.irfanullah.com.swap.Libraries.RetroLib;
import swap.irfanullah.com.swap.Models.Status;
import swap.irfanullah.com.swap.R;
import swap.irfanullah.com.swap.Storage.PrefStorage;

public class ComposeStatusDialog extends AppCompatDialogFragment {
    EditText composeTextArea;
    private Context context;
    Button post;
    ImageView cancel;
    ProgressBar progressBar;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.compose_status,null);
        composeTextArea = view.findViewById(R.id.composeProfileDescTextArea);
        post = view.findViewById(R.id.updateDescription);
        cancel = view.findViewById(R.id.btnCancel);
        progressBar = view.findViewById(R.id.postProgressBar);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String status = composeTextArea.getText().toString();
                    statusComposeRequest(status);
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
                //.setTitle("Compose New Status");

        return builder.create();

    }

    private void statusComposeRequest(String status)  {
        RetroLib.geApiService().composeStatus(PrefStorage.getUser(getContext()).getTOKEN(),status).enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if(response.isSuccessful())
                {
                    if(response.body().getAuthenticated()) {
                        if (response.body().getPosted()) {
                            progressBar.setVisibility(View.GONE);

                            Toast.makeText(getContext(), response.body().getMESSAGE(), Toast.LENGTH_LONG).show();
                            getDialog().dismiss();
                        } else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getContext(), response.body().getMESSAGE(), Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), response.body().getMESSAGE(), Toast.LENGTH_LONG).show();
                        getDialog().dismiss();
                    }
                }
                else
                {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(),response.body().getMESSAGE(),Toast.LENGTH_LONG).show();
                    getDialog().dismiss();
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Toast.makeText(getContext(),t.toString(),Toast.LENGTH_LONG).show();
                getDialog().dismiss();
            }
        });
    }


}
