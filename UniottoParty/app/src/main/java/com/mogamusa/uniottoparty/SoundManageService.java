package com.mogamusa.uniottoparty;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.NotificationCompat;
import android.text.format.Time;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static java.lang.Math.random;

/**
 * CodeZine
 * Android Studio 2で始めるアプリ開発入門
 * サービスサンプル
 *
 * サービスクラス。
 *
 * @author Shinzo SAITO
 */
public class SoundManageService extends Service {

    /**
     * メディアプレーヤーフィールド。
     */
    private MediaPlayer _player;
    DatabaseReference mysamref;
    ExecutorService loopC = Executors.newSingleThreadExecutor();
    ExecutorService loopC2 = Executors.newSingleThreadExecutor();
    MyApplication mApp;


    String nameList="";

    String roomid="room22";
    String roomid2="";
    String userName="masa1";//masayuki5・masa1がxperformance それ以外(masayuki4・masa2)がz4
    int limitMinute=100;
    int yeard,monthd,dayd,hourd,minuted,secondd;
    int hostflag=-1;
    public int flagnum=0;
    public int logFlag=0;
    String date="";
    String dateR="";
    int updateLim=0;
    int prenumber=-1;
    int memberID=-1;
    String photoUrl="nourl";
    int humancountmax=0;
    static MediaPlayer m;
    int selectcount=0;
    int flagstart=0;
    int tempturnnum=0;
    int enteriplay=0;
    Album insertAlbum;
    String selectTitle="未選択";
    String selectArtist="未選択";
    String selectImage="未選択";
    String selectUri="未選択";
    private String res = "";
    public String albumdes[][]=new String[25][2];
    ArrayList<Album> list;
    public String musicListArr[];
    public String musicListTitleArr[];
    public String musicListArtistArr[];
    public int musicListNum=0;
    String musicList="";
    int startBFlag=0;
    int countL=0;
    int flag=0;
    String keywordD="";
    public static Intent intent;

