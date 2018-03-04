package com.mogamusa.uniottoparty;

import android.app.Application;
import android.net.Uri;

import java.net.URI;

/**
 * Created by MASAYUKI on 2017/11/25.
 */

public class MyApplication extends Application {
    private String value = "";
    int startBFlag=0;
    int stopBFlag=0;

    private int count=0;
    String playingBo="";
    String playingTitle="曲名";
    String playingArtist="アーティスト名";
    String playingImage="";

    String userName;
    String roomid;

    String selectTitle;
    String selectArtist;
    String selectArtWork;

    String selectUri="未選択";

    String recommendTitleText1;
    String recommendTitleText2;
    String recommendTitleText3;
    String recommendTitleText4;

    String recommendArtistText1;
    String recommendArtistText2;
    String recommendArtistText3;
    String recommendArtistText4;

    String recommendImageURIText1;
    String recommendImageURIText2;
    String recommendImageURIText3;
    String recommendImageURIText4;

    String photoURL="";

    String nowHuman="";
    String nextHuman="";
    String nowHumanURI="";
    String nextHumanURI="";

    String roomNumber="0";

    String keyWord="不明";
    String mode="";


    @Override
    public void onCreate() {
        //アプリケーションクラス作成時
        super.onCreate();
    }
    public String getValue(){
        return this.value;
    }
    public void setValue(String value){
        this.value = value;
    }

    public int getStartBFlag(){
        return this.startBFlag;
    }
    public void setStartBFlag(int startBFlag){
        this.startBFlag=startBFlag;
    }
    public int getStopBFlag(){
        return this.stopBFlag;
    }
    public void setStopBFlag(int stopBFlag){
        this.stopBFlag=stopBFlag;
    }
    public int getCount(){
        return this.count;
    }
    public void setCount(int count){
        this.count = count;
    }
    public String getPlayingBo(){
        return this.playingBo;
    }
    public void setPlayingBo(String playingBo){
        this.playingBo = playingBo;
    }

    public String getSelectTitle(){
        return this.selectTitle;
    }
    public void setSelectTitle(String selectTitle){
        this.selectTitle = selectTitle;
    }
    public String getSelectArtist(){
        return this.selectArtist;
    }
    public void setSelectArtist(String selectArtist){
        this.selectArtist = selectArtist;
    }
    public String getSelectArtWork(){
        return this.selectArtWork;
    }
    public void setSelectArtWork(String selectArtWork){
        this.selectArtWork = selectArtWork;
    }

    public String getSelectUri(){
        return this.selectUri;
    }
    public void setSelectUri(String selectUri){
        this.selectUri = selectUri;
    }

    public String getPlayingTitle(){
        return this.playingTitle;
    }
    public void setPlayingTitle(String playingTitle){
        this.playingTitle = playingTitle;
    }
    public String getPlayingArtist(){
        return this.playingArtist;
    }
    public void setPlayingArtist(String playingArtist){
        this.playingArtist = playingArtist;
    }
    public String getPlayingImage(){
        return this.playingImage;
    }
    public void setPlayingImage(String playingImage){
        this.playingImage = playingImage;
    }

    public String getUserName(){
        return this.userName;
    }
    public void setUserName(String userName){
        this.userName = userName;
    }
    public String getRoomid(){
        return this.roomid;
    }
    public void setRoomid(String roomid){
        this.roomid = roomid;
    }
    public String getPhotoURL(){
        return this.photoURL;
    }
    public void setPhotoURL(String photoURL){
        this.photoURL = photoURL;
    }

    public String getNowHuman(){
        return this.nowHuman;
    }
    public void setNowHuman(String nowHuman){
        this.nowHuman = nowHuman;
    }
    public String getNextHuman(){
        return this.nextHuman;
    }
    public void setNextHuman(String nextHuman){
        this.nextHuman =nextHuman;
    }

    public String getNowHumanURI(){
        return this.nowHumanURI;
    }
    public void setNowHumanURI(String nowHumanURI){
        this.nowHumanURI = nowHumanURI;
    }
    public String getNextHumanURI(){
        return this.nextHumanURI;
    }
    public void setNextHumanURI(String nextHumanURI){
        this.nextHumanURI =nextHumanURI;
    }

    public String getRoomNumber(){
        return this.roomNumber;
    }
    public void setRoomNumber(String roomNumber){
        this.roomNumber = roomNumber;
    }


    public String getRecommendTitleText1(){
        return this.recommendTitleText1;
    }
    public void setRecommendTitleText1(String recommendTitleText1){
        this.recommendTitleText1 =recommendTitleText1;
    }
    public String getRecommendArtistText1(){
        return this.recommendArtistText1;
    }
    public void setRecommendArtistText1(String recommendArtistText1){
        this.recommendArtistText1 =recommendArtistText1;
    }
    public String getRecommendImageURIText1(){
        return this.recommendImageURIText1;
    }
    public  void setRecommendImageURIText1(String recommendImageURIText1){
        this.recommendImageURIText1=recommendImageURIText1;
    }

    public String getRecommendTitleText2(){
        return this.recommendTitleText2;
    }
    public void setRecommendTitleText2(String recommendTitleText1){
        this.recommendTitleText2 =recommendTitleText1;
    }
    public String getRecommendArtistText2(){
        return this.recommendArtistText2;
    }
    public void setRecommendArtistText2(String recommendArtistText1){
        this.recommendArtistText2=recommendArtistText1;
    }
    public String getRecommendImageURIText2(){
        return this.recommendImageURIText2;
    }
    public  void setRecommendImageURIText2(String recommendImageURIText1){
        this.recommendImageURIText2=recommendImageURIText1;
    }

    public String getRecommendTitleText3(){
        return this.recommendTitleText3;
    }
    public void setRecommendTitleText3(String recommendTitleText1){
        this.recommendTitleText3 =recommendTitleText1;
    }
    public String getRecommendArtistText3(){
        return this.recommendArtistText3;
    }
    public void setRecommendArtistText3(String recommendArtistText1){
        this.recommendArtistText3 =recommendArtistText1;
    }
    public String getRecommendImageURIText3(){
        return this.recommendImageURIText3;
    }
    public  void setRecommendImageURIText3(String recommendImageURIText1){
        this.recommendImageURIText3=recommendImageURIText1;
    }

    public String getRecommendTitleText4(){
        return this.recommendTitleText4;
    }
    public void setRecommendTitleText4(String recommendTitleText1){
        this.recommendTitleText4 =recommendTitleText1;
    }
    public String getRecommendArtistText4(){
        return this.recommendArtistText4;
    }
    public void setRecommendArtistText4(String recommendArtistText1){
        this.recommendArtistText4 =recommendArtistText1;
    }
    public String getRecommendImageURIText4(){
        return this.recommendImageURIText4;
    }
    public  void setRecommendImageURIText4(String recommendImageURIText1){
        this.recommendImageURIText4=recommendImageURIText1;
    }

    public String getKeyWord(){
        return this.keyWord;
    }
    public  void setKeyWord(String keyWord){
        this.keyWord=keyWord;
    }
    public String getMode(){
        return this.mode;
    }
    public  void setMode(String keyWord){
        this.mode=mode;
    }
}