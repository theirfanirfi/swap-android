package swap.irfanullah.com.swap.Models;

import com.google.gson.annotations.SerializedName;

public class Statistics {
    @SerializedName("isError")
    private Boolean IS_ERROR;
    @SerializedName("isFound")
    private Boolean IS_FOUND;
    @SerializedName("isEmpty")
    private Boolean IS_EMPTY;
    @SerializedName("isAuthenticated")
    private Boolean IS_AUTHENTICATED;
    @SerializedName("statuses")
    private int STATUSES_COUNT;
    @SerializedName("swaps")
    private int SWAPS_COUNT;
    @SerializedName("followers")
    private int FOLLOWERS_COUNT;
    @SerializedName("message")
    private String MESSAGE;
    @SerializedName("user")
    private User USER;
    @SerializedName("isfollow")
    private int isFollow;

    @SerializedName("avg_ratting")
    private float AVG_RATING;

    @SerializedName("reviews_count")
    private int ReviewsCount;

    @SerializedName("swaps_reviews")
    private Swap SWAPSREVIEWS;

    public int getIsFollow() {
        return isFollow;
    }

    public User getUSER() {
        return USER;
    }

    public String getMESSAGE() {
        return MESSAGE;
    }

    public Boolean getIS_ERROR() {
        return IS_ERROR;
    }

    public Boolean getIS_FOUND() {
        return IS_FOUND;
    }

    public Boolean getIS_EMPTY() {
        return IS_EMPTY;
    }

    public Boolean getIS_AUTHENTICATED() {
        return IS_AUTHENTICATED;
    }

    public int getSTATUSES_COUNT() {
        return STATUSES_COUNT;
    }

    public int getSWAPS_COUNT() {
        return SWAPS_COUNT;
    }

    public int getFOLLOWERS_COUNT() {
        return FOLLOWERS_COUNT;
    }

    public float getAVG_RATING() {
        return AVG_RATING;
    }

    public int getReviewsCount() {
        return ReviewsCount;
    }

    public Swap getSWAPSREVIEWS() {
        return SWAPSREVIEWS;
    }
}