    public static void start(@NonNull Context context) {
        intent = new Intent(context, SoundManageService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }



    @Override
    public void onCreate() {
        super.onCreate();
        mApp = (MyApplication) this.getApplication();
        roomid=mApp.getRoomid();
        userName=mApp.getUserName();
        photoUrl=mApp.getPhotoURL();

        NotificationHelper notificationHelper = new NotificationHelper(this);
        Notification.Builder builder = notificationHelper.getNotification();
        startForeground(2, builder.build());

        Time time = new Time("Asia/Tokyo");
        time.setToNow();
        yeard=time.year;
        monthd=time.month+1;
        dayd=time.monthDay;
        hourd=time.hour;
        minuted=time.minute;
        secondd=time.second;
        _player = new MediaPlayer();

        List<MusicItem> mItems = MusicItem.getItems(getApplicationContext());
        //MusicItem mItems2 =mItems.get(1);
        //Log.i("ItemList",""+mItems.size());
        //Log.i("ItemList",""+mItems.get(2800).title);
        musicListNum=mItems.size();
        musicListTitleArr=new String[musicListNum];
        musicListArtistArr=new String[musicListNum];
        musicListArr=new String[musicListNum];

        for(int i=0;i<musicListNum;i++) {
            musicList += mItems.get(i).title+",";
            musicListArr[i]=mItems.get(i).title+","+mItems.get(i).artist;
            musicListTitleArr[i]=mItems.get(i).title;
            musicListArtistArr[i]=mItems.get(i).artist;
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        mysamref=database.getReference("room");
        mysamref.child("aiueo").child("playing").setValue("false");

        room();
        member();
        turn();
        status();
        musicListFunc();
        playlist();
        //iplay("LIAR");


        //このループ内からsetTextしてはいけない！
        loopC.submit(new Runnable() {
            @Override
            public void run() {


                while (true) {
                    //Log.d("","check1");



                    //Log.d("","check2");
                    //Log.d("","memnum:"+memnum+",turnnum:"+turnnum+",nowhum:"+nowhum+",username"+userName+",turnping"+turnping+",nowplaying"+nowplayingname+",nowplayingart"+nowplayingartist);


                    startBFlag=mApp.getStartBFlag();
                    Log.d("",""+startBFlag);
                    if(((turnping.equals("false")&&nowhum.equals(userName)&&turnnum!=0)||(startBFlag==1&&nowhum.equals(userName)))){
                        Log.d("check2-2","check2-2"+selectTitle+","+mApp.getKeyWord());
                        if(keywordD=="") {
                            keywordD = keywords[(int) (random() * 22)];
                            mApp.setKeyWord(keywordD);
                            mysamref.child(roomid).child("keyword").setValue(keywordD);
                        }
                        Log.d("","mode:"+mode+",modeEq:"+(mode.equals("0"))+",Key:"+mApp.getKeyWord()+",URI"+mApp.getSelectUri());

                        if(mode.equals("0")||(mode.equals("1")&&mApp.getKeyWord()!="不明"&&mApp.getSelectUri()!="")) {
                            selectTitle = mApp.getSelectTitle();
                            selectUri = mApp.getSelectUri();
                            Log.d("","selectURI"+selectUri);
                            if (selectUri != "未選択"&&selectUri!="") {
                                Log.d("", "iplay:select" + selectTitle);
                                Uri seluri = Uri.parse(selectUri);
                                iplay2(seluri);


                                mApp.setSelectUri("未選択");
                                //getTest4(selectTitle,selectArtist);
                                selectcount++;

                            /*
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    selectcount++;
                                    selectMusicView.setText("未選択");
                                    selectArtistView.setText("未選択");
                                    recommendMusicView.setText("未選択");
                                    recommendArtistView.setText("未選択");
                                    if(path2!="") {
                                        artWorkimage.setImageBitmap(bitmap2);
                                    }
                                    //ftextView1.setText(adapter.getItem(selectcount));
                                }
                            });
                            */
                                flagstart = 2;
                            } else if (selectTitle != "未選択") {
                                Log.d("", "iplay:select" + selectTitle);
                                iplay(selectTitle);

                                mApp.setSelectTitle("未選択");
                                mApp.setSelectArtist("未選択");
                                mApp.setSelectArtWork("未選択");
                                getTest4(selectTitle, selectArtist);
                                selectcount++;

                            /*
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    selectcount++;
                                    selectMusicView.setText("未選択");
                                    selectArtistView.setText("未選択");
                                    recommendMusicView.setText("未選択");
                                    recommendArtistView.setText("未選択");
                                    if(path2!="") {
                                        artWorkimage.setImageBitmap(bitmap2);
                                    }
                                    //ftextView1.setText(adapter.getItem(selectcount));
                                }
                            });
                            */
                                flagstart = 2;
                            } else if (selectTitle != "未選択") {
                                Log.d("", "iplay:recom" + selectTitle);
                                iplay(selectTitle);
                                mApp.setSelectTitle("未選択");
                                mApp.setSelectArtist("未選択");
                                mApp.setSelectArtWork("未選択");
                                getTest4(selectTitle, selectArtist);
                                selectcount++;


                            /*
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    selectcount++;
                                    recommendMusicView.setText("未選択");
                                    recommendArtistView.setText("未選択");
                                    if(path2!="") {
                                        artWorkimage.setImageBitmap(bitmap2);
                                    }
                                    //ftextView1.setText(adapter.getItem(selectcount));
                                }
                            });
                            */
                                flagstart = 1;
                            }
                        }

                        //selectcount++;
                        //textView1.setText(adapter.getItem(selectcount));
                        // TODO Auto-generated method stub

                    }
                    //Log.d("","check3");


                    if(m!=null) {
                        //Log.d("playing",""+m.isPlaying());
                        if(m.isPlaying()==false&&turnphum==userName&&flagstart==2){
                            mysamref.child(roomid).child("playing").setValue("false");
                            mysamref.child(roomid).child("playinghuman").setValue("noone");
                            tempturnnum = Integer.parseInt(turnpnum) + 1;
                            mysamref.child(roomid).child("playingnumber").setValue(tempturnnum);
                            mysamref.child(roomid).child("keyword").setValue("不明");
                            mApp.setSelectUri("");
                            mApp.setPlayingImage("");
                            if(mApp.getKeyWord()!="不明") {
                                //mysamref.child(roomid).child("mode").setValue("1");
                                mApp.setKeyWord("不明");
                            }
                            //mApp.setKeyWord("不明");


                        }
                    }
                    if(m!=null){
                        if(m.isPlaying()==true){
                            flagstart=2;
                            enteriplay=0;
                            if(mApp.getStopBFlag()==1){
                                m.stop();
                            }
                        }
                    }
                    mApp.setStopBFlag(0);
                    if(startBFlag==1){
                        mApp.setStartBFlag(2);
                    }
                    //Log.d("","check4");

                    //Log.d("turn","ping:"+turnping+",phum:"+turnphum+",pnum:"+turnpnum+",nexthum:"+nexthum);
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                    }


                }

            }
        });


        /*
        //このループ内からsetTextしてはいけない！
        loopC2.submit(new Runnable() {
            @Override
            public void run() {


                while (true) {
                    //Log.d("","check1");
                    countL++;

                    //Log.d("","check4");
                    if(countL>20&&flag==0){

                            iplay("LIAR");
                        flag=1;

                    }

                    //Log.d("turn","ping:"+turnping+",phum:"+turnphum+",pnum:"+turnpnum+",nexthum:"+nexthum);
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                    }


                }

            }
        });
        */


    }

    int iC=0;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //String mediaFileUriStr = "android.resource://" + getPackageName() + "/" + R.raw.mountain_stream;
        //Uri mediaFileUri = Uri.parse(mediaFileUriStr);
        /*
        try {
            _player.setDataSource(SoundManageService.this, mediaFileUri);
            _player.setOnPreparedListener(new PlayerPreparedListener());
            _player.setOnCompletionListener(new PlayerCompletionListener());
            _player.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */

        /*
        loopC.submit(new Runnable() {
            @Override
            public void run() {
                iC=mApp.getCount();


                while (true) {
                    //Log.d("","check1");

                    iC++;
                    mysamref.child("aiueo").child("playing").setValue(_player.isPlaying());
                    mysamref.child("aiueo").child("count").setValue(iC);
                    if(iC>80&&_player.isPlaying()==false){
                        //_player.start();
                        iC=0;
                    }
                    mApp.setCount(iC);


                    //Log.d("turn","ping:"+turnping+",phum:"+turnphum+",pnum:"+turnpnum+",nexthum:"+nexthum);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }


                }

            }
        });
        */

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    int temproomnum;

    @Override
    public void onDestroy() {
        super.onDestroy();
        mysamref.removeEventListener(velstatus);
        mysamref.removeEventListener(velroom);
        mysamref.removeEventListener(velturn);
        mysamref.removeEventListener(velmember);
        mysamref.removeEventListener(velplaylist);

        if(Integer.parseInt(memnum)==1) {
            mysamref.child(roomid).setValue(null);
        }else{
            temproomnum=Integer.parseInt(memnum)-1;
            mysamref.child(roomid).child("member").child(String.valueOf(memberID)).setValue(null);
            mysamref.child(roomid).child("number").setValue(temproomnum);
        }
        if(m!=null) {
            if (m.isPlaying() == true) {
                m.stop();
            }
        }
        Log.i("Service", "サービス破棄");
        if(_player.isPlaying()) {
            _player.stop();
        }
        _player.release();
        _player = null;
    }

    /**
     * メディア再生準備が完了時のリスナクラス。
     */
    private class PlayerPreparedListener implements MediaPlayer.OnPreparedListener {

        @Override
        public void onPrepared(MediaPlayer mp) {
            Log.i("Service", "再生開始");

            mp.start();

            NotificationCompat.Builder builder = new NotificationCompat.Builder(SoundManageService.this);
            builder.setSmallIcon(android.R.drawable.ic_dialog_info);
            builder.setContentTitle("再生開始");
            builder.setContentText("音声ファイルの再生を開始しました");

            Intent intent = new Intent(SoundManageService.this, MainActivity.class);
            intent.putExtra("fromNotification", true);
            PendingIntent stopServiceIntent = PendingIntent.getActivity(SoundManageService.this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            builder.setContentIntent(stopServiceIntent);
            builder.setAutoCancel(true);

            Notification notification = builder.build();
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(1, notification);
        }
    }

    /**
     * メディア再生が終了したときのリスナクラス。
     */
    private class PlayerCompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            Log.i("Service", "再生終了");

            NotificationCompat.Builder builder = new NotificationCompat.Builder(SoundManageService.this);
            builder.setSmallIcon(android.R.drawable.ic_dialog_info);
            builder.setContentTitle("再生終了");
            builder.setContentText("音声ファイルの再生が終了しました");
            Notification notification = builder.build();
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(0, notification);

            //stopSelf();
        }
    }
    ValueEventListener velroom;
    String playList[][];

