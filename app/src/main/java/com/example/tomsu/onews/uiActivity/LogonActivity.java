package com.example.tomsu.onews.uiActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import com.example.tomsu.onews.MainActivity;
import com.example.tomsu.onews.R;
import com.example.tomsu.onews.control.Result;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.Base64;
import com.loopj.android.http.RequestParams;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * Created by 巩超 on 2016/7/30.
 */
public class LogonActivity extends AppCompatActivity {

    @BindView(R.id.username_et)
    EditText usernameEt;
    @BindView(R.id.password_et)
    EditText passwordEt;
    @BindView(R.id.email_et)
    EditText emailEt;
    String picturePath = null;
    Bitmap bitmap = null;


    AsyncHttpResponseHandler ahth = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            if (statusCode == 200) {

                String jsonString;
                try {
                    jsonString = URLDecoder.decode(new String(responseBody), "utf-8");
                    System.out.println(jsonString);
                    Result result = JSON.parseObject(jsonString, Result.class);
                    if (result.getStatusCode() == 1) {
                        Toast.makeText(LogonActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                        toMainActivity();

                    } else {
                        Toast.makeText(LogonActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.logon_et)
    public void logon() {
        String username = usernameEt.getText().toString();
        String password = passwordEt.getText().toString();
        String email = emailEt.getText().toString();
        Log.e("TEXT", "logon: "+username+"====="+password+"======"+email );
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //将bitmap一字节流输出 Bitmap.CompressFormat.PNG 压缩格式，100：压缩率，baos：字节流
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        try {
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] buffer = baos.toByteArray();
        //将图片的字节流数据加密成base64字符输出
        String photo = Base64.encodeToString(buffer, 0, buffer.length, Base64.DEFAULT);
        //photo=URLEncoder.encode(photo,"UTF-8")

        String path = "http://www.usthe.com:8080/today_history/LogonServlet";
        AsyncHttpClient ahc = new AsyncHttpClient();
        RequestParams rp = new RequestParams();
        rp.put("username", username);
        rp.put("password", password);
        rp.put("email", email);
        rp.put("photo", photo);

        ahc.post(path, rp, ahth);
    }

    @OnClick(R.id.photo_et)
    public void photo() {

        if (ContextCompat.checkSelfPermission(LogonActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(LogonActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(LogonActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }

        Intent i = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 10);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);

            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);

            bitmap = BitmapFactory.decodeFile(picturePath);
            cursor.close();

            ImageView imageView = (ImageView) findViewById(R.id.photo_et);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

// permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

// other 'case' lines to check for other
// permissions this app might request
        }
    }

    public void toMainActivity() {
        //设置Action和Data的方式是yinshi跳转
//        Intent intent = new Intent();
//        intent.setAction(Intent.ACTION_CALL);
//        intent.setData(Uri.parse("tel:110"));
//        startActivity(intent);
        //设置Activity的方式为显示跳转
        Intent intent = new Intent();
        intent.setClass(this, LoginActivity.class);
        startActivity(intent);

        //设置Activity的方式是隐式跳转
//        Intent intent = new Intent();
//        intent.setAction("com.icps.neu.chapter_07.newsactivity");
//        intent.setData(Uri.parse("neu:123"));
//        startActivity(intent);
    }

    @OnClick(R.id.return_et)
    public void back() {
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        startActivity(intent);
    }

}
