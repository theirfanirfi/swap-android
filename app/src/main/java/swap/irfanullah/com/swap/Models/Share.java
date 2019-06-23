package swap.irfanullah.com.swap.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Share {

    @SerializedName("id")
    private int SHARE_ID;
    @SerializedName("status_id")
    private int STATUS_ID;
    @SerializedName("user_id")
    private int USER_ID;
    @SerializedName("created_at")
    private String CREATED_AT;
    @SerializedName("updated_at")
    private String UPDATED_AT;
    @SerializedName("isError")
    private boolean isError;
    @SerializedName("isAuthenticated")
    private boolean isAuthenticated;
    @SerializedName("isAction")
    private boolean isAction;
    @SerializedName("message")
    private String Message;
    @SerializedName("share")
    private Share Share;
    @SerializedName("shares")
    private ArrayList<Share> Shares;

    public int getSHARE_ID() {
        return SHARE_ID;
    }

    public int getSTATUS_ID() {
        return STATUS_ID;
    }

    public int getUSER_ID() {
        return USER_ID;
    }

    public String getCREATED_AT() {
        return CREATED_AT;
    }

    public String getUPDATED_AT() {
        return UPDATED_AT;
    }

    public boolean isError() {
        return isError;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public boolean isAction() {
        return isAction;
    }

    public String getMessage() {
        return Message;
    }

    public swap.irfanullah.com.swap.Models.Share getShare() {
        return Share;
    }

    public ArrayList<swap.irfanullah.com.swap.Models.Share> getShares() {
        return Shares;
    }
}
