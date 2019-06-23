package swap.irfanullah.com.swap.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Comment {
    @SerializedName("id")
    private int COMMENTID;
    @SerializedName("user_id")
    private int USER_ID;
    @SerializedName("status_id")
    private int STATUS_ID;
    @SerializedName("comment")
    private String COMMENT;
    @SerializedName("message")
    private String MESSAGE;
    @SerializedName("isAlreadyCommented")
    private boolean isAlreadyCommented;
    @SerializedName("isCommented")
    private boolean isCommented;
    @SerializedName("isError")
    private boolean isError;
    @SerializedName("isAuthenticated")
    private boolean isAuthenticated;
    @SerializedName("isEmpty")
    private boolean isEmpty;

    @SerializedName("created_at")
    private String TIME;

    @SerializedName("profile_image")
    private String PROFILE_IMAGE;

    @SerializedName("name")
    private String USERNAME;

    public String getUSERNAME() {
        return USERNAME;
    }

    @SerializedName("isFound")
    private boolean isFound;

    @SerializedName("comments")
    private ArrayList<Comment> COMMENTS;

    public String getPROFILE_IMAGE() {
        return PROFILE_IMAGE;
    }

    public boolean isFound() {
        return isFound;
    }

    public ArrayList<Comment> getCOMMENTS() {
        return COMMENTS;
    }

    public String getTIME() {
        return TIME;
    }

    public int getCOMMENTID() {
        return COMMENTID;
    }

    public int getUSER_ID() {
        return USER_ID;
    }

    public int getSTATUS_ID() {
        return STATUS_ID;
    }

    public String getCOMMENT() {
        return COMMENT;
    }

    public String getMESSAGE() {
        return MESSAGE;
    }

    public boolean isAlreadyCommented() {
        return isAlreadyCommented;
    }

    public boolean isCommented() {
        return isCommented;
    }

    public boolean isError() {
        return isError;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public boolean isEmpty() {
        return isEmpty;
    }
}
