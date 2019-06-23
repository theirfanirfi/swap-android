package swap.irfanullah.com.swap.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import swap.irfanullah.com.swap.ImageViewer;
import swap.irfanullah.com.swap.Libraries.GLib;
import swap.irfanullah.com.swap.Models.Attachments;
import swap.irfanullah.com.swap.Models.RMsg;
import swap.irfanullah.com.swap.R;

public class StatusFragGridAdapter extends RecyclerView.Adapter<StatusFragGridAdapter.MediaViewHolder>{

    private Context context;
    private ArrayList<Attachments> attachmentsArrayList;
    private int STATUS_ID = 0;

    public StatusFragGridAdapter(Context context, ArrayList<Attachments> attachmentsArrayList, int status_id) {
        this.context = context;
        this.attachmentsArrayList = attachmentsArrayList;
        this.STATUS_ID = status_id;
    }

    @NonNull
    @Override
    public MediaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.status_frag_grid_custom_row,viewGroup,false);
        return new MediaViewHolder(view,context,attachmentsArrayList, STATUS_ID);
    }

    @Override
    public void onBindViewHolder(@NonNull MediaViewHolder mediaViewHolder, int i) {
        Attachments att = attachmentsArrayList.get(i);
        GLib.downloadImage(context,att.getATTACHMENT_URL()).into(mediaViewHolder.iv);
    }

    @Override
    public int getItemCount() {
        return this.attachmentsArrayList.size();
    }

    public static class MediaViewHolder extends RecyclerView.ViewHolder {
        private Context context;
        private ArrayList<Attachments> attachments;
        ImageView iv;
        public MediaViewHolder(@NonNull View itemView, final Context context, final ArrayList<Attachments> attachments, final int status_id) {
            super(itemView);
            this.context = context;
            this.attachments = attachments;
            iv = itemView.findViewById(R.id.customSquareImage);
            iv.setMinimumWidth(250);
            iv.setMinimumHeight(250);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent pager = new Intent(context,ImageViewer.class);
                    pager.putExtra("status_id",status_id);
                    context.startActivity(pager);
                }
            });
        }

    }

        public void notifyAdapter(ArrayList<Attachments> at){
        this.attachmentsArrayList = at;
        notifyDataSetChanged();
        //RMsg.logHere("notified");
    }
}
