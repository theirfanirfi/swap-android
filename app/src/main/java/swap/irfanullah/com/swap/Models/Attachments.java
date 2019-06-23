package swap.irfanullah.com.swap.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Attachments {
    @SerializedName("user_id")
    private int USER_ID;
    @SerializedName("attachment_id")
    private int ATTACHMENT_ID;
    @SerializedName("status_id")
    private int STATUS_ID;
    @SerializedName("does_belong")
    private Boolean DOES_BELONG;
    @SerializedName("isStatusFound")
    private Boolean isStatusFound;
    @SerializedName("isError")
    private Boolean IS_ERROR;
    @SerializedName("isAuthenticated")
    private Boolean IS_AUTHENTICATED;
    @SerializedName("isUploaded")
    private Boolean IS_UPLOADED;
    @SerializedName("attachment_type")
    private int ATTACHMENT_TYPE;
    @SerializedName("message")
    private String MESSAGE;
    @SerializedName("isSaved")
    private Boolean IS_SAVED;
    @SerializedName("attachments")
    private ArrayList<Attachments> attachmentsArrayList;
    @SerializedName("isFound")
    private Boolean isFound;
    @SerializedName("attachment_url")
    private String ATTACHMENT_URL;

    public String getATTACHMENT_URL() {
        return ATTACHMENT_URL;
    }

    public Boolean getFound() {
        return isFound;
    }

    public ArrayList<Attachments> getAttachmentsArrayList() {
        return attachmentsArrayList;
    }

    public Boolean getIS_SAVED() {
        return IS_SAVED;
    }

    public String getMESSAGE() {
        return MESSAGE;
    }

    public int getUSER_ID() {
        return USER_ID;
    }

    public int getATTACHMENT_ID() {
        return ATTACHMENT_ID;
    }

    public int getSTATUS_ID() {
        return STATUS_ID;
    }

    public Boolean getDOES_BELONG() {
        return DOES_BELONG;
    }

    public Boolean getStatusFound() {
        return isStatusFound;
    }

    public Boolean getIS_ERROR() {
        return IS_ERROR;
    }

    public Boolean getIS_AUTHENTICATED() {
        return IS_AUTHENTICATED;
    }

    public Boolean getIS_UPLOADED() {
        return IS_UPLOADED;
    }

    public int getATTACHMENT_TYPE() {
        return ATTACHMENT_TYPE;
    }
}
