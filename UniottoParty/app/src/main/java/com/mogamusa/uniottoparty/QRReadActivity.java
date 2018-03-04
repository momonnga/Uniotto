package com.mogamusa.uniottoparty;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.util.List;

public class QRReadActivity extends Activity {

    private SurfaceView mSurfaceView;
    private Camera mCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSurfaceView = new SurfaceView(this);
        mSurfaceView.setOnClickListener(onClickListener);
        setContentView(mSurfaceView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SurfaceHolder holder = mSurfaceView.getHolder();
        holder.addCallback(callback);
    }

    private SurfaceHolder.Callback callback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            // 生成されたとき
            mCamera = Camera.open();
            try {
                // プレビューをセットする
                mCamera.setPreviewDisplay(holder);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            // 変更されたとき
            Camera.Parameters parameters = mCamera.getParameters();
            List<Size> previewSizes = parameters.getSupportedPreviewSizes();
            Size previewSize = previewSizes.get(0);
            parameters.setPreviewSize(previewSize.width, previewSize.height);
            // width, heightを変更する
            mCamera.setParameters(parameters);
            mCamera.startPreview();
        }
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // 破棄されたとき
            mCamera.release();
            mCamera = null;
        }
    };

    private OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // オートフォーカス
            if (mCamera != null) {
                mCamera.autoFocus(autoFocusCallback);
            }
        }
    };

    private AutoFocusCallback autoFocusCallback = new AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean success, Camera camera) {
            if (success) {
                // 現在のプレビューをデータに変換
                camera.setOneShotPreviewCallback(previewCallback);
            }
        }
    };

    private PreviewCallback previewCallback = new PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            // 読み込む範囲
            int previewWidth = camera.getParameters().getPreviewSize().width;
            int previewHeight = camera.getParameters().getPreviewSize().height;

            // プレビューデータから BinaryBitmap を生成
            PlanarYUVLuminanceSource source = new PlanarYUVLuminanceSource(
                    data, previewWidth, previewHeight, 0, 0, previewWidth, previewHeight, false);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            // バーコードを読み込む
            Reader reader = new MultiFormatReader();
            Result result = null;
            try {
                result = reader.decode(bitmap);
                String text = result.getText();
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Not Found", Toast.LENGTH_SHORT).show();
            }
        }
    };

}
