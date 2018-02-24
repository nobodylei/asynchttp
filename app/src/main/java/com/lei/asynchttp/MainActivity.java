package com.lei.asynchttp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
;import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    AsyncHttpClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getPermission();
        client = new AsyncHttpClient();
    }

    //get按钮事件
    public void doGet(View view) {
        String url = "https://www.baidu.com";

        client.get(url, new AsyncHttpResponseHandler() {
            @Override//请求成功
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i("Test", "get click,onSuccess called,result" + new String(responseBody));
            }

            @Override//请求失败
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.i("Test", "get click,onFailure called,result" + new String(responseBody));
            }
        });
    }

    //post
    public void doPost(View view) {
        String url = "http://www.tuling123.com/openapi/api";
        //?key=bdae886baa234e17bdba421783d178de&info=
        RequestParams params = new RequestParams();
        params.put("key","bdae886baa234e17bdba421783d178de");
        params.put("info","hello");
        client.post(url,params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i("Test", "doPost click,onSuccess called,result" + new String(responseBody));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.i("Test", "doPost click,onFailure called,result" + new String(responseBody));
            }
        });
    }

    //下载
    public void doDownload(View view) {
        String url = "http://192.168.0.101:8080/okhttpDemo/files/aaa.jpg";

        client.get(url, new BinaryHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] binaryData) {
                Log.i("Test", "doDownload click,onSuccess called,result" + binaryData);
                Bitmap bitmap = BitmapFactory.decodeByteArray(binaryData, 0, binaryData.length);

                File directory = new File(Environment.getExternalStorageDirectory() + "/AAA");
                if(!directory.exists()) {
                    directory.mkdir();
                    Log.i("Test","mkdir");
                }

                File file = new File(directory, "/as.jpg");

                //设置压缩格式
                Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
                //压缩比例
                int quality = 100;
                if(file.exists()) {
                    file.delete();
                }
                try {
                    file.createNewFile();
                    OutputStream stream = new FileOutputStream(file);
                    bitmap.compress(format, quality, stream);
                    stream.close();
                    Log.i("Test", "save image uccess");
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i("Test", "save image Failure");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] binaryData, Throwable error) {
                Log.i("Test", "doDownload click,onFailure called,result" + error);
            }
        });
    }

    //上传
    public void doUpload(View view) {
        String url = "http://192.168.0.101:8080/okhttpDemo/postFile";
        File file =new File(Environment.getExternalStorageDirectory() + "/a.zip");
        RequestParams params = new RequestParams();
        try {
            params.put("uploadfile",file);
            client.post(url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    Log.i("Test", "doUpload click,onSuccess called,result" + responseBody);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.i("Test", "doUpload click,onFailure called,result" + responseBody);
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void getPermission() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }
}
