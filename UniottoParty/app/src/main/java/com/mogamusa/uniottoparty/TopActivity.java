package com.mogamusa.uniottoparty;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class TopActivity extends AppCompatActivity implements View.OnClickListener {


    public Button start_enter;
    public Button start_create;
    public Button login_facebook;
    private View mContentView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_top);
        start_enter=(Button)findViewById(R.id.button_enter);
        start_enter.setOnClickListener(this);

        start_create=(Button)findViewById(R.id.button_create);
        start_create.setOnClickListener(this);


/*
        mContentView= findViewById(R.id.fullscreen_content);
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

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


    }

    @Override
    public void onClick(View v){

        if(v==start_enter){

            Intent intent = new Intent(this, RoomActivity.class);

            //startActivityForResult(intent, 0);
            startActivity(intent);
            Log.d("","aiueo");
        }
        if(v==start_create){
            Intent intent = new Intent(this, RoomCActivity.class);

            //startActivityForResult(intent, 0);
            startActivity(intent);
        }
    }

}
