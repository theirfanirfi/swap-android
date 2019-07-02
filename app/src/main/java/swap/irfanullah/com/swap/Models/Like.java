package swap.irfanullah.com.swap.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Like {

    @SerializedName("id")
    private int LIKE_ID;
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

    @SerializedName("isLiked")
    private boolean isLiked;

    @SerializedName("isUnliked")
    private boolean isUnliked;

    @SerializedName("StatusLikes")
    private int StatusLikes;

    public int getStatusLikes() {
        return StatusLikes;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public boolean isUnliked() {
        return isUnliked;
    }

    @SerializedName("message")
    private String Message;
    @SerializedName("like")
    private Like LIKE;
    @SerializedName("likes")
    private ArrayList<Like> LIKES;

    public int getLIKE_ID() {
        return LIKE_ID;
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

    public String getMessage() {
        return Message;
    }

    public Like getLIKE() {
        return LIKE;
    }

    public ArrayList<Like> getLIKES() {
        return LIKES;
    }

    public boolean isAction() {
        return isAction;
    }
}
