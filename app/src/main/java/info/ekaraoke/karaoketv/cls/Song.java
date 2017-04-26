package info.ekaraoke.karaoketv.cls;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import info.ekaraoke.karaoketv.utils.SONG_FORMAT;

/**
 * Created by PCNTT on 04/16/2017.
 */

public class Song implements Parcelable {
    private long id;
    private String name;
    private String lyrics;
    private String path;
    private SONG_FORMAT format;
    private Bitmap thumbnail;

    public Song(){}

    public Song(long id, String name, String lyrics, String path, Bitmap thumbnail, SONG_FORMAT format, String sformat){
        this.id=id;
        this.name=name;
        this.format=format;
        this.path=path;
        this.lyrics=lyrics;
        this.thumbnail=thumbnail;
        setStrFormat(sformat);
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLyrics() {
        return lyrics;
    }

    public String getPath(){
        return this.path;
    }

    public SONG_FORMAT getFormat() {
        return format;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    public void setPath(String path){
        this.path=path;
    }

    public void setFormat(SONG_FORMAT format) {
        this.format = format;
    }

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setStrFormat(String sformat){
        switch (sformat.toLowerCase()){
            case "avi":
                format= SONG_FORMAT.AVI;
                break;
            case "mkv":
                format=SONG_FORMAT.MKV;
                break;
            case "mid":
                format=SONG_FORMAT.MID;
                break;
            case "mp3":
                format= SONG_FORMAT.MP3;
                break;
            case "mp4":
                format=SONG_FORMAT.MP4;
                break;
            case "vob":
                format=SONG_FORMAT.VOB;
                break;
            default:
                format=SONG_FORMAT.UNKNOW;
        }
    }

    public String getStrFormat(){
        switch (format){
            case AVI:
                return "AVI";
            case MKV:
                return "MKV";
            case MID:
                return "MID";
            case MP3:
                return "MP3";
            case MP4:
                return "MP4";
            case VOB:
                return "VOB";
        }
        return "UNKNOW";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeString(this.lyrics);
        dest.writeString(this.path);
        dest.writeInt(this.format == null ? -1 : this.format.ordinal());
        dest.writeParcelable(this.thumbnail, flags);
    }

    protected Song(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.lyrics = in.readString();
        this.path = in.readString();
        int tmpFormat = in.readInt();
        this.format = tmpFormat == -1 ? null : SONG_FORMAT.values()[tmpFormat];
        this.thumbnail = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel source) {
            return new Song(source);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };
}
