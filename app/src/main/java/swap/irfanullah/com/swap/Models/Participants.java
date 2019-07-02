package swap.irfanullah.com.swap.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Participants {
    @SerializedName("isAuthenticated")
    private Boolean isAuthenticated;
    @SerializedName("isFound")
    private Boolean isFound;
    @SerializedName("isError")
    private Boolean isError;
    @SerializedName("user_one")
    private int USER_ONE;
    @SerializedName("user_two")
    private int USER_TWO;
    @SerializedName("user_one_id")
    private int USER_ONE_ID;
    @SerializedName("user_two_id")
    private int USER_TWO_ID;
    @SerializedName("user_one_name")
    private String USER_ONE_NAME;
    @SerializedName("user_two_name")
    private String USER_TWO_NAME;
    @SerializedName("user_one_profile_image")
    private String USER_ONE_PROFILE_IMAGE;
    @SerializedName("user_two_profile_image")
    private String USER_TWO_PROFILE_IMAGE;
    @SerializedName("amIuserOne")
    private int AM_I_USER_ONE;
    @SerializedName("message")
    private String MESSAGE;
    @SerializedName("participants")
    private ArrayList<Participants> PARTICIPANTS;
    @SerializedName("chat_id")
    private int CHAT_ID;

    public int getCHAT_ID() {
        return CHAT_ID;
    }

    public Boolean getError() {
        return isError;
    }

    public ArrayList<Participants> getPARTICIPANTS() {
        return PARTICIPANTS;
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

    public int getUSER_ONE() {
        return USER_ONE;
    }

    public int getUSER_TWO() {
        return USER_TWO;
    }

    public int getUSER_ONE_ID() {
        return USER_ONE_ID;
    }

    public int getUSER_TWO_ID() {
        return USER_TWO_ID;
    }

    public String getUSER_ONE_NAME() {
        return USER_ONE_NAME;
    }

    public String getUSER_TWO_NAME() {
        return USER_TWO_NAME;
    }

    public String getUSER_ONE_PROFILE_IMAGE() {
        return USER_ONE_PROFILE_IMAGE;
    }

    public String getUSER_TWO_PROFILE_IMAGE() {
        return USER_TWO_PROFILE_IMAGE;
    }

    public int getAM_I_USER_ONE() {
        return AM_I_USER_ONE;
    }
}
