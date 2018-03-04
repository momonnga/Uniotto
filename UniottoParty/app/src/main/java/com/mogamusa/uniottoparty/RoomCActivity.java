package com.mogamusa.uniottoparty;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.concurrent.ThreadLocalRandom;

public class RoomCActivity extends AppCompatActivity implements View.OnClickListener {

    View viewroomc;
    String roomid;

    TextView roomidView;

    Button room_create;

    String data1="";
    String data2="";
    String userName="host";
    TextView userNameView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_room_c);
        viewroomc=findViewById(R.id.roomc);
        roomidView=(TextView)findViewById(R.id.textView12);
        RandomString gen = new RandomString(10, ThreadLocalRandom.current());
        Log.d("",gen.nextString());

        onClickQRCodeCreate(viewroomc,gen.nextString());

        room_create=(Button)findViewById(R.id.room_create);
        room_create.setOnClickListener(this);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();
            //userNameView.setText(""+name);
            userName=name;
            Log.d("user",name+","+email+","+","+photoUrl);

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            String uid = user.getUid();
        }




    }

    //QRCode作成
    public void onClickQRCodeCreate(View view,String qrcode)
    {
        // QRCodeの作成
        Bitmap qrCodeBitmap = this.createQRCode(qrcode);
        roomidView.setText(qrcode);
        roomid=qrcode;

        // QRCodeの作成に成功した場合
        if (qrCodeBitmap != null)
        {
            // 結果をImageViewに表示
            ImageView imageView = (ImageView) findViewById(R.id.room_qr);
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
    public void onClick(View v) {
        if(v==room_create){
            //Intent intent = new Intent(this, MainActivity.class);
            Intent intent = new Intent(this, KeyAnswerActivity.class);
            intent.putExtra("DATA1",userName);
            intent.putExtra("DATA2",roomid);

            //startActivityForResult(intent, 0);
            startActivity(intent);
        }
    }
}
