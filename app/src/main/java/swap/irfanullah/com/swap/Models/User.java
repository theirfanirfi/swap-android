package swap.irfanullah.com.swap.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import swap.irfanullah.com.swap.R;

public class User {
    @SerializedName("user_id")
    private int USER_ID;
    @SerializedName("username")
    private String USERNAME;
    @SerializedName("name")
    private String FULL_NAME;
    @SerializedName("email")
    private String EMAIL;
    @SerializedName("password")
    private String PASSWORD;
    @SerializedName("profile_image")
    private String PROFILE_IMAGE;
    @SerializedName("created_at")
    private String CREATED_AT;
    @SerializedName("updated_at")
    private String UPDATED_AT;
    @SerializedName("token")
    private String TOKEN;
    @SerializedName("ratting")
    private float RATING;
    @SerializedName("profile_description")
    private String PROFILE_DESCRIPTION;
    @SerializedName("isError")
    private Boolean IS_ERROR;
    @SerializedName("isAuthenticated")
    private Boolean IS_AUTHENTICATED;
    @SerializedName("isUpdated")
    private Boolean IS_UPDATED;
    @SerializedName("isNotMatched")
    private Boolean IS_NOT_MATCHED;
    @SerializedName("isOldPasswordInCorrect")
    private Boolean IS_OLD_PASSWORD_INCORRECT;
    @SerializedName("isLengthError")
    private Boolean IS_LENGTH_ERROR;
    @SerializedName("isChanged")
    private Boolean IS_CHANGED;
    @SerializedName("isEmpty")
    private Boolean IS_EMPTY;
    @SerializedName("user")
    private User USER;
    @SerializedName("message")
    private String MESSAGE;
    @SerializedName("is_invited")
    private int IS_INVITED;
    @SerializedName("invites")
    private int INVITES;

    @SerializedName("is_followed")
    private int IS_FOLLOWED;
    @SerializedName("followed")
    private int FOLLOWED;

    @SerializedName("isFound")
    private Boolean ISFOUND;

    @SerializedName("users")
    private ArrayList<User> USERS;
    @SerializedName("is_soc")
    private int isSocialMedia;

    @SerializedName("f_id")
    private int FID = 0;

    @SerializedName("isUserRegistered")
    private boolean isUserRegistered;

    @SerializedName("isUser")
    private boolean isUser;

    public boolean isUser() {
        return isUser;
    }

    public boolean isUserRegistered() {
        return isUserRegistered;
    }

    public int getFID() {
        return FID;
    }

    public int getIsSocialMedia() {
        return isSocialMedia;
    }

    public int getIS_FOLLOWED() {
        return IS_FOLLOWED;
    }

    public int getFOLLOWED() {
        return FOLLOWED;
    }
    public ArrayList<User> getUSERS() {
        return USERS;
    }

    public Boolean getISFOUND() {
        return ISFOUND;
    }

    public int getIS_INVITED() {
        return IS_INVITED;
    }

    public int getINVITES() {
        return INVITES;
    }

    public Boolean getIS_EMPTY() {
        return IS_EMPTY;
    }

    public Boolean getIS_NOT_MATCHED() {
        return IS_NOT_MATCHED;
    }

    public Boolean getIS_OLD_PASSWORD_INCORRECT() {
        return IS_OLD_PASSWORD_INCORRECT;
    }

    public Boolean getIS_LENGTH_ERROR() {
        return IS_LENGTH_ERROR;
    }

    public Boolean getIS_CHANGED() {
        return IS_CHANGED;
    }

    public String getMESSAGE() {
        return MESSAGE;
    }

    public Boolean getIS_ERROR() {
        return IS_ERROR;
    }

    public Boolean getIS_AUTHENTICATED() {
        return IS_AUTHENTICATED;
    }

    public Boolean getIS_UPDATED() {
        return IS_UPDATED;
    }

    public User getUSER() {
        return USER;
    }

    public String getPROFILE_DESCRIPTION() {
        return PROFILE_DESCRIPTION;
    }

    public float getRATING() {
        return RATING;
    }

    public String getTOKEN() {
        return TOKEN;
    }

    public void setTOKEN(String TOKEN) {
        this.TOKEN = TOKEN;
    }

    public String getFULL_NAME() {
        return FULL_NAME;
    }

    public void setFULL_NAME(String FULL_NAME) {
        this.FULL_NAME = FULL_NAME;
    }

    public int getUSER_ID() {
        return USER_ID;
    }

    public void setUSER_ID(int USER_ID) {
        this.USER_ID = USER_ID;
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }

    public void setPASSWORD(String PASSWORD) {
        this.PASSWORD = PASSWORD;
    }

    public String getPROFILE_IMAGE() {
        return PROFILE_IMAGE;
    }

    public void setPROFILE_IMAGE(String PROFILE_IMAGE) {
        this.PROFILE_IMAGE = PROFILE_IMAGE;
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
}
