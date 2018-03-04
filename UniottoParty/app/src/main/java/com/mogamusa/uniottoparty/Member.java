package com.mogamusa.uniottoparty;

/**
 * Created by masayuki on 2017/10/09.
 */

public class Member {
    //long id;
    String username;
    String imageuri;
    //private String playlist[][];

    public Member(){

    }
    public Member(String username,String imageuri){
        this.username=username;
        this.imageuri=imageuri;

    }

    /*
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    */
    public void setUsername(String s){
        username=s;
    }
    public String getUsername(String s){
        return username;
    }
    public void setImageuri(String s){
        imageuri=s;
    }
    public String getImageuri(String s){
        return imageuri;
    }


}
