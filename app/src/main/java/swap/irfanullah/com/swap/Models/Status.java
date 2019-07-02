package swap.irfanullah.com.swap.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Status{
    @SerializedName("username")
    private String USERNAME;
    @SerializedName("name")
    private String name;
    @SerializedName("user_id")
    private int USER_ID;
    @SerializedName("status_id")
    private int STATUS_ID;
    @SerializedName("status")
    private String STATUS;
    @SerializedName("created_at")
    private String TIME;
    @SerializedName("profile_image")
    private String PROFILE_IMAGE;
    @SerializedName("isPosted")
    private Boolean isPosted;
    @SerializedName("message")
    private String MESSAGE;
    @SerializedName("isAuthenticated")
    private Boolean isAuthenticated;
    @SerializedName("ratting")
    private float RATTING;
    @SerializedName("isFound")
    private Boolean isFound;
    @SerializedName("isDeleted")
    private Boolean IS_DELETED;
    @SerializedName("statuses")
    private ArrayList<Status> STATUSES;
    @SerializedName("isRated")
    private Boolean IS_RATED;
    @SerializedName("isAlreadyRated")
    private Boolean IS_ALREADY_RATED;
    @SerializedName("isEmpty")
    private Boolean IS_EMPTY;
    @SerializedName("isError")
    private Boolean IS_ERROR;
    @SerializedName("average_rating")
    private float AVERAGE_RATING;
    @SerializedName("is_accepted")
    private int IS_ACCEPTED;
    @SerializedName("is_rejected")
    private int IS_REJECTED;
    @SerializedName("obj_status")
    private Status OBJ_STATUS;
    @SerializedName("attachment_id")
    private int ATTACHMENT_ID;
    @SerializedName("attachment_url")
    private String ATTACHMENT_URL;
    @SerializedName("has_attachment")
    private int HAS_ATTACHMENTS;
    @SerializedName("attachments")
    private String ATTACHMENTS;
    @SerializedName("likes_count")
    private String LIKESCOUNT;

    @SerializedName("shares_count")
    private String SHARESCOUNT;

    @SerializedName("comments_count")
    private String COMMENTSCOUONT;

    @SerializedName("isLiked")
    private boolean isLiked;

    public boolean isLiked() {
        return isLiked;
    }

    public String getCOMMENTSCOUONT() {
        return COMMENTSCOUONT;
    }

    public String getSHARESCOUNT() {
        return SHARESCOUNT;
    }

    public String getLIKESCOUNT() {
        return LIKESCOUNT;
    }

    public Boolean getIS_ERROR() {
        return IS_ERROR;
    }

    public int getHAS_ATTACHMENTS() {
        return HAS_ATTACHMENTS;
    }

    public String getATTACHMENTS() {
        return ATTACHMENTS;
    }

    public Status getOBJ_STATUS() {
        return OBJ_STATUS;
    }

    public int getATTACHMENT_ID() {
        return ATTACHMENT_ID;
    }

    public String getATTACHMENT_URL() {
        return ATTACHMENT_URL;
    }

    public int getIS_ACCEPTED() {
        return IS_ACCEPTED;
    }

    public int getIS_REJECTED() {
        return IS_REJECTED;
    }

    public Boolean getIS_DELETED() {
        return IS_DELETED;
    }

    public float getAVERAGE_RATING() {
        return AVERAGE_RATING;
    }

    public String getName() {
        return name;
    }

    public Boolean getIS_EMPTY() {
        return IS_EMPTY;
    }

    public Boolean getIS_RATED() {
        return IS_RATED;
    }

    public Boolean getIS_ALREADY_RATED() {
        return IS_ALREADY_RATED;
    }


    public Boolean getFound() {
        return isFound;
    }

    public void setFound(Boolean found) {
        isFound = found;
    }

    public ArrayList<Status> getSTATUSES() {
        return STATUSES;
    }

    public void setSTATUSES(ArrayList<Status> STATUSES) {
        this.STATUSES = STATUSES;
    }

    public Status() {

    }

    public float getRATTING() {
        return RATTING;
    }

    public void setRATTING(float RATTING) {
        this.RATTING = RATTING;
    }

    public Boolean getAuthenticated() {
        return isAuthenticated;
    }

    public void setAuthenticated(Boolean authenticated) {
        isAuthenticated = authenticated;
    }

    public String getMESSAGE() {
        return MESSAGE;
    }

    public void setMESSAGE(String MESSAGE) {
        this.MESSAGE = MESSAGE;
    }

    public Boolean getPosted() {
        return isPosted;
    }

    public void setPosted(Boolean posted) {
        isPosted = posted;
    }

    public Status(String USERNAME, int USER_ID, int STATUS_ID, String STATUS, String TIME, String PROFILE_IMAGE) {
        this.USERNAME = USERNAME;
        this.USER_ID = USER_ID;
        this.STATUS_ID = STATUS_ID;
        this.STATUS = STATUS;
        this.TIME = TIME;
        this.PROFILE_IMAGE = PROFILE_IMAGE;
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    public int getUSER_ID() {
        return USER_ID;
    }

    public void setUSER_ID(int USER_ID) {
        this.USER_ID = USER_ID;
    }

    public int getSTATUS_ID() {
        return STATUS_ID;
    }

    public void setSTATUS_ID(int STATUS_ID) {
        this.STATUS_ID = STATUS_ID;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public String getTIME() {
        return TIME;
    }

    public void setTIME(String TIME) {
        this.TIME = TIME;
    }

    public String getPROFILE_IMAGE() {
        return PROFILE_IMAGE;
    }

    public void setPROFILE_IMAGE(String PROFILE_IMAGE) {
        this.PROFILE_IMAGE = PROFILE_IMAGE;
    }
}
