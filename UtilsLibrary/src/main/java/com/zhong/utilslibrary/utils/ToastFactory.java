package com.zhong.utilslibrary.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 自定义Toast
 */
public class ToastFactory extends Toast {
    private Context context;
    private int padding;

    public ToastFactory(Context context) {
        super(context);
        this.context = context;
        initParams();
    }

    private void initParams() {
        padding = dip2px(context, 13);
        this.setDuration(Toast.LENGTH_SHORT);
        this.setGravity(Gravity.BOTTOM, 0, 260);
    }

    /**
     * Show view.
     *
     * @param text 显示提示文本
     */
    public void showView(String text) {
        if (!TextUtils.isEmpty(text)) {
            TextView tipView = getTipView();
            tipView.setText(text);
            this.setView(tipView);
            this.show();
        }
    }

    private TextView getTipView() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, dip2px(context, 20));
        TextView tipView = new TextView(context);
        tipView.setLayoutParams(layoutParams);
        tipView.setTextColor(Color.WHITE);
        tipView.setPadding(padding, padding, padding, padding);

        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setCornerRadius(13);
        gradientDrawable.setColor(Color.BLACK);

        tipView.setBackground(gradientDrawable);
        tipView.getBackground().setAlpha(170);
        return tipView;
    }

    private int dip2px(Context context, float dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
