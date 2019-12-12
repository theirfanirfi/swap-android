package swap.irfanullah.com.swap.Models;

import com.google.gson.annotations.SerializedName;

import retrofit2.http.GET;

public class Swap {
    @SerializedName("swap_id")
    private int SWAP_ID;
    @SerializedName("poster_user_id")
    private int POSTER_USER_ID;
    @SerializedName("swaped_with_user_id")
    private int SWAPED_WITH_USER_ID;
    @SerializedName("status_id")
    private int STATUS_ID;
    @SerializedName("created_at")
    private String CREATED_AT;
    @SerializedName("updated_at")
    private String UPDATED_AT;
    @SerializedName("isAuthenticated")
    private Boolean isAuthenticated;
    @SerializedName("isError")
    private Boolean isError;
    @SerializedName("isAlready")
    private Boolean isAlready;
    @SerializedName("isSwaped")
    private Boolean isSwaped;
    @SerializedName("isEmpty")
    private Boolean isEmpty;
    @SerializedName("message")
    private String MESSAGE;
    @SerializedName("isDeSwaped")
    private Boolean DE_SWAPED;
    @SerializedName("isExist")
    private Boolean IS_EXIST;
    @SerializedName("isFound")
    private Boolean IS_FOUND;
    @SerializedName("isDeSwap")
    private Boolean IS_DE_SWAP;
    @SerializedName("is_accepted")
    private int IS_ACCEPTED;
    @SerializedName("is_rejected")
    private int IS_REJECTED;
    @SerializedName("swap")
    private Swap GET_SWAP;
    @SerializedName("isApproved")
    private Boolean isApproved;

    @SerializedName("isDeclined")
    private Boolean isDeclined;

    @SerializedName("count")
    private int SWAPREQUESTSCOUNT;

    @SerializedName("avg_ratting")
    private float AverageRatingSwapReviews;

    @SerializedName("reviews_count")
    private int ReviewsCount;

    public float getAverageRatingSwapReviews() {
        return AverageRatingSwapReviews;
    }

    public int getReviewsCount() {
        return ReviewsCount;
    }

    public Boolean getDeclined() {
        return isDeclined;
    }

    public Boolean getApproved() {
        return isApproved;
    }

    public Swap getGET_SWAP() {
        return GET_SWAP;
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

    public Boolean getIS_DE_SWAP() {
        return IS_DE_SWAP;
    }

    public Boolean getDE_SWAPED() {
        return DE_SWAPED;
    }

    public Boolean getIS_EXIST() {
        return IS_EXIST;
    }

    public Boolean getAuthenticated() {
        return isAuthenticated;
    }

    public Boolean getError() {
        return isError;
    }

    public Boolean getAlready() {
        return isAlready;
    }

    public Boolean getSwaped() {
        return isSwaped;
    }

    public Boolean getEmpty() {
        return isEmpty;
    }

    public String getMESSAGE() {
        return MESSAGE;
    }

    public int getSWAP_ID() {
        return SWAP_ID;
    }

    public void setSWAP_ID(int SWAP_ID) {
        this.SWAP_ID = SWAP_ID;
    }

    public int getPOSTER_USER_ID() {
        return POSTER_USER_ID;
    }

    public void setPOSTER_USER_ID(int POSTER_USER_ID) {
        this.POSTER_USER_ID = POSTER_USER_ID;
    }

    public int getSWAPED_WITH_USER_ID() {
        return SWAPED_WITH_USER_ID;
    }

    public void setSWAPED_WITH_USER_ID(int SWAPED_WITH_USER_ID) {
        this.SWAPED_WITH_USER_ID = SWAPED_WITH_USER_ID;
    }

    public int getSTATUS_ID() {
        return STATUS_ID;
    }

    public void setSTATUS_ID(int STATUS_ID) {
        this.STATUS_ID = STATUS_ID;
    }

    public String getCREATED_AT() {
        return CREATED_AT;
    }

    public void setCREATED_AT(String CREATED_AT) {
        this.CREATED_AT = CREATED_AT;
    }

    public String getUPDATED_AT() {
        return UPDATED_AT;
    }

    public void setUPDATED_AT(String UPDATED_AT) {
        this.UPDATED_AT = UPDATED_AT;
    }

    public int getSWAPREQUESTSCOUNT() {
        return SWAPREQUESTSCOUNT;
    }
}
