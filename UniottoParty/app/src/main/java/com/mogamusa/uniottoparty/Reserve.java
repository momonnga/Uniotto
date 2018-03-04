package com.mogamusa.uniottoparty;

/**
 * Created by masayuki on 2017/10/11.
 */

public class Reserve {
    int userid;
    String title;
    String artist;
    public Reserve(){

    }
    public int getUserid(){
        return userid;
    }
    public void setUserid(int i){
        this.userid=i;
    }
    public String getTitle(){
        return title;
    }
    public void setTitle(String s){
        this.title=s;
    }
    public String getArtist(){
        return artist;
    }
    public void setArtist(String s){
        this.artist=s;
    }
}
