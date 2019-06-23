package swap.irfanullah.com.swap.Models;

import com.google.gson.annotations.SerializedName;

public class ProfileModel {
    @SerializedName("isAuthenticated")
    private Boolean IS_AUTHENTICATED;
    @SerializedName("isError")
    private Boolean IS_ERROR;
    @SerializedName("isMoved")
    private Boolean IS_MOVED;
    @SerializedName("isSaved")
    private Boolean IS_SAVED;
    @SerializedName("isEmpty")
    private Boolean IS_EMPTY;
    @SerializedName("message")
    private String MESSAGE;
    @SerializedName("profile_image")
    private String PROFILE_IMAGE;
    @SerializedName("user")
    private User USER;

    public User getUSER() {
        return USER;
    }

    public Boolean getIS_AUTHENTICATED() {
        return IS_AUTHENTICATED;
    }

    public Boolean getIS_ERROR() {
        return IS_ERROR;
    }

    public Boolean getIS_MOVED() {
        return IS_MOVED;
    }

    public Boolean getIS_SAVED() {
        return IS_SAVED;
    }

    public Boolean getIS_EMPTY() {
        return IS_EMPTY;
    }

    public String getMESSAGE() {
        return MESSAGE;
    }

    public String getPROFILE_IMAGE() {
        return PROFILE_IMAGE;
    }
}
