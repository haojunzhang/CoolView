package idv.haojun.ezvideoplayer;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;

public class VideoItem implements Parcelable {
    private String path;
    private String name;
    private long duration;
    private long size;
    private String sizeText;

    public VideoItem(){

    }

    protected VideoItem(Parcel in) {
        path = in.readString();
        name = in.readString();
        duration = in.readLong();
        size = in.readLong();
        sizeText = in.readString();
    }

    public static final Creator<VideoItem> CREATOR = new Creator<VideoItem>() {
        @Override
        public VideoItem createFromParcel(Parcel in) {
            return new VideoItem(in);
        }

        @Override
        public VideoItem[] newArray(int size) {
            return new VideoItem[size];
        }
    };

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
        setName(new File(path).getName());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
        setSizeText(FileHelper.getFileSize(size));
    }

    public String getSizeText() {
        return sizeText;
    }

    public void setSizeText(String sizeText) {
        this.sizeText = sizeText;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(path);
        dest.writeString(name);
        dest.writeLong(duration);
        dest.writeLong(size);
        dest.writeString(sizeText);
    }
}
