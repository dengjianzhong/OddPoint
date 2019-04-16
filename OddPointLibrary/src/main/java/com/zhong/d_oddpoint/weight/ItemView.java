package com.zhong.d_oddpoint.weight;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class ItemView extends LinearLayout {
    private int match = LayoutParams.MATCH_PARENT;
    private int wrap = LayoutParams.WRAP_CONTENT;
    private int match_height = match;
    private int match_width = match;
    private int wrap_width = wrap;
    private int wrap_height = wrap;
    private LayoutParams globle_layout_params01;
    private LayoutParams globle_layout_params02;
    private LinearLayout main_layout;
    private LinearLayout layout01_1;
    private LinearLayout layout01_2;
    private LayoutParams globle_layout_params03;
    private LinearLayout layout01;
    private LayoutParams layoutParams_left;
    private LayoutParams layoutParams_right;
    private LinearLayout body_layout;


    public ItemView(Context context) {
        super(context);
        initLayout();
    }

    public void initLayout() {

        globle_layout_params01 = new LayoutParams(match_width, match_height);
        globle_layout_params02 = new LayoutParams(match_width, wrap_height);
        globle_layout_params03 = new LayoutParams(wrap_width, wrap_height);

        LayoutParams layoutParams1 = new LayoutParams(wrap_width, wrap_height);
        layoutParams1.setMargins(0,0,0,dp2px(6));
        LayoutParams layoutParams2 = new LayoutParams(wrap_width, wrap_height);
        layoutParams_left = new LayoutParams(wrap_width, wrap_height);
        layoutParams_left.weight=1;
        layoutParams_right = new LayoutParams(wrap_width, wrap_height);
        layoutParams_right.weight=1;


        body_layout = new LinearLayout(getContext());
        body_layout.setOrientation(LinearLayout.VERTICAL);
        body_layout.setLayoutParams(globle_layout_params02);
        //一级
        main_layout = new LinearLayout(getContext());
        main_layout.setOrientation(LinearLayout.HORIZONTAL);
        main_layout.setLayoutParams(globle_layout_params01);
        main_layout.setGravity(Gravity.CENTER_VERTICAL);
        main_layout.setPadding(dp2px(9), dp2px(9), dp2px(9), dp2px(9));

        //二级
        layout01 = new LinearLayout(getContext());
        layout01.setOrientation(LinearLayout.VERTICAL);
        layout01.setLayoutParams(globle_layout_params02);
        layout01.setPadding(dp2px(9),0,0,0);

        //二级-1
        layout01_1 = new LinearLayout(getContext());
        layout01_1.setOrientation(LinearLayout.HORIZONTAL);
        layout01_1.setLayoutParams(globle_layout_params02);

        //二级-2
        layout01_2 = new LinearLayout(getContext());
        layout01_2.setOrientation(LinearLayout.HORIZONTAL);
        layout01_2.setLayoutParams(globle_layout_params02);
    }

    public ImageView addImageView() {
        LayoutParams layoutParams = new LayoutParams(dp2px(38), dp2px(38));
        ImageView imageView = new ImageView(getContext());
        imageView.setLayoutParams(layoutParams);
        main_layout.addView(imageView);
        return imageView;
    }

    public TextView addLeftView1() {
        TextView textView = new TextView(getContext());
        textView.setLayoutParams(layoutParams_left);
        textView.setGravity(Gravity.LEFT);
        textView.setTextSize(17f);
        textView.setTextColor(Color.parseColor("#1c8eff"));
        layout01_1.addView(textView);
        return textView;
    }

    public TextView addRightText1() {
        TextView textView = new TextView(getContext());
        textView.setLayoutParams(layoutParams_right);
        textView.setGravity(Gravity.RIGHT);
        textView.setTextSize(13f);
        layout01_1.addView(textView);
        return textView;
    }

    public TextView addLeftText2() {
        TextView textView = new TextView(getContext());
        textView.setLayoutParams(layoutParams_left);
        textView.setGravity(Gravity.LEFT);
        textView.setTextSize(13f);
        layout01_2.addView(textView);
        return textView;
    }

    public TextView addRightText2() {
        TextView textView = new TextView(getContext());
        textView.setLayoutParams(layoutParams_right);
        textView.setGravity(Gravity.RIGHT);
        textView.setTextSize(13f);
        layout01_2.addView(textView);
        return textView;
    }

    public void addLine(){
        LayoutParams layoutParams = new LayoutParams(match_width, 1);
        layoutParams.setMargins(dp2px(55),dp2px(1),0,0);
        TextView textView = new TextView(getContext());
        textView.setLayoutParams(layoutParams);
        textView.setBackgroundColor(Color.parseColor("#b6b5b5"));
        body_layout.addView(textView);
    }

    public ItemView addViewLaout() {
        layout01.addView(layout01_1);
        layout01.addView(layout01_2);
        main_layout.addView(layout01);
        body_layout.addView(main_layout);
        addLine();
        addView(body_layout);
        return this;
    }

    public int dp2px(float data) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (data * scale + 0.5f);
    }
}
