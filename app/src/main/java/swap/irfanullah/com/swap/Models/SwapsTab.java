package swap.irfanullah.com.swap.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SwapsTab {

    @SerializedName("swap_id")
    private int SWAP_ID;
    @SerializedName("poster_user_id")
    private int POSTER_USER_ID;
    @SerializedName("swaped_with_user_id")
    private int SWAPED_WITH_USER_ID;
    @SerializedName("status_id")
    private int STATUS_ID;
    @SerializedName("avg_ratting")
    private float AVG_RATTING;
    @SerializedName("poster_user_name")
    private String POSTER_FULLNAME;
    @SerializedName("swaped_with_user_name")
    private String SWAPED_WITH_FULLNAME;
    @SerializedName("status")
    private String STATUS;
    @SerializedName("swap_date")
    private String SWAP_DATE;
    @SerializedName("poster_profile_image")
    private String POSTER_PROFILE_IMAGE;
    @SerializedName("swaped_with_profile_image")
    private String SWAPED_WITH_PROFILE_IMAGE;
    @SerializedName("isAuthenticated")
    private Boolean IS_AUTHENTICATED;
    @SerializedName("isError")
    private Boolean IS_ERROR;
    @SerializedName("isFound")
    private Boolean IS_FOUND;
    @SerializedName("isMe")
    private Boolean IS_ME;
    @SerializedName("swaps")
    private ArrayList<SwapsTab> swapsTabArrayList;
    @SerializedName("is_accepted")
    private int IS_ACCEPTED;
    @SerializedName("is_rejected")
    private int IS_REJECTED;
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

    public String getLIKESCOUNT() {
        return LIKESCOUNT;
    }

    public String getSHARESCOUNT() {
        return SHARESCOUNT;
    }

    public String getCOMMENTSCOUONT() {
        return COMMENTSCOUONT;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public int getHAS_ATTACHMENTS() {
        return HAS_ATTACHMENTS;
    }

    public String getATTACHMENTS() {
        return ATTACHMENTS;
    }

    public int getIS_ACCEPTED() {
        return IS_ACCEPTED;
    }

    public int getIS_REJECTED() {
        return IS_REJECTED;
    }


    public int getSWAP_ID() {
        return SWAP_ID;
    }

    public int getPOSTER_USER_ID() {
        return POSTER_USER_ID;
    }

    public int getSWAPED_WITH_USER_ID() {
        return SWAPED_WITH_USER_ID;
    }

    public int getSTATUS_ID() {
        return STATUS_ID;
    }

    public float getAVG_RATTING() {
        return AVG_RATTING;
    }

    public String getPOSTER_FULLNAME() {
        return POSTER_FULLNAME;
    }

    public String getSWAPED_WITH_FULLNAME() {
        return SWAPED_WITH_FULLNAME;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public String getSWAP_DATE() {
        return SWAP_DATE;
    }

    public String getPOSTER_PROFILE_IMAGE() {
        return POSTER_PROFILE_IMAGE;
    }

    public String getSWAPED_WITH_PROFILE_IMAGE() {
        return SWAPED_WITH_PROFILE_IMAGE;
    }

    public Boolean getIS_AUTHENTICATED() {
        return IS_AUTHENTICATED;
    }

    public Boolean getIS_ERROR() {
        return IS_ERROR;
    }

    public Boolean getIS_FOUND() {
        return IS_FOUND;
    }

    public Boolean getIS_ME() {
        return IS_ME;
    }

    public ArrayList<SwapsTab> getSwapsTabArrayList() {
        return swapsTabArrayList;
    }
}
