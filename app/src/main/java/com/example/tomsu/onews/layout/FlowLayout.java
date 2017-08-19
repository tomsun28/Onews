package com.example.tomsu.onews.layout;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tomsu on 2017/5/15.
 */


import android.content.Context;
import android.util.AttributeSet;

import android.view.ViewGroup;
public class FlowLayout extends ViewGroup {
    /** 横向间隔 */
    private int mHSpac = 0;
    /** 纵向间隔 */
    private int mVSpac = 0;
    /** 当前行已用的宽度*/
    private int mHaveUsedWidth = 0;
    /** 每一行的集合 */
    private final List<TagLine> mTagLines = new ArrayList<TagLine>();
    private TagLine mTagLine = null;
    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setHorizontalSpacing(dip2px(5));
        setVerticalSpacing(dip2px(5));
    }
    public int dip2px(float dip) {
        float density = this.getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f);
    }
    public void setHorizontalSpacing(int spacing) {
        if (mHSpac != spacing) {
            mHSpac = spacing;
            requestLayout();
        }
    }

    public void setVerticalSpacing(int spacing) {
        if (mVSpac != spacing) {
            mVSpac = spacing;
            requestLayout();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int availableWidth  = MeasureSpec.getSize(widthMeasureSpec)
                - getPaddingRight() - getPaddingLeft();
        int availableHeight = MeasureSpec.getSize(heightMeasureSpec)
                - getPaddingTop() - getPaddingBottom();
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        resetLine();// 将行的状态重置为最原始的状态，因为新的一行的数据跟以往的无关
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);
            int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(availableWidth ,
                    widthMode == MeasureSpec.EXACTLY ? MeasureSpec.AT_MOST
                            : widthMode);
            int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                    availableHeight,
                    heightMode == MeasureSpec.EXACTLY ? MeasureSpec.AT_MOST
                            : heightMode);
            // 测量子控件
            child.measure(childWidthMeasureSpec, childHeightMeasureSpec);

            if (mTagLine == null) {
                mTagLine = new TagLine();
            }
            int childWidth = child.getMeasuredWidth();
            mHaveUsedWidth += childWidth;// 增加使用的宽度
            if (mHaveUsedWidth <= availableWidth ) {// 已经使用的宽度小于可用宽度，说明还有剩余空间，该子控件添加到这一行。
                mTagLine.addView(child);// 添加子控件
                mHaveUsedWidth += mHSpac;// 加上间距
                if (mHaveUsedWidth >= availableWidth ) {// 加上间距后已经使用的宽度大于等于可用宽度，说明这一行已满或者已经超出需要换行
                    addLine();
                }
            } else {
                //说明上一行已经被子控件占满，而下个子控件刚好是新的一行的第一个子控件
                if (mTagLine.getViewCount() == 0) {
                    mTagLine.addView(child);
                    addLine();
                } else {
                    //因为这个子控件的长度大于屏幕的宽度，在任何一行上面只要有子控件，不论子控件的长度多小，都需药换行
                    addLine();
                    mTagLine.addView(child);
                    mHaveUsedWidth += childWidth + mHSpac;
                }
            }
        }

        if (mTagLine != null && mTagLine.getViewCount() > 0
                && !mTagLines.contains(mTagLine)) {
            //此段代码的作用是为了防止因最后一行代码的子控件未占满空间，但是毕竟也是一行，所以也要添加到行的集合里面
            mTagLines.add(mTagLine);
        }

        int totalWidth = MeasureSpec.getSize(widthMeasureSpec);
        int totalHeight = 0;
        final int size = mTagLines.size();
        for (int i = 0; i < size; i++) {// 加上所有行的高度
            totalHeight += mTagLines.get(i).mChildHeight;
        }
        totalHeight += mVSpac * (size - 1);// 加上所有间距的高度
        totalHeight += getPaddingTop() + getPaddingBottom();// 加上padding
        setMeasuredDimension(totalWidth,
                resolveSize(totalHeight, heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int left = getPaddingLeft();// 获取最初的左上点
        int top = getPaddingTop();
        final int linesCount = mTagLines.size();
        for (int i = 0; i < linesCount; i++) {
            final TagLine oneLine = mTagLines.get(i);
            oneLine.layoutView(left, top);// 设置每一行所在的位置
            top += oneLine.mChildHeight + mVSpac;// 这个top的值其实就是下一个的上顶点值
        }
    }

    /** 将行的状态重置为最原始的状态*/
    private void resetLine() {
        mTagLines.clear();
        mTagLine = new TagLine();
        mHaveUsedWidth = 0;
    }

    /** 新增加一行 */
    private void addLine() {
        mTagLines.add(mTagLine);
        mTagLine = new TagLine();
        mHaveUsedWidth = 0;
    }
    /**
     * 代表着一行，封装了一行所占高度，该行子View的集合，以及所有View的宽度总和
     */
    private class TagLine {
        int mAllChildWidth = 0;// 该行中所有的子控件加起来的宽度
        int mChildHeight = 0;// 子控件的高度
        List<View> viewList= new ArrayList<View>();
        public void addView(View view) {// 添加子控件
            viewList.add(view);
            mAllChildWidth += view.getMeasuredWidth();
            int childHeight = view.getMeasuredHeight();
            mChildHeight = childHeight;// 行的高度当然是有子控件的高度决定了
        }
        public int getViewCount() {
            return viewList.size();
        }

        public void layoutView(int left, int top) {
            int childCount = getViewCount();
            //除去左右边距后可以使用的宽度
            int validWidth= getMeasuredWidth() - getPaddingLeft()
                    - getPaddingRight();
            // 除了子控件以及子控件之间的间距后剩余的空间
            int remainWidth = validWidth- mAllChildWidth - mHSpac
                    * (childCount - 1);
            if (remainWidth >= 0) {
                int divideSpac = (int) (remainWidth / childCount + 0.5);
                for (int i = 0; i < childCount; i++) {
                    final View view = viewList.get(i);
                    int childWidth = view.getMeasuredWidth();
                    int childHeight = view.getMeasuredHeight();
                    // 把剩余的空间平均分配到每个子控件上面
                    childWidth = childWidth + divideSpac;
                    view.getLayoutParams().width = childWidth;
                    // 由于平均分配剩余空间导致子控件的长度发生了变化，需要重新测量
                    int widthMeasureSpec = MeasureSpec.makeMeasureSpec(
                            childWidth, MeasureSpec.EXACTLY);
                    int heightMeasureSpec = MeasureSpec.makeMeasureSpec(
                            childHeight, MeasureSpec.EXACTLY);
                    view.measure(widthMeasureSpec, heightMeasureSpec);
                    // 设置子控件的位置
                    view.layout(left, top, left + childWidth, top
                            + childHeight);
                    left += childWidth + mHSpac; // 获取到的left值是下一个子控件的左边所在的位置
                }
            } else {
                if (childCount == 1) {//这一种就是一行只有一个子控件的情况
                    View view = viewList.get(0);
                    view.layout(left, top, left + view.getMeasuredWidth(), top
                            + view.getMeasuredHeight());
                }
            }
        }
    }

}