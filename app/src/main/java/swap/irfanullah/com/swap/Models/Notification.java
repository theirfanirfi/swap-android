package swap.irfanullah.com.swap.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Notification {
    @SerializedName("name")
    private String FULL_NAME;
    @SerializedName("notification_id")
    private int NOTIFICATION_ID;
    @SerializedName("isStatus")
    private int IS_STATUS;
    @SerializedName("isFollow")
    private int IS_FOLLOW;
    @SerializedName("isFollowed")
    private int IS_FOLLOWED;
    @SerializedName("isSwap")
    private int IS_SWAP;
    @SerializedName("isDeclined")
    private int IS_DECLINED;
    @SerializedName("status_id")
    private int STATUS_ID;
    @SerializedName("followed_id")
    private int FOLLOWED_ID;
    @SerializedName("follower_id")
    private int FOLLOWER_ID;
    @SerializedName("isAccepted")
    private int IS_ACCEPTED;
    @SerializedName("swaper_id")
    private int SWAPER_ID;
    @SerializedName("swaped_with_id")
    private int SWAPED_WITH_ID;
    @SerializedName("isViewed")
    private int IS_VIEWED;
    @SerializedName("notifications_count")
    private int NOTIFICATIONS_COUNT;
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
    @SerializedName("notifications")
    private ArrayList<Notification> notifications;
    @SerializedName("swap_id")
    private int SWAP_ID;
    @SerializedName("is_accepted")
    private int isACCEPTED;

    @SerializedName("isLike")
    private int isLike;

    @SerializedName("count")
    private int NOTIFICATIONSCOUNT;

    @SerializedName("isComment")
    private int isComment;

    @SerializedName("isTag")
    private int isTag;

    @SerializedName("isShare")
    private int isShare;

    @SerializedName("action_by")
    private int ACTION_BY;

    @SerializedName("isRatting")
    private int isRating;

    @SerializedName("isAction")
    private int isAction;

    @SerializedName("profile_image")
    private String PROFIE_IMAGE;

    public String getPROFIE_IMAGE() {
        return PROFIE_IMAGE;
    }

    @SerializedName("isCleared")
    private Boolean IS_CLEARED;

    public Boolean getIS_CLEARED() {
        return IS_CLEARED;
    }

    public int getSWAP_ID() {
        return SWAP_ID;
    }

    public int getIsACCEPTED() {
        return isACCEPTED;
    }

    public String getFULL_NAME() {
        return FULL_NAME;
    }

    public Boolean getIS_FOUND() {
        return IS_FOUND;
    }

    public ArrayList<Notification> getNotifications() {
        return notifications;
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

    public int getIS_VIEWED() {
        return IS_VIEWED;
    }

    public int getNOTIFICATIONS_COUNT() {
        return NOTIFICATIONS_COUNT;
    }

    public int getNOTIFICATION_ID() {
        return NOTIFICATION_ID;
    }

    public int getIS_STATUS() {
        return IS_STATUS;
    }

    public int getIS_FOLLOW() {
        return IS_FOLLOW;
    }

    public int getIS_FOLLOWED() {
        return IS_FOLLOWED;
    }

    public int getIS_SWAP() {
        return IS_SWAP;
    }

    public int getIS_DECLINED() {
        return IS_DECLINED;
    }

    public int getSTATUS_ID() {
        return STATUS_ID;
    }

    public int getFOLLOWED_ID() {
        return FOLLOWED_ID;
    }

    public int getFOLLOWER_ID() {
        return FOLLOWER_ID;
    }

    public int getIS_ACCEPTED() {
        return IS_ACCEPTED;
    }

    public int getSWAPER_ID() {
        return SWAPER_ID;
    }

    public int getSWAPED_WITH_ID() {
        return SWAPED_WITH_ID;
    }

    public int getIsLike() {
        return isLike;
    }

    public int getIsComment() {
        return isComment;
    }

    public int getIsTag() {
        return isTag;
    }

    public int getIsShare() {
        return isShare;
    }

    public int getACTION_BY() {
        return ACTION_BY;
    }

    public int getIsRating() {
        return isRating;
    }

    public int getIsAction() {
        return isAction;
    }

    public int getNOTIFICATIONSCOUNT() {
        return NOTIFICATIONSCOUNT;
    }
}
