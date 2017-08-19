package com.example.tomsu.onews.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.tomsu.onews.R;
import com.example.tomsu.onews.control.News;
import com.example.tomsu.onews.control.Passage;
import com.loopj.android.image.SmartImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tomsu on 2017/5/15.
 */

public class NewsAdapter extends BaseAdapter{
    Context context;
    List<News> newsList = new ArrayList<>();
    public NewsAdapter(List list, Context context){
        this.newsList = list;
        this.context = context;
    }
    @Override
    public int getCount() {
        return newsList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v;
        if(view != null){
            v = view;
        }else{
            v = View.inflate(context, R.layout.news_item_layout, null);
        }

        SmartImageView siv = (SmartImageView) v.findViewById(R.id.image_et);
        TextView titleTv = (TextView) v.findViewById(R.id.title_et);
        TextView loveTv = (TextView) v.findViewById(R.id.love_number_et);
        TextView dateTv = (TextView) v.findViewById(R.id.date_et);

        News news = newsList.get(i);


        titleTv.setText(news.getTitle());
        //loveTv.setText(Integer.toString(news.getLikeTimes()));
        dateTv.setText(news.getThisDate().toString());
        siv.setBackgroundResource(0);
        siv.setImageUrl(news.getPicture());
        return v;
    }
}
