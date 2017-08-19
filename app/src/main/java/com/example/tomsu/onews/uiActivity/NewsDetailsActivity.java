package com.example.tomsu.onews.uiActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import com.example.tomsu.onews.MainActivity;
import com.example.tomsu.onews.R;
import com.example.tomsu.onews.adapter.CommentsAdapter;
import com.example.tomsu.onews.control.Comments;
import com.example.tomsu.onews.control.LikeTimes;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class NewsDetailsActivity extends AppCompatActivity {
    @BindView(R.id.news_title)
    TextView news_title_tv;
    @BindView(R.id.date_et)
    TextView news_date_tv;
    @BindView(R.id.detail_content)
    TextView news_body_tv;
    @BindView(R.id.news_image)
    SmartImageView news_image;
    @BindView(R.id.comments_lv)
    ListView comments_lv;
    @BindView(R.id.commentEdit_et)
    EditText commentEdit_et;
    @BindView(R.id.commentSend_et)
    ImageView commentSend_et;
    List<Comments> commentsList = new ArrayList<>();
    @BindView(R.id.love_zan) ImageView love_zan;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_details_layout);
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
        Passage passage = (Passage) intent.getSerializableExtra("passage");
        news_title_tv.setText(passage.getTitle());
        news_date_tv.setText(passage.getHistoryDate().toString());
        String temp = passage.getContent().replace("\\n", "\n").replace("\\t", "\t");
        news_body_tv.setText(temp);
        news_image.setBackgroundResource(0);
        news_image.setImageUrl(passage.getPicture());

        MyApp staticAll = ((MyApp) getApplicationContext());
        staticAll.setPassageId(passage.getPassageId());

        String path = "http://www.usthe.com:8080/today_history/CommentServlet";
        AsyncHttpClient ahc = new AsyncHttpClient();
        RequestParams rp = new RequestParams();
        rp.put("passageId", staticAll.getPassageId());
        ahc.get(path, rp, ahth);

    }

    AsyncHttpResponseHandler ahth = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            if (statusCode == 200) {

                String jsonString = new String(responseBody);
                try {
                    jsonString = URLDecoder.decode(jsonString, "utf-8");
                    commentsList = JSON.parseArray(jsonString, Comments.class);
                    Log.e("TEST", "commentList " + commentsList.size());
                    CommentsAdapter mAdapter = new CommentsAdapter(commentsList, NewsDetailsActivity.this);
                    comments_lv.setAdapter(mAdapter);
                    setListViewHeightBasedOnChildren(comments_lv);
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

    public void setListViewHeightBasedOnChildren(ListView listview) {
        ListAdapter listAdapter = listview.getAdapter();
        if (listAdapter == null) {
            return;

        }
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目   
            View listItem = listAdapter.getView(i, null, listview);
// 计算子项View 的宽高   
            listItem.measure(0, 0);
// 统计所有子项的总高度   
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listview.getLayoutParams();
        params.height = totalHeight + (listview.getDividerHeight() * (listAdapter.getCount() - 1));
// listView.getDividerHeight()获取子项间分隔符占用的高度   
// params.height最后得到整个ListView完整显示需要的高度   
        listview.setLayoutParams(params);

    }


    @OnClick(R.id.return_et)
    public void back_et() {
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.love_zan)
    public void send_love()
    {
        MyApp staticAll = ((MyApp) getApplicationContext());
        if (staticAll.isThere()) {
            love_zan.setBackgroundResource(R.mipmap.zan_liang);
            LikeTimes like = new LikeTimes();
            like.setPassageId(staticAll.getPassageId());
            like.setUserId(staticAll.getUserId());
            like.setStatusCode(staticAll.isThere());
            String jsonString = JSON.toJSONString(like);
            String path2 = "http://www.usthe.com:8080/today_history/InLoveServlet";
            AsyncHttpClient ahc3 = new AsyncHttpClient();
            RequestParams rp3 = new RequestParams();
            try {
                rp3.put("love", URLEncoder.encode(jsonString, "utf-8"));
                ahc3.get(path2, rp3, ahth3);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        } else {
            Toast.makeText(NewsDetailsActivity.this, "请登录或注册！", Toast.LENGTH_SHORT).show();
        }
    }
    AsyncHttpResponseHandler ahth3 = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            if (statusCode == 200) {

                String jsonString = new String(responseBody);
                try {
                    jsonString = URLDecoder.decode(jsonString, "utf-8");
                    Result result = JSON.parseObject(jsonString, Result.class);
                    Toast.makeText(NewsDetailsActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        }
    };

    @OnClick(R.id.commentSend_et)
    public void send_comment() {
        MyApp staticAll = ((MyApp) getApplicationContext());
        if (staticAll.isThere()) {
            String comment_content = commentEdit_et.getText().toString();
            Date date = new Date();
            Comments comment = new Comments();
            comment.setContent(comment_content);
            comment.setCommentDate(date);
            comment.setPassageId(staticAll.getPassageId());
            comment.setReviewer(staticAll.getUserId());

            String jsonString = JSON.toJSONString(comment);
            String path2 = "http://www.usthe.com:8080/today_history/InCommentServlet";
            AsyncHttpClient ahc2 = new AsyncHttpClient();
            RequestParams rp2 = new RequestParams();
            try {
                rp2.put("comment", URLEncoder.encode(jsonString, "utf-8"));
                ahc2.get(path2, rp2, ahth2);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }


        } else {
            Toast.makeText(NewsDetailsActivity.this, "请登录或注册！", Toast.LENGTH_SHORT).show();
        }

    }

    AsyncHttpResponseHandler ahth2 = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            Log.e("TEXT", "incomment onSuccess: " + statusCode);
            if (statusCode == 200) {

                String jsonString = new String(responseBody);
                try {
                    jsonString = URLDecoder.decode(jsonString, "utf-8");
                    Result result = JSON.parseObject(jsonString, Result.class);
                    Toast.makeText(NewsDetailsActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
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
}
