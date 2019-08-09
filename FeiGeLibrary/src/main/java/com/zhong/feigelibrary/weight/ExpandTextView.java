package com.zhong.feigelibrary.weight;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * 展开/收藏View
 */
public class ExpandTextView extends RelativeLayout {

    private TextView mText;

    private TextView mExpandText;

    private int mTextColor = Color.GRAY;

    private int mTextLine = 1;

    private int mStart;

    private int mEnd;

    private boolean isFirst = true;

    private boolean isExpand = false;

    private TextView expandText;

    private TextView lineText;

    public ExpandTextView(Context context) {
        this(context, null);
    }

    public ExpandTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final ViewGroup.LayoutParams params = getLayoutParams();
        if (isFirst) {
            isFirst = false;
            mText.post(new Runnable() {
                @Override
                public void run() {
                    mStart = mText.getLineHeight() * mText.getLineCount();
                    params.height = mStart;
                    setLayoutParams(params);
                }
            });
            mExpandText.post(new Runnable() {
                @Override
                public void run() {
                    mEnd = mExpandText.getLineHeight() * mExpandText.getLineCount();
                }
            });
        }
        int lineCount = mText.getLineCount();
        int length = (int) mText.getPaint().measureText(mText.getText().toString());//文字的长度
        if (lineCount <= mTextLine && length / mTextLine < mText.getWidth()) {//小于3行又未满下一行
            if ( ExpandTextView.this.expandText!=null) {
                ExpandTextView.this.expandText.setVisibility(GONE);
            }
        }
    }

    public ExpandTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        mText = new TextView(context, attrs);
        lineText = new TextView(context);
        mText.setTextColor(mTextColor);
        mText.setEllipsize(TextUtils.TruncateAt.END);
        mText.setMaxLines(mTextLine);
        addView(mText, params);
        mExpandText = new TextView(context);
        mExpandText.setTextColor(Color.TRANSPARENT);
        addView(mExpandText, params);
    }

    public void setText(String text) {
        isFirst = true;
        mText.setText(text);
        mExpandText.setText(text);
        lineText.setText(text);
        requestLayout();
    }

    public void setTextColor(int color) {
        mTextColor = color;
        mText.setTextColor(color);
    }

    public void setTextSize(int size) {
        isFirst = true;
        mText.setTextSize(size);
        mExpandText.setTextSize(size);
        requestLayout();
    }

    public void setTextMaxLine(int num) {
        mTextLine = num;
        mText.setMaxLines(num);
    }

    public void setGravity(int gravity) {
        mText.setGravity(gravity);
        mExpandText.setGravity(gravity);
    }

    public void setEllipsize(TextUtils.TruncateAt ell) {
        mText.setEllipsize(ell);
    }

    public void setTextLineSpacingExtra(float spac) {
        mText.setLineSpacing(spac, 1.0f);
        mExpandText.setLineSpacing(spac, 1.0f);
    }

    public TextView text() {
        return mText;
    }

    public TextView expandText() {
        return mExpandText;
    }

    public int line() {
        return mTextLine;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void expand() {
        if (!isExpand) {
            isExpand = true;
            mText.setTextColor(Color.TRANSPARENT);
            mExpandText.setTextColor(mTextColor);
            Animation animation = new Animation() {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    ViewGroup.LayoutParams params = ExpandTextView.this.getLayoutParams();
                    params.height = mStart + (int) ((mEnd - mStart) * interpolatedTime);
                    setLayoutParams(params);
                }
            };
            animation.setDuration(500);
            startAnimation(animation);
        }

    }

    @Override
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        super.setLayoutParams(params);
    }

    public void shrink() {
        if (isExpand) {
            isExpand = false;
            Animation animation = new Animation() {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    ViewGroup.LayoutParams params = ExpandTextView.this.getLayoutParams();
                    params.height = mStart + (int) ((mEnd - mStart) * (1 - interpolatedTime));
                    setLayoutParams(params);
                }
            };
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mText.setTextColor(mTextColor);
                    mExpandText.setTextColor(Color.TRANSPARENT);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            animation.setDuration(500);
            startAnimation(animation);
        }
    }

    public void switchs() {
        if (isExpand) {
            shrink();
        } else {
            expand();
        }
    }

    public void getLineNum(TextView expandText) {
        this.expandText = expandText;
        lineText.post(new Runnable() {
            @Override
            public void run() {
                int LineCount = lineText.getLineCount();
                if (LineCount <= 3) {
                    ExpandTextView.this.expandText.setVisibility(GONE);
                }
            }
        });
    }
}
