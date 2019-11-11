package swap.irfanullah.com.swap.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SingleStatusModel {

    @SerializedName("isAuthenticated")
    private Boolean IS_AUTHENTICATED;
    @SerializedName("isError")
    private Boolean IS_ERROR;
    @SerializedName("isFound")
    private Boolean IS_FOUND;
    @SerializedName("isEmpty")
    private Boolean IS_EMPTY;
    @SerializedName("message")
    private String MESSAGE;
    @SerializedName("raters")
    private ArrayList<User> RATERS;
    @SerializedName("status")
    private Status STATUS;
    @SerializedName("swaps_count")
    private int SWAPS_COUNT;
    @SerializedName("average_rating")
    private float AVERAGE_RATING;
    @SerializedName("is_accepted")
    private int IS_ACCEPTED;
    @SerializedName("is_rejected")
    private int IS_REJECTED;

    @SerializedName("likes_count")
    private String LIKESCOUNT;

    @SerializedName("shares_count")
    private String SHARESCOUNT;

    @SerializedName("comments_count")
    private String COMMENTSCOUONT;

    @SerializedName("isLiked")
    private int isLiked;

    public String getLIKESCOUNT() {
        return LIKESCOUNT;
    }

    public String getSHARESCOUNT() {
        return SHARESCOUNT;
    }

    public String getCOMMENTSCOUONT() {
        return COMMENTSCOUONT;
    }

    public int isLiked() {
        return isLiked;
    }

    public int getIS_ACCEPTED() {
        return IS_ACCEPTED;
    }

    public int getIS_REJECTED() {
        return IS_REJECTED;
    }

    public Boolean getIS_FOUND() {
        return IS_FOUND;
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

    public String getMESSAGE() {
        return MESSAGE;
    }

    public ArrayList<User> getRATERS() {
        return RATERS;
    }

    public Status getSTATUS() {
        return STATUS;
    }

    public int getSWAPS_COUNT() {
        return SWAPS_COUNT;
    }

    public float getAVERAGE_RATING() {
        return AVERAGE_RATING;
    }
}
