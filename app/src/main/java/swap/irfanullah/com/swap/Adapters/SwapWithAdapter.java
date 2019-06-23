package swap.irfanullah.com.swap.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import swap.irfanullah.com.swap.Libraries.GLib;
import swap.irfanullah.com.swap.Libraries.RetroLib;
import swap.irfanullah.com.swap.Models.Followers;
import swap.irfanullah.com.swap.Models.RMsg;
import swap.irfanullah.com.swap.Models.Status;
import swap.irfanullah.com.swap.Models.Swap;
import swap.irfanullah.com.swap.NLUserProfile;
import swap.irfanullah.com.swap.R;
import swap.irfanullah.com.swap.Storage.PrefStorage;
import swap.irfanullah.com.swap.UserProfile;

public class SwapWithAdapter extends RecyclerView.Adapter<SwapWithAdapter.StatusViewHolder> {
    private Context context;
    private ArrayList<Followers> followers;
    private ArrayList<Swap> swaps;
    private int status_id;

    public SwapWithAdapter(Context context, ArrayList<Followers> followers, ArrayList<Swap> swaps, int status_id) {
        this.context = context;
        this.followers = followers;
        this.swaps = swaps;
        this.status_id = status_id;
    }

    @NonNull
    @Override
    public StatusViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.swap_with_custom_row,viewGroup,false);


        return new StatusViewHolder(view, this.context, this.followers,this.swaps,status_id);
    }

    @Override
    public void onBindViewHolder(@NonNull StatusViewHolder statusViewHolder, int i) {

        Followers follower = this.followers.get(i);
       statusViewHolder.name.setText(follower.getFULLNAME());
       if(follower.getPROFILE_IMAGE() == null){
           statusViewHolder.profile_image.setImageResource(R.drawable.ic_person);
       }else {
           GLib.downloadImage(context,follower.getPROFILE_IMAGE()).into(statusViewHolder.profile_image);
       }

       for(Swap item : this.swaps){
           if(item.getSWAPED_WITH_USER_ID() == follower.getFOLLOWED_USER_ID()){
               //this.swaps.get(i).getSWAP_ID() > 0 ? true : false
               statusViewHolder.swapedWith.setChecked(true);
               break;
           }
       }
    }



    @Override
    public int getItemCount() {
        return this.followers.size();
    }


    public static class StatusViewHolder extends RecyclerView.ViewHolder {
        CheckBox swapedWith;
        TextView name;
        ImageView profile_image;
        public StatusViewHolder(@NonNull View itemView, final Context context, final ArrayList<Followers> followers, ArrayList<Swap> swaps, final int status_id) {
            super(itemView);
            swapedWith = itemView.findViewById(R.id.swapedWithcheckBox);
            name = itemView.findViewById(R.id.statusTextView);
            profile_image = itemView.findViewById(R.id.profile_image);

            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Followers f = followers.get(position);
                    //Log.i(RMsg.LOG_MESSAGE,Integer.toString(f.getFOLLOWED_USER_ID()));
                    if(PrefStorage.isMe(context,f.getFOLLOWED_USER_ID())){
                        Intent profileAct = new Intent(context,UserProfile.class);
                        profileAct.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(profileAct);
                    }else {
                        Intent profileAct = new Intent(context,NLUserProfile.class);
                        profileAct.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        profileAct.putExtra("user_id",f.getFOLLOWED_USER_ID());
                        context.startActivity(profileAct);
                    }
                }
            });

            profile_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Followers f = followers.get(position);
                    //Log.i(RMsg.LOG_MESSAGE,Integer.toString(f.getFOLLOWED_USER_ID()));
                    if(PrefStorage.isMe(context,f.getFOLLOWED_USER_ID())){
                        Intent profileAct = new Intent(context,UserProfile.class);
                        profileAct.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(profileAct);
                    }else {
                        Intent profileAct = new Intent(context,NLUserProfile.class);
                        profileAct.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        profileAct.putExtra("user_id",f.getFOLLOWED_USER_ID());
                        context.startActivity(profileAct);
                    }
                }
            });




            swapedWith.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(final CompoundButton buttonView, boolean isChecked) {

                    if (buttonView.isPressed()) {

                        int position = getAdapterPosition();
                        Followers f = followers.get(position);
                        final int user_id = f.getFOLLOWED_USER_ID();

                        if (isChecked) {


                            new AsyncTask<Void,Void,Void>(){
                                @Override
                                protected void onPreExecute() {
                                    super.onPreExecute();
                                    //Toast.makeText(context,"loading...",Toast.LENGTH_LONG).show();
                                }

                                @Override
                                protected void onPostExecute(Void aVoid) {
                                    super.onPostExecute(aVoid);
                                   // Toast.makeText(context,"Done.",Toast.LENGTH_LONG).show();

                                }

                                @Override
                                protected Void doInBackground(Void... voids) {
                                    RetroLib.geApiService().swapStatus(PrefStorage.getUser(context).getTOKEN(), user_id, status_id).enqueue(new Callback<Swap>() {
                                        @Override
                                        public void onResponse(Call<Swap> call, Response<Swap> response) {
                                            Swap swap = response.body();
                                            if (swap.getAuthenticated()) {
                                                if (swap.getError()) {
                                                    Toast.makeText(context, swap.getMESSAGE(), Toast.LENGTH_LONG).show();
                                                    buttonView.setChecked(false);
                                                } else if (swap.getAlready()) {
                                                    Toast.makeText(context, swap.getMESSAGE(), Toast.LENGTH_LONG).show();
                                                } else if (swap.getSwaped()) {
                                                    Toast.makeText(context, swap.getMESSAGE(), Toast.LENGTH_LONG).show();
                                                } else if (swap.getEmpty()) {
                                                    Toast.makeText(context, swap.getMESSAGE(), Toast.LENGTH_LONG).show();
                                                    buttonView.setChecked(false);
                                                }
                                            } else {
                                                Toast.makeText(context, swap.getMESSAGE(), Toast.LENGTH_LONG).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Swap> call, Throwable t) {
                                            Toast.makeText(context, t.toString(), Toast.LENGTH_LONG).show();

                                        }
                                    });
                                    return  null;
                                }
                            }.execute();

                        } else if(!isChecked) {

                            new AsyncTask<Void,Void,Void>(){
                                @Override
                                protected void onPreExecute() {
                                    super.onPreExecute();
                                }

                                @Override
                                protected void onPostExecute(Void aVoid) {
                                    super.onPostExecute(aVoid);
                                }

                                @Override
                                protected Void doInBackground(Void... voids) {
                                    RetroLib.geApiService().unSwapStatus(PrefStorage.getUser(context).getTOKEN(),user_id,status_id).enqueue(new Callback<Swap>() {
                                        @Override
                                        public void onResponse(Call<Swap> call, Response<Swap> response) {
                                            Swap swap = response.body();
                                            if(swap.getAuthenticated()){
                                                if(swap.getError()){
                                                    Toast.makeText(context, swap.getMESSAGE(), Toast.LENGTH_LONG).show();
                                                }
                                                else if(swap.getIS_EXIST()){
                                                    Toast.makeText(context, swap.getMESSAGE(), Toast.LENGTH_LONG).show();

                                                }
                                                else if(swap.getDE_SWAPED()){
                                                    Toast.makeText(context, swap.getMESSAGE(), Toast.LENGTH_LONG).show();
                                                }
                                            }
                                            else
                                            {
                                                Toast.makeText(context, swap.getMESSAGE(), Toast.LENGTH_LONG).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Swap> call, Throwable t) {
                                            Toast.makeText(context, t.toString(), Toast.LENGTH_LONG).show();

                                        }
                                    });
                                    return null;
                                }
                            }.execute();

                        }
                    }
                }
            });
        }
    }


    public void FilterRV(ArrayList<Followers> followers)
    {
        this.followers = followers;
        notifyDataSetChanged();
    }
}
