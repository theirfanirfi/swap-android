package swap.irfanullah.com.swap.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import swap.irfanullah.com.swap.Models.PhoneContact;
import swap.irfanullah.com.swap.R;

public class InvitePhoneContactAdapter extends RecyclerView.Adapter<InvitePhoneContactAdapter.ContactHolder> {
    private Context context;
    private ArrayList<PhoneContact> contacts;
    private static InviteListener mInviteListener;

    public InvitePhoneContactAdapter(Context context, ArrayList<PhoneContact> contacts) {
        this.context = context;
        this.contacts = contacts;
    }

    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.invite_phone_contacts_row,viewGroup,false);
        return new ContactHolder(view,context,contacts);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactHolder contactHolder, int i) {
        contactHolder.name.setText(contacts.get(i).getCONTACT_NAME());
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public static class ContactHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;
        Button inviteBtn;
        //InviteListener mInviteListener
        public ContactHolder(@NonNull View itemView, final Context context, final ArrayList<PhoneContact> contacts) {
            super(itemView);
            name = itemView.findViewById(R.id.contactName);
            inviteBtn = itemView.findViewById(R.id.inviteBtn);
            inviteBtn.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mInviteListener.onInvite(v,getAdapterPosition());
        }
    }

    public void notifyAdapter(ArrayList<PhoneContact> contacts){
        this.contacts = contacts;
        notifyDataSetChanged();
    }

    public interface InviteListener{
        void onInvite(View v,int position);
    }

    public void setOnInviteClickListener(InviteListener inviteClickListener){
        InvitePhoneContactAdapter.mInviteListener = inviteClickListener;
    }
}
