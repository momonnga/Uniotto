package com.mogamusa.uniottoparty;

/**
 * Created by masayuki on 2018/01/31.
 */

import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Time;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class KeyAnswerActivity extends AppCompatActivity implements View.OnClickListener {
    MyApplication mApp;

    public Button button_segue;
    public ImageButton button_start;
    public ImageButton button_next;
    public Button button_pause;
    public Button button_api;
    public Button button_select;
    public Button button_select_keyword;
    // リストビューにアイテム (adapter) を追加
    ListView reserveListView;

    TextView selectMusicView;
    TextView selectArtistView;

    TextView selectKeyWord;

    TextView recommendMusicView;
    TextView recommendArtistView;

    TextView recommendTitleView1;
    TextView recommendTitleView2;
    TextView recommendTitleView3;
    TextView recommendTitleView4;

    TextView recommendArtistView1;
    TextView recommendArtistView2;
    TextView recommendArtistView3;
    TextView recommendArtistView4;

    public int trackingFlag=0;

    public TextView textView1;
    public String playingHum="";
    public String member[];

    int turnnum=0;
    int turnnumS=0;

    private String res = "";
    private String des = "";
    public String albumdes[][]=new String[25][2];

    public String musicListArr[];
    public String musicListTitleArr[];
    public String musicListArtistArr[];
    public int musicListNum=0;


    //HttpPostAsync hpa;
    String musicList="";
    String nameList="";
    String filter="";
    JSONObject[] bookObject;
    JSONObject[] plObject;
    String selection="";
    int flag=0;
    String url="";
    //RequestQueue queue;
    static MediaPlayer m;
    ExecutorService exec = Executors.newSingleThreadExecutor();
    public int a=0;
    private String s;
    public TextView mTextView;
    String firstTex="";
    String mode="select";//insert or select
    public String location="ex";//internal or external Storage
    DatabaseReference mysamref;
    DatabaseReference mysamref2;
    DatabaseReference mysamref3;
    DatabaseReference mysamrefs;

    String roomid="room22";
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
    Time timenow=new Time("Asia/Tokyo");
    int createFlag=0;
    String firstPerson="";
    String firstSong="";
    int playFirst=0;
    String fselection="";
    FirebaseDatabase database2;
    FirebaseDatabase databases;

    String playingD="false";
    String playingHumanD="";
    String playingNameD="";
    String playingOwnerD="";
    String playingNumberD="";

    String tempPlaying="false";

    int tempND=0;

    int falseupd=0;
    int trueupd=0;
    int tempDuration;
    int tempSeek=0;

    //ExecutorService exec2 = Executors.newSingleThreadExecutor();
    ExecutorService loopC = Executors.newSingleThreadExecutor();

    EditText editText;
    EditText editTextR;

    int startFlag=0;
    private final Handler mHideHandler = new Handler();
    private static final int UI_ANIMATION_DELAY = 300;
    private View mContentView;
    private View mContentView2;
    private View mContentView3;
    private View mContentViewNONE;

    int viewC=0;

    String data1="";
    String data2="";

    int tempLOG=0;
    int tempturnnum=0;
    TextView nexthuman;
    TextView nowhuman;

    private PopupWindow mPopupWindow;

    MenuItem menuItem;

    TextView roomidView;

    View viewroomc;

    String nowplayingnumber;
    String nowplayingname;
    String nowplayingartist;

    TextView songView;
    TextView artistView;

    int memberID=-1;
    String photoUrl="nourl";

    ImageView nowuserimage;
    ImageView nextuserimage;
    ImageView artWorkimage;
    ImageView selectImageView;

    ImageButton recommendImageView1;
    ImageButton recommendImageView2;
    ImageButton recommendImageView3;
    ImageButton recommendImageView4;

    Resources r;
    Bitmap bmpartworknone;
    Bitmap bmpartworknoneR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mApp = (MyApplication) this.getApplication();
        mApp.setCount(10);

        Intent intent = getIntent();
        data1 = intent.getStringExtra("DATA1");
        data2=intent.getStringExtra("DATA2");
        if(data1!=""){
            userName=data1;

        }
        mApp.setUserName(userName);
        Log.d("data1",data1);
        Log.d("data2",data2);
        if(data2!=""){
            roomid=data2;
            //roomid="room23";
        }
        mApp.setRoomid(roomid);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            //String name = user.getDisplayName();
            //String email = user.getEmail();
            photoUrl = user.getPhotoUrl().toString();
            mApp.setPhotoURL(photoUrl);
            Log.d("","url:"+photoUrl);
            // Check if user's email is verified

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            String uid = user.getUid();
        }

        //setContentView(R.layout.activity_main2);
        setContentView(R.layout.music_union);
        mContentView= findViewById(R.id.fullscreen_union);


        /*
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                */
        /*
        String s1 = "Stringクラスは文字列を表します。";
        String s4 = "クラス";

        if (s1.matches(".*" + s4 + ".*")) {
            // 部分一致です

        }
        else {
            // 部分一致ではありません
        }
        */

        mContentView2= findViewById(R.id.fullscreen_union_list);

        mContentView3=findViewById(R.id.fullscreen_union_prelist);

        mContentViewNONE=findViewById(R.id.fullscreen_content2);

        //menuItem=(MenuItem)findViewById(R.id.menu_tv);
        //menuItem.setOnMenuItemClickListener(this);
        /*
        mContentView2.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                */


        button_start=(ImageButton) findViewById(R.id.imagePlay);
        button_start.setOnClickListener(this);

        button_next=(ImageButton)findViewById(R.id.imageNext);
        button_next.setOnClickListener(this);



        nexthuman=(TextView)findViewById(R.id.nextHuman);
        nowhuman=(TextView)findViewById(R.id.playingHuman);

        songView=(TextView)findViewById(R.id.songView);
        artistView=(TextView)findViewById(R.id.artistView);

        artWorkimage=(ImageView)findViewById(R.id.artwork);

        button_select=(Button)findViewById(R.id.selectMusicButton);
        button_select.setOnClickListener(this);

        button_select_keyword=(Button)findViewById(R.id.selectKey);
        button_select_keyword.setOnClickListener(this);

        recommendTitleView1=(TextView)findViewById(R.id.recommendTitleView1);
        recommendTitleView2=(TextView)findViewById(R.id.recommendTitleView2);
        recommendTitleView3=(TextView)findViewById(R.id.recommendTitleView3);
        recommendTitleView4=(TextView)findViewById(R.id.recommendTitleView4);

        recommendArtistView1=(TextView)findViewById(R.id.recommendArtistView1);
        recommendArtistView2=(TextView)findViewById(R.id.recommendArtistView2);
        recommendArtistView3=(TextView)findViewById(R.id.recommendArtistView3);
        recommendArtistView4=(TextView)findViewById(R.id.recommendArtistView4);

        selectKeyWord=(TextView)findViewById(R.id.keyWordName);

        //roomidView=(TextView)findViewById(R.id.roomid_bar);


        /*

        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mHideHandler.postDelayed(mHidePart2Runnable,UI_ANIMATION_DELAY);
                Log.d("","aaaaaa");

                mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

            }
        });
        */
        Time time = new Time("Asia/Tokyo");
        time.setToNow();
        //String date = time.year + "-" + (time.month+1) + "-" + time.monthDay + "-"+time.hour + "-" + time.minute + "-" + time.second;

        yeard=time.year;
        monthd=time.month+1;
        dayd=time.monthDay;
        hourd=time.hour;
        minuted=time.minute;
        secondd=time.second;
        plObject = new JSONObject[0];

        //room();


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
        Log.i("musicList",musicList);
        //hpa=new HttpPostAsync();
        // apiのurl
        //url = "http://masayuki.nkmr.io/ourMusicList/android_test.php";
        //queue = Volley.newRequestQueue(this);
        //mTextView = (TextView) findViewById(R.id.text2);
        //firstTex=mTextView.getText().toString();
        Log.d("FT",""+firstTex);
        //mTextView.setText("aaa");


        selection = "title='" + filter + "'";
        Log.d("result3",selection);

        database2 = FirebaseDatabase.getInstance();
        databases = FirebaseDatabase.getInstance();
    }
    int countM=0;
    ArrayAdapter<String> adapter;
    ArrayList<Album> list;
    AlbumAdapter adapterapi;

    ArrayAdapter<String> adapterp;
    ArrayList<Album> listPre;
    AlbumAdapter adapterPre;

    // ハンドラを生成
    Handler handler = new Handler();
    ListView listView2;
    ListView listViewPre;

    Bitmap bmpping;
    Bitmap bmpnone;
    Bitmap bmpnonesel;

    String recommendImageURIText1="";
    String recommendImageURIText2="";
    String recommendImageURIText3="";
    String recommendImageURIText4="";


    @Override
    public void onResume(){
        super.onResume();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        r = getResources();

        bmpping = BitmapFactory.decodeResource(r, R.drawable.otherplaying);
        //bmpping = Bitmap.createScaledBitmap(bmpping, 100, 100, false);
        bmpnone = BitmapFactory.decodeResource(r, R.drawable.artworknone);
        bmpnone = Bitmap.createScaledBitmap(bmpnone, 390, 390, false);
        bmpnonesel = BitmapFactory.decodeResource(r, R.drawable.artworknone);
        bmpnonesel = Bitmap.createScaledBitmap(bmpnone, 100, 100, false);


        Log.d("", "room" + roomid + ",user" + userName);

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        mysamref=database.getReference("room");

        listPre = new ArrayList<>();
        adapterPre = new AlbumAdapter(KeyAnswerActivity.this);
        adapterPre.setAlbumList(listPre);
        listViewPre=(ListView)findViewById(R.id.previousListView);
        listViewPre.setAdapter(adapterPre);

        nowuserimage = (ImageView) findViewById(R.id.playingUserImage);
        nextuserimage = (ImageView) findViewById(R.id.nextUserImage);

        recommendImageView1=(ImageButton)findViewById(R.id.recommendImageView1);
        recommendImageView2=(ImageButton)findViewById(R.id.recommendImageView2);
        recommendImageView3=(ImageButton)findViewById(R.id.recommendImageView3);
        recommendImageView4=(ImageButton)findViewById(R.id.recommendImageView4);
        recommendImageView1.setImageBitmap(bmpnone);
        recommendImageView2.setImageBitmap(bmpnone);
        recommendImageView3.setImageBitmap(bmpnone);
        recommendImageView4.setImageBitmap(bmpnone);

        bmpartworknone = BitmapFactory.decodeResource(r, R.drawable.artworknone);
        bmpartworknoneR= Bitmap.createScaledBitmap(bmpartworknone, 300, 300, false);


        selectMusicView=(TextView)findViewById(R.id.selectMusic);
        selectArtistView=(TextView)findViewById(R.id.selectArtist);

        recommendMusicView=(TextView)findViewById(R.id.recommendMusic);
        recommendArtistView=(TextView)findViewById(R.id.recommendArtist);

        selectImageView=(ImageView) findViewById(R.id.selectImageVIew);
        selectImageView.setImageBitmap(bmpnonesel);

        recommendImageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mHideHandler.postDelayed(mHidePart2Runnable,UI_ANIMATION_DELAY);
                selectMusicView.setText(recommendTitleView1.getText());
                selectArtistView.setText(recommendArtistView1.getText());
                if(bitmapRecommend1!=null) {
                    selectImageView.setImageBitmap(bitmapRecommend1);
                }else{
                    selectImageView.setImageBitmap(bmpnonesel);
                }
                mApp.setSelectTitle(recommendTitleView1.getText().toString());
                mApp.setSelectArtist(recommendArtistView1.getText().toString());
            }
        });
        recommendImageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mHideHandler.postDelayed(mHidePart2Runnable,UI_ANIMATION_DELAY);
                selectMusicView.setText(recommendTitleView2.getText());
                selectArtistView.setText(recommendArtistView2.getText());
                //selectImageView.setImageBitmap(bitmapRecommend2);
                mApp.setSelectTitle(recommendTitleView2.getText().toString());
                mApp.setSelectArtist(recommendArtistView2.getText().toString());
                if(bitmapRecommend2!=null) {
                    selectImageView.setImageBitmap(bitmapRecommend2);
                }else{
                    selectImageView.setImageBitmap(bmpnonesel);
                }

            }
        });
        recommendImageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mHideHandler.postDelayed(mHidePart2Runnable,UI_ANIMATION_DELAY);
                selectMusicView.setText(recommendTitleView3.getText());
                selectArtistView.setText(recommendArtistView3.getText());
                //selectImageView.setImageBitmap(bitmapRecommend3);

                mApp.setSelectTitle(recommendTitleView3.getText().toString());
                mApp.setSelectArtist(recommendArtistView3.getText().toString());
                if(bitmapRecommend3!=null) {
                    selectImageView.setImageBitmap(bitmapRecommend3);
                }else{
                    selectImageView.setImageBitmap(bmpnonesel);
                }

            }
        });
        recommendImageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mHideHandler.postDelayed(mHidePart2Runnable,UI_ANIMATION_DELAY);
                selectMusicView.setText(recommendTitleView4.getText());
                selectArtistView.setText(recommendArtistView4.getText());
                //selectImageView.setImageBitmap(bitmapRecommend4);

                mApp.setSelectTitle(recommendTitleView4.getText().toString());
                mApp.setSelectArtist(recommendArtistView4.getText().toString());
                if(bitmapRecommend4!=null) {
                    selectImageView.setImageBitmap(bitmapRecommend4);
                }else{
                    selectImageView.setImageBitmap(bmpnonesel);
                }

            }
        });
        //imageView.setImageBitmap(qrCodeBitmap);



        /*
        room();
        member();
        turn();

        //ここをコメントアウトすると多重再生が治る
        status();
        musicListFunc();


        playlist();
        reserveList();
        */
        onStartService();




        list = new ArrayList<>();
        adapterapi = new AlbumAdapter(KeyAnswerActivity.this);
        adapterapi.setAlbumList(list);
        listView2=(ListView)findViewById(R.id.listView3);
        listView2.setAdapter(adapterapi);


        /*


        mContentView2=findViewById(R.id.fullscreen_content2);
        mContentView2.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                */
        viewC=2;
