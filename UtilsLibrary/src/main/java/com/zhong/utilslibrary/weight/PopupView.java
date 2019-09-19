package com.zhong.utilslibrary.weight;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 引导视图  注意：PopupWindow的标准宽高 width=dip2px(context,260) ,height=dip2px(context,160)
 */
//TODO tipTextView的计算公式：((height/3)*2+5)-32
public class PopupView extends RelativeLayout {
    private LayoutParams layoutParams;
    private LinearLayout.LayoutParams params;
    private float textSize = 16F;
    private Context context;
    private OnSelectListener onSelectListener;
    private int height;
    private LinearLayout bottomLinearView;
    private AnimationSet animationSet;

    /**
     * Instantiates a new Guide view.
     *
     * @param context          the context
     * @param onSelectListener the on select listener
     * @param height           这里的height指的是PopupWindow的高  ，注意一定要传所指定的PopupWindow的高  否则View定位会出现无法预料的结果 Todo
     */
    public PopupView(Context context, OnSelectListener onSelectListener, int height) {
        super(context);
        this.context = context;
        this.onSelectListener = onSelectListener;
        this.height = height;
    }

    @SuppressLint("ResourceType")
    public void initView(String hintMsg, String... btnText) {

        int margin = dip2px(context, 16);
        //----------------根布局----------------
        layoutParams = new LayoutParams(-2, height);
        this.setLayoutParams(layoutParams);
        this.setAnimation(getShowAnimation());

        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setCornerRadius(23);
        gradientDrawable.setColor(Color.WHITE);

        this.setBackground(gradientDrawable);


        //----------------提示文本----------------
        int tipTextHeight = ((height / 3) * 2) - dip2px(context, 32);
        layoutParams = new LayoutParams(-1, tipTextHeight);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP | RelativeLayout.CENTER_HORIZONTAL);
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
        this.addView(tipTextView);


        //----------------底部根视图----------------
        layoutParams = new LayoutParams(-1, -1);
        layoutParams.addRule(RelativeLayout.BELOW, tipTextView.getId());
        RelativeLayout bottomView = new RelativeLayout(context);
        bottomView.setLayoutParams(layoutParams);
        this.addView(bottomView);

        //----------------横向分割线 ----------------
        layoutParams = new LayoutParams(-1, 1);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        View splitViewH = new View(context);
        splitViewH.setId(0x19900000);
        splitViewH.setLayoutParams(layoutParams);
        splitViewH.setBackgroundColor(Color.parseColor("#D5D5D5"));
        bottomView.addView(splitViewH);

        layoutParams = new LayoutParams(-1, -1);
        layoutParams.addRule(RelativeLayout.BELOW, splitViewH.getId());
        bottomLinearView = new LinearLayout(context);
        bottomLinearView.setLayoutParams(layoutParams);
        bottomLinearView.setOrientation(LinearLayout.HORIZONTAL);
        bottomView.addView(bottomLinearView);


        if (btnText != null && btnText.length > 0) {
            for (int i = 0; i < btnText.length; i++) {
                params = new LinearLayout.LayoutParams(-2, -1);
                params.weight = 1;
                TextView btnView = new TextView(context);
                btnView.setTag(i);
                btnView.setLayoutParams(params);
                btnView.setGravity(Gravity.CENTER);
                btnView.setText(btnText[i]);
                btnView.setTextSize(textSize);
//                btnView.setTypeface(Typeface.DEFAULT_BOLD);
//                btnView.setTextColor(Color.parseColor("#00b839"));
                btnView.setClickable(true);
                btnView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (null == view) {
                            return;
                        }
                        Integer integer = (Integer) view.getTag();
                        if (null != integer) {
                            int tag = integer.intValue();
                            if (null != onSelectListener) {
                                onSelectListener.onPopClick(tag);
                            }
                        }
                    }
                });
                bottomLinearView.addView(btnView);

                //----------------竖向分割线 ----------------
                if (i != btnText.length - 1) {
                    params = new LinearLayout.LayoutParams(1, -1);
                    View splitView = new View(context);
                    splitView.setLayoutParams(params);
                    splitView.setBackgroundColor(Color.parseColor("#D5D5D5"));
                    bottomLinearView.addView(splitView);
                }
            }
        }
    }

    /**
     * 设置btn样式
     *
     * @param index     位于ViewGroup的下标
     * @param textColor the text color
     * @param typeface  字体风格 例如：加粗
     */
    public void setTextStyleByIndex(int index, int textColor, Typeface typeface) {
        if (bottomLinearView != null) {
            TextView btnView;
            if (index == 0) {
                btnView = (TextView) bottomLinearView.getChildAt(index);
            } else {
                btnView = (TextView) bottomLinearView.getChildAt(index / 2 == 0 ? index + 1 : index);
            }
            btnView.setTextColor(textColor);
            btnView.setTypeface(typeface);
        }
    }

    public AnimationSet getShowAnimation() {
        if (animationSet == null) {
            animationSet = new AnimationSet(true);
            animationSet.setDuration(200);
            AlphaAnimation alphaAnimation = new AlphaAnimation(0.0F, 1.0F);
            animationSet.addAnimation(alphaAnimation);
        }
        return animationSet;
    }

    private static int dip2px(Context context, float dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }


    public interface OnSelectListener {
        void onPopClick(int tag);
    }

    public void recycle() {
        onSelectListener = null;
        animationSet=null;
    }

}
