package swap.irfanullah.com.swap.Adapters.Profile;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import swap.irfanullah.com.swap.Libraries.GLib;
import swap.irfanullah.com.swap.Libraries.RetroLib;
import swap.irfanullah.com.swap.Libraries.TimeDiff;
import swap.irfanullah.com.swap.Models.RMsg;
import swap.irfanullah.com.swap.Models.Status;
import swap.irfanullah.com.swap.Models.SwapsTab;
import swap.irfanullah.com.swap.Models.User;
import swap.irfanullah.com.swap.NLUserProfile;
import swap.irfanullah.com.swap.R;
import swap.irfanullah.com.swap.StatusActivity;
import swap.irfanullah.com.swap.Storage.PrefStorage;
import swap.irfanullah.com.swap.UserProfile;

public class SwapsAdapter extends RecyclerView.Adapter<SwapsAdapter.StatusViewHolder> {
    private Context context, ctx;
    private ArrayList<SwapsTab> swapsTabArrayList;
    private final static String REPRESENTING_LOGGED_USER_IN_TAB = "You";

    public SwapsAdapter(Context context, ArrayList<SwapsTab> swapsTabArrayList) {
        this.context = context;
        this.swapsTabArrayList = swapsTabArrayList;
    }

    @NonNull
    @Override
    public StatusViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.swap_custom_row, viewGroup, false);


        return new StatusViewHolder(view, this.context, this.swapsTabArrayList);
    }

    @Override
    public void onBindViewHolder(@NonNull StatusViewHolder statusViewHolder, int i) {
        SwapsTab swap = this.swapsTabArrayList.get(i);
            statusViewHolder.username.setText(swap.getPOSTER_FULLNAME());
            statusViewHolder.withTextV.setText("\n"+swap.getSWAPED_WITH_FULLNAME());

            if(swap.getPOSTER_PROFILE_IMAGE() == null){
                statusViewHolder.profile_image.setImageResource(R.drawable.ic_person);
            }else {
                GLib.downloadImage(context,swap.getPOSTER_PROFILE_IMAGE()).into(statusViewHolder.profile_image);
            }

        statusViewHolder.statusDescription.setText(swap.getSTATUS());
        statusViewHolder.ratingBar.setRating(swap.getAVG_RATTING());
        statusViewHolder.swapTime.setText(TimeDiff.getTimeDifference(swap.getSWAP_DATE()));
    }


    @Override
    public int getItemCount() {
        return this.swapsTabArrayList.size();
    }


    public static class StatusViewHolder extends RecyclerView.ViewHolder {
        ImageView profile_image, unswap;
        TextView username, statusDescription, withTextV, swapTime;
        ConstraintLayout layout;
        RatingBar ratingBar;


        public StatusViewHolder(@NonNull View itemView, final Context context, final ArrayList<SwapsTab> swapsTabs) {
            super(itemView);

            layout = itemView.findViewById(R.id.statusLayout);
            profile_image = itemView.findViewById(R.id.profile_image);
            username = itemView.findViewById(R.id.usernameTextView);
            withTextV = itemView.findViewById(R.id.withTextView);
            statusDescription = itemView.findViewById(R.id.statusTextView);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            swapTime = itemView.findViewById(R.id.statusTimeTextView);
            layout = itemView.findViewById(R.id.statusLayout);
            //unswap = itemView.findViewById(R.id.cancelViewImgBtn);

                gotoProfile(context,swapsTabs);
            //rate the status

            ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBarr, float rating, boolean fromUser) {
                    if (fromUser) {
                        int position = getAdapterPosition();
                        SwapsTab swap = swapsTabs.get(position);


                        RetroLib.geApiService().rateStatus(PrefStorage.getUser(context).getTOKEN(), swap.getSTATUS_ID(), rating).enqueue(new Callback<Status>() {
                            @Override
                            public void onResponse(Call<Status> call, Response<Status> response) {

                                if (response.isSuccessful()) {

                                    Status status = response.body();
                                    if (status.getAuthenticated()) {
                                        if (status.getIS_EMPTY()) {
                                            Toast.makeText(context, status.getMESSAGE(), Toast.LENGTH_LONG).show();
                                        } else if (status.getIS_RATED()) {
                                            Toast.makeText(context, status.getMESSAGE(), Toast.LENGTH_LONG).show();
                                        } else if (status.getIS_ALREADY_RATED()) {
                                            Toast.makeText(context, status.getMESSAGE(), Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(context, status.getMESSAGE(), Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        Toast.makeText(context, RMsg.AUTH_ERROR_MESSAGE, Toast.LENGTH_LONG).show();
                                    }

                                } else {
                                    Toast.makeText(context, RMsg.REQ_ERROR_MESSAGE, Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Status> call, Throwable t) {
                                Toast.makeText(context, t.toString(), Toast.LENGTH_LONG).show();

                            }
                        });

                    }
                }
            });

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    SwapsTab swap = swapsTabs.get(position);
                    int status_id = swap.getSTATUS_ID();
                    int is_accepted = swap.getIS_ACCEPTED();
                    int swap_id = swap.getSWAP_ID();
                    Intent singleStatusAct = new Intent(context,StatusActivity.class);
                    singleStatusAct.putExtra("status_id",status_id);
                    singleStatusAct.putExtra("position",position);
                    singleStatusAct.putExtra("is_accepted",is_accepted);
                    singleStatusAct.putExtra("swap_id",swap_id);
                    context.startActivity(singleStatusAct);
                    Log.i(RMsg.LOG_MESSAGE,Integer.toString(swap_id)+" : "+Integer.toString(swap.getIS_ACCEPTED()));
                }
            });

        }

        private void gotoProfile(final Context context, final ArrayList<SwapsTab> swapsTabs) {

            profile_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    final SwapsTab swap = swapsTabs.get(position);
                    Log.i(RMsg.LOG_MESSAGE,Integer.toString(swap.getPOSTER_USER_ID()));
                    if(PrefStorage.isMe(context,swap.getPOSTER_USER_ID())){
                        Intent profileAct = new Intent(context,UserProfile.class);
                        profileAct.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        context.startActivity(profileAct);
                    }else {
                        Intent profileAct = new Intent(context,NLUserProfile.class);
                        profileAct.putExtra("user_id",swap.getPOSTER_USER_ID());
                        profileAct.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        context.startActivity(profileAct);
                    }
                }
            });
        }
    }

    public void notifySwapsAdapter(ArrayList<SwapsTab> swapsTabs) {
        this.swapsTabArrayList = swapsTabs;
        notifyDataSetChanged();
    }

    public void notifyRemovSwapsAdapter() {
        notifyDataSetChanged();
    }


}
