package com.example.tomsu.onews;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.tomsu.onews.adapter.PassageAdapter;
import com.example.tomsu.onews.control.Passage;
import com.example.tomsu.onews.staticAll.MyApp;
import com.example.tomsu.onews.uiActivity.LoginActivity;
import com.example.tomsu.onews.uiActivity.NewsDetailsActivity;
import com.example.tomsu.onews.uiActivity.TodayNewsActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.image.SmartImageView;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    @BindView(R.id.news_lv) ListView news_lv;



    List<Passage> passageList = new ArrayList<Passage>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setLogo(R.mipmap.ic_launcher);//设置app logo
        toolbar.setTitle("昨天");//设置主标题
        //toolbar.setSubtitle("Subtitle");//设置子标题
        setSupportActionBar(toolbar);

        String path = "http://www.usthe.com:8080/today_history/StartServlet";
        AsyncHttpClient ahc = new AsyncHttpClient();
        RequestParams rp = new RequestParams();

        final Date temp = new Date();
        final SimpleDateFormat  sdf = new SimpleDateFormat("yyyy-MM-dd");
        /*
        Log.e("DATE", "onCreate:================================== "+sdf.format(temp).toString() );
        rp.put("date",sdf.format(temp).toString() );
        */
        rp.put("flag",1);
        ahc.post(path, rp, ahth);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.today);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, sdf.format(temp).toString(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        //toolbar.setNavigationIcon(R.mipmap.user1);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        MyApp staticAll = ((MyApp)getApplicationContext());
        if(staticAll.isThere()) {

            SmartImageView home_at = (SmartImageView)navigationView.getHeaderView(0). findViewById(R.id.home_at);
            home_at.setBackgroundResource(0);
            home_at.setImageUrl(staticAll.getUserPhoto());
            TextView userName_et = (TextView)navigationView.getHeaderView(0).findViewById(R.id.userName_et);
            TextView userEmail_et = (TextView)navigationView.getHeaderView(0).findViewById(R.id.userEmail_et);
            userName_et.setText(staticAll.getUserName());
            userEmail_et.setText(staticAll.getUserEmail());
        }

    }

    AsyncHttpResponseHandler ahth = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            Log.e("TEST", "onSuccess: "+statusCode );
            if (statusCode == 200) {
                String jsonString = new String(responseBody);
                try {
                    jsonString = URLDecoder.decode(jsonString, "utf-8");
                    passageList = JSON.parseArray(jsonString, Passage.class);
                    PassageAdapter mAdapter = new PassageAdapter(passageList,MainActivity.this);
                    news_lv.setAdapter(mAdapter);
                    //news_lv.setMode(PullToRefreshBase.Mode.BOTH);

                    news_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Passage passage = passageList.get(position);

                            Intent intent = new Intent();
                            //传递简单数据，使用PutExtra的方式
                            //intent.putExtra("news_title", news.getTitle());
                            //传递复杂数据，如对象，使用Bundle的方式
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("passage",  passage);
                            intent.putExtras(bundle);
                            intent.setClass(MainActivity.this, NewsDetailsActivity.class);
                            startActivity(intent);
                        }
                    });
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                // SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences("user", MODE_PRIVATE);
                // sharedPreferences.getString("")
            }
            else{
                Log.e("TAG", "oness: cdcdkckdnc---------------------------");
            }
        }
        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Log.e("TEST", "onFailuress: "+statusCode );
        }
    };


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent();
            intent.setClass(this, TodayNewsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent intent = new Intent();
            intent.setClass(this,LoginActivity.class);
            startActivity(intent);

            // Handle the camera action

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
