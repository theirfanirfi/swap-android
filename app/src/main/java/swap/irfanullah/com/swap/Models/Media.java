package swap.irfanullah.com.swap.Models;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class Media implements Parcelable {
    private Uri uri;
    private int type;
    /* Types of Medias
    1. @Images = 1
    2. @Videos = 2
     */

    public Media(Uri uri, int type) {
        this.uri = uri;
        this.type = type;
    }

    protected Media(Parcel in) {
        uri = in.readParcelable(Uri.class.getClassLoader());
        type = in.readInt();
    }

    public static final Creator<Media> CREATOR = new Creator<Media>() {
        @Override
        public Media createFromParcel(Parcel in) {
            return new Media(in);
        }

        @Override
        public Media[] newArray(int size) {
            return new Media[size];
        }
    };

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(uri, flags);
        dest.writeInt(type);
    }
}
