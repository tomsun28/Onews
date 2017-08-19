package com.example.tomsu.onews.uiActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.tomsu.onews.MainActivity;
import com.example.tomsu.onews.R;
import com.example.tomsu.onews.adapter.CommentsAdapter;
import com.example.tomsu.onews.control.Comments;
import com.example.tomsu.onews.control.News;
import com.example.tomsu.onews.control.Passage;
import com.example.tomsu.onews.control.Result;
import com.example.tomsu.onews.staticAll.MyApp;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.image.SmartImageView;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * Created by tomsu on 2017/5/16.
 */

public class NewsDetails2Activity extends AppCompatActivity {
    @BindView(R.id.news_title)TextView news_title_tv;
    @BindView(R.id.date_et)TextView news_date_tv;
    @BindView(R.id.detail_content)TextView news_body_tv;
    @BindView(R.id.news_image)SmartImageView news_image;
    @BindView(R.id.comments_lv)ListView comments_lv;
    @BindView(R.id.commentEdit_et)EditText commentEdit_et;
    @BindView(R.id.commentSend_et)ImageView commentSend_et;
    List<Comments> commentsList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_details2_layout);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        //获取简单数据的方式，使用get方法
        //String title = intent.getStringExtra("news_title");
        //第一种方式，先获取Bundle,再Bundle数据
        /*
            Bundle bundle = intent.getExtras();
            News news = bundle.getSerializableExtra("news");
         */
        //第二种方式，直接获取
        News news = (News)intent.getSerializableExtra("news");
        news_title_tv.setText(news.getTitle());
        news_date_tv.setText(news.getThisDate().toString());
        String temp = news.getContent().replace("\\n","\n").replace("\\t","\t");
        news_body_tv.setText(temp);
        news_image.setBackgroundResource(0);
        news_image.setImageUrl(news.getPicture());


    }
    @OnClick(R.id.return_et)
    public void  back_et()
    {
        Intent intent = new Intent();
        intent.setClass(this, TodayNewsActivity.class);
        startActivity(intent);
    }
/*
    @OnClick(R.id.commentSend_et)
    public void send_comment()
    {
        MyApp staticAll = ((MyApp)getApplicationContext());
        if(staticAll.isThere())
        {
            String comment_content = commentEdit_et.getText().toString();
            Date date = new Date();
            Comments comment = new Comments();
            comment.setContent(comment_content);
            comment.setCommentDate(date);
            comment.setPassageId(staticAll.getPassageId());
            comment.setReviewer(staticAll.getUserId());

            String jsonString = JSON.toJSONString(comment);
            String path2 = "http://118.202.45.251:8080/today_history/InCommentServlet";
            AsyncHttpClient ahc2 = new AsyncHttpClient();
            RequestParams rp2 = new RequestParams();
            try {
                rp2.put("comment", URLEncoder.encode(jsonString,"utf-8") );
                ahc2.get(path2, rp2, ahth2);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }


        }
        else
        {
            Toast.makeText(NewsDetails2Activity.this,"请登录或注册！",Toast.LENGTH_SHORT).show();
        }

    }

    AsyncHttpResponseHandler ahth2 = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            Log.e("TEXT", "incomment onSuccess: "+statusCode );
            if (statusCode == 200) {

                String jsonString = new String(responseBody);
                try {
                    jsonString = URLDecoder.decode(jsonString, "utf-8");
                    Result result = JSON.parseObject(jsonString, Result.class);
                    Toast.makeText(NewsDetails2Activity.this,result.getMessage(),Toast.LENGTH_SHORT).show();
                    //news_lv.setMode(PullToRefreshBase.Mode.BOTH);


                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                // SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences("user", MODE_PRIVATE);
                // sharedPreferences.getString("")
            }
        }
        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        }
    };
    */
}
