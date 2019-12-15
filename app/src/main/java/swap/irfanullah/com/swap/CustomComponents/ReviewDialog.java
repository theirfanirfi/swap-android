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
import android.widget.RatingBar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import swap.irfanullah.com.swap.Libraries.RetroLib;
import swap.irfanullah.com.swap.Models.Comment;
import swap.irfanullah.com.swap.Models.RMsg;
import swap.irfanullah.com.swap.Models.SwapsTab;
import swap.irfanullah.com.swap.R;
import swap.irfanullah.com.swap.Storage.PrefStorage;

public class ReviewDialog extends AppCompatDialogFragment {
    EditText reviewTextBox;
    private Context context;
    Button revBtn;
    ImageView cancel;
    ProgressBar progressBar;
   String SWAP_ID;
    Bundle bundle;
    SwapReviewedCallBack swapReviewedCallBack;
    RatingBar reviewRatingBar;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.review_swap_dialog,null);
        context = getActivity();
        reviewTextBox = view.findViewById(R.id.review_text_box);
        revBtn = view.findViewById(R.id.revBtn);
        cancel = view.findViewById(R.id.btnCancel);
        progressBar = view.findViewById(R.id.postProgressBar);
        reviewRatingBar = view.findViewById(R.id.reviewRating);
        bundle = getArguments();

        SWAP_ID = bundle.getString("swap_id");
//        composeTextArea.setText(Integer.toString(STATUS_ID));
        revBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String reviewText = reviewTextBox.getText().toString();
                reviewSwap(reviewText,Float.toString(reviewRatingBar.getRating()));
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

    private void reviewSwap(String review,String rating)  {
        RetroLib.geApiService().reviewSwap(PrefStorage.getUser(context).getTOKEN(),SWAP_ID,review,rating).enqueue(new Callback<SwapsTab>() {
            @Override
            public void onResponse(Call<SwapsTab> call, Response<SwapsTab> response) {
                if(response.isSuccessful()){
                    SwapsTab swap = response.body();
                    if(swap.getIS_ERROR() || !swap.getIS_AUTHENTICATED()){
                        RMsg.toastHere(context,swap.getMessage());
                        progressBar.setVisibility(View.GONE);
                    }else if(swap.getIS_REVIEWED()){
                        RMsg.toastHere(context,swap.getMessage());
                        progressBar.setVisibility(View.GONE);
                        dismiss();
                        getDialog().dismiss();
                    }
                }else {
                    RMsg.logHere(response.raw().toString());
                    RMsg.toastHere(context,"Error: "+response.message());
                    progressBar.setVisibility(View.GONE);

                }
            }

            @Override
            public void onFailure(Call<SwapsTab> call, Throwable t) {
                RMsg.toastHere(context,t.getMessage());
                progressBar.setVisibility(View.GONE);

            }


        });

    }

    public interface SwapReviewedCallBack {
        void onReviewSuccess();
        void onReviewFailed();
    }

    public void setOnReviewCallBack(SwapReviewedCallBack sr){
        this.swapReviewedCallBack = sr;
    }


}
