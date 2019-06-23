package swap.irfanullah.com.swap.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import swap.irfanullah.com.swap.Libraries.GLib;
import swap.irfanullah.com.swap.Models.Notification;
import swap.irfanullah.com.swap.R;

public class SwapRequestAdapter extends RecyclerView.Adapter<SwapRequestAdapter.SwapRequestViewHolder> {
    private Context context;
    public static RequestListener requestListener;
    private ArrayList<Notification> swapRequestsList;

    public SwapRequestAdapter(Context context, ArrayList<Notification> swapRequestsList) {
        this.context = context;
        this.swapRequestsList = swapRequestsList;
    }

    @NonNull
    @Override
    public SwapRequestViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.swap_request_custom_row,viewGroup,false);
        return new SwapRequestViewHolder(view,context, this.swapRequestsList);
    }

    @Override
    public void onBindViewHolder(@NonNull SwapRequestViewHolder swapRequestViewHolder, int i) {
        Notification swap = this.swapRequestsList.get(i);
        swapRequestViewHolder.username.setText(swap.getFULL_NAME());
        if(swap.getPROFIE_IMAGE() == null){
            swapRequestViewHolder.profile_image.setImageResource(R.drawable.ic_person);
        }else {
            GLib.downloadImage(context,swap.getPROFIE_IMAGE()).into(swapRequestViewHolder.profile_image);
        }
    }

    @Override
    public int getItemCount() {
        return this.swapRequestsList.size();
    }

    public static class SwapRequestViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        AppCompatTextView username;
        AppCompatButton approve, decline;
        ImageView profile_image;
        private ArrayList<Notification> swapRequestsList;
        public SwapRequestViewHolder(@NonNull View itemView, Context context, ArrayList<Notification> swapRequestsList) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            approve = itemView.findViewById(R.id.approveBtn);
            decline = itemView.findViewById(R.id.declineBtn);
            profile_image = itemView.findViewById(R.id.profile_image);
            this.swapRequestsList = swapRequestsList;

            approve.setOnClickListener(this);
            decline.setOnClickListener(this);
            username.setOnClickListener(this);
            profile_image.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            int position = getAdapterPosition();
            Notification notification = this.swapRequestsList.get(position);
            switch (id){
                case R.id.approveBtn:
                    requestListener.onApprove(v,position, notification);
                    break;
                case R.id.declineBtn:
                    requestListener.onDecline(v,position, notification);
                    break;
                case R.id.username:
                    requestListener.onStatus(v,position, notification);
                    break;
                case R.id.profile_image:
                    requestListener.onProfile(v,position, notification);
                    break;
            }
        }
    }


    public void setRequestListener(RequestListener mRequestListener){
        requestListener = mRequestListener;
    }
    public interface RequestListener {
        void onApprove(View view, int position, Notification notification);
        void onDecline(View view, int position, Notification notification);
        void onProfile(View view, int position, Notification notification);
        void onStatus(View view, int position, Notification notification);
    }

    public void notifyAdapter(ArrayList<Notification> swapRequestsList){
        this.swapRequestsList = swapRequestsList;
        notifyDataSetChanged();
    }
}
