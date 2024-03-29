package swap.irfanullah.com.swap.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Messenger {

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
    @SerializedName("sender_id")
    private int SENDER_ID;
    @SerializedName("reciever_id")
    private int RECIEVER_ID;
    @SerializedName("chat_id")
    private int CHAT_ID;
    @SerializedName("created_at")
    private String CREATED_AT;
    @SerializedName("updated_at")
    private String UPDATED_AT;
    @SerializedName("response_message")
    private String RESPONSE_MESSAGE;
    @SerializedName("messages")
    private ArrayList<Messenger> MESSENGER;
    @SerializedName("isSent")
    private Boolean IS_SENT;
    @SerializedName("last_message")
    private Messenger LAST_MESSAGE;
    @SerializedName("count_unread_messages")
    private int COUNT_READ_MESSAGES;
    @SerializedName("last_message_count")
    private int LAST_MESSAGE_COUNT;

    @SerializedName("is_forwarded")
    private int isForwareded;

    @SerializedName("m_id")
    private int MESSAGE_ID;

    @SerializedName("is_audio")
    private int IS_AUDIO;

    @SerializedName("audio_attachment")
    private String AUDIO;

    @SerializedName("msg")
    private Messenger MSG;

    public int getLAST_MESSAGE_COUNT() {
        return LAST_MESSAGE_COUNT;
    }

    public Messenger getLAST_MESSAGE() {
        return LAST_MESSAGE;
    }

    public int getCOUNT_READ_MESSAGES() {
        return COUNT_READ_MESSAGES;
    }

    public Boolean getIS_SENT() {
        return IS_SENT;
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

    public Boolean getIS_FOUND() {
        return IS_FOUND;
    }

    public String getMESSAGE() {
        return MESSAGE;
    }

    public int getSENDER_ID() {
        return SENDER_ID;
    }

    public int getRECIEVER_ID() {
        return RECIEVER_ID;
    }

    public int getCHAT_ID() {
        return CHAT_ID;
    }

    public String getCREATED_AT() {
        return CREATED_AT;
    }

    public String getUPDATED_AT() {
        return UPDATED_AT;
    }

    public String getRESPONSE_MESSAGE() {
        return RESPONSE_MESSAGE;
    }

    public ArrayList<Messenger> getMESSENGER() {
        return MESSENGER;
    }

    public int isForwareded() {
        return isForwareded;
    }

    public int getIsForwareded() {
        return isForwareded;
    }

    public int getMESSAGE_ID() {
        return MESSAGE_ID;
    }

    public int getIS_AUDIO() {
        return IS_AUDIO;
    }

    public String getAUDIO() {
        return AUDIO;
    }

    public Messenger getMSG() {
        return MSG;
    }
}
