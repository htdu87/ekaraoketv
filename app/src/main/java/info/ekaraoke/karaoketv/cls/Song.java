package info.ekaraoke.karaoketv.cls;

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

    public Song(){}

    public Song(long id, String name, String lyrics, String path, SONG_FORMAT format){
        this.id=id;
        this.name=name;
        this.format=format;
        this.path=path;
        this.lyrics=lyrics;
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
    }

    protected Song(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.lyrics = in.readString();
        this.path = in.readString();
        int tmpFormat = in.readInt();
        this.format = tmpFormat == -1 ? null : SONG_FORMAT.values()[tmpFormat];
    }

    public static final Parcelable.Creator<Song> CREATOR = new Parcelable.Creator<Song>() {
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
