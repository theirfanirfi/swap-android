package swap.irfanullah.com.swap.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Groups {

    @SerializedName("isAuthenticated")
    private Boolean IS_AUTHENTICATED;
    @SerializedName("isError")
    private Boolean IS_ERROR;
    @SerializedName("isEmpty")
    private Boolean IS_EMPTY;
    @SerializedName("isFound")
    private Boolean IS_FOUND;
    @SerializedName("message")
    private String MESSAGE;
    @SerializedName("sender_id")
    private int SENDER_ID;
    @SerializedName("group_id")
    private int GROUP_ID;

    @SerializedName("group_name")
    private String GROUP_NAME;

    @SerializedName("created_at")
    private String CREATED_AT;
    @SerializedName("updated_at")
    private String UPDATED_AT;
    @SerializedName("response_message")
    private String RESPONSE_MESSAGE;

    @SerializedName("isCreated")
    private boolean isGroupCreated;

    @SerializedName("group")
    private Groups GROUP;

    @SerializedName("username")
    private String USERNAME;

    @SerializedName("isInvited")
    private boolean isInvited;

    @SerializedName("messages")
    private ArrayList<Groups> MESSENGER;

    @SerializedName("isSent")
    private Boolean IS_SENT;
    @SerializedName("last_message")
    private Groups LAST_MESSAGE;
    @SerializedName("count_unread_messages")
    private int COUNT_READ_MESSAGES;
    @SerializedName("last_message_count")
    private int LAST_MESSAGE_COUNT;

    public int getLAST_MESSAGE_COUNT() {
        return LAST_MESSAGE_COUNT;
    }

    public Groups getLAST_MESSAGE() {
        return LAST_MESSAGE;
    }

    public int getCOUNT_READ_MESSAGES() {
        return COUNT_READ_MESSAGES;
    }

    public Boolean getIS_SENT() {
        return IS_SENT;
    }

    public Boolean getIS_AUTHENTICATED() {
        return IS_AUTHENTICATED;
    }

    public Boolean getIS_ERROR() {
        return IS_ERROR;
    }

    public Boolean getIS_EMPTY() {
        return IS_EMPTY;
    }

    public Boolean getIS_FOUND() {
        return IS_FOUND;
    }

    public String getMESSAGE() {
        return MESSAGE;
    }

    public int getSENDER_ID() {
        return SENDER_ID;
    }


    public String getCREATED_AT() {
        return CREATED_AT;
    }

    public String getUPDATED_AT() {
        return UPDATED_AT;
    }

    public String getRESPONSE_MESSAGE() {
        return RESPONSE_MESSAGE;
    }

    public ArrayList<Groups> getMESSENGER() {
        return MESSENGER;
    }

    public int getGROUP_ID() {
        return GROUP_ID;
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public boolean isGroupCreated() {
        return isGroupCreated;
    }

    public Groups getGROUP() {
        return GROUP;
    }

    public String getGROUP_NAME() {
        return GROUP_NAME;
    }

    public boolean isInvited() {
        return isInvited;
    }
}

