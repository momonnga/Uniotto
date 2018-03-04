package com.mogamusa.uniottoparty;

/**
 * Created by masayuki on 2017/09/28.
 */

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RoomActivity extends AppCompatActivity implements View.OnClickListener{

    private View mContentView;
    private View mContentView2;
    DatabaseReference mysamref;
    String roomid="room22";
    String userName="masa1";//masayuki5・masa1がxperformance それ以外(masayuki4・masa2)がz4

    public Button startB;
    public Button start_read_qr;
    EditText editText;
    EditText editTextR;

    ExecutorService exec2 = Executors.newSingleThreadExecutor();
    ArrayAdapter<String> adapter;
    ListView listView1;

    private InputMethodManager inputMethodManager;
    private RelativeLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);


        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //画面全体のレイアウト
        mainLayout = (RelativeLayout)findViewById(R.id.fullscreen_content);
        //キーボード表示を制御するためのオブジェクト
        inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);





        /*

        if (Build.VERSION.SDK_INT >= 19) {
            View decor = this.getWindow().getDecorView();
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } else {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        */


        //setContentView(R.layout.activity_login);
        startB=(Button)findViewById(R.id.start);
        startB.setOnClickListener(this);

        //setContentView(R.layout.activity_login);
        start_read_qr=(Button)findViewById(R.id.button_read_qr);
        start_read_qr.setOnClickListener(this);

        mContentView= findViewById(R.id.fullscreen_content);
        /*

        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                */

        editText = (EditText) findViewById(R.id.editName);
        editTextR = (EditText) findViewById(R.id.editText);

        //EditText editText = (EditText)findViewById(R.id.editText);
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    /*
                    mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                            */
                    // フォーカスが外れた場合キーボードを非表示にする
                    InputMethodManager inputMethodMgr = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodMgr.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });
        //EditText editText = (EditText)findViewById(R.id.editText);
        editTextR.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    /*
                    mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                            */
                    // フォーカスが外れた場合キーボードを非表示にする
                    InputMethodManager inputMethodMgr = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodMgr.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });
        //フーリエ解析スレッド
        exec2.submit(new Runnable() {
            @Override
            public void run() {


                while (true) {


                    userName=editText.getText().toString();
                    roomid=editTextR.getText().toString();
                    //Log.d("check", "kiteruyo" + actTestView.flag);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                    }

                }

            }
        });
        // 追加するアイテムを生成する
        adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1);
        roomget();

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

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            editText.setText(name);
            //userName.setText(""+photoUrl);
            Log.d("user",name+","+email+","+","+photoUrl);

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            String uid = user.getUid();
        }
    }
    int roomGetC=0;

    ValueEventListener velget;

    public void roomget(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mysamref=database.getReference("room");

        //mysamref = database2.getReference("room");
        velget=new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null) {
                    if(roomGetC==1) {
                        // 追加するアイテムを生成する
                        adapter = new ArrayAdapter<String>(
                                RoomActivity.this, android.R.layout.simple_list_item_1);
                    }
                    String temproom = String.valueOf(dataSnapshot.getValue());

                    String[] preseparated = temproom.split("\\}, ");


                    for (int i = 0; i < preseparated.length; i++) {
                        if (i == 0) {
                            String[] separated = preseparated[0].split("=\\{");
                            String[] separated2 = separated[0].split("\\{");
                            //editText.setText(separated2[1]);

                            adapter.add(separated2[1]);
                        } else {
                            String[] separated = preseparated[i].split("=\\{");
                            //adapter.add(separated[0]);
                            String result2 = separated[0].substring(0, 1);

                            if (result2.equals("{")) {
                                //editText.setText(result2);
                            } else {
                                adapter.add(separated[0]);
                            }
                        }

                    }
                    roomGetC=1;
                    // リストビューにアイテム (adapter) を追加
                    listView1 = (ListView)findViewById(R.id.listView);
                    listView1.setAdapter(adapter);

                    // アイテムクリック時ののイベントを追加
                    listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent,
                                                View view, int pos, long id) {

                            // 選択アイテムを取得
                            ListView listView = (ListView)parent;
                            String item = (String)listView.getItemAtPosition(pos);

                            // 通知ダイアログを表示
                            Toast.makeText(RoomActivity.this,
                                    item, Toast.LENGTH_LONG
                            ).show();
                            roomid=item;
                            editTextR.setText(item);
                        }
                    });

                    //}
                    //adapter.add(preseparated[0]);
                    //adapter.add(preseparated[1]);

                    // Log.d("","NAmmmmmmmmmmmmmmmmmmmmmmmmeeeeeeeeeeeeeeee"+dataSnapshot.child("name").getValue());
                }

                Log.d("","NAmmmmmmmmmmmmmmmmmmmmmmmmeeeeeeeeeeeeeeee");
                //roomGetC=0;
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        };
        mysamref.addValueEventListener(velget);




    }
    @Override
    public void onClick(View v){

        if(v==startB){
            mysamref.removeEventListener(velget);
            //Intent intent = new Intent(this, MainActivity.class);
            Intent intent = new Intent(this, KeyAnswerActivity.class);
            intent.putExtra("DATA1",userName);
            intent.putExtra("DATA2",roomid);

            //startActivityForResult(intent, 0);
            startActivity(intent);
            Log.d("","aiueo");
        }
        if(v==start_read_qr){
            //mysamref.removeEventListener(velget);
            //Intent intent = new Intent(this, QRReadActivity.class);
            //startActivity(intent);
            new IntentIntegrator(RoomActivity.this).initiateScan();

        }
        //キーボードを隠す
        inputMethodManager.hideSoftInputFromWindow(mainLayout.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        //背景にフォーカスを移す
        mainLayout.requestFocus();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("TAG", "onActivityResult Start");
        Log.d("TAG", "requestCode: " + requestCode + " resultCode: " + resultCode + " data: " + data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        // 奇妙なことに null の場合
        if (intentResult == null) {
            Log.d("TAG", "Weird");
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }

        if (intentResult.getContents() == null) {
            // 戻るボタンをタップした場合
            Log.d("TAG", "Cancelled Scan");

        } else {
            // カメラで読み取った情報をラベルに表示
            Log.d("TAG", "Scanned");
            //TextView textViewScannedData = (TextView) findViewById(R.id.textViewScannedData);
            editTextR.setText(intentResult.getContents());
        }
    }
    @Override

    public boolean onTouchEvent(MotionEvent event) {

        // キーボード非表示

// InputMethodManager の変数　
inputMethodManager.hideSoftInputFromWindow(mainLayout.// RelativeLauoutの変数

        getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

// 背景にフォーカス

        mainLayout.requestFocus();

        return false;

    }



}
