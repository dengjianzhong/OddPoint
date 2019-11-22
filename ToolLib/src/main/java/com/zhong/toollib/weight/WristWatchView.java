package com.zhong.toollib.weight;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 钟表View
 */
public class WristWatchView extends View {

    private Paint paint;
    private Paint progressPaint;
    private float textSize = 35F;
    private int shapeColor1 = 0XFF000000;
    private int hourColor = 0XFFFF9800;
    private float hourlength = 52F;//小时指针刻度线长度
    private float minutelength = 26F;//分钟指针刻度线长度
    private float DEGREES = 360F;
    private float hourWidth = 4;
    private float minuteWidth = 2;
    private float hourMargin = 20F;
    private int width;
    private int height;
    private Timer timer;
    private SimpleDateFormat dateFormat;
    private int day;
    private float hour;
    private float minute;
    private float second;

    public WristWatchView(Context context) {
        super(context);
        initParams();
    }

    public WristWatchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initParams();
    }

    public WristWatchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initParams();
    }

    protected void initParams() {
        //文本画笔
        paint = new Paint();
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(shapeColor1);
        paint.setTextSize(textSize);
        paint.setStrokeWidth(2);


        //刻度线画笔
        progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressPaint.setAntiAlias(true);
        progressPaint.setColor(shapeColor1);
        progressPaint.setStrokeWidth(5);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setDither(true);


        dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        startTimer();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        width = getMeasuredWidth();
        height = getMeasuredHeight();

        drawCircle(canvas);
        drawCalibration(canvas);
        drawPointer(canvas);
    }


    private void startTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Calendar calendar = Calendar.getInstance();
                float milliSecond = calendar.get(Calendar.MILLISECOND);
                second = calendar.get(Calendar.SECOND) + milliSecond / 1000;// 精确到小数点后 保证圆滑
                minute = calendar.get(Calendar.MINUTE) + second / 60;
                hour = calendar.get(Calendar.HOUR) + minute / 60;
                day = calendar.get(Calendar.DATE);
                postInvalidate();
            }
        }, 0, 1000);
    }

    /**
     * 绘制表盘
     *
     * @param canvas
     */
    private void drawCircle(Canvas canvas) {
        progressPaint.setColor(shapeColor1);
        progressPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(width / 2, height / 2, width / 3, progressPaint);

        paint.setTextSize(textSize);
        paint.setColor(Color.WHITE);
        canvas.drawText("CARTIER", width / 2, height / 2 - width / 3 + 200, paint);
    }

    /**
     * 绘制表盘刻度
     *
     * @param canvas
     */
    private void drawCalibration(Canvas canvas) {
        float mLineLength;
        for (int i = 0; i <= DEGREES; i++) {
            if (i % 30 == 0) {//每一刻钟的旋转角度为30°,5个刻度为一刻钟
                progressPaint.setColor(hourColor);
                progressPaint.setStrokeWidth(hourWidth);
                mLineLength = hourlength;
            } else {
                progressPaint.setColor(Color.WHITE);
                progressPaint.setStrokeWidth(minuteWidth);
                mLineLength = minutelength;
            }
            if (i % 6 == 0) {//一个刻度两分钟的旋转角度为6°，
                canvas.save();
                canvas.rotate(i, width / 2, height / 2);
                canvas.drawLine(width / 2, height / 2 - width / 3, width / 2, height / 2 - width / 3 + mLineLength, progressPaint);
                canvas.restore();
            }

            paint.setTextSize(textSize);
            if ((float) i / 30 == 0F) {
                canvas.drawText("12", width / 2, height / 2 - width / 3 + mLineLength + 36F, paint);
            } else if ((float) i / 30 == 3F) {
                Paint.FontMetrics fontMetrics = paint.getFontMetrics();
                float baseLine = height / 2 - (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.top;
                canvas.drawText("3", width / 2 + width / 3 - mLineLength - hourMargin, baseLine, paint);
            } else if ((float) i / 30 == 6F) {
                canvas.drawText("6", width / 2, height / 2 + width / 3 - mLineLength - hourMargin, paint);
            } else if ((float) i / 30 == 9F) {
                Paint.FontMetrics fontMetrics = paint.getFontMetrics();
                float baseLine = height / 2 - (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.top;
                canvas.drawText("9", width / 2 - width / 3 + mLineLength + hourMargin, baseLine, paint);
            }
        }
    }

    /**
     * 绘制表盘指针及其时期
     *
     * @param canvas
     */
    private void drawPointer(Canvas canvas) {
        // TODO: 2019-11-20  秒针
        paint.setColor(hourColor);
        canvas.save();
        canvas.rotate(second / 60 * 360, width / 2, height / 2);
        canvas.drawLine(width / 2, height / 2, width / 2, height / 2 - width / 4, paint);
        canvas.restore();

        // TODO: 2019-11-20  分针
        canvas.save();
        canvas.rotate(minute / 60 * 360, width / 2, height / 2);
        canvas.drawLine(width / 2, height / 2, width / 2, height / 2 - width / 6, paint);
        canvas.restore();

        // TODO: 2019-11-20  时针
        canvas.save();
        canvas.rotate(hour / 12 * 360, width / 2, height / 2);
        canvas.drawLine(width / 2, height / 2, width / 2, height / 2 - width / 8, paint);
        canvas.restore();

        // TODO: 2019-11-20 绘制日期
        progressPaint.setColor(Color.WHITE);
        RectF rectF = new RectF(width / 2 - 30, height / 2 + width / 3 - 180, width / 2 + 30, height / 2 + width / 3 - 140);
        canvas.drawRoundRect(rectF, 6F, 6F, progressPaint);

        paint.setColor(shapeColor1);
        paint.setTextSize(27F);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float baseLine = (rectF.bottom + rectF.top) / 2 - (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.top;
        canvas.drawText(String.valueOf(day), (rectF.right + rectF.left) / 2, baseLine, paint);//绘制日期
    }

}
