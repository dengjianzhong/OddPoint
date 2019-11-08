package com.zhong.utilslibrary.weight;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ViewFlipper;

import java.util.List;

/**
 * 上下滚动自定义的View
 */
public class TextViewVerticalSlideMore extends ViewFlipper {

    private Context mContext;
    private boolean isSetAnimDuration = false;
    private int interval = 5000;
    private OnItemClickListener onItemClickListener;

    /**
     * 动画时间
     */
    private int animDuration = 500;

    public TextViewVerticalSlideMore(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        this.mContext = context;
        setFlipInterval(interval);

        setInAnimation(slideAnimIn());
        setOutAnimation(slideAnimOut());
    }

    private static AnimationSet slideAnimIn() {
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.setDuration(300);
        //平移
        TranslateAnimation translateAnimation = new TranslateAnimation(0F, 0F, 100F, 0F);
        animationSet.addAnimation(translateAnimation);
        //透明度
        AlphaAnimation alphaAnimation = new AlphaAnimation(0F, 1F);
        animationSet.addAnimation(alphaAnimation);
        return animationSet;
    }

    private static AnimationSet slideAnimOut() {
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.setDuration(300);
        //平移
        TranslateAnimation translateAnimation = new TranslateAnimation(0F, 0F, 0F, -100F);
        animationSet.addAnimation(translateAnimation);
        //透明度
        AlphaAnimation alphaAnimation = new AlphaAnimation(1F, 0F);
        animationSet.addAnimation(alphaAnimation);
        return animationSet;
    }

    /**
     * 设置循环滚动的View数组
     *
     * @param views
     */
    public void setViews(final List<View> views) {
        if (views == null || views.size() == 0) {
            return;
        }
        removeAllViews();
        for (int i = 0; i < views.size(); i++) {
            final int position = i;
            //设置监听回调
            views.get(i).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(position, views.get(position));
                    }
                }
            });
            addView(views.get(i));
        }
        startFlipping();
    }

    /**
     * 设置监听接口
     *
     * @param onItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * item_view的接口
     */
    public interface OnItemClickListener {
        void onItemClick(int position, View view);
    }
}
