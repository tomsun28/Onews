package com.example.tomsu.onews.layout;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;

public class DrawableUtils {

	public static GradientDrawable getGradientDrawable(boolean isStroke,int color, int radius) {
		GradientDrawable shape = new GradientDrawable();
		shape.setShape(GradientDrawable.RECTANGLE);
		shape.setCornerRadius(radius);
		if (isStroke){
		shape.setStroke(2, Color.GRAY);
		}
		shape.setColor(color);
		return shape;
	}

	//获取状态选择器
	public static StateListDrawable getSelector(Drawable normal, Drawable press) {
		StateListDrawable selector = new StateListDrawable();
		selector.addState(new int[] { android.R.attr.state_pressed }, press);
		selector.addState(new int[] {}, normal);
		return selector;
	}
	
	//获取状态选择器
	public static StateListDrawable getSelector(boolean isStroke,int normal, int press, int radius) {
		GradientDrawable bgNormal = getGradientDrawable(isStroke,normal, radius);
		GradientDrawable bgPress = getGradientDrawable(isStroke,press, radius);
		StateListDrawable selector = getSelector(bgNormal, bgPress);
		return selector;
	}

}
