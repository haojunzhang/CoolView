package idv.haojun.aplayer.model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class AVideo implements Parcelable{
    private long id;
    private String title;
    private int duration;
    private String data;

    public AVideo(){

    }

    public AVideo(long id, String title, int duration, String data) {
        this.id = id;
        this.title = title;

        this.duration = duration;
        this.data = data;
    }

    protected AVideo(Parcel in) {
        id = in.readLong();
        title = in.readString();
        duration = in.readInt();
        data = in.readString();
    }

    public static final Creator<AVideo> CREATOR = new Creator<AVideo>() {
        @Override
        public AVideo createFromParcel(Parcel in) {
            return new AVideo(in);
        }

        @Override
        public AVideo[] newArray(int size) {
            return new AVideo[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Uri getUri(){
        return Uri.parse(data);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeInt(duration);
        dest.writeString(data);
    }
}
