package com.zhong.feigelibrary.weight;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 引导视图  注意：PopupWindow的标准宽高 width=dip2px(context,260) ,height=dip2px(context,160)
 */
//TODO tipTextView的计算公式：((height/3)*2+5)-32
public class GuideView {
    private RelativeLayout.LayoutParams layoutParams;
    private LinearLayout.LayoutParams params;
    private float textSize = 16F;
    private Context context;
    private onSelectListener onSelectListener;
    private TextView cancelView;
    private View splitViewV;
    private TextView submitView;
    private int height;

    /**
     * Instantiates a new Guide view.
     *
     * @param context          the context
     * @param onSelectListener the on select listener
     */
    public GuideView(Context context, GuideView.onSelectListener onSelectListener, int height) {
        this.context = context;
        this.onSelectListener = onSelectListener;
        this.height=((height/3)*2+5)-32;
    }

    @SuppressLint("ResourceType")
    public View getView(String hintMsg, String cancelButtonText, String submitButtonText) {

        int margin = dip2px(context, 16);
        //----------------根布局----------------
        layoutParams = new RelativeLayout.LayoutParams(-1, -1);
        RelativeLayout relativeLayout = new RelativeLayout(context);
        relativeLayout.setLayoutParams(layoutParams);

        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setCornerRadius(23);
        gradientDrawable.setColor(Color.WHITE);

        relativeLayout.setBackground(gradientDrawable);


        //----------------提示文本----------------
        layoutParams = new RelativeLayout.LayoutParams(-1, dip2px(context,height));
        layoutParams.addRule(RelativeLayout.ABOVE | RelativeLayout.CENTER_HORIZONTAL);
        layoutParams.setMargins(margin, margin, margin, margin);
        TextView tipTextView = new TextView(context);
        tipTextView.setId(0x888888);
        tipTextView.setLayoutParams(layoutParams);
        tipTextView.setTextColor(Color.BLACK);
        tipTextView.setMaxEms(13);
        tipTextView.setMaxLines(3);
        tipTextView.setTextSize(textSize);
        tipTextView.setGravity(Gravity.CENTER);
        tipTextView.setLineSpacing(10F, 1);
        tipTextView.setText(hintMsg);
        relativeLayout.addView(tipTextView);


        //----------------底部根视图----------------
        layoutParams = new RelativeLayout.LayoutParams(-1, -1);
        layoutParams.addRule(RelativeLayout.BELOW,tipTextView.getId());
        RelativeLayout bottomView = new RelativeLayout(context);
        bottomView.setLayoutParams(layoutParams);
        relativeLayout.addView(bottomView);

        //----------------横向分割线 ----------------
        layoutParams = new RelativeLayout.LayoutParams(-1, 1);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        View splitViewH = new View(context);
        splitViewH.setId(0x19900000);
        splitViewH.setLayoutParams(layoutParams);
        splitViewH.setBackgroundColor(Color.parseColor("#D5D5D5"));
        splitViewH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectListener.onCancel();
            }
        });
        bottomView.addView(splitViewH);

        layoutParams = new RelativeLayout.LayoutParams(-1, -1);
        layoutParams.addRule(RelativeLayout.BELOW, splitViewH.getId());
        LinearLayout bottomLinearView = new LinearLayout(context);
        bottomLinearView.setLayoutParams(layoutParams);
        bottomLinearView.setOrientation(LinearLayout.HORIZONTAL);
        bottomView.addView(bottomLinearView);


        //----------------取消按钮 ----------------
        params = new LinearLayout.LayoutParams(-2, -1);
        params.weight = 1;
        cancelView = new TextView(context);
        cancelView.setLayoutParams(params);
        cancelView.setGravity(Gravity.CENTER);
        cancelView.setText(cancelButtonText);
        cancelView.setTextSize(textSize);
        cancelView.setClickable(true);
        cancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectListener.onCancel();
            }
        });
        bottomLinearView.addView(cancelView);

        //----------------竖向分割线 ----------------
        params = new LinearLayout.LayoutParams(1, -1);
        splitViewV = new View(context);
        splitViewV.setLayoutParams(params);
        splitViewV.setBackgroundColor(Color.parseColor("#D5D5D5"));
        bottomLinearView.addView(splitViewV);

        //----------------确定按钮 ----------------
        params = new LinearLayout.LayoutParams(-2, -1);
        params.weight = 1;
        submitView = new TextView(context);
        submitView.setLayoutParams(params);
        submitView.setGravity(Gravity.CENTER);
        submitView.setText(submitButtonText);
        submitView.setTextSize(textSize);
        submitView.setTypeface(Typeface.DEFAULT_BOLD);
        submitView.setTextColor(Color.parseColor("#00b839"));
        submitView.setClickable(true);
        submitView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectListener.onDetermine();
            }
        });
        bottomLinearView.addView(submitView);


        return relativeLayout;
    }

    @SuppressLint("ResourceType")
    public View getView(String hintMsg, String submitButtonText) {

        int margin = dip2px(context, 16);
        //----------------根布局----------------
        layoutParams = new RelativeLayout.LayoutParams(-1, -1);
        RelativeLayout relativeLayout = new RelativeLayout(context);
        relativeLayout.setLayoutParams(layoutParams);

        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setCornerRadius(23);
        gradientDrawable.setColor(Color.WHITE);

        relativeLayout.setBackground(gradientDrawable);


        //----------------提示文本----------------
        layoutParams = new RelativeLayout.LayoutParams(-1, dip2px(context,height));
        layoutParams.addRule(RelativeLayout.ABOVE | RelativeLayout.CENTER_HORIZONTAL);
        layoutParams.setMargins(margin, margin, margin, margin);
        TextView tipTextView = new TextView(context);
        tipTextView.setId(0x888888);
        tipTextView.setLayoutParams(layoutParams);
        tipTextView.setTextColor(Color.BLACK);
        tipTextView.setMaxEms(13);
        tipTextView.setMaxLines(3);
        tipTextView.setTextSize(textSize);
        tipTextView.setGravity(Gravity.CENTER);
        tipTextView.setLineSpacing(10F, 1);
        tipTextView.setText(hintMsg);
        relativeLayout.addView(tipTextView);


        //----------------底部根视图----------------
        layoutParams = new RelativeLayout.LayoutParams(-1, -1);
        layoutParams.addRule(RelativeLayout.BELOW,tipTextView.getId());
        RelativeLayout bottomView = new RelativeLayout(context);
        bottomView.setLayoutParams(layoutParams);
        relativeLayout.addView(bottomView);

        //----------------横向分割线 ----------------
        layoutParams = new RelativeLayout.LayoutParams(-1, 1);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        View splitViewH = new View(context);
        splitViewH.setId(0x19900000);
        splitViewH.setLayoutParams(layoutParams);
        splitViewH.setBackgroundColor(Color.parseColor("#D5D5D5"));
        splitViewH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectListener.onCancel();
            }
        });
        bottomView.addView(splitViewH);

        layoutParams = new RelativeLayout.LayoutParams(-1, -1);
        layoutParams.addRule(RelativeLayout.BELOW, splitViewH.getId());
        LinearLayout bottomLinearView = new LinearLayout(context);
        bottomLinearView.setLayoutParams(layoutParams);
        bottomLinearView.setOrientation(LinearLayout.HORIZONTAL);
        bottomView.addView(bottomLinearView);


        //----------------确定按钮 ----------------
        params = new LinearLayout.LayoutParams(-2, -1);
        params.weight = 1;
        submitView = new TextView(context);
        submitView.setLayoutParams(params);
        submitView.setGravity(Gravity.CENTER);
        submitView.setText(submitButtonText);
        submitView.setTextSize(textSize);
        submitView.setTypeface(Typeface.DEFAULT_BOLD);
        submitView.setTextColor(Color.parseColor("#00b839"));
        submitView.setClickable(true);
        submitView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectListener.onDetermine();
            }
        });
        bottomLinearView.addView(submitView);


        return relativeLayout;
    }


    private static int dip2px(Context context, float dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }


    /**
     * The interface On select listener.
     */
    public interface onSelectListener {
        /**
         * On cancel. 取消
         */
        void onCancel();

        /**
         * On determine. 确定
         */
        void onDetermine();
    }

}
