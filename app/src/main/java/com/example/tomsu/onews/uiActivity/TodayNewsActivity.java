package com.example.tomsu.onews.uiActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.tomsu.onews.MainActivity;
import com.example.tomsu.onews.R;
import com.example.tomsu.onews.adapter.NewsAdapter;
import com.example.tomsu.onews.adapter.PassageAdapter;
import com.example.tomsu.onews.control.News;
import com.example.tomsu.onews.control.Passage;
import com.example.tomsu.onews.control.Token;
import com.example.tomsu.onews.control.User;
import com.example.tomsu.onews.layout.DrawableUtils;
import com.example.tomsu.onews.layout.FlowLayout;
import com.example.tomsu.onews.staticAll.MyApp;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

import static android.R.attr.data;

/**
 * Created by tomsu on 2017/5/15.
 */

public class TodayNewsActivity extends AppCompatActivity{

    @BindView(R.id.fl)FlowLayout fl_et;
    @BindView(R.id.news_lv)ListView news_lv;
    @BindView(R.id.et_clear)EditText et_clear;
    private List<Token> tokenList = new ArrayList<Token>();
    private List<News> newsList = new ArrayList<News>();

    AsyncHttpResponseHandler ahth = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            if(statusCode == 200){

                String jsonString;
                try {
                    jsonString = URLDecoder.decode(new String(responseBody), "utf-8");

                    System.out.println(jsonString);
                    tokenList = JSON.parseArray(jsonString, Token.class);
                    initData(tokenList);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Log.e("TEST", "onFailuress:---11111111111111111111111111111111111111----------------------------------------- "+statusCode );
            Toast toast = Toast.makeText(TodayNewsActivity.this, "请检查网络！", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL , 0, 0);
            TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
            v.setTextColor(Color.YELLOW);
            toast.show();

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.today_main);
        ButterKnife.bind(this);

        String path = "http://www.usthe.com:8080/today_history/GetTokenServlet";
        AsyncHttpClient ahc = new AsyncHttpClient();
        RequestParams rp = new RequestParams();
        /*
        Date temp = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Log.e("DATE", "onCreate:================================== "+sdf.format(temp).toString() );
        rp.put("date",sdf.format(temp).toString() );
        */
        rp.put("flag",1);
        ahc.post(path, rp, ahth);



    }
    private void initData(List<Token> tokenlist) {
        int padding = dip2px(5);
        fl_et.setPadding(padding, padding, padding, padding);// 设置内边距
        final Iterator<Token> item = tokenlist.iterator();
        int i = 0;
        while(item.hasNext())
        {
            if(i == 0)
            {
                i++;
                TextView tv = new TextView(this);
                tv.setText("全部");
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                tv.setPadding(padding, padding, padding, padding);
                tv.setGravity(Gravity.CENTER);
                int color = 0xffcecece;// 按下后偏白的背景色
                StateListDrawable selector;
                selector = DrawableUtils.getSelector(true,Color.parseColor("#2c90d7"), color, dip2px(30));
                tv.setBackground(selector);
                fl_et.addView(tv);
                tv.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        getAllNews();
                        et_clear.setText("全部");
                        et_clear.setSelection("全部".trim().length());
                    }
                });
            }
            final Token items = item.next();
            final String tag = items.getKeyWord();
            TextView tv = new TextView(this);
            tv.setText(tag);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            tv.setPadding(padding, padding, padding, padding);
            tv.setGravity(Gravity.CENTER);
            int color = 0xffcecece;// 按下后偏白的背景色
            StateListDrawable selector;
            selector = DrawableUtils.getSelector(true,Color.WHITE, color, dip2px(30));
            //tv.setBackgroundDrawable(selector);
            tv.setBackground(selector);
            fl_et.addView(tv);
            tv.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    getThisNews(items.getTokenId());
                    et_clear.setText(tag);
                    et_clear.setSelection(tag.trim().length());
                }
            });
        }
    }

    public int dip2px(float dip) {
        float density = this.getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f);
    }
    public  void getThisNews(int tokenId)
    {
        String path = "http://www.usthe.com:8080/today_history/GetTokenNewsServlet";
        AsyncHttpClient ahc = new AsyncHttpClient();
        RequestParams rp = new RequestParams();
        rp.put("tokenId",tokenId );

        ahc.post(path, rp, ahth2);


    }
    AsyncHttpResponseHandler ahth2 = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            if(statusCode == 200){

                String jsonString;
                try {
                    jsonString = URLDecoder.decode(new String(responseBody), "utf-8");

                    System.out.println(jsonString);
                    newsList = JSON.parseArray(jsonString, News.class);
                    NewsAdapter mAdapter = new NewsAdapter(newsList,TodayNewsActivity.this);
                    news_lv.setAdapter(mAdapter);
                    //news_lv.setMode(PullToRefreshBase.Mode.BOTH);

                    news_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            News news = newsList.get(position);

                            Intent intent = new Intent();
                            //传递简单数据，使用PutExtra的方式
                            //intent.putExtra("news_title", news.getTitle());
                            //传递复杂数据，如对象，使用Bundle的方式
                            Bundle bundle = new Bundle();

                            bundle.putSerializable("news",news );
                            intent.putExtras(bundle);
                            intent.setClass(TodayNewsActivity.this, NewsDetails2Activity.class);
                            startActivity(intent);
                        }
                    });
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Log.e("TEST", "onFailuress:---222222222222222222222222222222222222222----------------------------------------- "+statusCode );
            Toast toast = Toast.makeText(TodayNewsActivity.this, "请检查网络！", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL , 0, 0);
            TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
            v.setTextColor(Color.YELLOW);
            toast.show();

        }
    };
    public  void getAllNews()
    {
        String path = "http://www.usthe.com:8080/today_history/GetNewsServlet";
        AsyncHttpClient ahc = new AsyncHttpClient();
        RequestParams rp = new RequestParams();
        /*
        Date temp = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Log.e("DATE", "onCreate:================================== "+sdf.format(temp).toString() );
        rp.put("date",sdf.format(temp).toString() );
        */
        rp.put("flag",1);
        ahc.post(path, rp, ahth3);

    }
    AsyncHttpResponseHandler ahth3 = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            if(statusCode == 200){

                String jsonString;
                try {
                    jsonString = URLDecoder.decode(new String(responseBody), "utf-8");

                    System.out.println(jsonString);
                    newsList = JSON.parseArray(jsonString, News.class);
                    NewsAdapter mAdapter = new NewsAdapter(newsList,TodayNewsActivity.this);
                    news_lv.setAdapter(mAdapter);
                    //news_lv.setMode(PullToRefreshBase.Mode.BOTH);

                    news_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            News news = newsList.get(position);

                            Intent intent = new Intent();
                            //传递简单数据，使用PutExtra的方式
                            //intent.putExtra("news_title", news.getTitle());
                            //传递复杂数据，如对象，使用Bundle的方式
                            Bundle bundle = new Bundle();

                            bundle.putSerializable("news",news );
                            intent.putExtras(bundle);
                            intent.setClass(TodayNewsActivity.this, NewsDetails2Activity.class);
                            startActivity(intent);
                        }
                    });

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Log.e("TEST", "onFailuress:---333333333333333333333333333333333333333333333----------------------------------------- "+statusCode );
            Toast toast = Toast.makeText(TodayNewsActivity.this, "请检查网络！", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL , 0, 0);
            TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
            v.setTextColor(Color.YELLOW);
            toast.show();

        }
    };




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