/*
        mContentView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mHideHandler.postDelayed(mHidePart2Runnable,UI_ANIMATION_DELAY);
                Log.d("","aaaaaa");

                mContentView2.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

            }
        });
        */

        //このループ内からsetTextしてはいけない！
        loopC.submit(new Runnable() {
            @Override
            public void run() {


                while (true) {

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            songView.setText(mApp.getPlayingTitle());
                            artistView.setText(mApp.getPlayingArtist());
                            if(mApp.getRoomNumber()=="1") {
                                uri = Uri.parse(mApp.getNowHumanURI());
                                Uri.Builder builder = uri.buildUpon();
                                task = new AsyncTaskHttpRequest(nowuserimage);
                                task.execute(builder);

                                urinext = Uri.parse(mApp.getNowHumanURI());
                                Uri.Builder buildernext = urinext.buildUpon();
                                tasknext = new AsyncTaskHttpRequest(nextuserimage);
                                tasknext.execute(buildernext);
                            }else if(mApp.getRoomNumber()!="0"){
                                uri = Uri.parse(mApp.getNowHumanURI());
                                Uri.Builder builder = uri.buildUpon();
                                task = new AsyncTaskHttpRequest(nowuserimage);
                                task.execute(builder);

                                urinext = Uri.parse(mApp.getNextHumanURI());
                                Uri.Builder buildernext = urinext.buildUpon();
                                tasknext = new AsyncTaskHttpRequest(nextuserimage);
                                tasknext.execute(buildernext);

                            }
                            selectKeyWord.setText(mApp.getKeyWord());


                            selectMusicView.setText(mApp.getSelectTitle());
                            selectArtistView.setText(mApp.getSelectArtist());
                            recommendTitleView1.setText(mApp.getRecommendTitleText1());
                            recommendArtistView1.setText(mApp.getRecommendArtistText1());
                            recommendTitleView2.setText(mApp.getRecommendTitleText2());
                            recommendArtistView2.setText(mApp.getRecommendArtistText2());
                            recommendTitleView3.setText(mApp.getRecommendTitleText3());
                            recommendArtistView3.setText(mApp.getRecommendArtistText3());
                            recommendTitleView4.setText(mApp.getRecommendTitleText4());
                            recommendArtistView4.setText(mApp.getRecommendArtistText4());
                            path2=mApp.getPlayingImage();
                            Log.d("","path2222"+path2);
                            if(path2!=""&&path2!=null) {
                                bitmap2=BitmapFactory.decodeFile(path2);
                                artWorkimage.setImageBitmap(bitmap2);
                            }else if(nowhuman.getText()!=userName){

                                artWorkimage.setImageBitmap(bmpping);
                            }else{

                                artWorkimage.setImageBitmap(bmpnone);
                            }
                            Log.d("","recoimageuri:"+mApp.getRecommendImageURIText1());

                            recommendImageURIText1=mApp.getRecommendImageURIText1();
                            recommendImageURIText2=mApp.getRecommendImageURIText2();
                            recommendImageURIText3=mApp.getRecommendImageURIText3();
                            recommendImageURIText4=mApp.getRecommendImageURIText4();

                            if(recommendImageURIText1==""||recommendImageURIText1==null){
                                recommendImageView1.setImageBitmap(bmpnone);
                            }
                            if(recommendImageURIText2==""||recommendImageURIText2==null){
                                recommendImageView2.setImageBitmap(bmpnone);
                            }
                            if(recommendImageURIText3==""||recommendImageURIText3==null){
                                recommendImageView3.setImageBitmap(bmpnone);
                            }
                            if(recommendImageURIText4==""||recommendImageURIText4==null){
                                recommendImageView4.setImageBitmap(bmpnone);
                            }



                            //ftextView1.setText(adapter.getItem(selectcount));
                        }
                    });


                    //selectcount++;
                    //textView1.setText(adapter.getItem(selectcount));
                    // TODO Auto-generated method stub




                    //Log.d("turn","ping:"+turnping+",phum:"+turnphum+",pnum:"+turnpnum+",nexthum:"+nexthum);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                    }


                }

            }
        });


        SeekBar seekBar2 = (SeekBar) findViewById(R.id.seekBar2);
        seekBar2.setMax(100);
        seekBar2.setProgress(30);
        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            // トラッキング開始時に呼び出されます
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.v("onStartTrackingTouch()",
                        String.valueOf(seekBar.getProgress()));
                trackingFlag=1;
            }

            // トラッキング中に呼び出されます
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
                Log.v("onProgressChanged()",
                        String.valueOf(progress) + ", " + String.valueOf(fromTouch));

            }

            // トラッキング終了時に呼び出されます
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.v("onStopTrackingTouch()",
                        String.valueOf(seekBar.getProgress()));
                if (m != null) {
                    if(m.isPlaying()==true) {
                        tempDuration = m.getDuration();
                        if (seekBar.getProgress() != 0) {
                            tempSeek = (int) (tempDuration / 100 * seekBar.getProgress());
                            //Log.d("tempSeek", "tempSeek:" + tempSeek);
                            m.seekTo(tempSeek);
                            trackingFlag=0;
                        }
                    }
                }
            }
        });
        if(nowView==0) {

            mContentView.setVisibility(View.VISIBLE);
            mContentView2.setVisibility(View.INVISIBLE);
            mContentView3.setVisibility(View.INVISIBLE);
            mContentViewNONE.setVisibility(View.INVISIBLE);
        }else if(nowView==1){

            //mTextMessage.setText(R.string.title_home);
            mContentView.setVisibility(View.INVISIBLE);
            mContentView2.setVisibility(View.VISIBLE);
            mContentView3.setVisibility(View.INVISIBLE);
            mContentViewNONE.setVisibility(View.INVISIBLE);

        }else if(nowView==2){

                mContentView.setVisibility(View.INVISIBLE);
                mContentView2.setVisibility(View.INVISIBLE);
                mContentView3.setVisibility(View.VISIBLE);
                mContentViewNONE.setVisibility(View.INVISIBLE);

        }else if(nowView==3){

                mContentView.setVisibility(View.INVISIBLE);
                mContentView2.setVisibility(View.INVISIBLE);
                mContentView3.setVisibility(View.INVISIBLE);
                mContentViewNONE.setVisibility(View.VISIBLE);

        }




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



    public void musicListFunc(){
        /*
        listF = new ArrayList<>();
        adapterFapi = new AlbumAdapter(KeyAnswerActivity.this);
        adapterFapi.setAlbumList(listF);
        musicListView=(ListView)findViewById(R.id.musicListView);
        musicListView.setAdapter(adapterFapi);


        mItems = MusicItem.getItems(getApplicationContext());
        //MusicItem mItems2 =mItems.get(1);
        //Log.i("ItemList",""+mItems.size());

        textView1 = (TextView)findViewById(R.id.textView10);
        String mttempName="";
        String mttempArtist="";
        int mttempInt=0;
        for(int i=0;i<10;i++){

            mttempInt=(int)(Math.random()*30);
            mttempName=mItems.get(mttempInt).title;
            mttempArtist=mItems.get(mttempInt).artist;
            //adapter.add(mttempName);


            Album albuminsF = new Album();
            //albumins.setName("HogeFuga");
            //albumins.setArtist("あいうえおかきくけこ");
            albuminsF.setName(mttempName);
            albuminsF.setArtist(mttempArtist);

            listF.add(albuminsF);

        }
        adapterFapi.notifyDataSetChanged();
        */

        mItems = MusicItem.getItems(getApplicationContext());
        //MusicItem mItems2 =mItems.get(1);
        //Log.i("ItemList",""+mItems.size());

        textView1 = (TextView)findViewById(R.id.textView10);
        String mttempName="";
        String mttempArtist="";
        int mttempInt=0;
        for(int i=0;i<10;i++){

            mttempInt=(int)(Math.random()*mItems.size());
            mttempName=mItems.get(mttempInt).title;
            mttempArtist=mItems.get(mttempInt).artist;
            //adapter.add(mttempName);


            Album albuminsF = new Album();
            //albumins.setName("HogeFuga");
            //albumins.setArtist("あいうえおかきくけこ");
            albuminsF.setName(mttempName);
            albuminsF.setArtist(mttempArtist);

            if(i==0){
                recommendTitleView1.setText(mttempName);
                recommendArtistView1.setText(mttempArtist);

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

                //path2=path2.getPath();
                /*
                Log.d("paaaaath2","paaaaaaaaaat:"+path2+","+uri);

                int albumDdIndex = cursor.getColumnIndex
                        (MediaStore.Audio.AudioColumns.ALBUM_ID);
                Log.d("paaaaath2","albumIndex:"+albumDdIndex);

                ContentResolver contentResolver1 = getApplicationContext().getContentResolver();
                Uri uri1 = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
                String[] projection1 = new String[]{MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART};
                String selection1 = MediaStore.Audio.Albums._ID + " = ?";
                String[] selectionArgs1 = new String[]{String.valueOf(albumDdIndex)};  //albumId is MediaStore.Audio.Media.ALBUM_ID

                Cursor cursor1 = contentResolver1.query(uri1, projection1, selection1, selectionArgs1, null);

                if (cursor1 != null) {
                    if (cursor1.moveToFirst()) {
                        String albumPath = cursor1.getString(cursor1.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
                        if (albumPath != null)
                            //Bitmap album_art = BitmapFactory.decodeStream(albumPath);
                            //selectImageView.setImageBitmap(album_art);
                            albumArt = BitmapFactory.decodeFile(albumPath);
                            selectImageView.setImageBitmap(albumArt);
                    }
                    cursor1.close();
                }
                */
                /*
                BitmapFactory.Options options = new BitmapFactory.Options();
                try {

                    final Uri sArtworkUri = Uri
                            .parse("content://media/external/audio/albumart");

                    Uri uriX = ContentUris.withAppendedId(sArtworkUri, albumDdIndex);
                    Log.d("paaaaath2","albumIndex:"+uriX);
                    try{
                        ContentResolver crT = getContentResolver();
                        InputStream is = crT.openInputStream(uriX);
                        Bitmap albumArt = BitmapFactory.decodeStream(is);
                        Log.d("paaaaath2","paaaaaaaaaath2"+albumArt.getWidth());
                    }catch(FileNotFoundException err){
                        err.printStackTrace();
                    }

                    ParcelFileDescriptor pfd = getApplicationContext().getContentResolver().openFileDescriptor(uriX, "r");
                    Log.d("paaaaath2","pfd:"+pfd);

                    if (pfd != null) {
                        FileDescriptor fd = pfd.getFileDescriptor();
                        Bitmap album_art = BitmapFactory.decodeFileDescriptor(fd, null,
                                options);
                        selectImageView.setImageBitmap(album_art);
                        Log.d("paaaaath2","paaaaaaaaaath2"+album_art.getWidth());
                        pfd = null;
                        fd = null;
                    }
                } catch (Error ee) {
                } catch (Exception e) {
                }
                */
                /*
                if(cursor != null){
                    //while(!cursor2.isAfterLast()){

                        int titleIndex =  cursor.getColumnIndex
                                (MediaStore.Audio.AudioColumns.TITLE);
                        int albumDdIndex = cursor.getColumnIndex
                                (MediaStore.Audio.AudioColumns.ALBUM_ID);
                        Log.d("paaaaath2","titleIndex:"+titleIndex);


                        String title = cursor.getString(titleIndex);
                        String albumIdStr = cursor.getString(albumDdIndex);
                        long albumId = Long.parseLong(albumIdStr);
                        Uri albumArtUri = Uri.parse(
                                "content://media/external/audio/albumart");
                        Uri albumUri = ContentUris.withAppendedId(albumArtUri, albumId);
                        //ジャケット画像のURIを取得
                        ContentResolver crK = getContentResolver();
                        try{
                            InputStream is = crK.openInputStream(albumUri);
                            Bitmap album_art = BitmapFactory.decodeStream(is);
                            selectImageView.setImageBitmap(album_art);
                            //URIからストリームを取得してそれを画像に変換
                        }catch(FileNotFoundException e) {
                            //ストリームが開けなかったときの処理
                        }

                   // }
                }
                */



                // try {
                if(path2!=null) {
                    File file = new File(path2);
                    Uri uriJ = Uri.fromFile(file);
                    Log.d("paaaaath2","fileURI:"+file+","+uriJ);
                }
                //Uri uriJ = Uri.fromFile(file);
                //Log.d("paaaaath2","paaaaaaaaaat:"+path2);
                //InputStream stream = new FileInputStream(file);
                //Bitmap bitmap = BitmapFactory.decodeStream(new BufferedInputStream(stream));
                //selectImageView.setImageBitmap(bitmap);
                //} catch (FileNotFoundException e) {
                //}


                bitmap2=BitmapFactory.decodeFile(path2);
                bitmap2=Bitmap.createScaledBitmap(bitmap2, 100, 100, false);
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
            }else if(i==1){
                recommendTitleView2.setText(mttempName);
                recommendArtistView2.setText(mttempArtist);

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
                //Log.d("paaaaath2","paaaaaaaaaath2"+path2);
                bitmap2=BitmapFactory.decodeFile(path2);
                bitmap2=Bitmap.createScaledBitmap(bitmap2, 100, 100, false);
                //Log.d("paaaaath2","paaaaaaaaaath2"+bitmap2.getWidth());
                bitmapRecommend2=bitmap2;
                //recommendImageView2.setImageBitmap(bitmap2);
                if(bitmap2!=null) {
                    recommendImageView2.setImageBitmap(bitmap2);
                }else{
                    recommendImageView2.setImageBitmap(bmpartworknoneR);
                }
            }else if(i==2){
                recommendTitleView3.setText(mttempName);
                recommendArtistView3.setText(mttempArtist);
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
                //Log.d("paaaaath2","paaaaaaaaaath2"+path2);
                bitmap2=BitmapFactory.decodeFile(path2);
                bitmap2=Bitmap.createScaledBitmap(bitmap2, 100, 100, false);
                bitmapRecommend3=bitmap2;
                //recommendImageView3.setImageBitmap(bitmap2);
                if(bitmap2!=null) {
                    recommendImageView3.setImageBitmap(bitmap2);
                }else{
                    recommendImageView3.setImageBitmap(bmpartworknoneR);
                }
            }else if(i==3){
                recommendTitleView4.setText(mttempName);
                recommendArtistView4.setText(mttempArtist);
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
                //Log.d("paaaaath2","paaaaaaaaaath2"+path2);
                bitmap2=BitmapFactory.decodeFile(path2);
                bitmap2=Bitmap.createScaledBitmap(bitmap2, 100, 100, false);
                bitmapRecommend4=bitmap2;
                //recommendImageView4.setImageBitmap(bitmap2);
                if(bitmap2!=null) {
                    recommendImageView4.setImageBitmap(bitmap2);
                }else{
                    recommendImageView4.setImageBitmap(bmpartworknoneR);
                }
            }

            //listF.add(albuminsF);

        }




    }

    public void recommendListFunc(View v){


        listR = new ArrayList<>();
        adapterRapi = new AlbumAdapter(KeyAnswerActivity.this);
        adapterRapi.setAlbumList(listR);
        recommendListView=(ListView)v.findViewById(R.id.recommendListView);
        recommendListView.setAdapter(adapterRapi);
        //recommendListView.setCacheColorHint();
        // 追加するアイテムを生成する


        if(list.size()==0) {
            mItems = MusicItem.getItems(getApplicationContext());

            String mttempName = "";
            String mttempArtist = "";
            int mttempInt = 0;
            for (int i = 0; i < 10; i++) {

                mttempInt = (int) (Math.random() * 100);
                mttempName = mItems.get(mttempInt).title;
                mttempArtist = mItems.get(mttempInt).artist;
                if(i==0){
                    selectMusicView.setText(mttempName);
                    selectArtistView.setText(mttempArtist);
                    //selectImageView.setImageBitmap(bitmapRecommend1);
                }


                Album albuminsR = new Album();
                //albumins.setName("HogeFuga");
                //albumins.setArtist("あいうえおかきくけこ");
                albuminsR.setName(mttempName);
                albuminsR.setArtist(mttempArtist);


                listR.add(albuminsR);

            }

            adapterRapi.notifyDataSetChanged();
        }else{
            //mItems = MusicItem.getItems(getApplicationContext());

            String mttempName = "";
            String mttempArtist = "";
            int mttempInt = 0;
            for (int i = 0; i < list.size(); i++) {


                //mttempInt = ;
                mttempName = list.get(i).getName();
                mttempArtist = list.get(i).getArtist();


                // 部分一致です
                Album albuminsR = new Album();
                //albumins.setName("HogeFuga");
                //albumins.setArtist("あいうえおかきくけこ");
                albuminsR.setName(mttempName);
                albuminsR.setArtist(mttempArtist);

                listR.add(albuminsR);


            }
            adapterRapi.notifyDataSetChanged();
        }

        // アイテムクリック時ののイベントを追加
        recommendListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent,
                                    View view, int pos, long id) {

                // 選択アイテムを取得
                ListView listView = (ListView)parent;
                //String item = (String)listView.getItemAtPosition(pos);
                Album item=(Album) listView.getItemAtPosition(pos);

                // 通知ダイアログを表示
                Toast.makeText(KeyAnswerActivity.this,
                        item.getName(), Toast.LENGTH_LONG
                ).show();
                selectMusicView.setText(item.getName());
                selectArtistView.setText(item.getArtist());
                insertFlag=1;


            }
        });
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

                /*
                listPre = new ArrayList<>();
                adapterPre = new AlbumAdapter(KeyAnswerActivity.this);
                adapterPre.setAlbumList(listPre);
                listViewPre=(ListView)findViewById(R.id.previousListView);
                listViewPre.setAdapter(adapterPre);
                */
                /*
                String mttempName="";
                String mttempArtist="";
                int mttempInt=0;
                for(int i=0;i<10;i++){
                    mttempInt=(int)(Math.random()*30);
                    mttempName=mItems.get(mttempInt).title;
                    mttempArtist=mItems.get(mttempInt).artist;


                    Album albuminsR = new Album();
                    //albumins.setName("HogeFuga");
                    //albumins.setArtist("あいうえおかきくけこ");
                    albuminsR.setName(mttempName);
                    albuminsR.setArtist(mttempArtist);

                    listPre.add(albuminsR);


                }
                */
                /*

                if(dataSnapshot.child(roomid).child("playlist").getValue()!=null){
                    if(dataSnapshot.child(roomid).child("playingnumber").getValue().toString()!="0") {
                        playList = new String [Integer.parseInt(dataSnapshot.child(roomid).child("playingnumber").getValue().toString())][2];
                        int tempInt3=0;
                        if(dataSnapshot.child(roomid).child("playingnumber").getValue()!=null) {
                            tempInt3 = Integer.parseInt(dataSnapshot.child(roomid).child("playingnumber").getValue().toString());
                        }
                        //Log.d("nummmmm","nummmmmmmm:"+Integer.parseInt(dataSnapshot.child(roomid).child("playingnumber").getValue().toString()));
                        for(int i=0;i<tempInt3;i++){
                            playList[i][0]=dataSnapshot.child(roomid).child("playlist").child(Integer.toString(i)).child("name").getValue().toString();
                            playList[i][1]=dataSnapshot.child(roomid).child("playlist").child(Integer.toString(i)).child("artist").getValue().toString();
                            Album albuminsP = new Album();
                            //albumins.setName("HogeFuga");
                            //albumins.setArtist("あいうえおかきくけこ");
                            albuminsP.setName(playList[i][0]);
                            albuminsP.setArtist(playList[i][1]);

                            listPre.add(albuminsP);
                        }
                        if(tempInt3!=0) {
                            getTest4(playList[tempInt3 - 1][0], playList[tempInt3 - 1][1]);
                            //selectMusicView.setText(list.get(0).getName());
                            //selectArtistView.setText(list.get(0).getArtist());
                        }
                        //selectMusicView.setText();
                    }
                }
                adapterPre.notifyDataSetChanged();
                */
                ///Log.d("","Change");
                /*

                if(dataSnapshot.child(roomid).child("playing").getValue()!=null&&dataSnapshot.child(roomid).child("playlist").getValue()!=null){
                    nowplayingnumber=dataSnapshot.child(roomid).child("playingnumber").getValue().toString();
                    Log.d("nownumber",nowplayingnumber);
                    if(dataSnapshot.child(roomid).child("playing").getValue().toString()=="true"&&dataSnapshot.child(roomid).child("playlist").child(nowplayingnumber).child("name").getValue()!=null){
                        if(dataSnapshot.child(roomid).child("playlist").child(nowplayingnumber).child("name").getValue().toString()!="") {

                            nowplayingname = dataSnapshot.child(roomid).child("playlist").child(nowplayingnumber).child("name").getValue().toString();
                            nowplayingartist = dataSnapshot.child(roomid).child("playlist").child(nowplayingnumber).child("artist").getValue().toString();
                            Log.d("", nowplayingnumber + "," + nowplayingartist + "," + nowplayingname);
                        }
                    }

                }
                */




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
    ValueEventListener velplaylist;
    String playlistarr[][]=new String [10][2];
    int i=0;

    public void playlist(){


        velplaylist=new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot){

                i=0;
                listPre = new ArrayList<>();
                adapterPre = new AlbumAdapter(KeyAnswerActivity.this);
                adapterPre.setAlbumList(listPre);
                listViewPre=(ListView)findViewById(R.id.previousListView);
                listViewPre.setAdapter(adapterPre);

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
                    adapterPre.notifyDataSetChanged();
                    songView.setText(listPre.get(i-1).getName());
                    artistView.setText(listPre.get(i-1).getArtist());
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


    int enteriplay=0;
    int humancountmax=0;
    Album insertAlbum;
    Bitmap bitmap2;
    Bitmap bitmapRecommend1;
    Bitmap bitmapRecommend2;
    Bitmap bitmapRecommend3;
    Bitmap bitmapRecommend4;
    String path2="";
    public void iplay(String title){
        Log.d("","enteriplay"+enteriplay);
        if(enteriplay==0) {
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
            bitmap2=BitmapFactory.decodeFile(path2);

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
    //やること複数人におけるターン処理
    public void turn(){

        velturn=new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                turnping=String.valueOf(dataSnapshot.child(roomid).child("playing").getValue());
                turnphum=String.valueOf(dataSnapshot.child(roomid).child("playinghuman").getValue());
                turnpnum=String.valueOf(dataSnapshot.child(roomid).child("playingnumber").getValue());

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


                    if (memnum.equals("1")) {
                        Log.d("memnum2",memnum);
                        nowMember=dataSnapshot.child(roomid).child("member").child(String.valueOf(tempturn)).getValue(Member.class);
                        nowhum = String.valueOf(dataSnapshot.child(roomid).child("member").child(String.valueOf(tempturn)).child("username").getValue());
                        nowhuman.setText(nowhum);
                        //Uri uri = Uri.parse("http://cdn-ak.f.st-hatena.com/images/fotolife/f/fjswkun/20150927/20150927140905.jpg");
                        uri = Uri.parse(nowMember.imageuri);
                        Uri.Builder builder = uri.buildUpon();
                        task = new AsyncTaskHttpRequest(nowuserimage);
                        task.execute(builder);

                        nextMember=dataSnapshot.child(roomid).child("member").child(String.valueOf(1)).getValue(Member.class);
                        nexthum = String.valueOf(dataSnapshot.child(roomid).child("member").child("1").child("username").getValue());
                        nexthuman.setText(nexthum);
                        //nextuserimage.setImageBitmap(getBitmapFromURL("http://icons.iconarchive.com/icons/laurent-baumann/neige/128/HTTP-icon.png"));
                        //Uri urinext = Uri.parse("http://cdn-ak.f.st-hatena.com/images/fotolife/f/fjswkun/20150927/20150927140905.jpg");
                        urinext = Uri.parse(nextMember.imageuri);
                        Uri.Builder buildernext = urinext.buildUpon();
                        tasknext = new AsyncTaskHttpRequest(nextuserimage);
                        tasknext.execute(buildernext);

                    } else {
                        nowMember=dataSnapshot.child(roomid).child("member").child(String.valueOf(tempturn)).getValue(Member.class);
                        nowhum = String.valueOf(dataSnapshot.child(roomid).child("member").child(String.valueOf(tempturn)).child("username").getValue());
                        nowhuman.setText(nowhum);
                        //nowuserimage.setImageURI(Uri.parse(nowMember.imageuri));
                        uri = Uri.parse(nowMember.imageuri);
                        Uri.Builder builder = uri.buildUpon();
                        task = new AsyncTaskHttpRequest(nowuserimage);
                        task.execute(builder);


                        tempturn=((Integer.parseInt(turnpnum)+1)%(Integer.parseInt(memnum)))+1;
                        nextMember=dataSnapshot.child(roomid).child("member").child(String.valueOf(tempturn)).getValue(Member.class);
                        nexthum = String.valueOf(dataSnapshot.child(roomid).child("member").child(String.valueOf(tempturn)).child("username").getValue());
                        nexthuman.setText(nexthum);
                        //nextuserimage.setImageURI(Uri.parse(nextMember.imageuri));
                        if(nextMember.imageuri!="null") {
                            urinext = Uri.parse(nextMember.imageuri);
                            Uri.Builder buildernext = urinext.buildUpon();
                            tasknext = new AsyncTaskHttpRequest(nextuserimage);
                            tasknext.execute(buildernext);
                        }

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
    //やること複数人におけるターン処理
    public void status(){

        velstatus=new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                statusping=String.valueOf(dataSnapshot.child(roomid).child("playing").getValue());
                statusphum=String.valueOf(dataSnapshot.child(roomid).child("playinghuman").getValue());
                statuspnum=String.valueOf(dataSnapshot.child(roomid).child("playingnumber").getValue());


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

    int selectcount=0;
    int flagstart=0;

    int insertFlag=0;
    // 識別用のコード
    private final static int CHOSE_FILE_CODE = 12345;
    static final int REQUEST_IMAGE_GET = 1;
    static final int REQUEST_AUDIO_GET=310;

    public void onClick(View v) {

        if(v==button_segue){

            if(velroom!=null) {
                mysamref.removeEventListener(velroom);
            }
            if(velturn!=null) {
                mysamrefs.removeEventListener(velturn);
            }
            finish();
        }
        if(v==button_start){
            /*
            if(!selectMusicView.getText().toString().equals("未選択")) {
                iplay(selectMusicView.getText().toString());
                getTest4(selectMusicView.getText().toString(),selectArtistView.getText().toString());
                selectcount++;
                if(path2!="") {
                    artWorkimage.setImageBitmap(bitmap2);
                }

                selectMusicView.setText("未選択");
                selectArtistView.setText("未選択");
                flagstart = 2;
            }
            */
            mApp.setStartBFlag(1);
        }
        if(v==button_next){
            /*
            if(m!=null){
                m.stop();
            }
            */
            mApp.setStopBFlag(1);
        }
        if(v==button_api){
            Log.d("","aiueopause");
            //getTest3();
        }
        if(v==button_select){
            mPopupWindow = new PopupWindow(KeyAnswerActivity.this);

            // レイアウト設定
            View popupView = getLayoutInflater().inflate(R.layout.popupwindow, null);
            //getTest4(selectMusicView.getText().toString(),selectArtistView.getText().toString());
            recommendListFunc(popupView);
            popupView.findViewById(R.id.close_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mPopupWindow.isShowing()) {
                        mPopupWindow.dismiss();
                        if(insertFlag==1) {
                            Reserve tempreserve = new Reserve();
                            tempreserve.setArtist(selectArtistView.getText().toString());
                            tempreserve.setTitle(selectMusicView.getText().toString());
                            tempreserve.setUserid(memberID);
                            mysamref.child(roomid).child("reserve").child(Integer.toString(memberID)).child(Integer.toString(reserveCount)).setValue(tempreserve);
                            insertFlag=0;
                        }
                    }
                }
            });
            mPopupWindow.setContentView(popupView);

            // 背景設定
            mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.popup_background));


            // タップ時に他のViewでキャッチされないための設定
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setFocusable(true);

            // 表示サイズの設定 今回は幅300dp
            float width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, getResources().getDisplayMetrics());
            mPopupWindow.setWindowLayoutMode((int) width, WindowManager.LayoutParams.WRAP_CONTENT);
            mPopupWindow.setWidth((int) width);
            mPopupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

            // 画面中央に表示
            mPopupWindow.showAtLocation(findViewById(R.id.selectMusicButton), Gravity.CENTER, 0, 0);
        }
        if(v==button_select_keyword){
            /*
            Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT,false);
            startActivityForResult(intent,REQUEST_CODE_RINGTONE_PICKER);
            */

            /*
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("/mnt/sdcard/*");
            startActivityForResult(intent, CHOSE_FILE_CODE);
            */
            /*
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent, REQUEST_IMAGE_GET);
            }
            */
            Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i,REQUEST_AUDIO_GET);

        }

    }
    int REQUEST_CODE_RINGTONE_PICKER=0;
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        if (requestCode == REQUEST_CODE_RINGTONE_PICKER){
            if (resultCode == RESULT_OK){

                Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                Ringtone ringtone = RingtoneManager.getRingtone(this,uri);
            }
        }else if(requestCode== REQUEST_AUDIO_GET){
            //if(requestCode==RESULT_OK){
            if(mApp.getSelectUri()=="未選択") {
                Uri audioUri = data.getData();
                mApp.setSelectUri(audioUri.toString());
                mApp.setStartBFlag(1);
                Log.d("", "uri: " + audioUri);
                mysamref.child(roomid).child("mode").setValue("1");
            }
                //MediaPlayer player2 = MediaPlayer.create(getApplicationContext(), audioUri);
                //player2.start(); // prepare()は必要ない
            //}
        }
    }
    // GET
    private void getTest() {
        Request request = new Request.Builder()
                .url("http://weather.livedoor.com/forecast/webservice/json/v1?city=130010")     // 130010->東京
                .get()
                .build();

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
                    JSONArray weathers = resJson.getJSONArray("pinpointLocations");     // 例として "pinpointLocations" を取り出す
                    JSONObject weather = weathers.getJSONObject(0);                     // 2番目のオブジェクトにアクセスしたい場合は"1"
                    String description = weather.getString("name");                     // 例として "name" を取り出す
                    des = description;

                    // UI反映
                    runOnUiThread(new Runnable() {
                        public void run() {
                            //textViewRes.setText(res);
                            //textViewDes.setText(des);
                            textView1.setText(des);

                            Log.d("",res);
                        }
                    });
                } catch(JSONException e) {
                    failMessage();
                    e.printStackTrace();
                }
            }
        });
    }

    // GET
    private void getTest2() {
        Request request = new Request.Builder()
                .url("https://c15326208.web.cddbp.net/webapi/json/1.0/radio/recommend?client=15326208-22D0B0DD9C64D2EA02DBA05C4843F699&user=43445848250403434-6DB39718FFC49710E554D91F2B51872E&seed=(text_artist_星野源)&return_count=25")     // 130010->東京
                .get()
                .build();

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
                    String description = weather.getString("STATUS");                     // 例として "name" を取り出す
                    des = description;

                    // UI反映
                    runOnUiThread(new Runnable() {
                        public void run() {
                            //textViewRes.setText(res);
                            //textViewDes.setText(des);
                            textView1.setText(des);

                            Log.d("",res);
                        }
                    });
                } catch(JSONException e) {
                    failMessage();
                    e.printStackTrace();
                }
            }
        });
    }

    // GET
    private void getTest3() {
        Request request = new Request.Builder()
                .url("https://c15326208.web.cddbp.net/webapi/json/1.0/radio/recommend?client=15326208-22D0B0DD9C64D2EA02DBA05C4843F699&user=43445848250403434-6DB39718FFC49710E554D91F2B51872E&seed=(text_artist_星野源;text_track_恋)&return_count=25")     // 130010->東京
                .get()
                .build();

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




                    // UI反映
                    runOnUiThread(new Runnable() {
                        public void run() {
                            //textViewRes.setText(res);
                            //textViewDes.setText(des);

                            for(int i=0;i<25;i++){
                                //mttemp=mItems.get((int)(Math.random()*30)).title;
                                Album albumins = new Album();
                                //albumins.setName("HogeFuga");
                                //albumins.setArtist("あいうえおかきくけこ");
                                albumins.setName(albumdes[i][1]);
                                albumins.setArtist(albumdes[i][0]);
                                list.add(albumins);
                                adapterapi.notifyDataSetChanged();

                            }
                            //textView1.setText(des);

                            // reserveListView.setAdapter(adapterapi);

                            Log.d("",res);
                        }
                    });
                } catch(JSONException e) {
                    failMessage();
                    e.printStackTrace();
                }
            }
        });
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




                    // UI反映
                    runOnUiThread(new Runnable() {
                        public void run() {
                            //textViewRes.setText(res);
                            //textViewDes.setText(des);
                            //list = new ArrayList<>();
                            list = new ArrayList<>();
                            adapterapi = new AlbumAdapter(KeyAnswerActivity.this);
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
                                recommendMusicView.setText(list.get(0).getName());
                                recommendArtistView.setText(list.get(0).getArtist());
                                recommendTitleView1.setText(list.get(0).getName());
                                recommendArtistView1.setText(list.get(0).getArtist());
                                selectMusicView.setText(list.get(0).getName());
                                selectArtistView.setText(list.get(0).getArtist());
                            }else{
                                musicListFunc();
                            }
                        }

                    });
                } catch(JSONException e) {
                    failMessage();
                    e.printStackTrace();
                }
            }
        });

    }

    // POST
    private void postTest() {
        RequestBody formBody = new FormBody.Builder()
                .add("tokyo", "130010")
                .add("osaka", "270000")
                .add("name", "nanashinogonbei")
                .add("action", "hoge")
                .add("value", "fuga")
                .build();

        Request request = new Request.Builder()
                .url("http://www.muryou-tools.com/test/aaaa.php")       // HTTPアクセス POST送信 テスト確認用ページ
                .post(formBody)
                .build();

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
                runOnUiThread(new Runnable() {
                    public void run() {
                        //textViewRes.setText(res);
                        //textViewDes.setText("No Data");
                        Log.d("",res);
                    }
                });
            }
        });
    }

    private void failMessage() {
        runOnUiThread(new Runnable() {
            public void run() {
                //textViewRes.setText("onFailure");
                //textViewDes.setText("No Data");
                Log.d("","failure");
            }
        });
    }
    int nowView=0;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //mTextMessage.setText(R.string.title_home);
                    mContentView.setVisibility(View.VISIBLE);
                    mContentView2.setVisibility(View.INVISIBLE);
                    mContentView3.setVisibility(View.INVISIBLE);
                    mContentViewNONE.setVisibility(View.INVISIBLE);
                    nowView=0;
                    return true;
                case R.id.navigation_enlist:
                    //mTextMessage.setText(R.string.title_home);
                    mContentView.setVisibility(View.INVISIBLE);
                    mContentView2.setVisibility(View.VISIBLE);
                    mContentView3.setVisibility(View.INVISIBLE);
                    mContentViewNONE.setVisibility(View.INVISIBLE);
                    nowView=1;
                    return true;
                case R.id.navigation_history:
                    //mTextMessage.setText(R.string.title_dashboard);
                    mContentView.setVisibility(View.INVISIBLE);
                    mContentView2.setVisibility(View.INVISIBLE);
                    mContentView3.setVisibility(View.VISIBLE);
                    mContentViewNONE.setVisibility(View.INVISIBLE);
                    nowView=2;
                    return true;
                case R.id.navigation_participant:
                    //mTextMessage.setText(R.string.title_notifications);
                    mContentView.setVisibility(View.INVISIBLE);
                    mContentView2.setVisibility(View.INVISIBLE);
                    mContentView3.setVisibility(View.INVISIBLE);
                    mContentViewNONE.setVisibility(View.VISIBLE);
                    nowView=3;
                    return true;
            }
            return false;
        }

    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_actionbar, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("aieuo","aieuo");


        mPopupWindow = new PopupWindow(KeyAnswerActivity.this);

        // レイアウト設定
        View popupView = getLayoutInflater().inflate(R.layout.popupmenu, null);
        //getTest4("未選択","null");
        //recommendListFunc(popupView);
        onClickQRCodeCreate(popupView,roomid);
        popupView.findViewById(R.id.close_button_bar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }
            }
        });
        mPopupWindow.setContentView(popupView);

        // 背景設定
        mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.popup_background));


        // タップ時に他のViewでキャッチされないための設定
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);

        // 表示サイズの設定 今回は幅300dp
        float width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 350, getResources().getDisplayMetrics());
        mPopupWindow.setWindowLayoutMode((int) width, WindowManager.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setWidth((int) width);
        mPopupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        // 画面中央に表示
        mPopupWindow.showAtLocation(findViewById(R.id.selectMusicButton), Gravity.CENTER, 0, 0);

        return true;
    }

    //QRCode作成
    public void onClickQRCodeCreate(View view,String qrcode)
    {
        // QRCodeの作成
        Bitmap qrCodeBitmap = this.createQRCode(qrcode);
        //roomidView.setText(qrcode);
        roomidView=(TextView)view.findViewById(R.id.roomid_bar);
        roomidView.setText(qrcode);
        roomid=qrcode;

        // QRCodeの作成に成功した場合
        if (qrCodeBitmap != null)
        {
            // 結果をImageViewに表示
            ImageView imageView = (ImageView) view.findViewById(R.id.union_qr_bar);
            imageView.setImageBitmap(qrCodeBitmap);
        }
    }

    private Bitmap createQRCode(String contents)
    {
        Bitmap qrBitmap = null;
        try {
            // QRコードの生成
            QRCodeWriter qrcodewriter = new QRCodeWriter();
            BitMatrix qrBitMatrix = qrcodewriter.encode(contents,
                    BarcodeFormat.QR_CODE,
                    300,
                    300);

            qrBitmap = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888);
            qrBitmap.setPixels(this.createDot(qrBitMatrix), 0, 300, 0, 0, 300, 300);
        }
        catch(Exception ex)
        {
            // エンコード失敗
            Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
        }
        finally
        {
            return qrBitmap;
        }
    }

    // ドット単位の判定
    private int[] createDot(BitMatrix qrBitMatrix)
    {
        // 縦幅・横幅の取得
        int width = qrBitMatrix.getWidth();
        int height = qrBitMatrix.getHeight();
        // 枠の生成
        int[] pixels = new int[width * height];

        // データが存在するところを黒にする
        for (int y = 0; y < height; y++)
        {
            // ループ回数盤目のOffsetの取得
            int offset = y * width;
            for (int x = 0; x < width; x++)
            {
                // データが存在する場合
                if (qrBitMatrix.get(x, y))
                {
                    pixels[offset + x] = Color.BLACK;
                }
                else
                {
                    pixels[offset + x] = Color.WHITE;
                }
            }
        }
        // 結果を返す
        return pixels;
    }
    public void preList(){

    }
    int temproomnum=0;
    @Override
    public void onStop(){
        /*
        mysamref.removeEventListener(velplaylist);
        mysamref.removeEventListener(velmember);
        mysamref.removeEventListener(velstatus);
        mysamref.removeEventListener(velroom);
        mysamref.removeEventListener(velturn);
        if(Integer.parseInt(memnum)==1) {
            mysamref.child(roomid).setValue(null);
        }else{
            temproomnum=Integer.parseInt(memnum)-1;
            mysamref.child(roomid).child("member").child(String.valueOf(memberID)).setValue(null);
            mysamref.child(roomid).child("number").setValue(temproomnum);
        }
        Log.d("stop","stopCycle");
        */

        super.onStop();


    }
    @Override
    public void onDestroy(){
        //mysamref.removeEventListener(velstatus);
        //mysamref.removeEventListener(velroom);
        //mysamref.removeEventListener(velturn);
        /*
        if(Integer.parseInt(memnum)==1) {
            mysamref.child(roomid).setValue(null);
        }else{
            temproomnum=Integer.parseInt(memnum)-1;
            mysamref.child(roomid).child("member").child(String.valueOf(memberID)).setValue(null);
            mysamref.child(roomid).child("number").setValue(temproomnum);
        }
        */
        onStopService();
        super.onDestroy();
        Toast.makeText(this, "onDestory", Toast.LENGTH_SHORT).show();
    }
    /*

    private Bitmap LoadImage(String URL, BitmapFactory.Options options)
    {
        Bitmap bitmap = null;
        InputStream in = null;
        try {
            in = OpenHttpConnection(URL);
            bitmap = BitmapFactory.decodeStream(in, null, options);
            in.close();
        } catch (IOException e1) {
        }
        return bitmap;
    }

    private InputStream OpenHttpConnection(String strURL) throws IOException{
        InputStream inputStream = null;
        URL url = new URL(strURL);
        URLConnection conn = url.openConnection();

        try{
            HttpURLConnection httpConn = (HttpURLConnection)conn;
            httpConn.setRequestMethod("GET");
            httpConn.connect();

            if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = httpConn.getInputStream();
            }
        }
        catch (Exception ex)
        {
        }
        return inputStream;
    }
    */

    public static Bitmap getBitmapFromURL(String src) {
        try {
            Log.e("src",src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.e("Bitmap","returned");
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception",e.getMessage());
            return null;
        }
    }
    ValueEventListener velreserve;
    int reserveCount=0;
    int reserveUID=0;
    //10人の
    String reserveArr[][][];
    Reserve tempreserve;
    public void reserveList() {
        velreserve=new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                int i=0;
                listReserve = new ArrayList<>();
                adapterReserveapi = new AlbumAdapter(KeyAnswerActivity.this);
                adapterReserveapi.setAlbumList(listReserve);
                recommendListView=(ListView)findViewById(R.id.reserveView);
                recommendListView.setAdapter(adapterReserveapi);
                reserveArr=new String [10][10][3];
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    //String sender = (String) dataSnapshot.child("username").getValue();
                    //String body = (String) dataSnapshot.child("username").getValue();
                    int j=0;
                    for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                        tempreserve = dataSnapshot2.getValue(Reserve.class);
                        reserveArr[i][j][0]=tempreserve.title;
                        reserveArr[i][j][1]=tempreserve.artist;
                        reserveArr[i][j][2]=Integer.toString(tempreserve.userid);
                        j++;

                        Log.d("Firebase", String.format("retitle:%s, reartist:%s", tempreserve.title, tempreserve.artist));
                    }
                    if(Integer.parseInt(dataSnapshot.getKey())==memberID) {
                        reserveCount=j;
                        reserveUID=i;
                    }

                    //Log.d("Firebase", String.format("sender:%s, body:%s", post.title, post.body));
                }
                for(int c=0;c<reserveCount;c++){
                    String mttempName = "";
                    String mttempArtist = "";
                    int mttempInt = 0;
                    //for (int c2 = 0; c2 < 10; c2++) {

                    mttempInt = 0;
                    mttempName = reserveArr[reserveUID][c][0];
                    mttempArtist = reserveArr[reserveUID][c][1];


                    Album albuminsReserve = new Album();
                    albuminsReserve.setName(mttempName);
                    albuminsReserve.setArtist(mttempArtist);

                    listReserve.add(albuminsReserve);

                    //}

                }
                if(i>0) {
                    adapterReserveapi.notifyDataSetChanged();
                }


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        };

        mysamref.child(roomid).child("reserve").addValueEventListener(velreserve);
    }
    public void onStartService() {
        Intent intent = new Intent(KeyAnswerActivity.this, SoundManageService.class);
        startService(intent);
        //Button btPlay = (Button) findViewById(R.id.btPlay);
        //Button btStop = (Button) findViewById(R.id.btStop);
        //btPlay.setEnabled(false);
        //btStop.setEnabled(true);
    }

    public void onStopService() {
        Intent intent = new Intent(KeyAnswerActivity.this, SoundManageService.class);
        stopService(intent);
        //Button btPlay = (Button) findViewById(R.id.btPlay);
        //Button btStop = (Button) findViewById(R.id.btStop);
        //btPlay.setEnabled(true);
        //btStop.setEnabled(false);
    }
}