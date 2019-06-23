package swap.irfanullah.com.swap.CustomComponents;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import swap.irfanullah.com.swap.Libraries.RetroLib;
import swap.irfanullah.com.swap.Models.RMsg;
import swap.irfanullah.com.swap.Models.User;
import swap.irfanullah.com.swap.R;
import swap.irfanullah.com.swap.Storage.PrefStorage;
import swap.irfanullah.com.swap.UserProfile;

public class PDialog extends AppCompatDialogFragment {
    BootstrapEditText profile_description;
    BootstrapButton updateBtn,cancelBtn;
    ProgressBar progressBar;
    public ResultLister mListner;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //return super.onCreateDialog(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.compose_profile_description,null);
        profile_description = view.findViewById(R.id.composeProfileDescTextArea);
        updateBtn = view.findViewById(R.id.updateDescription);
        progressBar = view.findViewById(R.id.updateProgressBar);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                makeRequest();
            }
        });

        cancelBtn = view.findViewById(R.id.btnCancel);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        User user = PrefStorage.getUser(getActivity());

        if(user.getPROFILE_DESCRIPTION() != null){
            profile_description.setText(user.getPROFILE_DESCRIPTION());
        }

        return builder.setView(view)
                .setCancelable(false)
                .create();
    }

    public void makeRequest(){
        String description = profile_description.getText().toString();
        RetroLib.geApiService().updateProfileDescription(PrefStorage.getUser(getActivity()).getTOKEN(),description).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()) {
                    User user = response.body();
                    if(user.getIS_AUTHENTICATED()){
                        if(user.getIS_ERROR()){
                            Toast.makeText(getContext(),user.getMESSAGE(),Toast.LENGTH_LONG).show();
                        }else if(user.getIS_UPDATED()){
                            mListner.onUpdate(true);
                            User updatedUser = user.getUSER();
                            Gson gson = new Gson();
                            String newUser = gson.toJson(updatedUser);
                            PrefStorage.getEditor(getContext()).putString(PrefStorage.USER_PREF_DETAILS,newUser).commit();
                            RMsg.logHere(PrefStorage.getUser(getContext()).getPROFILE_DESCRIPTION());
                            Toast.makeText(getContext(),user.getMESSAGE(),Toast.LENGTH_LONG).show();
                            getDialog().dismiss();
                        } else {
                            Toast.makeText(getContext(),user.getMESSAGE(),Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Toast.makeText(getContext(),RMsg.AUTH_ERROR_MESSAGE,Toast.LENGTH_LONG).show();

                    }
                }else {
                    Toast.makeText(getContext(),RMsg.REQ_ERROR_MESSAGE,Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getContext(),t.toString(),Toast.LENGTH_LONG).show();

            }
        });

        progressBar.setVisibility(View.GONE);

    }

    public interface ResultLister{
        public void onUpdate(Boolean isUpdated);
        public void onFailure(Boolean isUpdate);
    }

}
