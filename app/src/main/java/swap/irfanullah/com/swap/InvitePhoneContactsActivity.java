package swap.irfanullah.com.swap;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import swap.irfanullah.com.swap.Adapters.InvitePhoneContactAdapter;
import swap.irfanullah.com.swap.Libraries.RetroLib;
import swap.irfanullah.com.swap.Models.PhoneContact;
import swap.irfanullah.com.swap.Models.RMsg;
import swap.irfanullah.com.swap.Models.User;
import swap.irfanullah.com.swap.Storage.PrefStorage;

public class InvitePhoneContactsActivity extends AppCompatActivity {

    private RecyclerView contactRV;
    private InvitePhoneContactAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<PhoneContact> contacts;
    private Context context;
    private SmsManager smsManager;
    private ContentResolver contentResolver;
    private Cursor contactsCursor;
    private int INVITES = 0;
    private int TOTAL_INVITES = 5;
    private User user;
    private ConstraintLayout layout;
    private Button invite;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        getSupportActionBar().setTitle("Invite atleast 5 of your contacts");
        getSupportActionBar().setSubtitle(Integer.toString(TOTAL_INVITES - INVITES) + " remaining");
        smsManager = SmsManager.getDefault();
        contentResolver = getContentResolver();
        setContentView(R.layout.activity_invite_phone_contacts);
        contactRV = findViewById(R.id.contactPhoneNumber);
        layout = findViewById(R.id.clayout);
        layoutManager = new LinearLayoutManager(context);
        contactRV.setLayoutManager(layoutManager);
        contacts = new ArrayList<>();
        adapter = new InvitePhoneContactAdapter(context, contacts);
        user = PrefStorage.getUser(context);

        adapter.setOnInviteClickListener(new InvitePhoneContactAdapter.InviteListener() {
            @Override
            public void onInvite(View v, int position) {
                invite = v.findViewById(R.id.inviteBtn);
                try {
                    smsManager.sendTextMessage(contacts.get(position).getCONTACT_NUMBER(), null, "I am going to invite you", null, null);
                    updateInvitesStatus(position);
                } catch (Exception e) {
                    Snackbar.make(getCurrentFocus(), e.getMessage(), Snackbar.LENGTH_LONG).show();
                }

            }
        });

        contactRV.setAdapter(adapter);
        fetchContacts();


    }

    private void fetchContacts() {
        contactsCursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        if (contactsCursor.getCount() > 0) {
            int i = 0;
            while (contactsCursor.moveToNext()) {
                String id = contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                int hasPhoneNumber = Integer.parseInt(contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                if (hasPhoneNumber > 0) {
                    Cursor phoneNumberCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                    phoneNumberCursor.moveToFirst();

                    String number = phoneNumberCursor.getString(phoneNumberCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    contacts.add(new PhoneContact(name, number));
                    phoneNumberCursor.close();
                }


            }

            RMsg.ilogHere(contacts.size());
            adapter.notifyAdapter(contacts);
            contactsCursor.close();
        } else if(user.getIsSocialMedia() == 0){
            Intent ShareApp = new Intent(context, ShareApp.class);
            startActivity(ShareApp);
        }
        else {
            Intent homeAct = new Intent(context, HomeActivity.class);
            startActivity(homeAct);
        }
    }

    private void updateInviteCount() {
        if (INVITES >= 0) {
            getSupportActionBar().setSubtitle(Integer.toString(TOTAL_INVITES - INVITES) + " remaining");
        } else if (INVITES < 0) {
            getSupportActionBar().setSubtitle(Integer.toString(TOTAL_INVITES - INVITES) + " remaining");
        }


        if (INVITES >= 5) {
            getSupportActionBar().setSubtitle(Integer.toString(INVITES) + " Invited");


            Snackbar snackbar = Snackbar.make(layout, Integer.toString(INVITES) + " Users Invited.", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("Next", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Toast.makeText(context,"working",Toast.LENGTH_LONG).show();

                }
            });
            snackbar.setActionTextColor(getResources().getColor(R.color.colorWhite));
            snackbar.show();
        }
    }

    private void updateInvitesStatus(final int position) {
        RetroLib.geApiService().usersInvited(user.getTOKEN()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User res = response.body();
                    if (res.getIS_ERROR()) {
                        RMsg.toastHere(context, res.getMESSAGE());
                    } else if (res.getIS_AUTHENTICATED()) {
                        if (res.getIS_UPDATED()) {
                            RMsg.logHere("updated");

                            invite.setText("Invited");
                            INVITES++;
                            contacts.remove(position);
                            adapter.notifyAdapter(contacts);
                            updateInviteCount();

                        } else {
                            RMsg.toastHere(context, res.getMESSAGE());
                        }
                    } else {
                        RMsg.toastHere(context, res.getMESSAGE());
                    }
                } else {
                    RMsg.toastHere(context, RMsg.REQ_ERROR_MESSAGE);
                    RMsg.logHere(response.raw().toString());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.next_menu_startup_activities, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.next) {
            // updateInvitesStatus();
            gotoNext();
        } else if (id == R.id.logout) {
            logout();
        } else if (id == R.id.skip) {
            Intent homeActivity = new Intent(this, HomeActivity.class);
            startActivity(homeActivity);
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        PrefStorage.getEditor(this).remove(PrefStorage.USER_PREF_DETAILS).commit();
        PrefStorage.getEditor(this).remove(PrefStorage.START_NEXT_ACTIVITY).commit();
        PrefStorage.getEditor(this).remove(PrefStorage.AFTER_STARTUP_ACTIVITY).commit();
        Intent loginAct = new Intent(this, LoginActivity.class);
        startActivity(loginAct);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //finish();
    }

    public void gotoNext() {

        RMsg.ilogHere(user.getIS_INVITED());
        RMsg.ilogHere(user.getFOLLOWED());
        RMsg.ilogHere(user.getIsSocialMedia());

        if (user.getIS_INVITED() == 1 && user.getFOLLOWED() >= 5 && user.getIsSocialMedia() == 0) {
            Intent shareApp = new Intent(context, ShareApp.class);
            startActivity(shareApp);
        }
        //goto next activity
        else if (PrefStorage.getAfterStartupActivity(context).equals(PrefStorage.INVITES_ACTIVITY) && PrefStorage.gotoNextActivity(context) && user.getFOLLOWED() < 5) {
            Intent followAct = new Intent(context, FollowUsersActivity.class);
            startActivity(followAct);
        } else {
            //go to home activity
            Intent homeAct = new Intent(context, HomeActivity.class);
            startActivity(homeAct);
        }
    }
}
