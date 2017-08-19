package com.example.tomsu.onews.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.example.tomsu.onews.R;
import com.example.tomsu.onews.control.Comments;
import com.loopj.android.image.SmartImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tomsu on 2017/4/29.
 */

public class CommentsAdapter extends BaseAdapter {

    Context context;
    List<Comments> commentsList = new ArrayList<>();

    public CommentsAdapter(List list, Context context){
        this.commentsList = list;
        this.context = context;
    }


    @Override
    public int getCount() {
        return commentsList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v;
        if(view != null){
            v = view;
        }else{
            v = View.inflate(context, R.layout.comments_item_layout, null);
        }

        SmartImageView commentUserPhoto = (SmartImageView) v.findViewById(R.id.commentUserPhoto_et);
        TextView commentUserName = (TextView) v.findViewById(R.id.commentUserName_et);
        TextView commentTime = (TextView) v.findViewById(R.id.commentTime_et);
        TextView comment = (TextView) v.findViewById(R.id.comment_et);

        Comments comments = commentsList.get(i);

        commentUserPhoto.setImageUrl(comments.getUserPhoto());
        commentUserName.setText(comments.getUserName());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd    hh:mm:ss");
        commentTime.setText(sdf.format(comments.getCommentDate()).toString());
        comment.setText(comments.getContent());

        return v;
    }
}