    public void room(){
        // Write a message to the database


        velroom=new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if(dataSnapshot.child(roomid).child("date").getValue()==null){
                    //Log.d("roomid is","nullですね");
                    date = yeard + "-" + monthd + "-" + dayd + "-"+hourd + "-" + minuted + "-" + secondd;



                    mysamref.child(roomid).child("number").setValue(1);
                    mysamref.child(roomid).child("humancountmax").setValue(1);
                    mysamref.child(roomid).child("date").setValue(date);
                    mysamref.child(roomid).child("host").setValue(userName);
                    Log.d("","name:"+userName+",url"+photoUrl);
                    Member tempmember=new Member();
                    tempmember.setImageuri(photoUrl);
                    tempmember.setUsername(userName);
                    mysamref.child(roomid).child("member").child("1").setValue(tempmember);
                    //mysamref.child(roomid).child("member").child("1").child("name").setValue(userName);
                    mysamref.child(roomid).child("playing").setValue("false");
                    mysamref.child(roomid).child("playingnumber").setValue(0);
                    mysamref.child(roomid).child("playinghuman").setValue("noone");
                    if(keywordD=="") {
                        String keywordD = keywords[(int) (random() * 22)];
                        mApp.setKeyWord(keywordD);
                        mysamref.child(roomid).child("keyword").setValue(keywordD);
                    }
                    mysamref.child(roomid).child("mode").setValue("0");
                    //if(memberID==-1) {
                    memberID = 1;
                    //}

                    nameList=userName;
                    dateR = date;
                    hostflag=1;
                    logFlag=1;
                }else {
                    //Log.d("aiueo",(dataSnapshot.child(roomid).child("number").getValue()));
                    String number=String.valueOf(dataSnapshot.child(roomid).child("number").getValue());

                    //Log.d("aiueo",number);
                    int numberupd=Integer.parseInt(number);
                    //Log.d("","number:"+numberupd);
                    String name="";
                    nameList="";

                    for(int i=1;i<numberupd+1;i++) {
                        name=String.valueOf(dataSnapshot.child(roomid).child("member").child("" + i).child("username").getValue());

                        //Log.d("name",name+","+userName);
                        if(name==null){

                        }else{
                            if(nameList.equals("")){
                                nameList="'"+name+"'";
                            }else{
                                nameList=nameList+","+"'"+name+"'";
                            }


                        }

                        if ( name.equals(userName)) {

                            //Log.d("name","一致してる");
                            flagnum++;
                        }else{
                            //Log.d("name","一致してない");


                        }
                    }
                    //mysamref.child(roomid).child("date").setValue(date);
                    if(flagnum==0&&logFlag==0) {
                        date = yeard + "-" + monthd + "-" + dayd + "-"+hourd + "-" + minuted + "-" + secondd;
                        //if(name.equals("null")) {
                        //}else{

                        //本来ならここはコメントアウトを外すところ
                        mysamref.child(roomid).child("date").setValue(date);

                        numberupd++;
                        if(memberID==-1) {
                            memberID = numberupd;
                        }
                        //mysamref.child(roomid).child("member").child("" + numberupd).child("username").setValue(userName);
                        Member tempmember=new Member();
                        tempmember.setImageuri(photoUrl);
                        tempmember.setUsername(userName);
                        //memberID=numberupd;
                        mysamref.child(roomid).child("member").child("" + numberupd).setValue(tempmember);

                        mysamref.child(roomid).child("number").setValue(numberupd);
                        humancountmax=Integer.parseInt(dataSnapshot.child(roomid).child("humancountmax").getValue().toString());
                        if(numberupd>humancountmax) {
                            mysamref.child(roomid).child("humancountmax").setValue(numberupd);
                        }
                        logFlag=1;
                        //updateLim=1;

                        //}
                    }
                    if(prenumber!=-1) {
                        if (prenumber != numberupd) {
                            dateR = String.valueOf(dataSnapshot.child(roomid).child("date").getValue());
                        }
                    }

                    prenumber=numberupd;
                    if(hostflag==-1) {
                        hostflag = 0;
                    }


                }



            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        };

