package swap.irfanullah.com.swap.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Followers {

    @SerializedName("f_id")
    private int FID;
    @SerializedName("isAuthenticated")
    private Boolean isAuthenticated;
    @SerializedName("isFound")
    private Boolean isFound;
    @SerializedName("followed_user_id")
    private int FOLLOWED_USER_ID;
    @SerializedName("follower_user_id")
    private int FOLLOWER_USER_ID;
    @SerializedName("name")
    private String FULLNAME;
    @SerializedName("username")
    private String USERNAME;
    @SerializedName("profile_image")
    private String PROFILE_IMAGE;
    @SerializedName("swaps")
    private ArrayList<Swap> SWAPS;
    @SerializedName("followers")
    private ArrayList<Followers> followers;
    @SerializedName("status_id")
    private int STATUS_ID;
    @SerializedName("message")
    private String MESSAGE;
    @SerializedName("isFollowersFound")
    private Boolean isFollowersFound;
    @SerializedName("isSwapsFound")
    private Boolean isSwapsFound;
    @SerializedName("isFollowed")
    private Boolean isFollowed;
    @SerializedName("isAlreadyFollowed")
    private Boolean isAlreadyFollowed;

    @SerializedName("isUnFollowed")
    private Boolean isUnFollowed;
    @SerializedName("isAlreadyUnFollowed")
    private Boolean isAlreadyUnFollowed;
    @SerializedName("isError")
    private Boolean isError;

    public Boolean getUnFollowed() {
        return isUnFollowed;
    }

    public Boolean getAlreadyUnFollowed() {
        return isAlreadyUnFollowed;
    }

    public Boolean getFollowed() {
        return isFollowed;
    }

    public Boolean getAlreadyFollowed() {
        return isAlreadyFollowed;
    }

    public Boolean getError() {
        return isError;
    }

    public Boolean getFollowersFound() {
        return isFollowersFound;
    }

    public Boolean getSwapsFound() {
        return isSwapsFound;
    }

    public String getMESSAGE() {
        return MESSAGE;
    }

    public Boolean getAuthenticated() {
        return isAuthenticated;
    }

    public Boolean getFound() {
        return isFound;
    }

    public int getSTATUS_ID() {
        return STATUS_ID;
    }

    public void setSTATUS_ID(int STATUS_ID) {
        this.STATUS_ID = STATUS_ID;
    }

    public ArrayList<Followers> getFollowers() {
        return followers;
    }

    public void setFollowers(ArrayList<Followers> followers) {
        this.followers = followers;
    }

    public int getFID() {
        return FID;
    }

    public void setFID(int FID) {
        this.FID = FID;
    }

    public int getFOLLOWED_USER_ID() {
        return FOLLOWED_USER_ID;
    }

    public void setFOLLOWED_USER_ID(int FOLLOWED_USER_ID) {
        this.FOLLOWED_USER_ID = FOLLOWED_USER_ID;
    }

    public int getFOLLOWER_USER_ID() {
        return FOLLOWER_USER_ID;
    }

    public void setFOLLOWER_USER_ID(int FOLLOWER_USER_ID) {
        this.FOLLOWER_USER_ID = FOLLOWER_USER_ID;
    }

    public String getFULLNAME() {
        return FULLNAME;
    }

    public void setFULLNAME(String FULLNAME) {
        this.FULLNAME = FULLNAME;
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    public String getPROFILE_IMAGE() {
        return PROFILE_IMAGE;
    }

    public void setPROFILE_IMAGE(String PROFILE_IMAGE) {
        this.PROFILE_IMAGE = PROFILE_IMAGE;
    }

    public ArrayList<Swap> getSWAPS() {
        return SWAPS;
    }

    public void setSWAPS(ArrayList<Swap> SWAPS) {
        this.SWAPS = SWAPS;
    }
}
