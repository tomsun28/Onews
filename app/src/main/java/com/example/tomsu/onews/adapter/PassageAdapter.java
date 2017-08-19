package com.example.tomsu.onews.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.tomsu.onews.R;
import com.example.tomsu.onews.control.Passage;
import com.loopj.android.image.SmartImageView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by 巩超 on 2016/8/2.
 */
public class PassageAdapter   extends BaseAdapter {

    Context context;
    List<Passage> passageList = new ArrayList<>();
    public PassageAdapter(List list, Context context){
        this.passageList = list;
        this.context = context;
    }
    @Override
    public int getCount() {
        return passageList.size();
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

        Passage passage = passageList.get(i);


        titleTv.setText(passage.getTitle());
        loveTv.setText(Integer.toString(passage.getLikeTimes()));
        dateTv.setText(passage.getHistoryDate().toString());
        siv.setBackgroundResource(0);
        siv.setImageUrl(passage.getPicture());
        return v;
    }
}