        mysamref.addValueEventListener(velroom);

    }

    ValueEventListener velmember;
    String memberarr[]=new String [10];
    int membernum;
    String keywords[]={"春","夏","秋","冬","涙","青春","希望","絶望","恋","趣味","楽器","昔","愛","病気","少年","大人","疾走","迷子","旅","料理","和風","洋風"};

    public void member(){


        velmember=new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot){
                int i=0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    //String sender = (String) dataSnapshot.child("username").getValue();
                    //String body = (String) dataSnapshot.child("username").getValue();
                    Member tempmember=dataSnapshot.getValue(Member.class);
                    memberarr[i]=tempmember.username;
                    i++;
                    Log.d("Firebase", String.format("sender:%s, body:%s", tempmember.username, tempmember.imageuri));
                    //Log.d("Firebase", String.format("sender:%s, body:%s", post.title, post.body));
                }
            }



            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        };

        mysamref.child(roomid).child("member").addValueEventListener(velmember);

    }
    ValueEventListener velturn;
    String turnping="";
    String turnphum="";
    String turnpnum="";
    String memnum="";
    String nowhum="";
    String nexthum="";
    int tempturn=0;
    Member nowMember;
    Member nextMember;
    Uri uri;
    Uri urinext;
    AsyncTaskHttpRequest task;
    AsyncTaskHttpRequest tasknext;
    int turnnum=0;
    int turnnumS=0;
    String turnkey="";
    //やること複数人におけるターン処理
    public void turn(){

        velturn=new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                turnping=String.valueOf(dataSnapshot.child(roomid).child("playing").getValue());
                turnphum=String.valueOf(dataSnapshot.child(roomid).child("playinghuman").getValue());
                turnpnum=String.valueOf(dataSnapshot.child(roomid).child("playingnumber").getValue());
                turnkey=String.valueOf(dataSnapshot.child(roomid).child("keyword").getValue());

                memnum=String.valueOf(dataSnapshot.child(roomid).child("number").getValue());
                //tempturn=turnpnum+1;

                if(memnum!="null"&&memnum!=null&&turnpnum!="null"&&turnpnum!=null) {
                    Log.d("memnum", memnum);
                    turnnum=Integer.parseInt(turnpnum);
                    tempturn=((Integer.parseInt(turnpnum))%(Integer.parseInt(memnum)))+1;
                    /*
                    if (Integer.parseInt(memnum) != 0) {
                        member = new String[humancountmax];
                        for (int i = 0; i < humancountmax; i++) {
                            //if(String.valueOf(dataSnapshot.child(roomid).child("member").child(String.valueOf(i)).getValue())!=null) {
                            try{
                                member[i] = String.valueOf(dataSnapshot.child(roomid).child("member").child(String.valueOf(i)).child("name").getValue());
                            }catch(Exception e){

                            }
                        }
                    }
                    */

                    /*
                    for (int i = 0; i < Integer.parseInt(memnum); i++) {
                        if (member[i].equals(nowhuman)) {
                            tempturn = i+1;
                        }
                    }
                    */
                    Log.d("tempturn","tempturn:"+tempturn);
                    mApp.setRoomNumber(memnum);


                    if (memnum.equals("1")) {
                        Log.d("memnum2",memnum);
                        nowMember=dataSnapshot.child(roomid).child("member").child(String.valueOf(tempturn)).getValue(Member.class);
                        nowhum = String.valueOf(dataSnapshot.child(roomid).child("member").child(String.valueOf(tempturn)).child("username").getValue());
                        mApp.setNowHuman(nowhum);
                        mApp.setNowHumanURI(nowMember.imageuri);

                        nextMember=dataSnapshot.child(roomid).child("member").child(String.valueOf(1)).getValue(Member.class);
                        nexthum = String.valueOf(dataSnapshot.child(roomid).child("member").child("1").child("username").getValue());
                        mApp.setNextHuman(nexthum);
                        mApp.setNextHumanURI(nextMember.imageuri);

                        //nexthuman.setText(nexthum);
                        //nextuserimage.setImageBitmap(getBitmapFromURL("http://icons.iconarchive.com/icons/laurent-baumann/neige/128/HTTP-icon.png"));
                        //Uri urinext = Uri.parse("http://cdn-ak.f.st-hatena.com/images/fotolife/f/fjswkun/20150927/20150927140905.jpg");
                        //urinext = Uri.parse(nextMember.imageuri);
                        //Uri.Builder buildernext = urinext.buildUpon();
                        //tasknext = new AsyncTaskHttpRequest(nextuserimage);
                        //tasknext.execute(buildernext);

                    } else {
                        nowMember=dataSnapshot.child(roomid).child("member").child(String.valueOf(tempturn)).getValue(Member.class);
                        nowhum = String.valueOf(dataSnapshot.child(roomid).child("member").child(String.valueOf(tempturn)).child("username").getValue());
                        mApp.setNowHumanURI(nowMember.imageuri);


                        tempturn=((Integer.parseInt(turnpnum)+1)%(Integer.parseInt(memnum)))+1;
                        nextMember=dataSnapshot.child(roomid).child("member").child(String.valueOf(tempturn)).getValue(Member.class);
                        nexthum = String.valueOf(dataSnapshot.child(roomid).child("member").child(String.valueOf(tempturn)).child("username").getValue());
                        mApp.setNextHuman(nexthum);
                        mApp.setNextHumanURI(nextMember.imageuri);
                        //nexthuman.setText(nexthum);
                        //nextuserimage.setImageURI(Uri.parse(nextMember.imageuri));
                        if(nextMember.imageuri!="null") {
                            //urinext = Uri.parse(nextMember.imageuri);
                            //Uri.Builder buildernext = urinext.buildUpon();
                            //tasknext = new AsyncTaskHttpRequest(nextuserimage);
                            //tasknext.execute(buildernext);
                        }
                        //mApp.setKeyWord("あなたは回答者です。");

                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        };

        mysamref.addValueEventListener(velturn);
    }
    ValueEventListener velstatus;
    String statusping="";
    String statusphum="";
    String statuspnum="";
    String memnum2="";
    String nowhum2="";
    String nexthum2="";
    int tempturnS=0;
    String mode="";
    //やること複数人におけるターン処理
    public void status(){

        velstatus=new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                statusping=String.valueOf(dataSnapshot.child(roomid).child("playing").getValue());
                mApp.setPlayingBo(statusping);
                statusphum=String.valueOf(dataSnapshot.child(roomid).child("playinghuman").getValue());
                statuspnum=String.valueOf(dataSnapshot.child(roomid).child("playingnumber").getValue());
                mode=String.valueOf(dataSnapshot.child(roomid).child("mode").getValue());

                mApp.setMode(mode);


                //tempturn=turnpnum+1;

                if(memnum!="null"&&memnum!=null&&statuspnum!="null"&&statuspnum!=null) {
                    Log.d("memnumSSSSSSSS", memnum);
                    turnnumS=Integer.parseInt(statuspnum);
                    //tempturnS=((Integer.parseInt(turnpnum))%(Integer.parseInt(memnum)))+1;


                    /*
                    for (int i = 0; i < Integer.parseInt(memnum); i++) {
                        if (member[i].equals(nowhuman)) {
                            tempturn = i+1;
                        }
                    }
                    */
                    /*
                    if(String.valueOf(dataSnapshot.child(roomid).child("playlist").getValue())!=null){

                        nowplayingname=String.valueOf(dataSnapshot.child(roomid).child("playlist").child(statuspnum).child("name").getValue());
                        nowplayingartist=String.valueOf(dataSnapshot.child(roomid).child("playlist").child(statuspnum).child("artist").getValue());
                        if(nowplayingartist!="null"&&nowplayingname!="null") {
                            songView.setText(nowplayingname);
                            artistView.setText(nowplayingartist);
                        }


                    }
                    */

                    //Log.d("tempturn","tempturn:"+tempturn);



                }
                //Log.d("nowplaying:",nowplayingname+","+nowplayingartist);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        };

        mysamref.addValueEventListener(velstatus);

    }

    List<MusicItem> mItems;

    ArrayAdapter<String> adapterF;
    ArrayList<Album> listF;
    AlbumAdapter adapterFapi;
    ListView musicListView;

    ArrayAdapter<String> adapterR;
    ArrayList<Album> listR;
    AlbumAdapter adapterRapi;
    ListView recommendListView;

    ArrayAdapter<String> adapterResrve;
    ArrayList<Album> listReserve;
    AlbumAdapter adapterReserveapi;
    //ListView recommendListView;
    private Uri mImageUri;
    Bitmap albumArt;

    String fselection="";
    String path2="";




    public void musicListFunc(){


        mItems = MusicItem.getItems(getApplicationContext());
        //MusicItem mItems2 =mItems.get(1);
        //Log.i("ItemList",""+mItems.size());

        //textView1 = (TextView)findViewById(R.id.textView10);
        String mttempName="";
        String mttempArtist="";
        int mttempInt=0;
        for(int i=0;i<10;i++){

            mttempInt=(int)(random()*mItems.size());
            mttempName=mItems.get(mttempInt).title;
            mttempArtist=mItems.get(mttempInt).artist;
            //adapter.add(mttempName);


            Album albuminsF = new Album();
            //albumins.setName("HogeFuga");
            //albumins.setArtist("あいうえおかきくけこ");
            albuminsF.setName(mttempName);
            albuminsF.setArtist(mttempArtist);

            if(i==0){
                //recommendTitleView1.setText(mttempName);
                //recommendArtistView1.setText(mttempArtist);
                mApp.setRecommendTitleText1(mttempName);
                mApp.setRecommendArtistText1(mttempArtist);
                mApp.setSelectTitle(mttempName);
                mApp.setSelectArtist(mttempArtist);
                mysamref.child("aiueo").child("1").child("recT1").setValue(mttempName);
                mysamref.child("aiueo").child("1").child("recA1").setValue(mttempArtist);

                fselection = "title='" + mttempName + "'";
                ContentResolver cr = getApplicationContext().getContentResolver();
                Cursor cursor = cr.query(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,  //データの種類
                        new String[]{
                                MediaStore.Audio.Media.ALBUM,
                                MediaStore.Audio.Media.ARTIST,
                                MediaStore.Audio.Media.TITLE,
                                MediaStore.Audio.Media._ID,
                                MediaStore.Audio.Albums.ALBUM_ID
                        },//取得する
                        //"title='サムライハート (Some Like It Hot!!)'" , //フィルター条件 nullはフィルタリング無し
                        fselection,
                        null, //フィルター用のパラメータ
                        null   //並べ替え
                );
                ArrayList<String> listCur = new ArrayList<String>();
                cursor.moveToFirst();
                //do {
                listCur.add(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID)));
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ID));
                Log.d("paaaaaaaath","paaaaaaaaaaaaaaaaaath:"+path);

                ContentResolver cr2 = getApplicationContext().getContentResolver();
                Cursor cursor2 = cr2.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                        new String[] {MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART},
                        MediaStore.Audio.Albums._ID+ "="+path,
                        null,
                        null);

                cursor2.moveToFirst();
                Log.d("paaaaath2","cursor"+cr2+","+cursor2);

                path2 = cursor2.getString(cursor2.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));

                mApp.setRecommendImageURIText1(path2);
                //Uri uriJ = Uri.fromFile(file);
                //Log.d("paaaaath2","paaaaaaaaaat:"+path2);
                //InputStream stream = new FileInputStream(file);
                //Bitmap bitmap = BitmapFactory.decodeStream(new BufferedInputStream(stream));
                //selectImageView.setImageBitmap(bitmap);
                //} catch (FileNotFoundException e) {
                //}

                /*

                bitmap2= BitmapFactory.decodeFile(path2);
                bitmapRecommend1=bitmap2;
                if(bitmap2!=null) {
                    recommendImageView1.setImageBitmap(bitmap2);
                    selectImageView.setImageBitmap(bitmapRecommend1);

                }else{

                    recommendImageView1.setImageBitmap(bmpartworknoneR);
                    selectImageView.setImageBitmap(bmpartworknone);
                }
                selectMusicView.setText(recommendTitleView1.getText());
                selectArtistView.setText(recommendArtistView1.getText());
                */
            }else if(i==1){
                //recommendTitleView2.setText(mttempName);
                //recommendArtistView2.setText(mttempArtist);
                mApp.setRecommendTitleText2(mttempName);
                mApp.setRecommendArtistText2(mttempArtist);
                mysamref.child("aiueo").child("2").child("recT1").setValue(mttempName);
                mysamref.child("aiueo").child("2").child("recA1").setValue(mttempArtist);

                fselection = "title='" + mttempName + "'";
                ContentResolver cr = getApplicationContext().getContentResolver();
                Cursor cursor = cr.query(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,  //データの種類
                        new String[]{
                                MediaStore.Audio.Media.ALBUM,
                                MediaStore.Audio.Media.ARTIST,
                                MediaStore.Audio.Media.TITLE,
                                MediaStore.Audio.Media._ID,
                                MediaStore.Audio.Albums.ALBUM_ID
                        },//取得する
                        //"title='サムライハート (Some Like It Hot!!)'" , //フィルター条件 nullはフィルタリング無し
                        fselection,
                        null, //フィルター用のパラメータ
                        null   //並べ替え
                );
                ArrayList<String> listCur = new ArrayList<String>();
                cursor.moveToFirst();
                //do {
                listCur.add(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID)));
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ID));
                //Log.d("paaaaaaaath","paaaaaaaaaaaaaaaaaath:"+path);

                ContentResolver cr2 = getApplicationContext().getContentResolver();
                Cursor cursor2 = cr2.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                        new String[] {MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART},
                        MediaStore.Audio.Albums._ID+ "="+path,
                        null,
                        null);

                cursor2.moveToFirst();

                path2 = cursor2.getString(cursor2.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
                mApp.setRecommendImageURIText2(path2);
                //Log.d("paaaaath2","paaaaaaaaaath2"+path2);
                /*
                bitmap2=BitmapFactory.decodeFile(path2);
                //Log.d("paaaaath2","paaaaaaaaaath2"+bitmap2.getWidth());
                bitmapRecommend2=bitmap2;
                //recommendImageView2.setImageBitmap(bitmap2);
                if(bitmap2!=null) {
                    recommendImageView2.setImageBitmap(bitmap2);
                }else{
                    recommendImageView2.setImageBitmap(bmpartworknoneR);
                }
                */
            }else if(i==2){
                //recommendTitleView3.setText(mttempName);
                //recommendArtistView3.setText(mttempArtist);
                mApp.setRecommendTitleText3(mttempName);
                mApp.setRecommendArtistText3(mttempArtist);

                mysamref.child("aiueo").child("3").child("recT1").setValue(mttempName);
                mysamref.child("aiueo").child("3").child("recA1").setValue(mttempArtist);

                fselection = "title='" + mttempName + "'";
                ContentResolver cr = getApplicationContext().getContentResolver();
                Cursor cursor = cr.query(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,  //データの種類
                        new String[]{
                                MediaStore.Audio.Media.ALBUM,
                                MediaStore.Audio.Media.ARTIST,
                                MediaStore.Audio.Media.TITLE,
                                MediaStore.Audio.Media._ID,
                                MediaStore.Audio.Albums.ALBUM_ID
                        },//取得する
                        //"title='サムライハート (Some Like It Hot!!)'" , //フィルター条件 nullはフィルタリング無し
                        fselection,
                        null, //フィルター用のパラメータ
                        null   //並べ替え
                );
                ArrayList<String> listCur = new ArrayList<String>();
                cursor.moveToFirst();
                //do {
                listCur.add(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID)));
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ID));
                //Log.d("paaaaaaaath","paaaaaaaaaaaaaaaaaath:"+path);

                ContentResolver cr2 = getApplicationContext().getContentResolver();
                Cursor cursor2 = cr2.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                        new String[] {MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART},
                        MediaStore.Audio.Albums._ID+ "="+path,
                        null,
                        null);

                cursor2.moveToFirst();

                path2 = cursor2.getString(cursor2.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
                mApp.setRecommendImageURIText3(path2);
                //Log.d("paaaaath2","paaaaaaaaaath2"+path2);
                /*
                bitmap2=BitmapFactory.decodeFile(path2);
                bitmapRecommend3=bitmap2;
                //recommendImageView3.setImageBitmap(bitmap2);
                if(bitmap2!=null) {
                    recommendImageView3.setImageBitmap(bitmap2);
                }else{
                    recommendImageView3.setImageBitmap(bmpartworknoneR);
                }
                */
            }else if(i==3){
                //recommendTitleView4.setText(mttempName);
                //recommendArtistView4.setText(mttempArtist);

                mApp.setRecommendTitleText4(mttempName);
                mApp.setRecommendArtistText4(mttempArtist);

                mysamref.child("aiueo").child("4").child("recT1").setValue(mttempName);
                mysamref.child("aiueo").child("4").child("recA1").setValue(mttempArtist);

                fselection = "title='" + mttempName + "'";
                ContentResolver cr = getApplicationContext().getContentResolver();
                Cursor cursor = cr.query(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,  //データの種類
                        new String[]{
                                MediaStore.Audio.Media.ALBUM,
                                MediaStore.Audio.Media.ARTIST,
                                MediaStore.Audio.Media.TITLE,
                                MediaStore.Audio.Media._ID,
                                MediaStore.Audio.Albums.ALBUM_ID
                        },//取得する
                        //"title='サムライハート (Some Like It Hot!!)'" , //フィルター条件 nullはフィルタリング無し
                        fselection,
                        null, //フィルター用のパラメータ
                        null   //並べ替え
                );
                ArrayList<String> listCur = new ArrayList<String>();
                cursor.moveToFirst();
                //do {
                listCur.add(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID)));
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ID));
                //Log.d("paaaaaaaath","paaaaaaaaaaaaaaaaaath:"+path);

                ContentResolver cr2 = getApplicationContext().getContentResolver();
                Cursor cursor2 = cr2.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                        new String[] {MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART},
                        MediaStore.Audio.Albums._ID+ "="+path,
                        null,
                        null);

                cursor2.moveToFirst();

                path2 = cursor2.getString(cursor2.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
                mApp.setRecommendImageURIText4(path2);
                //Log.d("paaaaath2","paaaaaaaaaath2"+path2);
                /*
                bitmap2=BitmapFactory.decodeFile(path2);
                bitmapRecommend4=bitmap2;
                //recommendImageView4.setImageBitmap(bitmap2);
                if(bitmap2!=null) {
                    recommendImageView4.setImageBitmap(bitmap2);
                }else{
                    recommendImageView4.setImageBitmap(bmpartworknoneR);
                }
                */
            }

            //listF.add(albuminsF);

        }
    }
    ValueEventListener velplaylist;
    String playlistarr[][]=new String [10][2];
    int i=0;
    ArrayList<Album> listPre;
    AlbumAdapter adapterPre;

    public void playlist(){


        velplaylist=new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot){

                i=0;
                listPre = new ArrayList<>();
                //adapterPre = new AlbumAdapter(MainActivity.this);
                //adapterPre.setAlbumList(listPre);
                //listViewPre=(ListView)findViewById(R.id.previousListView);
                //listViewPre.setAdapter(adapterPre);

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    //String artist = (String) dataSnapshot.child("artist").getValue();
                    //String name = (String) dataSnapshot.child("name").getValue();
                    Album post=dataSnapshot.getValue(Album.class);
                    //playlistarr[i][0]=name;
                    //playlistarr[i][1]=artist;
                    Album albuminsP = new Album();
                    //albumins.setName("HogeFuga");
                    //albumins.setArtist("あいうえおかきくけこ");
                    albuminsP.setName(post.name);
                    albuminsP.setArtist(post.artist);

                    listPre.add(albuminsP);
                    i++;
                    Log.d("Firebase", String.format("name:%s, artist:%s", post.name, post.artist));
                    //Log.d("Firebase", String.format("sender:%s, body:%s", post.title, post.body));
                }


                if(i>0) {
                    //adapterPre.notifyDataSetChanged();
                    //songView.setText(listPre.get(i-1).getName());
                    //artistView.setText(listPre.get(i-1).getArtist());
                    mApp.setPlayingTitle(listPre.get(i-1).getName());
                    mApp.setPlayingArtist(listPre.get(i-1).getArtist());
                    getTest4(listPre.get(i-1).getName(),listPre.get(i-1).getArtist());
                }




            }



            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        };

        mysamref.child(roomid).child("playlist").addValueEventListener(velplaylist);

    }

    public void iplay(String title){
        Log.d("","enteriplay"+enteriplay+",enteriplayEq:"+(enteriplay==0));
        if(enteriplay==0) {
            mApp.setRecommendImageURIText1("");
            mApp.setRecommendImageURIText2("");
            mApp.setRecommendImageURIText3("");
            mApp.setRecommendImageURIText4("");
            enteriplay = 1;

            if (m != null) {
                if (m.isPlaying() == true) {
                    m.stop();
                }
            }
            fselection = "title='" + title + "'";
            ContentResolver cr = getApplicationContext().getContentResolver();
            Cursor cursor = cr.query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,  //データの種類
                    new String[]{
                            MediaStore.Audio.Media.ALBUM,
                            MediaStore.Audio.Media.ARTIST,
                            MediaStore.Audio.Media.TITLE,
                            MediaStore.Audio.Media._ID,
                            MediaStore.Audio.Albums.ALBUM_ID
                    },//取得する
                    //"title='サムライハート (Some Like It Hot!!)'" , //フィルター条件 nullはフィルタリング無し
                    fselection,
                    null, //フィルター用のパラメータ
                    null   //並べ替え
            );
            ArrayList<String> listCur = new ArrayList<String>();
            cursor.moveToFirst();
            //do {
            listCur.add(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID)));
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ID));
            Log.d("paaaaaaaath","paaaaaaaaaaaaaaaaaath:"+path);

            ContentResolver cr2 = getApplicationContext().getContentResolver();
            Cursor cursor2 = cr2.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                    new String[] {MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART},
                    MediaStore.Audio.Albums._ID+ "="+path,
                    null,
                    null);

            cursor2.moveToFirst();

            path2 = cursor2.getString(cursor2.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
            Log.d("paaaaath2","paaaaaaaaaath2"+path2);
            mApp.setPlayingImage(path2);
            //bitmap2=BitmapFactory.decodeFile(path2);

            // do whatever you need to do


            try {

                m = new MediaPlayer();
                String mediaId = listCur.get(0); //好きなIDを取得する
                m.setDataSource(getApplicationContext(), Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, mediaId));
                m.prepare();
                //mTextView.setText("" + m.getDuration());
                m.start();
                mysamref.child(roomid).child("playing").setValue("true");
                mysamref.child(roomid).child("playinghuman").setValue(userName);
                //tempturnnum = Integer.parseInt(turnpnum) + 1;
                //mysamref.child(roomid).child("playingnumber").setValue(tempturnnum);
                insertAlbum=new Album();
                insertAlbum.setName(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
                insertAlbum.setArtist(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));

                //mysamref.child(roomid).child("playlist").child(turnpnum).child("name").setValue(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
                //mysamref.child(roomid).child("playlist").child(turnpnum).child("artist").setValue(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
                //mysamref.child(roomid).child("playlist").child(turnpnum).setValue(insertAlbum);
                mysamref.child(roomid).child("playlist").child(turnpnum).setValue(insertAlbum);
                //status();



            } catch (IOException e) {

            }

            /*
            if(cursor != null){
                while(!cursor.isAfterLast()){

                    int titleIndex =  cursor.getColumnIndex
                            (MediaStore.Audio.AudioColumns.TITLE);
                    int albumDdIndex = cursor.getColumnIndex
                            (MediaStore.Audio.AudioColumns.ALBUM_ID);

                    //String title = cursor.getString(titleIndex);
                    String albumIdStr = cursor.getString(albumDdIndex);
                    long albumId = Long.parseLong(albumIdStr);
                    Uri albumArtUri = Uri.parse(
                            "content://media/external/audio/albumart");
                    Uri albumUri = ContentUris.withAppendedId(albumArtUri, albumId);
                    //ジャケット画像のURIを取得
                    //ContentResolver cr = getContentResolver();
                    try{
                        InputStream is = cr.openInputStream(albumUri);
                        Bitmap album_art = BitmapFactory.decodeStream(is);
                        artWorkimage.setImageBitmap(album_art);

                        //URIからストリームを取得してそれを画像に変換
                    }catch(FileNotFoundException e) {
                        //ストリームが開けなかったときの処理
                    }
                }
            }
            */
            //} while (cursor.moveToNext());



        }
    }
    public void iplay2(Uri titleURI){
        Log.d("","enteriplay"+enteriplay);
        if(enteriplay==0) {
            enteriplay = 1;

            if (m != null) {
                if (m.isPlaying() == true) {
                    m.stop();
                }
            }
            //fselection = "title='" + title + "'";
            ContentResolver cr = getApplicationContext().getContentResolver();
            /*
            Cursor cursor = cr.query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,  //データの種類
                    new String[]{
                            MediaStore.Audio.Media.ALBUM,
                            MediaStore.Audio.Media.ARTIST,
                            MediaStore.Audio.Media.TITLE,
                            MediaStore.Audio.Media._ID,
                            MediaStore.Audio.Albums.ALBUM_ID
                    },//取得する
                    //"title='サムライハート (Some Like It Hot!!)'" , //フィルター条件 nullはフィルタリング無し
                    fselection,
                    null, //フィルター用のパラメータ
                    null   //並べ替え
            );
            */
            String[] columns = { MediaStore.Audio.Media.ALBUM,
                    MediaStore.Audio.Media.ARTIST,
                    MediaStore.Audio.Media.TITLE,
                    MediaStore.Audio.Media._ID,
                    MediaStore.Audio.Albums.ALBUM_ID };
            Cursor cursor = cr.query(titleURI, columns, null, null, null);
            //cursor.moveToFirst();
            //String path = cursor.getString(0);
            //cursor.close();
            ArrayList<String> listCur = new ArrayList<String>();
            cursor.moveToFirst();
            //do {
            listCur.add(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID)));
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ID));
            Log.d("paaaaaaaath","paaaaaaaaaaaaaaaaaath:"+path);

            ContentResolver cr2 = getApplicationContext().getContentResolver();
            Cursor cursor2 = cr2.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                    new String[] {MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART},
                    MediaStore.Audio.Albums._ID+ "="+path,
                    null,
                    null);

            cursor2.moveToFirst();

            path2 = cursor2.getString(cursor2.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
            Log.d("paaaaath2","paaaaaaaaaath2"+path2);
            mApp.setPlayingImage(path2);
            //bitmap2=BitmapFactory.decodeFile(path2);

            // do whatever you need to do


            try {

                m = new MediaPlayer();
                String mediaId = listCur.get(0); //好きなIDを取得する
                m.setDataSource(getApplicationContext(), Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, mediaId));
                m.prepare();
                //mTextView.setText("" + m.getDuration());
                m.start();
                mysamref.child(roomid).child("playing").setValue("true");
                mysamref.child(roomid).child("playinghuman").setValue(userName);
                //tempturnnum = Integer.parseInt(turnpnum) + 1;
                //mysamref.child(roomid).child("playingnumber").setValue(tempturnnum);
                insertAlbum=new Album();
                insertAlbum.setName(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
                insertAlbum.setArtist(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));

                //mysamref.child(roomid).child("playlist").child(turnpnum).child("name").setValue(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
                //mysamref.child(roomid).child("playlist").child(turnpnum).child("artist").setValue(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
                //mysamref.child(roomid).child("playlist").child(turnpnum).setValue(insertAlbum);
                mysamref.child(roomid).child("playlist").child(turnpnum).setValue(insertAlbum);
                //status();



            } catch (IOException e) {

            }

            /*
            if(cursor != null){
                while(!cursor.isAfterLast()){

                    int titleIndex =  cursor.getColumnIndex
                            (MediaStore.Audio.AudioColumns.TITLE);
                    int albumDdIndex = cursor.getColumnIndex
                            (MediaStore.Audio.AudioColumns.ALBUM_ID);

                    //String title = cursor.getString(titleIndex);
                    String albumIdStr = cursor.getString(albumDdIndex);
                    long albumId = Long.parseLong(albumIdStr);
                    Uri albumArtUri = Uri.parse(
                            "content://media/external/audio/albumart");
                    Uri albumUri = ContentUris.withAppendedId(albumArtUri, albumId);
                    //ジャケット画像のURIを取得
                    //ContentResolver cr = getContentResolver();
                    try{
                        InputStream is = cr.openInputStream(albumUri);
                        Bitmap album_art = BitmapFactory.decodeStream(is);
                        artWorkimage.setImageBitmap(album_art);

                        //URIからストリームを取得してそれを画像に変換
                    }catch(FileNotFoundException e) {
                        //ストリームが開けなかったときの処理
                    }
                }
            }
            */
            //} while (cursor.moveToNext());



        }
    }
    String tempMatch;
    // GET
    private void getTest4(String name,String artist) {
        Log.d("aiueo","getTest4:"+name);
        //list = new ArrayList<>();
        Request request;
        if(!name.equals("未選択")) {
            request = new Request.Builder()
                    .url("https://c15326208.web.cddbp.net/webapi/json/1.0/radio/recommend?client=15326208-22D0B0DD9C64D2EA02DBA05C4843F699&user=43445848250403434-6DB39718FFC49710E554D91F2B51872E&seed=(text_artist_" + artist + ";text_track_" + name + ")&return_count=25")     // 130010->東京
                    .get()
                    .build();
        }else{
            request = new Request.Builder()
                    .url("https://c15326208.web.cddbp.net/webapi/json/1.0/radio/recommend?client=15326208-22D0B0DD9C64D2EA02DBA05C4843F699&user=43445848250403434-6DB39718FFC49710E554D91F2B51872E&seed=(text_artist_星野源;text_track_恋)&return_count=25")     // 130010->東京
                    .get()
                    .build();
        }

        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                failMessage();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                res = response.body().string();
                try {
                    JSONObject resJson = new JSONObject(res);
                    JSONArray weathers = resJson.getJSONArray("RESPONSE");     // 例として "pinpointLocations" を取り出す
                    JSONObject weather = weathers.getJSONObject(0);                     // 2番目のオブジェクトにアクセスしたい場合は"1"
                    JSONArray albums = weather.getJSONArray("ALBUM");
                    for(int i=0;i<25;i++) {
                        final JSONObject album = albums.getJSONObject(i);
                        JSONArray artists = album.getJSONArray("ARTIST");
                        JSONObject artist = artists.getJSONObject(0);
                        albumdes[i][1]=artist.getString("VALUE");
                        JSONArray names = album.getJSONArray("TITLE");
                        JSONObject name = names.getJSONObject(0);
                        albumdes[i][0]=name.getString("VALUE");
                        //String description = album.getString("ORD");
                    }
                    //String description = weather.getString("STATUS");                     // 例として "name" を取り出す
                    //des = description;
                    list = new ArrayList<>();
                    for(int i=0;i<25;i++){
                        //mttemp=mItems.get((int)(Math.random()*30)).title;
                        Album albumins = new Album();
                        //albumins.setName("HogeFuga");
                        //albumins.setArtist("あいうえおかきくけこ");
                        albumins.setName(albumdes[i][0]);
                        albumins.setArtist(albumdes[i][1]);
                                /*
                                if (musicList.matches(".*" + albumdes[i][0] + ".*")) {
                                    list.add(albumins);
                                    adapterapi.notifyDataSetChanged();
                                }else{

                                }
                                */
                        if(i==0){
                            //selectMusicView.setText(albumdes[i][0]);
                            //selectArtistView.setText(albumdes[i][1]);
                            //recommendTitleView1.setText(albumdes[i][0]);
                            //recommendArtistView1.setText(albumdes[i][1]);
                            //selectImageView.setImageBitmap(bitmapRecommend1);
                        }else if(i==1){
                            //recommendTitleView1.setText(albumdes[i][0]);
                            //recommendArtistView1.setText(albumdes[i][1]);
                        }else if(i==2){
                            //recommendTitleView1.setText(albumdes[i][0]);
                            //recommendArtistView1.setText(albumdes[i][1]);
                        }else if(i==3){
                            ///recommendTitleView1.setText(albumdes[i][0]);
                            //recommendArtistView1.setText(albumdes[i][1]);
                        }
                        tempMatch=albumdes[i][0]+","+albumdes[i][1];
                        if(Arrays.asList(musicListArr).contains(tempMatch)){
                            if(listPre!=null) {
                                if(listPre.size()!=0) {
                                    if(listPre.contains(albumins)){

                                    }else {
                                        list.add(albumins);
                                        //adapterapi.notifyDataSetChanged();
                                    }
                                }
                            }else{
                                list.add(albumins);
                                //adapterapi.notifyDataSetChanged();
                            }
                        }


                    }
                    //textView1.setText(des);

                    // reserveListView.setAdapter(adapterapi);

                    Log.d("",res);

                    if(list.size()!=0) {
                                /*
                                recommendMusicView.setText(list.get(0).getName());
                                recommendArtistView.setText(list.get(0).getArtist());
                                recommendTitleView1.setText(list.get(0).getName());
                                recommendArtistView1.setText(list.get(0).getArtist());
                                selectMusicView.setText(list.get(0).getName());
                                selectArtistView.setText(list.get(0).getArtist());
                                */
                        mApp.setSelectTitle(list.get(0).getName());
                        mApp.setSelectArtist(list.get(0).getArtist());
                        mApp.setRecommendTitleText1(list.get(0).getName());
                        mApp.setRecommendArtistText1(list.get(0).getArtist());
                        //mApp.setSelectArtWork();
                    }else{
                        musicListFunc();
                    }



                    /*

                    // UI反映
                    runOnUiThread(new Runnable() {
                        public void run() {
                            //textViewRes.setText(res);
                            //textViewDes.setText(des);
                            //list = new ArrayList<>();
                            list = new ArrayList<>();
                            adapterapi = new AlbumAdapter(MainActivity.this);
                            adapterapi.setAlbumList(list);
                            listView2=(ListView)findViewById(R.id.listView3);
                            listView2.setAdapter(adapterapi);

                            for(int i=0;i<25;i++){
                                //mttemp=mItems.get((int)(Math.random()*30)).title;
                                Album albumins = new Album();
                                //albumins.setName("HogeFuga");
                                //albumins.setArtist("あいうえおかきくけこ");
                                albumins.setName(albumdes[i][0]);
                                albumins.setArtist(albumdes[i][1]);

                                if(i==0){
                                    //selectMusicView.setText(albumdes[i][0]);
                                    //selectArtistView.setText(albumdes[i][1]);
                                    //recommendTitleView1.setText(albumdes[i][0]);
                                    //recommendArtistView1.setText(albumdes[i][1]);
                                    //selectImageView.setImageBitmap(bitmapRecommend1);
                                }else if(i==1){
                                    //recommendTitleView1.setText(albumdes[i][0]);
                                    //recommendArtistView1.setText(albumdes[i][1]);
                                }else if(i==2){
                                    //recommendTitleView1.setText(albumdes[i][0]);
                                    //recommendArtistView1.setText(albumdes[i][1]);
                                }else if(i==3){
                                    ///recommendTitleView1.setText(albumdes[i][0]);
                                    //recommendArtistView1.setText(albumdes[i][1]);
                                }
                                tempMatch=albumdes[i][0]+","+albumdes[i][1];
                                if(Arrays.asList(musicListArr).contains(tempMatch)){
                                    if(listPre!=null) {
                                        if(listPre.size()!=0) {
                                            if(listPre.contains(albumins)){

                                            }else {
                                                list.add(albumins);
                                                adapterapi.notifyDataSetChanged();
                                            }
                                        }
                                    }else{
                                        list.add(albumins);
                                        adapterapi.notifyDataSetChanged();
                                    }
                                }


                            }
                            //textView1.setText(des);

                            // reserveListView.setAdapter(adapterapi);

                            Log.d("",res);

                            if(list.size()!=0) {

                                mApp.setSelectTitle(list.get(0).getName());
                                mApp.setSelectArtist(list.get(0).getArtist());
                                //mApp.setSelectArtWork();
                            }else{
                                musicListFunc();
                            }
                        }

                    });
        */
                } catch(JSONException e) {
                    failMessage();
                    e.printStackTrace();
                }
            }
        });

    }
    private void failMessage() {
        /*
        runOnUiThread(new Runnable() {
            public void run() {
                //textViewRes.setText("onFailure");
                //textViewDes.setText("No Data");
                Log.d("","failure");
            }
        });
        */
    }
}
