package com.mogamusa.uniottoparty;

/**
 * Created by masayuki on 2017/09/28.
 */

public class Album {
    long id;
    String name;
    String artist;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String tweet) {
        this.artist = tweet;
    }
}