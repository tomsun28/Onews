package com.example.tomsu.onews.uiActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import com.example.tomsu.onews.MainActivity;
import com.example.tomsu.onews.R;
import com.example.tomsu.onews.control.User;
import com.example.tomsu.onews.staticAll.MyApp;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.username_et)EditText usernameEt;
    @BindView(R.id.password_et)EditText passwordEt;

    AsyncHttpResponseHandler ahth = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            if(statusCode == 200){

                String jsonString;
                try {
                    jsonString = URLDecoder.decode(new String(responseBody), "utf-8");

                    System.out.println(jsonString);
                    User user = JSON.parseObject(jsonString, User.class);

                    if(user != null){

                       Toast toast = Toast.makeText(LoginActivity.this, "欢迎回来！", Toast.LENGTH_SHORT);
                        //toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL , 0, 0);
                        //TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                        //v.setTextColor(Color.YELLOW);
                        toast.show();


                        //SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences("user", MODE_PRIVATE);
                        //SharedPreferences.Editor et = sharedPreferences.edit();
                        //et.putString();
                        //et.commit();

                        MyApp staticAll = ((MyApp)getApplicationContext());
                        staticAll.setUserId(user.getUserId());
                        staticAll.setUserPhoto(user.getPhoto());
                        staticAll.setUserEmail(user.getEmail());
                        staticAll.setUserName(user.getUserName());
                        staticAll.setThere(true);
                        toMainActivity();
                    }
                    else
                    {
                        Toast.makeText(LoginActivity.this,"用户名密码错误！",Toast.LENGTH_SHORT).show();
                        MyApp staticAll = ((MyApp)getApplicationContext());
                        staticAll.setThere(false);

                    }

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Log.e("TEST", "onFailuress:---11111111111111111111111111111111111111----------------------------------------- "+statusCode );
            Toast toast = Toast.makeText(LoginActivity.this, "请检查网络！", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL , 0, 0);
            TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
            v.setTextColor(Color.YELLOW);
            toast.show();

        }
    };

    @OnClick(R.id.quest_et)
    public void question()
    {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse("https://zhidao.baidu.com/");
        intent.setData(content_url);
        intent.setClassName("com.android.browser","com.android.browser.BrowserActivity");
        startActivity(intent);

    }

    @OnClick(R.id.logon_et)
    public void logon()
    {
        toLogonActivity();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.login_et)
    public void login(){
        String username = usernameEt.getText().toString();
        String password = passwordEt.getText().toString();
        Log.e("TEXT", "login: "+username+"======================"+password );

        String path = "http://www.usthe.com:8080/today_history/LoginServlet";
        AsyncHttpClient ahc = new AsyncHttpClient();

        RequestParams rp = new RequestParams();
        rp.add("username", username);
        rp.add("password", password);
        ahc.get(path, rp, ahth);
    }

    public void toLogonActivity()
    {
        Intent intent = new Intent();
        intent.setClass(this,LogonActivity.class);
        startActivity(intent);

    }
    public void toMainActivity(){
        //设置Action和Data的方式是yinshi跳转
//        Intent intent = new Intent();
//        intent.setAction(Intent.ACTION_CALL);
//        intent.setData(Uri.parse("tel:110"));
//        startActivity(intent);
        //设置Activity的方式为显示跳转
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        startActivity(intent);

        //设置Activity的方式是隐式跳转
//        Intent intent = new Intent();
//        intent.setAction("com.icps.neu.chapter_07.newsactivity");
//        intent.setData(Uri.parse("neu:123"));
//        startActivity(intent);
    }

    @OnClick(R.id.return_et)
    public void back()
    {
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        startActivity(intent);
    }
}
