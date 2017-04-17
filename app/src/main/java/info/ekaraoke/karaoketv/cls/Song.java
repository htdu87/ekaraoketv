package info.ekaraoke.karaoketv.cls;

import info.ekaraoke.karaoketv.utils.SONG_FORMAT;

/**
 * Created by PCNTT on 04/16/2017.
 */

public class Song {
    private long id;
    private String name;
    private String lyrics;
    private SONG_FORMAT format;

    public Song(){}

    public Song(long id, String name, String lyrics, SONG_FORMAT format){
        this.id=id;
        this.name=name;
        this.format=format;
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

    public void setFormat(SONG_FORMAT format) {
        this.format = format;
    }
}
